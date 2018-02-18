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
		
		System.out.println("id: "+ id);
		String where = "id = " + id;
		String select = "id,title,ingrements,rauthor,description,category";
		String database="cookme.recipe";
		int type = 0;
		if (id == -1) {
			where = "id = id";
			select = "id,title,category";
		}
		if (id == -3) {
			where = "id = id";
			select = "id,categoryname";
			database= "cookme.categories";
			type = 3;
		}
		 DatabaseResponse responce =select(type,database, select, where);
		 if (responce == null) {
			 return "empty";
		 }
		 //for (String title : responce.getTitle()) {
		//	System.out.println("----------"+ title);
		//}
		 JsonArray recipesJson = new JsonArray();
		 if (id == -3) {
			 List<RecipeCategory> list= responce.toRecipeCategoryList();
			 if (list.isEmpty()) {
				 System.out.println("Kategorie nicht vorhanden:");
				 return "empty";
			 }
			 			
			 for (RecipeCategory recipeCategory : list) {
				 System.out.println("name:" + recipeCategory.getCategoryName() + "+++++++++++++2");
				 JsonObject rJson = new JsonObject();
				 rJson.addProperty("name", recipeCategory.getCategoryName());
				 rJson.addProperty("id", recipeCategory.getId());
				 recipesJson.add(rJson);
			}
		 }else {
		 
		 List<Recipe> list= responce.toRecipeList();
		 if (list.isEmpty()) {
			 System.out.println("Rezept nicht vorhanden:");
			 return "empty";
		 }
		 
		
		 for (Recipe recipe : list) {
			 System.out.println("title:" + recipe.getTitle() + "+++++++++++++2");
			 JsonObject rJson = new JsonObject();
			 rJson.addProperty("title", recipe.getTitle());
			 rJson.addProperty("id", recipe.getId());
			 rJson.addProperty("category", recipe.getCategoryId());
			 if(id != -1) {
				 rJson.addProperty("description", recipe.getDescription());
			 }
			 recipesJson.add(rJson);
		}
		 }
		 System.out.println(recipesJson.toString());

		return recipesJson.toString();
	}

	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePerson(@FormParam("id") int id, @FormParam("username") String userName) {
		System.out.println(id);
		if (!update(1, "cookme.person", "username", "username = 'patrick'")) {
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
		if (select(1, "cookme.person", "id,password,username", where) == null) {
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
