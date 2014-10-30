/**
 *+
 *	SampleOne.java
 *	1.0.0  2014-07-31  Leo Hinterlang
 *-
 */
package com.fidelis.argface.sample;

import com.fidelis.argface.ArgFace;
import com.fidelis.argface.ArgPrototype;

/**
 * SampleOne ArgFace program.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class SampleOne {
	private final String usageText =
			"Usage: program [-a] [-b/--brand] [-c <name>] [find <pattern>] <file>...";
	private boolean aOption;
	private boolean bOption;
	private boolean cOption;
	private String  cName;
	private boolean findOperand;
	private String  patternOperand;
	private String [] fileOperand;

	public static void main (String [] args) {
		SampleOne prog = new SampleOne();
		ArgFace argFace = ArgPrototype.create(prog.usageText, prog);
		if (argFace == null) {
			System.exit(1);
		}
		int nArg = argFace.parse(args);
		if (nArg < 0) {
			System.exit(1);
		}
		if (prog.aOption) {
			System.out.println("-a option specified");
		} else {
			System.out.println("-a option NOT specified");
		}
		if (prog.bOption) {
			System.out.println("-b option specified");
		} else {
			System.out.println("-b option NOT specified");
		}
		if (prog.cOption) {
			System.out.println("-c option specified");
			System.out.println("<cName> = " + prog.cName);
		} else {
			System.out.println("-c option NOT specified");
		}
		if (prog.findOperand) {
			System.out.println("'find' operand specified");
			System.out.println("<pattern> = " + prog.patternOperand);
		} else {
			System.out.println("'find <pattern>' NOT specified");
		}
		if (prog.fileOperand != null) {
		    for (String file : prog.fileOperand) {
			    System.out.println("<file> = " + file);
		    }
		} else {
			System.out.println("<file> operand NOT specified");
		}
	}
}