/**
 *+
 *	ArgUtilTest.java
 *	1.0.0  Oct 20, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static com.fidelis.argface.TestOut.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardErrorStreamLog;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

import static org.junit.contrib.java.lang.system.LogMode.LOG_ONLY;

import org.junit.rules.TestName;

/**
 * ArgUtilTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgUtilTest {
	
	@Rule public TestName testName = new TestName();
	@Rule public final StandardErrorStreamLog errLog = new StandardErrorStreamLog(LOG_ONLY);
	
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
	 * Test method for {@link com.fidelis.argface.ArgUtil#getInstance()}.
	 */
	@Test
	public void testGetInstance () {
		ArgUtil util = ArgUtil.getInstance();
		assertNotNull(util);
		ArgUtil other = ArgUtil.getInstance();
		assertSame(util, other);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#camelCase(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCamelCaseStringString () {
		camelCase("first", "second", "firstSecond");
		camelCase("a", "option", "aOption");
		camelCase("A", "option", "AOption");
		camelCase("set", "aOption", "setaOption");
		camelCase("set", "AOption", "setAOption");
		camelCase("a", "b", "aB");
		camelCase("a", "B", "aB");
		camelCase("A", "b", "AB");
		camelCase("A", "B", "AB");
		camelCase("A", "bC", "AbC");
		camelCase("A", "BC", "ABC");
		passed();
	}
	
	private void camelCase (String first, String second, String expected) {
		String actual = ArgUtil.camelCase(first, second);
		testCase(first + " " + second, actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#camelCase(java.lang.String)}.
	 */
	@Test
	public void testCamelCaseString () {
		camelText(null, null);
		camelText("first-second", "firstSecond");
		camelText("one-two-three", "oneTwoThree");
		camelText("set-bOption", "setbOption");
		camelText("get-BOption", "getBOption");
		camelText("a-b-c", "aBC");
		camelText("set-a-value", "setAValue");
		camelText("anything", "anything");
		camelText("more-camel-Text", "moreCamelText");
		camelText("more-came-lText", "moreCamelText");
		passed();
	}
	
	private void camelText (String text, String expected) {
		String actual = ArgUtil.camelCase(text);
		testCase(text, actual);
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#cantAccess(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testCantAccessArgOption () {
		ArgOption opt = new ArgOption("a");
		ArgUtil util = ArgUtil.getInstance();
		util.setBase(ArgStandard.create());
		util.setProgramName("testProgram");
		util.setOptionSuffix("TestSuffix");
		errLog.clear();
		ArgUtil.cantAccess(opt);
		String actual = errLog.getLog().trim();
		testCase("-a", actual);
		String expected = "testProgram: Can't access \"aTestSuffix\"";
		assertEquals(expected, actual);
		opt.setAltName("alt-opt-name");
		errLog.clear();
		ArgUtil.cantAccess(opt);
		actual = errLog.getLog().trim();
		testCase("with altOptName", actual);
		expected = "testProgram: Can't access either \"aTestSuffix\" or \"altOptNameTestSuffix\"";
		assertEquals(expected, actual);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#cantAccessArg(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testCantAccessArg () {
		ArgOption opt = new ArgOption("d");
		opt.setArgName("path");
		ArgUtil util = ArgUtil.getInstance();
		util.setBase(ArgStandard.create());
		util.setProgramName("prog");
		util.setOptionSuffix("Opt");
		errLog.clear();
		ArgUtil.cantAccessArg(opt);
		String actual = errLog.getLog().trim();
		testCase("-d <path>", actual);
		String expected = "prog: Can't access \"dPath\"";
		assertEquals(expected, actual);
		opt.setAltName("dir");
		errLog.clear();
		ArgUtil.cantAccessArg(opt);
		actual = errLog.getLog().trim();
		testCase("-d/--dir <path>", actual);
		expected = "prog: Can't access either \"dPath\" or \"dirPath\"";
		assertEquals(expected, actual);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#cantAccess(com.fidelis.argface.ArgOperand)}.
	 */
	@Test
	public void testCantAccessArgOperand () {
		ArgOperand opr = new ArgOperand().literal();
		opr.setFieldName("matchOperand");
		ArgUtil util = ArgUtil.getInstance();
		util.setBase(ArgStandard.create());
		util.setProgramName("test");
		errLog.clear();
		ArgUtil.cantAccess(opr);
		String actual = errLog.getLog().trim();
		testCase("match", actual);
		String expected = "test: Can't access \"matchOperand\"";
		assertEquals(expected, actual);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#cantAccess(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCantAccessStringString () {
		ArgUtil util = ArgUtil.getInstance();
		util.setBase(ArgStandard.create());
		util.setProgramName("testCantAccess");
		String s1 = "StringOne";
		String s2 = "StringTwo";
		errLog.clear();
		ArgUtil.cantAccess(s1, s2);
		String actual = errLog.getLog().trim();
		testCase("StringOne & StringTwo", actual);
		String expected = "testCantAccess: Can't access either \"StringOne\" or \"StringTwo\"";
		assertEquals(expected, actual);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#cantAccess(java.lang.String)}.
	 */
	@Test
	public void testCantAccessString () {
		ArgUtil util = ArgUtil.getInstance();
		util.setBase(ArgStandard.create());
		util.setProgramName("program");
		errLog.clear();
		ArgUtil.cantAccess("testString");
		String actual = errLog.getLog().trim();
		testCase("testString", actual);
		String expected = "program: Can't access \"testString\"";
		assertEquals(expected, actual);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#printArgs(java.lang.String[])}.
	 */
	@Test
	public void testPrintArgs () {
		String[] args = {
				"-a", "-d", "/path", "operand", "-o", "file.txt"
		};
		errLog.clear();
		ArgUtil.printArgs(args);
		String actual = errLog.getLog().trim();
		testCase("args", actual);
		String expected = "\"-a -d /path operand -o file.txt\"";
		assertEquals(expected, actual);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgUtil#printError(java.lang.String)}.
	 */
	@Test
	public void testPrintError () {
		ArgUtil util = ArgUtil.getInstance();
		util.setBase(ArgStandard.create());
		util.setProgramName("anyProgram");
		errLog.clear();
		ArgUtil.printError("This is the error text");
		String actual = errLog.getLog().trim();
		testCase("error text", actual);
		String expected = "anyProgram: This is the error text";
		assertEquals(expected, actual);
		passed();
	}

}
