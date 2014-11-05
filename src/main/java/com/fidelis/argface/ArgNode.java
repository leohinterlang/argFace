/**
 *+
 *  ArgNode.java
 *	1.0.0	Apr 12, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * Arguments Node.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgNode {
    
    /** Link to the south node */
    private ArgNode south;
    
    /** Link to the east node */
    private ArgNode east;
    
    /** The object held by this node */
    private Object entry;
    
    /** A flag indicating this node as optional */
    private boolean optional;
    
    /** A flag indicating this node as repeating */
    private boolean repeat;
    
    /** Specification options */
    private int spec;
    
    /**
     * Creates a new empty {@code ArgNode}.
     */
    public ArgNode () {
    }
    
    /**
     * Creates a new {@code ArgNode} with the specified entry.
     * 
     * @param entry the {@code Object} entry held by this node
     */
    public ArgNode (Object entry) {
        this.entry = entry;
    }
    
    /**
     * Returns {@code true} if this node contains an operand.
     * 
     * @return {@code true} if this node contains an operand
     */
    public boolean isOperand () {
        return entry instanceof ArgOperand;
    }
    
    /**
     * Returns {@code true} if this node contains an option.
     * 
     * @return {@code true} if this node contains an option
     */
    public boolean isOption () {
        return entry instanceof ArgOption;
    }
    
    /**
     * Returns {@code true} if this node contains a group.
     * 
     * @return {@code true} if this node contains a group
     */
    public boolean isGroup () {
        return entry instanceof ArgList;
    }
    
    /**
     * Returns this entry as an {@code ArgOperand} or null.
     * The entry is checked to see if it is an operand.
     * If it is, the operand is returned. Otherwise, {@code null}
     * is returned.
     * 
     * @return the operand or null
     */
    public ArgOperand getOperand () {
        if (isOperand()) {
            return (ArgOperand) entry;
        }
        return null;
    }
    
    /**
     * Returns this entry as an {@code ArgOption} or null.
     * The entry is checked to see if it is an option.
     * If it is, the option is returned. Otherwise, {@code null}
     * is returned.
     * 
     * @return the {@code ArgOption} or null
     */
    public ArgOption getOption () {
        if (isOption()) {
            return (ArgOption) entry;
        }
        return null;
    }
    
    /**
     * Returns this entry as an {@code ArgList} group or null.
     * The entry is checked to see if it is a group.
     * If it is, the group is returned. Otherwise, {@code null}
     * is returned.
     * 
     * @return the {@code ArgList} group or null
     */
    public ArgList getGroup () {
        if (isGroup()) {
            return (ArgList) entry;
        }
        return null;
    }

    /**
     * Returns the {@code ArgNode} to the south.
     * 
     * @return the node to the south
     */
    public ArgNode getSouth () {
        return south;
    }

    /**
     * Sets the {@code ArgNode} to the south.
     * 
     * @param south the node to the south
     */
    public void setSouth (ArgNode south) {
        this.south = south;
    }

    /**
     * Returns the {@code ArgNode} to the east.
     * 
     * @return the node to the east
     */
    public ArgNode getEast () {
        return east;
    }

    /**
     * Sets the {@code ArgNode} to the east.
     * 
     * @param east the node to the east
     */
    public void setEast (ArgNode east) {
        this.east = east;
    }

    /**
     * Returns the {@code Object} entry for this node.
     * 
     * @return the entry for this node
     */
    public Object getEntry () {
        return entry;
    }

    /**
     * Sets the {@code Object} entry for this node.
     * 
     * @param entry the entry for this node
     */
    public void setEntry (Object entry) {
        this.entry = entry;
    }

    /**
     * Returns {@code true} if this node is optional.
     * 
     * @return {@code true} if this node is optional
     */
    public boolean isOptional () {
        return optional;
    }

    /**
     * Sets the indicator that this node is optional.
     * 
     * @param optional {@code true} to set this node as optional
     */
    public void setOptional (boolean optional) {
        this.optional = optional;
    }

    /**
     * Returns {@code true} if this node is repeatable.
     * 
     * @return {@code true} if this node is repeatable
     */
    public boolean isRepeat () {
        return repeat;
    }

    /**
     * Sets the indicator that this node is repeatable.
     * 
     * @param repeat {@code true} sets this node as repeatable
     */
    public void setRepeat (boolean repeat) {
        this.repeat = repeat;
    }
    
    /**
     * @return the spec
     */
    public int getSpec () {
        return spec;
    }

    /**
     * @param spec the spec to set
     */
    public void setSpec (int spec) {
        this.spec = spec;
    }

    public String brief () {
        if (isGroup()) {
            return getGroup().brief();
        }
        return toString();
    }
   
    @Override
    public String toString () {
        return String.format("%s %s %s %s %s %s",
                (south == null ? " " : "S"),
                (east  == null ? " " : "E"),
                (entry instanceof ArgOption ? "Option: " :
                    (entry instanceof ArgOperand ? "Operand:" : "Group:  ")),
                (optional ? "O" : " "),
                (repeat ? "R" : " "),
                entry);
    }

}
