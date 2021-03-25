
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
@WebServlet("/HalfMonthResults")
public class HalfMonthResults extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/**
		 * ①今日の日付を取得
		 */
		Date today = new Date();
		java.sql.Date resultsDate = convert(today); //spl文に対応したDate型に変換。(下記で定義しているconvertメソッドで)

		/**
		 * ②今日から半年前の日付を取得
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -6);
		today = calendar.getTime();
		java.sql.Date sqlDate = convert(today); //spl文に対応したDate型に変換。(下記で定義しているconvertメソッドで)

		/**
		 * ②、過去半年の運勢の割合を取得する
		 */
		/**１、resultsテーブルから「今日から過去半年間の全データ数」を取得*/
		double halfMonthDataNum = ResultsDao.receiveHalfMonthResultsData(sqlDate, resultsDate);
		/**２、resultsテーブルから「今日から過去半年間の各運勢のデータ数」を取得*/
		//全運勢が出力されるようにするための処理。
		//（配列に各運勢名を入れてfor文を回して、１つずつ運勢を条件にresultsテーブルから当てはまる運勢名があるかを検索して、なければ０を代入。）
		String[] arrayHfortuneName = {"大吉","中吉","小吉", "末吉","吉","凶"};
		//※必ず運勢全てが詰められるように用意したlist。
		List<OmikujiBean> receiveHarfMonthResultsFortuneData = new ArrayList<OmikujiBean>();
		//配列(arrayHfortuneName)に詰めた運勢を１つずつ回す。
		for (String hfortuneName : arrayHfortuneName) {
			//ここで一致する運勢があればOmikujiDaoから取ってきた物をそのままhreceiveDataListに詰めて、
			//なければ運勢名と運勢数０(ゼロ)を代入してhreceiveDataListに詰める。
			List<OmikujiBean> hreceiveDataList = OmikujiDao.receiveHalfMonthResultsFortuneData(sqlDate, resultsDate, hfortuneName);
			if(!hreceiveDataList.isEmpty()) {
				receiveHarfMonthResultsFortuneData.addAll(hreceiveDataList);
			}else if(hreceiveDataList.isEmpty()){
				OmikujiBean omikujiBean = new OmikujiBean();
				omikujiBean.setFortuneName(hfortuneName);
				omikujiBean.setHmrFortuneDataNum("0");
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
			hfortuneNumName = receiveFortuneBean.getFortuneName();
			hfortuneNum = Double.parseDouble(receiveFortuneBean.getHmrFortuneDataNum());
			hroundingPercent = ((double)Math.round(hfortuneNum / halfMonthDataNum * 100 * 10)) / 10;
			String percent = hfortuneNumName + ":" + hroundingPercent + "%";
			resultPercent.add(percent);
		}
		/**
		 * ③、本日の運勢データの割合を取得する
		 */
		/**１、resultsテーブルから「今日の全データ数」を取得*/
		double todayDataNum = ResultsDao.receiveTodayResultsData(resultsDate);
		/**２、resultsテーブルから「今日の各運勢のデータ数」を取得*/
//		配列に各運勢名を入れてfor文を回して、１つずつの運勢でresultsテーブルに当てはまる運勢があるかを検索して、なければ０をここで入れるようにしている。
		String[] arrayFortuneName = {"大吉","中吉","小吉", "末吉","吉","凶"};
//		※ここで必ず運勢全てがlistに詰められるようになる
		List<OmikujiBean> receiveTodayResultsFortuneData = new ArrayList<OmikujiBean>();
//		配列に詰めた運勢を１つずつ回す
		for (String fortuneName : arrayFortuneName) {
//			もし、for文で回っている運勢と一致する運勢名がresultsテーブルに入っていたらlistに取ってきた値（resultsテーブルにある各運勢の数）を積める
			List<OmikujiBean> receiveDataList = OmikujiDao.receiveTodayResultsFortuneData(resultsDate, fortuneName);
			if(!receiveDataList.isEmpty()) {
				receiveTodayResultsFortuneData.addAll(receiveDataList);
//				System.out.println(receiveTodayResultsFortuneData);
			}else if(receiveDataList.isEmpty()){
				OmikujiBean omikujiBean = new OmikujiBean();
				omikujiBean.setFortuneName(fortuneName);
				omikujiBean.setHmrFortuneDataNum("0");
				receiveTodayResultsFortuneData.add(omikujiBean);
//				System.out.println(receiveTodayResultsFortuneData);
			}
		}
		List<String> resultsTodayList = new ArrayList<String>();

		String tFortuneNumName = "";
		double tFortuneNum = 0;
		double tRoundingPercent = 0;
		for (OmikujiBean receiveFortuneBean : receiveTodayResultsFortuneData) {
			tFortuneNumName = receiveFortuneBean.getFortuneName();
			tFortuneNum = Double.parseDouble(receiveFortuneBean.getHmrFortuneDataNum());
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
	 * @return resultDate
	 */
	private static java.sql.Date convert(java.util.Date utilDate) {
		java.sql.Date resultDate = new java.sql.Date(utilDate.getTime());
		return resultDate;
	}

}
