package services;

import static services.Constant.TYPE_ITEM;
import static services.Constant.TYPE_INGREDIENT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static services.Constant.GET_ALL_INGREDIENTS;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/ingredientList")
@Produces(MediaType.APPLICATION_JSON)
public class RESTIngredients extends DatabaseAdapter {

	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getIngredient(@PathParam("id") int id) {
		DatabaseResponse response = null;
		Connection conn = null;
		String json = "[";
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;
			String select = "`id`,`name`";

			st = conn.prepareStatement("SELECT " + select + " FROM item WHERE id = ? ;");
			st.setInt(1, id);

			if (id == GET_ALL_INGREDIENTS) {
				st = conn.prepareStatement("SELECT " + select + " FROM item;");
			}

			response = select(TYPE_INGREDIENT, st, select);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response = null;
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		}

		if (null == response) {
			return "[]";
		}
		if (response.getIngredientsItem().size() != response.getIngredientsValue().size()) {
			System.out.println("Error+++++++++++++++++++++++++++++++++++++");
			return "[]";
		}
		for (int i = 0; i < response.getIngredientsValue().size(); i++) {
			json += "{\"id\":" + response.getIngredientsValue().get(i);
			json += ",\"name\":\"" + response.getIngredientsItem().get(i);
			json += "\"}";
			if (i + 1 < response.getIngredientsValue().size()) {
				json += ",";
			}
		}
		json += "]";
		return json;
	}

	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String getRecipes(@FormParam("where") String where) {
		DatabaseResponse response = null;
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;
			String select = "rec.title as title,fk_recipe as id,COUNT(*) as count ";

			List<String> l = new ArrayList<String>();
			where = where.replace(" ", "");
			for (String string1 : where.split("\\|\\|")) {
				if (string1.replace(" ", "").length() > 0) {
					l.add(string1);
				}
			}
			String sqlMiddle = " 0 = 1";
			int i = 1;
			for (String string2 : l) {
				if (string2.length() > 0) {
					sqlMiddle += " || fk_item = ? ";
					i++;
				}
			}
			st = conn.prepareStatement(
					"SELECT " + select + " FROM recipeitems join recipe rec on rec.id = fk_recipe WHERE " + sqlMiddle
							+ " group by fk_recipe order by count desc;");
			i = 1;
			for (String string2 : l) {
				st.setInt(i, Integer.parseInt(string2));
				i++;
			}

			response = select(TYPE_ITEM, st, select);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response = null;
		} finally {
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
		String json = "[";
		for (int i = 0; i < response.getItemCount().size(); i++) {
			json += "{\"title\":\"" + response.getTitle().get(i);
			json += "\",\"count\":" + response.getItemCount().get(i);
			json += ",\"id\":" + response.getId().get(i);
			json += "}";
			if (i + 1 < response.getItemCount().size()) {
				json += ",";
			}
		}
		json += "]";

		return json;
	}
}
