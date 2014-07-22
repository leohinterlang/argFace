/**
 *+
 *  ArgPosix.java
 *	1.0.0	May 23, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * ArgFace Posix Sample Application.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgPosix {
    
    private final String versionText = "ArgPosix version 1.0.0";
    
    private final String[] aboutText = {
        versionText,
        "  ArgFace Posix Sample Application",
        "  2014-05-23",
        "  Leo Hinterlang -- leohinterlang@gmail.com",
        "  Fidelis Software Technologies  Butler, PA"
    };
    
    private final String [] usageText = {
        "Usage: ArgPosix ([options] find <pattern> <file>...)",
            "| (copy <pattern> <file>... <outfile>)",
        "Options:",
            "-c, --copy allow copies",
            "-a, --all [size] process all files",
            "--binary binary mode"
    };
    
    private final boolean   posixFormat   = true;
    private final boolean   sortOptions   = true;

    private final String    optionSuffix  = "Opt";
    private final String    operandSuffix = "";

    private boolean         aOpt;
    private String          aSize;
    private boolean         binaryOpt;
    private boolean         cOpt;
    
    private boolean         find;
    private boolean         copy;
    private String          pattern;
    private String []       file;
    private String          outfile;
   
    /**
     * The main method.
     * 
     * @param args command line arguments
     */
    public static void main (String [] args) {
        Debug.setTrace(false);
        ArgPosix program = new ArgPosix();
        program.execute(args);
    }
    
    private void execute (String [] args) {
        Debug.setTrace(true);
        ArgFace argFace = ArgProcedure.create(usageText, this);
        if (argFace == null) {
            System.exit(1);
        }
        argFace.setPatternWatch(true);
        // argFace.setSortOptions(true);
        argFace.printHelp();
        int nArg = argFace.parse(args);
        if (nArg < 0) {
            System.exit(1);
        }
        System.out.println("Pattern match: " + argFace.getPatternMatch());
        while (nArg < args.length) {
            System.out.println("arg: " + args[nArg++]);
        }
        report(argFace);
    }
    
    private void report (ArgFace argFace) {
        if (argFace instanceof ArgProcedure) {
            setArgumentVariables(argFace);
        }
        if (aOpt) {
            System.out.println("-a");
            if (aSize != null) {
                System.out.println("size: " + aSize);
            }
        }
        if (binaryOpt) {
            System.out.println("--binary");
        }
        if (cOpt) {
            System.out.println("-c");
        }
        if (find) {
            for (String fname : file) {
                System.out.println("Find: \"" + pattern + "\" " + fname);
            }
        } else if (copy) {
            for (String fname : file) {
                System.out.println("Copy: \"" + pattern + "\" " + fname);
            }
            System.out.println("Output file: " + outfile);
        }
    }
    
    private void setArgumentVariables (ArgFace argFace) {
        setaOpt(argFace.has("-a"));
        setaSize(argFace.value("-a"));
        setBinaryOpt(argFace.has("-binary"));
        setcOpt(argFace.has("-c"));
        setFind(argFace.has("<find>"));
        setCopy(argFace.has("<copy>"));
        setPattern(argFace.value("<pattern>"));
        setFile(argFace.valueArray("<file>"));
        setOutfile(argFace.value("<outfile>"));
    }
       
    /**
     * @return the aOpt
     */
    public boolean isaOpt () {
        return aOpt;
    }

    /**
     * @param aOpt the aOpt to set
     */
    public void setaOpt (boolean aOpt) {
        this.aOpt = aOpt;
    }

    /**
     * @return the aSize
     */
    public String getaSize () {
        return aSize;
    }

    /**
     * @param aSize the aSize to set
     */
    public void setaSize (String aSize) {
        this.aSize = aSize;
    }

    /**
     * @return the binaryOpt
     */
    public boolean isBinaryOpt () {
        return binaryOpt;
    }

    /**
     * @param binaryOpt the binaryOpt to set
     */
    public void setBinaryOpt (boolean binaryOpt) {
        this.binaryOpt = binaryOpt;
    }

    /**
     * @return the cOpt
     */
    public boolean iscOpt () {
        return cOpt;
    }

    /**
     * @param cOpt the cOpt to set
     */
    public void setcOpt (boolean cOpt) {
        this.cOpt = cOpt;
    }

    /**
     * @return the find
     */
    public boolean isFind () {
        return find;
    }

    /**
     * @param find the find to set
     */
    public void setFind (boolean find) {
        this.find = find;
    }

    /**
     * @return the copy
     */
    public boolean isCopy () {
        return copy;
    }

    /**
     * @param copy the copy to set
     */
    public void setCopy (boolean copy) {
        this.copy = copy;
    }

    /**
     * @return the pattern
     */
    public String getPattern () {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern (String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the file
     */
    public String [] getFile () {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile (String [] file) {
        this.file = file;
    }

    /**
     * @return the outfile
     */
    public String getOutfile () {
        return outfile;
    }

    /**
     * @param outfile the outfile to set
     */
    public void setOutfile (String outfile) {
        this.outfile = outfile;
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
     * @return the usageText
     */
    public String [] getUsageText () {
        return usageText;
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
    
}
