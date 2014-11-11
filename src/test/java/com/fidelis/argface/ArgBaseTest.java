/**
 *+
 *	ArgBaseTest.java
 *	1.0.0  Nov 6, 2014  Leo Hinterlang
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
 * ArgBaseTest
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgBaseTest {
	
	@Rule public TestName testName = new TestName();
	
	private BaseModel model = new BaseModel();

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
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetUsageText()}.
	 */
	@Test
	public void testModelGetUsageText () {
		String usageText = model.modelGetUsageText();
		testCase("model usage text", usageText);
		assertEquals("the usage text", usageText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetVersionText()}.
	 */
	@Test
	public void testModelGetVersionText () {
		String versionText = model.modelGetVersionText();
		testCase("model version text", versionText);
		assertEquals("the version text", versionText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetAboutText()}.
	 */
	@Test
	public void testModelGetAboutText () {
		String aboutText = model.modelGetAboutText();
		testCase("model about text", aboutText);
		assertEquals("the about text", aboutText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetHelpText()}.
	 */
	@Test
	public void testModelGetHelpText () {
		String helpText = model.modelGetHelpText();
		testCase("model help text", helpText);
		assertEquals("the help text", helpText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetAllowOverwrite()}.
	 */
	@Test
	public void testModelGetAllowOverwrite () {
		Boolean allowOverwrite = model.modelGetAllowOverwrite();
		testCase("model allowOverwrite", allowOverwrite);
		assertNull(allowOverwrite);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetSuppressHelp()}.
	 */
	@Test
	public void testModelGetSuppressHelp () {
		boolean suppressHelp = model.modelGetSuppressHelp();
		testCase("model suppressHelp", suppressHelp);
		assertFalse(suppressHelp);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetPosixFormat()}.
	 */
	@Test
	public void testModelGetPosixFormat () {
		boolean posixFormat = model.modelGetPosixFormat();
		testCase("model posixFormat", posixFormat);
		assertFalse(posixFormat);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetSortOptions()}.
	 */
	@Test
	public void testModelGetSortOptions () {
		boolean sortOptions = model.modelGetSortOptions();
		testCase("model sortOptions", sortOptions);
		assertFalse(sortOptions);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetOperandSuffix()}.
	 */
	@Test
	public void testModelGetOperandSuffix () {
		String operandSuffix = model.modelGetOperandSuffix();
		testCase("model operandSuffix", operandSuffix);
		assertEquals("ModelOperandSuffix", operandSuffix);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#modelGetOptionSuffix()}.
	 */
	@Test
	public void testModelGetOptionSuffix () {
		String optionSuffix = model.modelGetOptionSuffix();
		testCase("model optionSuffix", optionSuffix);
		assertEquals("ModelOptionSuffix", optionSuffix);
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgBase#modelPostProcess(com.fidelis.argface.ArgOperand)}.
	 */
	@Test
	public void testModelPostProcessArgOperand () {
		model.setPostProcess(true);
		ArgOperand operand = new ArgOperand().variable();
		operand.setName("one");
		boolean status = model.modelPostProcess(operand);
		testCase("model postProcess operand", operand.getName());
		assertTrue(status);
		assertEquals("post-process-operand-one", operand.getName());
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgBase#modelPostProcess(com.fidelis.argface.ArgOption)}.
	 */
	@Test
	public void testModelPostProcessArgOption () {
		model.setPostProcess(true);
		ArgOption option = new ArgOption("a");
		boolean status = model.modelPostProcess(option);
		testCase("model postProcess option", option.getName());
		assertTrue(status);
		assertEquals("post-process-option-a", option.getName());
		passed();
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgBase#modelSetNonOptions(java.util.List)}.
	 */
	@Test
	public void testModelSetNonOptions () {
		List<String> nonOptionList = new ArrayList<String>();
		nonOptionList.add("ALFA");
		nonOptionList.add("BRAVO");
		nonOptionList.add("CHARLIE");
		model.modelSetNonOptions(nonOptionList);
		valNonOption(0, "ALFA");
		valNonOption(1, "BRAVO");
		valNonOption(2, "CHARLIE");
		passed();
	}
	
	private void valNonOption (int index, String name) {
		String nonOption = model.getNonOption(index);
		testCase("nonOption " + index, nonOption);
		assertEquals(name, nonOption);
	}

	/**
	 * Test method for
	 * {@link com.fidelis.argface.ArgBase#modelSetProgramVariables(java.util.List, java.util.List)}.
	 */
	@Test
	public void testModelSetProgramVariables () {
		List<ArgOperand> operandList = buildLitList();
		operandList.addAll(buildVarList());
		List<ArgOption> optionList = buildOptionList();
		model.modelSetProgramVariables(operandList, optionList);
		valOperand(0, "alfa");
		valOperand(1, "bravo");
		valOperand(2, "charlie");
		valOperand(3, "one");
		valOperand(4, "two");
		valOperand(5, "three");
		valOption(0, "a");
		ArgOption opt = valOption(1, "b");
		testCase("alternate name", opt.getAltName());
		assertEquals("binary", opt.getAltName());
		opt = valOption(2, "c");
		testCase("argument name", opt.getArgName());
		assertEquals("feature", opt.getArgName());
		passed();
	}
	
	private List<ArgOperand> buildLitList () {
		List<ArgOperand> litList = new ArrayList<ArgOperand>();
		ArgOperand alfa = new ArgOperand().literal();
		alfa.setName("alfa");
		ArgOperand bravo = new ArgOperand().literal();
		bravo.setName("bravo");
		ArgOperand charlie = new ArgOperand().literal();
		charlie.setName("charlie");
		litList.add(alfa);
		litList.add(bravo);
		litList.add(charlie);
		return litList;
	}
	
	private List<ArgOperand> buildVarList () {
		List<ArgOperand> varList = new ArrayList<ArgOperand>();
		ArgOperand one = new ArgOperand().variable();
		one.setName("one");
		ArgOperand two = new ArgOperand().variable();
		two.setName("two");
		ArgOperand three = new ArgOperand().variable();
		three.setName("three");
		varList.add(one);
		varList.add(two);
		varList.add(three);
		return varList;
	}
	
	private List<ArgOption> buildOptionList () {
		List<ArgOption> optionList = new ArrayList<ArgOption>();
		ArgOption aOpt = new ArgOption("a");
		ArgOption bOpt = new ArgOption("b");
		bOpt.setAltName("binary");
		ArgOption cOpt = new ArgOption("c");
		cOpt.setArgName("feature");
		optionList.add(aOpt);
		optionList.add(bOpt);
		optionList.add(cOpt);
		return optionList;
	}
	
	private ArgOperand valOperand (int index, String name) {
		ArgOperand operand = model.getOperand(index);
		testCase("operand " + index, operand.getName());
		assertEquals(name, operand.getName());
		return operand;
	}
	
	private ArgOption valOption (int index, String name) {
		ArgOption option = model.getOption(index);
		testCase("option " + index, option.getName());
		assertEquals(name, option.getName());
		return option;
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#has(java.lang.String)}.
	 */
	@Test
	public void testHas () {
		List<ArgOperand> litList = buildLitList();
		List<ArgOperand> varList = buildVarList();
		List<ArgOption> optionList = buildOptionList();
		for (ArgOperand operand : litList) {
			if ("bravo".equals(operand.getName())) {
				operand.setHas(true);
			}
		}
		for (ArgOperand operand : varList) {
			if ("one".equals(operand.getName())
					|| "three".equals(operand.getName())) {
				operand.setHas(true);
			}
		}
		for (ArgOption option : optionList) {
			if ("a".equals(option.getName())
					|| "b".equals(option.getName())) {
				option.setHas(true);
			}
		}
		model.setLitList(litList);
		model.setVarList(varList);
		model.setOptionList(optionList);
		valHas("alfa", false);
		valHas("bravo", true);
		valHas("<charlie>", false);
		valHas("<one>", true);
		valHas("two", false);
		valHas("three", true);
		valHas("-a", true);
		valHas("--binary", true);
		valHas("--c", false);
		valHas("bogus", false);
		
		passed();
	}
	
	private void valHas (String name, boolean expected) {
		boolean has = model.has(name);
		testCase("has " + name, has);
		assertEquals(expected, has);
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#count(java.lang.String)}.
	 */
	@Test
	public void testCount () {
		List<ArgOperand> litList = buildLitList();
		List<ArgOperand> varList = buildVarList();
		List<ArgOption> optionList = buildOptionList();
		for (ArgOperand operand : litList) {
			if ("charlie".equals(operand.getName())) {
				operand.setCount(3);
			}
		}
		for (ArgOperand operand : varList) {
			if ("one".equals(operand.getName())) {
				operand.setCount(4);
			}
			else if ("two".equals(operand.getName())) {
				operand.setCount(2);
			}
		}
		for (ArgOption option : optionList) {
			if ("b".equals(option.getName())) {
				option.setCount(3);
			}
			else if ("c".equals(option.getName())) {
				option.setCount(7);
			}
		}
		model.setLitList(litList);
		model.setVarList(varList);
		model.setOptionList(optionList);
		valCount("<alfa>", 0);
		valCount("bravo", 0);
		valCount("charlie", 3);
		valCount("one", 4);
		valCount("<two>", 2);
		valCount("<three>", 0);
		valCount("--a", 0);
		valCount("binary", 3);
		valCount("c", 7);
		valCount("bogus", 0);
		
		passed();
	}
	
	private void valCount (String name, int expected) {
		int count = model.count(name);
		testCase("count " + name, count);
		assertEquals(expected, count);
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#value(java.lang.String)}.
	 */
	@Test
	public void testValue () {
		List<ArgOperand> litList = buildLitList();
		List<ArgOperand> varList = buildVarList();
		List<ArgOption> optionList = buildOptionList();
		for (ArgOperand operand : litList) {
			operand.setValue(operand.getName());
		}
		for (ArgOperand operand : varList) {
			operand.setValue("value " + operand.getName());
		}
		for (ArgOption option : optionList) {
			option.setArgValue("value " + option.getName());
		}
		model.setLitList(litList);
		model.setVarList(varList);
		model.setOptionList(optionList);
		valValue("alfa", "alfa");
		valValue("bravo", "bravo");
		valValue("charlie", "charlie");
		valValue("one", "value one");
		valValue("two", "value two");
		valValue("three", "value three");
		valValue("-a", "value a");
		valValue("-b", "value b");
		valValue("-c", "value c");
		valValue("bogus", null);
		passed();
	}
	
	private void valValue (String name, String expected) {
		String value = model.value(name);
		testCase("value for " + name, value);
		assertEquals(expected, value);
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#valueArray(java.lang.String)}.
	 */
	@Test
	public void testValueArray () {
		List<ArgOperand> litList = buildLitList();
		List<ArgOperand> varList = buildVarList();
		List<ArgOption> optionList = buildOptionList();
		List<String> list = new ArrayList<String>();
		list.add("DELTA");
		list.add("ECHO");
		list.add("FOXTROT");
		for (ArgOperand operand : litList) {
			if ("alfa".equals(operand.getName())) {
				operand.setList(list);
			}
		}
		for (ArgOperand operand : varList) {
			if ("one".equals(operand.getName())) {
				operand.setList(list);
			}
		}
		for (ArgOption option : optionList) {
			if ("c".equals(option.getName())) {
				option.setList(list);
			}
		}
		model.setLitList(litList);
		model.setVarList(varList);
		model.setOptionList(optionList);
		valValueArray("alfa", true);
		valValueArray("bravo", false);
		valValueArray("charlie", false);
		valValueArray("one", true);
		valValueArray("two", false);
		valValueArray("three", false);
		valValueArray("-a", false);
		valValueArray("-b", false);
		valValueArray("-c", true);
		valValueArray("bogus", false);
		passed();
	}
	
	private void valValueArray (String name, boolean isList) {
		String[] valueArray = model.valueArray(name);
		testCase("valueArray for " + name, valueArray != null);
		if (isList) {
			assertNotNull(valueArray);
			assertEquals("DELTA", valueArray[0]);
			assertEquals("ECHO", valueArray[1]);
			assertEquals("FOXTROT", valueArray[2]);
		} else {
			assertNull(valueArray);
		}
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#valueList(java.lang.String)}.
	 */
	@Test
	public void testValueList () {
		List<ArgOperand> litList = buildLitList();
		List<ArgOperand> varList = buildVarList();
		List<ArgOption> optionList = buildOptionList();
		List<String> list = new ArrayList<String>();
		list.add("DELTA");
		list.add("ECHO");
		list.add("FOXTROT");
		for (ArgOperand operand : litList) {
			if ("bravo".equals(operand.getName())) {
				operand.setList(list);
			}
		}
		for (ArgOperand operand : varList) {
			if ("three".equals(operand.getName())) {
				operand.setList(list);
			}
		}
		for (ArgOption option : optionList) {
			if ("a".equals(option.getName())
					|| "c".equals(option.getName())) {
				option.setList(list);
			}
		}
		model.setLitList(litList);
		model.setVarList(varList);
		model.setOptionList(optionList);
		valValueList("alfa", false);
		valValueList("bravo", true);
		valValueList("charlie", false);
		valValueList("one", false);
		valValueList("two", false);
		valValueList("three", true);
		valValueList("-a", true);
		valValueList("-b", false);
		valValueList("-c", true);
		valValueList("bogus", false);
		passed();
	}
	
	private void valValueList (String name, boolean isList) {
		List<String> valueList = model.valueList(name);
		testCase("valueList for " + name, valueList != null);
		if (isList) {
			assertNotNull(valueList);
			assertEquals("DELTA", valueList.get(0));
			assertEquals("ECHO", valueList.get(1));
			assertEquals("FOXTROT", valueList.get(2));
		} else {
			assertNull(valueList);
		}
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#operandArray()}.
	 */
	@Test
	public void testOperandArray () {
		List<String> nonOptionList = new ArrayList<String>();
		nonOptionList.add("INDIA");
		nonOptionList.add("JULIETT");
		nonOptionList.add("KILO");
		model.setNonOptionList(nonOptionList);
		String[] operandArray = model.operandArray();
		int index = 0;
		testCase("operandArray " + index, operandArray[index]);
		assertEquals("INDIA", operandArray[index++]);
		testCase("operandArray " + index, operandArray[index]);
		assertEquals("JULIETT", operandArray[index++]);
		testCase("operandArray " + index, operandArray[index]);
		assertEquals("KILO", operandArray[index]);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#operandList()}.
	 */
	@Test
	public void testOperandList () {
		List<String> nonOptionList = new ArrayList<String>();
		nonOptionList.add("LIMA");
		nonOptionList.add("MIKE");
		nonOptionList.add("NOVEMBER");
		model.setNonOptionList(nonOptionList);
		List<String> operandList = model.operandList();
		int index = 0;
		testCase("operandList " + index, operandList.get(index));
		assertEquals("LIMA", operandList.get(index++));
		testCase("operandList " + index, operandList.get(index));
		assertEquals("MIKE", operandList.get(index++));
		testCase("operandList " + index, operandList.get(index));
		assertEquals("NOVEMBER", operandList.get(index));
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#checkOption(java.lang.String)}.
	 */
	@Test
	public void testCheckOption () {
		boolean result = model.checkOption("-a");
		testCase("-a", result);
		assertTrue(result);
		result = model.checkOption("<one>");
		testCase("<one>", result);
		assertFalse(result);
		result = model.checkOption("match");
		testCase("match", result);
		assertTrue(result);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#checkOperand(java.lang.String)}.
	 */
	@Test
	public void testCheckOperand () {
		boolean result = model.checkOperand("-a");
		testCase("-a", result);
		assertFalse(result);
		result = model.checkOperand("<one>");
		testCase("<one>", result);
		assertTrue(result);
		result = model.checkOperand("match");
		testCase("match", result);
		assertTrue(result);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#findNamedOption(java.lang.String)}.
	 */
	@Test
	public void testFindNamedOption () {
		List<ArgOption> optionList = buildOptionList();
		model.setOptionList(optionList);
		ArgOption option = model.findNamedOption("-a");
		testCase("-a", option.getName());
		assertEquals("a", option.getName());
		option = model.findNamedOption("--binary");
		testCase("--binary", option.getName());
		assertEquals("b", option.getName());
		option = model.findNamedOption("match");
		testCase("match", option);
		assertNull(option);
		option = model.findNamedOption("c");
		testCase("c", option.getName());
		assertEquals("c", option.getName());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#findNamedOperand(java.lang.String)}.
	 */
	@Test
	public void testFindNamedOperand () {
		List<ArgOperand> litList = buildLitList();
		List<ArgOperand> varList = buildVarList();
		model.setLitList(litList);
		model.setVarList(varList);
		ArgOperand operand = model.findNamedOperand("<one>");
		testCase("<one>", operand.getName());
		assertEquals("one", operand.getName());
		operand = model.findNamedOperand("<bravo>");
		testCase("<bravo>", operand.getName());
		assertEquals("bravo", operand.getName());
		operand = model.findNamedOperand("match");
		testCase("match", operand);
		assertNull(operand);
		operand = model.findNamedOperand("three");
		testCase("three", operand.getName());
		assertEquals("three", operand.getName());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getVersionText()}.
	 */
	@Test
	public void testGetVersionText () {
		String versionText = model.getVersionText();
		testCase("model versionText", versionText);
		assertEquals("the version text", versionText);
		model.setVersionText("version 1.4");
		versionText = model.getVersionText();
		testCase("specified versionText", versionText);
		assertEquals("version 1.4", versionText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getAboutText()}.
	 */
	@Test
	public void testGetAboutText () {
		String aboutText = model.getAboutText();
		testCase("model aboutText", aboutText);
		assertEquals("the about text", aboutText);
		model.setAboutText("what is it about");
		aboutText = model.getAboutText();
		testCase("specified aboutText", aboutText);
		assertEquals("what is it about", aboutText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getHelpText()}.
	 */
	@Test
	public void testGetHelpText () {
		String helpText = model.getHelpText();
		testCase("model helpText", helpText);
		assertEquals("the help text", helpText);
		model.setHelpText("sorry no help");
		helpText = model.getHelpText();
		testCase("specified helpText", helpText);
		assertEquals("sorry no help", helpText);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#isSuppressHelp()}.
	 */
	@Test
	public void testIsSuppressHelp () {
		boolean suppressHelp = model.isSuppressHelp();
		testCase("model suppressHelp", suppressHelp);
		assertFalse(suppressHelp);
		model.setSuppressHelp(true);
		suppressHelp = model.isSuppressHelp();
		testCase("specified suppressHelp", suppressHelp);
		assertTrue(suppressHelp);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#isPosixFormat()}.
	 */
	@Test
	public void testIsPosixFormat () {
		boolean posixFormat = model.isPosixFormat();
		testCase("model posixFormat", posixFormat);
		assertFalse(posixFormat);
		model.setPosixFormat(true);
		posixFormat = model.isPosixFormat();
		testCase("specified posixFormat", posixFormat);
		assertTrue(posixFormat);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#isSortOptions()}.
	 */
	@Test
	public void testIsSortOptions () {
		boolean sortOptions = model.isSortOptions();
		testCase("model sortOptions", sortOptions);
		assertFalse(sortOptions);
		model.setSortOptions(true);
		sortOptions = model.isSortOptions();
		testCase("specified sortOptions", sortOptions);
		assertTrue(sortOptions);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getOperandSuffix()}.
	 */
	@Test
	public void testGetOperandSuffix () {
		String operandSuffix = model.getOperandSuffix();
		testCase("model operandSuffix", operandSuffix);
		assertEquals("ModelOperandSuffix", operandSuffix);
		model.setOperandSuffix("Operand");
		operandSuffix = model.getOperandSuffix();
		testCase("specified operandSuffix", operandSuffix);
		assertEquals("Operand", operandSuffix);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getOptionSuffix()}.
	 */
	@Test
	public void testGetOptionSuffix () {
		String optionSuffix = model.getOptionSuffix();
		testCase("model optionSuffix", optionSuffix);
		assertEquals("ModelOptionSuffix", optionSuffix);
		model.setOptionSuffix("Option");
		optionSuffix = model.getOptionSuffix();
		testCase("specified optionSuffix", optionSuffix);
		assertEquals("Option", optionSuffix);
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getVarList()}.
	 */
	@Test
	public void testGetVarList () {
		model.setVarList(buildVarList());
		List<ArgOperand> varList = model.getVarList();
		ArgOperand operand = varList.get(0);
		testCase("operand 0", operand.getName());
		assertEquals("one", operand.getName());
		operand = varList.get(1);
		testCase("operand 1", operand.getName());
		assertEquals("two", operand.getName());
		operand = varList.get(2);
		testCase("operand 2", operand.getName());
		assertEquals("three", operand.getName());
		passed();
	}
	
	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getLitList()}.
	 */
	@Test
	public void testGetLitList () {
		model.setLitList(buildLitList());
		List<ArgOperand> litList = model.getLitList();
		ArgOperand operand = litList.get(0);
		testCase("operand 0", operand.getName());
		assertEquals("alfa", operand.getName());
		operand = litList.get(1);
		testCase("operand 1", operand.getName());
		assertEquals("bravo", operand.getName());
		operand = litList.get(2);
		testCase("operand 2", operand.getName());
		assertEquals("charlie", operand.getName());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#getOptionList()}.
	 */
	@Test
	public void testGetOptionList () {
		model.setOptionList(buildOptionList());
		List<ArgOption> optionList = model.getOptionList();
		ArgOption option = optionList.get(0);
		testCase("option 0", option.getName());
		assertEquals("a", option.getName());
		option = optionList.get(1);
		testCase("option 1", option.getName());
		assertEquals("b", option.getName());
		option = optionList.get(2);
		testCase("option 2", option.getName());
		assertEquals("c", option.getName());
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#parseUsage()}.
	 */
	@Test
	public void testParseUsage () {
		String[] usageText = {
			"Usage:",
				"test [-a] [-b/--binary] [-c <feature>]",
				"(alfa | bravo | charlie) <one> <two> [<three>]"
		};
		model.setUsageText(usageText);
		model.setPostProcess(false);
		boolean status = model.parseUsage();
		testCase("parseUsage status", status);
		assertTrue(status);
		String programName = model.getProgramName();
		testCase("program name", programName);
		assertEquals("test", programName);
		List<ArgOperand> varList = model.getVarList();
		List<ArgOperand> litList = model.getLitList();
		List<ArgOption> optionList = model.getOptionList();
		ArgList argList = model.getArgList();
		
		// Validate options.
		int index = 0;
		ArgOption option = optionList.get(index++);
		String name = option.getName();
		testCase("option list 0", name);
		assertEquals("a", name);
		option = optionList.get(index++);
		name = option.getName();
		testCase("option list 1", name);
		assertEquals("b", name);
		option = optionList.get(index++);
		name = option.getName();
		testCase("option list 2", name);
		assertEquals("c", name);
		
		// Validate literals.
		index = 0;
		ArgOperand lit = litList.get(index++);
		name = lit.getName();
		testCase("literal list 0", name);
		assertEquals("alfa", name);
		lit = litList.get(index++);
		name = lit.getName();
		testCase("literal list 1", name);
		assertEquals("bravo", name);
		lit = litList.get(index++);
		name = lit.getName();
		testCase("literal list 2", name);
		assertEquals("charlie", name);
		
		// Validate variables.
		index = 0;
		ArgOperand var = varList.get(index++);
		name = var.getName();
		testCase("variable list 0", name);
		assertEquals("one", name);
		var = varList.get(index++);
		name = var.getName();
		testCase("variable list 1", name);
		assertEquals("two", name);
		var = varList.get(index++);
		name = var.getName();
		testCase("variable list 2", name);
		assertEquals("three", name);
		
		// Validate argList.
		ArgNode node = argList.goHome();
		ArgOperand operand = node.getOperand();
		name = operand.getName();
		testCase("program name", name);
		assertEquals("test", name);
		
		// Options.
		node = argList.goEast();
		option = node.getOption();
		name = option.getName();
		testCase("-a", name);
		assertEquals("a", name);
		boolean optional = node.isOptional();
		testCase("optional -a", optional);
		assertTrue(optional);
		node = argList.goEast();
		option = node.getOption();
		name = option.getName();
		testCase("-b", name);
		assertEquals("b", name);
		name = option.getAltName();
		testCase("--binary", name);
		assertEquals("binary", name);
		optional = node.isOptional();
		testCase("optional -b", optional);
		assertTrue(optional);
		node = argList.goEast();
		option = node.getOption();
		name = option.getName();
		testCase("-c", name);
		assertEquals("c", name);
		name = option.getArgName();
		testCase("-c <feature>", name);
		assertEquals("feature", name);
		optional = node.isOptional();
		testCase("optional -c", optional);
		assertTrue(optional);
		
		// Literal operands.
		node = argList.goEast();
		ArgNode base = node;
		operand = node.getOperand();
		name = operand.getName();
		testCase("literal alfa", name);
		assertEquals("alfa", name);
		optional = node.isOptional();
		testCase("optional alfa", optional);
		assertFalse(optional);
		node = argList.goSouth();
		operand = node.getOperand();
		name = operand.getName();
		testCase("literal bravo", name);
		assertEquals("bravo", name);
		optional = node.isOptional();
		testCase("optional bravo", optional);
		assertFalse(optional);
		node = argList.goSouth();
		operand = node.getOperand();
		name = operand.getName();
		testCase("literal charlie", name);
		assertEquals("charlie", name);
		optional = node.isOptional();
		testCase("optional charlie", optional);
		assertFalse(optional);
		argList.setCurrent(base);
		
		// Validate variable operands.
		node = argList.goEast();
		operand = node.getOperand();
		name = operand.getName();
		testCase("<one>", name);
		assertEquals("one", name);
		optional = node.isOptional();
		testCase("optional <one>", optional);
		assertFalse(optional);
		node = argList.goEast();
		operand = node.getOperand();
		name = operand.getName();
		testCase("<two>", name);
		assertEquals("two", name);
		optional = node.isOptional();
		testCase("optional <two>", optional);
		assertFalse(optional);
		node = argList.goEast();
		operand = node.getOperand();
		name = operand.getName();
		testCase("<three>", name);
		assertEquals("three", name);
		optional = node.isOptional();
		testCase("optional <three>", optional);
		assertTrue(optional);
		
		// End of argList.
		node = argList.goEast();
		testCase("argList end", node);
		assertNull(node);
		
		passed();
	}

	/**
	 * Test method for {@link com.fidelis.argface.ArgBase#parseArguments(java.lang.String[])}.
	 */
	@Test
	public void testParseArguments () {
		incomplete();
	}

}
