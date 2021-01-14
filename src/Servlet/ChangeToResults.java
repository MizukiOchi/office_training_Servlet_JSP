package Servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public static String errorMessage1 = "";
	public static String errorMessage2 = "";
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/**
		 * ①Servlet画面で入力した誕生日を取得する
		 */
		String errorNum = "";
		//HTML（サーブレット）から入力されたパラメータの値を取得
		String birthday = request.getParameter("birthday");
		//checkBirthday（入力チェックするメソッドで）がfalseの時、InputBirthdayクラスに戻ってエラーを画面に出す
		String checkBirthday = checkBirthday(birthday);
		if(checkBirthday == nonCorrectLength) {
			errorMessage1 = "例の通り８桁を入力してください。";
			request.setAttribute("errorMessage1", errorMessage1);
			request.getRequestDispatcher("/InputBirthday").forward(request, response);
			return;
		}else if(checkBirthday == nonCorrectDate) {
			errorMessage2 = "正しい日付を入力してください。";
		request.setAttribute("errorMessage2", errorMessage2);
		request.getRequestDispatcher("/InputBirthday").forward(request, response);
		return;
		}

		/**
		 * ②今日の日付を取得する
		 */
		Date date = new Date();
		java.sql.Date results_date = convert(date);

		/**
		 * ③resultsテーブルから誕生日・本日の日付を条件にデータを取得する
		 * １、JDBCで繋ぐ
		 * ２、resultsテーブルから条件に当てはまるデータを取得する
		 * ３、データがあれば④。
		 * ４、テータがなければ⑤。
		 *  return resultsBean
		 */
		ResultsBean rb = ResultsDao.selectByBirthday(results_date, birthday);

		/**
		 * ④変数rbに入っているデータのomikuji_idを取得してデータがあればそのデータを出力する。
		 * （＝変数rbにomikuji_idが入っていれば、条件に一致した事になるため）
		 * １、omikujiテーブルとfortuneテーブルの結合したデータを取得→出力→終了
		 *  return omikujiBean
		 */
		// ResultsDaoのselectByBirthdayメソッドを呼び出して、取得した結果を変数rbに代入。
		// （=条件に一致しているデータがあれば、変数rbに代入されることになる
		// ＝その結果を出力すれば良いということになる）
		String omikuji_id = rb.getOmikuji_id();

		/**
		 * ⑤resultsテーブルに入力した結果がない場合→おみくじを生成する
		 * １、もしomikujiテーブルにデータがなければ、omikujiテーブルのデータを書き込むメソッドを呼ぶ
		 * ２、omikujiテーブルのレコード数(sqlのcountを使用)でrandomの数字を取得
		 * ２、⑤ー１、で出た数字をデータのomikuji_idを条件にして、SELECT文を使用してomikujiテーブル（fortuneテーブルも結合する）からデータを取得
		 * ３、resultsテーブルに⑤ー２、で取得したデータを登録する（omikuji_id, results_date, birthday）
		 * ４、コンソールに⑤−２、で取得したデータを出力する。
		 */
		if (omikuji_id == null) {

			// ⑤ー１、もしomikujiテーブルにデータがなければ、omikujiテーブルのデータを書き込むメソッド(OmikujiDao.count())を呼ぶ
			int omikujiCnt = OmikujiDao.count();
			if (omikujiCnt == 0) {
				CSVReader.readCsv();
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
		request.setAttribute("results",oi);
		request.getRequestDispatcher("/jsp/OmikujiResults.jsp").forward(request, response);
}

	/**
	 * ❷誕生日を求めるメソッド。コンソールに出力できるように設定。
	 *
	 * @return sDate
	 */
//	定数を使用して何の値かを分かりやすくする↓
	public static final String nonCorrectLength = "1";
	public static final String nonCorrectDate = "2";
	public static String checkBirthday(String birthday) {
		String checkBirthday = null;
				/**
				 * ①入力チェックをする。
				 * １、入力された日付が８桁以外の場合は、エラーメッセージを出力
				 * ２、正しい年月日かどうか入力チェックする。
				 */
				// ①ー１、入力されたのが８桁かどうかをチェックする。
				// ８桁以外が入力された場合
				// →"例にの通り、８桁を入力してください。"と出力して、次の処理に行かずに誕生日の再入力を求める。
				if (birthday.length() != 8) {
					 checkBirthday = nonCorrectLength;
					 return checkBirthday;
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
					checkBirthday = nonCorrectDate;
				}
		return checkBirthday;
	}


	/**
	 * ●utilクラスのDate型からsqlクラスのDate型に変換するメソッド
	 * （本日の日付を求めるため、utilクラスのDate型をDaoと同じsqlクラスのDate型に変更する必要があるため。）
	 *
	 * @param uDate
	 * @return
	 */
	private static java.sql.Date convert(java.util.Date utilDate) {
		java.sql.Date result_date = new java.sql.Date(utilDate.getTime());
		return result_date;
	}
}
