package services;

import static services.Constant.TYPE_COMMENT;

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

@Path("/commentary")
@Produces(MediaType.APPLICATION_JSON)
public class RESTComment extends DatabaseAdapter {
	
	@GET
	@Path("/{recipeId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserSelection(@PathParam("recipeId") Integer recipeId){
		String userSelection="";
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"SELECT author,comment FROM comments WHERE id = ? ;");
			st.setInt(1, recipeId);
			
			DatabaseResponse response = select(TYPE_COMMENT, st, "author,comment");
			
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				response = null;
			} // end finally try
			
			if ( null == response) {
				return "";
			}
			userSelection = response.toSelectionString();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
		}
		return userSelection;
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void updateFavourites( @FormParam("id") Integer id, 
								  @FormParam("user") String username, 
								  @FormParam("comment") String comment, 
								  @FormParam("time") Integer time ){
		//String updateInformation = "";
		//String context = "";
		Boolean responseOK = false;
		//TODO implement code
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"Update set ? FROM comments WHERE id = ? ;");
			//st.setInt(1, recipeId);
			
			responseOK = update(TYPE_COMMENT, st);
			
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				responseOK = false;
			} // end finally try
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
		}
		if ( !responseOK) {
			return;
		}
		
		//update(TYPE_COMMENT,"cookme.person", updateInformation, context);
	}
}
