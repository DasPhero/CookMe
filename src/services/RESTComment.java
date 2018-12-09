package services;

import static services.Constant.TYPE_COMMENT;
import static services.Constant.TYPE_USERID;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
	
	@GET
	@Path("/comments/{recipeId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllCommentsOfRecipeId(@PathParam("recipeId") Integer recipeId){
		Connection conn = null;
		String jsonString = "[]";
		
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"SELECT p.username, c.comment, c.id FROM person p JOIN comments c ON p.id = c.author WHERE c.recipe = ? ORDER BY c.time;");
			st.setInt(1, recipeId);
			
			DatabaseResponse response = select(TYPE_COMMENT, st, "username, comment, id");
			try {
			List<String> commentList = response.toCommentTextList();
			List<String> authorList = response.toCommentAuthorList();
			List<Integer> idList = response.toCommentIdsList();
			
			jsonString = "[";
			int index = 0;
			for(; index < idList.size() - 1; index++) {
				jsonString += "{ \"comment\": \"" + commentList.get(index) + "\", \"author\": \"" + authorList.get(index) + "\" },";
			}
			jsonString += "{ \"comment\": \"" + commentList.get(index) + "\", \"author\": \"" + authorList.get(index) + "\" }]";
			
			}
			catch (Exception e){ 
				try {
					if (conn != null)
						conn.close();
				} catch (SQLException se) {
					se.printStackTrace();
					response = null;
				}
				return jsonString; 
			}
			
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				response = null;
			} // end finally try
			
			if ( null == response) {
				return jsonString;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
		}
		return jsonString;
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
			
			PreparedStatement st;
			System.out.println("params" + recipeId + userId + comment + time);
			st = conn.prepareStatement(	"INSERT INTO comments (author, comment, time, recipe) VALUES (?, ?, ?, ?);");
			st.setInt(1, Integer.parseInt(userId));
			st.setString(2, comment);
			st.setLong(3, time);
			st.setInt(4, Integer.parseInt(recipeId));	
			responseOK = insert(st);

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
