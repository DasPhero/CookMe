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
import static services.Constant.RESOLVE_USERNAME;

import java.util.List;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class RESTPerson extends DatabaseInterface {

	@GET
	@Path("/{id}/{uuid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCookie(@PathParam("id") int id,@PathParam("uuid") String uuid) {
		if (id != RESOLVE_USERNAME ) {
			 String where = "id = " + id;
			 String select = "`id`,`username`,`squestion`,`sanswer`,`password`,  LEFT(\r\n" + 
			 		"                                   REPLACE(\r\n" + 
			 		"                                       REPLACE(\r\n" + 
			 		"                                           REPLACE(\r\n" + 
			 		"                                               TO_BASE64(\r\n" + 
			 		"                                                   UNHEX(\r\n" + 
			 		"                                                       MD5(\r\n" + 
			 		"                                                           RAND()\r\n" + 
			 		"                                                       )\r\n" + 
			 		"                                                   )\r\n" + 
			 		"                                               ), \"/\", \"\"\r\n" + 
			 		"                                           ), \"+\", \"\"\r\n" + 
			 		"                                       ), \"=\", \"\"\r\n" + 
			 		"                                   ), 20\r\n" + 
			 		"                               )as `cookie`";
			 List<Person> list = select(TYPE_PERSON_LOGIN,"cookme.person", select, where).toPersonList();
			 if (list.size() == 0) {
				 return "invalid";
			 }
			 Person t = list.get(0);
			 String cookie = t.getCookie();
			 System.out.println(cookie);
			return cookie;
		}else {
			
			 String where = "cookie = '" + uuid+"'";
			 String select = "`id`,`username`,`cookie`";
			 List<Person> list = select(TYPE_PERSON_LOGIN,"cookme.person", select, where).toPersonList();
			 if (list.size() == 0) {
				 return "";
			 }
			 Person t = list.get(0);
			 String userName = t.getUserName();
			 System.out.println(userName);
			return userName;
		}
	}

	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCookie(@FormParam("id") int id, @FormParam("cookie") String cookie) {
		System.out.println("id: "+ id);
		System.out.println( " cookie: " + cookie);
		if (!update(TYPE_PERSON_LOGIN, " `person`", "`cookie` = '"+ cookie + "' ", "`id` = "+ id +"")) {
			System.out.println("Error!!!!!");
			return "Das Objekt ist nicht Vorhanden in der DB.";
		} else
			return "Success";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	//@Produces(MediaType.TEXT_PLAIN)
	public Person loginPerson(@FormParam("password") String password, @FormParam("username") String userName) {
		String where = "username = '" + userName + "' && password = '" + password + "'";
		DatabaseResponse response1 = select(TYPE_PERSON_LOGIN, "cookme.person", "id,password,username", where);
		Person t;
		if ( response1 == null) {
			t = new Person();
			t.setId(-1);
			return t;
		} else {
			List<Person> list1 = response1.toPersonList();
			t= list1.get(0);
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
