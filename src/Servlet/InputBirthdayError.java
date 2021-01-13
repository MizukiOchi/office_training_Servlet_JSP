package Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 不要クラス Servlet implementation class InputBirthdayError
 */
@WebServlet("/InputBirthdayError")
public class InputBirthdayError extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse respose) throws ServletException, IOException {
		respose.setContentType("text/html; charset=UTF-8");
	}

}
