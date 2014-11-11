/**
 *+
 *	BaseModel.java
 *	1.0.0  Nov 6, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.List;

/**
 * BaseModel
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class BaseModel extends ArgBase {
	
	private List<String>	 nonOptionList;
	private List<ArgOperand> operandList;
	private List<ArgOption>  optionList;
	private boolean postProcess;

	public int parse (String[] args) {
		return 0;
	}
	
	@Override
	protected String modelGetUsageText () {
		return "the usage text";
	}
	
	@Override
	protected String modelGetVersionText () {
		return "the version text";
	}
	
	@Override
	protected String modelGetAboutText () {
		return "the about text";
	}
	
	@Override
	protected String modelGetHelpText () {
		return "the help text";
	}
	
	@Override
	protected Boolean modelGetAllowOverwrite () {
		return null;
	}
	
	@Override
	protected String modelGetOperandSuffix () {
		return "ModelOperandSuffix";
	}
	
	@Override
	protected String modelGetOptionSuffix () {
		return "ModelOptionSuffix";
	}
	
	public void setPostProcess (boolean postProcess) {
		this.postProcess = postProcess;
	}
	
	@Override
	protected boolean modelPostProcess (ArgOperand operand) {
		if (postProcess) {
			String name = operand.getName();
			operand.setName("post-process-operand-" + name);
		}
		return true;
	}
	
	@Override
	protected boolean modelPostProcess (ArgOption option) {
		if (postProcess) {
			String name = option.getName();
			option.setName("post-process-option-" + name);
		}
		return true;
	}
	
	@Override
	protected void modelSetNonOptions (List<String> nonOptionList) {
		this.nonOptionList = nonOptionList;
	}
	
	public String getNonOption (int index) {
		if (0 <= index && index < nonOptionList.size()) {
			return nonOptionList.get(index);
		}
		return null;
	}
	
	@Override
	protected void modelSetProgramVariables (List<ArgOperand> operandList,
			List<ArgOption> optionList) {
		this.operandList = operandList;
		this.optionList = optionList;
	}
	
	public ArgOperand getOperand (int index) {
		if (0 <= index && index < operandList.size()) {
			return operandList.get(index);
		}
		return null;
	}
	
	public ArgOption getOption (int index) {
		if (0 <= index && index < optionList.size()) {
			return optionList.get(index);
		}
		return null;
	}
	
}
