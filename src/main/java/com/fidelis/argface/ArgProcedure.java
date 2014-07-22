/**
 *+
 *  ArgProcedure.java
 *	1.0.0	Apr 18, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * The procedure implementation of ArgFace. Uses no reflection at all.
 * 
 * @version 1.0.1
 * @author Leo Hinterlang
 * 
 */
public class ArgProcedure extends ArgBase implements ArgFace {
    private static ArgProcedure instance;

    /**
     * Creates or obtains the one and only {@code ArgProcedure} instance.
     * 
     * @return the one and only {@code ArgProcedure} instance
     */
    public static ArgProcedure create () {
        if (instance == null) {
            instance = new ArgProcedure();
        }
        return instance;
    }
    
    /**
     * Creates a new {@code ArgProcedure} model of ArgFace.
     * 
     * @param usageText the usage text as a String
     * @param pojo the program object
     * @return the {@code ArgProcedure} instance or null
     */
    public static ArgProcedure create (String usageText, Object pojo) {
        create().setUsageText(usageText);
        if (instance.parseUsage(pojo)) {
            return instance;
        }
        return null;
    }
    
    /**
     * Creates a new {@code ArgProcedure} model of ArgFace.
     * 
     * @param usageText the usage text as an array of Strings
     * @param pojo the program object
     * @return the {@code ArgProcedure} instance or null
     */
    public static ArgProcedure create (String [] usageText, Object pojo) {
        create().setUsageText(usageText);
        if (instance.parseUsage(pojo)) {
            return instance;
        }
        return null;
    }
    
    private boolean parseUsage (Object pojo) {
        setProgramName(pojo.getClass().getSimpleName());
        return parseUsage();
    }
    
    public int parse (String [] args) {
        return parseArguments(args);
    }

}
