/**
 *+
 *  ArgFind.java
 *	1.0.0	Apr 20, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * ArgFace finders.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgFind {
    private ArgReflect     reflect;
    private ArgUtil        util = ArgUtil.getInstance();
    private String         operandSuffix;
    private String         optionSuffix;
    
    /**
     * No argument constructor.
     */
    public ArgFind () {
    }
    
    /**
     * Constructs a new {@code ArgFind} instance with the specified {@code ArgReflect} object.
     * 
     * @param reflect the {@code ArgReflect} object
     */
    public ArgFind (ArgReflect reflect) {
    	setReflect(reflect);
    }
    
    /**
     * Sets the {@code ArgReflect} object for this {@code ArgFind} instance.
     * 
     * @param reflect the {@code ArgReflect} object
     */
    public void setReflect (ArgReflect reflect) {
		this.reflect = reflect;
	}
    
    /**
     * Returns the {@code ArgReflect} object for this {@code ArgFind} instance.
     * 
     * @return the {@code ArgReflect} object
     */
    public ArgReflect getReflect () {
    	return reflect;
    }
    
    private void setup () {
        if (operandSuffix == null) {
            operandSuffix = util.getOperandSuffix();
        }
        if (optionSuffix == null) {
            optionSuffix = util.getOptionSuffix();
        }
    }
    
    private String getName (ArgOperand operand) {
        return ArgUtil.camelCase(operand.getName());
    }
    
    private String getName (ArgOption option) {
        return ArgUtil.camelCase(option.getName());
    }
    
    private String getAltName (ArgOption option) {
        return ArgUtil.camelCase(option.getAltName());
    }
    
    private String getArgName (ArgOption option) {
        return ArgUtil.camelCase(option.getArgName());
    }
  
    /**
     * Finds a setter method for the specified operand.
     * This method only works for {@code boolean} and {@code String} setters and
     * for repeating operands of type {@code String[]} or {@code List}.
     * 
     * @param operand the operand to match to a setter method
     * @return the corresponding setter {@code Method}
     */
    public Method findOperandSetter (ArgOperand operand) {
        setup();
        Method method = null;
        String name = getName(operand);
        String fieldName = name + operandSuffix;
        operand.setFieldName(fieldName);
        if (operand.isRepeat()) {
            method = reflect.findSetter(fieldName, String[].class);
            if (method == null) {
                method = reflect.findSetter(fieldName, List.class);
                if (method != null) {
                    operand.setRepeatList(true);
                }
            }
        } else {
            method = reflect.findSetter(fieldName, String.class);
            if (method == null) {
                method = reflect.findSetter(fieldName, boolean.class);
            }
        }
        if (method != null) {
            operand.setSetter(method);
        }
        return method;
    }
    
    /**
     * Finds a field for the specified operand.
     * This method only works for {@code boolean} and {@code String} fields and
     * for repeating operands of types {@code String[]} or {@code List}.
     * 
     * @param operand the operand to match to a member variable
     * @return the corresponding variable {@code Field}
     */
    public Field findOperandField (ArgOperand operand) {
        setup();
        Field field = null;
        String name = getName(operand);
        String fieldName = name + operandSuffix;
        operand.setFieldName(fieldName);
        if (operand.isRepeat()) {
            field = reflect.findField(fieldName, String[].class);
            if (field == null) {
                field = reflect.findField(fieldName, List.class);
                if (field != null) {
                    operand.setRepeatList(true);
                }
            }
        } else {
            field = reflect.findField(fieldName, String.class);
            if (field == null) {
                field = reflect.findField(fieldName, boolean.class);
            }
        }
        if (field != null) {
            operand.setField(field);
        }
        return field;
    }
    
    /**
     * Finds a setter {@code Method} for a given {@code Option}. The setter
     * method name is formed using camelCase from the merging of the words
     * "set", the option {@code <name>} and the {@code <optionSuffix>} which is
     * "Option" by default. So if the option name is "debug", the setter name
     * would be "setDebugOption".
     * <p>
     * If the program has no setter for the {@code <name>} version of the
     * option, the {@code <altName>} version is tried. If neither of these
     * setter names are present in the program, a {@code null} value is
     * returned.
     * <p>
     * When a setter {@code Method} is found, the {@code Option} object is
     * updated with the "setter" method and the "fieldName" for the matching
     * variable.
     * 
     * @param option the {@code Option} object
     * @return the option setter {@code Method} or null
     */
    public Method findOptionSetter (ArgOption option) {
        setup();
        Method method = null;
        String name = getName(option);
        String altName = getAltName(option);
        String fieldName = name + optionSuffix;
        method = reflect.findSetter(fieldName, boolean.class);
        if (method == null) {
            if (altName != null) {
                fieldName = altName + optionSuffix;
                method = reflect.findSetter(fieldName, boolean.class);
            }
        }
        if (method != null) {
            option.setSetter(method);
            option.setFieldName(fieldName);
        }
        return method;
    }

    /**
     * Finds a variable {@code Field} for a given {@code Option}. The name of
     * the variable is formed using camelCase from the merger of the option
     * {@code <name>} and the {@code <optionSuffix>} which is "Option" by default.
     * So if the name is "debug", the variable name will be "debugOption".
     * <p>
     * If the program does not have the variable for the {@code <name>} version
     * of the option, the {@code <altName>} version is tried. If neither version
     * of the variable name is present in the program, a {@code null} value is returned.
     * <p>
     * When a {@code Field} is found, the {@code Option} object is updated with the "field"
     * and the "fieldName" for the matching variable.
     * 
     * @param option the {@code Option} object
     * @return the {@code Field} for the variable or null
     */
    public Field findOptionField (ArgOption option) {
        setup();
        Field field = null;
        String name = getName(option);
        String altName = getAltName(option);
        String fieldName = name + optionSuffix;
        Debug.verbose("findOptionField: " + fieldName);
        field = reflect.findField(fieldName, boolean.class);
        if (field == null) {
            if (altName != null) {
                fieldName = altName + optionSuffix;
                Debug.verbose("findOptionField alt: " + fieldName);
                field = reflect.findField(fieldName, boolean.class);
            }
        }
        if (field != null) {
            option.setField(field);
            option.setFieldName(fieldName);
        }
        return field;
    }

    /**
     * Finds a setter {@code Method} for the argument variable of a given
     * {@code Option}. The setter method name is formed using camelCase from the
     * merging of the word "set", the {@code <name>} and the {@code <argName>}
     * components of the option. So if the option name is "cd" and the argument
     * name is "path", the method name would be "setCdPath".
     * <p>
     * If the program does not define the setter method for the {@code <name>}
     * version of the argument, the {@code <altName>} version is tried. If
     * neither of these methods are defined by the program, a {@code null} value
     * is returned.
     * <p>
     * When a setter {@code Method} is found, the {@code Option} object is
     * updated with the "argSetter" and the "argName" set accordingly.
     * 
     * @param option the {@code Option} object
     * @return the setter {@code Method} for the option argument or null
     */
    public Method findArgSetter (ArgOption option) {
        String name = getName(option);
        String altName = getAltName(option);
        String argName = getArgName(option);
        String argFieldName = ArgUtil.camelCase(name, argName);
        Method setter = findArgSet(option, argFieldName);
        if (setter == null) {
            if (altName != null) {
                argFieldName = ArgUtil.camelCase(altName, argName);
                setter = findArgSet(option, argFieldName);
            }
        }
        if (setter != null) {
            option.setArgSetter(setter);
            option.setArgFieldName(argFieldName);
        }
        return setter;
    }
    
    private Method findArgSet (ArgOption option, String argFieldName) {
        Class<?> c = String.class;
        if (option.isRepeat()) {
            c = String[].class;
            option.setRepeatList(false);
        }
        Method setter = reflect.findSetter(argFieldName, c);
        if (setter != null) {
            option.setRepeatList(false);
        } else {
            if (option.isRepeat()) {
                c = List.class;
                option.setRepeatList(true);
                setter = reflect.findSetter(argFieldName, c);
            }
        }
        return setter;
    }
    
    /**
     * Finds a variable {@code Field} for the option argument given by {@code
     * Option}. The field name is formed using camelCase for the merger of the
     * option components for the {@code <name>} and the {@code <argName>}. So if
     * the name of the option is "xml" and the argument name is "file", then the
     * variable name would be "xmlFile".
     * <p>
     * If the program has not defined a variable for the {@code <name>} version
     * of option, the {@code <altName>} version is tried. If neither of these
     * versions are found in the program, a {@code null} value is returned.
     * <p>
     * When the variable {@code Field} of the option argument is found, the
     * {@code Option} object is updated with the "argField" and "argName"
     * components set accordingly.
     * 
     * @param option the {@code Option} object
     * @return the argument variable {@code Field} or null
     */
    public Field findArgField (ArgOption option) {
        Field field = null;
        String name = getName(option);
        String altName = getAltName(option);
        String argName = getArgName(option);
        String argFieldName = ArgUtil.camelCase(name, argName);
        field = findArgFld(option, argFieldName);
        if (field == null) {
            if (altName != null) {
                argFieldName = ArgUtil.camelCase(altName, argName);
                field = findArgFld(option, argFieldName);
            }
        }
        if (field != null) {
            option.setArgField(field);
            option.setArgFieldName(argFieldName);
        }
        return field;
    }
    
    private Field findArgFld (ArgOption option, String argFieldName) {
        Class<?> c = String.class;
        if (option.isRepeat()) {
            c = String[].class;
            option.setRepeatList(false);
        }
        Field field = reflect.findField(argFieldName, c);
        if (field == null) {
            if (option.isRepeat()) {
                c = List.class;
                option.setRepeatList(true);
            }
            field = reflect.findField(argFieldName, c);
        }
        return field;
    }
    
}
