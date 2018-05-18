package assign3;

import java.sql.*;

import javax.swing.table.AbstractTableModel;

import com.mysql.jdbc.Statement;

public class DBModel extends AbstractTableModel {

	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;

	private static final int TABLE_COLUMNS = 3;
	private static final int TABLE_ROWS = 30;
	private static final String[] COL_NAMES = { "Metropolis", "Continent", "Population" };

	private ResultSet rs;
	
	private Connection con;
	
	public DBModel() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");

		con = DriverManager.getConnection("jdbc:mysql://" + server, account, password);
	}

	public void search(String metropolisField, String continentField, String populationField, boolean lessOrEqual,
			boolean exactMatch) {
		try {
			boolean whereUsed = false;
			String command = "SELECT * FROM metropolises";
			

			Statement stmt = (Statement) con.createStatement();
			stmt.executeQuery("USE " + database);

			if (metropolisField.isEmpty() && continentField.isEmpty() && populationField.isEmpty()){
				rs = stmt.executeQuery("SELECT * FROM metropolises");
				fireTableDataChanged();
			}
			
			if (exactMatch) {
				if (!metropolisField.isEmpty()) {
					if (!whereUsed) {
						command += " WHERE metropolises.metropolis = " + "\"" + metropolisField + "\"";
						whereUsed = true;
					} else {
						command += " And metropolises.metropolis = " + "\"" + metropolisField + "\"";
					}
				}
				if (!continentField.isEmpty()) {
					if (!whereUsed) {
						command += " WHERE metropolises.continent = " + "\"" + continentField + "\"";
						whereUsed = true;
					} else {
						command += " AND metropolises.continent = " + "\"" + continentField + "\"";
					}
				}
			}

			if(!populationField.isEmpty()){
				if (lessOrEqual) {
					if (!whereUsed) {
						command += " WHERE metropolises.population <= " + "\"" + Long.parseLong(populationField) + "\"";
						whereUsed = true;
					} else {
						command += " AND metropolises.population <= " + "\"" + Long.parseLong(populationField) + "\"";
					}
				} else {
					if (!whereUsed) {
						command += " WHERE metropolises.population > " + "\"" + Long.parseLong(populationField) + "\"";
						whereUsed = true;
					} else {
						command += " AND metropolises.population > " + "\"" + Long.parseLong(populationField) + "\"";
					}
				}
			}

			if (!exactMatch) {
				if (!metropolisField.isEmpty()) {
					if (!whereUsed) {
						command += " WHERE metropolises.metropolis like " + "\""  + "%" + metropolisField + "%"
								 + "\"";
						whereUsed = true;
					} else {
						command += " And metropolises.metropolis like " + "\""  + "%" + metropolisField + "%"
								+ "\"";
					}
				}
				if (!continentField.isEmpty()) {
					if (!whereUsed) {
						command += " WHERE metropolises.continent like " + "\""  + "%" + continentField + "%"
								 + "\"";
						whereUsed = true;
					} else {
						command += " AND metropolises.continent like " + "\""  + "%" + continentField + "%" 
								+ "\"";
					}
				}
			}

			command += ";";

			rs = stmt.executeQuery(command);
			
			fireTableDataChanged();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public void add(String metropolisField, String continentField, String populationField) {
		try {
			Statement stmt = (Statement) con.createStatement();
			stmt.executeQuery("USE " + database);
			if (metropolisField.isEmpty() || continentField.isEmpty() || populationField.isEmpty())
				return;
			else if (!metropolisField.isEmpty() && !continentField.isEmpty() && !populationField.isEmpty()) {
				String insert = "INSERT INTO METROPOLISES VALUES" + " " + "(" + "\"" + metropolisField + "\"" + "," + "\""
						+ continentField + "\"" + "," + populationField + ")";
				stmt.executeUpdate(insert);
				search(metropolisField, continentField, populationField, true, true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public int getColumnCount() {
		return TABLE_COLUMNS;
	}

	@Override
	public int getRowCount() {
		int count = 0;
		if (rs != null)
			try {	
				if (rs.last()) {
				  count = rs.getRow();
				  rs.beforeFirst(); 
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return count;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		try {
			if (rs != null) {
				rs.absolute(rowIndex + 1);
				result =  rs.getObject(columnIndex + 1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return COL_NAMES[columnIndex];
	}
}
