package services;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Impressum
 */
@WebServlet("/impressum")
public class Impressum extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public Impressum() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("<div class='impressum'><h1>Impressum</h1>" + 
				"Diese Website wurde von<br>" + 
				"Veronique Embach-Deeg <br>"
				+ "Niko Samson <br>"
				+ "Patrick Müller <br>"
				+ "erstellt. ");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
