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
 * メソッドの説明：
 * Servlet/JSP間の処理実装(メインメソッド)
 * Servlet implementation class ChangeToResults
 *
 * @author m_ochi
 */
@WebServlet("/ChangeToResults")
public class ChangeToResults extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/** Servlet画面で入力した誕生日を取得する*/
		//HTML（サーブレット）から入力されたパラメータの値を取得
		String birthday = request.getParameter("birthday");
		request.setAttribute("birthday", birthday); //画面遷移１回で値が消滅する
		//エラー時には入力した誕生日がtext内に残す処理（一時的にgetParameterで取得した値を保存）
		HttpSession session = request.getSession();
		session.setAttribute("birthday", birthday);//画面遷移を何回しても半永久敵に値が保存できる

		/**エラーメッセージの処理*/
		//checkBirthday（入力チェックするメソッドで）対象の値が返ってきた時は、InputBirthdayクラスに戻ってエラーを画面に出す
		List<String> ErrorMessageList = checkBirthday(birthday);
		if (ErrorMessageList != null && ErrorMessageList.size() > 0) {
			request.setAttribute("ErrorMessageList", ErrorMessageList);
			request.getRequestDispatcher("/InputBirthday").forward(request, response);
		} else {

			/**今日の日付を取得する */
			Date date = new Date();
			java.sql.Date resultsDate = convert(date);

			/** resultsテーブルから「誕生日」と「本日の日付」を条件にomikujiIdを取得する。
			 * １、データあり：取得したomikujiIdで出力
			 * ２、データなし：これからおみくじを生成し、そのomikujiIdで出力
			 *
			 * @return resultsBean
			 */
			ResultsBean rb = ResultsDao.selectByBirthday(resultsDate, birthday);
			String omikujiId = rb.getOmikujiId();
			String path =this.getServletContext().getRealPath("/WEB-INF/fortuneTelling.csv");

			/**新しくおみくじを生成してomikujiIdを取得する処理。*/
			if (omikujiId == null) {
				/**omikujiテーブルにデータが入っているかをチェックする処理。*/
				// もしomikujiテーブルにデータがなければ、csvファイルを読み込んでomikujiテーブルのデータを書き込む処理。
				int omikujiCnt = OmikujiDao.count();
				if (omikujiCnt == 0) {
					CSVReader.readCsv(path);
					omikujiCnt = OmikujiDao.count();
				}
				/**Randomでomikuji_idを取得する処理。（omikuji_idの全IDをシャッフルする） */
				Random random = new Random();
				// DBの接続して、randomの引数をSQLのCountを使用して取得する
				int randomValue = random.nextInt(omikujiCnt) + 1; // メソッドが０からカウントされるため、メソッド（）＋１をする
				omikujiId = Integer.toString(randomValue);
				/** 新しく生成したおみくじ(results_date・omikuji_id・birthday)DBに登録する処理。*/
				ResultsDao.insertResults(resultsDate, omikujiId, birthday);
			}

			/**omikuji_idを条件にomikujiテーブルから占い結果を取得する処理。*/
			OmikujiBean oi = OmikujiDao.selectByOmikuji(omikujiId);

			/**jspで画面に出力する。*/
			request.setAttribute("results", oi);
			request.getRequestDispatcher("/jsp/OmikujiResults.jsp").forward(request, response);
		}
	}

	/**
	 * メソッドの説明：
	 * 受け取った誕生日の値が正しいかどうかをチェックするメソッド。
	 * １、入力された日付が８桁かのチェック
	 * ２、存在する日付かどうかのチェック
	 *
	 * @return checkBirthday
	 */
	public static List<String> checkBirthday(String birthday) {
		List<String> checkBirthday = new ArrayList<String>();
		/**入力された日付が８桁かのチェック*/
		if (birthday.length() != 8) {
			checkBirthday.add("例の通り８桁を入力してください。");
		}
		/**存在する日付かどうかのチェック*/
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
	 * メソッドの説明：
	 * utilクラスのDate型からsqlクラスのDate型に変換するメソッド
	 * （SQLで取得した本日の日付を使用するために、SQL使用できる型に変換する）
	 *
	 * @param java.util.Date utilDate
	 * @return resultDate
	 */
	private static java.sql.Date convert(java.util.Date utilDate) {
		java.sql.Date resultDate = new java.sql.Date(utilDate.getTime());
		return resultDate;
	}
}
