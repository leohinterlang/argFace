/**
 *+
 *  ArgCommon.java
 *	1.0.0	Apr 21, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * ArgFace common code.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
/**
 * ArgCommon
 *
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgCommon {
    private ArgReflect reflect;
    private ArgFind finder;
    
    /**
     * No argument constructor.
     */
    public ArgCommon () {
    }
    
    /**
     * Constructor that sets the {@code ArgReflect} object for this instance.
     */
    public ArgCommon (ArgReflect reflect) {
    	setReflect(reflect);
    	finder = new ArgFind(reflect);
    }
    
    /**
     * Returns the {@code ArgReflect} object for this {@code ArgCommon} instance.
     * 
     * @return the {@code ArgReflect} object
     */
    public ArgReflect getReflect () {
    	return reflect;
    }
    
    /**
     * Sets the {@code ArgReflect} object for this {@code ArgCommon} instance.
     * 
	 * @param reflect the {@code ArgReflect} object
	 */
	public void setReflect (ArgReflect reflect) {
		this.reflect = reflect;
	}

    /**
     * Returns the "usage" text defined by the program. If there is no {@code
     * usageText} variable defined by the program, and no getter method to
     * retrieve it, a "Can't access" message is printed, and {@code null} is
     * returned.
     * 
     * @return the usage text or null
     */
    public String getUsageText () {
        String varName = "usageText";
        String usageText = reflect.getString(varName);
        if (usageText == null) {
            ArgUtil.cantAccess(varName);
        }
        return usageText;
    }
    
    /**
     * Returns the "versionText" from the program. If this value could not
     * be found, or contains the value "none", a {@code null} value is returned.
     * The program may wish to deal with the version option on its own. Therefore,
     * no error message is output.
     * 
     * @return the version text String or null
     */
    public String getVersionText () {
        String versionText = reflect.getString("versionText");
        if ("none".equals(versionText)) {
            versionText = null;
        }
        return versionText;
    }
    
    /**
     * Returns the "aboutText" from the program. If this value could not
     * be found, or contains the value "none", a {@code null} value is returned.
     * The program may wish to handle the about option on its own. No error
     * message is output.
     * 
     * @return the about text or null
     */
    public String getAboutText () {
        String aboutText = reflect.getString("aboutText");
        if ("none".equals(aboutText)) {
            aboutText = null;
        }
        return aboutText;
    }
    
    /**
     * Returns the "helpText" from the program. If this value is not defined,
     * or contains the value "none", a {@code null} value is returned.
     * The program may handle the help option on its own. No error message is
     * output.
     * 
     * @return the help text or null
     */
    public String getHelpText () {
        String helpText = reflect.getString("helpText");
        if ("none".equals(helpText)) {
            helpText = null;
        }
        return helpText;
    }
  
    /**
     * Returns the "operandSuffix" text to be used to form the names of the
     * "operand" variables. If the program has not defined an "operandSuffix"
     * variable, the default suffix "Operand" is returned.
     * 
     * @return the operand suffix text, the default is "Operand"
     */
    public String getOperandSuffix () {
        String operandSuffix = reflect.getString("operandSuffix");
        if (operandSuffix == null) {
            operandSuffix = "Operand";
        }
        return operandSuffix;
    }
    
    /**
     * Returns the "optionSuffix" text to be used to form the names of the
     * "option" variables. If the program has not defined an "optionSuffix"
     * variable, the default suffix "Option" is returned.
     * 
     * @return the option suffix text, the default is "Option"
     */
    public String getOptionSuffix () {
        String optionSuffix = reflect.getString("optionSuffix");
        if (optionSuffix == null) {
            optionSuffix = "Option";
        }
        return optionSuffix;
    }
    
    /**
     * Returns the "allowOverwrite" value from the program. If the variable is
     * not defined, a {@code null} value is returned.
     * 
     * @return the allow overwrite {@code Boolean} value or null
     */
    public Boolean getAllowOverwrite () {
        return reflect.getBoolean("allowOverwrite");
    }
    
    /**
     * Returns the "suppressHelp" value from the program. If this value could
     * not be found, or contains the value {@code false}, then the help facility
     * of {@code ArgFace} will operate in the normal manner. If the value is
     * {@code true}, the help facility will not produce any output.
     * 
     * @return {@code true} to suppress the {@code ArgFace} help facility
     */
    public boolean getSuppressHelp () {
        Boolean suppressHelp = reflect.getBoolean("suppressHelp");
        if (suppressHelp == null) {
            suppressHelp = false;
        }
        return suppressHelp;
    }

    /**
     * Returns the "posixFormat" value from the program. If this value could not
     * be found, or contains the value {@code false}, options and operands may
     * be mixed on the command line. If the value is {@code true}, then
     * arguments are expected to have options first followed by operands.
     * 
     * @return {@code true} requires options before operands
     */
    public boolean getPosixFormat () {
        Boolean posixFormat = reflect.getBoolean("posixFormat");
        if (posixFormat == null) {
            posixFormat = false;
        }
        return posixFormat;
    }
    
    /**
     * Returns the "sortOptions" value from the program. If this value could not
     * be found, or contains the value {@code false}, the options listed in the
     * help text will be ordered as they were encountered in the usage text. If
     * the value is {@code true}, then options will be listed in sorted order.
     * 
     * @return {@code true} to list options in sorted order
     */
    public boolean getSortOptions () {
        Boolean sortOptions = reflect.getBoolean("sortOptions");
        if (sortOptions == null) {
            sortOptions = false;
        }
        return sortOptions;
    }

    /**
     * Sets the non-option "operands" variable in the program.
     * 
     * @param nonOptionList list of non-option operands
     */
    public void setNonOptions (List<String> nonOptionList) {
        String fieldName = "operands";
        Method setter = reflect.findSetter(fieldName, String[].class);
        if (setter != null) {
            String [] array = nonOptionList.toArray(new String[0]);
            reflect.setValue(setter, array);
            return;
        }
        setter = reflect.findSetter(fieldName, List.class);
        if (setter != null) {
            reflect.setValue(setter, nonOptionList);
            return;
        }
        Field field = reflect.findField(fieldName, String[].class);
        if (field != null) {
            String [] array = nonOptionList.toArray(new String[0]);
            reflect.setValue(field, array);
            return;
        }
        field = reflect.findField(fieldName, List.class);
        if (field != null) {
            reflect.setValue(field, nonOptionList);
        }
    }
    
    /**
     * Post process an operand.
     * 
     * @param operand the operand
     * @return {@code true} on success
     */
    public boolean postProcess (ArgOperand operand) {
        boolean status = true;
        
        // Find setter method for this operand.
        Method setter = finder.findOperandSetter(operand);
        
        // If no setter, find field.
        Field field = null;
        if (setter == null) {
            field = finder.findOperandField(operand);
        }
        
        // If no setter and no field, print can't access msg.
        if (setter == null && field == null) {
            ArgUtil.cantAccess(operand);
            status = false;
        }
        
        Debug.trace("pp: " + operand);
        return status;
    }
    
    /**
     * Post process an option.
     * 
     * @param option the option
     * @return {@code true} on success
     */
    public boolean postProcess (ArgOption option) {
        boolean status = true;

        // Find a setter for <name><optionSuffix> or <altName><optionSuffix>.
        Method setter = finder.findOptionSetter(option);

        // If there is no setter, try field variable.
        // Again either <name><optionSuffix> or <altName><optionSuffix>.
        Field field = null;
        if (setter == null) {
            field = finder.findOptionField(option);
        }

        // There is no field and no setter.
        // Unless this option is a creation, Print can't access msg.
        if ((field == null) && (setter == null)) {
            
            if (! option.isCreation()) {
                ArgUtil.cantAccess(option);
                status = false;
            }
        }

        // If this option includes an argument, find access to variable.
        String argName = option.getArgName();
        if (argName != null) {

            // Try setter for argument.
            Method argSetter = finder.findArgSetter(option);

            // If setter not found, try for argument field.
            if (argSetter == null) {
                Field argField = finder.findArgField(option);

                // Field not found, print can't access msg.
                if (argField == null) {
                    ArgUtil.cantAccessArg(option);
                    status = false;
                }
            }
        }
        Debug.trace("pp: " + option);
        return status;
    }

    /**
     * Sets the program variables for the indicated operands and options.
     * 
     * @param operands the list of operands
     * @param options the list of options
     */
    public void setProgramVariables (List<ArgOperand> operands,
            List<ArgOption> options) {
        for (ArgOperand operand : operands) {
            setOperandVariables(operand);
        }
        for (ArgOption option : options) {
            setOptionVariables(option);
        }
    }
    
    private void setOperandVariables (ArgOperand operand) {
        if (operand.has()) {
            if (operand.isLiteral()) {
                setOperandValue(operand, true);
            } else if (operand.isVariable()) {
                if (operand.isRepeat()) {
                    
                }
                setOperandValue(operand, operand.getValue());
            }
        }
    }
    
    /**
     * Sets the value of an operand.
     * 
     * @param operand the {@code ArgOperand}
     * @param value the value to set
     */
    private void setOperandValue (ArgOperand operand, Object value) {
        Method setter = operand.getSetter();
        Field field = operand.getField();
        if (operand.isRepeat()) {
            setRepeat(setter, field, operand.getList(), operand.isRepeatList());
        } else {
            if (setter != null) {
                reflect.setValue(setter, value);
            } else if (field != null) {
                reflect.setValue(field, value);
            }
        }
    }
    
    private void setOptionVariables (ArgOption option) {
        if (option.has()) {
            setOptionVar(option);
            setOptionArgVar(option);
            setOptionCount(option);
        }
    }
    
    private void setOptionVar (ArgOption option) {
        Method setter = option.getSetter();
        if (setter != null) {
            reflect.setValue(setter, true);
        } else {
            Field field = option.getField();
            if (field != null) {
                reflect.setValue(field, true);
            }
        }
    }
    
    private void setOptionArgVar (ArgOption option) {
        Method setter = option.getArgSetter();
        Field field = option.getArgField();
        if (option.isRepeat()) {
            List<String> list = option.getList();
            if (setter != null) {
                if (option.isRepeatList()) {
                    reflect.setValue(setter, list);
                } else {
                    reflect.setValue(setter, list.toArray(new String[0]));
                }
            } else if (field != null) {
                if (field.getType().isArray()) {
                    reflect.setValue(field, list.toArray(new String[0]));
                } else {
                    reflect.setValue(field, list);
                }
            }       
        } else {
            String value = option.getArgValue();
            if (setter != null) {
                reflect.setValue(setter, value);
            } else if (field != null) {
                reflect.setValue(field, value);
            }
        }
    }
    
    private void setOptionCount (ArgOption option) {
        String name = option.getName();
        String altName = option.getAltName();
        int count = option.getCount();
        String countSuffix = "Count";
        String fieldName = name + countSuffix;
        Method setter = reflect.findSetter(fieldName, int.class);
        if (setter == null) {
            if (altName != null) {
                fieldName = altName + countSuffix;
                setter = reflect.findSetter(fieldName, int.class);
            }
        }
        if (setter != null) {
            reflect.setValue(setter, count);
        } else {
            fieldName = name + countSuffix;
            Field field = reflect.findField(fieldName, int.class);
            if (field == null) {
                fieldName = altName + countSuffix;
                field = reflect.findField(fieldName, int.class);
            }
            if (field != null) {
                reflect.setValue(field, count);
            }
        } 
    }

    public void setRepeat (Method setter, Field field, List<String> list,
            boolean isList) {
        if (setter != null) {
            if (isList) {
                reflect.setValue(setter, list);
            } else {
                reflect.setValue(setter, list.toArray(new String[0]));
            }
        } else if (field != null) {
            if (isList) {
                reflect.setValue(field, list);
            } else {
                reflect.setValue(field, list.toArray(new String[0]));
            }
        }       
    }
}
