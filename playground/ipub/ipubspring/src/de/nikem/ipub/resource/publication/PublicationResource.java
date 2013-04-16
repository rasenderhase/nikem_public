package de.nikem.ipub.resource.publication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.json.JSONWriter;

import com.sun.jersey.api.NotFoundException;

import de.nikem.ipub.jersey.JsonOutput;
import de.nikem.jdbc.JdbcUtil;
import de.nikem.jdbc.QueryParam;

@Path("/publication/{guid}")
public class PublicationResource {
	private @Context ServletContext context;
	private JdbcUtil jdbcUtil;
	private PublicationAction publicationAction;

	@GET
	@Produces("application/json;charset=UTF-8")
	public Response doGet(final @PathParam("guid") String guid) {
		StreamingOutput output = new JsonOutput() {
			@Override
			protected void write(JSONWriter w) {
				List<Map<String, ?>> result = jdbcUtil
						.executeNamedQuery("getPublication", new QueryParam("guid", guid));
				if (result.isEmpty()) {
					throw new NotFoundException("Publication with guid " + guid + " does not exist.");
				}

				Map<String, ?> row = result.get(0);
				w.object();
				w.key("records");
				{
					w.array();
					w.object();
					w.key("guid").value(row.get("GUID"));
					w.key("autorName").value(row.get("AUTOR_NAME"));
					w.key("autorVorname").value(row.get("AUTOR_VORNAME"));
					w.key("institut").value(row.get("INSTITUT"));
					w.key("jahr").value(row.get("JAHR"));
					w.key("nummer").value(row.get("NUMMER"));
					w.key("titel").value(row.get("TITEL"));
					w.key("href").value("publication/" + row.get("GUID"));
					w.endObject();
					w.endArray();
				}
				w.endObject();
			}
		};
		return Response.ok(output).header("Content-Disposition", "attachment;filename=publication.json").build();
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/json;charset=UTF-8")
	public Response doPost(final @PathParam("guid") String guid, final @FormParam("autorName") String autorName,
			final @FormParam("autorVorname") String autorVorname, final @FormParam("titel") String titel,
			final @FormParam("institut") String institut, final @FormParam("nummer") BigDecimal nummer, final @FormParam("jahr") int jahr) {

		return publicationAction.save(guid, autorName, autorVorname, titel, institut, nummer, jahr);
	}

	public PublicationAction getPublicationAction() {
		return publicationAction;
	}

	public void setPublicationAction(PublicationAction publicationAction) {
		this.publicationAction = publicationAction;
	}

	public JdbcUtil getJdbcUtil() {
		return jdbcUtil;
	}

	public void setJdbcUtil(JdbcUtil jdbcUtil) {
		this.jdbcUtil = jdbcUtil;
	}

}
