/**
 *+
 *  ArgFace.java
 *	1.0.0	Apr 18, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.List;

/**
 * Arguments Interface.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 */
public interface ArgFace {
    
    /**
     * Parses the command line arguments according to the "usage" text.
     * 
     * @param args the command line arguments
     * @return the argument index or a negative value on error
     */
    public int parse (String [] args);
    
    /**
     * Returns {@code true} if the command line has the named argument.
     * <p>
     * Option names may be specified with one or two leading dashes. (-x,
     * --opt-name) Operand names, even literal operands, may be specified between
     * angle brackets. (&lt;literal-name&gt;, &lt;variable-name&gt;) An argument
     * name without these special characters is also acceptable.
     * 
     * @param name the argument name
     * @return {@code true} if the command line has the argument
     */
    public boolean has (String name);
    
    /**
     * Returns the {@code String} value of the named argument.
     * <p>
     * Option names may be specified with one or two leading dashes. (-x,
     * --opt-name) Operand names, even literal operands, may be specified between
     * angle brackets. (&lt;literal-name&gt;, &lt;variable-name&gt;) An argument
     * name without these special characters is also acceptable.
     * 
     * @param name the argument name
     * @return the {@code String} value
     */
    public String value (String name);
    
    /**
     * Returns the {@code String} array value of the named argument.
     * <p>
     * Option names may be specified with one or two leading dashes. (-x,
     * --opt-name) Operand names, even literal operands, may be specified between
     * angle brackets. (&lt;literal-name&gt;, &lt;variable-name&gt;) An argument
     * name without these special characters is also acceptable.
     * 
     * @param name the argument name
     * @return the {@code String []} value
     */
    public String [] valueArray (String name);
    
    /**
     * Returns the {@code String} list value of the named argument.
     * <p>
     * Option names may be specified with one or two leading dashes. (-x,
     * --opt-name) Operand names, even literal operands, may be specified between
     * angle brackets. (&lt;literal-name&gt;, &lt;variable-name&gt;) An argument
     * name without these special characters is also acceptable.
     * 
     * @param name the argument name
     * @return the {@code List<String>} value
     */
    public List<String> valueList (String name);

    /**
     * Returns the {@code String} array value of the argument operands.
     * 
     * @return the {@code String[]} value
     */
    public String [] operandArray ();
    
    /**
     * Returns the {@code String} list value of the argument operands.
     * 
     * @return the {@code List<String>} value
     */
    public List<String> operandList ();

    /**
     * Returns the number of times the named argument is specified on the command line.
     * <p>
     * Option names may be specified with one or two leading dashes. (-x,
     * --opt-name) Operand names, even literal operands, may be specified between
     * angle brackets. (&lt;literal-name&gt;, &lt;variable-name&gt;) An argument
     * name without these special characters is also acceptable.
     * 
     * @param name the argument name
     * @return the count
     */
    public int count (String name);
    
    /**
     * Sets the "usage" text from a String.
     * 
     * @param usageText the usage text
     */
    public void setUsageText (String usageText);
    
    /**
     * Sets the "usage" text from an array of Strings.
     * 
     * @param usageText the usage text
     */
    public void setUsageText (String[] usageText);
    
    /**
     * Sets the "version" text from a String. The version text is output when
     * the --version option is specified.
     * 
     * @param versionText the version text
     */
    public void setVersionText (String versionText);

    /**
     * Sets the "version" text from an array of Strings. The version text is
     * output when the --version option is specified.
     * <p>
     * The individual Strings of the array will be merged together to form a
     * single String with a newline terminator after each element.
     * 
     * @param versionText the version text
     */
    public void setVersionText (String [] versionText);

    /**
     * Sets the "about" text from a String. The about text is output when the
     * --about option is specified.
     * 
     * @param aboutText the about text
     */
    public void setAboutText (String aboutText);

    /**
     * Sets the "about" text from an array of Strings. The about text is output
     * when the --about option is specified.
     * <p>
     * The individual Strings of the array will be merged together to form a
     * single String with a newline terminator after each element.
     * 
     * @param aboutText the about text
     */
    public void setAboutText (String [] aboutText);

    /**
     * Sets the "help" text from a String. The help text appears after the
     * output from the --help option. Use this text to provide
     * clarifications and to explain oddities that may exist in the program.
     * 
     * @param helpText the help text
     */
    public void setHelpText (String helpText);

    /**
     * Sets the "help" text from an array of Strings. The help text appears
     * after the output from the --help option. Use this text to provide
     * clarifications and to explain oddities that may exist in the program.
     * <p>
     * The individual Strings of the array will be merged together to form a
     * single String with a newline terminator after each element.
     * 
     * @param helpText the help text
     */
    public void setHelpText (String [] helpText);

    /**
     * Sets the operand suffix.
     * 
     * @param operandSuffix the operand suffix
     */
    public void setOperandSuffix (String operandSuffix);
    
    /**
     * Sets the option suffix.
     * 
     * @param optionSuffix the option suffix
     */
    public void setOptionSuffix (String optionSuffix);
    
    /**
     * Sets the "allowOverwrite" operating mode.
     * 
     * @param allowOverwrite {@code true} enables overwrite mode
     */
    public void setAllowOverwrite (boolean allowOverwrite);
    
    /**
     * Sets the "suppressHelp" operating mode.
     * 
     * @param suppressHelp {@code true} enables help suppression mode
     */
    public void setSuppressHelp (boolean suppressHelp);
    
    /**
     * Sets the "posixFormat" operating mode.
     * 
     * @param posixFormat {@code true} enables posix format mode
     */
    public void setPosixFormat (boolean posixFormat);
    
    /**
     * Sets the "sortOptions" feature.
     * 
     * @param sortOptions {@code true} enables the sort options feature
     */
    public void setSortOptions (boolean sortOptions);
    
    /**
     * Sets the "patternWatch" feature.
     * 
     * @param patternWatch {@code true} enables the pattern watch feature
     */
    public void setPatternWatch (boolean patternWatch);
    
    /**
     * Returns the matching pattern text following successful argument parsing.
     * 
     * @return the matching pattern text
     */
    public String getPatternMatch ();
    
    /**
     * Prints the "usage" text. This is a program request and is not affected
     * by the "suppressHelp" setting.
     */
    public void printUsage ();
    
    /**
     * Prints the "help" text. This is a program request and is not affected
     * by the "suppressHelp" setting.
     */
    public void printHelp ();
    
    /**
     * Reports the results of command line parsing.
     * The selection specifies what the report should show.
     * 
     * @param selection indicates the desired output
     */
    public void report (String selection);
    
}
