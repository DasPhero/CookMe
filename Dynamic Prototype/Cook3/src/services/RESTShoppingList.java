package services;

import static services.Constant.TYPE_SELECTION;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
		return selectedRecipes;
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void updateFavourites( @FormParam("cookie") String cookie, @FormParam("selectedrecipes") String selection ){
		String updateInformation = "selectedrecipes=\"" + selection +"\"";
		String context = "cookie=\"" + cookie + "\"";
		Boolean done = update(TYPE_SELECTION,"cookme.person", updateInformation, context);
		if(done) { System.out.println("Selected recipes successfully updated."); }
		else { System.out.println("Failed updating selected recipes! cookie: " + cookie + " selectedrecipes: " + selection); };
	}
}
