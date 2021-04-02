package List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DAO.DBManager;

/**
 * メソッドの説明：
 * 　fortuneTelling.csvをomikujiテーブルに登録するためのメソッド
 *
 * @author m_ochi
 *
 */
public class CSVReader {
	public static void readCsv(String path) throws IOException {
		Connection connection = null;// 特定のDBとの接続
		try {
			/**DBManagerのgetConnectionメソッドを呼び出してDBに接続。*/
			connection = DBManager.getConnection();

			/**csvファイルを読み込む処理。*/
			//CSVファイルを１行ずつ読み込む
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String data = "";
			int x = 1;
			// ※箱は１つで良いためwhile文の外で作成しておく
			String[] contents;
			// 不要な一行目をループ前に読み込む処理。
			bufferedReader.readLine(); //←１行ずつ読み込むメソッド
			//読み込んだ値をセットするための変数を用意しておく。
			int unsei = 0;
			String wish = "";
			String business = "";
			String study = "";
			String ochi = "越智";

			while ((data = bufferedReader.readLine()) != null) {
				/** csvファイルの値を分解する処理。*/
				contents = data.split(",");
				/**値をセットする処理。*/
				//omikujiテーブルに代入するために値をセットする
				unsei = JudgeUnseiCode(contents[0]); //←omikujiテーブルのfortune_idと紐づけるために文字列を数字に変換する必要がある。
				wish = contents[1];
				business = contents[2];
				study = contents[3];

				/**csvファイルで読み込んだデータをomikujiテーブルに登録する処理。*/
				String sql = "INSERT INTO omikuji(omikuji_id,fortune_id, wish, business, study, changer, update_date, author, create_date) values (?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp);";
				//javaで動的に値をセットする処理。
				PreparedStatement ps = connection.prepareStatement(sql);
				//登録するSQL文を動的にsetする
				ps.setInt(1, x++);
				ps.setInt(2, unsei);
				ps.setString(3, wish);
				ps.setString(4, business);
				ps.setString(5, study);
				ps.setString(6, ochi);
				ps.setString(7, ochi);
				//SQL文を実行するメソッド。
				ps.executeUpdate(); //※executeUpdate()メソッドは、DBに登録・更新・削除をする際に使用するメソッド。
			}
			bufferedReader.close();
		} catch (SQLException e) {
			e.getMessage();
		}
	}

/**
 * メソッドの説明：
 * CSVファイルから読み込んだ文字列（fortune_name:大吉・中吉など）をfortune_idの数字に変換するメソッド
 * （例：大吉の場合 → １）
 * @param s
 * @return unsei
 */
	private static int JudgeUnseiCode(String s) {
		int unsei = 0;
		// fortune_idにつける番号をswitch文を使用して割り当てる。
		switch (s) {
		case "大吉":
			unsei = 1;
			break;
		case "中吉":
			unsei = 2;
			break;
		case "小吉":
			unsei = 3;
			break;
		case "末吉":
			unsei = 4;
			break;
		case "吉":
			unsei = 5;
			break;
		case "凶":
			unsei = 6;
			break;
		}
		return unsei;
	}

}
