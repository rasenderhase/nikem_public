package de.nikem.playground.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class RestServlet
 */
@WebServlet(description = "Handles REST request of the test REST application", urlPatterns = {"/rest/"})
public class RestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RestServlet() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		System.out.println("isMultipart: " + isMultipart);
		if (isMultipart) {

			
			//DiskFileItemFactory hält items bis zu einer Größe von 10kB im Speicher
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) {
					System.out.println(item);
				}
			} catch (FileUploadException e) {
				throw new ServletException("Fehler beim Parsen des multipart Requests", e);
			}
			
			response.setContentType("multipart/mixed");			
		}
	}
}
