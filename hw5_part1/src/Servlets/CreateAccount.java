package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Data.AccountManager;

@WebServlet("/CreateAccount")
public class CreateAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		String key = request.getParameter("name");
		String value = request.getParameter("password");
		AccountManager manager = (AccountManager) getServletContext().getAttribute("AccountManager");

		boolean logged = manager.add(key, value);

		PrintWriter out = response.getWriter();
		
		out.println(write(logged, key));
	}

	private String write(boolean logged, String key) {
		String result = "<!DOCTYPE html><html><head><meta charset=\"utf-8\">";
		result += logged ? "<title>Welcome " + key + "</title>" : "<title>Create Account</title>";
		result += "</head><body>";
		result += logged ? "<h1>Welcome " + key + "</h1>" : "<h1>The Name " + key + " is Already in Use </h1>";
		
		if(!logged)
			result += "<a>Please enter another name and password.</a>"
					+ "<form action=\"CreateAccount\" method=\"post\">"
					+ "<p>User Name: <input type=\"text\" name=\"name\" /></p>"
					+ "<p>Password: <input type=\"password\" name=\"password\" />"
					+ "<input type=\"submit\" value=\"Login\" /></p></form>";
		
		result += "</body></html>";
		
		return result;
	}

}
