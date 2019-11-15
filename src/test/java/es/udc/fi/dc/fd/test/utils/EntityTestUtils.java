package es.udc.fi.dc.fd.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EntityTestUtils {
	private static Object generateRandomValue(Field field, int seed) {
		// WARNING: if field type is not basic, null will always be the value

		Object value = null;
		if (field.getType().isAssignableFrom(boolean.class) || field.getType().isAssignableFrom(Boolean.class)) {
			value = (seed % 2 == 0) ? Boolean.TRUE : Boolean.FALSE;
		}
		if (field.getType().isAssignableFrom(int.class) || field.getType().isAssignableFrom(Integer.class)) {
			value = seed;
		}
		if (field.getType().isAssignableFrom(long.class) || field.getType().isAssignableFrom(Long.class)) {
			value = (long) seed;
		}
		if (field.getType().isAssignableFrom(float.class) || field.getType().isAssignableFrom(Float.class)) {
			value = (float) seed;
		}
		if (field.getType().isAssignableFrom(double.class) || field.getType().isAssignableFrom(Double.class)) {
			value = (double) seed;
		}
		if (field.getType().isAssignableFrom(String.class)) {
			value = "string-" + Integer.toString(seed);
		}
		return value;
	}

	private static Object createInstance(@SuppressWarnings("rawtypes") Class type) {
		List<Field> fields = Arrays.asList(type.getDeclaredFields());
		Random generator = new Random();
		try {
			@SuppressWarnings("unchecked")
			Object object = type.getConstructor().newInstance();
			for (Field field : fields) {
				field.setAccessible(true);
				field.set(object, generateRandomValue(field, generator.nextInt()));
			}
			return object;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	private static Object copyInstance(Object ori) {
		List<Field> fields = Arrays.asList(ori.getClass().getDeclaredFields());
		Object result;
		try {
			result = ori.getClass().getConstructor().newInstance();
			for (Field field : fields) {
				if (field.isSynthetic()) {
					continue;
				}
				field.setAccessible(true);
				field.set(result, field.get(ori));
			}
			return result;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void testEquals(@SuppressWarnings("rawtypes") Class type, List<String> fieldsToIgnore) {
		try {
			type.getDeclaredMethod("equals", Object.class);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			return;
		}
		Object object = createInstance(type);
		assertFalse(object.equals(null));
		assertTrue(object.equals(object));
		assertFalse(object.equals(""));
		assertTrue(object.equals(copyInstance(object)));

		Object copy = copyInstance(object);
		Field[] fields = object.getClass().getDeclaredFields();
		Random generator = new Random();
		for (Field field : fields) {
			if (field.isSynthetic()) {
				continue;
			}
			if (fieldsToIgnore.contains(field.getName())) {
				continue;
			}
			try {
				field.setAccessible(true);
				Object oldValue = field.get(copy);
				assertEquals(object, copy);

				field.set(copy, generateRandomValue(field, generator.nextInt()));
				assertFalse(object.equals(copy));
				field.set(copy, oldValue);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}

		}
	}

	@SuppressWarnings("unchecked")
	public static void testHashCode(@SuppressWarnings("rawtypes") Class type) {
		try {
			type.getDeclaredMethod("equals", Object.class);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			return;
		}
		Object object = createInstance(type);
		Object copy = copyInstance(object);
		assertEquals(object.hashCode(), object.hashCode());
		assertEquals(object.hashCode(), copy.hashCode());
	}

	public static void testGettersAndSetters(@SuppressWarnings("rawtypes") Class type,
			List<String> fieldsToIgnoreGetter, List<String> fieldsToIgnoreSetter) {
		List<Method> getters = new ArrayList<>();
		List<Method> setters = new ArrayList<>();

		Arrays.asList(type.getDeclaredMethods()).forEach((Method e) -> {

			if (!e.isSynthetic()) {
				List<Method> listToAdd = null;
				if (e.getName().startsWith("get") && (e.getParameterCount() == 0)
						&& (!fieldsToIgnoreGetter.contains(e.getName()))) {
					listToAdd = getters;
				} else if (e.getName().startsWith("set") && (e.getParameterCount() == 1)
						&& (!fieldsToIgnoreSetter.contains(e.getName()))) {
					listToAdd = setters;
				} else {
					return;
				}
				String fieldName = e.getName().substring(3);
				fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
				listToAdd.add(e);

			}
		});

		Random generator = new Random();
		Object object = createInstance(type);

		try {
			for (int i = 0; i < getters.size(); i++) {
				Method getter = getters.get(i);

				String fieldName = getter.getName().substring(3);
				fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

				Field field = type.getDeclaredField(fieldName);
				Object value = generateRandomValue(field, generator.nextInt());
				field.setAccessible(true);
				field.set(object, value);
				getter.setAccessible(true);
				assertEquals(getter.invoke(object), value);
			}
			for (int i = 0; i < setters.size(); i++) {
				Method setter = setters.get(i);
				String fieldName = setter.getName().substring(3);
				fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

				Field field = type.getDeclaredField(fieldName);
				Object value = generateRandomValue(field, generator.nextInt());
				field.setAccessible(true);
				setter.setAccessible(true);
				setter.invoke(object, value);
				assertEquals(field.get(object), value);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException
				| SecurityException e1) {
			throw new RuntimeException(e1);
		}

	}

	@SuppressWarnings("unchecked")
	public static void testToString(@SuppressWarnings("rawtypes") Class type, List<String> fieldsToIgnoreToString) {
		try {
			Object object = createInstance(type);
			String string = (String) type.getDeclaredMethod("toString").invoke(object);
			for (Field field : type.getDeclaredFields()) {
				if (field.isSynthetic() || fieldsToIgnoreToString.contains(field.getName())) {
					continue;
				}
				field.setAccessible(true);
				assertTrue(string.contains(field.get(object).toString()));
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			return;
		}
	}

	public static void testEntity(@SuppressWarnings("rawtypes") Class type, List<String> fieldsToIgnoreEquals,
			List<String> fieldsToIgnoreGetter, List<String> fieldsToIgnoreSetter, List<String> fieldsToIgnoreToString) {

		testGettersAndSetters(type, fieldsToIgnoreGetter, fieldsToIgnoreSetter);
		testEquals(type, fieldsToIgnoreEquals);
		testHashCode(type);
		testToString(type, fieldsToIgnoreToString);
	}

	public static void testEntity(@SuppressWarnings("rawtypes") Class type) {
		testEntity(type, Arrays.asList(new String[0]), Arrays.asList(new String[0]),
				Arrays.asList(new String[0]), Arrays.asList(new String[0]));
	}
}
