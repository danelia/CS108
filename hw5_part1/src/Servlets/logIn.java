package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Data.*;

@WebServlet("/logIn")
public class logIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		String key = request.getParameter("name");
		String value = request.getParameter("password");
		AccountManager manager = (AccountManager) getServletContext().getAttribute("AccountManager");

		boolean logged = manager.check(key, value);

		PrintWriter out = response.getWriter();
		
		out.println(write(logged, key));
	}

	private String write(boolean logged, String key) {
		String result = "<!DOCTYPE html><html><head><meta charset=\"utf-8\">";
		result += logged ? "<title>Welcome " + key + "</title>" : "<title>Information Incorrect</title>";
		result += "</head><body>";
		result += logged ? "<h1>Welcome " + key + "</h1>" : "<h1>Please Try Again </h1>";
		
		if(!logged)
			result += "<a>Either your user name or your password is incorrect. Please try again.</a>"
					+ "<form action=\"logIn\" method=\"post\">"
					+ "<p>User Name: <input type=\"text\" name=\"name\" /></p>"
					+ "<p>Password: <input type=\"password\" name=\"password\" />"
					+ "<input type=\"submit\" value=\"Login\" /></p></form>"
					+ "<a href = \"CreateAccount.html\">Create Account</a>";
		
		result += "</body></html>";
		
		return result;
	}
}
