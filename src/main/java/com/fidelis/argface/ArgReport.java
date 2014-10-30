/**
 *+
 *	ArgReport.java
 *	1.0.0  2014-08-02  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.List;

/**
 * Report on ArgFace results.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 */
public class ArgReport {
	
	/**
	 * Reports on the results of command line parsing.
	 * The selection indicates the desired output. These include:
	 * <ul>
	 * <li>all - all information.</li>
	 * <li>has - only items that appear on the command line.</li>
	 * <li>options - just the options</li>
	 * <li>operands - just the operands</li>
	 * <li>has-options - options that appear on the command line.</li>
	 * <li>has-operands - operands that appear on the command line.</li>
	 * <li>-option - a named option.</li>
	 * <li>&lt;operand&gt; - a named operand.</li>
	 * </ul>
	 *
	 * @param selection indicates the desired output
	 * @param base the ArgBase class
	 */
	static void report (String selection, ArgBase base) {
		if (selection == null) {
			return;
		} else if (selection.equals("all")) {
			reportAllOptions(base);
			reportAllOperands(base);
		} else if (selection.equals("has")) {
			reportHasOptions(base);
			reportHasOperands(base);
		} else if (selection.equals("options")) {
			reportAllOptions(base);
		} else if (selection.equals("operands")) {
			reportAllOperands(base);
		} else if (selection.equals("has-options")) {
			reportHasOptions(base);
		} else if (selection.equals("has-operands")) {
			reportHasOperands(base);
		} else {
			if (base.checkOption(selection)) {
				ArgOption option = base.findNamedOption(selection);
				if (option != null) {
					reportOption(option);
					return;
				}
			}
			if (base.checkOperand(selection)) {
				ArgOperand operand = base.findNamedOperand(selection);
				if (operand != null) {
					reportOperand(operand);
				}
			}
		}
	}
	
	private static void reportAllOptions (ArgBase base) {
		List<ArgOption> optionList = base.getOptionList();
		for (ArgOption option : optionList) {
			reportOption(option);
		}
	}
	
	private static void reportHasOptions (ArgBase base) {
		List<ArgOption> optionList = base.getOptionList();
		for (ArgOption option : optionList) {
			if (option.has()) {
				reportOption(option);
			}
		}
	}
	
	private static void reportOption (ArgOption option) {
		StringBuffer sb = new StringBuffer(64);
		sb.append(option.getText());
		sb.append(' ');
		for (int n = sb.length(); n < 21; n++) {
			sb.append('.');
		}
		sb.append(" : ");
		if (option.has()) {
			sb.append("true");
			int count = option.getCount();
			if (count > 1) {
				sb.append("  count = ");
				sb.append(count);
			}
			String argName = option.getArgName();
			if (argName != null) {
				String argSpec = option.isArgOptional()
						? "    [" + argName + "]"
						: "    <" + argName + ">";
				if (option.isRepeat()) {
					for (String s : option.getList()) {
						sb.append('\n');
						if (argSpec.length() < 21) {
							for (int n = 0; n < 21 - argSpec.length(); n++) {
								sb.append(' ');
							}
						}
						sb.append(argSpec + " : \"" + s + "\"");
					}
				} else {
					String s = option.getArgValue();
					sb.append('\n');
					if (argSpec.length() < 21) {
						for (int n = argSpec.length(); n < 21; n++) {
							sb.append(' ');
						}
					}
					sb.append(argSpec);
					if (s != null) {
						sb.append(" : \"" + s + "\"");
					} else {
						sb.append(" : null");
					}
				}
			}
		} else {
			sb.append("false");
		}
		System.out.println(sb);
	}
	
	private static void reportAllOperands (ArgBase base) {
		reportAllOperands(base.getLitList());
		reportAllOperands(base.getVarList());
	}
	
	private static void reportAllOperands (List<ArgOperand> operandList) {
		for (ArgOperand operand : operandList) {
			reportOperand(operand);
		}
	}
	
	private static void reportHasOperands (ArgBase base) {
		reportHasOperands(base.getLitList());
		reportHasOperands(base.getVarList());
	}
	
	private static void reportHasOperands (List<ArgOperand> operandList) {
		for (ArgOperand operand : operandList) {
			if (operand.has()) {
				reportOperand(operand);
			}
		}
	}
	
	private static void reportOperand (ArgOperand operand) {
		if (operand.getName().equals("options")) {
			return;
		}
		StringBuilder sb = new StringBuilder(64);
		sb.append(operand.getSpecText());
		sb.append(' ');
		for (int n = sb.length(); n < 21; n++) {
			sb.append('.');
		}
		if (operand.has()) {
			int count = operand.getCount();
			if (count > 1) {
				sb.append(" : count = ");
				sb.append(count);
			}
			if (operand.isVariable()) {
				if (operand.isRepeat()) {
					int index = 0;
					for (String s : operand.getList()) {
						++index;
						sb.append('\n');
						for (int n = 0; n < 19; n++) {
							sb.append(' ');
						}
						sb.append(String.format("%02d : \"%s\"", index, s));
					}
				} else {
					sb.append(" : \"" + operand.getValue() + "\"");
				}
			} else {
				sb.append(" : true");
			}
		} else {
			sb.append(" : false");
		}
		System.out.println(sb);
	}

}
