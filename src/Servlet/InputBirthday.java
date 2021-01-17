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
 * Servlet implementation class InputBirthday
 */
@WebServlet("/InputBirthday")
public class InputBirthday extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html; charset=UTF-8");
//		List<String> ErrorMessageList = new ArrayList<String>();
		//listで受け取って入っている分だけ回す（エラーが増えてもリクエストの処理を増やさなくて済む）
		List<String> ErrorMessageList = (List<String>)request.getAttribute("ErrorMessageList");

		/**htmlで記入する際は、PrintWriterクラスのgerWriter()メソッドを使用する*/
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		/**使用文字の設定*/
		out.println("<meta charset=\"UTF-8\">");
		/**タブに表示されるタイトル設定*/
		out.println("<title>おみくじ</title>");
		/**画面のデザイン設定（<head>内に設定すること）*/
		out.println("<style>");
		//共通部品
		out.println("html{");
		out.println("margin: 200px 200px 800px 800px;");
//		out.println("font-family: Tahoma;");
		out.println("background: #EEE8AA;");
		out.println("}");
		//h3で反映される部品
		out.println("h3{");
		out.println("}");
		//.errorMessage部分の設定
		out.println(".errorMessage{");
		out.println("font-size: 14px;");
		out.println("color: #FF4F50;");
		out.println("}");
		//.explain部分の設定
		out.println(".explain{");
		out.println("font-size: 50px");
		out.println("font-family: cursive;");
		out.println("color: #708090;");
		out.println("}");
		out.println(" </style>");
		out.println("</head>");

		/**画面表示の内容*/
		out.println("<bady>");
		out.println("<h3>おみくじ</h3>");
		out.println("<p class=\"explain\">Input Your Birthday Here↓</p>");
		// もしlistにerrorMessageが入っていたらエラーメッセージを全て出力する
		if (ErrorMessageList != null) {
			for(String ErrorMessage : ErrorMessageList) {
			out.println("<p class=\"errorMessage\">" + ErrorMessage + "</p>");
			}
		}
		//誕生日入力テキストと占うボタン
		out.println("<form action=\"/office_training_Servlet_JSP/ChangeToResults\" method=\"post\">");
		out.println("<input class=\"InputBirthday\" type=\"text\" id=\"birthday\" name=\"birthday\" placeholder=\"例：20210107\">");
		out.println("<input type=\"submit\" value=\"占う\"/>");
		out.println("<form>");
		out.println("<br>");
		//画像の挿入
		out.println("<img src=\"/office_training_Servlet_JSP/img/panda.jpg\">");
		out.println("</body>");
		out.println("</html>");
	}
}
