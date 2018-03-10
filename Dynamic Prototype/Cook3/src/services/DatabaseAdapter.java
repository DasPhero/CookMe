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

	protected final String USER = System.getenv("DE_COOKME_API_DB_USER");
	protected final String PASS = System.getenv("DE_COOKME_API_DB_PASSWORD");
	protected final String DB_URL = System.getenv("DE_COOKME_API_DB_URL");

	private DatabaseResponse getIngredients(int id) {
		DatabaseResponse response = new DatabaseResponse();
		Connection conn = null;
		boolean result = false;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// Execute SQL query
			PreparedStatement st;

			st = conn.prepareStatement(
					"SELECT recipeitems.id,fk_recipe,recipeitems.value,u.name as unit,i.name as item FROM recipeitems join unit u on u.id = fk_unit join item i on i.id = fk_item WHERE fk_recipe = ? ;");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();

			// Extract data from result set
			while (rs.next()) {
				// response.addi(rs.getString("title"));
				response.addIngredientsItem(rs.getString("item"));
				response.addIngredientsValue(rs.getInt("value"));
				response.addIngredientsUnit(rs.getString("unit"));
				result = true;
			}
			// Clean-up environment
			rs.close();
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

	public DatabaseResponse select(int type, PreparedStatement st, String select) {
		DatabaseResponse response = new DatabaseResponse();

		boolean result = false;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

//			System.out.println(st.toString());
			ResultSet rs = st.executeQuery();

			// Extract data from result set
			while (rs.next()) {
				response.addId(rs.getInt("id"));
				System.out.println("aqwewq" + type);
				if (type == TYPE_RECIPE) {// recipe
					response.addTitle(rs.getString("title"));
					response.addCategoryId(rs.getInt("category"));
					if (select == "id,title,rauthor,description,category,nutritionfacts") {
						response.addDescription(rs.getString("description"));
						DatabaseResponse ingredientsResponse = getIngredients(rs.getInt("id"));
						if (null != ingredientsResponse) {
							response.addIngredientsItemList(ingredientsResponse.getIngredientsItem());
							response.addIngredientsUnitList(ingredientsResponse.getIngredientsUnit());
							response.addIngredientsValueList(ingredientsResponse.getIngredientsValue());
						} else {
							response.addIngredientsItemList(new ArrayList<String>());
							response.addIngredientsUnitList(new ArrayList<String>());
							response.addIngredientsValueList(new ArrayList<Integer>());
						}

						response.addNutritionFacts(rs.getString("nutritionfacts"));
						response.addAuthor(rs.getInt("rauthor"));
					} else {
						response.addDescription("");
						response.addIngredientsItem("");
						response.addIngredientsUnit("");
						response.addIngredientsValue(0);
						response.addAuthor(0);
						response.addNutritionFacts("");
					}
				} else if (type == TYPE_PERSON_LOGIN) { // person login
					if (select == "id,password,username,squestion,sanswer") {
						response.addSAnswer(rs.getString("sanswer"));
						response.addSQuestion(rs.getInt("squestion"));
					} else {
						response.addSAnswer("");
						response.addSQuestion(0);
					}
					if (select.contains("cookie")) {
						response.addCookie(rs.getString("cookie"));
					} else {
						response.addCookie("");
					}
					if (select == "`id`,`username`,`cookie`") {
						response.addPassword("");
					} else {
						response.addPassword(rs.getString("password"));
					}
					response.addUserName(rs.getString("username"));
				} else if (type == TYPE_CATEGORY) { // categories
					response.addCategoryName(rs.getString("categoryname"));
				} else if (type == TYPE_FAVOURITES) {
					response.setFavourites(rs.getString("favourites"));
				} else if (type == TYPE_SELECTION) {
					response.setSelection(rs.getString("selectedrecipes"));
				} else if (type == TYPE_ITEM) {
					response.addTitle(rs.getString("title"));
					response.addItemCount(rs.getInt("count"));
				} else if (type == TYPE_INGREDIENT) {
					response.addIngredientsItem(rs.getString("name"));
					response.addIngredientsValue(rs.getInt("id"));
				}
				 else if(type == TYPE_USERID) {
					response.setUserId(rs.getInt("id"));
					System.out.println("asd" + rs.getInt("id"));
				}

				result = true;
			}
			// Clean-up environment
			rs.close();

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

	public boolean update(int type, PreparedStatement st) {
		/// TODO add
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			st.executeUpdate();
			// Clean-up environment
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
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

		// DatabaseResponse response = new DatabaseResponse();
		Statement stmt = null;
		Connection conn = null;
		boolean result = false;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			/*
			 * if (!checkSQL(insert, values)) { System.out.println("SQL Injection!"); return
			 * false; }
			 */
			// Execute SQL query
			stmt = conn.createStatement();
			String sql = "INSERT INTO  " + insert + " VALUES( " + values + " );";
			// String s = "Test2";
			// sql = "INSERT INTO `recipe` (`title`,`description`,`ingredients`,`category`)
			// VALUES ( '"+ s +"','Beschreibungstext','Zutaten',2);";
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
