/**
 *+
 *  ArgParseUsage.java
 *	1.0.0	Apr 25, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgParseUsage {

    private TokenSource          source;
    private final String         delimiters = " \t\n\'-+|[]<>()=:,/";
    private String               programName;
    private int                  optionalDepth;
    private boolean              optionDefForm;
    private boolean              exor;
    private boolean              newOption;
    private boolean              specOptions;
    private ArgOperand           operand;
    private ArgOption            option;
    private ArgList              argList;
    private Deque<ArgList>       listStack  = new ArrayDeque<ArgList>();
    private List<ArgOperand>     varList    = new ArrayList<ArgOperand>();
    private List<ArgOperand>     litList    = new ArrayList<ArgOperand>();
    private List<ArgOption>      optionList = new ArrayList<ArgOption>();
    
    /**
     * Parses the specified usage text.
     * 
     * @param usage the usage text
     * @return {@code true} if successful
     */
    public boolean parse (String usage) {
        Debug.trace(usage);
        boolean status = true;
        source = new TokenString(usage, delimiters);
        argList = new ArgList();
        
        // First token. Usage is optional.
        String token = source.next();
        if (token.equalsIgnoreCase("usage")) {
            token = source.next();
        }
        if (token.equals(":")) {
            token = source.next();
        }
        programName = token;
        
        // Each time program name occurs is another use spec.
        while (token.equals(programName)) {
            operand = new ArgOperand().literal();
            operand.setName(programName);
            argList.goHome();
            argList.endSouth();
            argList.addSouth(operand);
            token = parseUseSpec();
        }
        if ("options".equalsIgnoreCase(token)) {
            if (! parseOptionsSection()) {
                return false;
            }
        }
        if ("error".equals(token)) {
            return false;
        }
        for (ArgOperand operand : varList) {
            Debug.trace("np var operand: " + operand);
        }
        for (ArgOperand operand : litList) {
            Debug.trace("np lit operand: " + operand);
        }
        for (ArgOption option : optionList) {
            Debug.trace("np     option:  " + option);
        }
        Debug.trace(argList.toString());
        String u = argList.buildUsage();
        Debug.trace(u);
        return status;
    }
    
    /**
     * Returns the program name as specified in the usage text.
     * 
     * @return the program name from the usage text
     */
    public String getProgramName () {
        return programName;
    }
    
    /**
     * Returns the argument list.
     * 
     * @return the argument list
     */
    public ArgList getArgList () {
        return argList;
    }
    
    /**
     * Returns the variable operands list.
     * 
     * @return the variable operands list
     */
    public List<ArgOperand> getVarList () {
        return varList;
    }
    
    /**
     * Returns the literal operands list.
     * 
     * @return the literal operands list
     */
    public List<ArgOperand> getLitList () {
        return litList;
    }
    
    /**
     * Returns the option list.
     * 
     * @return the option list
     */
    public List<ArgOption> getOptionList () {
        return optionList;
    }
    
    private String parseUseSpec () {
        
        // Get next token after program name.
        String token = source.next();
        
        // Loop until no more tokens.
        while (token != null) {
        	
            Debug.trace("Token: " + token);
            
        	// Stop if program name signals another usage.
        	if (token.equals(programName)) {
        		break;
        	}
 
            // Start of options section. Exit loop.
            if (token.equalsIgnoreCase("options")) {
                break;
            }
            
            // Start optional section.
            else if (token.equals("[")) {
                optionalDepth++;
                if (optionalDepth == 1) {
                    token = source.next();
                    Debug.trace("token: " + token);
                    if (token.equalsIgnoreCase("options")) {
                        token = source.next();
                        if (token.equals("]")) {
                            --optionalDepth;
                            specOptions = true;
                            literalOptions();
                            token = source.next();
                            continue;
                        } else {
                            source.push("options");
                            source.push();
                        }
                    } else {
                        source.push();
                    }
                }
                startGroup();
            }
            
            // End optional section.
            else if (token.equals("]")) {
                optionalDepth--;
                endGroup();
            }
            
            // Variable operand.
            else if (token.equals("<")) {
                varOperand();
            }
            
            // Option indicator.
            else if (token.equals("-")) {
                if (! singleDash()) {
                	return "error";
                }
            }
            
            // Mutual exclusion or alternate option name.
            else if (token.equals("|")) {
                exor = true;
            }
   
            // Group indicator.
            else if (token.equals("(")) {
                startGroup();
            }
            
            // Group end indicator.
            else if (token.equals(")")) {
                endGroup();
            }
            
            // Single quote string.
            else if (token.equals("<sQuote>")) {
                option.setHelp(source.getToken());
            }
            
            // Repeat indicator.
            else if (token.equals("...") || token.equals("+")) {
                if (option != null) {
                    option.setRepeat(true);
                } else if (operand != null) {
                    operand.setRepeat(true);
                }
                argList.setRepeat(true);
            }
            
            // Literal operand.
            else {
            	if (!token.isEmpty()) {
            		literal(token);
            	}
            }
            
            // Get next token.
            token = source.next();
            
        }
        if (token == null) {
        	return "done";
        }
        return token;
    }
    
    private boolean parseOptionsSection () {
        Debug.trace("parse Options section...");
        // Get next token. Colon is optional.
        String token = source.next();
        if (token.equals(":")) {
            token = source.next();
        }
        while (token != null) {
            Debug.trace("Token: " + token);
            
            // Dash.
            if (token.equals("-")) {
                if (! parseOption()) {
                    return false;
                }
            }
            
            // Next token.
            token = source.next();
        }
        return true;
    }
    
    private boolean varOperand () {
        
        option = null;
        
        // Get the full name.
        String name = fullName();
        
        Debug.trace("var operand: " + name);
        
        // Check for end of variable - close angle bracket.
        String token = source.next();
        if (! token.equals(">")) {
            ArgUtil.printError("Invalid operand terminator: <" + name + token);
            return false;
        }
        
        // Operand not already defined. Make a new one.
        // Otherwise, use the one already defined.
        operand = findVar(name);
        if (operand == null) {
            operand = new ArgOperand().variable();
            operand.setName(name);
            varList.add(operand);
        }
        if (exor) {
            exor = false;
            argList.appendSouth(operand);
        } else {
            argList.addEast(operand);
        }
        return true;
    }
    
    private String fullName () {
        
        // Valid name tokens separated by dashes.
        String full = "";
        String token = source.next();
        while (validName(token)) {
            full += token;
            token = source.next();
            if (token.equals("-")) {
                full += token;
                token = source.next();
            } else {
                source.push();
                break;
            }
        }
        return full;
    }
   
    private boolean singleDash () {
        
        operand = null;
        
        // Second dash. Long name.
        String token = source.next();
        if (token.equals("-")) {
            return doubleDash();
        }
        
        // Letter group.
        Debug.trace("single dash: " + token);
        if (token.length() > 1) {
            return letterGroup(token);
        }
        
        // Single letter.
        if (! validLetterOption(token)) {
            return false;
        }
        addOption(token);
        return afterOption();
    }
    
    private void addOption (String name) {
        
        // Use existing option if present.
        // Otherwise, create a new one.
        option = findOption(name);
        if (option == null) {
            option = new ArgOption(name);
            option.setSpec(1);
            optionList.add(option);
        }
        boolean optional = false;
        if (optionalDepth > 0) {
            optional = true;
        }
        if (exor) {
            exor = false;
            argList.appendSouth(option, optional);
        } else {
            argList.addEast(option, optional);
        }
    }
    
    private boolean doubleDash () {
        
        // Get full name.
        String name = fullName();
        Debug.trace("double dash: " + name);
        
        // If option definition format, add alternate name.
        if (optionDefForm) {
            Debug.trace("option def format");
            optionDefForm = false;
            option.setAltName(name);
            option.setSpec(3);
        }
        
        // Otherwise, create new option.
        else {
            addOption(name);
            option.setSpec(1);
        }
        return afterOption();
    }
    
    private boolean letterGroup (String letters) {
        for (int n = 0; n < letters.length(); n++) {
            String letter = letters.substring(n, n + 1);
            if (! validLettersDigits(letter)) {
                ArgUtil.printError("Invalid option letter: -" + letter);
                return false;
            }
            addOption(letter);
            if (option.getSpec() == 0) {
                option.setSpec(1);
            }
        }
        if (afterOption()) {
            return true;
        }
        return false;
    }
    
    private boolean afterOption () {
        
        // Get next token.
        String token = source.next();
        
        // Option may be followed by an argument.
        // It may be optional...
        if (token.equals("[")) {
            return optionArg(true);
        }
        
        // ...or required.
        else if (token.equals("<")) {
            return optionArg(false);
        }
        
        // But a slash or comma indicates a second option name.
        else if (token.equals("/") || token.equals(",")) {
            option.setSpec(3);
            return secondOptionName();
        }
        
        // Otherwise, push back token.
        else {
            source.push();
        }
        return true;
    }
    
    private boolean optionArg (boolean optional) {
        
        // Get the full name.
        String name = fullName();
        
        // Valid terminator.
        String token = source.next();
        if (optional) {
            if (! token.equals("]")) {
                ArgUtil.printError("Invalid option argument terminator: [" +
                        name + token);
                return false;
            }
        } else if (! token.equals(">")) {
            ArgUtil.printError("Invalid option argument terminator: <" +
                    name + token);
            return false;
        }
        option.setArgOptional(optional);
        option.setArgName(name);
        return true;
    }
    
    private void startGroup () {
        listStack.push(argList);
        ArgList group = new ArgList();
        if (exor) {
            exor = false;
            argList.addSouth(group);
        } else {
            argList.addEast(group);
        }
        if (optionalDepth > 0) {
            argList.setOptional(true);
        }
        argList = group;
    }
    
    private void endGroup () {
        ArgList group = argList;
        argList = listStack.pop();
        
        // Get the count of non-options at the base level of the group.
        int baseCount = group.baseCount();
        if (baseCount <= 1) {
            argList.transfer(group);
        }
    }
    
    private void literal (String literal) {
        
        option = null;
        
        // Check for identical literal operand.
        operand = findLit(literal);
        
        // Not found. Create a new one.
        if (operand == null) {
            operand = new ArgOperand().literal();
            operand.setName(literal);
            litList.add(operand);
        }
        if (exor) {
            exor = false;
            argList.appendSouth(operand);
        } else {
            argList.addEast(operand);
        }
        if (optionalDepth > 0) {
            String token = source.next();
            source.push();
            if (token.equals("]")) {
                ArgNode curr = argList.getCurrent();
                ArgNode node = argList.endSouth();
                node.setSpec(2);
                argList.setCurrent(curr);
            }
        }
    }
    
    private void literalOptions () {
        ArgOperand operand = new ArgOperand().literal();
        operand.setName("options");
        operand.setOptional(true);
        litList.add(operand);
        argList.addEast(operand, true);
    }
    
    private boolean parseOption () {
        
        // Next token.
        String name = source.next();
        if (name.equals("-")) {
            name = fullName();
        }
        
        // Single dash. Must be single letter option.
        // Check validity.
        else if (! validLetterOption(name)) {
            return false;
        }
        
        Debug.trace("parse option: " + name);
        
        // If not existing option, create a new one.
        newOption = false;
        option = findOption(name);
        if (option == null) {
            newOption = true;
            option = new ArgOption(name);
        }
        return secondOption();
    }
    
    private boolean secondOption () {
        
        // Next token not a comma. After option and description.
        String token = source.next();
        if (! token.equals(",")) {
            source.push();
            if (! afterOption()) {
                return false;
            }
            if (newOption) {
                addNewOption(1);
            }
            token = source.next();
            if (token.equals("...") || token.equals("+")) {
                option.setRepeat(true);
                argList.setRepeat(true);
                token = source.next();
            }
            return processDescription(token);
        }
        
        // Set second option name.
        if (! secondOptionName()) {
            return false;
        }
        
        // Description text.
        token = source.next();
        return processDescription(token);
    }
    
    private boolean secondOptionName () {
        
        // Second option name.
        String token = source.next();
        if (! token.equals("-")) {
            ArgUtil.printError("Expected second option after comma: " +
                    option.getText() + ", " + token);
            return false;
        }
        
        // Another dash. Second is word option.
        String name = source.next();
        if (name.equals("-")) {
            name = fullName();
        }
        
        // Single letter option.
        else {
            if (! validLetterOption(name)) {
                return false;
            }
        }
        Debug.trace("second option name: " + name);
        
        // If second name does not exist, set alternate name.
        ArgOption opt = findOption(name);
        if (opt == null) {
            if (! setAltName(name)) {
                return false;
            }
            
            // New option. Add to lists.
            if (newOption) {
                addNewOption(3);
            }
        } 
        
        // Option exists but not under the first name.
        // Add first as alternate name.
        else if (newOption) {
            String altName = option.getName();
            option = opt;
            if (! setAltName(altName)) {
                return false;
            }
        }
        
        // Option exists and so did the first one.
        // If they're not the same, that's an error.
        else if (opt != option) {
            ArgUtil.printError("Specified options are different: " +
                    option.getText() + ", " + opt.getText());
            return false;
        }
        
        // Argument specified after option.
        if (! afterOption()) {
            return false;
        }
        return true;
    }
    
    private boolean setAltName (String name) {
        String altName = option.getAltName();
        if (altName == null) {
            option.setAltName(name);
        } else {
            ArgUtil.printError("Option already has an alternate name: " +
                    option.getText() + " : --" + name);
            return false;
        }
        return true;
    }
    
    private void addNewOption (int spec) {
        if (specOptions) {
            option.setSpec(0);
        } else {
            option.setSpec(spec);
        }
        optionList.add(option);
        
        // Find non-option in arg list.
        ArgNode node = argList.goHome();
        node = node.getEast();
        while (node != null) {
            if (node.isOption()) {
                argList.goEast();
                node = node.getEast();
            } else {
                break;
            }
        }
        argList.insertEast(option);
        argList.setOptional(true);
    }
    
    private boolean processDescription (String token) {
        TokenString ts = (TokenString) source;
        ts.setFilterLineBreaks(false);
        ts.setTrimSpaces(false);
        String help = token;
        while (token != null) {
            token = ts.next();
            if (token.equals("\n")) {
                break;
            }
            help += token;
        }
        Debug.trace("Help text: " + help);
        option.setHelp(help);
        ts.setTrimSpaces(true);
        ts.setFilterLineBreaks(true);
        return true;
    }
    
    private ArgOperand findVar (String name) {
        return findOperand(varList, name);
    }
    
    private ArgOperand findLit (String name) {
        return findOperand(litList, name);
    }
    
    private ArgOperand findOperand (List<ArgOperand> list, String name) {
        for (ArgOperand operand : list) {
            if (operand.getName().equals(name)) {
                return operand; 
            }
        }
        return null;
    }
    
    private ArgOption findOption (String name) {
        for (ArgOption option : optionList) {
            if (option.nameMatch(name)) {
                return option;
            }
        }
        return null;
    }
    
    private boolean validLetterOption (String name) {
        if (name.length() != 1) {
            ArgUtil.printError("Invalid letter option: -" + name);
            return false;
        }
        if (! validLettersDigits(name)) {
            ArgUtil.printError("Invalid letter option: -" + name);
            return false;
        }
        return true;
    }
    
    private boolean validName (String name) {
        for (char c : name.toCharArray()) {
            if (Character.isLetterOrDigit(c) || c == '_') {
                continue;
            }
            return false;
        }
        return true;
    }
    
    private boolean validLettersDigits (String name) {
        for (char c : name.toCharArray()) {
            if (! Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

}
