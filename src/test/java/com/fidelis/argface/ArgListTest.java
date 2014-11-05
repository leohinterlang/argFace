/**
 *+
 *	ArgListTest.java
 *	1.0.0  Nov 1, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import static com.fidelis.argface.TestOut.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * ArgListTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgListTest {
	
	@Rule public TestName testName = new TestName();
	
	private final String alfa = "ALFA";
	private final String bravo = "BRAVO";
	private final String charlie = "CHARLIE";
	private final String delta = "DELTA";
	private final String echo = "ECHO";
	private final String foxtrot = "FOXTROT";
	
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
	 * Test method for {@link com.fidelis.argface.ArgList#ArgList()}.
	 */
	@Test
	public void testArgList () {
		ArgList list = new ArgList();
		assertNotNull(list);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#addEast(java.lang.Object)}.
	 */
	@Test
	public void testAddEastObject () {
		ArgList list = new ArgList();
		
		// Add option "a" to the list.
		ArgOption option = new ArgOption("a");
		ArgNode node = list.addEast(option);
		
		// First node is current.
		ArgNode current = list.getCurrent();
		testCase("first node", "is current");
		assertSame(node, current);
		
		// First node is home.
		ArgNode home = list.getHome();
		testCase("first node", "is home");
		assertSame(node, home);
		
		// First node is option "a".
		ArgOption opt = node.getOption();
		testCase("option " + option.getName(), opt.getName());
		assertSame(option, opt);
		
		// Add an operand to the list.
		ArgOperand operand = new ArgOperand().variable();
		operand.setName("first");
		node = list.addEast(operand);
		
		// New node is current.
		current = list.getCurrent();
		testCase("new node", "is current");
		assertSame(node, current);
		
		// New node is not home.
		home = list.getHome();
		testCase("new node", "is not home");
		assertNotSame(node, home);
		
		// New node is operand "first".
		ArgOperand opr = node.getOperand();
		testCase("operand " + operand.getName(), opr.getName());
		assertSame(operand, opr);
		
		// Add option "b".
		option = new ArgOption("b");
		node = list.addEast(option);
		
		// New node is current.
		current = list.getCurrent();
		testCase("new node", "is current");
		assertSame(node, current);
		
		// New node is not home.
		home = list.getHome();
		testCase("new node", "is not home");
		assertNotSame(node, home);
		
		// New node is option "b".
		opt = node.getOption();
		testCase("option " + option.getName(), opt.getName());
		
		// Verify from home eastward.
		node = list.goHome();
		opt = node.getOption();
		testCase("option", opt.getName());
		assertEquals("a", opt.getName());
		node = list.goEast();
		opr = node.getOperand();
		testCase("operand", opr.getName());
		assertEquals("first", opr.getName());
		node = list.goEast();
		opt = node.getOption();
		testCase("option", opt.getName());
		assertEquals("b", opt.getName());
		node = list.goEast();
		testCase("end of list", node);
		assertNull(node);
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgList#addEast(java.lang.Object, boolean)}.
	 */
	@Test
	public void testAddEastObjectBoolean () {
		ArgList list = new ArgList();
		
		// Add to the list.
		list.addEast(alfa, false);
		list.addEast(bravo, true);
		list.addEast(charlie, true);
		
		// Validate ordering and optional setting.
		valOptional(list.goHome(), 0, alfa, false);
		valOptional(list.goEast(), 1, bravo, true);
		valOptional(list.goEast(), 2, charlie, true);
				
		passed();
	}
	
	private ArgNode valOptional (ArgNode node, int index, String expected,
			boolean optional) {
		valEntry(node, index, expected);
		assertEquals(optional, node.isOptional());
		return node;
	}
	
	private ArgNode valEntry (ArgNode node, int index, String expected) {
		String entry = (String) node.getEntry();
		testCase("entry " + index, entry);
		assertEquals(expected, entry);
		return node;
	}
	
	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgList#insertEast(java.lang.Object)}.
	 */
	@Test
	public void testInsertEast () {
		ArgList list = new ArgList();
		
		// Insert adds each to the end of the list.
		list.insertEast(alfa);
		list.insertEast(bravo);
		list.insertEast(charlie);
		
		// Validate ordering.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goEast(), 1, bravo);
		valEntry(list.goEast(), 2, charlie);
		
		// Insert after first entry.
		list.goHome();
		list.insertEast(delta);
		
		// Validate order.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goEast(), 1, delta);
		valEntry(list.goEast(), 2, bravo);
		valEntry(list.goEast(), 3, charlie);
		
		// Insert after last entry.
		list.insertEast(echo);
		
		// Validate.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goEast(), 1, delta);
		valEntry(list.goEast(), 2, bravo);
		valEntry(list.goEast(), 3, charlie);
		valEntry(list.goEast(), 4, echo);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#addSouth(java.lang.Object)}.
	 */
	@Test
	public void testAddSouthObject () {
		ArgList list = new ArgList();
		
		// Add to the list.
		list.addSouth(alfa);
		list.addSouth(bravo);
		list.addSouth(charlie);
		
		// Validate ordering.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goSouth(), 1, bravo);
		valEntry(list.goSouth(), 2, charlie);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#addSouth(java.lang.Object, boolean)}.
	 */
	@Test
	public void testAddSouthObjectBoolean () {
		ArgList list = new ArgList();
		
		// Add to list with optional setting.
		list.addSouth(alfa, true);
		list.addSouth(bravo, false);
		list.addSouth(charlie, false);
		
		// Validate ordering and optional setting.
		valOptional(list.goHome(), 0, alfa, true);
		valOptional(list.goSouth(), 1, bravo, false);
		valOptional(list.goSouth(), 2, charlie, false);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#appendSouth(java.lang.Object)}.
	 */
	@Test
	public void testAppendSouthObject () {
		ArgList list = new ArgList();
		
		// Append to list.
		list.appendSouth(alfa);
		list.appendSouth(bravo);
		list.appendSouth(charlie);
		
		// Validate ordering.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goSouth(), 1, bravo);
		valEntry(list.goSouth(), 2, charlie);
		
		// Set current to home and append.
		list.goHome();
		list.appendSouth(delta);
		
		// Validate ordering.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goSouth(), 1, bravo);
		valEntry(list.goSouth(), 2, charlie);
		valEntry(list.goSouth(), 3, delta);
		
		// Add an entry to the east of bravo.
		list.goHome();
		list.goSouth();
		list.addEast(echo);
		
		// Append south from echo.
		list.appendSouth(foxtrot);
		
		// Validate path to foxtrot.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goSouth(), 1, bravo);
		valEntry(list.goEast(), 2, echo);
		valEntry(list.goSouth(), 3, foxtrot);
		
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgList#appendSouth(java.lang.Object, boolean)}.
	 */
	@Test
	public void testAppendSouthObjectBoolean () {
		ArgList list = new ArgList();
		
		// Append to list with optional setting.
		list.appendSouth(alfa, false);
		list.appendSouth(bravo, true);
		list.appendSouth(charlie, false);
		
		// Validate ordering and optional setting.
		valOptional(list.goHome(), 0, alfa, false);
		valOptional(list.goSouth(), 1, bravo, true);
		valOptional(list.goSouth(), 2, charlie, false);
		
		// Set current to home and append.
		list.goHome();
		list.appendSouth(delta, true);
		
		// Validate ordering and optional setting.
		valOptional(list.goHome(), 0, alfa, false);
		valOptional(list.goSouth(), 1, bravo, true);
		valOptional(list.goSouth(), 2, charlie, false);
		valOptional(list.goSouth(), 3, delta, true);
		
		// Add east node to bravo.
		list.goHome();
		list.goSouth();
		list.addEast(echo, true);
		
		// Append south of echo.
		list.appendSouth(foxtrot, false);
		
		// Validate path to foxtrot.
		valOptional(list.goHome(), 0, alfa, false);
		valOptional(list.goSouth(), 1, bravo, true);
		valOptional(list.goEast(), 2, echo, true);
		valOptional(list.goSouth(), 3, foxtrot, false);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#goHome()}.
	 */
	@Test
	public void testGoHome () {
		ArgList list = new ArgList();
		
		// Empty list.
		ArgNode node = list.goHome();
		testCase("empty list", node);
		assertNull(node);
		
		// Add first entry.
		list.addEast(alfa);
		valEntry(list.goHome(), 0, alfa);
		
		// Add other entries.
		list.appendSouth(bravo);
		list.addEast(charlie);
		list.appendSouth(delta);
		list.addEast(echo);
		list.addSouth(foxtrot);
		
		// Validate eastern path.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goEast(), 1, charlie);
		valEntry(list.goEast(), 2, echo);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#getCurrent()}.
	 */
	@Test
	public void testGetCurrent () {
		ArgList list = new ArgList();
		
		// Empty list.
		ArgNode node = list.getCurrent();
		testCase("empty list", node);
		assertNull(node);
		
		// Add first entry.
		list.addEast(alfa);
		valEntry(list.getCurrent(), 0, alfa);
		
		// Add another.
		list.addEast(bravo);
		valEntry(list.getCurrent(), 1, bravo);
		list.addEast(charlie);
		valEntry(list.getCurrent(), 2, charlie);
		
		// Append south.
		list.appendSouth(delta);
		valEntry(list.getCurrent(), 3, charlie);
		list.appendSouth(echo);
		valEntry(list.getCurrent(), 4, charlie);
		
		// Validate south and home.
		valEntry(list.goSouth(), 5, delta);
		valEntry(list.goSouth(), 6, echo);
		list.goHome();
		valEntry(list.getCurrent(), 0, alfa);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#setCurrent(com.fidelis.argface.ArgNode)}.
	 */
	@Test
	public void testSetCurrent () {
		ArgList list = new ArgList();
		
		// Add to list.
		list.addEast(alfa);
		ArgNode node = list.addEast(bravo);
		list.addEast(charlie);
		
		// Set current to bravo.
		list.setCurrent(node);
		valEntry(list.getCurrent(), 0, bravo);
		
		// Add south of bravo.
		node = list.addSouth(delta);
		list.addSouth(echo);
		valEntry(list.getCurrent(), 1, echo);
		list.setCurrent(node);
		valEntry(list.getCurrent(), 2, delta);
		
		// Check append south from home.
		list.goHome();
		list.appendSouth(foxtrot);
		valEntry(list.getCurrent(), 3, alfa);
		valEntry(list.goSouth(), 4, foxtrot);
		
		// Jump to delta.
		list.setCurrent(node);
		valEntry(list.getCurrent(), 5, delta);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#goSouth()}.
	 */
	@Test
	public void testGoSouth () {
		ArgList list = new ArgList();
		
		// Empty list.
		ArgNode node = list.goSouth();
		testCase("empty list", node);
		assertNull(node);
		
		// Add south.
		list.addSouth(alfa);
		list.addSouth(bravo);
		list.addSouth(charlie);
		
		// Validate southern navigation.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goSouth(), 1, bravo);
		valEntry(list.goSouth(), 2, charlie);
		
		// Add east to charlie then south.
		list.addEast(delta);
		list.appendSouth(echo);
		valEntry(list.goSouth(), 0, echo);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#goEast()}.
	 */
	@Test
	public void testGoEast () {
		ArgList list = new ArgList();
		
		// Empty list.
		ArgNode node = list.goEast();
		testCase("empty list", node);
		assertNull(node);
		
		// Add to list.
		list.addEast(alfa);
		list.addEast(bravo);
		list.addEast(charlie);
		
		// Validate list.
		valEntry(list.goHome(), 0, alfa);
		valEntry(list.goEast(), 1, bravo);
		valEntry(list.goEast(), 2, charlie);
		node = list.goEast();
		testCase("end list", node);
		assertNull(node);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#endEast()}.
	 */
	@Test
	public void testEndEast () {
		ArgList list = new ArgList();
		
		// Empty list.
		ArgNode node = list.endEast();
		testCase("empty list", node);
		assertNull(node);
		
		// Add to list.
		list.addEast(alfa);
		list.addEast(bravo);
		list.addEast(charlie);
		
		// Validate last entry.
		valEntry(list.endEast(), 0, charlie);
		
		// From home.
		list.goHome();
		valEntry(list.endEast(), 1, charlie);
		
		// From middle of list.
		list.goHome();
		list.goEast();
		valEntry(list.endEast(), 2, charlie);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#endSouth()}.
	 */
	@Test
	public void testEndSouth () {
		ArgList list = new ArgList();
		
		// Empty list.
		ArgNode node = list.endSouth();
		testCase("empty list", node);
		assertNull(node);
		
		// Add to list.
		list.addSouth(alfa);
		list.addSouth(bravo);
		list.addSouth(charlie);
		
		// Validate last entry.
		valEntry(list.endSouth(), 0, charlie);
		
		// From home.
		list.goHome();
		valEntry(list.endSouth(), 1, charlie);
		
		// From middle of list.
		list.goHome();
		list.goSouth();
		valEntry(list.endSouth(), 2, charlie);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#setOptional(boolean)}.
	 */
	@Test
	public void testSetOptional () {
		ArgList list = new ArgList();
		
		// Empty list.
		list.setOptional(true);
		ArgNode node = list.goHome();
		testCase("empty list", node);
		assertNull(node);
		
		// Add to list.
		list.addEast(alfa);
		list.addEast(bravo);
		list.addEast(charlie);
		
		// Set bravo and charlie to optional.
		list.goHome();
		list.goEast();
		list.setOptional(true);
		list.goEast();
		list.setOptional(true);
		
		// Add 2 more entries.
		list.addEast(delta, false);
		list.addEast(echo, true);
		
		// Validate optional setting.
		valOptional(list.goHome(), 0, alfa, false);
		valOptional(list.goEast(), 1, bravo, true);
		valOptional(list.goEast(), 2, charlie, true);
		valOptional(list.goEast(), 3, delta, false);
		valOptional(list.goEast(), 4, echo, true);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#setRepeat(boolean)}.
	 */
	@Test
	public void testSetRepeat () {
		ArgList list = new ArgList();
		
		// Create list. Delta is repeat.
		list.addEast(alfa);
		list.addEast(bravo);
		list.addEast(charlie);
		list.addEast(delta);
		list.setRepeat(true);
		list.addEast(echo);
		
		// Set bravo also as repeat.
		list.goHome();
		list.goEast();
		list.setRepeat(true);
		
		// Validate repeat setting.
		valRepeat(list.goHome(), 0, alfa, false);
		valRepeat(list.goEast(), 1, bravo, true);
		valRepeat(list.goEast(), 2, charlie, false);
		valRepeat(list.goEast(), 3, delta, true);
		valRepeat(list.goEast(), 4, echo, false);
		
		passed();
	}
	
	private ArgNode valRepeat (ArgNode node, int index, String expected,
			boolean repeat) {
		valEntry(node, index, expected);
		assertEquals(repeat, node.isRepeat());
		return node;
	}
	
	/**
	 * Test method for {@link com.fidelis.argface.ArgList#baseCount()}.
	 */
	@Test
	public void testBaseCount () {
		ArgList list = new ArgList();
		
		// Create 4 options.
		ArgOption aOption = new ArgOption("a");
		ArgOption bOption = new ArgOption("b");
		ArgOption cOption = new ArgOption("c");
		ArgOption dOption = new ArgOption("d");
		
		// Create 5 operands.
		ArgOperand firstOperand = new ArgOperand().literal();
		firstOperand.setName("first");
		ArgOperand secondOperand = new ArgOperand().literal();
		secondOperand.setName("second");
		ArgOperand oneOperand = new ArgOperand().variable();
		oneOperand.setName("one");
		ArgOperand twoOperand = new ArgOperand().variable();
		twoOperand.setName("two");
		ArgOperand threeOperand = new ArgOperand().variable();
		threeOperand.setName("three");
		
		// Empty list.
		int count = list.baseCount();
		testCase("empty list", count);
		assertEquals(0, count);
		
		// Add all options.
		list.addEast(aOption);
		list.addEast(bOption);
		list.addEast(cOption);
		list.addEast(dOption);
		
		// Check list with just options.
		count = list.baseCount();
		testCase("options only", count);
		assertEquals(0, count);
		
		// Add 2 of the operands.
		list.goHome();
		list.insertEast(firstOperand);
		list.goEast();
		list.insertEast(secondOperand);
		
		// Validate count.
		count = list.baseCount();
		testCase("2 literal operands", count);
		assertEquals(2, count);
		
		// Add other operands.
		list.goEast();
		list.insertEast(oneOperand);
		list.insertEast(twoOperand);
		list.endEast();
		list.insertEast(threeOperand);
		
		// Validate count.
		count = list.baseCount();
		testCase("5 operands, 4 options", count);
		assertEquals(5, count);
		
		// Add other non-options.
		list.addEast(alfa);
		list.addEast(bravo);
		list.addEast(charlie);
		
		// Count should be 8.
		count = list.baseCount();
		testCase("8 non-options", count);
		assertEquals(8, count);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#transfer(com.fidelis.argface.ArgList)}.
	 */
	@Test
	public void testTransfer () {
		ArgList listA = new ArgList();
		ArgList listB = new ArgList();
		
		// Load items in list A.
		listA.addEast(alfa);
		listA.addEast(bravo);
		listA.addEast(charlie);
		
		// This last item will be replaced and is optional.
		listA.addEast("REPLACE", true);
		
		// Load list B in the southern direction.
		listB.addSouth(delta);
		listB.addSouth(echo);
		listB.addSouth(foxtrot);
		
		// Transfer B to A.
		listA.transfer(listB);
		
		// Validate entries and optional settings.
		valOptional(listA.goHome(), 0, alfa, false);
		valOptional(listA.goEast(), 1, bravo, false);
		valOptional(listA.goEast(), 2, charlie, false);
		valOptional(listA.goEast(), 3, delta, true);
		valOptional(listA.goSouth(), 4, echo, true);
		valOptional(listA.goSouth(), 5, foxtrot, true);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgList#buildUsage()}.
	 */
	@Test
	public void testBuildUsage () {
		ArgList list = new ArgList();
		ArgOperand prog = new ArgOperand();
		prog.setName("program");
		list.addEast(prog);
		String text = list.buildUsage().replace('\n', '_');
		testCase("1", text);
		assertEquals("Usage:_  program _", text);
		
		ArgOption aOption = new ArgOption("a");
		ArgOption bOption = new ArgOption("b");
		bOption.setAltName("binary");
		ArgOption cOption = new ArgOption("c");
		cOption.setArgName("name");
		cOption.setArgOptional(true);
		aOption.setSpec(1);
		bOption.setSpec(3);
		cOption.setSpec(1);
		list.addEast(aOption, true);
		list.addEast(bOption, true);
		list.addEast(cOption, true);
		text = list.buildUsage().replace('\n', '_');
		testCase("2", text);
		assertEquals("Usage:_  program [-a] [-b|--binary] [-c [name]] _", text);
		
		ArgList group = new ArgList();
		ArgOption xOption = new ArgOption("x");
		ArgOption yOption = new ArgOption("y");
		xOption.setSpec(1);
		yOption.setSpec(1);
		group.addEast(xOption, true);
		group.addSouth(yOption, true);
		list.addEast(group);
		text = list.buildUsage().replace('\n', '_');
		testCase("3", text);
		assertEquals("Usage:_  program [-a] [-b|--binary] [-c [name]] " +
				"([-x] | [-y]) _",
				text);
		
		ArgOperand first = new ArgOperand().literal();
		first.setName("first");
		ArgOperand one = new ArgOperand().variable();
		one.setName("one");
		ArgOperand two = new ArgOperand().variable();
		two.setName("two");
		list.addEast(first);
		list.addEast(one);
		list.addEast(two, true);
		text = list.buildUsage().replace('\n', '_');
		testCase("4", text);
		assertEquals("Usage:_  program [-a] [-b|--binary] [-c [name]] " +
				"([-x] | [-y]) first <one> [<two>] _",
				text);
		
		passed();
	}

}
