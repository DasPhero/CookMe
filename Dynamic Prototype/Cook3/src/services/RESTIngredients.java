package services;

import static services.Constant.TYPE_INGREDIENT;
import static services.Constant.TYPE_ITEM;
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
		String where = "id = " + id;
		if (id == GET_ALL_INGREDIENTS) {
			where = "id = id";
		}
		String select = "`id`,`name`";
		DatabaseResponse response = select(TYPE_INGREDIENT, "cookme.item", select ,"",where,"");
		if (null == response) {
			return "[]";
		}
		if (response.getIngredientsItem().size() != response.getIngredientsValue().size()) {
			System.out.println("Error+++++++++++++++++++++++++++++++++++++");
			return "[]";
		}
		String json="[";
		System.out.println(response.getIngredientsItem().size());
		for (int i = 0; i < response.getIngredientsValue().size(); i++) {
			json+="{\"id\":" + response.getIngredientsValue().get(i);
			json+=",\"name\":\"" + response.getIngredientsItem().get(i);
			json+="\"}";
			if (i+1 < response.getIngredientsValue().size()) {
				json+=",";
			}
		}
		json+="]";
		return json;
	}
	
	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String getRecipes(@FormParam("where") String where) {
		System.out.println(where);
		String whereSQL = where + " GROUP by fk_recipe order by count desc";
		DatabaseResponse response = select(TYPE_ITEM, " recipeitems","rec.title as title,fk_recipe as id " ," recipe rec on rec.id = fk_recipe", whereSQL,"COUNT(*) as count");
		
		if (null == response) {
			return "";
		}
		String json="[";
		for (int i = 0; i < response.getItemCount().size(); i++) {
			json+="{\"title\":\"" + response.getTitle().get(i);
			json+="\",\"count\":" + response.getItemCount().get(i);
			json+=",\"id\":" + response.getId().get(i);
			json+="}";
			if (i+1 < response.getItemCount().size()) {
				json+=",";
			}
		}
		json+="]";
		
		return json;
	}
}
