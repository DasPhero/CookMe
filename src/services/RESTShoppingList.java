package services;

import static services.Constant.TYPE_PERSON_LOGIN;
import static services.Constant.TYPE_SELECTION;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/shoppingList")
@Produces(MediaType.APPLICATION_JSON)
public class RESTShoppingList extends DatabaseAdapter {

	@GET
	@Path("/selectedItems/{cookie}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserSelection(@PathParam("cookie") String cookie){
		
		DatabaseResponse response = null;
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			String select = "`selectedrecipes`,`id`";
			st = conn.prepareStatement(	"SELECT "+select+" FROM person WHERE cookie = ? ;");
			st.setString(1, cookie);
			
			response = select(TYPE_SELECTION, st, select);
			
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				response = null;
			} // end finally try
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		}
		if (null == response) {
			return "";
		}
		String selectedRecipes = response.toSelectionString();
		return selectedRecipes;
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void updateFavourites( @FormParam("cookie") String cookie, @FormParam("selectedrecipes") String selection ){
		Boolean responseOK = false;
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"update person set selectedrecipes= ? WHERE cookie = ? ;");
			st.setString(1, selection);
			st.setString(2, cookie);
			
			responseOK = update(TYPE_PERSON_LOGIN, st);
			
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				responseOK = false;
			} // end finally try
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		}
		if(responseOK) { System.out.println("Selected recipes successfully updated."); }
		else { System.out.println("Failed updating selected recipes! cookie: " + cookie + " selectedrecipes: " + selection); };
	}
}
