<%@ page language="java" contentType="text/html; utf-8"
	pageEncoding="utf-8"%>
<%@ page import="java.util.*, DataBase.*, Objects.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	List<Product> data = (List<Product>) getServletContext().getAttribute("ProductList");
	Cart cart = (Cart) session.getAttribute("cart");
	double total = cart.getTotal();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Shopping Cart</title>
</head>
<body>
	<h1>Shopping Cart</h1>
	<form action="./CartManager" method="post">
		<ul>
			<%
				for (Product product : data) {
					int count = cart.get(product);
					if(count != 0)
						out.print("<li> <input type ='number' value='" + count + "' name='" + product.getId() + "'>"
								+ product.getName() + ", " + product.getPrice() + "</li>");
				}
			%>
		</ul>
		<p>
			Total:<%=total%><input type="submit" value="Update Cart" />
		</p>
	</form>

	<a href= "index.jsp" >Continue shopping</a>

</body>
</html>