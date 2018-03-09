package services;

import static services.Constant.TYPE_COMMENT;

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
	@Path("/{recipeId}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCurrentUserSelection(@PathParam("recipeId") Integer recipeId){
		String selection = "";
		String context = "";
		String comments = select(TYPE_COMMENT,"cookme.person", selection, context).toSelectionString();
		return comments;
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	public void updateFavourites( @FormParam("id") Integer id, 
								  @FormParam("user") String username, 
								  @FormParam("comment") String comment, 
								  @FormParam("time") Integer time ){
		String updateInformation = "";
		String context = "";
		update(TYPE_COMMENT,"cookme.person", updateInformation, context);
	}
}
