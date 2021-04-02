
package Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@WebServlet("/HalfMonthResults")
public class HalfMonthResults extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/**今日の日付を取得する */
		Date today = new Date();
		java.sql.Date resultsDate = convert(today);

		/**今日から半年前の日付を取得*/
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -6);
		today = calendar.getTime();
		java.sql.Date sqlDate = convert(today);

		/**
		 * 過去半年の運勢の割合を取得する処理。
		 * １、resultsテーブルから「今日から過去半年間の全データ数」を取得
		 * ２、resultsテーブルから「今日から過去半年間の各運勢のデータ数」を取得
		 */
		/**resultsテーブルから「今日から過去半年間」の「全データ数」を取得する処理。*/
		double halfMonthDataNum = ResultsDao.receiveHalfMonthResultsData(sqlDate, resultsDate);
		/**resultsテーブルから「今日から過去半年間」の「運勢名」と「各運勢のデータ数」を取得する処理。*/
		List<OmikujiBean> receiveHarfMonthResultsFortuneData = new ArrayList<OmikujiBean>();
		List<OmikujiBean> hreceiveDataList = OmikujiDao.receiveHalfMonthResultsFortuneData(sqlDate, resultsDate);
		receiveHarfMonthResultsFortuneData.addAll(hreceiveDataList);
		/**過去半年の運勢割合の計算する処理。*/
		List<Map<String,String>> resultPercent = new ArrayList <Map<String,String>>();
		String hfortuneNumName = "";
		double hfortuneNum = 0;
		double hroundingPercent = 0;
		//receiveHarfMonthResultsFortuneData（半年の各運勢）を１行ずつ回して割合を計算。(少数第２位を四捨五入して、少数第１位まで出力）
		for (OmikujiBean receiveFortuneBean : receiveHarfMonthResultsFortuneData) {
			hfortuneNumName = receiveFortuneBean.getFortuneName();
			hfortuneNum = Double.parseDouble(receiveFortuneBean.getHmrFortuneDataNum());
			hroundingPercent = ((double)Math.round(hfortuneNum / halfMonthDataNum * 100 * 10)) / 10;
			//Mapにして横並びのデザイン修正。
			Map<String,String> map = new HashMap<String, String>();
			map.put("hUnseimei", hfortuneNumName);
			map.put("hWariai", hroundingPercent + "%");
			resultPercent.add(map);
		}

		/**
		 * 本日の運勢の割合を取得する処理。
		 * １、resultsテーブルから「今日の全データ数」を取得
		 * ２、resultsテーブルから「今日の各運勢のデータ数」を取得する
		 */
		/**resultsテーブルから「今日の全データ数」を取得する処理。*/
		double todayDataNum = ResultsDao.receiveTodayResultsData(resultsDate);
		/**resultsテーブルから「今日の各運勢のデータ数」を取得する処理。*/
		List<OmikujiBean> receiveTodayResultsFortuneData = new ArrayList<OmikujiBean>();
		List<OmikujiBean> receiveDataList = OmikujiDao.receiveTodayResultsFortuneData(resultsDate);
		receiveTodayResultsFortuneData.addAll(receiveDataList);
		/**本日の運勢割合を計算する処理。*/
		List<Map<String,String>> resultsTodayList = new ArrayList <Map<String,String>>();
		String tFortuneNumName = "";
		double tFortuneNum = 0;
		double tRoundingPercent = 0;
		//receiveTodayResultsFortuneData（各運勢）を１行ずつ回して割合を計算。(少数第２位を四捨五入して、少数第１位まで出力）
		for (OmikujiBean receiveFortuneBean : receiveTodayResultsFortuneData) {
			tFortuneNumName = receiveFortuneBean.getFortuneName();
			tFortuneNum = Double.parseDouble(receiveFortuneBean.getHmrFortuneDataNum());
			tRoundingPercent = ((double)Math.round(tFortuneNum / todayDataNum * 100 * 10)) / 10;
			//Mapにして横並びのデザイン修正。
			Map<String,String> map = new HashMap<String, String>();
			map.put("unseimei", tFortuneNumName);
			map.put("wariai", tRoundingPercent + "%");
			resultsTodayList.add(map);
		}

		request.setAttribute("resultsPercentList", resultPercent);
		request.setAttribute("resultsTodayList", resultsTodayList);
		request.getRequestDispatcher("/jsp/JhalfMonthResults.jsp").forward(request, response);
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
