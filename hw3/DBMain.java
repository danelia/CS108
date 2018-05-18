package assign3;

import java.sql.SQLException;

public class DBMain {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		DBModel model = new DBModel();
		DBView view = new DBView(model);
		DBController controller = new DBController(model, view);
	}
}
