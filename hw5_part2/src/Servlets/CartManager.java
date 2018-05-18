package Servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DataBase.*;
import Objects.*;

/**
 * Servlet implementation class CartManager
 */
@WebServlet("/CartManager")
public class CartManager extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Product> data = (List<Product>) getServletContext().getAttribute("ProductList");
		
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		
		if(cart == null){
			cart = new Cart();
		}
		
		String id = request.getParameter("id");
		Product product = null;
		
		if(id != null){
			for(int i = 0; i < data.size(); i++)
				if(data.get(i).getId().equals(id))
					product = data.get(i);
			
			cart.changeCart(product, cart.get(product) + 1);
		}else{
			Enumeration<String> parameters = request.getParameterNames();
			
			while(parameters.hasMoreElements()){
				id = parameters.nextElement();
				
				for(int i = 0; i < data.size(); i++)
					if(data.get(i).getId().equals(id))
						product = data.get(i);
				
				cart.changeCart(product, Integer.parseInt(request.getParameter(id)));
			}
		}
		
		session.setAttribute("cart", cart);
		
		response.sendRedirect("cart.jsp");
	}
}
