package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static services.Constant.TYPE_PERSON_LOGIN;
import static services.Constant.TYPE_CATEGORY;
import static services.Constant.TYPE_RECIPE;

public class DatabaseInterface {

	private final String USER = "cookme";
	private final String PASS = "12345";
	private final String DB_URL = "jdbc:mysql://192.168.3.3:3307/cookme";

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
				if (type == TYPE_RECIPE) {// recipe
					response.addTitle(rs.getString("title"));
					response.addCategoryId(rs.getInt("category"));
					if (select == "id,title,ingredients,rauthor,description,category") {
						response.addDescription(rs.getString("description"));
						response.addIngredients(rs.getString("ingredients"));
						response.addAuthor(rs.getInt("rauthor"));
					}else {
						response.addDescription("");
						response.addIngredients("");
						response.addAuthor(0);
					}
					
				} else if (type == TYPE_PERSON_LOGIN){ // person login
					if (select == "id,password,username,squestion,sanswer") {
						response.addSAnswer(rs.getString("sanswer"));
						response.addSQuestion(rs.getInt("squestion"));
					}else {
						response.addSAnswer("");
						response.addSQuestion(0);
					}
					response.addUserName(rs.getString("username"));
					response.addPassword(rs.getString("password"));
				} else if (type == TYPE_CATEGORY) { //categories
					response.addCategoryName(rs.getString("categoryname"));
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

	public boolean insert(int type, String insert, String values) {
		/// TODO add

		//DatabaseResponse response = new DatabaseResponse();
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
			sql = "INSERT INTO  " + insert + " VALUES( " + values + " );";
			//String s = "Test2";
			//sql = "INSERT INTO `recipe` (`title`,`description`,`ingredients`,`category`) VALUES ( '"+ s +"','Beschreibungstext','Zutaten',2);";
			System.out.println(sql);
			int rs = stmt.executeUpdate(sql);

			System.out.println("++++++++++++++++++" + rs);
			// Extract data from result set

			//while (rs.next()) {
				/*if (type == TYPE_RECIPE) {// recipe
					response.addTitle(rs.getString("title"));
					response.addCategoryId(rs.getInt("category"));
					if (select == "id,title,ingrements,rauthor,description,category") {
						response.addDescription(rs.getString("description"));
						response.addIngrements(rs.getString("ingrements"));
						response.addAuthor(rs.getInt("rauthor"));
					}else {
						response.addDescription("");
						response.addIngrements("");
						response.addAuthor(0);
					}
					
				} else if (type == TYPE_PERSON_LOGIN){ // person login
					if (select == "id,password,username,squestion,sanswer") {
						response.addSAnswer(rs.getString("sanswer"));
						response.addSQuestion(rs.getInt("squestion"));
					}else {
						response.addSAnswer("");
						response.addSQuestion(0);
					}
					response.addUserName(rs.getString("username"));
					response.addPassword(rs.getString("password"));
				} else if (type == TYPE_CATEGORY) { //categories
					response.addCategoryName(rs.getString("categoryname"));
				}
				response.addId(rs.getInt("id"));
				result = true;
				System.out.println("test!");*/
			//}
			// Clean-up environment
			//rs.close();
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
			return false;
		}
		System.out.println("return!");
		
		return true;
	}
	
}
