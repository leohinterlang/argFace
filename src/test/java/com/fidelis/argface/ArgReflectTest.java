/**
 *+
 *	ArgReflectTest.java
 *	1.0.0  Oct 25, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import static com.fidelis.argface.TestOut.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * ArgReflectTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgReflectTest {
	
	@Rule public TestName testName = new TestName();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp () throws Exception {
		announce(testName);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown () throws Exception {
		passFail(testName);
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#ArgReflect()}.
	 */
	@Test
	public void testArgReflect () {
		ArgReflect reflect = new ArgReflect();
		assertNotNull(reflect);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#ArgReflect(java.lang.Object)}.
	 */
	@Test
	public void testArgReflectObject () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		assertNotNull(pojo);
		assertNotNull(reflect);
		assertSame(pojo, reflect.getObject());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#setObject(java.lang.Object)}.
	 */
	@Test
	public void testSetObject () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect();
		reflect.setObject(pojo);
		assertSame(pojo, reflect.getObject());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getObject()}.
	 */
	@Test
	public void testGetObject () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect();
		assertNull(reflect.getObject());
		reflect.setObject(pojo);
		assertSame(pojo, reflect.getObject());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#setPrivateAccess(boolean)}.
	 */
	@Test
	public void testSetPrivateAccess () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Expect null return on private access variable.
		Field field = reflect.findField("privateBooleanField", boolean.class);
		testCase("privateBooleanField", field == null ? "not found" : "accessible");
		assertNull(field);
		
		// Set private access.
		reflect.setPrivateAccess(true);
		
		// Expect access to private field.
		field = reflect.findField("privateBooleanField", boolean.class);
		testCase("after setPrivateAccess", field == null ? "not found" : "accessible");
		assertNotNull(field);
		
		// Set field to true.
		reflect.setValue(field, true);
		
		// Expect true return from pojo.
		boolean value = pojo.booleanField();
		testCase("set to true", value);
		assertTrue(value);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getString(java.lang.String)}.
	 */
	@Test
	public void testGetStringString () {
		String foxtrot = "FOXTROT";
		String charlie = "CHARLIE";
		ReflectPojo pojo = new ReflectPojo(false, 79, foxtrot);
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Get from private access.
		String value = reflect.getString("privateStringField");
		testCase(foxtrot, value);
		assertEquals(foxtrot, value);
		
		// Get from public getter.
		pojo.setPrivateStringMethod(charlie);
		value = reflect.getString("privateStringMethod");
		testCase(charlie, value);
		assertEquals(charlie, value);
		
		// Get String array from field.
		value = reflect.getString("privateStringArrayField");
		testCase("String array field", value);
		assertEquals("WHISKEYTANGOXRAY", value);
		
		// Get String array from public getter.
		value = reflect.getString("privateStringArrayMethod");
		testCase("String array method", value);
		assertEquals("HOTELBRAVOMIKE", value);
		
		// Get String array from field with name ending in "Text".
		value = reflect.getString("privateStringArrayFieldText");
		
		// Replace newlines with underscores.
		value = value.replace('\n', '_');
		testCase("String array field Text", value);
		assertEquals("NOVEMBER_KILO_QUEBEC_", value);
		
		// Get String array from public getter with name ending in "Text".
		value = reflect.getString("privateStringArrayMethodText");
		
		// Replace newlines with underscores.
		value = value.replace('\n', '_');
		testCase("String array method Text", value);
		assertEquals("SIERRA_ZULU_INDIA_", value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getString(java.lang.reflect.Method)}.
	 */
	@Test
	public void testGetStringMethod () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Set a value.
		String echo = "ECHO";
		pojo.setPrivateStringMethod(echo);
		
		// Find the getter method.
		Method method = reflect.findMethod("getPrivateStringMethod", String.class);
		if (method != null) {
			testCase("getter method", method.getName());
		}
		assertNotNull(method);
		
		// Get the value.
		String value = reflect.getString(method);
		testCase(echo, value);
		assertEquals(echo, value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getString(java.lang.reflect.Field)}.
	 */
	@Test
	public void testGetStringField () {
		String delta = "DELTA";
		String golf = "GOLF";
		ReflectPojo pojo = new ReflectPojo(true, 7043, delta);
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Find the String Field.
		Field field = reflect.findField("privateStringField", String.class);
		if (field != null) {
			testCase("field found", field.getName());
		}
		assertNotNull(field);
		
		// Get the String value from the pojo.
		String value = reflect.getString(field);
		testCase(field.getName(), value);
		assertEquals(delta, value);
		
		// Set value for String method.
		pojo.setPrivateStringMethod(golf);
		
		// Find the String field.
		field = reflect.findField("privateStringMethod", String.class);
		if (field != null) {
			testCase("field found", field.getName());
		}
		assertNotNull(field);
		
		// Get the String value from the pojo.
		value = reflect.getString(field);
		testCase(field.getName(), value);
		assertEquals(golf, value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getStringArray(java.lang.reflect.Method, boolean)}.
	 */
	@Test
	public void testGetStringArrayMethodBoolean () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Find the string array method.
		Method method = reflect.findMethod("getPrivateStringArrayMethod", String[].class);
		if (method != null) {
			testCase("method found", method.getName());
		}
		assertNotNull(method);
		
		// Get the String array.
		String value = reflect.getStringArray(method, false);
		testCase(method.getName(), value);
		assertEquals("HOTELBRAVOMIKE", value);
		
		// Again but with newlines enabled.
		value = reflect.getStringArray(method, true);
		value = value.replace('\n', '_');
		testCase("newlines replaced by underscore", value);
		assertEquals("HOTEL_BRAVO_MIKE_", value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getStringArray(java.lang.reflect.Field, boolean)}.
	 */
	@Test
	public void testGetStringArrayFieldBoolean () {
		String juliett = "JULIETT";
		ReflectPojo pojo = new ReflectPojo(true, 42, juliett);
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Find the String array field.
		Field field = reflect.findField("privateStringArrayField", String[].class);
		if (field != null) {
			testCase("field found", field.getName());
		}
		assertNotNull(field);
		
		// Get the String array value.
		String value = reflect.getStringArray(field, false);
		testCase(field.getName(), value);
		assertEquals("WHISKEYTANGOXRAY", value);
		
		// Again but with newlines enabled.
		value = reflect.getStringArray(field, true);
		value = value.replace('\n', '_');
		testCase("newlines replaced by underscore", value);
		assertEquals("WHISKEY_TANGO_XRAY_", value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getBoolean(java.lang.String)}.
	 */
	@Test
	public void testGetBooleanString () {
		ReflectPojo pojo = new ReflectPojo(true, 22, "ANYTHING");
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Get from a getter.
		pojo.setPrivateBooleanMethod(false);
		String name = "privateBooleanMethod";
		Boolean value = reflect.getBoolean(name);
		if (value == null) {
			testCase(name, "null");
		} else {
			testCase(name, value);
		}
		assertFalse(value);
		
		// Get from a field.
		name = "privateBooleanField";
		value = reflect.getBoolean(name);
		if (value == null) {
			testCase(name, "null");
		} else {
			testCase(name, value);
		}
		assertTrue(value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getBoolean(java.lang.reflect.Method)}.
	 */
	@Test
	public void testGetBooleanMethod () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Set boolean value.
		pojo.setPrivateBooleanMethod(false);
		
		// Find the getter method.
		String name = "isPrivateBooleanMethod";
		Method method = reflect.findMethod(name, boolean.class);
		if (method != null) {
			testCase("method found", method.getName());
		}
		assertNotNull(method);
		
		// Get the boolean value using the getter method.
		boolean value = reflect.getBoolean(method);
		testCase(method.getName(), value);
		assertFalse(value);
		
		// Change to true and test again.
		pojo.setPrivateBooleanMethod(true);
		value = reflect.getBoolean(method);
		testCase(method.getName(), value);
		assertTrue(value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getBoolean(java.lang.reflect.Field)}.
	 */
	@Test
	public void testGetBooleanField () {
		ReflectPojo pojo = new ReflectPojo(false, 99, "Don't Care");
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Find the field.
		String name = "privateBooleanField";
		Field field = reflect.findField(name, boolean.class);
		if (field != null) {
			testCase("field found", field.getName());
		}
		assertNotNull(field);
		
		// Get the value from the field.
		boolean value = reflect.getBoolean(field);
		testCase(field.getName(), value);
		assertFalse(value);
		
		// Change the value and test again.
		reflect.setValue(field, true);
		value = reflect.getBoolean(field);
		testCase(field.getName(), value);
		assertTrue(value);
		
		passed();
	}
	
	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getInteger(java.lang.String)}.
	 */
	@Test
	public void testGetIntegerString () {
		int pojoNumber = 5394;
		ReflectPojo pojo = new ReflectPojo(true, pojoNumber, "ALFA");
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Set integer value.
		int number = 6430;
		pojo.setPrivateIntMethod(number);
		
		// Get integer value via getter.
		String name = "privateIntMethod";
		Integer value = reflect.getInteger(name);
		testCase(name, value);
		assertEquals(number, value.intValue());
		
		// Get integer value via field.
		name = "privateIntField";
		value = reflect.getInteger(name);
		testCase(name, value);
		assertEquals(pojoNumber, value.intValue());
		
		// Check for null on invalid name.
		name = "invalidName";
		value = reflect.getInteger(name);
		testCase(name, value);
		assertNull(value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getInteger(java.lang.reflect.Method)}.
	 */
	@Test
	public void testGetIntegerMethod () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Set integer value.
		int number = 9172;
		pojo.setPrivateIntMethod(number);
		
		// Find getter method.
		String name = "getPrivateIntMethod";
		Method getter = reflect.findMethod(name, int.class);
		if (getter != null) {
			testCase("getter found", getter.getName());
		}
		assertNotNull(getter);
		
		// Get integer value.
		Integer value = reflect.getInteger(getter);
		testCase(getter.getName(), value);
		assertEquals(number, value.intValue());
		
		passed();
	}
	
	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#getInteger(java.lang.reflect.Field)}.
	 */
	@Test
	public void testGetIntegerField () {
		int number = 6931;
		ReflectPojo pojo = new ReflectPojo(true, number, "KILO");
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Find the field.
		String name = "privateIntField";
		Field field = reflect.findField(name, int.class);
		if (field != null) {
			testCase("field found", field.getName());
		}
		assertNotNull(field);
		
		// Get integer value.
		Integer value = reflect.getInteger(field);
		testCase(field.getName(), value);
		assertEquals(number, value.intValue());
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#findGetString(java.lang.String)}.
	 */
	@Test
	public void testFindGetString () {
		String oscar = "OSCAR";
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Set a value.
		pojo.setPrivateStringMethod(oscar);
		
		// Find getter for String.
		String name = "privateStringMethod";
		Method getter = reflect.findGetString(name);
		if (getter != null) {
			testCase("getter found", getter.getName());
		}
		assertNotNull(getter);
		
		// Retrieve the value.
		String value = reflect.getString(getter);
		testCase(getter.getName(), value);
		assertEquals(oscar, value);
		
		passed();
	}
	
	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#findIsBoolean(java.lang.String)}.
	 */
	@Test
	public void testFindIsBoolean () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Set a value.
		pojo.setPrivateBooleanMethod(true);
		
		// Find boolean "is" getter method.
		String name = "privateBooleanMethod";
		Method getter = reflect.findIsBoolean(name);
		if (getter != null) {
			testCase("is getter found", getter.getName());
		}
		assertNotNull(getter);
		
		// Get the value using the getter method.
		Boolean value = reflect.getBoolean(getter);
		testCase(getter.getName(), value);
		assertTrue(value);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#findGetter(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFindGetter () {
		String kilo = "KILO";
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Set some values.
		pojo.setPrivateIntMethod(2490);
		pojo.setPrivateStringMethod(kilo);
		
		// Find getter for int.
		String name = "privateIntMethod";
		Method getter = reflect.findGetter(name, int.class);
		if (getter != null) {
			testCase("int getter found", getter.getName());
		}
		assertNotNull(getter);
		Integer iValue = reflect.getInteger(getter);
		testCase(getter.getName(), iValue.toString());
		assertEquals(2490, iValue.intValue());
		
		// Find getter for String.
		name = "privateStringMethod";
		getter = reflect.findGetter(name, String.class);
		if (getter != null) {
			testCase("String getter found", getter.getName());
		}
		assertNotNull(getter);
		String sValue = reflect.getString(getter);
		testCase(getter.getName(), sValue);
		assertEquals(kilo, sValue);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#findSetter(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFindSetter () {
		String xray = "XRAY";
		int number = 7729;
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Find setter for integer.
		String name = "privateIntMethod";
		Method setter = reflect.findSetter(name, int.class);
		if (setter != null) {
			testCase("int setter found", setter.getName());
		}
		assertNotNull(setter);
		
		// Set integer value.
		reflect.setValue(setter, number);
		int value = pojo.getPrivateIntMethod();
		testCase(setter.getName(), value);
		assertEquals(number, value);
		
		// Find setter for String.
		name = "privateStringMethod";
		setter = reflect.findSetter(name, String.class);
		if (setter != null) {
			testCase("String setter found", setter.getName());
		}
		assertNotNull(setter);
		
		// Set String value.
		reflect.setValue(setter, xray);
		String sValue = pojo.getPrivateStringMethod();
		testCase(setter.getName(), sValue);
		assertEquals(xray, sValue);
		
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgReflect#findOneParam(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFindOneParam () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Find method for a setter.
		String name = "setPrivateBooleanMethod";
		Method setter = reflect.findOneParam(name, boolean.class);
		if (setter != null) {
			testCase("method found", setter.getName());
		}
		assertNotNull(setter);
		
		// Set a value and check result.
		boolean value = pojo.isPrivateBooleanMethod();
		assertFalse(value);
		reflect.setValue(setter, true);
		value = pojo.isPrivateBooleanMethod();
		testCase(setter.getName(), value);
		assertTrue(value);
		
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgReflect#findMethod(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFindMethod () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Find boolean setter method.
		String name = "isPrivateBooleanMethod";
		Method method = reflect.findMethod(name, boolean.class);
		if (method != null) {
			testCase("method found", method.getName());
		}
		assertNotNull(method);
		
		// Find String array getter method.
		name = "getPrivateStringArrayMethod";
		method = reflect.findMethod(name, String[].class);
		if (method != null) {
			testCase("String array method found", method.getName());
		}
		assertNotNull(method);
		
		// Check for null on invalid method name.
		name = "invalidMethodName";
		method = reflect.findMethod(name, String.class);
		testCase(name, method);
		assertNull(method);
		
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgReflect#findField(java.lang.String, java.lang.Class)}.
	 */
	@Test
	public void testFindField () {
		String yankee = "YANKEE";
		ReflectPojo pojo = new ReflectPojo(false, 9999, yankee);
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Find boolean field.
		String name = "privateBooleanField";
		Field field = reflect.findField(name, boolean.class);
		if (field != null) {
			testCase("boolean field found", field.getName());
		}
		assertNotNull(field);
		
		// Find int field.
		name = "privateIntField";
		field = reflect.findField(name, int.class);
		if (field != null) {
			testCase("int field found", field.getName());
		}
		assertNotNull(field);
		
		// Find String field that has setter/getter methods.
		name = "privateStringMethod";
		field = reflect.findField(name, String.class);
		if (field != null) {
			testCase("String field found", field.getName());
		}
		assertNotNull(field);
		
		// Check for null return on invalid field name.
		name = "invalidFieldName";
		field = reflect.findField(name, String.class);
		testCase(name, field);
		assertNull(field);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#setValue(java.lang.reflect.Method, java.lang.Object)}.
	 */
	@Test
	public void testSetValueMethodObject () {
		ReflectPojo pojo = new ReflectPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		
		// Define some values.
		int number = 3964;
		String romeo = "ROMEO";
		
		// Get setter method for boolean.
		String name = "privateBooleanMethod";
		Method setter = reflect.findSetter(name, boolean.class);
		if (setter != null) {
			testCase("boolean setter found", setter.getName());
		}
		assertNotNull(setter);
		
		// Set value for boolean and check thru pojo.
		reflect.setValue(setter, true);
		boolean value = pojo.isPrivateBooleanMethod();
		testCase(setter.getName(), value);
		assertTrue(value);
		
		// Get setter method for int.
		name = "privateIntMethod";
		setter = reflect.findSetter(name, int.class);
		if (setter != null) {
			testCase("int setter found", setter.getName());
		}
		assertNotNull(setter);
		
		// Set value for int and check thru pojo.
		reflect.setValue(setter, number);
		int iValue = pojo.getPrivateIntMethod();
		testCase(setter.getName(), iValue);
		assertEquals(number, iValue);
		
		// Get setter method for String.
		name = "privateStringMethod";
		setter = reflect.findSetter(name, String.class);
		if (setter != null) {
			testCase("String setter found", setter.getName());
		}
		assertNotNull(setter);
		
		// Set value for String and check thru pojo.
		reflect.setValue(setter, romeo);
		String sValue = pojo.getPrivateStringMethod();
		testCase(setter.getName(), sValue);
		assertEquals(romeo, sValue);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgReflect#setValue(java.lang.reflect.Field, java.lang.Object)}.
	 */
	@Test
	public void testSetValueFieldObject () {
		int number = 7392;
		String victor = "VICTOR";
		ReflectPojo pojo = new ReflectPojo(true, number, victor);
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		
		// Find int field.
		String name = "privateIntField";
		Field field = reflect.findField(name, int.class);
		if (field != null) {
			testCase("field found", field.getName());
		}
		assertNotNull(field);
		
		// Check current value as set by constructor.
		int iValue = reflect.getInteger(name);
		testCase("current int value", iValue);
		assertEquals(number, iValue);
		
		// Change int value.
		reflect.setValue(field, 2053);
		iValue = reflect.getInteger(name);
		testCase("new int value", iValue);
		assertEquals(2053, iValue);
		
		// Find String field.
		name = "privateStringField";
		field = reflect.findField(name, String.class);
		if (field != null) {
			testCase("field found", field.getName());
		}
		assertNotNull(field);
		
		// Check current value as set by constructor.
		String sValue = reflect.getString(name);
		testCase("current String value", sValue);
		assertEquals(victor, sValue);
		
		// Change String value.
		reflect.setValue(field, "UNIFORM");
		sValue = reflect.getString(name);
		testCase("new String value", sValue);
		assertEquals("UNIFORM", sValue);
		
		passed();
	}

}
