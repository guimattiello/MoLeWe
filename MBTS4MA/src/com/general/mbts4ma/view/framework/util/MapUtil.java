package com.general.mbts4ma.view.framework.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {

	public static synchronized Map<String, String> fromList(List<String> listValues) {
		Map<String, String> values = new LinkedHashMap<String, String>();

		for (String listValue : listValues) {
			values.put(listValue, "");
		}

		return values;
	}

	public static Object getKeyFromValue(Map<String, Object> map, Object value) {
		for (String o : map.keySet()) {
			if (map.get(o).equals(value)) {
				return o;
			}
		}

		return null;
	}

}
