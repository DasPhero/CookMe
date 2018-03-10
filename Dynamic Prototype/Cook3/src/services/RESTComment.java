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
	@Path("/usernametoid/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public Integer getCurrentUserSelection(@PathParam("username") String username){
		Connection conn = null;
		Integer userId = 0;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"SELECT id FROM person WHERE username = ? ;");
			st.setString(1, username);
			
			DatabaseResponse response = select(TYPE_COMMENT, st, "id");
			
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
	public void updateComments( @FormParam("id") Integer recipeId, 
								  @FormParam("user") Integer userId, 
								  @FormParam("comment") String comment, 
								  @FormParam("time") Integer time ){
		System.out.println("asljdklsa");
		Boolean responseOK = false;

		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;
			System.out.println("params" + recipeId + userId + comment + time);
			st = conn.prepareStatement(	"INSERT INTO comments (author, comment, time, recipe) VALUES (?, ?, ?, ?);");
			st.setInt(1, userId);
			st.setString(2, comment);
			st.setInt(3, time);
			st.setInt(4, recipeId);
			
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
		System.out.println(responseOK);
		if ( !responseOK) {
			return;
		}
	}
}
