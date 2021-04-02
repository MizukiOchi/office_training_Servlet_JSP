package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * メソッドの説明：
 * 最初の出力画面。javaで記載しているが、jspの役割。
 *
 * Servlet implementation class InputBirthday
 */
@WebServlet("/InputBirthday")
//doGetとdoPostの両方を使用できるようにここで宣言
public class InputBirthday extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html; charset=UTF-8");
		// 入力チェックで引っかかったエラー全てを入れるためのlistを用意
		List<String> ErrorMessageList = (List<String>) request.getAttribute("ErrorMessageList");
		//ここで入力した誕生日の値をChangeToResults.javaクラス(サーブレット)に渡すためにsetしておく
		String birthday = (String) request.getAttribute("birthday");

		//	htmlで記入する際は、PrintWriterクラスのgerWriter()メソッドを使用する
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		/** 使用文字の設定 */
		out.println("<meta charset=\"UTF-8\">");
		/** タブに表示されるタイトル設定 */
		out.println("<title>おみくじ</title>");

		/** ↓↓↓画面のデザイン設定（<head>内に設定すること）↓↓↓ */
		out.println("<style>");
		// 共通部品
		out.println("body{");
		out.println("margin-left: 950px;");
		out.println("margin-top: 200px;");
		out.println("background: #EEE8AA;");
		out.println("}");
		// h3で反映される部品
		out.println("title{");
		out.println("text-align: center;");
		out.println("}");
		//InputImage部分の設定
		out.println(".InputImage{");
		out.println("width: 500px;");
		out.println("height: 500px;");
		out.println("}");
		// .balloon部分の設定
		out.println(".balloon{");
		out.println("position: relative;");
		out.println("display: inline-block;");
		out.println("margin-left: -900px;");
		out.println("margin-top: -150px;");
		out.println("margin-bottom: -300px;");
		out.println("width: 300px;");
		out.println("height: 150px;");
		out.println("text-align: center;");
		out.println("padding:50px 0;");
		out.println("color: #FFF;");
		out.println("font-family: fantasy;");
		out.println("font-size: 25px;");
		out.println("font-weight: bold;");
		out.println("background: #778899;");
		out.println("border-radius: 50%;");
		out.println("box-sizing: border-box;");
		out.println("}");

		out.println(".balloon:before {");
		out.println("content:\"\";");
		out.println("position: absolute;");
		out.println("bottom: -25px;");
		out.println("left: 50%;");
		out.println("margin-left: -15px;");
		out.println("border: 15px solid transparent;");
		out.println("border-top: 15px solid #778899;");
		out.println("z-index: 0;");
		out.println("}");

		out.println(".balloon p {");
		out.println("margin: 0;");
		out.println("padding: 0;");
		out.println("text-align: center;");
		out.println("color: #fff;");
		out.println("line-height: 90px !important;");
		out.println("}");
		/**画像の挿入*/
		// .panda画像の設定
		out.println(".panda{");
		out.println("margin-left: -900px;");
		out.println("margin-top: 100px;");
		out.println("width: 300px;");
		out.println("height: 300px;");
		out.println("}");
		/**InputBirthdayの全体*/
		// InputBirthday部分の設定
		out.println(".InputBirthday{");
		out.println("float: left;");
		out.println("}");
		/**エラー文のデザイン*/
		// .errorMessage部分の設定
		out.println(".errorMessage{");
		out.println("font-size: 14px;");
		out.println("color: #CD5C5C;");
		out.println("}");
		/**説明文のデザイン*/
		// .explain部分の設定
		out.println(".explain{");
		out.println("font-size: 50px");
		out.println("font-family: cursive;");
		out.println("color: #708090;");
		out.println("}");
		out.println(" </style>");
		out.println("</head>");
		/**↑↑↑デザイン終了↑↑↑*/

		/** ↓↓↓画面表示の内容↓↓↓ */
		out.println("<bady>");
		out.println("<h3 id=\"title\">おみくじ</h3>");
		out.println("<div class=\"InputBirthday\">");
		out.println("<p class=\"explain\">Input Your Birthday Here↓</p>");
		/**エラーメッセージの処理*/
		//上で用意したlist（ErrorMessageList）にエラーメッセージが入っていたら、入っている分だけ回してエラーメッセージを出力する処理。
		//（listに入れることでエラーが増えてもリクエストの処理を増やさなくて済むため）
		if (ErrorMessageList != null) {
			for (String ErrorMessage : ErrorMessageList) {
				out.println("<p class=\"errorMessage\">" + ErrorMessage + "</p>");
			}
		}
		/**ChangeToResults.java（サーブレット）クラスに飛ぶための処理*/
		out.println("<form action=\"/office_training_Servlet_JSP/ChangeToResults\" method=\"post\">");
		/**誕生日が入っていなければ空にする処理
		 * （空にしておかないと初めからnullが表示されるため）
		 */
		if (birthday == null) {
			birthday = "";
		}
		/**誕生日入力テキストの設定*/
		out.println("<input type=\"text\" id=\"birthday\" name=\"birthday\" placeholder=\"例：20210107\" value=" + birthday + ">");
		/**「占う」ボタンの設定*/
		out.println("<input class=\"button\" type=\"submit\" value=\"占う\"/>");
		out.println("</form>");
		out.println("</div>");

		/** 吹き出しと画像の追加処理*/
		out.println("<div class=\"InputImage\">");
		// 吹き出しの挿入
		out.println("<div class=\"balloon\">");
		out.println("今日のあなたの運勢は...");
		out.println("</div>");
		// 画像の挿入
		out.println("<div>");
		out.println("<img class=\"panda\" src=\"/office_training_Servlet_JSP/img/panda.jpg\">");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		/** ↑↑↑画面表示の内容↑↑↑ */

		out.println("</html>");
	}
}
