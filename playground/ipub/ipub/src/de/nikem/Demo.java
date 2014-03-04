package de.nikem;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONWriter;

import de.nikem.dataj.ListPage;
import de.nikem.dataj.jq.JqPaginationQuery;

public class Demo extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


		try {
			DataSource dataSource = (DataSource) InitialContext.doLookup("java:comp/env/JDBC");
			final Writer writer = resp.getWriter();
			final JSONWriter w = new JSONWriter(writer);
			w.object();
			
			w.key("aaData");
			ListPage<Object> page = new JqPaginationQuery<Object>(dataSource, "select * from employees", req.getParameterMap()) {
				@Override
				protected Object mapRow(ResultSet rs) throws SQLException {
					w.key("FIRST_NAME").value(rs.getString("FIRST_NAME"));
					w.key("LAST_NAME").value(rs.getString("LAST_NAME"));
					w.key("BIRTHDAY").value(new SimpleDateFormat().format(rs.getDate("BIRTHDAY")));
					//... and so on
					return null; //return value not needed because content is directly put to JSON output
				}
			}.execute();

			w.key("iTotalRecords").value(page.getTotalRecords());
			w.key("iTotalDisplayRecords").value(page.getTotalDisplayRecords());
			w.key("sEcho").value(page.getEcho());
			w.endObject();
		} catch (SQLException ex) {
			throw new ServletException("DB access failed");
		} catch (NamingException e) {
			throw new ServletException("Lookup failed");
		}
	}
}
