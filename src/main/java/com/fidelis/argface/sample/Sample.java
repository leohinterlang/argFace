/**
 *+
 *	Sample.java
 *-
 */
package com.fidelis.argface.sample;

/**
 * Sample program.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class Sample {

	/**
	 * @param args
	 */
	public static void main (String[] args) {
		SampleCLI cli = new SampleCLI();
		if (! cli.parse(args)) {
			System.out.println("Error exit");
			System.exit(1);
		}
		System.out.println("Results:");
		if (cli.isaOption()) {
			System.out.println("-a");
		}
		if (cli.isbOption()) {
			System.out.println("-b");
		}
		if (cli.iscOption()) {
			System.out.println("-c " + cli.getcName());
			
		}
		if (cli.isFindOperand()) {
			System.out.println("find " + cli.getPatternOperand());
		}
		for (String filename : cli.getFileOperand()) {
			System.out.println(filename);
		}
		
	}

}
