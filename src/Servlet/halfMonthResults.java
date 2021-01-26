package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Bean.OmikujiBean;
import DAO.OmikujiDao;
import DAO.ResultsDao;

/**
 * Servlet implementation class halfMonthResults
 */
@WebServlet("/halfMonthResults")
public class halfMonthResults extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/**
		 * ①今日の日付を取得
		 */
		Date today = new Date();
		java.sql.Date results_date = convert(today);

		/**
		 * ②今日から半年前の日付を取得
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -6);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		java.sql.Date sqlDate = java.sql.Date.valueOf(year + "-" + (month + 1) + "-" + date);

		//		/**
		//		 * ③sqlで今日から半年のデータを全て取得
		//		 */
		//		List<ResultsBean> rb = ResultsDao.selecthalfMonthResults(sqlDate, results_date);

		/**
		 * ④resultsテーブルから「今日から過去半年間の全データ数」を取得
		 */
		int receiveAllNum = ResultsDao.receiveHalfMonthResultsData(sqlDate, results_date);
		System.out.println("過去半年のデータ数は：" + receiveAllNum);

		List<OmikujiBean> receiveFortuneNum = OmikujiDao.receiveHalfMonthResultsFortuneData(sqlDate, results_date);
		List<String> fortuneNameList = new ArrayList<String>();

		for (OmikujiBean omikujiBean : receiveFortuneNum) {
			fortuneNameList.add(omikujiBean.getFortune_name());
			System.out.println("過去半年のデータ数は(運勢) ：" + omikujiBean.getFortune_name());
		}

		request.getRequestDispatcher("/jsp/JhalfMonthResults.jsp").forward(request, response);

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
