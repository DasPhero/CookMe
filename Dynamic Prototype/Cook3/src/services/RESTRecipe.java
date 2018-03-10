package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import services.Recipe;
import static services.Constant.GET_NAV_TITLES;
import static services.Constant.GET_CATEGORIES;

import static services.Constant.TYPE_CATEGORY;
import static services.Constant.TYPE_RECIPE;

@Path("/recipe")
@Produces(MediaType.APPLICATION_JSON)
public class RESTRecipe extends DatabaseAdapter {

	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getRecipe(@PathParam("id") int id) {
		System.out.println("url=" + DB_URL);
		String select = "id,title,rauthor,description,category,nutritionfacts";
		int type = TYPE_RECIPE;
		Connection conn = null;
		DatabaseResponse response = null;
		// Register JDBC driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			PreparedStatement st;

			st = conn.prepareStatement("SELECT " + select + " FROM recipe WHERE id = ? ;");
			st.setInt(1, id);
			if (id == GET_NAV_TITLES) {
				select = "id,title,category ";
				st = conn.prepareStatement("SELECT " + select + " FROM recipe ;");
			}
			if (id == GET_CATEGORIES) {
				select = "id,categoryname ";
				st = conn.prepareStatement("SELECT " + select + " FROM categories ;");
				type = TYPE_CATEGORY;
			}
			System.out.println(st.toString());
			response = select(type, st, select);

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "empty";

		} finally {
			// finally block used to close resources
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try

		if (response == null) {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
			return "empty";
		}
		JsonArray recipesJson = new JsonArray();
		if (id == GET_CATEGORIES) {
			List<RecipeCategory> list = response.toRecipeCategoryList();
			if (list.isEmpty()) {
				System.out.println("Kategorie nicht vorhanden:");
				return "empty";
			}

			for (RecipeCategory recipeCategory : list) {
				JsonObject rJson = new JsonObject();
				rJson.addProperty("name", recipeCategory.getCategoryName());
				rJson.addProperty("id", recipeCategory.getId());
				recipesJson.add(rJson);
			}
		} else {
			List<Recipe> list = response.toRecipeList();
			if (list.isEmpty()) {
				System.out.println("Rezept nicht vorhanden:");
				return "empty";
			}

			for (Recipe recipe : list) {
				JsonObject rJson = new JsonObject();
				rJson.addProperty("title", recipe.getTitle());
				rJson.addProperty("id", recipe.getId());
				rJson.addProperty("category", recipe.getCategoryId());
				if (id != GET_NAV_TITLES) {
					rJson.addProperty("description", recipe.getDescription());
					rJson.addProperty("ingredients", convertJSON(recipe));
					rJson.addProperty("nutritionfacts", recipe.getNutritionFacts());
				}
				recipesJson.add(rJson);
			}
		}
		System.out.println(recipesJson.toString());
		return recipesJson.toString();
	}

	private String convertJSON(Recipe recipe) {
		String json = "[";
		for (int i = 0; i < recipe.getIngredientsItem().size(); i++) {
			json += "{\"item\":\"";
			json += recipe.getIngredientsItem().get(i);
			json += "\",\"unit\":\"";
			json += recipe.getIngredientsUnit().get(i);
			json += "\",\"value\":";
			json += recipe.getIngredientsValue().get(i);
			json += "}";
			if (i + 1 < recipe.getIngredientsItem().size()) {
				json += ",";
			}
		}
		json += "]";
		return json;
	}

	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePerson(@FormParam("id") int id, @FormParam("username") String userName) {
		if (!update(1, "cookme.person", "username", "username = 'patrick'")) {
			return "Das Objekt ist nicht Vorhanden in der DB.";
		} else
			return "Success";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String insertRecipe(@FormParam("title") String title, @FormParam("ingredients") String ingredients,
			@FormParam("description") String description, @FormParam("category") String category) {
		String insert = " `recipe` ( `title`,`description`,`category`) ";
		String values = "'" + title + "', '" + description + "','" + ingredients + "'," + category + "";
		if (!insert(TYPE_RECIPE, insert, values)) {
			// dbs.insert(t);
			return "Error wrong username or password";
		} else
			return "Login sucessfull";
	}

	@DELETE
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteTPerson(@FormParam("password") String password, @FormParam("username") String userName) {
		System.out.println("DELETE----------");
		if (delete(1, userName, password)) {
			return "Success";
		} else
			return "Das Objekt ist nicht Vorhanden in der DB.";
	}

}
