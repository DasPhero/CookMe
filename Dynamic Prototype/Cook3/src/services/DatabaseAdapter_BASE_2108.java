package services;

import static services.Constant.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.PreparedStatement;

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
	
	
	private Boolean checkSQL(String select, String where) {
		if (select.contains(";") || where.contains(";")) {
			System.out.println("Achtung Angriff14");
			return false;
		}
		if (select.toLowerCase().contains("drop") || where.toLowerCase().contains("drop")) {
			System.out.println("Achtung Angriff13");
			return false;
		}
		if (select.toLowerCase().contains("delete") || where.toLowerCase().contains("delete")) {
			System.out.println("Achtung Angriff12");
			return false;
		}
		if (select.toLowerCase().contains("remove") || where.toLowerCase().contains("remove")) {
			System.out.println("Achtung Angriff12");
			return false;
		}
		if (select.toLowerCase().contains("insert") || where.toLowerCase().contains("insert")) {
			System.out.println("Achtung Angriff11");
			return false;
		}
		if (select.toLowerCase().contains("union") || where.toLowerCase().contains("union")) {
			System.out.println("Achtung Angriff10");
			return false;
		}
		if (select.toLowerCase().contains("//") || where.toLowerCase().contains("//")) {
			System.out.println("Achtung Angriff9");
			return false;
		}
		if (select.toLowerCase().contains("--") || where.toLowerCase().contains("--")) {
			System.out.println("Achtung Angriff8");
			return false;
		}
		if (select.toLowerCase().contains("version") || where.toLowerCase().contains("version")) {
			System.out.println("Achtung Angriff7");
			return false;
		}
		if (select.toLowerCase().contains("database") || where.toLowerCase().contains("database")) {
			System.out.println("Achtung Angriff6");
			return false;
		}
		if (select.toLowerCase().contains("user()") || where.toLowerCase().contains("user()")) {
			System.out.println("Achtung Angriff user");
			return false;
		}
		if (select.toLowerCase().contains("1=1") || where.toLowerCase().contains("1=1")) {
			System.out.println("Achtung Angriff and");
			return false;
		}
		if (select.toLowerCase().contains("file") || where.toLowerCase().contains("file")) {
			System.out.println("Achtung Angriff5");
			return false;
		}
		if (select.toLowerCase().contains("benchmark") || where.toLowerCase().contains("benchmark")) {
			System.out.println("Achtung Angriff4");
			return false;
		}
		if (select.toLowerCase().contains("concat") || where.toLowerCase().contains("concat")) {
			System.out.println("Achtung Angriff3");
			return false;
		}
		if (select.toLowerCase().contains("schema") || where.toLowerCase().contains("schema")) {
			System.out.println("Achtung Angriff2");
			return false;
		}
		if (select.toLowerCase().contains("hex()") || where.toLowerCase().contains("hex()")) {
			System.out.println("Achtung Angriff 1 ");
			return false;
		}
		return true;
	}
	
	public DatabaseResponse select(int type, String database, String select,String join, String where, String count) {
		DatabaseResponse response = new DatabaseResponse();
		Statement stmt = null;
		Connection conn = null;
		boolean result = false;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			/*if (!checkSQL(select, where)) {
				System.out.println("SQL Injection!");
				return null;
			}*/
			
			// Execute SQL query
			//stmt = conn.createStatement();
			//String sql = "SELECT " + select + " FROM " + database + " WHERE " + where + ";";
			//System.out.println(sql);
			
			PreparedStatement st;
			if (join == "" && count == "") {
				st = conn.prepareStatement(	"SELECT ? FROM ? WHERE ? ;");
				st.setString(1, select);
				st.setString(2, database);
				st.setString(3, where);
			}else if (count == "") {
				st = conn.prepareStatement(	"SELECT ? FROM ? JOIN ? WHERE ? ");
				st.setString(1, select);
				st.setString(2, database);
				st.setString(3, join);
				st.setString(4, where);
			}else {
				st = conn.prepareStatement(	"SELECT ? ,COUNT(*) as count  FROM ? JOIN ? WHERE ? ");
				st.setString(1, select);
				st.setString(2, database);
				st.setString(3, join);
				st.setString(4, where);
			}
			System.out.println(st.toString());
			//String sql = "SELECT " + select + " FROM " + database + " WHERE " + where + ";";
			//System.out.println(sql);
			
			
			ResultSet rs = st.executeQuery();

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
				  else if (type == TYPE_SELECTION) {
					response.setSelection(rs.getString("selectedrecipes"));
				  }
				  else if (type == TYPE_ITEM) {
					response.addTitle(rs.getString("title"));
					response.addItemCount(rs.getInt("count"));
				} else if (type == TYPE_INGREDIENT) {
					response.addIngredientsItem(rs.getString("name"));
					response.addIngredientsValue(rs.getInt("id"));
				}
				
				result = true;
			}
			// Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			System.out.println("wrong mysql statement");
			result = false;
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


	public DatabaseResponse select2(int type, PreparedStatement st,String select) {
		DatabaseResponse response = new DatabaseResponse();
		//Statement stmt = null;
		//Connection conn = null;
		boolean result = false;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			//conn = DriverManager.getConnection(DB_URL, USER, PASS);

			/*if (!checkSQL(select, where)) {
				System.out.println("SQL Injection!");
				return null;
			}*/
			
			// Execute SQL query
			//stmt = conn.createStatement();
			//String sql = "SELECT " + select + " FROM " + database + " WHERE " + where + ";";
			//System.out.println(sql);
			
			/*PreparedStatement st;
			if (join == "" && count == "") {
				st = conn.prepareStatement(	"SELECT ? FROM ? WHERE ? ;");
				st.setString(1, select);
				st.setString(2, database);
				st.setString(3, where);
			}else if (count == "") {
				st = conn.prepareStatement(	"SELECT ? FROM ? JOIN ? WHERE ? ");
				st.setString(1, select);
				st.setString(2, database);
				st.setString(3, join);
				st.setString(4, where);
			}else {
				st = conn.prepareStatement(	"SELECT ? ,COUNT(*) as count  FROM ? JOIN ? WHERE ? ");
				st.setString(1, select);
				st.setString(2, database);
				st.setString(3, join);
				st.setString(4, where);
			}
			System.out.println(st.toString());*/
			//String sql = "SELECT " + select + " FROM " + database + " WHERE " + where + ";";
			//System.out.println(sql);
			
			
			ResultSet rs = st.executeQuery();

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
				  else if (type == TYPE_SELECTION) {
					response.setSelection(rs.getString("selectedrecipes"));
				  }
				  else if (type == TYPE_ITEM) {
					response.addTitle(rs.getString("title"));
					response.addItemCount(rs.getInt("count"));
				} else if (type == TYPE_INGREDIENT) {
					response.addIngredientsItem(rs.getString("name"));
					response.addIngredientsValue(rs.getInt("id"));
				}
				
				result = true;
			}
			// Clean-up environment
			rs.close();
			//stmt.close();
			//conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			System.out.println("wrong mysql statement");
			result = false;
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			
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

			/*if (!checkSQL(update, where)) {
				System.out.println("SQL Injection!");
				return false;
			}*/
			
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

			/*if (!checkSQL(insert, values)) {
				System.out.println("SQL Injection!");
				return false;
			}*/
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
