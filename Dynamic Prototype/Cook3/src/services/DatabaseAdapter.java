package services;

import static services.Constant.TYPE_CATEGORY;
import static services.Constant.TYPE_PERSON_LOGIN;
import static services.Constant.TYPE_RECIPE;
import static services.Constant.TYPE_FAVOURITES;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseAdapter {

	private final String USER = "cookme";
	private final String PASS = "12345";
	private final String DB_URL = "jdbc:mysql://192.168.3.3:3307/cookme";

	private DatabaseResponse getIngredients(String select, String where) {
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
			String sql = "SELECT " + select + " WHERE " + where + ";";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);

			// Extract data from result set
			while (rs.next()) {
				//response.addi(rs.getString("title"));
				response.addIngredientsItem(rs.getString("item"));
				response.addIngredientsValue(rs.getInt("value"));
				response.addIngredientsUnit(rs.getString("unit"));
				result = true;
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
		return response;

	}
	
	
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
			String sql = "SELECT " + select + " FROM " + database + " WHERE " + where + ";";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);

			// Extract data from result set
			while (rs.next()) {
				response.addId(rs.getInt("id"));
				if (type == TYPE_RECIPE) {// recipe
					response.addTitle(rs.getString("title"));
					response.addCategoryId(rs.getInt("category"));
					if (select == "id,title,rauthor,description,category,nutritionfacts") {
						response.addDescription(rs.getString("description"));
						DatabaseResponse ingredientsResponse = getIngredients("recipeitems.id,fk_recipe,recipeitems.value,u.name as unit,i.name as item FROM `recipeitems` " + 
								"join unit u on u.id = fk_unit join item i on i.id = fk_item",  "fk_recipe="+rs.getInt("id"));
						if (null != ingredientsResponse ) {
							response.addIngredientsItemList(ingredientsResponse.getIngredientsItem());
							response.addIngredientsUnitList(ingredientsResponse.getIngredientsUnit());
							response.addIngredientsValueList(ingredientsResponse.getIngredientsValue());
						}else {
							response.addIngredientsItemList( new ArrayList<String>() );
							response.addIngredientsUnitList( new ArrayList<String>() );
							response.addIngredientsValueList( new ArrayList<Integer>() );
						}
						
						response.addNutritionFacts(rs.getString("nutritionfacts"));
						response.addAuthor(rs.getInt("rauthor"));
					}else {
						response.addDescription("");
						response.addIngredientsItem("");
						response.addIngredientsUnit("");
						response.addIngredientsValue(0);
						response.addAuthor(0);
						response.addNutritionFacts("");
					}
				} else if (type == TYPE_PERSON_LOGIN){ // person login
					if (select == "id,password,username,squestion,sanswer") {
						response.addSAnswer(rs.getString("sanswer"));
						response.addSQuestion(rs.getInt("squestion"));
					}else {
						response.addSAnswer("");
						response.addSQuestion(0);
					}
					if (select.contains("cookie")) {
						response.addCookie(rs.getString("cookie"));
					}else {
						response.addCookie("");
					}
					if (select == "`id`,`username`,`cookie`") {
						response.addPassword("");
					}else {
						response.addPassword(rs.getString("password"));	
					}
					response.addUserName(rs.getString("username"));
				} else if (type == TYPE_CATEGORY) { //categories
					response.addCategoryName(rs.getString("categoryname"));
				} else if (type == TYPE_FAVOURITES) {
					response.setFavourites(rs.getString("favourites"));
				}
				
				result = true;
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
		return response;
	}

	public boolean update(int type, String database, String update, String where) {
		/// TODO add

		Statement stmt = null;
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Execute SQL query
			stmt = conn.createStatement();
			String sql = "UPDATE "+ database + " SET "+ update + " WHERE " +where+ " ;";
			//sql = "INSERT INTO `recipe` (`title`,`description`,`ingredients`,`category`) VALUES ( '"+ s +"','Beschreibungstext','Zutaten',2);";
			//System.out.println(sql);
			stmt.executeUpdate(sql);
			// Clean-up environment
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
		return true;
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
			String sql = "INSERT INTO  " + insert + " VALUES( " + values + " );";
			//String s = "Test2";
			//sql = "INSERT INTO `recipe` (`title`,`description`,`ingredients`,`category`) VALUES ( '"+ s +"','Beschreibungstext','Zutaten',2);";
			stmt.executeUpdate(sql);

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
		return true;
	}
	
}
