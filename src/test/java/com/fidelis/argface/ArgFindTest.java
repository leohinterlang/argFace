/**
 *+
 *	ArgFindTest.java
 *	1.0.0  Oct 28, 2014  Leo Hinterlang
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
 * ArgFindTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgFindTest {
	
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
	 * Test method for {@link com.fidelis.argface.ArgFind#ArgFind()}.
	 */
	@Test
	public void testArgFind () {
		ArgFind finder = new ArgFind();
		assertNotNull(finder);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#ArgFind(com.fidelis.argface.ArgReflect)}.
	 */
	@Test
	public void testArgFindArgReflect () {
		ArgReflect reflect = new ArgReflect();
		ArgFind finder = new ArgFind(reflect);
		assertNotNull(reflect);
		assertNotNull(finder);
		assertSame(reflect, finder.getReflect());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#setReflect(com.fidelis.argface.ArgReflect)}.
	 */
	@Test
	public void testSetReflect () {
		ArgReflect reflect = new ArgReflect();
		ArgFind finder = new ArgFind();
		assertNull(finder.getReflect());
		finder.setReflect(reflect);
		ArgReflect finderReflect = finder.getReflect();
		assertNotNull(finderReflect);
		assertSame(reflect, finderReflect);
		passed();
	}
	
	private ArgFind commonSetup () {
		ReflectPojo pojo = new ReflectPojo(true, 9999, "DELTA");
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		ArgFind finder = new ArgFind(reflect);
		ArgUtil util = ArgUtil.getInstance();
		util.setOperandSuffix("");
		util.setOptionSuffix("");
		return finder;
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#findOperandSetter(com.fidelis.argface.ArgOperand)}.
	 */
	@Test
	public void testFindOperandSetter () {
		ArgFind finder = commonSetup();
		ArgOperand operand = new ArgOperand().literal();
		
		// Find setter for boolean.
		operand.setName("private-boolean-method");
		Method setter = finder.findOperandSetter(operand);
		if (setter != null) {
			testCase(operand.getName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateBooleanMethod", setter.getName());
		
		// Try String setter.
		operand.setName("private-string-method");
		setter = finder.findOperandSetter(operand);
		if (setter != null) {
			testCase(operand.getName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateStringMethod", setter.getName());
		
		// Try repeatable operand.
		operand.setName("private-string-array-method");
		operand.setRepeat(true);
		setter = finder.findOperandSetter(operand);
		if (setter != null) {
			testCase(operand.getName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateStringArrayMethod", setter.getName());
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#findOperandField(com.fidelis.argface.ArgOperand)}.
	 */
	@Test
	public void testFindOperandField () {
		ArgFind finder = commonSetup();
		ArgOperand operand = new ArgOperand().variable();
		
		// Try boolean field.
		String name = "private-boolean-field";
		operand.setName(name);
		Field field = finder.findOperandField(operand);
		if (field != null) {
			testCase(operand.getName(), field.getName());
		}
		assertNotNull(field);
		assertEquals("privateBooleanField", field.getName());
		
		// Try String field.
		name = "privateStringField";
		operand.setName(name);
		field = finder.findOperandField(operand);
		if (field != null) {
			testCase(operand.getName(), field.getName());
		}
		assertNotNull(field);
		assertEquals(name, field.getName());
		
		// Try repeating operand field.
		name = "private-string-array-field";
		operand.setName(name);
		operand.setRepeat(true);
		field = finder.findOperandField(operand);
		if (field != null) {
			testCase(operand.getName(), field.getName());
		}
		assertNotNull(field);
		assertEquals("privateStringArrayField", field.getName());
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#findOptionSetter(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testFindOptionSetter () {
		ArgFind finder = commonSetup();
		
		// Find setter for named option.
		ArgOption option = new ArgOption("private-boolean-method");
		Method setter = finder.findOptionSetter(option);
		if (setter != null) {
			testCase(option.getName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateBooleanMethod", setter.getName());
		
		// Try alternate name.
		option.setName("x");
		option.setAltName("privateBooleanMethod");
		setter = finder.findOptionSetter(option);
		if (setter != null) {
			testCase(option.getAltName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateBooleanMethod", setter.getName());
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#findOptionField(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testFindOptionField () {
		ArgFind finder = commonSetup();
		
		// Find field for named option.
		ArgOption option = new ArgOption("private-boolean-field");
		Field field = finder.findOptionField(option);
		if (field != null) {
			testCase(option.getName(), field.getName());
		}
		assertNotNull(field);
		assertEquals("privateBooleanField", field.getName());
		
		// Try alternate name.
		option.setName("x");
		option.setAltName("privateBooleanField");
		field = finder.findOptionField(option);
		if (field != null) {
			testCase(option.getAltName(), field.getName());
		}
		assertNotNull(field);
		assertEquals(option.getAltName(), field.getName());
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#findArgSetter(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testFindArgSetter () {
		ArgFind finder = commonSetup();
		
		// Find setter for String argument.
		ArgOption option = new ArgOption("private");
		option.setArgName("string-method");
		Method setter = finder.findArgSetter(option);
		if (setter != null) {
			testCase(option.getArgName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateStringMethod", setter.getName());
		
		// Try alternate name.
		option.setName("x");
		option.setAltName("private-string");
		option.setArgName("method");
		setter = finder.findArgSetter(option);
		if (setter != null) {
			testCase(option.getAltName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateStringMethod", setter.getName());
		
		// Try String array.
		option.setArgName("array-method");
		option.setRepeat(true);
		setter = finder.findArgSetter(option);
		if (setter != null) {
			testCase(option.getArgName(), setter.getName());
		}
		assertNotNull(setter);
		assertEquals("setPrivateStringArrayMethod", setter.getName());
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgFind#findArgField(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testFindArgField () {
		ArgFind finder = commonSetup();
		
		// Find field for String argument.
		ArgOption option = new ArgOption("private");
		option.setArgName("string-field");
		Field field = finder.findArgField(option);
		if (field != null) {
			testCase(option.getArgName(), field.getName());
		}
		assertNotNull(field);
		assertEquals("privateStringField", field.getName());
		
		// Try alternate name.
		option.setName("x");
		option.setAltName("private-string");
		option.setArgName("field");
		field = finder.findArgField(option);
		if (field != null) {
			testCase(option.getAltName(), field.getName());
		}
		assertNotNull(field);
		assertEquals("privateStringField", field.getName());
		
		// Try String array.
		option.setArgName("array-field");
		option.setRepeat(true);
		field = finder.findArgField(option);
		if (field != null) {
			testCase(option.getArgName(), field.getName());
		}
		assertNotNull(field);
		assertEquals("privateStringArrayField", field.getName());
		
		passed();
	}

}
