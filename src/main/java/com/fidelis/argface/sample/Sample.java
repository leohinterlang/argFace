/**
 *+
 *	Sample.java
 *-
 */
package com.fidelis.argface.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Sample program.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class Sample {
	private SampleCLI cli;
	private String [] lines = new String[6];
	private int lineNumber;
	private int lineStartIndex;
	private int lineNextIndex;

	/**
	 * Sample program to illustrate the use of the ArgFace CLI.
	 * Here is the usage specification:
	 * <blockquote><code>
	 * sample [-a] [-b/--brand] [-c &lt;name&gt] [-d/--dir &lt;path&gt;]
	 *   [find &lt;pattern>] file...
	 * </code></blockquote>
	 * 
	 * @param args command line arguments
	 */
	public static void main (String[] args) {
		
		// Create this program class.
		Sample prog = new Sample();
		
		// Execute the program.
		if (! prog.execute(args)) {
			System.out.println("Error exit");
			System.exit(1);
		}
	}
	
	private boolean execute (String [] args) {
		
		// SampleCLI is a POJO that contains all of the logic for
		// the command line interface.
		cli = new SampleCLI();
		
		// Parse the command line arguments.
		if (! cli.parse(args)) {
			return false;
		}
		// Display the CLI results.
		System.out.println("Results ----");
		System.out.println("Options:");
		cli.report("options");
		System.out.println("Operands:");
		cli.report("operands");
		return true;
	}
	
	private void processFile (String filename) {
		String pattern = "public";
		if (cli.isFindOperand()) {
			pattern = cli.getPatternOperand();
		}
		Path file = null;
		if (cli.isDirOption()) {
			file = Paths.get(cli.getDirPath() + "/" + filename);
		} else {
			file = Paths.get(filename);
		}
		Charset charset = Charset.forName("US-ASCII");
		try {
			BufferedReader reader = Files.newBufferedReader(file, charset);
			String line = null;
			int lineNo = 0;
			while ((line = reader.readLine()) != null) {
				++lineNo;
				if (line.contains(pattern)) {
					if (cli.isbOption()) {
						showBrand(filename);
					}
					dumpLines();
					System.out.printf("%04d %s\n", lineNo, line);
				} else {
					storeLine(lineNo, line);
				}
			}
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	private void showBrand (String filename) {
		StringBuilder sb = new StringBuilder(filename + " ");
		for (int i = sb.length(); i < 80; i++) {
			sb.append('-');
		}
		System.out.println(sb.toString());
	}
	
	private void storeLine (int lineNo, String line) {
		int limit = lines.length;
		lines[lineNextIndex] = line;
		if (lineNumber == 0) {
			lineNumber = lineNo;
			lineStartIndex = lineNextIndex;
		}
		++lineNextIndex;
		if (lineNextIndex == limit) {
			lineNextIndex = 0;
		}
		if (lineStartIndex == lineNextIndex) {
			++lineStartIndex;
			++lineNumber;
			if (lineStartIndex == limit) {
				lineStartIndex = 0;
			}
		}
	}
	
	private void dumpLines () {
		showLines();
		lineNumber = 0;
	}
	
	private void showLines () {
		int limit = lines.length;
		int lineNo = lineNumber;
		int start = lineStartIndex;
		while (start != lineNextIndex) {
			System.out.printf("%04d %s\n", lineNo++, lines[start]);
			++start;
			if (start == limit) {
				start = 0;
			}
		}
	}

}
