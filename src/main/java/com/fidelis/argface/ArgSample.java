/**
 *+
 *  ArgSample.java
 *	1.0.0	Apr 22, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * ArgFace Sample Application.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgSample {
   
    private final String[] usageText = {

         "ArgSample",
             "set [ -x, --extend 'help description for x' | -y, --yield | -z, --sleep ]",
             "[-e] [[upper | lower | any] <pattern>] <other>",
                
        "ArgSample",
            "((match [upper | lower | any] <other>) |",
            " (ending <file>) |",
            " (<other> match <pattern>))",
            "[set | clear]",
            
        "ArgSample",
            "[-a, --after  <lines>] 'number of lines after a match'",
            "[-b, --before <lines>] 'number of lines before a match'",
            "[-cde]",
            "match|starting|ending|<other> <pattern> <file>...",
       
        "Options:",
            "-c confirm actions",
            "-d delete matching line and by the way, if this runs too long, " +
                "and continues for a long time, where will all this text go ?",
            "-e, --exit exit on error",
            "-y, --yield yeild to substitutions",
            "-z, --sleep dormant state when completed"
    };
    private final String[]  helpText = {
        "Demonstrates the \"ArgFace\" command line interface.",
        "All output is based on the command line arguments.",
        "No files were harmed during the running of this program."
    };
    private final String    versionText = "ArgSample version 1.0.0";
    private final String[]  aboutText   = {
            versionText,
            "  Developed to demonstrate the \"ArgFace\" Command Line Interface.",
            "  2014-04-23",
            "  Leo Hinterlang -- leohinterlang@gmail.com",
            "  Fidelis Software Technologies, Butler, PA" 
    };
 
    private final boolean   allowOverwrite  = false;
    private final boolean   suppressHelp    = false;
    private final boolean   posixFormat     = false;
    private final boolean   sortOptions     = true;

    private final String    optionSuffix    = null;
    private final String    operandSuffix   = null;

    private boolean         afterOption;
    private boolean         beforeOption;
    private String          afterLines;
    private String          beforeLines;

    private boolean         cOption;
    private boolean         dOption;
    private boolean         eOption;
    
    private boolean         xOption;
    private boolean         yOption;
    private boolean         zOption;

    private boolean         matchOperand;
    private boolean         startingOperand;
    private boolean         endingOperand;
    private String          otherOperand;
    private String          patternOperand;
    private String []       fileOperand;

    private boolean         upperOperand;
    private boolean         lowerOperand;
    private boolean         anyOperand;
    private boolean         setOperand;
    private boolean         clearOperand;

    private boolean         helpOption;
    private boolean         versionOption;
    private boolean         aboutOption;

    private String []       operands;

    private boolean         skipBlank;
    private boolean         show;
    private ArgFace         argFace;

    /**
     * ArgFace Sample Program.
     * 
     * @param args the command line arguments
     */
    public static void main (String [] args) {
        ArgSample sample = new ArgSample();
        sample.execute(args);
        sample.otherTests();
    }
    
    private void execute (String [] args) {
        
        boolean verbose = true;
        
        Debug.setTrace(verbose);
        Debug.setVerbose(verbose);
        show = verbose;
        skipBlank = true;
        
        argFace = ArgStandard.create(usageText, this);
        if (argFace == null) {
            System.exit(1);
        }
        
        argFace.setPatternWatch(true);
        
        if (argFace instanceof ArgProcedure) {
            argFace.setAboutText(aboutText);
            argFace.setVersionText(versionText);
            argFace.setHelpText(helpText);

            argFace.setAllowOverwrite(allowOverwrite);
            argFace.setSuppressHelp(suppressHelp);
            argFace.setPosixFormat(posixFormat);
            argFace.setSortOptions(sortOptions);
        }
        
        int nArg = argFace.parse(args);
        if (nArg < 0) {
            System.out.println("Error exit");
            System.exit(1);
        }
        argFace.printHelp();
        if (argFace instanceof ArgProcedure) {
            setArgumentVariables(argFace);
        }
        showResults(nArg, args);
    }
    
    private void otherTests () {
        String [] testCommandLines = {

                "--about",
                "about",
                
                "--version",
                "version",
                
                "--help",
                "help",
                
                "-a 5 -b 5 ending public file.java file2 file3",
                "after=5 before=5 ending pattern=public file=[file.java,file2,file3",
                
                "-cd total patternValue file.java another.file yetAnother.file " +
                    "more more2 more3 more4 more5 more6",
                "-c -d <other>=total <pattern>=patternValue <file>=[file.java,another.file," +
                    "yetAnother.file,more,more2,more3,more4,more5,more6",
                    
                "begins xxx yyy",
                "other=begins pattern=xxx file=yyy",
                
                "-c -d aaa bbb ccc -w",
                "fail",
                
                "--exit --d --c starting Leo myfile",
                "-exit --d --c <starting> <pattern>=Leo <file>=myfile",
                
                "-after=7 -before:5 match xyzzy cave",
                "after=7 before=5 match pattern=xyzzy file=cave",
                
                "yet possible -c -b: 12 thefile",
                "other=yet pattern=possible c b=12 file=thefile",
                
                "match name=value file.txt",
                "match pattern=name=value file=file.txt",
                
                "-ccc ending pat f1 f2 f3",
                "-c=:3 ending pattern=pat file=[f1,f2,f3",
                
                "ending -dd pat -d f1 f2 -d f3",
                "-d=:4 ending pattern=pat file=[f1,f2,f3",
                
                "match xoxo",
                "match other=xoxo",
                
                "ending 999",
                "ending file=999",
                
                "-a 7 ending no-options",
                "fail",
                
                "-a 7 ending with-file filename",
                "a=7 ending pattern=with-file file=filename",
                
                "ending",
                "fail",
                
                "match",
                "fail",
                
                "xxx match yyy",
                "other=xxx match pattern=yyy",
                
                "match upper NAME",
                "match upper other=NAME",

                "match Size",
                "match other=Size",
                
                "match lower name",
                "match lower other=name",
                
                "match any other",
                "match any other=other",
                
                "ending file",
                "ending file=file",
                
                "other match pattern",
                "other=other match pattern=pattern",
                
                "other match pattern clear",
                "other match pattern=pattern clear",
                
                "ending file set",
                "ending file=file set",
                
                "match any other clear",
                "match any other=other clear",
                
                "match any other",
                "match any other=other",
                
                "match other clear",
                "match other=other clear",
                
                "match other",
                "match other=other",
                
                "set any pattern other",
                "set any pattern=pattern other=other",
                
                "set other",
                "set other=other",
                
                "set -e pattern other",
                "set -e pattern=pattern other=other",
                
                "set -x -e pattern -y other",
                "fail",
                
                "set -z -- -x filename",
                "set -z pattern=-x other=filename"
                
        };
        for (int n = 0; n < testCommandLines.length; n++) {
            String commandLine = testCommandLines[n++];
            String valueCheck =  testCommandLines[n];
            if (testCommandLine(commandLine)) {
                checkValues(valueCheck);
            } else {
                if (! valueCheck.equals("fail")) {
                    System.out.println("*** ERROR *** Should not have failed");
                }
            }
        } 
    }
    
    private boolean testCommandLine (String commandLine) {
        clearArgumentVariables();
        System.out.println("\n" + commandLine);
        Debug.trace("=========================");
        String args[] = commandLine.split("\\s");
        int nArg = argFace.parse(args);
        if (nArg < 0) {
            System.out.println("Error: nArg = " + nArg);
            if (show) {
                showResults(0, args);
            }
            return false;
        }
        if (argFace instanceof ArgProcedure) {
            setArgumentVariables(argFace);
        }
        if (show) {
            showResults(nArg, args);
        }
        return true;
    }
    
    private void checkValues (String valueCheck) {
        if (valueCheck.equals("fail")) {
            System.out.println("*** ERROR *** expected FAIL didn't happen");
            return;
        }
        String [] tokens = valueCheck.split("\\s");
        for (String tok : tokens) {
            checkValue(tok);
        }
    }
    
    private void checkValue (String tok) {
        String [] nvs = tok.split("=", 2);
        String name = nvs[0];
        String value = null;
        if (nvs.length == 2) {
            value = nvs[1];
        }
        if (! argFace.has(name)) {
            System.out.printf("*** ERROR ***  %s fails \"has\", should be true%n", name);
        }
        if (! checkTrue(name)) {
            System.out.printf("*** ERROR ***  %s program variable should be set%n", name);
        }
        if (value != null) {
            if (value.startsWith(":")) {
                value = value.substring(1);
                int valCount = intPositive(value);
                int count = argFace.count(name);
                if (valCount != count) {
                    System.out.printf("*** ERROR ***  %s count expected %d is %d%n",
                            name, valCount, count);
                }
                return;
            } else if (value.startsWith("[")) {
                value = value.substring(1);
                String[] values = value.split(",");
                String[] vals = argFace.valueArray(name);
                for (int n = 0; n < vals.length; n++) {
                    value = values[n];
                    if (! value.equals(vals[n])) {
                        System.out.printf("*** ERROR ***  " +
                                "%s value expected \"%s\" is \"%s\"%n,",
                                name, value, vals[n]);
                    }
                }
            }
            String val = argFace.value(name);
            if (! value.equals(val)) {
                System.out.printf("*** ERROR ***  %s value expected \"%s\" is \"%s\"%n",
                        name, value, val);
            }
        }
    }
    
    private boolean checkTrue (String name) {
        if (name.startsWith("-")) {
            name = name.substring(1);
            if (name.startsWith("-")) {
                name = name.substring(1);
            }
        } else if (name.startsWith("<")) {
            name = name.substring(1);
            if (name.endsWith(">")) {
                name = name.substring(0, name.length() - 1);
            }
        }
        if (name.equals("about"))   return aboutOption;
        if (name.equals("version")) return versionOption;
        if (name.equals("help"))    return helpOption;
        
        if (name.equals("after"))   return afterOption;
        if (name.equals("before"))  return beforeOption;
        if (name.equals("a"))       return afterOption;
        if (name.equals("b"))       return beforeOption;
        if (name.equals("c"))       return cOption;
        if (name.equals("d"))       return dOption;
        if (name.equals("e"))       return eOption;
        if (name.equals("exit"))    return eOption;
        
        if (name.equals("match"))   return matchOperand;
        if (name.equals("starting")) return startingOperand;
        if (name.equals("ending"))  return endingOperand;
        if (name.equals("upper"))   return upperOperand;
        if (name.equals("lower"))   return lowerOperand;
        if (name.equals("any"))     return anyOperand;
        if (name.equals("set"))     return setOperand;
        if (name.equals("clear"))   return clearOperand;
        
        if (name.equals("other"))   return otherOperand != null;
        if (name.equals("pattern")) return patternOperand != null;
        if (name.equals("file"))    return fileOperand != null;
        if (name.equals("x"))       return xOption;
        if (name.equals("y"))       return yOption;
        if (name.equals("z"))       return zOption;
        Debug.trace("unknown name: " + name);
        return false;
    }
    
    private void showResults (int nArg, String[] args) {
       
        int before = 0;
        int after = 0;
        System.out.println("nArg = " + nArg);
        if (beforeOption) {
            before = intPositive(beforeLines);
        }
        if (afterOption) {
            after = intPositive(afterLines);
        }
        while (nArg < args.length) {
            String arg = args[nArg++];
            System.out.println("sample arg: " + arg);
        }
        if (operands != null) {
            for (String arg : operands) {
                System.out.println("sample operand: " + arg);
            }
        }

        outBoolean("afterOption", afterOption);
        outString("afterLines", afterLines);
        outInteger("after", after, afterOption);
        outBoolean("beforeOption", beforeOption);
        outString("beforeLines", beforeLines);
        outInteger("before", before, beforeOption);
        outBoolean("cOption", cOption);
        outBoolean("dOption", dOption);
        outBoolean("eOption", eOption);
        
        outBoolean("xOption", xOption);
        outBoolean("yOption", yOption);
        outBoolean("zOption", zOption);
   
        outBoolean("matchOperand", matchOperand);
        outBoolean("startingOperand", startingOperand);
        outBoolean("endingOperand", endingOperand);
        
        outBoolean("upperOperand", upperOperand);
        outBoolean("lowerOperand", lowerOperand);
        outBoolean("anyOperand", anyOperand);
        
        outBoolean("setOperand", setOperand);
        outBoolean("clearOperand", clearOperand);
        
        outString("otherOperand", otherOperand);
        outString("patternOperand", patternOperand);
        outArray("fileOperand", fileOperand);
        
        outBoolean("helpOption", helpOption);
        outBoolean("versionOption", versionOption);
        outBoolean("aboutOption", aboutOption);
    }
    
    private void setArgumentVariables (ArgFace argFace) {
        beforeOption =  argFace.has("-before");
        beforeLines =   argFace.value("-before");
        afterOption =   argFace.has("-after");
        afterLines =    argFace.value("-after");
        cOption =       argFace.has("-c");
        dOption =       argFace.has("-d");
        eOption =       argFace.has("-e");
        matchOperand =  argFace.has("<match>");
        startingOperand = argFace.has("<starting>");
        endingOperand = argFace.has("<ending>");
        otherOperand =  argFace.value("<other>");
        patternOperand = argFace.value("<pattern>");
        fileOperand =   argFace.valueArray("<file>");
        helpOption =    argFace.has("-help");
        versionOption = argFace.has("-version");
        aboutOption =   argFace.has("-about");
        operands =      argFace.operandArray();
        upperOperand =  argFace.has("<upper>");
        lowerOperand =  argFace.has("<lower>");
        anyOperand =    argFace.has("<any>");
        setOperand =    argFace.has("<set>");
        clearOperand =  argFace.has("<clear>");
        xOption =       argFace.has("-x");
        yOption =       argFace.has("-y");
        zOption =       argFace.has("-z");
    }
    
    private void clearArgumentVariables () {
        beforeOption =  false;
        beforeLines =   null;
        afterOption =   false;
        afterLines =    null;
        cOption =       false;
        dOption =       false;
        eOption =       false;
        matchOperand =  false;
        startingOperand=false;
        endingOperand = false;
        otherOperand =  null;
        patternOperand= null;
        fileOperand =   null;
        helpOption =    false;
        versionOption = false;
        aboutOption =   false;
        operands =      null;
        upperOperand =  false;
        lowerOperand =  false;
        anyOperand =    false;
        setOperand =    false;
        clearOperand =  false;
        xOption =       false;
        yOption =       false;
        zOption =       false;
    }
    
    private int intPositive (String number) {
        int value = intValue(number);
        if (value < 0) {
            System.out.println("Invalid positive integer: " + number);
            value = 0;
        }
        return value;
    }
    
    private int intValue (String number) {
        int value = 0;
        try {
            value = Integer.valueOf(number);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid integer: " + number);
        }
        return value;
    }
    
    private void outBoolean(String name, boolean value) {
        if (skipBlank) {
            if (value == false) {
                return;
            }
        }
        String x = name;
        if (x.endsWith("Option")) {
            x = x.substring(0, x.length() - 6);
        } else if (x.endsWith("Operand")) {
            x = x.substring(0, x.length() - 7);
        }
        int n = argFace.count(x);
        System.out.printf("%d %15.15s: %b%n", n, name, value);
    }
    
    private void outString(String name, String value) {
        if (skipBlank) {
            if (value == null) {
                return;
            }
        }
        String x = name;
        if (x.endsWith("Operand")) {
            x = x.substring(0, x.length() - 7);
        }
        int n = argFace.count(x);
        System.out.printf("%d %15.15s: %s%n", n, name, value);
    }
    
    private void outInteger (String name, int value, boolean show) {
        if (show) {
            System.out.printf("  %15.15s: %d%n", name, value);
        }
    }
    
    private void outArray (String name, String [] array) {
        if (array == null) {
            return;
        }
        for (String value : array) {
            outString(name, value);
        }
    }

    /**
     * @return the afterOption
     */
     public boolean isAfterOption () {
        return afterOption;
    }

    /**
     * @param afterOption the afterOption to set
     */
    public void setAfterOption (boolean afterOption) {
        this.afterOption = afterOption;
    }

    /**
     * @return the beforeOption
     */
    public boolean isBeforeOption () {
        return beforeOption;
    }

    /**
     * @param beforeOption the beforeOption to set
     */
    public void setBeforeOption (boolean beforeOption) {
        this.beforeOption = beforeOption;
    }

    /**
     * @return the afterLines
     */
    public String getAfterLines () {
        return afterLines;
    }

    /**
     * @param afterLines the afterLines to set
     */
    public void setAfterLines (String afterLines) {
        this.afterLines = afterLines;
    }

    /**
     * @return the beforeLines
     */
    public String getBeforeLines () {
        return beforeLines;
    }

    /**
     * @param beforeLines the beforeLines to set
     */
    public void setBeforeLines (String beforeLines) {
        this.beforeLines = beforeLines;
    }

    /**
     * @return the cOption
     */
    public boolean iscOption () {
        return cOption;
    }

    /**
     * @param cOption the cOption to set
     */
    public void setcOption (boolean cOption) {
        this.cOption = cOption;
    }

    /**
     * @return the dOption
     */
    public boolean isdOption () {
        return dOption;
    }

    /**
     * @param dOption the dOption to set
     */
    public void setdOption (boolean dOption) {
        this.dOption = dOption;
    }

    /**
     * @return the eOption
     */
    public boolean iseOption () {
        return eOption;
    }

    /**
     * @param eOption the eOption to set
     */
    public void seteOption (boolean eOption) {
        this.eOption = eOption;
    }

    /**
     * @return the matchOperand
     */
    public boolean isMatchOperand () {
        return matchOperand;
    }

    /**
     * @param matchOperand the matchOperand to set
     */
    public void setMatchOperand (boolean matchOperand) {
        this.matchOperand = matchOperand;
    }

    /**
     * @return the startingOperand
     */
    public boolean isStartingOperand () {
        return startingOperand;
    }

    /**
     * @param startingOperand the startingOperand to set
     */
    public void setStartingOperand (boolean startingOperand) {
        this.startingOperand = startingOperand;
    }

    /**
     * @return the endingOperand
     */
    public boolean isEndingOperand () {
        return endingOperand;
    }

    /**
     * @param endingOperand the endingOperand to set
     */
    public void setEndingOperand (boolean endingOperand) {
        this.endingOperand = endingOperand;
    }

    /**
     * @return the otherOperand
     */
    public String getOtherOperand () {
        return otherOperand;
    }

    /**
     * @param otherOperand the otherOperand to set
     */
    public void setOtherOperand (String otherOperand) {
        this.otherOperand = otherOperand;
    }

    /**
     * @return the patternOperand
     */
    public String getPatternOperand () {
        return patternOperand;
    }

    /**
     * @param patternOperand the patternOperand to set
     */
    public void setPatternOperand (String patternOperand) {
        this.patternOperand = patternOperand;
    }

    /**
     * @return the fileOperand
     */
    public String [] getFileOperand () {
        return fileOperand;
    }

    /**
     * @param fileOperand the fileOperand to set
     */
    public void setFileOperand (String [] fileOperand) {
        this.fileOperand = fileOperand;
    }

    /**
     * @return the operands
     */
    public String [] getOperands () {
        return operands;
    }

    /**
     * @param operands the operands to set
     */
    public void setOperands (String [] operands) {
        this.operands = operands;
    }

    /**
     * @return the helpOption
     */
    public boolean isHelpOption () {
        return helpOption;
    }

    /**
     * @param helpOption the helpOption to set
     */
    public void setHelpOption (boolean helpOption) {
        this.helpOption = helpOption;
    }

    /**
     * @return the versionOption
     */
    public boolean isVersionOption () {
        return versionOption;
    }

    /**
     * @param versionOption the versionOption to set
     */
    public void setVersionOption (boolean versionOption) {
        this.versionOption = versionOption;
    }

    /**
     * @return the aboutOption
     */
    public boolean isAboutOption () {
        return aboutOption;
    }

    /**
     * @param aboutOption the aboutOption to set
     */
    public void setAboutOption (boolean aboutOption) {
        this.aboutOption = aboutOption;
    }

    /**
     * @return the upperOperand
     */
    public boolean isUpperOperand () {
        return upperOperand;
    }

    /**
     * @param upperOperand the upperOperand to set
     */
    public void setUpperOperand (boolean upperOperand) {
        this.upperOperand = upperOperand;
    }

    /**
     * @return the lowerOperand
     */
    public boolean isLowerOperand () {
        return lowerOperand;
    }

    /**
     * @param lowerOperand the lowerOperand to set
     */
    public void setLowerOperand (boolean lowerOperand) {
        this.lowerOperand = lowerOperand;
    }

    /**
     * @return the anyOperand
     */
    public boolean isAnyOperand () {
        return anyOperand;
    }

    /**
     * @param anyOperand the anyOperand to set
     */
    public void setAnyOperand (boolean anyOperand) {
        this.anyOperand = anyOperand;
    }

    /**
     * @return the setOperand
     */
    public boolean isSetOperand () {
        return setOperand;
    }

    /**
     * @param setOperand the setOperand to set
     */
    public void setSetOperand (boolean setOperand) {
        this.setOperand = setOperand;
    }

    /**
     * @return the clearOperand
     */
    public boolean isClearOperand () {
        return clearOperand;
    }

    /**
     * @param clearOperand the clearOperand to set
     */
    public void setClearOperand (boolean clearOperand) {
        this.clearOperand = clearOperand;
    }

    /**
     * @return the usageText
     */
    public String [] getUsageText () {
        return usageText;
    }

    /**
     * @return the helpText
     */
    public String [] getHelpText () {
        return helpText;
    }

    /**
     * @return the versionText
     */
    public String getVersionText () {
        return versionText;
    }

    /**
     * @return the aboutText
     */
    public String [] getAboutText () {
        return aboutText;
    }

    /**
     * @return the allowOverwrite
     */
    public boolean isAllowOverwrite () {
        return allowOverwrite;
    }

    /**
     * @return the suppressHelp
     */
    public boolean isSuppressHelp () {
        return suppressHelp;
    }

    /**
     * @return the posixFormat
     */
    public boolean isPosixFormat () {
        return posixFormat;
    }

    /**
     * @return the sortOptions
     */
    public boolean isSortOptions () {
        return sortOptions;
    }

    /**
     * @return the optionSuffix
     */
    public String getOptionSuffix () {
        return optionSuffix;
    }

    /**
     * @return the operandSuffix
     */
    public String getOperandSuffix () {
        return operandSuffix;
    }

    /**
     * @return the xOption
     */
    public boolean isxOption () {
        return xOption;
    }

    /**
     * @param xOption the xOption to set
     */
    public void setxOption (boolean xOption) {
        this.xOption = xOption;
    }

    /**
     * @return the yOption
     */
    public boolean isyOption () {
        return yOption;
    }

    /**
     * @param yOption the yOption to set
     */
    public void setyOption (boolean yOption) {
        this.yOption = yOption;
    }

    /**
     * @return the zOption
     */
    public boolean iszOption () {
        return zOption;
    }

    /**
     * @param zOption the zOption to set
     */
    public void setzOption (boolean zOption) {
        this.zOption = zOption;
    }

}
