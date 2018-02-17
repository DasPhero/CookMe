package services;

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


@Path("/recipe")
@Produces(MediaType.APPLICATION_JSON)
public class RESTRecipe extends DatabaseInterface {

	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getRecipe(@PathParam("id") int id) {
		String where = "id = " + id;
		String select = "id,title,ingrements,rauthor,description";
		if (id == -1) {
			where = "id = id";
			select = "id,title";
		}
		 
		 DatabaseInterface dbi = new DatabaseInterface();
		 DatabaseResponse responce =dbi.select(0,"cookme.recipe", select, where);
		 if (responce == null) {
			 return "empty";
		 }
		 for (String title : responce.getTitle()) {
			System.out.println("----------"+ title);
		}
		 List<Recipe> list= responce.toRecipeList();
		 if (list.isEmpty()) {
			 System.out.println("Rezept nicht vorhanden:");
			 return "empty";
		 }
		 JsonArray recipesJson = new JsonArray();
		 for (Recipe recipe : list) {
			 System.out.println("titel:" + recipe.getTitle() + "+++++++++++++2");
			 JsonObject rJson = new JsonObject();
			 rJson.addProperty("titel", recipe.getTitle());
			 rJson.addProperty("id", recipe.getId());
			 recipesJson.add(rJson);
		}
		 
		 System.out.println(recipesJson.toString());
		 
		String s = list.get(0).getTitle();
		System.out.println("Rezept:\n" + s);
		return recipesJson.toString();
	}

	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePerson(@FormParam("id") int id, @FormParam("username") String userName) {
		System.out.println(id);
		DatabaseInterface dbi = new DatabaseInterface();
		if (!dbi.update(1, "cookme.person", "username", "username = 'patrick'")) {
			System.out.println("Error!!!!!");
			return "Das Objekt ist nicht Vorhanden in der DB.";
		} else
			return "Success";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String loginPerson(@FormParam("password") String password, @FormParam("username") String userName) {
		System.out.println("POST----------");
		String where = "username = '" + userName + "' && password = '" + password + "'";
		DatabaseInterface dbi = new DatabaseInterface();
		if (dbi.select(1, "cookme.person", "id,password,username", where) == null) {
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
		DatabaseInterface dbi = new DatabaseInterface();
		if (dbi.delete(1, userName, password)) {
			return "Success";
		} else
			return "Das Objekt ist nicht Vorhanden in der DB.";
	}

}
