
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
		 * ①、今日の誕生日と過去半年前の日付を取得する
		 */
		//本日の日付の取得
		Date today = new Date();
		java.sql.Date results_date = convert(today);
		//今日から半年前の日付の取得
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -6);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int date = calendar.get(Calendar.DAY_OF_MONTH);
		java.sql.Date sqlDate = java.sql.Date.valueOf(year + "-" + (month + 1) + "-" + date);

		/**
		 * ②、過去半年の運勢の割合を取得する
		 */
		/**１、resultsテーブルから「今日から過去半年間の全データ数」を取得*/
		double halfMonthDataNum = ResultsDao.receiveHalfMonthResultsData(sqlDate, results_date);
		/**２、resultsテーブルから「今日から過去半年間の各運勢のデータ数」を取得*/
		//全運勢が出力されるようにするための処理。
		//（配列に各運勢名を入れてfor文を回して、１つずつ運勢を条件にresultsテーブルから当てはまる運勢名があるかを検索して、なければ０を代入。）
		String[] hfortune_name = {"大吉","中吉","小吉", "末吉","吉","凶"};
		//※必ず運勢全てが詰められるように用意したlist。
		List<OmikujiBean> receiveHarfMonthResultsFortuneData = new ArrayList<OmikujiBean>();
		//配列(hfortune_name)に詰めた運勢を１つずつ回す。
		for (String hfortuneName : hfortune_name) {
			//ここで一致する運勢があればOmikujiDaoから取ってきた物をそのままhreceiveDataListに詰めて、
			//なければ運勢名と運勢数０(ゼロ)を代入してhreceiveDataListに詰める。
			List<OmikujiBean> hreceiveDataList = OmikujiDao.receiveHalfMonthResultsFortuneData(sqlDate, results_date, hfortuneName);
			if(!hreceiveDataList.isEmpty()) {
				receiveHarfMonthResultsFortuneData.addAll(hreceiveDataList);
			}else if(hreceiveDataList.isEmpty()){
				OmikujiBean omikujiBean = new OmikujiBean();
				omikujiBean.setFortune_name(hfortuneName);
				omikujiBean.setHmr_fortune_data_num("0");
				receiveHarfMonthResultsFortuneData.add(omikujiBean);
			}
		}
		/**３、過去半年の運勢割合の計算する処理。*/
		List<String> resultPercent = new ArrayList<String>();
		String hfortuneNumName = "";
		double hfortuneNum = 0;
		double hroundingPercent = 0;
		//上で詰めたリスト(receiveHarfMonthResultsFortuneData)のデータ(全運勢(６つ))を、
		//以下のfor文で回して１つずつ運勢名と割合計算をしてlist(resultPercent)に詰める。(少数第１位まで出力(少数第２位を四捨五入))
		for (OmikujiBean receiveFortuneBean : receiveHarfMonthResultsFortuneData) {
			hfortuneNumName = receiveFortuneBean.getFortune_name();
			hfortuneNum = Double.parseDouble(receiveFortuneBean.getHmr_fortune_data_num());
			hroundingPercent = ((double)Math.round(hfortuneNum / halfMonthDataNum * 100 * 10)) / 10;
			String percent = hfortuneNumName + ":" + hroundingPercent + "%";
			resultPercent.add(percent);
		}
		/**
		 * ③、本日の運勢データの割合を取得する
		 */
		/**１、resultsテーブルから「今日の全データ数」を取得*/
		double todayDataNum = ResultsDao.receiveTodayResultsData(results_date);
		/**２、resultsテーブルから「今日の各運勢のデータ数」を取得*/
		/**
		 * ⑦resultsテーブルから「今日の各運勢のデータ数」を取得
		 * ⑧各運勢のデータ数(⑦)÷全体(⑥)
		 */
//		配列に各運勢名を入れてfor文を回して、１つずつの運勢でresultsテーブルに当てはまる運勢があるかを検索して、なければ０をここで入れるようにしている。
		String[] fortune_name = {"大吉","中吉","小吉", "末吉","吉","凶"};
//		※ここで必ず運勢全てがlistに詰められるようになる
		List<OmikujiBean> receiveTodayResultsFortuneData = new ArrayList<OmikujiBean>();
//		配列に詰めた運勢を１つずつ回す
		for (String fortuneName : fortune_name) {
//			もし、for文で回っている運勢と一致する運勢名がresultsテーブルに入っていたらlistに取ってきた値（resultsテーブルにある各運勢の数）を積める
			List<OmikujiBean> receiveDataList = OmikujiDao.receiveTodayResultsFortuneData(results_date, fortuneName);
			if(!receiveDataList.isEmpty()) {
				receiveTodayResultsFortuneData.addAll(receiveDataList);
//				System.out.println(receiveTodayResultsFortuneData);
			}else if(receiveDataList.isEmpty()){
				OmikujiBean omikujiBean = new OmikujiBean();
				omikujiBean.setFortune_name(fortuneName);
				omikujiBean.setHmr_fortune_data_num("0");
				receiveTodayResultsFortuneData.add(omikujiBean);
//				System.out.println(receiveTodayResultsFortuneData);
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
//			System.out.println(todayPercent);
			resultsTodayList.add(todayPercent);
//			System.out.println(resultsTodayList);
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
