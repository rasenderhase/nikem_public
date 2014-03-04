package de.nikem;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private static final long serialVersionUID = 5056999204692421446L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			Object o = InitialContext.doLookup("");
			System.out.println(o);
			
			DataSource dataSource = (DataSource) InitialContext.doLookup("java:/comp/env/jdbc/reanim8or");
			resp.setCharacterEncoding("UTF-8");
			resp.setHeader("Content-Type", "application/json;charset=utf-8");
			final Writer writer = resp.getWriter();
			final JSONWriter w = new JSONWriter(writer);
			w.object();
			
			w.key("aaData");
			w.array();
			ListPage<Object> page = new JqPaginationQuery<Object>(dataSource, "select autor_name AUTORNAME, autor_vorname AUTORVORNAME, jahr JAHR from ipub_publications", req.getParameterMap()) {
				@Override
				protected Object mapRow(ResultSet rs) throws SQLException {
					w.object();
					w.key("FIRST_NAME").value(rs.getString("AUTORNAME"));
					w.key("LAST_NAME").value(rs.getString("AUTORVORNAME"));
					w.key("JAHR").value(rs.getInt("JAHR"));
					//... and so on
					w.endObject();
					return null; //return value not needed because content is directly put to JSON output
				}
			}.execute();
			w.endArray();
			w.key("iTotalRecords").value(page.getTotalRecords());
			w.key("iTotalDisplayRecords").value(page.getTotalDisplayRecords());
			w.key("sEcho").value(page.getEcho());
			w.endObject();
		} catch (SQLException ex) {
			throw new ServletException("DB access failed", ex);
		} catch (NamingException e) {
			throw new ServletException("Lookup failed", e);
		}
	}
}
