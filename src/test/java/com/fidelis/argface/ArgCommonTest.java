/**
 *+
 *	ArgCommonTest.java
 *	1.0.0  Oct 29, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import static com.fidelis.argface.TestOut.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * ArgCommonTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgCommonTest {
	
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
	 * Test method for {@link com.fidelis.argface.ArgCommon#ArgCommon()}.
	 */
	@Test
	public void testArgCommon () {
		ArgCommon common = new ArgCommon();
		assertNotNull(common);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#ArgCommon(com.fidelis.argface.ArgReflect)}.
	 */
	@Test
	public void testArgCommonArgReflect () {
		ArgReflect reflect = new ArgReflect();
		ArgCommon common = new ArgCommon(reflect);
		assertNotNull(reflect);
		assertNotNull(common);
		assertSame(reflect, common.getReflect());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getReflect()}.
	 */
	@Test
	public void testGetReflect () {
		ArgCommon common = new ArgCommon();
		assertNull(common.getReflect());
		ArgReflect reflect = new ArgReflect();
		common.setReflect(reflect);
		assertNotNull(common.getReflect());
		assertSame(reflect, common.getReflect());
		passed();
	}

	private ArgCommon commonSetup () {
		CommonPojo pojo = new CommonPojo();
		ArgReflect reflect = new ArgReflect(pojo);
		reflect.setPrivateAccess(true);
		ArgCommon common = new ArgCommon(reflect);
		ArgUtil util = ArgUtil.getInstance();
		util.setOptionSuffix("Option");
		util.setOperandSuffix("Operand");
		return common;
	}
	
	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getUsageText()}.
	 */
	@Test
	public void testGetUsageText () {
		ArgCommon common = commonSetup();
		String usageText = common.getUsageText();
		testCase("usageText", usageText);
		assertEquals("The usage text.", usageText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getVersionText()}.
	 */
	@Test
	public void testGetVersionText () {
		ArgCommon common = commonSetup();
		String versionText = common.getVersionText();
		testCase("versionText", versionText);
		assertEquals("The version text.", versionText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getAboutText()}.
	 */
	@Test
	public void testGetAboutText () {
		ArgCommon common = commonSetup();
		String aboutText = common.getAboutText();
		testCase("aboutText", aboutText);
		assertEquals("The about text.", aboutText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getHelpText()}.
	 */
	@Test
	public void testGetHelpText () {
		ArgCommon common = commonSetup();
		String helpText = common.getHelpText();
		testCase("helpText", helpText);
		assertEquals("The help text.", helpText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getOperandSuffix()}.
	 */
	@Test
	public void testGetOperandSuffix () {
		ArgCommon common = commonSetup();
		String operandSuffix = common.getOperandSuffix();
		testCase("operandSuffix", operandSuffix);
		assertEquals("TheOperandSuffix", operandSuffix);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getOptionSuffix()}.
	 */
	@Test
	public void testGetOptionSuffix () {
		ArgCommon common = commonSetup();
		String optionSuffix = common.getOptionSuffix();
		testCase("optionSuffix", optionSuffix);
		assertEquals("TheOptionSuffix", optionSuffix);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getAllowOverwrite()}.
	 */
	@Test
	public void testGetAllowOverwrite () {
		ArgCommon common = commonSetup();
		boolean allowOverwrite = common.getAllowOverwrite();
		testCase("allowOverwrite", allowOverwrite);
		assertTrue(allowOverwrite);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getSuppressHelp()}.
	 */
	@Test
	public void testGetSuppressHelp () {
		ArgCommon common = commonSetup();
		boolean suppressHelp = common.getSuppressHelp();
		testCase("suppressHelp", suppressHelp);
		assertTrue(suppressHelp);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getPosixFormat()}.
	 */
	@Test
	public void testGetPosixFormat () {
		ArgCommon common = commonSetup();
		boolean posixFormat = common.getPosixFormat();
		testCase("posixFormat", posixFormat);
		assertTrue(posixFormat);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#getSortOptions()}.
	 */
	@Test
	public void testGetSortOptions () {
		ArgCommon common = commonSetup();
		boolean sortOptions = common.getSortOptions();
		testCase("sortOptions", sortOptions);
		assertTrue(sortOptions);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#setNonOptions(java.util.List)}.
	 */
	@Test
	public void testSetNonOptions () {
		ArgCommon common = commonSetup();
		List<String> nonOptions = new ArrayList<String>();
		nonOptions.add("ALPHA");
		nonOptions.add("HOTEL");
		nonOptions.add("VICTOR");
		common.setNonOptions(nonOptions);
		CommonPojo pojo = (CommonPojo) common.getReflect().getObject();
		List<String> operands = pojo.getOperands();
		for (int n = 0; n < operands.size(); n++) {
			String nonOption = nonOptions.get(n);
			String operand = operands.get(n);
			testCase("nonOption " + n, operand);
			assertEquals(nonOption, operand);
		}
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#postProcess(com.fidelis.argface.ArgOperand)}.
	 */
	@Test
	public void testPostProcessArgOperand () {
		ArgCommon common = commonSetup();
		
		// Post process operand field.
		ArgOperand operand = new ArgOperand().variable();
		operand.setName("one");
		testCase("operand one", operand.getFieldName());
		assertNull(operand.getFieldName());
		assertNull(operand.getField());
		boolean status = common.postProcess(operand);
		testCase(operand.getName(), operand.getFieldName());
		assertTrue(status);
		assertNotNull(operand.getFieldName());
		assertNotNull(operand.getField());
		
		// Try Operand with getter/setter.
		operand = new ArgOperand().variable();
		operand.setName("two");
		testCase("operand two", operand.getSetter());
		status = common.postProcess(operand);
		testCase(operand.getName(), operand.getSetter().getName());
		assertTrue(status);
		assertNotNull(operand.getSetter());
		assertEquals("setTwoOperand", operand.getSetter().getName());
		
		// Try literal operand.
		operand = new ArgOperand().literal();
		operand.setName("three");
		testCase("operand three", operand.getFieldName());
		status = common.postProcess(operand);
		testCase(operand.getName(), operand.getFieldName());
		assertTrue(status);
		assertNotNull(operand.getFieldName());
		assertEquals("threeOperand", operand.getFieldName());
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgCommon#postProcess(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testPostProcessArgOption () {
		ArgCommon common = commonSetup();
		
		// Post process option field.
		ArgOption option = new ArgOption("a");
		testCase("option a", option.getFieldName());
		assertNull(option.getFieldName());
		assertNull(option.getField());
		boolean status = common.postProcess(option);
		testCase(option.getName(), option.getFieldName());
		assertTrue(status);
		assertNotNull(option.getFieldName());
		assertNotNull(option.getField());
		assertEquals("aOption", option.getFieldName());
		
		// Try option with getter/setter.
		option = new ArgOption("b");
		testCase("option b", option.getFieldName());
		assertNull(option.getFieldName());
		assertNull(option.getSetter());
		status = common.postProcess(option);
		testCase(option.getName(), option.getFieldName());
		assertTrue(status);
		assertNotNull(option.getFieldName());
		assertNotNull(option.getSetter());
		assertEquals("bOption", option.getFieldName());
		
		// Field with alternate name.
		option = new ArgOption("x");
		option.setAltName("copy");
		testCase("copy option", option.getFieldName());
		status = common.postProcess(option);
		testCase(option.getAltName(), option.getFieldName());
		assertTrue(status);
		assertNotNull(option.getField());
		assertEquals("copyOption", option.getFieldName());
		
		// Setter with alternate name.
		option = new ArgOption("x");
		option.setAltName("print");
		testCase("print option", option.getFieldName());
		status = common.postProcess(option);
		testCase(option.getAltName(), option.getFieldName());
		assertTrue(status);
		assertNotNull(option.getSetter());
		assertEquals("printOption", option.getFieldName());
		
		// Option argument field.
		option = new ArgOption("c");
		option.setArgName("name");
		testCase("c option <name>", option.getArgFieldName());
		status = common.postProcess(option);
		testCase(option.getArgName(), option.getArgFieldName());
		assertTrue(status);
		assertNotNull(option.getArgField());
		assertEquals("cName", option.getArgFieldName());
		
		// Option argument setter.
		option = new ArgOption("d");
		option.setArgName("arg");
		testCase("d option <arg>", option.getArgFieldName());
		status = common.postProcess(option);
		testCase(option.getArgName(), option.getArgFieldName());
		assertTrue(status);
		assertNotNull(option.getArgSetter());
		assertEquals("dArg", option.getArgFieldName());
		
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgCommon#setProgramVariables(java.util.List, java.util.List)}.
	 */
	@Test
	public void testSetProgramVariables () {
		ArgCommon common = commonSetup();
		
		// Build operand list.
		List<ArgOperand> operands = new ArrayList<ArgOperand>();
		ArgOperand operand = new ArgOperand().variable();
		operand.setName("one");
		operand.setHas(false);
		operand.setValue("LIMA");
		operands.add(operand);
		
		operand = new ArgOperand().variable();
		operand.setName("two");
		operand.setHas(true);
		operand.setValue("MIKE");
		operands.add(operand);
		
		operand = new ArgOperand().literal();
		operand.setName("three");
		operand.setHas(true);
		operands.add(operand);
		
		for (ArgOperand opr : operands) {
			assertTrue(common.postProcess(opr));
		}
		
		// Build option list.
		List<ArgOption> options = new ArrayList<ArgOption>();
		ArgOption option = new ArgOption("a");
		option.setHas(false);
		options.add(option);
		
		option = new ArgOption("b");
		option.setHas(true);
		options.add(option);
		
		option = new ArgOption("x");
		option.setAltName("copy");
		option.setHas(true);
		options.add(option);
		
		option = new ArgOption("c");
		option.setHas(true);
		option.setArgName("name");
		option.setArgValue("SIERRA");
		options.add(option);
		
		option = new ArgOption("d");
		option.setHas(true);
		option.setArgName("arg");
		option.setArgValue("TANGO");
		options.add(option);
		
		for (ArgOption opt : options) {
			assertTrue(common.postProcess(opt));
		}
		
		// Set program variables.
		common.setProgramVariables(operands, options);
		
		// Verify from pojo.
		CommonPojo pojo = (CommonPojo) common.getReflect().getObject();
		
		testCase("one operand", pojo.getOneOperand());
		assertNull(pojo.getOneOperand());
		
		testCase("two operand", pojo.getTwoOperand());
		assertEquals("MIKE", pojo.getTwoOperand());
		
		testCase("three operand", pojo.isThreeOperand());
		assertTrue(pojo.isThreeOperand());
		
		testCase("a option", pojo.isaOption());
		assertFalse(pojo.isaOption());
		
		testCase("b option", pojo.isbOption());
		assertTrue(pojo.isbOption());
		
		testCase("copy option", pojo.isCopyOption());
		assertTrue(pojo.isCopyOption());
		
		testCase("c option", pojo.iscOption());
		assertTrue(pojo.iscOption());
		testCase("c <name>", pojo.getcName());
		assertEquals("SIERRA", pojo.getcName());
		
		testCase("d option", pojo.isdOption());
		assertTrue(pojo.isdOption());
		testCase("d option <arg>", pojo.getdArg());
		assertEquals("TANGO", pojo.getdArg());
		
		passed();
	}

	/**
	 * Test method for
	 *  {@link com.fidelis.argface.ArgCommon#setRepeat(java.lang.reflect.Method,
	 *   java.lang.reflect.Field, java.util.List, boolean)}.
	 */
	@Test
	public void testSetRepeat () {
		ArgCommon common = commonSetup();
		
		// Test String array operand by field.
		ArgOperand operand = new ArgOperand().variable();
		operand.setName("four");
		operand.setRepeat(true);
		boolean status = common.postProcess(operand);
		testCase(operand.getName(), operand.getFieldName());
		assertTrue(status);
		
		// Get the field for four.
		Field field = operand.getField();
		assertNotNull(field);
		
		// Create data list.
		List<String> fourList = new ArrayList<String>();
		String[] fourData = { "FOXTROT", "GOLF", "HOTEL" };
		for (String data : fourData) {
			fourList.add(data);
		}
		
		// Set the repeat data.
		common.setRepeat(null, field, fourList, false);
		
		// Verify through pojo.
		CommonPojo pojo = (CommonPojo) common.getReflect().getObject();
		String[] pojoData = pojo.getFourOperand();
		assertNotNull(pojoData);
		int index = 0;
		for (String data : fourData) {
			testCase("four " + index, pojoData[index]);
			assertEquals(data, pojoData[index]);
			++index;
		}
		
		// Test String array operand by Setter.
		operand = new ArgOperand().variable();
		operand.setName("five");
		operand.setRepeat(true);
		status = common.postProcess(operand);
		testCase(operand.getName(), operand.getFieldName());
		assertTrue(status);
		
		// Get the setter for five.
		Method setter = operand.getSetter();
		assertNotNull(setter);
		
		// Create data list.
		List<String> fiveList = new ArrayList<String>();
		String[] fiveData = { "INDIA", "JULIETT", "KILO" };
		for (String data : fiveData) {
			fiveList.add(data);
		}
		
		// Set the repeat data.
		common.setRepeat(setter, null, fiveList, false);
		
		// Verify through pojo.
		pojoData = pojo.getFiveOperand();
		assertNotNull(pojoData);
		index = 0;
		for (String data : fiveData) {
			testCase("five " + index, data);
			assertEquals(data, pojoData[index]);
			++index;
		}
		
		// Test List<String> operand by field.
		operand = new ArgOperand().variable();
		operand.setName("six");
		operand.setRepeat(true);
		operand.setRepeatList(true);
		status = common.postProcess(operand);
		testCase(operand.getName(), operand.getFieldName());
		assertTrue(status);
		
		// Get the field for six.
		field = operand.getField();
		assertNotNull(field);
		
		// Create data list.
		List<String> sixList = new ArrayList<String>();
		String[] sixData = { "LIMA", "MIKE", "NOVEMBER" };
		for (String data : sixData) {
			sixList.add(data);
		}
		
		// Set the repeat data.
		common.setRepeat(null, field, sixList, true);
		
		// Verify through pojo.
		List<String> pojoList = pojo.getSixOperand();
		index = 0;
		for (String data : sixData) {
			testCase("six " + index, data);
			assertEquals(data, pojoList.get(index));
			++index;
		}
		
		// Test List<String> operand by setter.
		operand = new ArgOperand().variable();
		operand.setName("seven");
		operand.setRepeat(true);
		operand.setRepeatList(true);
		status = common.postProcess(operand);
		testCase(operand.getName(), operand.getFieldName());
		assertTrue(status);
		
		// Get setter for seven.
		setter = operand.getSetter();
		assertNotNull(setter);
		
		// Create data list.
		List<String> sevenList = new ArrayList<String>();
		String[] sevenData = { "OSCAR", "PAPA", "QUEBEC" };
		for (String data : sevenData) {
			sevenList.add(data);
		}
		
		// Set the repeat data.
		common.setRepeat(setter, null, sevenList, true);
		
		// Verify through pojo.
		pojoList = pojo.getSevenOperand();
		index = 0;
		for (String data : sevenData) {
			testCase("seven " + index, data);
			assertEquals(data, pojoList.get(index));
			++index;
		}
		
		passed();
	}

}
