package io;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

public class JSONDeserializer {
	public static <T> T fromJSON(Class<T> clazz, String json) {
		T obj = null;
		try {
			obj = clazz.newInstance();
			
			ParsedJSONObject jo = new ParsedJSONObject(json);
			for (Field field : clazz.getDeclaredFields()) {
				
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				if (!field.getName().equals("$assertionsDisabled") && (Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
					continue;
				}
				
				if (field.getType() == int.class) {
					field.setInt(obj, jo.getAsInteger(field.getName()));
				}
				else if (field.getType() == float.class) {
					field.setFloat(obj, jo.getAsFloat(field.getName()));
				}
				else if (field.getType() == double.class) {
					field.setDouble(obj, jo.getAsDouble(field.getName()));
				}
				else if (field.getType() == char.class) {
					field.setChar(obj, jo.getAsCharacter(field.getName()));
				}
				else if(field.getType() == String.class) {
					field.set(obj, jo.getAsString(field.getName()));
				}
				else if (field.getType() == boolean.class) {
					field.setBoolean(obj, jo.getAsBoolean(field.getName()));
				}
				else if(field.getType().isArray()) {
					handleArrays(obj, jo, field, field.getType().getComponentType());
				}
				else if(field.getType().isAssignableFrom(List.class)) {
					handleList(obj, jo, field);
				}
				else if (!field.getType().isPrimitive()) {
					field.set(obj, fromJSON(field.getType(), jo.getAsString(field.getName())));
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	private static <T, V> void handleList(T obj, ParsedJSONObject jo, Field field) throws IllegalAccessException {
		Class<?> arrayType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
		if(arrayType == int.class) {
			field.set(obj, jo.getAsIntegerArray(field.getName()));
		}
		else if(arrayType == float.class) {
			field.set(obj, jo.getAsFloatArray(field.getName()));
		}
		else if(arrayType == double.class) {
			field.set(obj, jo.getAsDoubleArray(field.getName()));
		}
		else if(arrayType == char.class) {
			field.set(obj, jo.getAsCharArray(field.getName()));
		}
		else if(arrayType == String.class) {
			field.set(obj, jo.getAsStringArray(field.getName()));
		}
		else if(arrayType == boolean.class) {
			field.set(obj, jo.getAsBooleanArray(field.getName()));
		}
		else if(!field.getType().isPrimitive()) {
			field.set(obj, field.getType().cast(jo.getAsStringArray(field.getName()).stream().map(s -> fromJSON(arrayType, s)).collect(Collectors.toList())));
		}
	}
	
	private static <T, V> void handleArrays(T obj, ParsedJSONObject jo, Field field, Class<V> componentClass) throws IllegalAccessException {
		if(componentClass == int.class) {
			field.set(obj, jo.getAsIntegerArray(field.getName()).stream().mapToInt(x -> x).toArray());
		}
		else if(componentClass == float.class) {
			List<Float> list = jo.getAsFloatArray(field.getName());
			float[] array = new float[list.size()];
			for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
			field.set(obj, array);
		}
		else if(componentClass == double.class) {
			field.set(obj, jo.getAsDoubleArray(field.getName()).stream().mapToDouble(x -> x).toArray());
		}
		else if(componentClass == char.class) {
			List<Character> list = jo.getAsCharArray(field.getName());
			char[] array = new char[list.size()];
			for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
			field.set(obj, array);

		}
		else if(componentClass == String.class) {
			field.set(obj, jo.getAsStringArray(field.getName()).toArray(new String[0]));
		}
		else if(componentClass == boolean.class) {
			List<Boolean> list = jo.getAsBooleanArray(field.getName());
			boolean[] array = new boolean[list.size()];
			for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
			field.set(obj, array);
			
			field.set(obj, list);
		}
		else if(!field.getType().isPrimitive()) {
			Object[] list = jo.getAsStringArray(field.getName()).stream().map(s -> fromJSON(componentClass, s)).collect(Collectors.toList()).toArray((V[]) Array.newInstance(componentClass, 0));
			field.set(obj, field.getType().cast(list));
		}
	}
}
