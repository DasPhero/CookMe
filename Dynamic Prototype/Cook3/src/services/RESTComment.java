package services;

import static services.Constant.TYPE_COMMENT;
import static services.Constant.TYPE_USERID;

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
	@Path("/usernametoid/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public Integer getUserIdFromName(@PathParam("username") String username){
		System.out.println(username);
		Connection conn = null;
		Integer userId = 0;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"SELECT id FROM person WHERE username = ? ;");
			st.setString(1, username);
			
			DatabaseResponse response = select(TYPE_USERID, st, "id");
			
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				response = null;
			} // end finally try
			
			if ( null == response) {
				return 0;
			}
			userId = response.getUserId();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
		}
		return userId;
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void updateComments(   @FormParam("id") String recipeId, 
								  @FormParam("user") String userId, 
								  @FormParam("comment") String comment, 
								  @FormParam("time") Long time ){
		Boolean responseOK = false;

		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
			String inserts = "comments (author, comment, time, recipe)";
			String values = userId + ", '" + comment + "', " + time.toString() + ", " + recipeId; 
			
			responseOK = insert(TYPE_COMMENT, inserts, values);
			
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
	}
}
