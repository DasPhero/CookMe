package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import services.Person;

import static services.Constant.*;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class RESTPerson extends DatabaseAdapter {

	@GET
	@Path("/cookie/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCookie(@PathParam("id") int id) {

		String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(20);
		for (int i = 0; i < 20; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		String cookie = sb.toString();
		return cookie;
	}

	@GET
	@Path("/username/{uuid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUsername(@PathParam("uuid") String uuid) {
		
		DatabaseResponse response = null;
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"SELECT id, username FROM person WHERE cookie = ? ;");
			st.setString(1, uuid);
			
			response = select(TYPE_USERNAME, st, "id,username");
			
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
		String userName = response.getUsername();
		return userName;
	}

	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCookie(@FormParam("id") int id, @FormParam("cookie") String cookie) {
		Boolean responseOK = false;
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement(	"update person set cookie = ? WHERE id = ? ;");
			st.setString(1, cookie);
			st.setInt(2, id);
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
		if (!responseOK) {
			System.out.println("Error!!!!!");
			return "Das Objekt ist nicht Vorhanden in der DB.";
		} else
			return "Success";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	// @Produces(MediaType.TEXT_PLAIN)
	public Person loginPerson(@FormParam("password") String password, @FormParam("username") String userName) {
		DatabaseResponse response = null;
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			String select = "id,password,username";
			st = conn.prepareStatement(	"SELECT "+select+" FROM person WHERE username = ? && password = ? ;");
			st.setString(1, userName);
			st.setString(2, password);
			
			response = select(TYPE_PERSON_LOGIN, st, select);
			
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
		
		Person t;
		if (null == response) {
			t = new Person();
			t.setId(-1);
			return t;
		} else {
			List<Person> list1 = response.toPersonList();
			t = list1.get(0);
			return t;
		}
	}

	@DELETE
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteTPerson(@FormParam("password") String password, @FormParam("username") String userName) {
		System.out.println("DELETE----------");
		if (delete(TYPE_PERSON_LOGIN, userName, password)) {
			return "Success";
		} else
			return "Das Objekt ist nicht Vorhanden in der DB.";
	}

}
