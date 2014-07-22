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
    private static ArgStandard instance;
    private ArgReflect reflect;
    private ArgCommon common;
    
    private ArgStandard () {
        reflect = ArgReflect.getInstance();
        reflect.setPrivateAccess(false);
        common = ArgCommon.getInstance();
    }
    
    /**
     * Creates or obtains the one and only {@code ArgStandard} instance.
     * 
     * @return the one and only {@code ArgStandard} instance
     */
    public static ArgStandard create () {
        if (instance == null) {
            instance = new ArgStandard();
        }
        return instance;
    }
    
    public static ArgStandard create (String usageText) {
        create().setUsageText(usageText);
        return instance;
    }
    
    public static ArgStandard create (String[] usageText) {
        create().setUsageText(usageText);
        return instance;
    }
    
    public static ArgStandard create (String usageText, Object pojo) {
        create().setUsageText(usageText);
        if (instance.parseUsage(pojo)) {
            return instance;
        }
        return null;
    }
    
    public static ArgStandard create (String [] usageText, Object pojo) {
        create().setUsageText(usageText);
        if (instance.parseUsage(pojo)) {
            return instance;
        }
        return null;
    }
    
    private boolean parseUsage (Object pojo) {
        reflect.setObject(pojo);
        setProgramName(pojo.getClass().getSimpleName());
        return (parseUsage());
    }
    
    public int parse (String [] args) {
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
