/**
 * 
 */
package com.fidelis.argface.sample.command;

import com.fidelis.argface.ArgCommand;

/**
 * @author leo
 *
 */
public class ShowStatusCommand {
	
	private final String[] usageText = {
			"status [options] [<command> [<command-args>]...]",
			"options:",
				"-w, --wait Wait for response."
	};
	
	private boolean waitOpt;
	
	private String command;
	private String[] commandArgs;
	
	/**
	 * Executes the "show status" command.
	 * 
	 * @param args the command arguments
	 * @return {@code true} if the command completes successfully
	 */
	public boolean execute (String... args) {
		
		// Create the next level ArgFace command object.
		ArgCommand argCommand = ArgCommand.create(usageText, this);
		if (argCommand == null) {
			return false;
		}
		
		// Parse the command arguments.
		int nArg = argCommand.parse(args);
		if (nArg < 0) {
			return false;
		}
		
		// Get the PROGRAM level CLI object.
		SampleCommand progCLI = (SampleCommand) argCommand.getCLI(ArgCommand.PROGRAM);
		
		// Report option?
		if (progCLI.isReportOpt()) {
			
			// Generate report output.
			System.out.println("ShowStatusCommand report:");
			argCommand.report(progCLI.getReportType());
		}
		
		// Another command following "show status"?
		if (getCommand() != null) {
			
			// Execute next level command.
			if (! argCommand.executeCommand()) {
				return false;
			}
		}
		
		// Just "show status" command.
		else {
			System.out.println("*** SHOW STATUS COMMAND");
		}
		return true;
	}

	/**
	 * @return the waitOpt
	 */
	public boolean isWaitOpt () {
		return waitOpt;
	}

	/**
	 * @param waitOpt the waitOpt to set
	 */
	public void setWaitOpt (boolean waitOpt) {
		this.waitOpt = waitOpt;
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

}
