/**
 *+
 *	CommonPojo.java
 *	1.0.0  Oct 29, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.List;

/**
 * CommonPojo
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class CommonPojo {
	
	private static final String usageText = "The usage text.";
	
	private static final String versionText = "The version text.";
	
	private static final String aboutText = "The about text.";
	
	private static final String helpText = "The help text.";
	
	private static final String operandSuffix = "TheOperandSuffix";
	
	private static final String optionSuffix = "TheOptionSuffix";
	
	private static final boolean allowOverwrite = true;
	
	private static final boolean suppressHelp = true;
	
	private static final boolean posixFormat = true;
	
	private static final boolean sortOptions = true;
	
	private List<String> operands;
	
	public List<String> getOperands () {
		return operands;
	}
	
	private String oneOperand;
	
	public String getOneOperand () {
		return oneOperand;
	}
	
	private String twoOperand;
	
	public String getTwoOperand () {
		return twoOperand;
	}
	
	public void setTwoOperand (String twoOperand) {
		this.twoOperand = twoOperand;
	}
	
	private boolean threeOperand;
	
	public boolean isThreeOperand () {
		return threeOperand;
	}
	
	private String[] fourOperand;
	
	public String[] getFourOperand () {
		return fourOperand;
	}
	
	private String[] fiveOperand;
	
	public String[] getFiveOperand () {
		return fiveOperand;
	}
	
	public void setFiveOperand (String[] fiveOperand) {
		this.fiveOperand = fiveOperand;
	}
	
	private List<String> sixOperand;
	
	public List<String> getSixOperand () {
		return sixOperand;
	}
	
	private List<String> sevenOperand;
	
	public List<String> getSevenOperand () {
		return sevenOperand;
	}
	
	public void setSevenOperand (List<String> sevenOperand) {
		this.sevenOperand = sevenOperand;
	}
	
	private boolean aOption;
	
	public boolean isaOption () {
		return aOption;
	}
	
	private boolean bOption;
	
	public boolean isbOption () {
		return bOption;
	}
	
	public void setbOption (boolean bOption) {
		this.bOption = bOption;
	}
	
	private boolean copyOption;
	
	public boolean isCopyOption () {
		return copyOption;
	}
	
	private boolean printOption;
	
	public boolean isPrintOption () {
		return printOption;
	}
	
	public void setPrintOption (boolean printOption) {
		this.printOption = printOption;
	}
	
	private boolean cOption;
	private String cName;
	
	public boolean iscOption () {
		return cOption;
	}
	
	public String getcName () {
		return cName;
	}
	
	private boolean dOption;
	private String dArg;

	public boolean isdOption () {
		return dOption;
	}

	public void setdOption (boolean dOption) {
		this.dOption = dOption;
	}

	public String getdArg () {
		return dArg;
	}

	public void setdArg (String dArg) {
		this.dArg = dArg;
	}
	
}
