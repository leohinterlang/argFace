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
			"Usage: sample [-a] [-b/--brand] [-c] [-d/--dir [path]] [-e] [--ace]",
			  "[-A] 'Capital letter option'",
			  "[-x/--Extra[what]]'Just a test case.'",
			  "[--underscores_join_words]",
			  "[--ace]",
			  //"[<first>] <second> [<third>]",
			  //"one [two] three",
			  "<file>...",
			"Options:",
			  "-a, --all Process all files as a unit.",
			  "-b Brand each line.",
			  "-c Define a name.",
			  "-d Set directory base path."
	};
	private final String versionText = "Sample 1.0.0";
	private final String [] aboutText = {
			versionText,
			"2014-07-26",
			"Leo Hinterlang    leohinterlang@gmail.com",
			"Fidelis Software Technologies, Butler, PA"
	};
			
	private boolean		aOption;
	private int         aCount;
	private boolean		bOption;
	private boolean		cOption;
	private String []     cName;
	private boolean     dirOption;
	private String      dirPath;
	private boolean     eOption;
	private boolean     aceOption;
	
	private boolean AOption;
	private boolean extraOption;
	private boolean xOption;
	private String  xWhat;
	private boolean underscores_join_wordsOption;
	
	private boolean     helpOption;
	private boolean     versionOption;
	private boolean     aboutOption;

	private boolean		findOperand;
	private String		 patternOperand;
	private String[]	   fileOperand;
	
	private String firstOperand;
	private String secondOperand;
	private String thirdOperand;
	private boolean oneOperand;
	private boolean twoOperand;
	private boolean threeOperand;
	private ArgFace argFace;

	public boolean parse (String [] args) {
		 Debug.setTrace(true);
		 Debug.setVerbose(true);
		argFace = ArgPrototype.create(usageText, this);
		if (argFace == null) {
			return false;
		}
		argFace.printHelp();
		argFace.setPatternWatch(true);
		int nArg = argFace.parse(args);
		if (nArg < 0) {
			return false;
		}
		return true;
	}
	
	public void report (String selection) {
		argFace.report(selection);
	}

	/**
	 * @return the usageText
	 */
	public String[] getUsageText () {
		return usageText;
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
	 * @return the helpOption
	 */
	public boolean isHelpOption () {
		return helpOption;
	}

	/**
	 * @param helpOption the helpOption to set
	 */
	public void setHelpOption (boolean helpOption) {
		this.helpOption = helpOption;
	}

	/**
	 * @return the versionOption
	 */
	public boolean isVersionOption () {
		return versionOption;
	}

	/**
	 * @param versionOption the versionOption to set
	 */
	public void setVersionOption (boolean versionOption) {
		this.versionOption = versionOption;
	}

	/**
	 * @return the aboutOption
	 */
	public boolean isAboutOption () {
		return aboutOption;
	}

	/**
	 * @param aboutOption the aboutOption to set
	 */
	public void setAboutOption (boolean aboutOption) {
		this.aboutOption = aboutOption;
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
	 * @return the aOption
	 */
	public boolean isAOption () {
		return AOption;
	}

	/**
	 * @param aOption the aOption to set
	 */
	public void setAOption (boolean aOption) {
		AOption = aOption;
	}

	/**
	 * @return the aCount
	 */
	public int getaCount () {
		return aCount;
	}

	/**
	 * @param aCount the aCount to set
	 */
	public void setaCount (int aCount) {
		this.aCount = aCount;
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
	 * @return the dirOption
	 */
	public boolean isDirOption () {
		return dirOption;
	}

	/**
	 * @param dirOption the dirOption to set
	 */
	public void setDirOption (boolean dirOption) {
		this.dirOption = dirOption;
	}

	/**
	 * @return the dirPath
	 */
	public String getDirPath () {
		return dirPath;
	}

	/**
	 * @param dirPath the dirPath to set
	 */
	public void setDirPath (String dirPath) {
		this.dirPath = dirPath;
	}

	/**
	 * @return the eOption
	 */
	public boolean iseOption () {
		return eOption;
	}

	/**
	 * @param eOption the eOption to set
	 */
	public void seteOption (boolean eOption) {
		this.eOption = eOption;
	}

	/**
	 * @return the aceOption
	 */
	public boolean isAceOption () {
		return aceOption;
	}

	/**
	 * @param aceOption the aceOption to set
	 */
	public void setAceOption (boolean aceOption) {
		this.aceOption = aceOption;
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
	
	/**
	 * @return the extraOption
	 */
	public boolean isExtraOption () {
		return extraOption;
	}

	/**
	 * @param extraOption the extraOption to set
	 */
	public void setExtraOption (boolean extraOption) {
		this.extraOption = extraOption;
	}

	/**
	 * @return the xWhat
	 */
	public String getxWhat () {
		return xWhat;
	}

	/**
	 * @param xWhat the xWhat to set
	 */
	public void setxWhat (String xWhat) {
		this.xWhat = xWhat;
	}


	/**
	 * @return the underscores_join_wordsOption
	 */
	public boolean isUnderscores_join_wordsOption () {
		return underscores_join_wordsOption;
	}

	/**
	 * @param underscores_join_wordsOption the underscores_join_wordsOption to set
	 */
	public void setUnderscores_join_wordsOption (
			boolean underscores_join_wordsOption) {
		this.underscores_join_wordsOption = underscores_join_wordsOption;
	}

	/**
	 * @return the firstOperand
	 */
	public String getFirstOperand () {
		return firstOperand;
	}

	/**
	 * @param firstOperand the firstOperand to set
	 */
	public void setFirstOperand (String firstOperand) {
		this.firstOperand = firstOperand;
	}

	/**
	 * @return the secondOperand
	 */
	public String getSecondOperand () {
		return secondOperand;
	}

	/**
	 * @param secondOperand the secondOperand to set
	 */
	public void setSecondOperand (String secondOperand) {
		this.secondOperand = secondOperand;
	}

	/**
	 * @return the thirdOperand
	 */
	public String getThirdOperand () {
		return thirdOperand;
	}

	/**
	 * @param thirdOperand the thirdOperand to set
	 */
	public void setThirdOperand (String thirdOperand) {
		this.thirdOperand = thirdOperand;
	}

}
