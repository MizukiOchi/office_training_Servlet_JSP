package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Bean.OmikujiBean;
import Bean.ResultsBean;

public class ResultsDao {

	/**
	 * メソッドの説明：
	 * resultsテーブルから取得した『誕生日」と「本日」の日付を条件に一致したデータをDBから取得するメソッド
	 *
	 * @param resultsDate,birthday
	 * @return resultsBean
	 */
	public static ResultsBean selectByBirthday(Date resultsDate, String birthday) {
		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		ResultsBean resultsBean = new ResultsBean();
		try {
			/**DBManagerのgetConnectionメソッドを呼び出してDBに接続。*/
			connection = DBManager.getConnection();

			/**
			 * resultsテーブルから２つのデータを条件にDBからデータを取得する処理。
			 * １、mainメソッドで取得した本日の日付（resultsDate）
			 * ２、mainメソッドで入力画面から受け渡された誕生日（birthday）
			 */
			String sql = "SELECT results_date, omikuji_id, birthday, changer, update_date, author,create_date FROM results WHERE results_date = ? AND birthday = ?;";
			// ●sqlに詰めたSELECT文をpreparedStatementに代入してjavaで動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO：PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, resultsDate); // ②ー１
			preparedStatement.setString(2, birthday); // ②ー２
			// ●executeQueryメソッドを呼び出してSQLを実行する処理。
			ResultSet resultSet = preparedStatement.executeQuery();
			// ●変数resultSetに入っている実行結果をResultsBeanにsetしながら１行ずつ読み込む
			// （=条件に一致しているデータがあれば、変数resultSetに代入されている）
			while (resultSet.next()) {
				resultsBean.setResultsDate(resultSet.getDate("results_date"));
				resultsBean.setOmikujiId(resultSet.getString("omikuji_id"));
				resultsBean.setBirthday(resultSet.getString("birthday"));
				resultsBean.setChanger(resultSet.getString("changer"));
				resultsBean.setUpdateDate(resultSet.getString("update_date"));
				resultsBean.setAuthor(resultSet.getString("author"));
				resultsBean.setCreateDate(resultSet.getString("create_date"));
				// ↑※利用しないフィールドも今後利用するかもしれないためセットしていることが多い。
				// その理由から今回もセットしている。
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return resultsBean;
	}

	/**
	 * メソッドの説明：
	 * 新しく生成したおみくじをresultsテーブルに登録するメソッド。
	 *
	 * @param resultsDate, omikujiId, birthday
	 * @return resultsBean
	 */
	public static void insertResults(Date resultsDate, String omikujiId, String birthday) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			String sql = "INSERT INTO results(results_date, omikuji_id, birthday, changer, update_date, author, create_date) VALUES (?, ?, ?, '越智', CURRENT_TIMESTAMP, '越智', CURRENT_TIMESTAMP); ";
			// PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, resultsDate);
			preparedStatement.setString(2, omikujiId);
			preparedStatement.setString(3, birthday);
			//SQL文を実行する処理。
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
	}
	/**
	 * メソッドの説明：
	 * resultsテーブルから「今日から過去半年間」の「全データ数」を取得するメソッド
	 *
	 * @param sqlDate, resultsDate
	 * @return receiveHalfMonthResultsData
	 */
	public static int receiveHalfMonthResultsData(Date sqlDate, Date resultsDate) {
		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		int receiveHalfMonthResultsData = 0;
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			//「今日から過去半年間のデータ」のデータ数を取得
			String sql = "SELECT COUNT(*) AS receiveHalfMonthResultsDataNum FROM results WHERE results_date BETWEEN ? AND ?;";
			// sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, sqlDate);
			preparedStatement.setDate(2, resultsDate);
			// SQLを実行する処理。
			ResultSet resultSet = preparedStatement.executeQuery();
			// 結果をResultsBeanにsetしながら１行ずつ読み込む処理。
			while (resultSet.next()) {
				receiveHalfMonthResultsData = resultSet.getInt("receiveHalfMonthResultsDataNum");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return receiveHalfMonthResultsData;
	}

	/**
	 * resultsテーブルから「本日占ったデータ」からbirthdayを取得するメソッド
	 *
	 * @param todaysBirthdayList
	 * @return todaysBirthdayList
	 */
	public static List<ResultsBean> todaysBirhday(Date resultsDate) {
		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		List<ResultsBean> todaysBirthdayList = new ArrayList<ResultsBean>();
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// 本日から過去半年間のデータの個数を取得
			String sql = "SELECT birthday FROM results WHERE results_date = ?;";
			// ●sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, resultsDate); //
			// ●executeQueryメソッドを呼び出してSELECT文を実行して、実行結果（=検索結果）をResultSet型の変数に代入
			ResultSet resultSet = preparedStatement.executeQuery();
			// ●変数resultSetに入っている実行結果をResultsBeanにsetしながら１行ずつ読み込む
			// （=条件に一致しているデータがあれば、変数resultSetに代入されている）
			while (resultSet.next()) {
				ResultsBean rb = new ResultsBean();
//				rb.setResults_date(resultSet.getDate("results_date"));
//				rb.setOmikuji_id(resultSet.getString("omikuji_id"));
				rb.setBirthday(resultSet.getString("birthday"));
//				rb.setChanger(resultSet.getString("changer"));
//				rb.setUpdate_date(resultSet.getString("update_date"));
//				rb.setAuthor(resultSet.getString("author"));
//				rb.setCreate_date(resultSet.getString("create_date"));
//				rb.setReceiveHalfMonthResultsData(resultSet.getInt("receiveHalfMonthResultsData"));
//				rb.setTodaysBirthday(resultSet.getString("todaysBirthday"));
				todaysBirthdayList.add(rb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return todaysBirthdayList;
	}

	/**
	 * メソッドの説明：
	 * いま入力された誕生日の過去半年間の結果を取得するメソッド
	 *
	 * @param sqlDate, resultsDate, birthday
	 * @return pastBirthdayResultsList
	 */
	public static List<ResultsBean> pastBirhdayResults(Date sqlDate, Date resultsDate, String birthday) {
		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		List<ResultsBean> pastBirthdayResultsList = new ArrayList<ResultsBean>();
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// いま入力された同じ誕生日の過去半年間のデータを取得
			String sql = "SELECT r.results_date, f.fortune_name, o.wish, o.business, o.study FROM  results r  LEFT OUTER JOIN omikuji o ON (r.omikuji_id = o.omikuji_id) LEFT OUTER JOIN fortune f ON (o.fortune_id = f.fortune_id) WHERE r.results_date BETWEEN ? AND ? AND r.birthday = ?;";
			// sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, sqlDate);
			preparedStatement.setDate(2, resultsDate);
			preparedStatement.setString(3, birthday);
			// SQLを実行する処理。
			ResultSet resultSet = preparedStatement.executeQuery();
			//結果をResultsBeanにsetしながら１行ずつ読み込む
			while (resultSet.next()) {
				ResultsBean rb = new ResultsBean();
				rb.setResultsDate(resultSet.getDate("results_date"));
				OmikujiBean ob = new OmikujiBean();
				ob.setFortuneName(resultSet.getString("fortune_name"));
				ob.setWish(resultSet.getString("wish"));
				ob.setBusiness(resultSet.getString("business"));
				ob.setStudy(resultSet.getString("study"));
				rb.setOb(ob);
				pastBirthdayResultsList.add(rb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return pastBirthdayResultsList;
	}

	/**
	 * resultsテーブルから「今日から過去半年間のデータ」のデータ個数を取得するメソッド
	 *
	 * @param receiveHalfMonthResultsDataNum
	 * @return receiveTodayResultsData
	 */
	public static int receiveTodayResultsData(Date resultsDate) {

		Connection connection = null; // 特定のDBとの接続
		PreparedStatement ps = null; // SQL文がプレコンパイルされ、PreparedStatementに格納される。
		int receiveTodayResultsData = 0;
		try {
			// DBに接続する
			connection = DBManager.getConnection();
			// 本日から過去半年間のデータの個数を取得
			String sql = "SELECT COUNT(*) AS receiveTodayResultsDataNum FROM results WHERE results_date = ?;";
			// ●sqlに詰めたSELECT文をpreparedStatementに代入して動的に条件を変更できるようにする。
			PreparedStatement preparedStatement = connection.prepareStatement(sql); // MEMO:PreparedStatementは条件を動的にしてjavaで条件を自由に変更できる
			preparedStatement.setDate(1, resultsDate);
			// ●executeQueryメソッドを呼び出してSELECT文を実行して、実行結果（=検索結果）をResultSet型の変数に代入
			ResultSet resultSet = preparedStatement.executeQuery();
			// ●変数resultSetに入っている実行結果をResultsBeanにsetしながら１行ずつ読み込む
			// （=条件に一致しているデータがあれば、変数resultSetに代入されている）
			while (resultSet.next()) {
				receiveTodayResultsData = resultSet.getInt("receiveTodayResultsDataNum");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(ps, connection);
		}
		return receiveTodayResultsData;
	}




}
