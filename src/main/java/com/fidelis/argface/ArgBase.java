/**
 *+
 *  ArgBase.java
 *	1.0.0	Apr 18, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for ArgFace implementations.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 */
public abstract class ArgBase implements ArgFace {

    private ArgParseUsage    parser;
    private ArgHelp          help;
    private ArgUtil          util;
    private ArgPattern		 pattern;

    private String           programName;
    private String           usageText;
    private String           versionText;
    private String           aboutText;
    private String           helpText;
    private String           optionSuffix;
    private String           operandSuffix;
    
    // Operating variables.
    private Boolean          allowOverwrite;
    private Boolean          suppressHelp;
    private Boolean          posixFormat;
    private Boolean          sortOptions;

    private List<ArgOperand> varList;
    private List<ArgOperand> litList;
    private List<ArgOption>  optionList;
    private ArgList          argList;
    private String           letters;
    private boolean          helpOption;
    private boolean          versionOption;
    private boolean          aboutOption;
    private List<String>     nonOptionList;
    private List<ArgOperand> targetOperands;
    private List<ArgOption>  targetOptions;
    private boolean          patternWatch;
    private boolean          preParseDone;
    private String            patternMatch;

    protected ArgBase () {
        parser = new ArgParseUsage();
        help = new ArgHelp();
        util = ArgUtil.getInstance();
        util.setBase(this);
        pattern = new ArgPattern();
    }

    protected String modelGetUsageText () {
        return null;
    }

    protected String modelGetVersionText () {
        return null;
    }

    protected String modelGetAboutText () {
        return null;
    }

    protected String modelGetHelpText () {
        return null;
    }

    protected Boolean modelGetAllowOverwrite () {
        return false;
    }

    protected Boolean modelGetSuppressHelp () {
        return false;
    }

    protected Boolean modelGetPosixFormat () {
        return false;
    }
    
    protected Boolean modelGetSortOptions () {
        return false;
    }

    protected String modelGetOperandSuffix () {
        return null;
    }

    protected String modelGetOptionSuffix () {
        return null;
    }

    protected boolean modelPostProcess (ArgOperand operand) {
        return true;
    }

    protected boolean modelPostProcess (ArgOption option) {
        return true;
    }

    protected void modelSetNonOptions (List<String> nonOptionList) {
    }

    protected void modelSetProgramVariables (List<ArgOperand> operands,
            List<ArgOption> options) {
    }

    /**
     * Returns {@code true} if the command line has the named argument.
     * 
     * @param name the argument name
     * @return {@code true} if the command line has the named argument
     */
    public boolean has (String name) {
        if (checkOption(name)) {
            ArgOption option = findNamedOption(name);
            if (option != null) {
                return option.has();
            }
        }
        if (checkOperand(name)) {
            ArgOperand operand = findNamedOperand(name);
            if (operand != null) {
                return operand.has();
            }
        }
        return false;
    }

    /**
     * Returns the number of times that the named argument was specified on the
     * command line.
     * 
     * @param name the name of the argument
     * @return the count
     */
    public int count (String name) {
        if (checkOption(name)) {
            ArgOption option = findNamedOption(name);
            if (option != null) {
                return option.getCount();
            }
        }
        if (checkOperand(name)) {
            ArgOperand operand = findNamedOperand(name);
            if (operand != null) {
                return operand.getCount();
            }
        }
        return 0;
    }

    /**
     * Returns the value String for the named argument. For an option that takes
     * an argument, the option argument String is returned. For an operand, the
     * value of the operand is returned.
     * 
     * @param name the name of the argument
     * @return the option argument or operand String or null
     */
    public String value (String name) {
        if (checkOption(name)) {
            ArgOption option = findNamedOption(name);
            if (option != null) {
                return option.getArgValue();
            }
        }
        if (checkOperand(name)) {
            ArgOperand operand = findNamedOperand(name);
            if (operand != null) {
                return operand.getValue();
            }
        }
        return null;
    }

    public String [] valueArray (String name) {
        if (checkOption(name)) {
            ArgOption option = findNamedOption(name);
            if (option != null) {
                List<String> list = option.getList();
                if (list != null) {
                    return list.toArray(new String [0]);
                }
            }
        }
        if (checkOperand(name)) {
            ArgOperand operand = findNamedOperand(name);
            if (operand != null) {
                List<String> list = operand.getList();
                if (list != null) {
                    return list.toArray(new String [0]);
                }
            }
        }
        return null;
    }

    /**
     * Returns a list of Strings for the named argument.
     * 
     * @param name the argument name
     * @return a list of Strings or null
     */
    public List<String> valueList (String name) {
        if (checkOption(name)) {
            ArgOption option = findNamedOption(name);
            if (option != null) {
                return option.getList();
            }
        }
        if (checkOperand(name)) {
            ArgOperand operand = findNamedOperand(name);
            if (operand != null) {
                return operand.getList();
            }
        }
        return null;
    }
    
    public void setNonOptionList (List<String> nonOptionList) {
    	this.nonOptionList = nonOptionList;
    }

    public String [] operandArray () {
        return nonOptionList.toArray(new String [0]);
    }

    public List<String> operandList () {
        return nonOptionList;
    }

    boolean checkOption (String name) {
        if (name.startsWith("<")) {
            return false;
        }
        return true;
    }

    boolean checkOperand (String name) {
        if (name.startsWith("-")) {
            return false;
        }
        return true;
    }

    /**
     * Finds the named option. The option name may include one or two leading
     * dashes, but they are not required.
     * 
     * @param name the option name
     * @return the {@code ArgOption} or null
     */
    ArgOption findNamedOption (String name) {
        if (optionList == null) {
            return null;
        }
        name = name.startsWith("-") ? name.substring(1) : name;
        name = name.startsWith("-") ? name.substring(1) : name;
        for (ArgOption option : optionList) {
            if (option.nameMatch(name)) {
                return option;
            }
        }
        return null;
    }

    /**
     * Finds the named operand.
     * 
     * @param name the operand name
     * @return the {@code ArgOperand} or null
     */
    ArgOperand findNamedOperand (String name) {
        if (name.startsWith("<")) {
            name = name.substring(1);
        }
        if (name.endsWith(">")) {
            name = name.substring(0, name.length() - 1);
        }
        ArgOperand operand = findOperand(name, varList);
        if (operand == null) {
            operand = findOperand(name, litList);
        }
        return operand;
    }

    private ArgOperand findOperand (String name, List<ArgOperand> list) {
        if (list == null) {
            return null;
        }
        for (ArgOperand operand : list) {
            if (operand.getName().equals(name)) {
                return operand;
            }
        }
        return null;
    }

    /**
     * Prints the "usage" text. This method is not affected by the
     * "suppressHelp" setting.
     */
    public void printUsage () {
        help.outputUsage();
    }

    /**
     * Prints the "help" text. This method is not affected by the "suppressHelp"
     * setting.
     */
    public void printHelp () {
        help.outputHelp();
    }

    public String getProgramName () {
    	return programName;
    }
    
    /**
     * Sets the program name.
     * 
     * @param programName the name of the program
     */
    public void setProgramName (String programName) {
        this.programName = programName;
    }

    /**
     * Sets the "usage" text from a String.
     * 
     * @param usageText the usage text
     */
    public void setUsageText (String usageText) {
        this.usageText = usageText;
    }

    /**
     * Sets the "usage" text from an array of Strings.
     * 
     * @param usageText the usage text
     */
    public void setUsageText (String [] usageText) {
        setUsageText(arrayToString(usageText));
    }

    /**
     * Sets the "version" text as a String.
     * 
     * @param versionText the version text
     */
    public void setVersionText (String versionText) {
        this.versionText = versionText;
    }

    /**
     * Sets the "version" text as an array of Strings.
     * 
     * @param versionText the version text
     */
    public void setVersionText (String [] versionText) {
        setVersionText(arrayToString(versionText));
    }

    /**
     * Sets the "about" text as a String.
     * 
     * @param aboutText the about text
     */
    public void setAboutText (String aboutText) {
        this.aboutText = aboutText;
    }

    /**
     * Sets the "about" text as an array of Strings.
     * 
     * @param aboutText the about text
     */
    public void setAboutText (String [] aboutText) {
        setAboutText(arrayToString(aboutText));
    }

    /**
     * Sets the "help" text as a String.
     * 
     * @param helpText the help text
     */
    public void setHelpText (String helpText) {
        this.helpText = helpText;
    }

    /**
     * Sets the "help" text as an array of Strings.
     * 
     * @param helpText the help text
     */
    public void setHelpText (String [] helpText) {
        setHelpText(arrayToString(helpText));
    }

    /**
     * Sets the operand suffix.
     * 
     * @param operandSuffix the operand suffix
     */
    public void setOperandSuffix (String operandSuffix) {
        this.operandSuffix = operandSuffix;
    }

    /**
     * Sets the option suffix.
     * 
     * @param optionSuffix the option suffix
     */
    public void setOptionSuffix (String optionSuffix) {
        this.optionSuffix = optionSuffix;
    }

    /**
     * Sets the "allowOverwrite" operating mode.
     * 
     * @param allowOverwrite {@code true} to allow overwrite mode
     */
    public void setAllowOverwrite (boolean allowOverwrite) {
        this.allowOverwrite = allowOverwrite;
    }

    /**
     * Sets the "suppressHelp" operating mode.
     * 
     * @param suppressHelp {@code true} to suppress the help facility
     */
    public void setSuppressHelp (boolean suppressHelp) {
        this.suppressHelp = suppressHelp;
    }

    /**
     * Sets the "posixFormat" operating mode.
     * 
     * @param posixFormat {@code true} requires options before operands
     */
    public void setPosixFormat (boolean posixFormat) {
        this.posixFormat = posixFormat;
    }
    
    /**
     * Sets the "sortOptions" feature.
     * If the option list is already available, the sort is done immediately.
     * 
     * @param sortOptions {@code true} enables the sort options feature
     */
    public void setSortOptions (boolean sortOptions) {
        this.sortOptions = sortOptions;
        if (sortOptions) {
        	if (optionList != null) {
        		Collections.sort(optionList);
        	}
        }
    }
    
    /**
     * Sets the "patternWatch" feature.
     * 
     * @param patternWatch {@code true} enables the pattern watch feature
     */
    public void setPatternWatch (boolean patternWatch) {
        this.patternWatch = patternWatch;
    }

    /**
     * @return the usageText
     */
    public String getUsageText () {
        return usageText;
    }

    /**
     * Returns the "version" text.
     * 
     * @return the version text
     */
    public String getVersionText () {
        if (versionText == null) {
            versionText = modelGetVersionText();
        }
        return versionText;
    }

    /**
     * Returns the "about" text.
     * 
     * @return the about text
     */
    public String getAboutText () {
        if (aboutText == null) {
            aboutText = modelGetAboutText();
        }
        return aboutText;
    }

    /**
     * Returns the "help" text.
     * 
     * @return the help text
     */
    public String getHelpText () {
        if (helpText == null) {
            helpText = modelGetHelpText();
        }
        return helpText;
    }

    /**
     * Returns the "suppressHelp" operating mode.
     * 
     * @return {@code true} if suppress help mode is enabled
     */
    public boolean isSuppressHelp () {
        if (suppressHelp == null) {
            suppressHelp = modelGetSuppressHelp();
        }
        return suppressHelp;
    }

    /**
     * Returns {@code true} if posix format is enabled.
     * 
     * @return {@code true} if posix format is enabled.
     */
    public boolean isPosixFormat () {
        if (posixFormat == null) {
            posixFormat = modelGetPosixFormat();
        }
        return posixFormat;
    }
    
    /**
     * Returns {@code true} if sort options is enabled.
     * 
     * @return {@code true} if sort options is enabled
     */
    public boolean isSortOptions () {
        if (sortOptions == null) {
            sortOptions = modelGetSortOptions();
        }
        return sortOptions;
    }

    /**
     * Returns the operand suffix.
     * 
     * @return the operand suffix
     */
    public String getOperandSuffix () {
        if (operandSuffix == null) {
            operandSuffix = modelGetOperandSuffix();
        }
        return operandSuffix;
    }

    /**
     * Returns the option suffix.
     * 
     * @return the option suffix
     */
    public String getOptionSuffix () {
        if (optionSuffix == null) {
            optionSuffix = modelGetOptionSuffix();
        }
        return optionSuffix;
    }

    /**
     * Returns the variables operand list.
     * 
     * @return the variables operand list
     */
    public List<ArgOperand> getVarList () {
        return varList;
    }
    
    public void setVarList (List<ArgOperand> varList) {
    	this.varList = varList;
    }

    /**
     * Returns the literals operand list.
     * 
     * @return the literals operand list
     */
    public List<ArgOperand> getLitList () {
        return litList;
    }
    
    public void setLitList (List<ArgOperand> litList) {
    	this.litList = litList;
    }

    /**
     * Returns the option list.
     * 
     * @return the option list
     */
    public List<ArgOption> getOptionList () {
        return optionList;
    }
    
    public void setOptionList (List<ArgOption> optionList) {
    	this.optionList = optionList;
    }
    
    public ArgList getArgList () {
    	return argList;
    }
    
    public void setArgList (ArgList argList) {
    	this.argList = argList;
    }
    
    /**
     * Returns the matching pattern text following successful argument parsing.
     * 
     * @return the matching pattern text
     */
    public String getPatternMatch () {
        return patternMatch;
    }
    
    /**
     * Reports the results of command line parsing.
     * 
     * @param selection indicates the desired output.
     */
    public void report (String selection) {
    	ArgReport.report(selection, this);
    }

    /**
     * Converts an array of Strings to a String. New line characters are added
     * after each element of the array.
     * 
     * @param array the String array
     * @return newline delimited String
     */
    private String arrayToString (String [] array) {
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s);
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Parses the "usage" text to produce the argument lists.
     * 
     * @return {@code true} on success
     */
    protected boolean parseUsage () {
        if (usageText == null) {
            ArgUtil.printError("No usage text");
            return false;
        }
        util.setProgramName(programName);
        util.setOperandSuffix(getOperandSuffix());
        util.setOptionSuffix(getOptionSuffix());
        if (! parser.parse(usageText)) {
            return false;
        }
        programName = parser.getProgramName();
        varList = parser.getVarList();
        litList = parser.getLitList();
        optionList = parser.getOptionList();
        argList = parser.getArgList();
        if (! postProcess()) {
            return false;
        }
        preParseDone = false;
        return true;
    }

    /**
     * Parses the command line arguments. This method may be used to test the
     * results of different argument sets without having to parse the usage text
     * for each test case.
     * 
     * @param args the command line arguments
     * @return the number of arguments remaining or a negative value on error
     */
    protected int parseArguments (String [] args) {
        if (! preParse()) {
            return -1;
        }
        help.initProblems();
        reset();
        targetOptions.clear();
        int nArg = 0;
        ArgNode node = argList.goHome();
        while (node != null) {
            Debug.trace(argList.buildUsageSpec(node));
            argList.setCurrent(node);
            nArg = parseArguments(node, args);
            if (nArg >= 0) {
                modelSetProgramVariables(targetOperands, targetOptions);
                break;
            } else if (nArg == -999) {
                break;
            }
            reset();
            targetOptions.clear();
            nonOptionList.clear();
            node = node.getSouth();
            Debug.trace("----------------");
        }
        if (nArg < 0) {
            if (nArg != -999) {
                ArgUtil.printError("Missing or invalid arguments");
                ArgUtil.printArgs(args);
                help.printProblems();
            }
            nArg = -1;
        } else {
            modelSetNonOptions(nonOptionList);
        }
        return nArg;
    }
    
    private void reset () {
        for (ArgOperand operand : varList) {
            operand.reset();
        }
        for (ArgOperand operand : litList) {
            operand.reset();
        }
        for (ArgOption option : optionList) {
            option.reset();
        }
    }
    
    private int parseArguments (ArgNode start, String [] args) {
        int nArg = parseArgOne(start, args);
        if (nArg < 0) {
            return nArg;
        }
        int n = parseArgTwo(start);
        if (n < 0) {
            nArg = n;
        }
        return nArg;
    }

    /**
     * Parse arguments step one. Separate the options and their arguments from
     * the operands.
     * 
     * @param start the starting node of the arguments list
     * @param args the command line arguments
     * @return the argument index or negative if there is an error
     */
    private int parseArgOne (ArgNode start, String [] args) {
        int nArg = 0;
        int firstOperand = -1;
        boolean takeAllArgs = false;
        boolean separatorSeen = false;
        nonOptionList.clear();

        // Process each argument.
        for (nArg = 0; nArg < args.length; nArg++) {
            String arg = args[nArg];
            Debug.trace("arg: " + arg);
            if (takeAllArgs) {
                addNonOption(arg);
                continue;
            }
            String name = null;
            boolean doubleDash = false;

            // Double dash alone is a nonOption.
            // Else, begin to isolate option name.
            if (arg.startsWith("--")) {
                doubleDash = true;
                if (arg.length() == 2) {
                    takeAllArgs = true;
                    if (posixFormat) {
                        firstOperand = nArg;
                    }
                    continue;
                } else {
                    name = arg.substring(2);
                }
            }

            // Single dash alone is a nonOption.
            // Else, begin to isolate option name.
            else if (arg.startsWith("-")) {
                if (arg.length() == 1) {
                    addNonOption(arg);
                    if (posixFormat) {
                        takeAllArgs = true;
                        firstOperand = nArg;
                    }
                    continue;
                } else {
                    name = arg.substring(1);
                }
            }

            // Not dash or double dash, regular nonOption.
            else {
                addNonOption(arg);
                if (posixFormat) {
                    takeAllArgs = true;
                    firstOperand = nArg;
                }
                continue;
            }

            separatorSeen = false;
            String value = null;

            // Option contains an equal sign or colon separator.
            if (name.contains("=") || name.contains(":")) {

                // Split name from value.
                // Also allows <option>= with argument yet to come.
                separatorSeen = true;
                String [] parts = name.split("[=:]", 2);
                name = parts[0];
                value = parts[1];
                if (value.isEmpty()) {
                    value = null;
                }
            }

            // A letter group must use a single dash.
            else if ((!doubleDash) && (letterGroup(letters, name))) {

                // Option is a group of single letters w/o arguments.
                // Set them each to true and continue.
                for (int n = 0; n < name.length(); n++) {
                    String letter = name.substring(n, n + 1);
                    ArgOption opt = findOption(start, letter);
                    if (opt != null) {
                        setOptionTrue(opt);
                        targetOptions.add(opt);
                    } else {
                        return -1;
                    }
                }
                continue;
            }

            // Option is single letter with attached argument.
            else if (letterArg(letters, name)) {

                // Isolate option letter from the value.
                value = name.substring(1);
                name = name.substring(0, 1);
            }

            // Find the Option.
            // If not, try abbreviation
            ArgOption option = findOption(start, name);
            if (option == null) {
                option = findOptionAbb(start, name);
            }

            // No option found.
            if (option == null) {

                // Does this option even exist in the option list?
                if (findOption(name) != null) {

                    // Yes, try another usage.
                    String text = "Option -" + name + " does not apply to this usage";
                    help.addProblem(start, text);
                    return -1;
                }
                // Otherwise, print bad option and stop.
                else {
                    ArgUtil.printError("Invalid option: " + arg);
                    return -999;
                }
            }

            // Option found.
            else {
                
                // Check this option against others in target list
                // for mutual exclusion.
                if (mutexOption(start, option)) {
                    return -1;
                }
                
                // Set option as true and add to target list.
                setOptionTrue(option);
                targetOptions.add(option);

                // help option.
                // Print help and exit.
                if (option.nameMatch("help")) {
                    if (help.printHelp()) {
                        nArg = args.length;
                        break;
                    }
                }

                // version option.
                // Print version text and exit.
                if (option.nameMatch("version")) {
                    if (help.printVersion()) {
                        nArg = args.length;
                        break;
                    }
                }

                // about option.
                // Print about text and exit.
                if (option.nameMatch("about")) {
                    if (help.printAbout()) {
                        nArg = args.length;
                        break;
                    }
                }

                // This option does not include an argument.
                // Continue with next program arg.
                String argName = option.getArgName();
                if (argName == null) {
                    continue;
                }

                // Argument value is not yet known.
                if (value == null) {

                    // The option argument is optional.
                    if (option.isArgOptional()) {

                        // More program args.
                        if (nArg + 1 < args.length) {

                            // Separator already seen, take next argument.
                            if (separatorSeen) {
                                value = args[++nArg];
                            }

                            // Separator not seen, check next arg.
                            // If it doesn't start with '=' or ':', argument not
                            // specified.
                            // Continue with next program arg.
                            else {
                                if (!args[nArg + 1].startsWith("=")
                                        && !args[nArg + 1].startsWith(":")) {
                                    continue;
                                }

                                // Next arg starts with separator.
                                // Isolate value.
                                else {
                                    value = args[++nArg].substring(1);

                                    // Is there more?
                                    if (value.length() == 0) {

                                        // No. Another program arg?
                                        if (nArg + 1 < args.length) {

                                            // Yes.
                                            value = args[++nArg];
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Value not known, option argument is required.
                    else {
                        while (++nArg < args.length) {

                            // Get next program arg.
                            value = args[nArg];

                            // If it starts with '=' or ':', isolate value.
                            if (value.startsWith("=") || value.startsWith(":")) {
                                value = value.substring(1);

                                // More after separator, done.
                                if (value.length() > 0) {
                                    break;
                                }
                            }

                            // Doesn't start with separator, done.
                            else {
                                break;
                            }
                        }
                    }
                }
                Debug.trace("opt: " + value);
                setArgString(option, value);
            }
        }
        if (firstOperand >= 0) {
            nArg = firstOperand;
        }
        return nArg;
    }
 
    private int parseArgTwo (ArgNode usageNode) {
        
        // Setup for patterns.
        pattern.setArgList(argList);
        pattern.setNonOptionList(nonOptionList);
        pattern.setPatternWatch(patternWatch);
        
        // Match non options to usage patterns.
        patternMatch = null;
        if (pattern.matchUsage(usageNode)) {
            targetOperands = pattern.getTargetOperands();
            patternMatch = pattern.getPatternMatch();
            return 0;
        }
        return -1;
    }

   /* private boolean exitOnNonOption (String arg) {
        if (! posixFormat) {
            addNonOption(arg);
        }
        return posixFormat;
    }*/

    private void addNonOption (String arg) {
        nonOptionList.add(arg);
    }
    
    private ArgOption findOption (String name) {
        for (ArgOption option : optionList) {
            if (option.nameMatch(name)) {
                return option;
            }
        }
        return null;
    }

    private ArgOption findOption (ArgNode start, String name) {
        ArgOption option = null;
        ArgNode south = null;
        ArgList group = null;
        ArgNode node = start.getEast();
        while (node != null) {
            option = node.getOption();
            if (option != null) {
                if (option.nameMatch(name)) {
                    return option;
                }
            }
            
            // If this is a group node, check the group.
            else if ((group = node.getGroup()) != null) {
                option = findGroup(group, name);
                if (option != null) {
                    return option;
                }
            }
            
            // If there is an alternative branch, check the alternatives.
            if ((south = node.getSouth()) != null) {
                option = findAlternative(south, name);
                if (option != null) {
                    return option;
                }
            }
            node = node.getEast();
        }
        return null;
    }
    
    private ArgOption findAlternative (ArgNode node, String name) {
        ArgOption option = null;
        ArgList group = null;
        while (node != null) {
            option = node.getOption();
            if (option != null) {
                if (option.nameMatch(name)) {
                    return option;
                }
            } else if ((group = node.getGroup()) != null) {
                option = findGroup(group, name);
                if (option != null) {
                    return option;
                }
            }
            node = node.getSouth();
        }
        return null;
    }
    
    private ArgOption findGroup (ArgList list, String name) {
        ArgNode node = list.goHome();
        return findOption(node, name);
    }
    
    /**
     * Tests the specified option against other options in the target list
     * for mutual exclusion. A list is created from the argument list that
     * contains all of the options that are mutually exclusive with respect
     * to the specified option. Then each of these are checked against the
     * previously specified options in the target list.
     * 
     * @param start the {@code ArgNode} of the usage specification
     * @param option the {@code ArgOption} to be checked
     * @return {@code true} if there is a mutual exclusion conflict
     */
    private boolean mutexOption (ArgNode start, ArgOption option) {
        List<ArgOption> mutexList = new ArrayList<ArgOption>();
        if (mutexCollect(start, option, mutexList)) {
            for (ArgOption opt : mutexList) {
                for (ArgOption target : targetOptions) {
                    if (target == opt) {
                        String text = "Mutually exclusive options: -" +
                            target.getName() + " and -" + option.getName();
                        help.addProblem(start, text);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean mutexCollect (ArgNode start, ArgOption option, List<ArgOption> list) {
        ArgNode node = start.getEast();
        ArgList group = null;
        while (node != null) {
            ArgOption opt = node.getOption();
            if (opt != null) {
                if (opt == option) {
                    return ! list.isEmpty();
                }
            } else if ((group = node.getGroup()) != null) {
                if (mutexGroup(group, option, list)) {
                    return true;
                }
            }
            if (node.getSouth() != null) {
                if (mutexAlternative(node, option, list)) {
                    return true;
                }
            }
            node = node.getEast();
        }
        return false;
    }
    
    private boolean mutexGroup (ArgList group, ArgOption option, List<ArgOption> list) {
        ArgNode node = group.goHome();
        return mutexCollect(node, option, list);
    }
    
    private boolean mutexAlternative (ArgNode node, ArgOption option, List<ArgOption> list) {
        boolean status = false;
        ArgList group = null;
        while (node != null) {
            ArgOption opt = node.getOption();
            if (opt != null) {
                list.add(opt);
                if (opt == option) {
                    status = true;
                }
            } else if ((group = node.getGroup()) != null) {
                if (mutexGroup(group, option, list)) {
                    return true;
                }
            }
            node = node.getSouth();
        }
        if (status == false) {
            list.clear();
        }
        return status;
    }

    private ArgOption findOptionAbb (ArgNode start, String abb) {
        return null;
    }

    private boolean postProcess () {
        boolean status = true;
        initLetters();
        helpOption = false;
        versionOption = false;
        aboutOption = false;

        // Post process the operands.
        for (ArgOperand operand : varList) {
            if (!postProcess(operand)) {
                status = false;
            }
        }
        for (ArgOperand operand : litList) {
            if (!postProcess(operand)) {
                status = false;
            }
        }

        // Post process the options.
        for (ArgOption option : optionList) {
            if (!postProcess(option)) {
                status = false;
            }
        }
        
        // Check argument list for "options" operands.
        checkAllOptions();
        
        // Initialize help.
        help.setArgList(argList);
        help.setOptionList(optionList);
       
        return status;
    }
    
    public ArgHelp getHelp () {
    	return help;
    }
    
    private boolean preParse () {
        
        boolean status = true;
        
        // Only run this once after usage has been parsed.
        if (preParseDone) {
            return status;
        } else {
            preParseDone = true;
        }
        
        // Sort options prior to adding help, version, or about.
        if (isSortOptions()) {
            Collections.sort(optionList);
        }
        
        // If help, version, or about options are not specified,
        // create a new usage alternative for them.
        if (!helpOption || !versionOption || !aboutOption) {
            ArgOperand operand = new ArgOperand().literal();
            operand.setName(programName);
            argList.goHome();
            argList.endSouth();
            argList.addSouth(operand);
        }
        ArgOption option = null;
        boolean exor = false;
        if (!helpOption) {
            option = createHelpOption();
            modelPostProcess(option);
            optionList.add(option);
            argList.addEast(option);
            exor = true;
        }
        if ((!versionOption) && (getVersionText() != null)) {
            option = createVersionOption();
            modelPostProcess(option);
            optionList.add(option);
            if (exor) {
                argList.addSouth(option);
            } else {
                argList.addEast(option);
                exor = true;
            }
        }
        if ((!aboutOption) && (getAboutText() != null)) {
            option = createAboutOption();
            modelPostProcess(option);
            optionList.add(option);
            if (exor) {
                argList.addSouth(option);
            } else {
                argList.addEast(option);
            }
        }
        help.setAboutText(getAboutText());
        help.setVersionText(getVersionText());
        help.setHelpText(getHelpText());
        help.setSuppressHelp(isSuppressHelp());
        isPosixFormat();
        nonOptionList = new ArrayList<String>();
        targetOptions = new ArrayList<ArgOption>();
        return status;
    }

    private boolean postProcess (ArgOperand operand) {
        
        // Special handling for literal "options" operand.
        if ((operand.isLiteral()) &&
            (operand.getName().equalsIgnoreCase("options"))) {
            return true;
        }

        // If repeating operand, set the list.
        if (operand.isRepeat()) {
            operand.setList(new ArrayList<String>());
        }

        // Model specific operand post processing.
        return modelPostProcess(operand);
    }

    private boolean postProcess (ArgOption option) {
        String name = option.getName();
        String altName = option.getAltName();

        // Check if help, version, or about options were specified.
        if (option.nameMatch("help")) {
            helpOption = true;
        }
        if (option.nameMatch("version")) {
            versionOption = true;
        }
        if (option.nameMatch("about")) {
            aboutOption = true;
        }

        // Accumulate the single letter options.
        addLetters(name);
        if (altName != null) {
            addLetters(altName);
        }

        // If repeating option, set the list.
        if (option.isRepeat()) {
            option.setList(new ArrayList<String>());
        }

        // Model specific option post processing.
        if (!modelPostProcess(option)) {
            return false;
        }
        return true;
    }
    
    private void checkAllOptions () {
        ArgNode node = null;
        for (node = argList.goHome(); node != null; node = argList.goSouth()) {
            while ((node = node.getEast()) != null) {
                ArgOperand operand = node.getOperand();
                if (operand != null) {
                    if ((operand.isLiteral()) &&
                        (operand.getName().equalsIgnoreCase("options"))) {
                        includeAllOptions(node);
                    }
                }
            }
        }
    }
    
    private void includeAllOptions (ArgNode node) {
        ArgNode saveNode = argList.getCurrent();
        argList.setCurrent(node);
        for (ArgOption option : optionList) {
            if (findOption(node, option.getName()) != null) {
                continue;
            } else {
                node = argList.insertEast(option);
            }
        }
        argList.setCurrent(saveNode);
    }

    private void initLetters () {
        letters = "";
    }

    private void addLetters (String name) {
        if (name.length() == 1) {
            if (letters.contains(name)) {
                ArgUtil.printError("Warning: Duplicate option letter: -"
                        + name);
            } else {
                letters += name;
            }
        }
    }

    private boolean letterGroup (String letters, String name) {

        // If letter group spells another option, not a letter group.
        ArgOption opt = findOption(name);
        if (opt != null) {
            return false;
        }

        // If just one letter, not a group.
        if (name.length() == 1) {
            return false;
        }

        // Check each letter.
        for (int n = 0; n < name.length(); n++) {
            String letter = name.substring(n, n + 1);

            // Not one of the letter options.
            if (!letters.contains(letter)) {
                return false;
            }

            // Letter has no Option.
            opt = findOption(letter);
            if (opt == null) {
                return false;
            }

            // This letter option takes an argument.
            if (opt.getArgName() != null) {
                return false;
            }
        }
        return true;
    }

    private boolean letterArg (String letters, String name) {

        // More than single letter.
        if (name.length() > 1) {

            // If the name matches another option, not letterArg.
            if (findOption(name) != null) {
                return false;
            }
            String letter = name.substring(0, 1);

            // One of the option letters.
            if (letters.contains(letter)) {

                // Find the option for this letter.
                ArgOption opt = findOption(letter);

                // If the letter takes an option, return true.
                if (opt.getArgName() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArgOption createHelpOption () {
        return createOption("h", "help", "show this help text");
    }

    private ArgOption createVersionOption () {
        return createOption("v", "version", "show version information");
    }

    private ArgOption createAboutOption () {
        return createOption("a", "about", "show information about this program");
    }

    private ArgOption createOption (String name, String altName, String helpText) {
        if (letters.contains(name)) {
            name = altName;
            altName = null;
        }
        ArgOption option = new ArgOption(name);
        if (altName != null) {
            option.setAltName(altName);
        }
        option.setSpec(3);
        option.setHelp(helpText);
        option.setCreation(true);
        return option;
    }

    private void setOptionTrue (ArgOption option) {
        option.setHas(true);
        option.setCount(option.getCount() + 1);
    }

    private boolean setArgString (ArgOption option, String value) {
        if (option.isRepeat()) {
            option.addList(value);
        } else if (checkArgSet(option)) {
            option.setArgValue(value);
        } else {
            return false;
        }
        return true;
    }

    private boolean checkArgSet (ArgOption option) {
        if (option.isArgSet()) {
            if (allowOverwrite == null) {
                String text = option.getText();
                ArgUtil.printError("Option " + text + " may not be repeated");
                return false;
            }
            return allowOverwrite;
        }
        option.setArgSet(true);
        return true;
    }

}
