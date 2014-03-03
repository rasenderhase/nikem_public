package de.nikem.ipub.resource.publication;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.json.JSONWriter;

import de.nikem.ipub.app.App;
import de.nikem.ipub.jersey.JsonOutput;
import de.nikem.jdbc.QueryParam;

public class PublicationAction {

	public Response save(ServletContext context, final String guid, final String autorName, final String autorVorname, final String titel,
			final String institut, final BigDecimal nummer, final int jahr) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(Calendar.YEAR, jahr);

		int updated = App
				.getSingletonFactory(context)
				.getJdbcUtil()
				.executeUpdateNamedQuery("updatePublication", null, new QueryParam("guid", guid), new QueryParam("autorName", autorName),
						new QueryParam("autorVorname", autorVorname), new QueryParam("titel", titel), new QueryParam("institut", institut),
						new QueryParam("nummer", nummer), new QueryParam("aendUser", "andreas"),
						new QueryParam("jahr", new Date(cal.getTime().getTime())));

		if (updated == 0) {
			App.getSingletonFactory(context)
					.getJdbcUtil()
					.executeUpdateNamedQuery("insertPublication", null, new QueryParam("guid", guid), new QueryParam("autorName", autorName),
							new QueryParam("autorVorname", autorVorname), new QueryParam("titel", titel), new QueryParam("institut", institut),
							new QueryParam("nummer", nummer), new QueryParam("owner", "andreas"), new QueryParam("aendUser", "andreas"),
							new QueryParam("jahr", new Date(cal.getTime().getTime())));
			URI uri;
			try {
				uri = new URI(guid);
			} catch (URISyntaxException e) {
				throw new WebApplicationException(e);
			}
			return Response.status(Status.CREATED).location(uri).entity(createJsonOutput(guid)).build();
		}
		return Response.ok(createJsonOutput(guid)).build();
	}

	/**
	 * Hier können die auf dem Server geänderten Werte zurückgeliefert werden.
	 * 
	 * @param guid
	 * @return
	 */
	protected StreamingOutput createJsonOutput(final String guid) {
		return new JsonOutput() {

			@Override
			protected void write(JSONWriter w) {
				w.object();
				w.key("records");
				{
					w.array();
					w.object();
					w.key("guid").value(guid);
					w.key("href").value("publication/" + guid);
					w.endObject();
					w.endArray();
				}
				w.endObject();

			}
		};
	}
}
