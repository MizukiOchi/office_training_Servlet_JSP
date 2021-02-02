package Servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Bean.ResultsBean;
import DAO.ResultsDao;

/**
 * Servlet implementation class sameBirthday
 */
@WebServlet("/sameBirthday")
public class sameBirthday extends HttpServlet {
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

		/**
		 * ③いま占った誕生日を全て取得
		 */
		HttpSession session = request.getSession();
		String birthday = (String) session.getAttribute("birthday");
		System.out.println(birthday);
		List<ResultsBean> pastBirhdayResults = ResultsDao.pastBirhdayResults(sqlDate,
				results_date, birthday);
		for (ResultsBean resultsBean : pastBirhdayResults) {
			System.out.println(resultsBean.getOb().getFortune_name());

		}

		request.setAttribute("pastBirhdayResults", pastBirhdayResults);
		request.getRequestDispatcher("/jsp/JsameBirthday.jsp").forward(request, response);
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
