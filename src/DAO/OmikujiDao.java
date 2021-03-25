package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Bean.OmikujiBean;

public class OmikujiDao {
	/**
	 * ●omikujiIdを条件におみくじを検索するメソッド
	 *（出力できるようにfortuneテーブルと結合している）
	 * @param omikujiId
	 * @return omikujiBean
	 */
	public static OmikujiBean selectByOmikuji(String omikujiId) {

		Connection connection = null;
		PreparedStatement ps = null;
		OmikujiBean omikujiBean = new OmikujiBean();
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// sqlにselect文を入れる
			String sql = "SELECT f.fortune_id, f.fortune_name, f.changer, f.update_date, f.author, f.create_date, o.omikuji_id, o.fortune_id, o.wish, o.business, o.study,o.changer, o.update_date, o.author, o.create_date FROM fortune f LEFT OUTER JOIN omikuji o ON f.fortune_id = o.fortune_id WHERE o.omikuji_id = ?;";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, omikujiId);
			// resultsテーブルから値を取得
			ResultSet resultSet = preparedStatement.executeQuery();
			// ③ー２、結果ファイルを１行ずつ読む。
			while (resultSet.next()) {
				// // resultsテーブルを１行ずつ読み込んで出力する
				omikujiBean.setOmikujiId(resultSet.getString("omikuji_id"));
				omikujiBean.setFortuneId(resultSet.getString("fortune_id"));
				omikujiBean.setWish(resultSet.getString("wish"));
				omikujiBean.setBusiness(resultSet.getString("business"));
				omikujiBean.setStudy(resultSet.getString("study"));
				omikujiBean.setChanger(resultSet.getString("changer"));
				omikujiBean.setUpdateDate(resultSet.getString("update_date"));
				omikujiBean.setAuthor(resultSet.getString("author"));
				omikujiBean.setCreateDate(resultSet.getString("create_date"));
				omikujiBean.setFortuneName(resultSet.getString("fortune_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			DBManager.close(ps, connection);

		}
		return omikujiBean;

	}

	/**
	 * omikujiテーブルの件数を検索するメソッド。
	 * （randomの引数(omikujiテーブルの登録数)をSQLのCountを使用して取得するため）
	 *
	 * @param num
	 * @return num
	 */
	public static int count() {

		Connection connection = null;
		PreparedStatement ps = null;
		int num = 0;
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// sqlにselect文を入れる（AS テーブル名（今回はnum）:１時的にnumテーブルを作成する）
			String sql = "SELECT COUNT(*) AS num FROM omikuji; ";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			// resultsテーブルから値を取得
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				num = resultSet.getInt("num");
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			DBManager.close(ps, connection);

		}
		return num;

	}

	/**
	 * resultsテーブルから「今日から過去半年間の各運勢データ」のデータ数を取得するメソッド
	 *
	 * @param receiveHalfMonthResultsFortuneData
	 * @return receiveHalfMonthResultsFortuneData
	 */
	public static List<OmikujiBean> receiveHalfMonthResultsFortuneData(Date sqlDate, Date resultsDate, String hfortuneName) {

		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		List<OmikujiBean> receiveHalfMonthResultsFortuneData = new ArrayList<OmikujiBean>();

		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// 本日から過去半年間のデータの個数を取得
			String sql = "SELECT f.fortune_name, COUNT(*) AS hmr_fortune_data_num FROM results r LEFT OUTER JOIN omikuji o ON r.omikuji_id = o.omikuji_id LEFT OUTER JOIN fortune f ON o.fortune_id = f.fortune_id WHERE r.results_date BETWEEN ? AND ? AND f.fortune_name = ? GROUP BY f.fortune_id ORDER BY f.fortune_id ASC ;";
			// ●sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, sqlDate); // ②ー１
			preparedStatement.setDate(2, resultsDate); // ②ー２
			preparedStatement.setString(3, hfortuneName);
			// ●executeQueryメソッドを呼び出してSELECT文を実行して、実行結果（=検索結果）をResultSet型の変数に代入
			ResultSet resultSet = preparedStatement.executeQuery();
			// ●変数resultSetに入っている実行結果をResultsBeanにsetしながら１行ずつ読み込む
			// （=条件に一致しているデータがあれば、変数resultSetに代入されている）
			while (resultSet.next()) {
				OmikujiBean omikujiBean = new OmikujiBean();
				omikujiBean.setFortuneName(resultSet.getString("fortune_name"));
				omikujiBean.setHmrFortuneDataNum(resultSet.getString("hmr_fortune_data_num"));
				receiveHalfMonthResultsFortuneData.add(omikujiBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return receiveHalfMonthResultsFortuneData;
	}

	/**
	 * resultsテーブルから「今日の各運勢データ」のデータ数を取得するメソッド
	 *
	 * @param receiveTodayResultsFortuneData
	 * @return receiveTodayResultsFortuneData
	 */
	public static List<OmikujiBean> receiveTodayResultsFortuneData(Date resultsDate, String fortuneName) {

		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		List<OmikujiBean> receiveTodayResultsFortuneData = new ArrayList<OmikujiBean>();

		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// 本日から過去半年間のデータの個数を取得
			String sql = "SELECT f.fortune_name, COUNT(*) AS tdr_fortune_data_num FROM results r LEFT OUTER JOIN omikuji o ON r.omikuji_id = o.omikuji_id LEFT OUTER JOIN fortune f ON o.fortune_id = f.fortune_id WHERE r.results_date = ? AND f.fortune_name = ? GROUP BY f.fortune_id ORDER BY f.fortune_id ASC ;";
			// ●sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, resultsDate);
			preparedStatement.setString(2, fortuneName);
			// ●executeQueryメソッドを呼び出してSELECT文を実行して、実行結果（=検索結果）をResultSet型の変数に代入
			ResultSet resultSet = preparedStatement.executeQuery();
			// ●変数resultSetに入っている実行結果をResultsBeanにsetしながら１行ずつ読み込む
			// （=条件に一致しているデータがあれば、変数resultSetに代入されている）
			while (resultSet.next()) {
				OmikujiBean omikujiBean = new OmikujiBean();
				omikujiBean.setFortuneName(resultSet.getString("fortune_name"));
				omikujiBean.setHmrFortuneDataNum(resultSet.getString("tdr_fortune_data_num"));
				receiveTodayResultsFortuneData.add(omikujiBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return receiveTodayResultsFortuneData;
	}

}
