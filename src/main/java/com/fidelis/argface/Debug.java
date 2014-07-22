/**
 *+
 *  ArgDebug.java
 *	1.0.0	Jun 19, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class Debug {
    private static boolean verbose;
    private static boolean trace;

    /**
     * Tests to see if the "verbose" option has been enabled.
     * 
     * @return {@code true} if the verbose option is enabled
     */
    public static boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets the verbose option as specified.
     * 
     * @param onOff {@code true} to enable the verbose option, {@code false}
     *            turns it off
     */
    public static void setVerbose(boolean onOff) {
        verbose = onOff;
    }

    /**
     * Tests to see if the "trace" option has been enabled.
     * 
     * @return {@code true} if the trace option is enabled
     */
    public static boolean isTrace() {
        return trace;
    }

    /**
     * Sets the trace option as specified.
     * 
     * @param onOff {@code true} to enable the trace option
     *              {@code false} turns it off
     */
    public static void setTrace(boolean onOff) {
        trace = onOff;
    }

    /**
     * Produces diagnostic output when verbose mode is enabled.
     * This method uses "println" to the "System.out" stream.
     * 
     * @param text the output String
     */
    public static void verbose(String text) {
        if (verbose) {
            System.out.println(text);
        }
    }

    /**
     * Produces diagnostic output for an {@code Object} when verbose mode is
     * enabled. The {@code String.valueOf} method is used on the {@code Object}
     * to get its String representation. Then "println" is used to the
     * "System.out"
     * stream.
     * 
     * @param object the {@code Object} to print
     */
    public static void verbose(Object object) {
        if (verbose) {
            System.out.println(String.valueOf(object));
        }
    }

    /**
     * Produces diagnostic output when trace mode is enabled.
     * This method uses "println" to the "System.out" stream.
     * 
     * @param text the output String
     */
    public static void trace(String text) {
        if (trace) {
            System.out.println(text);
        }
    }

    /**
     * Produces diagnostic output in verbose mode.
     * This method uses "print" to the "System.out" stream.
     * 
     * @param text the output String
     */
    public static void vprint(String text) {
        if (verbose) {
            System.out.print(text);
        }
    }

}
