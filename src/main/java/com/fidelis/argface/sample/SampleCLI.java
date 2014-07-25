/**
 * 
 */
package com.fidelis.argface.sample;

import com.fidelis.argface.ArgFace;
import com.fidelis.argface.ArgPrototype;
import com.fidelis.argface.ArgStandard;
import com.fidelis.argface.Debug;

/**
 * Sample CLI.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 */
public class SampleCLI {
	private final String [] usageText = {
			"Usage: sample [-a] [-b/--brand] [-c <name>] [find <pattern>] <file>...",
			"Options:",
			"-a, --all Process all files as a unit.",
			"-b Brand each line.",
			"-c Define a name."
	};
			
	private boolean		aOption;
	private boolean		bOption;
	private boolean		cOption;
	private String		 cName;

	private boolean		findOperand;
	private String		 patternOperand;
	private String[]	   fileOperand;

	public boolean parse (String [] args) {
		// Debug.setTrace(true);
		// Debug.setVerbose(true);
		ArgFace argFace = ArgStandard.create(usageText, this);
		if (argFace == null) {
			return false;
		}
		argFace.printHelp();
		int nArg = argFace.parse(args);
		if (nArg < 0) {
			return false;
		}
		return true;
	}

	/**
	 * @return the aOption
	 */
	public boolean isaOption () {
		return aOption;
	}

	/**
	 * @param aOption the aOption to set
	 */
	public void setaOption (boolean aOption) {
		this.aOption = aOption;
	}

	/**
	 * @return the bOption
	 */
	public boolean isbOption () {
		return bOption;
	}

	/**
	 * @param bOption the bOption to set
	 */
	public void setbOption (boolean bOption) {
		this.bOption = bOption;
	}

	/**
	 * @return the cOption
	 */
	public boolean iscOption () {
		return cOption;
	}

	/**
	 * @param cOption the cOption to set
	 */
	public void setcOption (boolean cOption) {
		this.cOption = cOption;
	}

	/**
	 * @return the cName
	 */
	public String getcName () {
		return cName;
	}

	/**
	 * @param cName the cName to set
	 */
	public void setcName (String cName) {
		this.cName = cName;
	}

	/**
	 * @return the findOperand
	 */
	public boolean isFindOperand () {
		return findOperand;
	}

	/**
	 * @param findOperand the findOperand to set
	 */
	public void setFindOperand (boolean findOperand) {
		this.findOperand = findOperand;
	}

	/**
	 * @return the patternOperand
	 */
	public String getPatternOperand () {
		return patternOperand;
	}

	/**
	 * @param patternOperand the patternOperand to set
	 */
	public void setPatternOperand (String patternOperand) {
		this.patternOperand = patternOperand;
	}

	/**
	 * @return the fileOperand
	 */
	public String [] getFileOperand () {
		return fileOperand;
	}

	/**
	 * @param fileOperand the fileOperand to set
	 */
	public void setFileOperand (String [] fileOperand) {
		this.fileOperand = fileOperand;
	}

}
