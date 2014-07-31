package de.nikem.playground.rest.resource.test;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.api.view.Viewable;

import de.nikem.playground.model.Model;
import de.nikem.playground.model.MyModel;

@Path("/model")
public class ModelResource {

	private static final Map<String, Object> persistent = new HashMap<String, Object>();
	static {
		persistent.put("STUECKZAHL", 99);
		persistent.put("NAME", "Schrauben");
		persistent.put("GESAMTPREIS", 123.99);

		persistent.put("EINZELPREIS", 123.99 / 99);
	}

	@GET
	public Object get(@Context UriInfo allUri) {
		Model model;
		MultivaluedMap<String, String> params = allUri.getQueryParameters();
		if (params.isEmpty()) {
			model = new MyModel();
			model.setPersistent(loadData());
		} else {
			model = calc(params);
		}
		return new Viewable("/rest/model", model);
	}

	@POST
	public Object post(MultivaluedMap<String, String> form) {
		MyModel model = new MyModel();
		model.setPersistent(loadData());

		model.setClientData(form);
		if (model.validate()) {
			model.calculate();
			saveData(model.getPersistent());
		}

		return new Viewable("/rest/model", model);
	}

	private MyModel calc(MultivaluedMap<String, String> form) {
		MyModel model = new MyModel();

		model.setClientData(form);
		if (model.validate()) {
			model.calculate();
		}

		return model;
	}

	private Map<String, Object> loadData() {
		return new HashMap<String, Object>(persistent);
	}

	private void saveData(Map<String, Object> data) {
		persistent.putAll(data);
	}
}
