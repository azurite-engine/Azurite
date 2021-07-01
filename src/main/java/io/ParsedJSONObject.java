package io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static util.Utils.splitIgnoringSubCategories;
import static util.Utils.unwrapString;

public class ParsedJSONObject {
	
	private List<JSONElement> elements = new ArrayList<>();
	
	public ParsedJSONObject(String json) {
		json = unwrapString(json, "{", "}");
		for (String pair : splitIgnoringSubCategories(json, ',', new Character[]{'{', '['}, new Character[]{'}', ']'})) {
			String[] split = pair.split(":", 2);
			elements.add(new JSONElement(split[0], split[1]));
		}
	}
	
	public String getAsString(String key) {
		return elements.stream().filter(element -> element.key.equals(key)).findFirst().map(jsonElement -> jsonElement.value).orElse(null);
	}
	
	public int getAsInteger(String key) {
		return Integer.parseInt(getAsString(key));
	}
	
	public float getAsFloat(String key) {
		return Float.parseFloat(getAsString(key));
	}
	
	public double getAsDouble(String key) {
		return Double.parseDouble(getAsString(key));
	}
	
	public char getAsCharacter(String key) {
		return getAsString(key).charAt(0);
	}
	
	public boolean getAsBoolean(String key) {
		return Boolean.parseBoolean(getAsString(key));
	}
	
	public ParsedJSONObject getAsJsonObject(String key) {
		return new ParsedJSONObject(getAsString(key));
	}
	
	public Collection<String> getAsStringArray(String key) {
		String jsonArr = unwrapString(getAsString(key), "[", "]");
		return splitIgnoringSubCategories(jsonArr, ',', new Character[]{'{', '['}, new Character[]{'}', ']'});
	}
	
	public Collection<Integer> getAsIntegerArray(String key) {
		return getAsStringArray(key).stream().map(Integer::parseInt).collect(Collectors.toList());
	}
	
	public Collection<Float> getAsFloatArray(String key) {
		return getAsStringArray(key).stream().map(Float::parseFloat).collect(Collectors.toList());
	}
	
	public Collection<Double> getAsDoubleArray(String key) {
		return getAsStringArray(key).stream().map(Double::parseDouble).collect(Collectors.toList());
	}
	
	public Collection<Character> getAsCharArray(String key) {
		return getAsStringArray(key).stream().map(s ->  s.charAt(0)).collect(Collectors.toList());
	}
	
	public Collection<Boolean> getAsBooleanArray(String key) {
		return getAsStringArray(key).stream().map(Boolean::parseBoolean).collect(Collectors.toList());
	}
	
	public Collection<ParsedJSONObject> getAsJsonObjectArray(String key) {
		return getAsStringArray(key).stream().map(ParsedJSONObject::new).collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return elements.stream().map(JSONElement::toString).collect(Collectors.joining("\n"));
	}
	
	private static class JSONElement {
		private final String key;
		private final String value;
		
		public JSONElement(String key, String value) {
			this.key = unwrapString(key, "\"", "\"");
			this.value = value.trim();
		}
		
		@Override
		public String toString() {
			return key + " : " + value;
		}
	}
}