/**
 *+
 *  ArgOption.java
 *	1.0.0	Mar 15, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Command Line Arguments Option Class. This class contains all of the
 * information that the {@code Arguments} processor needs to know about a
 * particular command line option.
 * <p>
 * Each option has a name and an optional alternate name. Because these names
 * are converted to lower camelCase, their original versions are maintained as
 * well. Note that a name like --this-option-is-long turns into
 * thisOptionIsLong.
 * <p>
 * By using reflection to test for what the program supplies in the way of
 * setters and option variables, the field name is established. If there is a
 * setter for the variable, the setter {@code Method} is utilized. But if only
 * the variable is found, the variable {@code Field} is set.
 * <p>
 * Options can take an argument and its name is again determined by what the
 * program has declared. If the argument name is {@code null}, that indicates
 * that there is no argument. An original version of the argument name, with
 * possible embedded dashes, is available here as well. When it is determined
 * what variable name is supplied by the program, that becomes the new name for
 * the argument. There are argument setter and argument field components also
 * available.
 * <p>
 * There is a flag to indicate if an option argument is optional. There is a
 * flag to indicate a repeatable option. There should be a flag to indicate a
 * repeatable argument (comma separated list). There's more to write about here.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 * 
 */
public class ArgOption implements Comparable<ArgOption> {
    private int          spec;
    private String       name;
    private String       altName;
    private String       fieldName;
    private Field        field;
    private Method       setter;
    private boolean      has;
    private int          count;
    private boolean      creation;
    private String       argName;
    private String       argFieldName;
    private Field        argField;
    private Method       argSetter;
    private String       argValue;
    private boolean      argOptional;
    private boolean      argRepeat;
    private boolean      argSet;
    private boolean      repeat;
    private boolean      repeatList;
    private List<String> list;
    private String       help;

    /**
     * Creates a new {@code ArgOption}.
     * 
     */
    public ArgOption () {
    }

    /**
     * Creates a new {@code ArgOption} with the specified name.
     * 
     * @param name the name of the option
     */
    public ArgOption(String name) {
        this.name = name;
    }

    /**
     * @return the option text
     */
    public String getText () {
        String text = name.length() > 1 ? "--" : "-";
        text += name;
        if (altName != null) {
            text += ", " + (altName.length() > 1 ? "--" : "-") + altName;
        }
        return text;
    }
    
    /**
     * Returns the usage specification text for this option.
     * 
     * @return the usage specification text
     */
    public String getSpecText () {
        if (spec == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(32);
        sb.append('-');
        if (spec == 1 || spec == 3 || altName == null) {
            if (name.length() > 1) {
                sb.append('-');
            }
            sb.append(name);
        }
        if (spec == 2 || spec == 3) {
            if (altName != null) {
                if (spec == 3) {
                    sb.append("|-");
                }
                if (altName.length() > 1) {
                    sb.append('-');
                }
                sb.append(altName);
            }
        }
        if (argName != null) {
            if (argOptional) {
                sb.append(" [");
                sb.append(argName);
                sb.append(']');
            } else {
                sb.append(" <");
                sb.append(argName);
                sb.append('>');
            }
        }
        return sb.toString();
    }

    /**
     * Returns {@code true} if the given name matches this option.
     * 
     * @param name the name for this option
     * @return {@code true} for a match of the name or the alternate name
     */
    public boolean nameMatch (String name) {
        if (this.name.equals(name)) {
            return true;
        }
        if (name.equals(altName)) {
            return true;
        }
        return false;
    }

    /**
     * Returns the specification indicator.
     * 
     * @return the specification indicator
     */
    public int getSpec () {
        return spec;
    }
    

    /**
     * Sets the specification indicator.
     * 
     * @param spec the specification indicator
     */
    public void setSpec (int spec) {
        this.spec = spec;
    }
    

    /**
     * Returns the name of this option.
     * 
     * @return the name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of this option.
     * 
     * @param name the name to set
     */
    public void setName (String name) {
        this.name = name;
    }
    
    /**
     * Returns the alternate name of this option.
     * 
     * @return the alternate name
     */
    public String getAltName () {
        return altName;
    }

    /**
     * Sets the alternate name of this option.
     * If the alternate name is shorter than the name, these values are swapped.
     * 
     * @param altName the alternate name
     */
    public void setAltName (String altName) {
        this.altName = altName;
        if (altName.length() < name.length()) {
            this.altName = name;
            name = altName;
        }
    }

    /**
     * Returns the field name for this option variable.
     * 
     * @return the name of the field
     */
    public String getFieldName () {
        return fieldName;
    }

    /**
     * Sets the field name for this option variable.
     * 
     * @param fieldName the name of the field
     */
    public void setFieldName (String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Returns the {@code Field} for this option variable.
     * 
     * @return the {@code Field} for this option variable
     */
    public Field getField () {
        return field;
    }

    /**
     * Sets the {@code Field} for this option variable.
     * 
     * @param field the {@code Field} for this option variable
     */
    public void setField (Field field) {
        this.field = field;
    }

    /**
     * Returns the setter {@code Method} for this option variable.
     * 
     * @return the setter {@code Method} for this option variable
     */
    public Method getSetter () {
        return setter;
    }

    /**
     * Sets the setter {@code Method} for this option variable.
     * 
     * @param setter the setter {@code Method} for this option variable
     */
    public void setSetter (Method setter) {
        this.setter = setter;
    }

    /**
     * Returns {@code true} if this option was specified in the command line
     * arguments.
     * 
     * @return {@code true} if this option was specified in the command line
     *         arguments
     */
    public boolean has () {
        return has;
    }

    /**
     * Sets the indicator for whether this option was specified in the command
     * line arguments.
     * 
     * @param has {@code true} to indicate that this option was specified in the
     *            command line arguments.
     */
    public void setHas (boolean has) {
        this.has = has;
    }

    /**
     * Returns the number of times this option was specified on the command
     * line.
     * 
     * @return the count
     */
    public int getCount () {
        return count;
    }

    /**
     * Sets the number of times this option was specified on the command line.
     * 
     * @param count the count
     */
    public void setCount (int count) {
        this.count = count;
    }

    /**
     * Tests if this option is an internal creation.
     * 
     * @return {@code true} if this option is an internal creation
     */
    public boolean isCreation () {
        return creation;
    }

    /**
     * Sets the indicator for whether this option is an internal creation.
     * 
     * @param creation {@code true} to indicate that this option is an internal
     *            creation
     */
    public void setCreation (boolean creation) {
        this.creation = creation;
    }
    

    /**
     * Returns the name of the argument for this option.
     * 
     * @return the name of the argument
     */
    public String getArgName () {
        return argName;
    }

    /**
     * Sets the name of the argument for this option.
     * 
     * @param argName the name of the argument
     */
    public void setArgName (String argName) {
        this.argName = argName;
    }
    
    /**
     * Returns the name of the argument field in the program.
     * 
     * @return the argument field name
     */
    public String getArgFieldName () {
        return argFieldName;
    }

    /**
     * Sets the name of the argument field in the program.
     * 
     * @param argFieldName the argument field name
     */
    public void setArgFieldName (String argFieldName) {
        this.argFieldName = argFieldName;
    }

    /**
     * Returns the {@code Field} for the argument variable.
     * 
     * @return the {@code Field} for the argument variable
     */
    public Field getArgField () {
        return argField;
    }

    /**
     * Sets the {@code Field} for the argument variable.
     * 
     * @param argField the {@code Field} for the argument variable
     */
    public void setArgField (Field argField) {
        this.argField = argField;
    }

    /**
     * Returns the setter {@code Method} for the argument variable.
     * 
     * @return the setter {@code Method} for the argument variable
     */
    public Method getArgSetter () {
        return argSetter;
    }

    /**
     * Sets the setter {@code Method} for the argument variable.
     * 
     * @param argSetter the setter {@code Method} for the argument variable
     */
    public void setArgSetter (Method argSetter) {
        this.argSetter = argSetter;
    }

    /**
     * Returns the argument value for this option.
     * 
     * @return the argument value
     */
    public String getArgValue () {
        return argValue;
    }
    

    /**
     * Sets the argument value for this option.
     * 
     * @param argValue the argument value
     */
    public void setArgValue (String argValue) {
        this.argValue = argValue;
    }

    /**
     * Sets the flag for whether the option argument is optional.
     * 
     * @param argOptional {@code true} if the option argument is optional
     */
    public void setArgOptional (boolean argOptional) {
        this.argOptional = argOptional;
    }

    /**
     * Returns {@code true} if the option argument is optional.
     * 
     * @return {@code true} if the option argument is optional
     */
    public boolean isArgOptional () {
        return argOptional;
    }
  
    /**
     * @return the argRepeat
     */
    public boolean isArgRepeat () {
        return argRepeat;
    }

    /**
     * @param argRepeat the argRepeat to set
     */
    public void setArgRepeat (boolean argRepeat) {
        this.argRepeat = argRepeat;
    }

    /**
     * Sets the flag whether the option argument has been set.
     * 
     * @param argSet {@code true} to indicate that the option argument has been set
     */
    public void setArgSet (boolean argSet) {
        this.argSet = argSet;
    }

    /**
     * Tests if the option argument has been set.
     * 
     * @return {@code true} if the option argument has been set
     */
    public boolean isArgSet () {
        return argSet;
    }

    /**
     * Tests if this option is repeatable.
     * 
     * @return {@code true} if this option is repeatable
     */
    public boolean isRepeat () {
        return repeat;
    }

    /**
     * Sets the condition of the repeatable flag.
     * 
     * @param repeat {@code true} to set this option as repeatable
     */
    public void setRepeat (boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * Returns {@code true} if the option argument is a {@code
     * List&lt;String&gt;} type. Otherwise, the option argument is an array of
     * {@code String}s. This only applies if the option is repeatable.
     * 
     * @return {@code true} for {@code List} of type {@code String}
     */
    public boolean isRepeatList () {
        return repeatList;
    }
    

    /**
     * Sets the option argument as a {@code List&lt;String&gt;} type.
     * If this is {@code false}, the option argument is an array of {@code String}s.
     * This only applies if the option is repeatable.
     * 
     * @param repeatList the repeatList to set
     */
    public void setRepeatList (boolean repeatList) {
        this.repeatList = repeatList;
    }
    

    /**
     * Returns the list of argument values for this option.
     * 
     * @return the list of argument values
     */
    public List<String> getList () {
        return list;
    }

    /**
     * Adds a {@code String} to the list of argument values.
     * 
     * @param text the {@code String} to add to the list
     */
    public void addList (String text) {
        list.add(text);
    }

    /**
     * Sets the list of argument values for this option.
     * 
     * @param list the list of argument values
     */
    public void setList (List<String> list) {
        this.list = list;
    }

    /**
     * @return the help
     */
    public String getHelp () {
        return help;
    }

    /**
     * @param help the help to set
     */
    public void setHelp (String help) {
        this.help = help;
    }
    
    public void reset () {
        has = false;
        count = 0;
        argValue = null;
        argSet = false;
        if (list != null) {
            list.clear();
        }
    }

    @Override
    public String toString () {
        return String.format("%8.8s %8.8s %12.12s %s %12.12s %s %s %s %s",
                name,
                (altName == null ? " " : altName),
                (fieldName == null ? " " : fieldName),
                (setter == null ? (field == null ? " " : "F") : "S"),
                (argName == null ? " " : argName),
                (argOptional ? "O" : " "),
                (argSetter == null ? (argField == null ? " " : "F") : "S"),
                (repeat ? "R" : " "),
                (help == null ? " " : "H"));
    }

    public int compareTo (ArgOption other) {
        return name.compareTo(other.name);
    }

}
