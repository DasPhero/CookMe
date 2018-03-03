package services;

import static services.Constant.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/shoppingList")
@Produces(MediaType.APPLICATION_JSON)
public class RESTShoppingList extends DatabaseAdapter {

	@GET
	@Path("/selectedItems/{cookie}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserSelection(@PathParam("cookie") String cookie){
		String selection = "`selectedrecipes`,`id`";
		String context = "cookie=\"" + cookie + "\"";
		String selectedRecipes = select(TYPE_SELECTION,"cookme.person", selection, context).toSelectionString();
		System.out.println(selectedRecipes);
		return selectedRecipes;
	}
	
	@GET
	@Path("/recipeId/{recipeName}")
	@Produces(MediaType.TEXT_PLAIN)
	public Integer getCurrentRecipeId(@PathParam("recipeName") String recipeName){
		String selection = "`id`";
		String context = "title=\"" + recipeName + "\"";
		Integer id = select(TYPE_RECIPE_ID,"cookme.recipe", selection, context).toRecipeId();
		return id;
	}
}
