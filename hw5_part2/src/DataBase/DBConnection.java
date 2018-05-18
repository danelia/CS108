package DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import Objects.*;

public class DBConnection {

	private static final int NUM_OF_INF = 4;

	public List<Product> getProductList() {
		List<Product> res = new ArrayList<Product>();

		connection conn = new connection();

		conn.call();
		conn.sendStatement("SELECT * FROM products");

		while (conn.isNext()) {
			String[] data = new String[4];
			for(int i = 1; i <= NUM_OF_INF; i++)
				data[i - 1] = conn.getString(i);
			
			res.add(new Product(data[0], data[1], data[2], data[3]));
		}

		conn.close();
		
		return res;
	}

	private class connection {

		private String server = MyDBInfo.MYSQL_DATABASE_SERVER;
		private String username = MyDBInfo.MYSQL_USERNAME;
		private String password = MyDBInfo.MYSQL_PASSWORD;
		private String database = MyDBInfo.MYSQL_DATABASE_NAME;

		private Connection con;

		private ResultSet rs;

		private Statement statement;

		public void call() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://" + server, username, password);

				Statement stmt = (Statement) con.createStatement();
				stmt.executeQuery("USE " + database);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		public void sendStatement(String statementString) {
			try {
				statement = (Statement) con.createStatement();
				rs = statement.executeQuery(statementString);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		public boolean isNext() {
			boolean isThereNext = false;
			try {
				isThereNext = rs.next();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return isThereNext;
		}

		public String getString(int index) {
			String res = null;

			try {
				res = rs.getString(index);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return res;
		}

		public void close() {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
