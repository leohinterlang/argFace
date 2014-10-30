/**
 *+
 *	ArgCommand.java
 *	1.0.0  2014-08-03  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * The command implementation of ArgFace.
 * 
 * @author Leo Hinterlang
 *
 */
public class ArgCommand extends ArgStandard implements ArgFace {
	public static final int PROGRAM = 0;
	public static final int COMMAND1 = 1;
	public static final int COMMAND2 = 2;
	public static final int COMMAND3 = 3;
	public static final int COMMAND4 = 4;
	public static final int COMMAND5 = 5;
	public static final int COMMAND6 = 6;
	public static final int COMMAND7 = 7;
	public static final int COMMAND8 = 8;
	public static final int MAXINDEX = 8;
	private static Object[] clis = new Object[MAXINDEX + 1];
	private static ArgCommand[] argCommands = new ArgCommand[MAXINDEX + 1];
	private static int previousLevel = 0;
	private static String programOptionSuffix;
	private int currentLevel = 0;
   
    /**
     * Protected no argument constructor.
     */
    protected ArgCommand () {
    }
    
    /**
     * Creates a new {@code ArgCommand} instance.
     * 
     * @return the new {@code ArgCommand} instance
     * @see #create(String)
     * @see #create(String[])
     */
    public static ArgCommand create () {
    	ArgCommand argCommand = new ArgCommand();
    	argCommand.currentLevel = previousLevel++;
    	argCommand.setArgCommand(argCommand);
        return argCommand;
    }
    
    /**
     * Creates a new {@code ArgCommand} instance and sets the
     * usage text specification.
     * 
     * @param usageText the usage text as a String
     * @return the new {@code ArgCommand} instance
     * @see #create()
     * @see #create(String[])
     * @see #setUsageText(String)
     */
    public static ArgCommand create (String usageText) {
        ArgCommand argCommand = create();
        argCommand.setUsageText(usageText);
        return argCommand;
    }
    
    /**
     * Creates a new {@code ArgCommand} instance and sets the
     * usage text specification.
     * 
     * @param usageText the usage text as an array of Strings
     * @return the new {@code ArgCommand} instance
     * @see #create()
     * @see #create(String)
     * @see #setUsageText(String[])
     */
    public static ArgCommand create (String[] usageText) {
    	ArgCommand argCommand = create();
        argCommand.setUsageText(usageText);
        return argCommand;
    }
    
    public static ArgCommand create (String usageText, Object cli) {
        ArgCommand argCommand = create(usageText);
        argCommand.setCLI(cli);
        if (argCommand.parseUsage(cli)) {
        	return argCommand;
        }
        return null;
    }
    
    public static ArgCommand create (String [] usageText, Object cli) {
        ArgCommand argCommand = create(usageText);
        argCommand.setCLI(cli);
        if (argCommand.parseUsage(cli)) {
            return argCommand;
        }
        return null;
    }
    
    public int parse (String [] args) {
        int nArg = parseArguments(args);
        if (nArg < 0) {
        	return nArg;
        }
        return nArg;
    }
    
    public boolean executeCommand () {
    	String command = value("<command>");
    	if (command != null) {
    		String [] commandArgs = valueArray("<command-args>");
    		return executeCommand(command, commandArgs);
    	}
    	return false;
    }
   
	public boolean executeCommand(String command, String [] commandArgs) {
    	Object cli = getCLI(currentLevel);
    	if (commandArgs == null) {
    		commandArgs = new String[0];
    	}
		String packageName = cli.getClass().getPackage().getName();
		String cmd = Character.toUpperCase(command.charAt(0)) + command.substring(1);
		String cmdPrefix = "";
		if (currentLevel > PROGRAM) {
			cmdPrefix = cli.getClass().getSimpleName();
			cmdPrefix = cmdPrefix.substring(0, cmdPrefix.length() - 7);
		}
		cmd = cmdPrefix + cmd + "Command";
		String className = packageName + "." + cmd;
		Class<?> commandClass = null;
		try {
			commandClass = Class.forName(className);
		} catch (ClassNotFoundException ex) {
			System.out.println("Invalid command: " + classToCommand(cmd));
			System.out.println("Looking for: " + className);
			return false;
		}
		Object commandObject = null;
		try {
			commandObject = commandClass.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		Method m = null;
		try {
			Class<?> [] exParams = {String[].class};
			m = commandClass.getMethod("execute", exParams);
		} catch (NoSuchMethodException e) {
			System.out.println("No \"execute\" method for \"" + command + "\" command");
			return false;
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		Boolean ret = null;
		try {
			ret = (Boolean) m.invoke(commandObject, (Object)commandArgs);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return ret.booleanValue();
	}

	public ArgCommand getArgCommand (int index) {
		if (0 <= index && index <= MAXINDEX) {
			return argCommands[index];
		}
		return null;
	}

	private void setArgCommand (ArgCommand argCommand) {
		argCommands[currentLevel] = argCommand;
	}

	public Object getCLI (int index) {
		if (0 <= index && index <= MAXINDEX) {
			return clis[index];
		}
		return null;
	}

	private void setCLI (Object cli) {
		clis[currentLevel] = cli;
	}

	protected Boolean modelGetPosixFormat () {
		return true;
	}
	
	protected String modelGetOptionSuffix () {
		String optionSuffix = super.modelGetOptionSuffix();
		if (currentLevel == PROGRAM) {
			programOptionSuffix = optionSuffix;
		}
		if (optionSuffix.equals("Option")) {
			if (programOptionSuffix != null) {
				optionSuffix = programOptionSuffix;
			}
		}
		return optionSuffix;
	}
	
	protected String modelGetOperandSuffix () {
		String operandSuffix = super.modelGetOperandSuffix();
		if (operandSuffix.equals("Operand")) {
			operandSuffix = "";
		}
		return operandSuffix;
	}
	
	public String classToCommand (String name) {
		if (name.endsWith("Command")) {
			name = name.substring(0, name.length() - 7);
			StringBuilder command = new StringBuilder(32);
			boolean first = true;
			for (char c : name.toCharArray()) {
				if (Character.isUpperCase(c)) {
					if (first) {
						first = false;
					} else {
						command.append(' ');
					}
					command.append(Character.toLowerCase(c));
				} else {
					command.append(c);
				}
			}
			return command.toString();
		}
		return null;
	}
}
