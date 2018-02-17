package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInterface {

	protected final String USER = "cookuser";
	protected final String PASS = "12345";
	protected final String DB_URL = "jdbc:mysql://localhost:3306/cookme";

	public DatabaseResponse select(int type, String database, String select, String where) {
		DatabaseResponse response = new DatabaseResponse();
		Statement stmt = null;
		Connection conn = null;
		boolean result = false;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Execute SQL query
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT " + select + " FROM " + database + " WHERE " + where + ";";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);

			System.out.println("+++++++++++++++++++");
			// Extract data from result set

			while (rs.next()) {
				if (type == 0) {// recipe
					response.addTitle(rs.getString("title"));
					if (select == "id,title,ingrements,rauthor,description") {
						response.addDescription(rs.getString("description"));
						response.addIngrements(rs.getString("ingrements"));
						response.addAuthor(rs.getInt("rauthor"));
					}else {
						response.addDescription("");
						response.addIngrements("");
						response.addAuthor(0);
					}
				} else { // person login
					if (select == "id,password,username,squestion,sanswer") {
						response.addSAnswer(rs.getString("sanswer"));
						response.addSQuestion(rs.getInt("squestion"));
					}else {
						response.addSAnswer("");
						response.addSQuestion(0);
					}
					response.addUserName(rs.getString("username"));
					response.addPassword(rs.getString("password"));
				}
				response.addId(rs.getInt("id"));
				result = true;
				System.out.println("test!");
			}
			// Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try

		if (!result) {
			return null;
		}
		System.out.println("return!");
		return response;
	}

	public boolean update(int type, String database, String update, String where) {
		/// TODO add

		return false;
	}

	public boolean delete(int type, String username, String password) {
		// TODO implement

		return false;
	}

}
