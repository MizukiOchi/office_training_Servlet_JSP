package Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InputBirthday1
 */
@WebServlet("/InputBirthday")
public class InputBirthday extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse respose)
			throws ServletException, IOException {
		respose.setContentType("text/html; charset=UTF-8");
		PrintWriter out = respose.getWriter();

		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>おみくじ</title>");
		out.println("</head>");
		out.println("<bady>");
		out.println("<h3>誕生日を入力してください。</h3>");
		out.println("<form action=\"/office_training_Servlet_JSP/ChangeToResults\" method=\"post\">");
		out.println("<input class=\"InputBirthday\" type=\"text\" id=\"birthday\" name=\"birthday\" placeholder=\"例：20210107\">");
		out.println("<input type=\"submit\" value=\"占う\"/>");
		out.println("<form>");
		out.println("</body>");
		out.println("</html>");
	}

}




