/**
 * 
 */
package com.fidelis.argface.sample.command;

import java.io.File;

import com.fidelis.argface.ArgCommand;
import com.fidelis.argface.Debug;

/**
 * @author Leo Hinterlang
 *
 */
public class SampleCommand {
	
	private final String[] usageText = {
			"usage:",
				"SampleCommand [options] <command> [<command-args>]...",
			"options:",
				"-l, --list list commands",
				"-r, --report <type> ArgFace report of specified type"
	};
	
	private final String versionText = "SampleCommand  Version 1.0.0";
	
	private final String[] aboutText = {
			versionText,
			"  2014-08-16",
			"  Demonstrates the ArgFace command model - ArgCommand.",
			"  Leo Hinterlang -- leohinterlang@gmail.com",
			"  Fidelis Software Technologies, Butler, PA"
	};
	
	private final String optionSuffix = "Opt";
	
	private boolean listOpt;
	private boolean reportOpt;
	private String reportType;
	
	private String command;
	private String[] commandArgs;
	
	private ArgCommand argCommand;
	
	/**
	 * The main deal.
	 * 
	 * @param args command line arguments
	 */
	public static void main (String... args) {
		SampleCommand prog = new SampleCommand();
		if (! prog.processArguments(args)) {
			System.exit(1);
		}
	}
	
	private boolean processArguments (String... args) {
		
		// Create PROGRAM level ArgFace command object.
		argCommand = ArgCommand.create(usageText, this);
		if (argCommand == null) {
			return false;
		}
		
		// Parse the command line arguments.
		int nArg = argCommand.parse(args);
		if (nArg < 0) {
			return false;
		}
		
		// Process options.
		if (! processOptions()) {
			return false;
		}
		
		// Command specified?
		if (getCommand() != null) {
			
			// Execute the command.
			if (! argCommand.executeCommand()) {
				return false;
			}
		}
		return true;
	}
	
	private boolean processOptions () {
		
		// ArgFace report option?
		if (isReportOpt()) {
			System.out.println("SampleCommand report:");
			argCommand.report(getReportType());
		}
		
		// List commands option?
		if (isListOpt()) {
			listCommands();
		}
		return true;
	}
	
	private void listCommands () {
		System.out.println("Available Commands:");
		File file = new File("./target/classes/com/fidelis/argface/sample/command");
		String[] names = file.list();
		for (String name : names) {
			if (name.endsWith(".class")) {
				name = name.substring(0, name.length() - 6);
			} else {
				continue;
			}
			if (name.equals("SampleCommand")) {
				continue;
			}
			String command = argCommand.classToCommand(name);
			if (command != null) {
				System.out.println("  " + command);
			}
		}
	}

	/**
	 * @return the listOpt
	 */
	public boolean isListOpt () {
		return listOpt;
	}

	/**
	 * @param listOpt the listOpt to set
	 */
	public void setListOpt (boolean listOpt) {
		this.listOpt = listOpt;
	}

	/**
	 * @return the reportOpt
	 */
	public boolean isReportOpt () {
		return reportOpt;
	}

	/**
	 * @param reportOpt the reportOpt to set
	 */
	public void setReportOpt (boolean reportOpt) {
		this.reportOpt = reportOpt;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType () {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType (String reportType) {
		this.reportType = reportType;
	}

	/**
	 * @return the command
	 */
	public String getCommand () {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand (String command) {
		this.command = command;
	}

	/**
	 * @return the commandArgs
	 */
	public String[] getCommandArgs () {
		return commandArgs;
	}

	/**
	 * @param commandArgs the commandArgs to set
	 */
	public void setCommandArgs (String[] commandArgs) {
		this.commandArgs = commandArgs;
	}

	/**
	 * @return the versionText
	 */
	public String getVersionText () {
		return versionText;
	}

	/**
	 * @return the aboutText
	 */
	public String[] getAboutText () {
		return aboutText;
	}

	/**
	 * @return the optionSuffix
	 */
	public String getOptionSuffix () {
		return optionSuffix;
	}

}
