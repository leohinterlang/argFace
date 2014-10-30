/**
 *+
 *  ArgOperand.java
 *	1.0.0	Apr 1, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Defines an argument operand.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgOperand {
    private static final int UNKNOWN  = 0;
    private static final int VARIABLE = 1;
    private static final int LITERAL  = 2;

    private int              type;
    private String           name;
    private String           fieldName;
    private boolean          optional;
    private Method           setter;
    private Field            field;
    private boolean         repeat;
    private boolean         repeatList;
    private List<String>     list;
    private boolean          has;
    private String           value;
    private int              count;

    /**
     * Creates a new {@code ArgOperand}.
     * 
     */
    public ArgOperand () {
        type = UNKNOWN;
    }
    
    /**
     * Sets this operand type as variable.
     * 
     * @return this {@code ArgOperand}
     */
    public ArgOperand variable () {
        type = VARIABLE;
        return this;
    }
    
    /**
     * Tests if this operand type is variable.
     * 
     * @return {@code true} if this operand type is variable
     */
    public boolean isVariable () {
        return type == VARIABLE;
    }
    
    /**
     * Sets this operand type as literal.
     * 
     * @return this {@code ArgOperand}
     */
    public ArgOperand literal () {
        type = LITERAL;
        return this;
    }
    
    /**
     * Tests if this operand type is literal.
     * 
     * @return {@code true} if this operand type is literal
     */
    public boolean isLiteral () {
        return type == LITERAL;
    }

    /**
     * Returns the name of this operand.
     * 
     * @return the operand name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of this operand.
     * 
     * @param name the operand name
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * Returns the field name for this operand.
     * 
     * @return the field name
     */
    public String getFieldName () {
        return fieldName;
    }

    /**
     * Sets the field name for this operand.
     * 
     * @param fieldName the field name
     */
    public void setFieldName (String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Test if this operand is optional.
     * 
     * @return {@code true} if this operand is optional
     */
    public boolean isOptional () {
        return optional;
    }

    /**
     * Sets this operand as optional or not.
     * 
     * @param optional {@code true} sets this operand as optional
     */
    public void setOptional (boolean optional) {
        this.optional = optional;
    }
    
    /**
     * Returns the setter {@code Method} for this operand.
     * 
     * @return the setter {@code Method}
     */
    public Method getSetter () {
        return setter;
    }

    /**
     * Sets the setter {@code Method} for this operand.
     * 
     * @param setter the setter {@code Method}
     */
    public void setSetter (Method setter) {
        this.setter = setter;
    }

    /**
     * Returns the {@code Field} for this operand.
     * 
     * @return the {@code Field}
     */
    public Field getField () {
        return field;
    }

    /**
     * Sets the {@code Field} for this operand.
     * 
     * @param field the {@code Field}
     */
    public void setField (Field field) {
        this.field = field;
    }

    /**
     * @return the repeat
     */
    public boolean isRepeat () {
        return repeat;
    }

    /**
     * @param repeat the repeat to set
     */
    public void setRepeat (boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * @return the repeatList
     */
    public boolean isRepeatList () {
        return repeatList;
    }
    
    /**
     * @param repeatList the repeatList to set
     */
    public void setRepeatList (boolean repeatList) {
        this.repeatList = repeatList;
    }

    /**
     * @return the list
     */
    public List<String> getList () {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList (List<String> list) {
        this.list = list;
    }
    
    /**
     * Adds a new String to the operand list.
     * 
     * @param value String to add to the operand list
     */
    public void addList (String value) {
        list.add(value);
    }

    /**
     * Returns {@code true} if this operand has been specified on the command line.
     * 
     * @return {@code true} if this operand has been specified
     */
    public boolean has () {
        return has;
    }

    /**
     * Sets the indicator that this operand has been specified on the command line.
     * 
     * @param has {@code true} to indicate that this operand has been specified
     */
    public void setHas (boolean has) {
        this.has = has;
    }

    /**
     * Returns the value for this operand.
     * 
     * @return the value for this operand
     */
    public String getValue () {
        return value;
    }

    /**
     * Sets the value for this operand.
     * 
     * @param value the value for this operand
     */
    public void setValue (String value) {
        this.value = value;
    }

    /**
     * Returns the number of times this operand appears on the command line.
     * 
     * @return the count
     */
    public int getCount () {
        return count;
    }

    /**
     * Sets the number of times this operand appears on the command line.
     * 
     * @param count the count
     */
    public void setCount (int count) {
        this.count = count;
    }
    
    /**
     * Returns a {@code String} in a form suitable for the usage specification.
     * 
     * @return the specification text
     */
    public String getSpecText () {
        if (isLiteral()) {
            return name;
        }
        if (isVariable()) {
            return "<" + name + ">";
        }
        return null;
    }
    
    /**
     * Resets the argument components of this operand.
     */
    public void reset () {
        has = false;
        value = null;
        count = 0;
        if (list != null) {
            list.clear();
        }
    }

    @Override
    public String toString () {
        return String.format("%12.12s %s %s %s %s %12.12s",
                name,
                (optional ? "O" : " "),
                (repeat ? "R" : " "),
                (type == VARIABLE ? "VAR" :
                    (type == LITERAL ? "LIT" : "UNK")),
                (setter != null ? "S" :
                    (field != null ? "F" : " ")),
                (fieldName != null ? fieldName : " "));
    }

}
