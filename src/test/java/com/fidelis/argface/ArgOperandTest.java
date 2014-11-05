/**
 *+
 *	ArgOperandTest.java
 *	1.0.0  Oct 30, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import static com.fidelis.argface.TestOut.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * ArgOperandTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgOperandTest {
	
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
	 * Test method for {@link com.fidelis.argface.ArgOperand#ArgOperand()}.
	 */
	@Test
	public void testArgOperand () {
		ArgOperand operand = new ArgOperand();
		assertNotNull(operand);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOperand#addList(java.lang.String)}.
	 */
	@Test
	public void testAddList () {
		ArgOperand operand = new ArgOperand();
		List<String> list = new ArrayList<String>();
		operand.setList(list);
		operand.addList("GOLF");
		operand.addList("YANKEE");
		operand.addList("WHISKEY");
		operand.addList("UNIFORM");
		List<String> ol = operand.getList();
		int index = 0;
		testCase("item " + index, ol.get(index));
		assertEquals("GOLF", ol.get(index++));
		testCase("item " + index, ol.get(index));
		assertEquals("YANKEE", ol.get(index++));
		testCase("item " + index, ol.get(index));
		assertEquals("WHISKEY", ol.get(index++));
		testCase("item " + index, ol.get(index));
		assertEquals("UNIFORM", ol.get(index++));
		testCase("size", ol.size());
		assertEquals(index, ol.size());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOperand#getSpecText()}.
	 */
	@Test
	public void testGetSpecText () {
		ArgOperand operand = new ArgOperand().literal();
		operand.setName("match");
		String text = operand.getSpecText();
		testCase("literal match", text);
		operand = new ArgOperand().variable();
		operand.setName("path");
		text = operand.getSpecText();
		testCase("variable path", text);
		assertEquals("<path>", text);
		passed();
	}

}
