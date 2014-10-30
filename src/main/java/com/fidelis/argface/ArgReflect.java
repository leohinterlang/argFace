/**
 *+
 *  ArgReflect.java
 *	1.0.0	Apr 8, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Command Line Arguments Reflection Class.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 * 
 */
public class ArgReflect {
    private Object            object;
    private Class<?>          objectClass;
    private boolean           privateAccess;
    
    /**
     * No argument constructor.
     */
    public ArgReflect () {
    }
    
    /**
     * Constructor that sets the object to be accessed through reflection.
     * 
     * @param pojo the Object accessed through reflection
     */
    public ArgReflect (Object pojo) {
    	setObject(pojo);
    }
    
    /**
     * Sets the {@code Object} to be accessed through reflection.
     * 
     * @param object the reflection object
     */
    public void setObject (Object object) {
        this.object = object;
        this.objectClass = object.getClass();
    }
    
    /**
     * Returns the {@code Object} being accessed through reflection.
     * 
     * @return the reflection object
     */
    public Object getObject () {
    	return object;
    }
    
    /**
     * Sets the "privateAccess" operating mode.
     * 
     * @param privateAccess {@code true} to allow access to private fields
     */
    public void setPrivateAccess (boolean privateAccess) {
        this.privateAccess = privateAccess;
    }
    
    /**
     * Returns a {@code String} from the named variable.
     * If there is a getter method for the variable, that will be
     * used to retrieve the value. Otherwise, access will be tried
     * by using the variable's {@code Field} directly.
     * <p>
     * If neither a getter nor a field is found for the variable a
     * {@code null} value is returned.
     * 
     * @param name the variable name
     * @return the text from the variable or null
     */
    String getString (String name) {
        Method getter = findGetString(name);
        if (getter == null) {
            getter = findGetter(name, String[].class);
            if (getter != null) {
                return getStringArray(getter, newlines(name));
            }
        } else {
            return getString(getter);
        }
        Field field = findField(name, String.class);
        if (field == null) {
            field = findField(name, String[].class);
            if (field != null) {
                return getStringArray(field, newlines(name));
            }
        } else {
            return getString(field);
        }
        return null;
    }

    private boolean newlines (String name) {
        boolean newlines = false;
        if (name.endsWith("Text")) {
            newlines = true;
        }
        return newlines;
    }
    
    /**
     * Returns a {@code String} from a getter {@code Method}.
     * 
     * @param getter the {@code Method} for the getter
     * @return the text returned by the getter or null
     */
    String getString (Method getter) {
        String value = null;
        try {
            value = (String) getter.invoke(object);
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Returns a {@code String} from a {@code Field} for a variable.
     * 
     * @param field the {@code Field} for the variable
     * @return the text from the variable or null
     */
    String getString (Field field) {
        String value = null;
        try {
            value = (String) field.get(object);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return value;
    }
    
    /**
     * Returns a {@code String} from a getter method which yields a String array.
     * The Strings from the array are appended together with an optional
     * newline between each element.
     * 
     * @param getter the getter {@code Method}
     * @param newlines optional newline inclusion
     * @return the appended text
     */
    String getStringArray (Method getter, boolean newlines) {
        String value = null;
        try {
            String [] array = (String []) getter.invoke(object);
            value = stringFromArray(array, newlines);
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }
    
    /**
     * Returns a {@code String} from a String array variable.
     * The Strings from the array are appended together with an optional
     * newline between each element.
     * 
     * @param field the variable {@code Field}
     * @param newlines optional newline inclusion
     * @return the appended text
     */
    String getStringArray (Field field, boolean newlines) {
        String value = null;
        try {
            String [] array = (String []) field.get(object);
            value = stringFromArray(array, newlines);
        } catch (IllegalArgumentException ex) {
        } catch (IllegalAccessException ex) {
        }
        return value;
    }
    
    private String stringFromArray (String [] array, boolean newlines) {
        StringBuilder sb = new StringBuilder();
        String nl = "";
        if (newlines) {
            nl = "\n";
        }
        for (String s : array) {
            sb.append(s);
            sb.append(nl);
        }
        return sb.toString();
    }
    
    /**
     * Returns a {@code Boolean} value for the named boolean variable.
     * If a getter of the form, {@code isVariable} is found, it is used to
     * retrieve the value. Otherwise, the name is used to access the field
     * of the variable directly. Any problems will return a {@code null} value.
     * 
     * @param name the variable name
     * @return the {@code Boolean} value or null
     */
    Boolean getBoolean (String name) {
        Method getter = findIsBoolean(name);
        if (getter != null) {
            return getBoolean(getter);
        }
        Field field = findField(name, boolean.class);
        if (field != null) {
            return getBoolean(field);
        }
        return null;
    }
    
    /**
     * Returns a {@code Boolean} value using a getter method.
     * 
     * @param getter the getter {@code Method}
     * @return a {@code Boolean} value or null
     */
    Boolean getBoolean (Method getter) {
        Boolean value = null;
        try {
            value = (Boolean) getter.invoke(object);
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }
    
    /**
     * Returns a {@code Boolean} value from a {@code Field}.
     * 
     * @param field the value {@code Field}
     * @return a {@code Boolean} value or null
     */
    Boolean getBoolean (Field field) {
        Boolean value = null;
        try {
            value = field.getBoolean(object);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return value;
    }
    
    Integer getInteger (String name) {
    	Method getter = findGetInteger(name);
    	if (getter != null) {
    		return getInteger(getter);
    	}
    	Field field = findField(name, int.class);
    	if (field != null) {
    		return getInteger(field);
    	}
    	return null;
    }
    
    Integer getInteger (Method getter) {
    	Integer value = null;
    	try {
			value = (Integer) getter.invoke(object);
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    	return value;
    }
    
    Integer getInteger (Field field) {
    	Integer value = null;
    	try {
			value = (Integer) field.getInt(object);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
    	return value;
    }

    /**
     * Finds a getter {@code Method} for a named variable that returns a {@code
     * String}.
     * 
     * @param name the variable name for the getter
     * @return the getter {@code Method} or null
     */
    Method findGetString (String name) {
        return findGetter(name, String.class);
    }

    /**
     * Finds a getter {@code Method} ("is") for a named variable that returns
     * a {@code boolean} value.
     * 
     * @param name the variable name for the getter
     * @return the getter {@code Method} or null
     */
    Method findIsBoolean (String name) {
        String methodName = ArgUtil.camelCase("is", name);
        return findMethod(methodName, boolean.class);
    }
    
    /**
     * Finds a getter {@code Method} for a named variable that returns
     * an {@code int} value.
     * 
     * @param name the variable name for the getter
     * @return the getter {@code Method} of null
     */
    Method findGetInteger (String name) {
    	return findGetter(name, int.class);
    }

    /**
     * Finds a getter {@code Method} for a named variable that returns the
     * specified {@code Class}.
     * 
     * @param name the variable name for the getter
     * @param returnClass the {@code Class} that the getter returns
     * @return the getter {@code Method} or null
     */
    public Method findGetter (String name, Class<?> returnClass) {
        String methodName = ArgUtil.camelCase("get", name);
        return findMethod(methodName, returnClass);
    }

    /**
     * Finds a setter {@code Method} for the named variable with a matching
     * parameter type.
     * 
     * @param fieldName the name of the variable
     * @param paramClass the class of the variable to be set
     * @return the setter {@code Method} or null
     */
    public Method findSetter (String fieldName, Class<?> paramClass) {
        String methodName = ArgUtil.camelCase("set", fieldName);
        return findOneParam(methodName, paramClass);
    }
    
    /**
     * Finds a {@code Method} with one parameter of a given type.
     * 
     * @param methodName the name of the {@code Method}
     * @param paramClass the class of the parameter
     * @return the {@code Method} or null
     */
    public Method findOneParam (String methodName, Class<?> paramClass) {
        Method method = null;
        Class<?> [] paramTypes = new Class<?>[1];
        paramTypes[0] = paramClass;
        try {
            method = objectClass.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }
        return method;
    }
    
    /**
     * Finds a named {@code Method} with a given return type. If the return
     * class parameter is {@code null}, the {@code Method} is returned
     * regardless of its type.
     * 
     * @param methodName the name of the method
     * @param returnClass the class of the return value or null
     * @return the {@code Method} or null
     */
    public Method findMethod (String methodName, Class<?> returnClass) {
        Method method = null;
        try {
            method = objectClass.getMethod(methodName);
            if (returnClass != null) {
                Class<?> c = method.getReturnType();
                if (c != returnClass) {
                    method = null;
                }
            }
        } catch (NoSuchMethodException ex) {
        } catch (SecurityException ex) {
        }
        return method;
    }

    /**
     * Finds a {@code Field} corresponding to a named variable and matching the
     * specified class for the variable. If the class parameter is {@code null},
     * the type of the variable is not checked and will return the {@code Field}
     * regardless of its type.
     * 
     * @param fieldName the name of the variable
     * @param fieldClass the class of the variable or null
     * @return the {@code Field} for the variable
     */
    public Field findField (String fieldName, Class<?> fieldClass) {
        Field field = null;
        try {
            if (privateAccess) {
                field = objectClass.getDeclaredField(fieldName);
            } else {
                field = objectClass.getField(fieldName);
            }
            if (fieldClass != null) {
                Class<?> c = field.getType();
                if (c != fieldClass) {
                    field = null;
                }
            }
        } catch (NoSuchFieldException e) {
        } catch (SecurityException e) {
        }
        if ((field != null) && privateAccess) {
            try {
                int mods = field.getModifiers();
                if ((mods & Modifier.PRIVATE) != 0) {
                    field.setAccessible(true);
                }
            } catch (SecurityException e) {
                ArgUtil.printError("Security exception - private field: " + fieldName);
                field = null;
            }
        }
        return field;
    }
    
    /**
     * Sets the value of a variable using its setter {@code Method}.
     * 
     * @param setter the {@code Method} to invoke
     * @param value the {@code Object} holding the value to set
     * @return {@code true} if successful
     */
    public boolean setValue (Method setter, Object value) {
        boolean status = false;
        try {
            setter.invoke(object, value);
            status = true;
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
            System.out.println("Method \"" + setter.getName() +
                    "\" argument of wrong type. Should be: " +
                    value.getClass().getName());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return status;
    }
    
    /**
     * Sets the value of a variable by its {@code Field}.
     * 
     * @param field the {@code Field} of the variable
     * @param value the {@code Object} containing the value to set
     * @return {@code true} if successful
     */
    public boolean setValue (Field field, Object value) {
        boolean status = false;
        try {
            field.set(object, value);
            status = true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return status;
    }
    
}
