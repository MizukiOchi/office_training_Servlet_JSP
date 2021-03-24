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
 * ●Fortune telling.csvをomikujiテーブルに読み込むメソッド
 *
 * @author m_ochi
 *
 */
public class CSVReader {
	public static void readCsv(String path) throws IOException {
		/**
		 * ①Fortune telling.csvファイルを読み込んでomikujiテーブルに追加する。
		 * １、JDBCを使用してDBに接続
		 * ２、CSVファイルを読むためのクラスを呼び出す
		 * ３、CSVファイルを１行ずつ読み込む
		 * ４、SQLの実行（csvファイルで読み込んだデータをInsert文にセットしてomikujiテーブルに登録する処理。）
		 */
		//①−１、JDBCを使用してDBに接続
		Connection connection = null;
		try {
			connection = DBManager.getConnection();
			//①ー２、csvファイルを読むクラスを呼び出す
<<<<<<< HEAD

			File file = new File(path);

//			String path = sc.getRealPath("WEB-INF/fortuneTelling.csv");
=======
//			@WebServlet("/CSVReader")
//			//doGetとdoPostの両方を使用できるようにここで宣言
//			public class CSVReader extends HttpServlet {
//				protected void doGet(HttpServletRequest request, HttpServletResponse response)
//						throws ServletException, IOException {
//					doPost(request, response);
//				}
//		    ServletContext sc =request.getContextPath();
//			String path = sc.getRealPath("WEBINF/fortuneTelling.csv");
			File file = new File("/Applications/Eclipse_2019-09.app/Contents/workspace/office_training_Servlet_JSP/WebContent/WEB-INF/fortuneTelling.csv");
>>>>>>> branch 'master' of https://github.com/MizukiOchi/office_training_Servlet_JSP.git
			//①ー３、CSVファイルを１行ずつ読み込む
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String data = "";
			int x = 1;
			// contentsというString型の箱をwhile文の外で作成しておく（※箱は１つで良いため）
			String[] contents;
			// CSVファイルの２行目から読み込む(不要な一行目をループ前に読み込んでいる。)
			bufferedReader.readLine(); //←１行ずつ読み込むメソッド

			//読み込んだ値をセットするための変数を用意しておく。
			int unsei = 0;
			String wish = "";
			String business = "";
			String study = "";
			String ochi = "越智";

			while ((data = bufferedReader.readLine()) != null) {
				// csvファイルの値を分解する
				contents = data.split(",");
				//omikujiテーブルに代入するために値をセットする
				unsei = JudgeUnseiCode(contents[0]); //←JudgeUnseiCodeメソッドで文字列をswitch文を利用して数字に変換したものを使用。（fortune_idと紐づけるため。）
				wish = contents[1];
				business = contents[2];
				study = contents[3];

				//SQLの実行（csvファイルで読み込んだデータをInsert文にセットしてomikujiテーブルに登録する処理。）
				//DBに書き込むSQL文を変数sqlに代入する。
				String sql = "INSERT INTO omikuji(omikuji_id,fortune_id, wish, business, study, changer, update_date, author, create_date) values (?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp);";
				//動的に値をセットできるメソッドを呼び出す。
				PreparedStatement ps = connection.prepareStatement(sql);
				//登録するSQL文を動的にsetする
				ps.setInt(1, x++);
				ps.setInt(2, unsei);
				ps.setString(3, wish);
				ps.setString(4, business);
				ps.setString(5, study);
				ps.setString(6, ochi);
				ps.setString(7, ochi);
				//SQL文を実行するメソッドを呼び出す。
				ps.executeUpdate(); //※executeUpdate()メソッドは、DBに登録・更新・削除をする際に使用するメソッド。
			}
			bufferedReader.close();
		} catch (SQLException e) {
			e.getMessage();
		}
	}

/**
 * ●CSVファイルから読み込んだ文字列（fortune_name:大吉・中吉など）をfortune_idの数字に変換するメソッド
 * （CSVファイルとomikujiテーブルのfortune_idとfortuneテーブルのfortune_idとを紐づけるため。）
 * @param s
 * @return unsei
 */
	// fortune_idを文字から数字に割り当てたメソッド（例：大吉の場合 → １）
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
