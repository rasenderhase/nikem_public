package de.nikem.ipub.jersey;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

public abstract class JerseyUtil {

	public static Map<String, String[]> toNormalParameterMap(MultivaluedMap<String, String> mvMap) {
		Map<String, String[]> map = new LinkedHashMap<String, String[]>();
		String[] array;
		for (Map.Entry<String, List<String>> entry : mvMap.entrySet()) {
			array = new String[entry.getValue().size()];
			map.put(entry.getKey(), entry.getValue().toArray(array));
		}
		return map;
	}
}
