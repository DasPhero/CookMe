package services;

import static services.Constant.TYPE_RECIPE_ID;
import static services.Constant.TYPE_FAVOURITES;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/favourization")
@Produces(MediaType.APPLICATION_JSON)
public class RESTFavourizationService extends DatabaseAdapter {

	@GET
	@Path("/favourizedItems/{cookie}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserFavourites(@PathParam("cookie") String cookie) {
		String selection = "`favourites`,`id`";
		String favourites ="";

		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement("SELECT "+selection+" FROM person WHERE cookie = ? ;");
			st.setString(1, cookie);

			DatabaseResponse response = select(TYPE_FAVOURITES, st, selection);
			if (null == response) {
				return "";
			}
			favourites=response.toFavouritesString();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return "";
		} finally {

		}
		return favourites;
	}

	@GET
	@Path("/recipeId/{recipeName}")
	@Produces(MediaType.TEXT_PLAIN)
	public Integer getCurrentRecipeId(@PathParam("recipeName") String recipeName) {
		String selection = "`id`";
		Integer id = 0; 
		
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement("SELECT "+selection+" FROM recipe WHERE title = ? ;");
			st.setString(1, recipeName);

			DatabaseResponse response = select(TYPE_RECIPE_ID, st, selection);
			if (null == response) {
				return 0;
			}
			id= response.toRecipeId();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {

		}		
		return id;
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void updateFavourites(@FormParam("cookie") String cookie, @FormParam("favourites") String favourites) {
		String updateInformation = "favourites=\"" + favourites + "\"";
		String context = "cookie=\"" + cookie + "\"";
		Boolean done = update(TYPE_FAVOURITES, "cookme.person", updateInformation, context);
		if (done) {
			System.out.println("Favourites successfully updated.");
		} else {
			System.out.println("Failed updating favourites! cookie: " + cookie + " favourites: " + favourites);
		}
		;
	}

	@PUT
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
