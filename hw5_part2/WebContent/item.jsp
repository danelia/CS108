<%@ page language="java" contentType="text/html; utf-8"
    pageEncoding="utf-8" %>
<%@ page import= "java.util.*, Objects.*, DataBase.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	//change this to your path
	String path = "file://C:/Users/jiiok/jiio/Programming/OOP/workspace/hw5_part2/store-images/";

	List<Product> data = (List<Product>) getServletContext().getAttribute("ProductList");
	Product product = null;
	
	for(int i = 0; i < data.size(); i++){
		if(data.get(i).getId().equals(request.getParameter("id"))){
			product = data.get(i);
		}
	}
	
	String imgPath = path + product.getImg();
	
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
		<title><%= product.getName() %></title>
	</head>
	<body>
	<form action = "./CartManager" method = "post" >
		
		<input name = "id" type = "hidden" value="<%= product.getId() %>">
		<h1><%= product.getName() %></h1>
		
		<img src = <%= imgPath %> />
		
		<p>$<%= product.getPrice() %>
				<input type = "submit" value = "Add to Cart" /></p>
		
		</form>
	
	</body>
</html>