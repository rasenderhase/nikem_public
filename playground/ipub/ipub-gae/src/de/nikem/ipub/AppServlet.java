package de.nikem.ipub;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AppServlet
 */
public class AppServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AppServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("app_jsp_path_info", request.getPathInfo());
		request.getRequestDispatcher("/WEB-INF/app.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("app_jsp_path_info", request.getPathInfo());
		request.getRequestDispatcher("/WEB-INF/app.jsp").forward(request, response);
	}

}
