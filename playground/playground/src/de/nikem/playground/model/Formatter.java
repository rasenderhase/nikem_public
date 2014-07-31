package de.nikem.playground.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class Formatter implements Map<String, String> {
	private final Logger log = Logger.getLogger(getClass());

	private Model model;

	private String formatWithFormatter(String key, Object p) {
		String value;
		Formatter f = getModel().getFormatters().get(key);
		if (f != null) {
			value = f.format(key, p);
		} else {
			value = format(key, p);
		}
		return value;
	}

	private Object parseWithFormatter(String key, String value) {
		Object v;
		Formatter f = getModel().getFormatters().get(key);
		if (f != null) {
			v = f.parse(key, value);
		} else {
			v = parse(key, value);
		}
		return v;
	}

	public String format(String key, Object p) {
		String value = null;
		if (p != null) {
			value = p.toString();
		}
		return value;
	};

	protected Object parse(String key, String value) {
		return value;
	};

	@Override
	public void clear() {
	}

	@Override
	public boolean containsKey(Object key) {
		log.warn("Call of unimplemented method");
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		log.warn("Call of unimplemented method");
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		log.warn("Call of unimplemented method");
		return null;
	}

	@Override
	public String get(Object key) {
		String value = getModel().getDisplay().get(key);
		Object p;
		if (value == null && (p = getModel().getPersistent().get(key)) != null) {
			value = formatWithFormatter((String) key, p);
			getModel().getDisplay().put(value, value);
		}
		return value;
	}

	@Override
	public boolean isEmpty() {
		log.warn("Call of unimplemented method");
		return false;
	}

	@Override
	public Set<String> keySet() {
		log.warn("Call of unimplemented method");
		return null;
	}

	@Override
	public String put(String key, String value) {
		String previous = getModel().getDisplay().put(key, value);
		getModel().getPersistent().put(key, parseWithFormatter(key, value));
		return previous;
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		log.warn("Call of unimplemented method");
	}

	@Override
	public String remove(Object key) {
		log.warn("Call of unimplemented method");
		return null;
	}

	@Override
	public int size() {
		log.warn("Call of unimplemented method");
		return 0;
	}

	@Override
	public Collection<String> values() {
		log.warn("Call of unimplemented method");
		return null;
	}

	protected Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
}
