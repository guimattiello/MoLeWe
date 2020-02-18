package com.general.mbts4ma.view.framework.util;

import java.text.Normalizer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtil {

	public static synchronized String toCamelCase(final String init, boolean upperCaseFirstCharacter, String... excepts) {
		if (init == null) {
			return null;
		}

		final StringBuilder ret = new StringBuilder(init.length());

		boolean isFirstCharacter = true;

		for (final String word : init.replaceAll("[\\/_]", " ").split(" ")) {
			if (!word.isEmpty()) {
				ret.append(isFirstCharacter && !upperCaseFirstCharacter ? word.substring(0, 1).toLowerCase() : word.substring(0, 1).toUpperCase());
				ret.append(word.substring(1).toLowerCase());

				if (isFirstCharacter) {
					isFirstCharacter = !isFirstCharacter;
				}
			}
		}

		String value = ret.toString();

		if (excepts != null && excepts.length > 0) {
			for (String except : excepts) {
				if (except.equals(value)) {
					return value;
				}
			}
		}

		return unaccent(stripAccents(value));
	}

	public static synchronized String stripAccents(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	}

	public static synchronized String unaccent(String src) {
		return src.replaceAll("\\p{Punct}", "").replaceAll("[^\\w\\s]+", "");
	}

	public static synchronized String replace(String content, Map<String, String> values) {
		if (values != null && !values.isEmpty()) {
			Iterator<String> iValues = values.keySet().iterator();

			while (iValues.hasNext()) {
				String key = iValues.next();
				String value = values.get(key);

				content = content.replace(key, value);
			}
		}

		return content;
	}

	public static synchronized String convertListToString(List<String> objects, String... elementsToBeRemoved) {
		if (objects == null || objects.isEmpty()) {
			return null;
		}

		if (elementsToBeRemoved != null && elementsToBeRemoved.length > 0) {
			for (String elementToBeRemoved : elementsToBeRemoved) {
				objects.remove(elementToBeRemoved);
			}
		}

		return objects.toString().substring(1, objects.toString().length() - 1);
	}

	public static synchronized String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	public static synchronized List<String> getValuesWithRegEx(String data, String regex) {
		if (data == null || "".equalsIgnoreCase(data) || regex == null || "".equalsIgnoreCase(regex)) {
			return null;
		}

		List<String> values = new LinkedList<String>();

		Pattern p = Pattern.compile(regex);

		Matcher matcher = p.matcher(data);

		while (matcher.find()) {
			values.add(matcher.group(1));
		}

		return values;
	}

}
