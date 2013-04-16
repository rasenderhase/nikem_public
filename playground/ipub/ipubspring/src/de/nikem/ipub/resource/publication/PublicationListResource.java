package de.nikem.ipub.resource.publication;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;

import org.json.JSONWriter;

import de.nikem.ipub.jersey.JerseyUtil;
import de.nikem.ipub.jersey.JsonOutput;
import de.nikem.jdbc.JdbcUtil;
import de.nikem.jdbc.QueryParam;
import de.nikem.jdbc.jquery.JQueryListPage;
import de.nikem.jdbc.jquery.JQueryScrollDataBlockQuery;

@Path("/publication")
public class PublicationListResource {

	private @Context ServletContext context;
	private JdbcUtil jdbcUtil;
	private PublicationAction publicationAction;

	@GET
	@Produces("application/json;charset=UTF-8")
	public Response doGet(final @Context UriInfo allUri) {
		StreamingOutput output = new JsonOutput() {
			@Override
			protected void write(JSONWriter w) {
				Map<String, String[]> parameterMap = JerseyUtil.toNormalParameterMap(allUri.getQueryParameters());
				String queryString = allUri.getQueryParameters().getFirst("query");
				BigDecimal queryNumber = BigDecimal.ZERO;
				if (queryString == null || queryString.length() == 0) {
					queryString = "_";
				} else {
					try {
						queryNumber = BigDecimal.valueOf(Long.parseLong(queryString));
					} catch (NumberFormatException ex) {
						queryString = queryString.toLowerCase() + "%";
					}
				}
				
				JQueryScrollDataBlockQuery query = new JQueryScrollDataBlockQuery(jdbcUtil, "getPublications",
						parameterMap, new QueryParam("queryString", queryString), new QueryParam("queryNumber", queryNumber));
				JQueryListPage listPage = query.execute();
				listPage.setTotalRecords(((Number) jdbcUtil.executeNamedQuery("getTotalCountPublications")
						.get(0).get("COUNT")).intValue());
				
				w.object();
				w.key("iTotalRecords").value(listPage.getTotalRecords());
				w.key("iTotalDisplayRecords").value(listPage.getTotalDisplayRecords());
				w.key("sEcho").value(listPage.getEcho());
				w.key("records");
				{
					w.array();
					for (Map<String, ?> row : listPage.getAaData()) {
						w.object();
						w.key("guid").value(row.get("GUID"));
						w.key("autorName").value(row.get("AUTORNAME"));
						w.key("autorVorname").value(row.get("AUTORVORNAME"));
						w.key("institut").value(row.get("INSTITUT"));
						w.key("jahr").value(row.get("JAHR"));
						w.key("nummer").value(row.get("NUMMER"));
						w.key("titel").value(row.get("TITEL"));
						w.key("href").value("publication/" + row.get("GUID"));
						w.endObject();
					}
					w.endArray();
				}
				w.endObject();
			}
		};

		return Response.ok(output).header("Content-Disposition", "attachment;filename=publications.json").build();
	}

	@POST
	@Produces("application/json")
	public Response doPost(final @FormParam("guid") String guid, final @FormParam("autorName") String autorName,
			final @FormParam("autorVorname") String autorVorname, final @FormParam("titel") String titel,
			final @FormParam("institut") String institut, final @FormParam("nummer") BigDecimal nummer, final @FormParam("jahr") int jahr) {
		return publicationAction.save(guid, autorName, autorVorname, titel, institut, nummer, jahr);
	}

	public JdbcUtil getJdbcUtil() {
		return jdbcUtil;
	}

	public void setJdbcUtil(JdbcUtil jdbcUtil) {
		this.jdbcUtil = jdbcUtil;
	}

	public PublicationAction getPublicationAction() {
		return publicationAction;
	}

	public void setPublicationAction(PublicationAction publicationAction) {
		this.publicationAction = publicationAction;
	}
}
