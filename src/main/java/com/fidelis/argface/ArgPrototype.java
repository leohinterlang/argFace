/**
 *+
 *  ArgPrototype.java
 *	1.0.0	Apr 18, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.List;

/**
 * The prototype implementation of ArgFace.
 * Allows access to {@code private} variables through reflection.
 * A SecurityManager may prohibit such access.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgPrototype extends ArgBase implements ArgFace {
    private static ArgPrototype instance;
    private ArgReflect reflect;
    private ArgCommon common;
    
    /**
     * Private no argument constructor.
     */
    private ArgPrototype () {
        reflect = new ArgReflect();
        reflect.setPrivateAccess(true);
        common = new ArgCommon(reflect);
    }
    
    /**
     * Creates or obtains the one and only {@code ArgPrototype} instance.
     * Once created, this method may be used at any time to obtain this instance.
     * 
     * @return the one and only {@code ArgPrototype} instance
     * @see #create(String)
     * @see #create(String[])
     */
    public static ArgPrototype create () {
        if (instance == null) {
            instance = new ArgPrototype();
        }
        return instance;
    }
    
    /**
     * Creates the one and only {@code ArgPrototype} instance and sets the
     * usage text specification.
     * 
     * @param usageText the usage text as a String
     * @return the one and only {@code ArgPrototype} instance
     * @see #create()
     * @see #create(String[])
     * @see #setUsageText(String)
     */
    public static ArgPrototype create (String usageText) {
        create().setUsageText(usageText);
        return instance;
    }
    
    /**
     * Creates the one and only {@code ArgPrototype} instance and sets the
     * usage text specification.
     * 
     * @param usageText the usage text as an array of Strings
     * @return the one and only {@code ArgPrototype} instance
     * @see #create()
     * @see #create(String)
     * @see #setUsageText(String[])
     */
    public static ArgPrototype create (String[] usageText) {
        create().setUsageText(usageText);
        return instance;
    }
    
    public static ArgPrototype create (String usageText, Object pojo) {
        create().setUsageText(usageText);
        if (instance.parseUsage(pojo)) {
            return instance;
        }
        return null;
    }
    
    public static ArgPrototype create (String [] usageText, Object pojo) {
        create().setUsageText(usageText);
        if (instance.parseUsage(pojo)) {
            return instance;
        }
        return null;
    }
    
    private boolean parseUsage (Object pojo) {
        reflect.setObject(pojo);
        setProgramName(pojo.getClass().getSimpleName());
        return parseUsage();
    }

    /**
     * Returns the "usage" text defined by the program.
     * 
     * @return the usage text or null
     */
    @Override
    protected String modelGetUsageText () {
        return common.getUsageText();
    }
    
    /**
     * Returns the "version" text defined by the program.
     * 
     * @return the version text or null
     */
    @Override
    protected String modelGetVersionText () {
        return common.getVersionText();
    }
    
    /**
     * Returns the "about" text defined by the program.
     * 
     * @return the about text or null
     */
    @Override
    protected String modelGetAboutText () {
        return common.getAboutText();
    }
    
    /**
     * Returns the extended "help" text defined by the program.
     * The help text appears after the standard output from the
     * --help option.
     * 
     * @return the extended help text or null
     */
    @Override
    protected String modelGetHelpText () {
        return common.getHelpText();
    }
    
    /**
     * Returns the boolean value "allowOverwrite" as defined by the program.
     * This value determines what action should be taken if an option that takes an
     * argument is specified more than once and has not been specified as repeatable.
     * <p>
     * The default action (when this variable is not defined) is to treat this situation
     * as an error. If allowOverwrite is defined as {@code false}, then the first value
     * entered will be retained. If set to {@code true}, subsequent entries on the command
     * line will overwrite any previous value so that the last one entered remains as the
     * final value.
     * 
     * @return the allow overwrite operating mode or null
     */
    @Override
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
    
    public int parse (String [] args) {
        return parseArguments(args);
    }
    
}
