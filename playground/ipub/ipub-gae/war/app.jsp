<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8"%><%
	PrintWriter writer = response.getWriter();
	String httpMethod = request.getMethod();
	String pathInfo = (String) request.getAttribute("app_jsp_path_info");

	if ("get".equalsIgnoreCase(httpMethod)) {
		if (pathInfo == null || pathInfo.length() <= 1) {
		//get List
			int sEcho = Integer.parseInt(request.getParameter("sEcho"));
			int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String query = request.getParameter("query");
			int iSortingCols = Integer.parseInt(request.getParameter("iSortingCols"));
			
			int[] iSortCol = new int[iSortingCols];
			String[] sSortDir = new String[iSortingCols];
			
			for (int i = 0; i < iSortingCols; i++) {
				iSortCol[i] = Integer.parseInt(request.getParameter("iSortCol_" + i));
				sSortDir[i] = request.getParameter("sSortDir_" + i);
			}

			writer.write("{ \"iTotalRecords\" : 2, \"iTotalDisplayRecords\" : 2, \"sEcho\" : \"" + sEcho + "\", \"records\" : [ { \"guid\" : \"4711\" , \"autorName\" : \"Autor Name 4711\", \"autorVorname\" : \"Autor Vorname 4711\", \"titel\" : \"Titel 4711\", \"institut\" : \"Institut 4711\", \"nummer\" : \"Nummer 4711\", \"jahr\" : \"03.01.2012\", \"href\" : \"ipub/4711\" },"
				+ " { \"guid\" : \"4712\" , \"autorName\" : \"Autor Name 4712\", \"autorVorname\" : \"Autor Vorname 4712\", \"titel\" : \"Titel 4712\", \"institut\" : \"Institut 4712\", \"nummer\" : \"Nummer 4712\", \"jahr\" : \"22.12.1981\", \"href\" : \"ipub/4712\" } ] }");
		} else {
			//extract guid
			String guid = null;
			String[] pathParts;
			if (pathInfo != null && (pathParts = pathInfo.split("/")).length > 1) {
				guid = pathParts[1];
			}
			writer.write("{ \"records\" : [ { \"guid\" : \"" + guid + "\" , \"autorName\" : \"Autor Name " + guid.substring(0, 4) + "\", \"autorVorname\" : \"Autor Vorname " + guid.substring(0, 4) + "\", \"titel\" : \"Titel " + guid.substring(0, 4) + "\", \"institut\" : \"Institut " + guid.substring(0, 4) + "\", \"nummer\" : \"Nummer " + guid.substring(0, 4) + "\", \"jahr\" : \"22.02.2012\", \"href\" : \"ipub/" + guid + "\" } ] }");
		}
	} else {
		//POST
		//return updated data
		String guid = request.getParameter("guid");
		String autorName = request.getParameter("autorName");
		String autorVorname = request.getParameter("autorVorname");
		String titel = request.getParameter("titel");
		String institut = request.getParameter("institut");
		String nummer = request.getParameter("nummer");
		String jahr = request.getParameter("jahr");
		writer.write("{ \"records\" : [ { \"guid\" : \"" + guid + "\" , \"autorName\" : \"" + autorName + "\", \"autorVorname\" : \"" + autorVorname + "\", \"titel\" : \"" + titel + "\", \"institut\" : \"" + institut + "\", \"nummer\" : \"" + nummer + "\", \"jahr\" : \"" + jahr + "\", \"href\" : \"ipub/" + guid + "\" } ] }");
	}
	writer.flush();
%>