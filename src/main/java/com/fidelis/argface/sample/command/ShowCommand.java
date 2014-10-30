/**
 * 
 */
package com.fidelis.argface.sample.command;

import com.fidelis.argface.ArgCommand;

/**
 * @author leo
 *
 */
public class ShowCommand {
	
	private final String[] usageText = {
			"show [options] [<command> [<command-args>]...]",
			"options:",
			    "-p, --pager pipe output to less"
	};
	
	private final String[] helpText = {
			"Shows the specified information."
	};
	
	// Operating variables
	// posixFormat is true for the ArgCommand model.
	// operandSuffix is "" for the ArgCommand model.
	// optionSuffix specified by the main program.
	
	// Options
	private boolean pagerOpt;
	
	// Operands
	private String command;
	private String[] commandArgs;
	
	
	public boolean execute (String... args) {
		ArgCommand argCommand = ArgCommand.create(usageText, this);
		if (argCommand == null) {
			return false;
		}
		int nArg = argCommand.parse(args);
		if (nArg < 0) {
			return false;
		}
		SampleCommand progCLI = (SampleCommand) argCommand.getCLI(ArgCommand.PROGRAM);
		if (progCLI.isReportOpt()) {
			System.out.println("ShowCommand report:");
			argCommand.report(progCLI.getReportType());
		}
		if (getCommand() != null) {
			if (! argCommand.executeCommand()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the pagerOpt
	 */
	public boolean isPagerOpt () {
		return pagerOpt;
	}


	/**
	 * @param pagerOpt the pagerOpt to set
	 */
	public void setPagerOpt (boolean pagerOpt) {
		this.pagerOpt = pagerOpt;
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
	 * @return the helpText
	 */
	public String[] getHelpText () {
		return helpText;
	}

}
