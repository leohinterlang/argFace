/**
 *+
 *  ArgUtil.java
 *	1.0.0	Apr 18, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * ArgFace utilities.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 */
public class ArgUtil {
    
    private static ArgUtil instance = new ArgUtil();
    private static ArgBase base;
    
    private String programName;
    private String operandSuffix;
    private String optionSuffix;

    /**
     * Private no argument constructor.
     */
    private ArgUtil () {
    }
    
    /**
     * Obtains the one and only {@code ArgUtil} instance.
     * 
     * @return the one and only {@code ArgUtil} instance
     */
    public static ArgUtil getInstance () {
        return instance;
    }
    
    /**
     * Sets the reference to the {@code ArgBase} instance.
     * 
     * @param base the ArgBase reference
     */
    public void setBase(ArgBase base) {
    	ArgUtil.base = base;
    }
    
    /**
     * Returns the program name.
     * 
     * @return the program name
     */
    public String getProgramName () {
        return programName;
    }
    
    /**
     * Sets the program name for error reporting purposes.
     * 
     * @param programName the name of the program
     */
    public void setProgramName (String programName) {
        this.programName = programName;
    }

    /**
     * Returns the operand suffix.
     * 
     * @return the operand suffix
     */
    public String getOperandSuffix () {
        return operandSuffix;
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
     * Returns the option suffix.
     * 
     * @return the option suffix
     */
    public String getOptionSuffix () {
        return optionSuffix;
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
     * Merges two Strings to produce a camelCase result. Here are the
     * rules. The front portion does not change. The first letter of the
     * back portion is capitalized as long as the next letter, the second
     * letter of the back portion, is not upperCase.
     * <p>
     * For example:<pre>
     *  "debug", "Option" -> "debugOption"
     *  "set", "debugOption" -> "setDebugOption"
     *  "set", "aOption" -> "setaOption"</pre>
     * <p>
     * Note that in the last example, because the second letter of "aOption"
     * is capitalized, the lowerCase "a" remains in lowerCase.
     * <p>
     * Since the front portion does not change, this method does not enforce
     * upper camelCase (UpperCamelCase).
     * 
     * @param front the front portion
     * @param back the back portion
     * @return the front and back merged in camel case
     */
    public static String camelCase (String front, String back) {
        String camel = front;
        if ((back.length() > 1)
        &&  (Character.isUpperCase(back.charAt(1)))) {
            camel += back;
        }
        else {
        	camel += Character.toUpperCase(back.charAt(0)) +
        			back.substring(1);
        }
        return camel;
    }

    /**
     * Returns a camelCase {@code String} from text that may contain
     * dashes.
     * 
     * @param text with possible dashes
     * @return camelCase version of the text
     */
    public static String camelCase (String text) {
        if (text == null) {
            return null;
        }
        String [] parts = text.split("-");
        if (parts.length == 1) {
            return text;
        }
        String camel = parts[0];
        for (int n = 1; n < parts.length; n++) {
            camel = camelCase(camel, parts[n]);
        }
        return camel;
    }
    
    /**
     * Prints a "Can't access" message for one or two option variable names
     * from an {@code ArgOption} object.
     * 
     * @param option the {@code ArgOption} object
     */
    public static void cantAccess (ArgOption option) {
        String name = camelCase(option.getName());
        String altName = camelCase(option.getAltName());
        if (altName != null) {
            cantAccess(name + instance.optionSuffix, altName + instance.optionSuffix);
        } else {
            cantAccess(name + instance.optionSuffix);
        }
    }

    /**
     * Prints a "Can't access" message for one or two argument variable names
     * from an {@code ArgOption} object.
     * 
     * @param option the {@code ArgOption} object
     */
    public static void cantAccessArg (ArgOption option) {
        String name = camelCase(option.getName());
        String altName = camelCase(option.getAltName());
        String argName = camelCase(option.getArgName());
        if (altName != null) {
            cantAccess(camelCase(name, argName), camelCase(altName, argName));
        } else {
            cantAccess(camelCase(name, argName));
        }
    }
    
    /**
     * Prints a "Can't access" message for the field name of an {@code ArgOperand}
     * object.
     * 
     * @param operand the {@code ArgOperand} with a field name
     */
    public static void cantAccess (ArgOperand operand) {
        String fieldName = operand.getFieldName();
        cantAccess(fieldName);
    }

    /**
     * Prints a "Can't access" message for two versions of a variable name.
     * 
     * @param name the name of the variable
     * @param alt the alternate name of the variable
     */
    public static void cantAccess (String name, String alt) {
        printError("Can't access either \"" + name + "\" or \"" + alt + "\"");
    }

    /**
     * Prints a "Can't access" message for a particular variable name.
     * 
     * @param name the name of the variable
     */
    public static void cantAccess (String name) {
        printError("Can't access \"" + name + "\"");
    }
    
    /**
     * Prints the program argument Strings inside a set of quotes.
     * 
     * @param args the argument array of Strings
     */
    public static void printArgs (String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("    \"");
        for (String s : args) {
            sb.append(s);
            sb.append(' ');
        }
        sb.setLength(sb.length() - 1);
        sb.append("\"");
        System.err.println(sb);
    }
    
    /**
     * Prints an error message with usage text.
     * 
     * @param text the error message
     */
    public static void printError (String text) {
    	ArgHelp help = base.getHelp();
        help.printUsage();
        System.err.printf("%s: %s%n", instance.programName, text);
    }
    
}
