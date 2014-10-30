/**
 * 
 */
package com.fidelis.argface.sample.command;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fidelis.argface.ArgCommand;

/**
 * Show files command.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ShowFilesCommand {
	
	private final String[] usageText = {
			"$command [options] [<path>]...",
			"options:",
				"-a, --all do not hide entries starting with .",
				"-R, --recursive list subdirectories recursively"
	};
	
	// Options
	private boolean aOpt;
	private boolean ROpt;
	
	// Operands
	private List<String> path;

	private ArgCommand argCommand;
	
	public boolean execute (String... args) {
		
		// Create ArgFace command object.
		argCommand = ArgCommand.create(usageText, this);
		if (argCommand == null) {
			return false;
		}
		
		// Parse command arguments.
		int nArg = argCommand.parse(args);
		if (nArg < 0) {
			return false;
		}
		
		// Report ArgFace results?
		SampleCommand progCLI = (SampleCommand) argCommand.getCLI(ArgCommand.PROGRAM);
		if (progCLI.isReportOpt()) {
			System.out.println("ShowFilesCommand report:");
			argCommand.report(progCLI.getReportType());
		}
		
		// Recursive subdirectories?
		if (isROpt()) {
			System.out.println("*** SHOW FILES RECURSIVE");
			recursive(".");
		} else {
			System.out.println("*** SHOW FILES STANDARD");
			standard(".");
		}
		return true;
	}
	
	private void recursive (String dirName) {
		
		File dir = new File(dirName);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				String fileName = file.getName();
				if (fileName.startsWith(".")) {
					if (! isaOpt()) {
						continue;
					}
				}
				String name = dirName + "/" + file.getName();
				System.out.println(name + ":");
				standard(name);
				System.out.println();
				recursive(name);
			}
		}
	}
	
	private void standard (String dirName) {
		
		File dir = new File(dirName);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		SimpleDateFormat df = new SimpleDateFormat("MMM dd HH:mm");
		for (File file : files) {
			String name = file.getName();
			if (file.isDirectory()) {
				name += "/";
			}
			Date modDate = new Date(file.lastModified());
			String mod = df.format(modDate);
			System.out.printf("%s %s%n", mod, name);
		}
	}

	/**
	 * @return the aOpt
	 */
	public boolean isaOpt () {
		return aOpt;
	}

	/**
	 * @param aOpt the aOpt to set
	 */
	public void setaOpt (boolean aOpt) {
		this.aOpt = aOpt;
	}

	/**
	 * @return the rOpt
	 */
	public boolean isROpt () {
		return ROpt;
	}

	/**
	 * @param rOpt the rOpt to set
	 */
	public void setROpt (boolean rOpt) {
		ROpt = rOpt;
	}

	/**
	 * @return the path
	 */
	public List<String> getPath () {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath (List<String> path) {
		this.path = path;
	}
	
}
