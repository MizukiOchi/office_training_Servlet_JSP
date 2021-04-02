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
	 * メソッドの説明：
	 * omikuji_idを条件に結果を取得するメソッド。
	 *
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
			String sql = "SELECT f.fortune_id, f.fortune_name, f.changer, f.update_date, f.author, f.create_date, o.omikuji_id, o.fortune_id, o.wish, o.business, o.study,o.changer, o.update_date, o.author, o.create_date FROM fortune f LEFT OUTER JOIN omikuji o ON f.fortune_id = o.fortune_id WHERE o.omikuji_id = ?;";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, omikujiId);
			// SQLを実行する処理。
			ResultSet resultSet = preparedStatement.executeQuery();
			// 取得した結果を１行ずつ読み込む処理。
			while (resultSet.next()) {
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
	 * メソッドの説明：
	 * omikujiテーブルの件数を検索するメソッド。
	 *
	 * @param num
	 * @return num
	 */
	public static int count() {
		Connection connection = null;
		PreparedStatement ps = null;
		int num = 0;
		try {
			/**DBManagerのgetConnectionメソッドを呼び出してDBに接続。*/
			connection = DBManager.getConnection();

			/**omikujiテーブルのデータ数をカウントする処理。*/
			String sql = "SELECT COUNT(*) AS num FROM omikuji; "; //Memo：ASは一時的にカラム名をつけることができる
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる。
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			// SQLを実行する処理。
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
	 * メソッドの説明：
	 * resultsテーブルから「今日から過去半年間」の「運勢名」と「各運勢のデータ数」を取得するメソッド
	 *
	 * @param sqlDate, resultsDate
	 * @return receiveHalfMonthResultsFortuneData
	 */
	public static List<OmikujiBean> receiveHalfMonthResultsFortuneData(Date sqlDate, Date resultsDate) {
		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		List<OmikujiBean> receiveHalfMonthResultsFortuneData = new ArrayList<OmikujiBean>();
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// 本日から過去半年間の各運勢データの個数を取得
			String sql = "SELECT f.fortune_id, f.fortune_name, COUNT(r.*) AS hmr_fortune_data_num FROM fortune f LEFT OUTER JOIN omikuji o ON f.fortune_id = o.fortune_id LEFT OUTER JOIN results r ON o.omikuji_id = r.omikuji_id AND r.results_date BETWEEN ? AND ? GROUP BY f.fortune_id ORDER BY f.fortune_id ASC;";
			// sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, sqlDate); // ②ー１
			preparedStatement.setDate(2, resultsDate); // ②ー２
			// SQLを実行する処理。
			ResultSet resultSet = preparedStatement.executeQuery();
			// 結果をResultsBeanにsetしながら１行ずつ読み込む
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
	public static List<OmikujiBean> receiveTodayResultsFortuneData(Date resultsDate) {

		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		List<OmikujiBean> receiveTodayResultsFortuneData = new ArrayList<OmikujiBean>();

		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// 本日から過去半年間のデータの個数を取得
			String sql = "SELECT f.fortune_name, COUNT(r.*) AS tdr_fortune_data_num FROM fortune f LEFT OUTER JOIN omikuji o ON f.fortune_id = o.fortune_id LEFT OUTER JOIN results r ON o.omikuji_id = r.omikuji_id AND r.results_date =? GROUP BY f.fortune_id ORDER BY f.fortune_id ASC;";
			// ●sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, resultsDate);
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
