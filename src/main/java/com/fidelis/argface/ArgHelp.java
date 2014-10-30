/**
 *+
 *  ArgumentsHelp.java
 *	1.0.0	Mar 18, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.ArrayList;
import java.util.List;

/**
 * ArgFace Help Class.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgHelp {
    private ArgList         argList;
    private List<ArgOption> optionList;
    private boolean         suppressHelp;
    private String          usageText;
    private String          versionText;
    private String          aboutText;
    private String          helpText;
    private List<ArgNode>   problemUsage = new ArrayList<ArgNode>();
    private List<String>    problemText  = new ArrayList<String>();
    
    /**
     * Sets the argument list.
     * 
     * @param argList the argument list
     */
    public void setArgList (ArgList argList) {
        this.argList = argList;
    }

    /**
     * Sets the option list.
     * 
     * @param optionList the option list
     */
    public void setOptionList (List<ArgOption> optionList) {
        this.optionList = optionList;
    }

    /**
     * Sets the "suppressHelp" operating mode.
     * 
     * @param suppressHelp {@code true} to suppress help
     */
    public void setSuppressHelp (boolean suppressHelp) {
        this.suppressHelp = suppressHelp;
    }

    /**
     * Sets the "version" text.
     * 
     * @param versionText the version text
     */
    public void setVersionText (String versionText) {
        this.versionText = versionText;
    }

    /**
     * Sets the "about" text.
     * 
     * @param aboutText the aboutText to set
     */
    public void setAboutText (String aboutText) {
        this.aboutText = aboutText;
    }

    /**
     * Sets the "help" text.
     * 
     * @param helpText the help text
     */
    public void setHelpText (String helpText) {
        this.helpText = helpText;
    }
    
    /**
     * Prints the "usage" text.
     * 
     * @return {@code true} if the usage text was printed
     */
    public boolean printUsage () {
        if (suppressHelp) {
            return false;
        }
        return outputUsage();
    }
    
    /**
     * Outputs the "usage" text.
     * 
     * @return {@code true} if the usage text was output
     */
    public boolean outputUsage () {
        if (usageText == null) {
            usageText = buildUsageText();
        }
        if (usageText == null) {
            return false;
        }
        System.out.println(usageText);
        return true;
    }
    
    /**
     * Prints the "help" text.
     * 
     * @return {@code true} if the help text was printed
     */
    public boolean printHelp () {
        if (suppressHelp) {
            return false;
        }
        return outputHelp();
    }
    
    /**
     * Outputs the "help" text.
     * 
     * @return {@code true} if the help text was output
     */
    public boolean outputHelp () {
        int maxNameLength = 0;
        int maxAltLength = 0;
        int maxArgLength = 0;
        int nameLength = 0;
        int altLength = 0;
        int argLength = 0;
        for (ArgOption opt : optionList) {
            String name = opt.getName();
            String altName = opt.getAltName();
            String argName = opt.getArgName();
            nameLength = name.length();
            if (nameLength > 1) {
                ++nameLength;
                if (altName == null) {
                    altName = name;
                    nameLength = 1;
                }
            }
            ++nameLength;
            if (nameLength > maxNameLength) {
                maxNameLength = nameLength;
            }
            if (altName != null) {
                altLength = altName.length() + 5;
                if (altLength > maxAltLength) {
                    maxAltLength = altLength;
                }
            }
            if (argName != null) {
                argLength = argName.length() + 2;
                if (argLength > maxArgLength) {
                    maxArgLength = argLength;
                }
            }
        }
        String nameFormat = maxNameLength == 0 ? "%s" : "%" + maxNameLength + "s";
        String altFormat =  maxAltLength == 0 ? "%s" : "%-" + maxAltLength + "s";
        String argFormat = maxArgLength == 0 ? "%s" : "%-" + maxArgLength + "s";
        outputUsage();
        System.out.println("Options:");
        for (ArgOption opt : optionList) {
            String name = opt.getName();
            String altName = opt.getAltName();
            String argName = opt.getArgName();
            String nDash = "-";
            String comma = ", ";
            if (name.length() > 1) {
                nDash = "--";
                if (altName == null) {
                    altName = name;
                    name = "";
                    nDash = "";
                    comma = "  ";
                }
            }
            StringBuilder sb = new StringBuilder("  ");
            sb.append(String.format(nameFormat, nDash + name));
            if (altName != null) {
                String aDash = altName.length() == 1 ? "-" : "--";
                sb.append(String.format(altFormat, comma + aDash + altName));
            } else {
                sb.append(String.format(altFormat, " "));
            }
            if (argName != null) {
                String arg = opt.isArgOptional() ? "[" + argName + "]"
                                                 : "<" + argName + ">";
                sb.append(String.format(argFormat, arg));
            } else {
                sb.append(String.format(argFormat, " "));
            }
            String help = opt.getHelp();
            if (help != null) {
                String [] parts = help.split("\\s");
                int slen = 0;
                int position = sb.length();
                for (String s : parts) {
                    if ((sb.length() - slen + s.length()) > 80) {
                        sb.append('\n');
                        slen = sb.length();
                        for (int n = 0; n <= position; n++) {
                            sb.append(' ');
                        }
                    } else {
                        sb.append(' ');
                    }
                    sb.append(s);
                }
            }
            System.out.println(sb);
        }
        if (helpText != null) {
            System.out.println();
            System.out.println(helpText);
        }
        return true;
    }
    
    /**
     * Prints the "version" text.
     * 
     * @return {@code true} if the version text was printed
     */
    public boolean printVersion () {
        if (suppressHelp) {
            return false;
        }
        return outputVersion();
    }
    
    /**
     * Outputs the "version" text if there is any.
     * 
     * @return {@code true} if the version text was output
     */
    public boolean outputVersion () {
        if (versionText == null) {
            return false;
        }
        System.out.println(versionText);
        return true;
    }
    
    /**
     * Prints the "about" text.
     * 
     * @return {@code true} if the about text was printed
     */
    public boolean printAbout () {
        if (suppressHelp) {
            return false;
        }
        return outputAbout();
    }

    /**
     * Outputs the "about" text if there is any.
     * 
     * @return {@code true} if the about text was output
     */
    public boolean outputAbout () {
        if (aboutText == null) {
            return false;
        }
        System.out.println(aboutText);
        return true;
    }
    
    public void initProblems () {
        problemUsage.clear();
        problemText.clear();
    }
    
    public void addProblem (ArgNode base, String text) {
        problemUsage.add(base);
        problemText.add(text);
    }
    
    public void printProblems () {
        int size = problemUsage.size();
        if (size == 0) {
            System.out.println("No help for this problem");
            return;
        }
        if (size == 1) {
            System.out.println("Possible problem:");
        } else {
            System.out.println("Possible problems:");
        }
        for (int n = 0; n < size; n++) {
            ArgNode base = problemUsage.get(n);
            String text = problemText.get(n);
            String usageSpec = argList.buildUsageSpec(base);
            System.out.println("  " + usageSpec);
            System.out.println("  " + text);
            System.out.println();
        }
    }
    
    private String buildUsageText () {
        if (argList != null) {
            return argList.buildUsage();
        }
        return null;
    }
    
}
