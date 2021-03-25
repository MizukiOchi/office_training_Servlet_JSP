package Servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.OmikujiBean;
import Bean.ResultsBean;
import DAO.OmikujiDao;
import DAO.ResultsDao;
import List.CSVReader;

/**
 * ●Servlet/JSP間の処理実装(メインメソッド)
 * Servlet implementation class ChangeToResults
 *
 * @author m_ochi
 */
@WebServlet("/ChangeToResults")
public class ChangeToResults extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/**
		 * Servlet画面で入力した誕生日を取得する
		 */
		//HTML（サーブレット）から入力されたパラメータの値を取得
		String birthday = request.getParameter("birthday");
		request.setAttribute("birthday", birthday); //画面遷移１回で値が消滅する
		//エラー時には入力した誕生日がtext内に残す処理（一時的にgetParameterで取得した値を保存）
		HttpSession session = request.getSession();
		session.setAttribute("birthday", birthday);//画面遷移を何回しても半永久敵に値が保存できる

		/**エラーメッセージの処理*/
		//checkBirthday（入力チェックするメソッドで）対象の値が返ってきた時は、InputBirthdayクラスに戻ってエラーを画面に出す
		List<String> ErrorMessageList = checkBirthday(birthday);
		//ErrorMessageListにエラーが入っていれば、InputBirthdayに画面遷移とエラーメッセージを引き渡す
		if (ErrorMessageList != null && ErrorMessageList.size() > 0) {
			request.setAttribute("ErrorMessageList", ErrorMessageList);
			request.getRequestDispatcher("/InputBirthday").forward(request, response);
		} else {

			/**
			 * 今日の日付を取得する
			 */
			Date date = new Date();
			java.sql.Date results_date = convert(date);

			/**
			 * resultsテーブルから誕生日・本日の日付を条件にデータを取得する。
			 * ●変数rbに入っているデータのomikuji_idを取得してデータがあればそのデータを出力する。
			 * ResultsDaoのselectByBirthdayメソッドを呼び出して、取得した結果を変数rbに代入。
			 * （=条件に一致しているデータがあれば、変数rbに代入されることになる
			 * ＝その結果を出力すれば良いということになる）
			 * @return resultsBean
			 */
			//
			ResultsBean rb = ResultsDao.selectByBirthday(results_date, birthday);
			String omikuji_id = rb.getOmikuji_id();
			String path =this.getServletContext().getRealPath("/WEB-INF/fortuneTelling.csv");
			//CSVReaderで使用できるように用意する
//			String csvReader = CSVReader.readCsv(realPath);
//			ServletContext sc =request.getContextPath();
			String realPath = this.getServletContext().getRealPath("WEB-INF/fortuneTelling.csv");

			/**
			 * resultsテーブルに入力した結果がない場合→おみくじを生成する
			 * １、もしomikujiテーブルにデータがなければ、omikujiテーブルのデータを書き込むメソッドを呼ぶ
			 * ２、omikujiテーブルのレコード数(sqlのcountを使用)でrandomの数字を取得
			 * ２、⑤ー１、で出た数字をデータのomikuji_idを条件にして、SELECT文を使用してomikujiテーブル（fortuneテーブルも結合する）からデータを取得
			 * ３、resultsテーブルに⑤ー２、で取得したデータを登録する（omikuji_id, results_date, birthday）
			 * ４、コンソールに⑤−２、で取得したデータを出力する。
			 */
			if (omikuji_id == null) {

				// もしomikujiテーブルにデータがなければ、omikujiテーブルのデータを書き込むメソッド(OmikujiDao.count())を呼ぶ
				int omikujiCnt = OmikujiDao.count();
				if (omikujiCnt == 0) {

					CSVReader.readCsv(path);


					// omikujiテーブルにデータを入れた後、もう一度omikujiテーブルのデータ数を数える。
					omikujiCnt = OmikujiDao.count();
				}

				/**
				 * Randomでomikuji_idを取得する
				 */
				Random random = new Random();
				// DBの接続して、randomの引数をSQLのCountを使用して取得する
				int randomValue = random.nextInt(omikujiCnt) + 1; // メソッドが０からカウントされるため、メソッド（）＋１をする
				omikuji_id = Integer.toString(randomValue);

				/**
				 * 取得したresults_date・omikuji_id・birthdayを動的にINSERTする
				 */
				// 取得したomikuji_id・birthday・results_dateをresultsテーブルにINSERTする
				ResultsDao.insertResults(results_date, omikuji_id, birthday);
			}

			/**
			 * omikuji_idを条件にomikujiテーブルからデータを取得
			 */
			// omikuji_idを条件にomikujiテーブルからデータを取得
			OmikujiBean oi = OmikujiDao.selectByOmikuji(omikuji_id);
			//		System.out.println(oi.getFortune_name());
			//jspで画面に出力する。
			request.setAttribute("results", oi);
			request.getRequestDispatcher("/jsp/OmikujiResults.jsp").forward(request, response);
		}
	}

	/**
	 * ❷誕生日を求めるメソッド。
	 * 入力チェックに引っかかればそのエラーが全てリストに詰められる
	 *
	 * @return checkBirthday
	 */
	public static List<String> checkBirthday(String birthday) {
		List<String> checkBirthday = new ArrayList<String>();

		/**
		 * ①入力チェックをする。
		 * １、入力された日付が８桁以外の場合は、エラーメッセージを出力
		 * ２、正しい年月日かどうか入力チェックする。
		 */
		// ①ー１、入力されたのが８桁かどうかをチェックする。
		// ８桁以外が入力された場合
		// →"例にの通り、８桁を入力してください。"と出力して、次の処理に行かずに誕生日の再入力を求める。
		if (birthday.length() != 8) {
			//エラーメッセージがある場合に処理をする
			checkBirthday.add("例の通り８桁を入力してください。");
		}

		// ①ー２、正しい年月日かどうかをチェックする。
		// 正しい年月日でない場合
		// →"正しい日付を入力してください。"を出力して、誕生日の再入力を求める。
		// ↓日付や時刻を厳密にチェック（厳密にテェック＝存在しない日付を指定された場合、Exception を発生させること。）
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setLenient(false); // Lenient(寛大)に処理を行うか聞かれているため、falseを入力する。
		try {
			format.parse(birthday);
		} catch (Exception e) {
			checkBirthday.add("正しい日付を入力してください。");
		}
		return checkBirthday;
	}

	/**
	 * ●utilクラスのDate型からsqlクラスのDate型に変換するメソッド
	 * （本日の日付を求めるため、utilクラスのDate型をDaoと同じsqlクラスのDate型に変更する必要があるため。）
	 *
	 * @param uDate
	 * @return result_date
	 */
	private static java.sql.Date convert(java.util.Date utilDate) {
		java.sql.Date result_date = new java.sql.Date(utilDate.getTime());
		return result_date;
	}
}
