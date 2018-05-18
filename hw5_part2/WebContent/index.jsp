<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
<%@ page import="java.util.*, DataBase.*, Objects.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
		<title>Student Store</title>
	</head>
	<body>
		
		<h1>Student Store</h1>
		<a>Items Available:</a>
		
		<ul>
			<%
				List<Product> list = (List<Product>) getServletContext().getAttribute("ProductList");
				String url = "\"item.jsp?";
				
				for (int i = 0; i < list.size(); i ++) {
					Product product = list.get(i);
					out.println("<li> <a href=" + url + "id=" + product.getId() + "\">" + product.getName() + "</a></li>");
				}
			%>
		</ul>
		
	</body>
</html>