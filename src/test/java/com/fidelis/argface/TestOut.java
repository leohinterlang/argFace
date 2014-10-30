/**
 *+
 *	Test.java
 *	1.0.0  Oct 20, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import org.junit.rules.TestName;

/**
 * Test
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class TestOut {
	
	private static final String OUTPUT_MODE = "outputMode";
	private static String outputMode;
	private static boolean quiet;
	private static boolean normal;
	private static boolean verbose;
	private static boolean firstCase;
	private static boolean testIncomplete;
	private static boolean passed;
	
	public static String getOutputMode () {
		setOutputMode();
		return outputMode;
	}
	
	public static void setOutputMode () {
		if (outputMode == null) {
			outputMode = System.getProperty(OUTPUT_MODE, "normal");
			quiet = outputMode.equals("quiet");
			normal = outputMode.equals("normal");
			verbose = outputMode.equals("verbose");
		}
	}
	
	public static boolean isQuiet () {
		setOutputMode();
		return quiet;
	}
	
	public static boolean isNormal () {
		setOutputMode();
		return normal;
	}
	
	public static boolean isVerbose () {
		setOutputMode();
		return verbose;
	}
	
	public static void announce (TestName testName) {
		if (! isQuiet()) {
			String test = testName.getMethodName();
			System.out.print("Testing: " + test);
		}
		firstCase = true;
		testIncomplete = false;
		passed = false;
	}
	
	public static void passFail (TestName testName) {
		if (! isQuiet()) {
			String test = testName.getMethodName();
			if (! firstCase) {
				System.out.print("         " + test);
			}
			for (int n = test.length(); n < 30; n++) {
				System.out.print('.');
			}
			if (testIncomplete) {
				System.out.println(" incomplete ***");
			}
			else {
				System.out.println(passed ? " passed" : " failed ***");
			}
		}
	}
	
	public static void passed () {
		passed = true;
	}
	
	public static void testCase (String theCase, String actual) {
		if (isVerbose()) {
			checkFirstCase();
			System.out.print("   Case: " + theCase);
			int length = 4;
			if (theCase != null) {
				length = theCase.length();
			}
			for (int n = length; n < 30; n++) {
				System.out.print('.');
			}
			System.out.println(" " + actual);
		}
	}
	
	public static void testCase (String theCase, boolean value) {
		testCase(theCase, value ? "true" : "false");
	}
	
	public static void testCase (String theCase, Object object) {
		testCase(theCase, object == null ? "null" : object.toString());
	}
	
	public static void format(String format, Object ... args) {
		if (isVerbose()) {
			checkFirstCase();
			System.out.println(String.format(format, args));
		}
	}
	
	private static void checkFirstCase () {
		if (firstCase) {
			firstCase = false;
			System.out.println();
		}
	}
	
	public static void incomplete () {
		testIncomplete = true;
	}

}
