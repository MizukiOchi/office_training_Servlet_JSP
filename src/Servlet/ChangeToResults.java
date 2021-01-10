package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChangeToResults
 */
@WebServlet("/ChangeToResults")
public class ChangeToResults extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		DBに接続するメソッドを入れる


//結果を送る

		request.getRequestDispatcher("/JSP/OmikujiResults.jsp").forward(request, response);
	}

}
