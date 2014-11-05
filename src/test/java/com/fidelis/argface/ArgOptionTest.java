/**
 *+
 *	ArgOptionTest.java
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
 * ArgOptionTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgOptionTest {
	
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
	 * Test method for {@link com.fidelis.argface.ArgOption#ArgOption()}.
	 */
	@Test
	public void testArgOption () {
		ArgOption option = new ArgOption();
		assertNotNull(option);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOption#ArgOption(java.lang.String)}.
	 */
	@Test
	public void testArgOptionString () {
		ArgOption option = new ArgOption("dir");
		assertNotNull(option);
		testCase("option name", option.getName());
		assertEquals("dir", option.getName());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOption#getText()}.
	 */
	@Test
	public void testGetText () {
		ArgOption option = new ArgOption("a");
		String text = option.getText();
		testCase("-a", text);
		assertEquals("-a", text);
		option.setAltName("all");
		text = option.getText();
		testCase("-a, --all", text);
		assertEquals("-a, --all", text);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOption#getSpecText()}.
	 */
	@Test
	public void testGetSpecText () {
		ArgOption option = new ArgOption("b");
		option.setAltName("binary");
		option.setArgName("arg");
		
		option.setSpec(0);
		String text = option.getSpecText();
		testCase("spec type 0", text);
		assertNull(text);
		
		option.setSpec(1);
		text = option.getSpecText();
		testCase("spec type 1", text);
		assertEquals("-b <arg>", text);
		
		option.setSpec(2);
		text = option.getSpecText();
		testCase("spec type 2", text);
		assertEquals("--binary <arg>", text);
		
		option.setSpec(3);
		text = option.getSpecText();
		testCase("spec type 3", text);
		assertEquals("-b|--binary <arg>", text);
		
		option.setArgOptional(true);
		text = option.getSpecText();
		testCase("optional argument", text);
		assertEquals("-b|--binary [arg]", text);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOption#nameMatch(java.lang.String)}.
	 */
	@Test
	public void testNameMatch () {
		ArgOption option = new ArgOption("c");
		boolean match = option.nameMatch("c");
		testCase("-c", match);
		assertTrue(match);
		match = option.nameMatch("a");
		testCase("-a", match);
		assertFalse(match);
		option.setAltName("config");
		match = option.nameMatch("config");
		testCase("--config", match);
		assertTrue(match);
		match = option.nameMatch("all");
		testCase("--all", match);
		assertFalse(match);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOption#addList(java.lang.String)}.
	 */
	@Test
	public void testAddList () {
		ArgOption option = new ArgOption("list");
		List<String> list = new ArrayList<String>();
		option.setList(list);
		option.addList("BRAVO");
		option.addList("VICTOR");
		option.addList("OSCAR");
		option.addList("ALFA");
		List<String> ol = option.getList();
		int index = 0;
		testCase("item " + index, ol.get(index));
		assertEquals("BRAVO", ol.get(index++));
		testCase("item " + index, ol.get(index));
		assertEquals("VICTOR", ol.get(index++));
		testCase("item " + index, ol.get(index));
		assertEquals("OSCAR", ol.get(index++));
		testCase("item " + index, ol.get(index));
		assertEquals("ALFA", ol.get(index++));
		int size = ol.size();
		testCase("size", size);
		assertEquals(index, size);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgOption#compareTo(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testCompareTo () {
		ArgOption option = new ArgOption("e");
		ArgOption other = new ArgOption("f");
		int cmp = option.compareTo(other);
		testCase("-e to -f", cmp);
		assertTrue(cmp < 0);
		cmp = other.compareTo(option);
		testCase("-f to -e", cmp);
		assertTrue(cmp > 0);
		cmp = option.compareTo(option);
		testCase("-e to -e", cmp);
		assertTrue(cmp == 0);
		passed();
	}

}
