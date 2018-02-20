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
import static services.Constant.TYPE_PERSON_LOGIN;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class RESTPerson extends DatabaseInterface {

	@GET
	@Path("/{id}")
	// @Produces(MediaType.TEXT_PLAIN)
	public Person getPerson(@PathParam("id") int id) {
		// String where = "id = " + id;
		// DatabaseInterface dbi = new DatabaseInterface();
		// Person t = dbi.select(1,"cookme.person", "id,username",
		// where).toPersonList().get(0);
		System.out.println("Sicherheitsrisiko!!!");
		return null;
	}

	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePerson(@FormParam("id") int id, @FormParam("username") String userName) {
		System.out.println(id);
		if (!update(TYPE_PERSON_LOGIN, "cookme.person", "username", "username = 'patrick'")) {
			System.out.println("Error!!!!!");
			return "Das Objekt ist nicht Vorhanden in der DB.";
		} else
			return "Success";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String loginPerson(@FormParam("password") String password, @FormParam("username") String userName) {
		String where = "username = '" + userName + "' && password = '" + password + "'";
		if (select(TYPE_PERSON_LOGIN, "cookme.person", "id,password,username", where) == null) {
			return "Error";
		} else
			return "Success";
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
