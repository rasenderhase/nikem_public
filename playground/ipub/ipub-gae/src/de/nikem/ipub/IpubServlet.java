package de.nikem.ipub;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class IpubServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			UserService userService = UserServiceFactory.getUserService();
			String thisURL = req.getRequestURI();
			if (userService.isUserLoggedIn()) {

				User user = userService.getCurrentUser();

				req.setAttribute("user", user);
				req.setAttribute("logoutUrl", userService.createLogoutURL(thisURL));
			} else {
				req.setAttribute("loginUrl", userService.createLoginURL(thisURL));
			}
			req.getRequestDispatcher("index.jsp").include(req, resp);
		} catch (ServletException e) {
			resp.setContentType("text/plain");
			e.printStackTrace(resp.getWriter());
			resp.sendError(500);
		}
	}
}
