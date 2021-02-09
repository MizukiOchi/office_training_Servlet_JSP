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

		/**
		 * ③resultsテーブルから「今日から過去半年間の全データ数」を取得
		 */
		double halfMonthDataNum = ResultsDao.receiveHalfMonthResultsData(sqlDate, results_date);

		/**
		 * ④resultsテーブルから「今日から過去半年間の各運勢のデータ数」を取得
		 * ⑤各運勢のデータ数(④)÷全体(③)
		 */
		List<OmikujiBean> receiveHalfMonthResultsFortuneData = OmikujiDao.receiveHalfMonthResultsFortuneData(sqlDate,
				results_date);
		List<String> resultPercent = new ArrayList<String>();

		String fortuneNumName = "";
		double fortuneNum = 0;
		double roundingPercent = 0;
		for (OmikujiBean receiveFortuneBean : receiveHalfMonthResultsFortuneData) {
			fortuneNumName = receiveFortuneBean.getFortune_name();
			fortuneNum = Double.parseDouble(receiveFortuneBean.getHmr_fortune_data_num());
			roundingPercent = ((double)Math.round(fortuneNum / halfMonthDataNum * 100 * 10)) / 10;
//			System.out.println(roundingPercent);
			String percent = fortuneNumName + ":" + roundingPercent + "%";
			resultPercent.add(percent);
		}
		/**
		 * ⑥本日の全データ数を取得
		 *
		 */
		double todayDataNum = ResultsDao.receiveTodayResultsData(results_date);
//		System.out.println(todayDataNum);

		/**
		 * ⑦resultsテーブルから「今日の各運勢のデータ数」を取得
		 * ⑧各運勢のデータ数(⑦)÷全体(⑥)
		 */
		String[] fortune_name = {"大吉","中吉","小吉", "末吉","吉","凶"};
		List<OmikujiBean> receiveTodayResultsFortuneData = new ArrayList<OmikujiBean>();
		for (String fortuneName : fortune_name) {
			List<OmikujiBean> receiveDataList = OmikujiDao.receiveTodayResultsFortuneData(results_date, fortuneName);
			if(!receiveDataList.isEmpty()) {
				receiveTodayResultsFortuneData.addAll(receiveDataList);
				System.out.println(receiveTodayResultsFortuneData);
			}else if(receiveDataList.isEmpty()){
				OmikujiBean omikujiBean = new OmikujiBean();
				omikujiBean.setFortune_name(fortuneName);
				omikujiBean.setHmr_fortune_data_num("0");
				receiveTodayResultsFortuneData.add(omikujiBean);
				System.out.println(receiveTodayResultsFortuneData);
			}
		}
		List<String> resultsTodayList = new ArrayList<String>();

		String tFortuneNumName = "";
		double tFortuneNum = 0;
		double tRoundingPercent = 0;
		for (OmikujiBean receiveFortuneBean : receiveTodayResultsFortuneData) {
			tFortuneNumName = receiveFortuneBean.getFortune_name();
			tFortuneNum = Double.parseDouble(receiveFortuneBean.getHmr_fortune_data_num());
			tRoundingPercent = ((double)Math.round(tFortuneNum / todayDataNum * 100 * 10)) / 10;
			String todayPercent = tFortuneNumName + ":" + tRoundingPercent + "%";
			System.out.println(todayPercent);
			resultsTodayList.add(todayPercent);
			System.out.println(resultsTodayList);
		}

		request.setAttribute("resultsPercentList", resultPercent);
		request.setAttribute("resultsTodayList", resultsTodayList);
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
