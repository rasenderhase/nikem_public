package de.nikem.playground.model;

import java.util.HashMap;
import java.util.Map;

public class Model {
	private Map<String, Object> persistent = new HashMap<String, Object>();
	private final Map<String, String> display = new HashMap<String, String>();
	private final Formatter string = new Formatter();

	private final Map<String, String> errors = new HashMap<String, String>();

	private final Map<String, Formatter> formatters = new HashMap<String, Formatter>();

	public Model() {
		string.setModel(this);
	}

	public Map<String, Object> getPersistent() {
		return persistent;
	}

	public void setPersistent(Map<String, Object> persistent) {
		this.persistent = persistent;
	}

	Map<String, String> getDisplay() {
		return display;
	}

	public Formatter getString() {
		return string;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	protected Map<String, Formatter> getFormatters() {
		return formatters;
	}

	public Formatter getFormat() {
		return string;
	}

	public boolean validate() {
		return getErrors().isEmpty();
	}

	protected void invalidateDisplay() {
		getDisplay().clear();
	}
}
