package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ticketMachine
 */
public class ticketMachine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ticketMachine() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//Cookie representing the current client status
		Cookie clientStatus = new Cookie("clientStatus", "");
		//Cookie used for reading from client's cookies
		Cookie cookies[] = request.getCookies();
		//Cookie used for controlling the status changes
		Cookie numOfState = new Cookie("numOfState", "0");
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true); // new Session created
														// if did not exist
		// Reading cookies from the client
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (clientStatus.getName().equals(cookie.getName()))
					clientStatus = cookie;
				if (numOfState.getName().equals(cookie.getName()))
					numOfState = cookie;
			}
		}
		//Checking if the session exists 
		if (session.getAttribute("clientStatus") == null && !numOfState.getValue().equals(("3"))) {
			session.setAttribute("clientStatus", "notFirstRequest");
			clientStatus.setValue("NEW");
			numOfState.setValue("1");
			response.addCookie(clientStatus);
			response.addCookie(numOfState);
			out.println("Your current status is " + clientStatus.getValue());
			out.println("Please use again GET to change status");
		} else if (clientStatus.getValue().equals("NEW")) {
			clientStatus.setValue("PAYMENT");
			numOfState.setValue("2");
			response.addCookie(clientStatus);
			response.addCookie(numOfState);
			out.println("Your current status is " + clientStatus.getValue());
			out.println("Please use again GET to change status");
		} else if (clientStatus.getValue().equals("PAYMENT")) {
			clientStatus.setValue("COMPLETED");
			numOfState.setValue("3");
			response.addCookie(clientStatus);
			response.addCookie(numOfState);
			out.println("Your current status is " + clientStatus.getValue());
			out.println("FINAL STATUS REACHED. BYE");
			session.invalidate();       //invalidating the session
			clientStatus.setMaxAge(0); // Destroying the cookie
		}
	}
}
