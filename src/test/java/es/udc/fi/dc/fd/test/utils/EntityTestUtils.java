package es.udc.fi.dc.fd.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EntityTestUtils {
	@SuppressWarnings("unchecked")
	private static Object generateRandomValue(@SuppressWarnings("rawtypes") Class type, int seed) {
		// WARNING: if field type is not basic, null will always be the value

		Object value = null;
		if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) {
			value = (seed % 2 == 0) ? Boolean.TRUE : Boolean.FALSE;
		}
		if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
			value = seed;
		}
		if (type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) {
			value = (long) seed;
		}
		if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) {
			value = (float) seed;
		}
		if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) {
			value = (double) seed;
		}
		if (type.isAssignableFrom(byte.class) || type.isAssignableFrom(Byte.class)) {
			value = (byte) seed;
		}
		if (type.isAssignableFrom(String.class)) {
			value = "string-" + Integer.toString(seed);
		}
		if (value == null) {
			if (type.isArray()) {
				Object array = Array.newInstance(type.getComponentType(), 1);
				Array.set(array, 0, generateRandomValue(type.getComponentType(), seed));
				return array;
			}
			try {
				return createInstance(type);
			} catch(Exception e) {
				return null;
			}
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
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);
				field.set(object, generateRandomValue(field.getType(), generator.nextInt()));
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
				if (Modifier.isStatic(field.getModifiers())) {
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
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			if (fieldsToIgnore.contains(field.getName())) {
				continue;
			}
			try {
				field.setAccessible(true);
				Object oldValue = field.get(copy);
				if (oldValue == null) {
					continue;
				}
				assertEquals(object, copy);

				field.set(copy, generateRandomValue(field.getType(), generator.nextInt()));
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
				Object value = generateRandomValue(field.getType(), generator.nextInt());
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
				Object value = generateRandomValue(field.getType(), generator.nextInt());
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
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if ((!field.getType().isPrimitive()) && (field.getType() != String.class)) {
					continue;
				}
				field.setAccessible(true);
				Object val = field.get(object);
				if (val == null) {
					continue;
				}
				assertTrue(string.contains(val.toString()));
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
