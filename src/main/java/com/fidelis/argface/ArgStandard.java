/**
 *+
 *  ArgStandard.java
 *	1.0.0	Apr 18, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.List;

/**
 * The standard version of ArgFace.
 * Uses reflection on {@code public} methods and fields.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 */
public class ArgStandard extends ArgBase implements ArgFace {
    private ArgReflect reflect;
    private ArgCommon common;
    
    /**
     * Protected no argument constructor.
     */
    protected ArgStandard () {
        reflect = new ArgReflect();
        reflect.setPrivateAccess(false);
        common = new ArgCommon(reflect);
    }
    
    /**
     * Creates a new {@code ArgStandard} instance.
     * 
     * @return a new {@code ArgStandard} instance
     */
    protected static ArgStandard create () {
    	return new ArgStandard();
    }
    
    /**
     * Creates a new {@code ArgStandard} instance with the supplied usage specification
     * text and command line variables bean.
     * 
     * @param usageText the usage specification text as a String
     * @param bean the command line variables bean object
     * @return a new {@code ArgStandard} instance or null on failure
     */
    public static ArgStandard create (String usageText, Object bean) {
        ArgStandard instance = create();
        instance.setUsageText(usageText);
        if (instance.parseUsage(bean)) {
            return instance;
        }
        return null;
    }
    
    /**
     * Creates a new {@code ArgStandard} instance from the supplied usage specification
     * text and command line variables bean.
     * 
     * @param usageText the usage specification text as an array of Strings
     * @param bean the command line variables bean object
     * @return a new {@code ArgStandard} instance or null on failure
     */
    public static ArgStandard create (String [] usageText, Object bean) {
        ArgStandard instance = create();
        instance.setUsageText(usageText);
        if (instance.parseUsage(bean)) {
            return instance;
        }
        return null;
    }
    
    protected boolean parseUsage (Object bean) {
        reflect.setObject(bean);
        setProgramName(bean.getClass().getSimpleName());
        return (parseUsage());
    }
    
    /**
     * Parses the command line arguments for this argument interface model.
     * 
     * @param args the command line arguments
     * @return the argument index of the first operand or a negative value on error 
     * @see com.fidelis.argface.ArgFace#parse(java.lang.String[])
     */
    public int parse (String[] args) {
        return parseArguments(args);
    }

    protected String modelGetUsageText () {
        return common.getUsageText();
    }
    
    protected String modelGetVersionText () {
        return common.getVersionText();
    }
    
    protected String modelGetAboutText () {
        return common.getAboutText();
    }
    
    protected String modelGetHelpText () {
        return common.getHelpText();
    }
    
    protected Boolean modelGetAllowOverwrite () {
        return common.getAllowOverwrite();
    }
    
    protected Boolean modelGetSuppressHelp () {
        return common.getSuppressHelp();
    }
    
    protected Boolean modelGetPosixFormat () {
        return common.getPosixFormat();
    }
    
    protected Boolean modelGetSortOptions () {
        return common.getSortOptions();
    }
    
    protected String modelGetOperandSuffix () {
        return common.getOperandSuffix();
    }
    
    protected String modelGetOptionSuffix () {
        return common.getOptionSuffix();
    }
    
    protected boolean modelPostProcess (ArgOperand operand) {
        return common.postProcess(operand);
    }
    
    protected boolean modelPostProcess (ArgOption option) {
        return common.postProcess(option);
    }
    
    protected void modelSetNonOptions (List<String> nonOptionList) {
        common.setNonOptions(nonOptionList);
    }
    
    protected void modelSetProgramVariables (List<ArgOperand> operands,
            List<ArgOption> options) {
        common.setProgramVariables(operands, options);
    }
   
}
