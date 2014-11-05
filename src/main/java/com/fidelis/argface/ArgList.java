/**
 *+
 *  ArgList.java
 *	1.0.0	Apr 12, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

/**
 * @author Leo Hinterlang
 *
 */
public class ArgList {
    private ArgNode home;
    private ArgNode curr;
    
    /**
     * Creates a new empty {@code ArgList}.
     */
    public ArgList () {
    }
    
    public ArgNode getHome () {
    	return home;
    }
    
    /**
     * Returns the current node.
     * 
     * @return the current node
     */
    public ArgNode getCurrent () {
        return curr;
    }
    
    /**
     * Sets the specified node as the current node.
     * 
     * @param node the new current node
     * @return the current node
     */
    public ArgNode setCurrent (ArgNode node) {
        return curr = node;
    }
    
    /**
     * Generates a new {@code ArgNode} and adds it to the east of
     * the current node. The new node becomes the current node.
     * 
     * @param entry the {@code Object} for the new node
     * @return the new node
     */
    public ArgNode addEast (Object entry) {
        ArgNode node = new ArgNode(entry);
        if (home == null) {
            home = node;
        } else {
            curr.setEast(node);
        }
        return curr = node;
    }
    
    public ArgNode addEast (Object entry, boolean optional) {
        ArgNode node = addEast(entry);
        node.setOptional(optional);
        return node;
    }
    
    public ArgNode insertEast (Object entry) {
        ArgNode node = new ArgNode(entry);
        if (home == null) {
            home = node;
        } else {
            ArgNode temp = curr.getEast();
            curr.setEast(node);
            node.setEast(temp);
        }
        return curr = node;
    }
    
    /**
     * Generates a new {@code ArgNode} and adds it to the south of
     * the current node. The new node becomes the current node.
     * 
     * @param entry the {@code Object} for the new node
     * @return the new node
     */
    public ArgNode addSouth (Object entry) {
        ArgNode node = new ArgNode(entry);
        if (home == null) {
            home = node;
        } else {
            curr.setSouth(node);
        }
        return curr = node;
    }
    
    public ArgNode addSouth (Object entry, boolean optional) {
        ArgNode node = addSouth(entry);
        node.setOptional(optional);
        return node;
    }
    
    /**
     * Generates a new {@code ArgNode} and adds it to the end of
     * the list in the southern direction. Beginning with the current
     * node, the new node is added to the south of the most southern
     * node in the list. The current node is then reset to the starting
     * point and returned.
     * 
     * @param entry the {@code Object} for the new node
     * @return the original current node, not the new node
     */
    public ArgNode appendSouth (Object entry) {
        if (home == null) {
            addSouth(entry);
        } else {
            ArgNode save = curr;
            endSouth();
            addSouth(entry);
            curr = save;
        }
        return curr;
    }
    
    public ArgNode appendSouth (Object entry, boolean optional) {
        if (home == null) {
            addSouth(entry, optional);
        } else {
            ArgNode save = curr;
            endSouth();
            addSouth(entry, optional);
            curr = save;
        }
        return curr;
    }
  
    
    /**
     * Navigates to and returns the "home" node.
     * 
     * @return the home node
     */
    public ArgNode goHome () {
        return curr = home;
    }
    
    /**
     * Returns the node to the south of the current node.
     * If this node exists, it becomes the current node.
     * Otherwise, null is returned and the current node does not change.
     * 
     * @return the node to the south of the current node
     */
    public ArgNode  goSouth () {
    	ArgNode node = curr;
        if (node != null) {
            node = curr.getSouth();
            if (node != null) {
            	curr = node;
            }
        }
        return node;
    }
    
    /**
     * Returns the node to the east of the current node.
     * If this node exists, it becomes the current node.
     * Otherwise, null is returned and the current node does not change.
     * 
     * @return the node to the east of the current node
     */
    public ArgNode goEast () {
        ArgNode node = curr;
        if (node != null) {
            node = curr.getEast();
            if (node != null) {
            	curr = node;
            }
        }
        return node;
    }
    
    public ArgNode endEast () {
        if (curr != null) {
            while (goEast() != null) ;
        }
        return curr;
    }
    
    /**
     * Returns the node at the end of the list in the
     * southern direction. The current node is set to the
     * end of the list.
     * 
     * @return the current node at the end of the southern list
     */
    public ArgNode endSouth () {
        if (curr != null) {
            while (goSouth() != null) ;
        }
        return curr;
    }
    
    /**
     * Sets the optional indicator for the current node.
     * 
     * @param optional {@code true} to set the current node as optional
     */
    public void setOptional (boolean optional) {
    	if (curr != null) {
    		curr.setOptional(optional);
    	}
    }
    
    /**
     * Sets the repeat indicator for the current node.
     * 
     * @param repeat {@code true} to set the current node as repeating
     */
    public void setRepeat (boolean repeat) {
    	if (curr != null) {
    		curr.setRepeat(repeat);
    	}
    }

    /**
     * Returns the number of non-option nodes at the base level of this {@code
     * ArgList}.
     * 
     * @return the number of operands at the base level
     */
    public int baseCount () {
        int baseCount = 0;
        for (ArgNode node = home; node != null; node = node.getEast()) {
            if (! node.isOption()) {
                ++baseCount;
            }
        }
        return baseCount;
    }
    
    /**
     * Transfers items to this {@code ArgList} from another {@code ArgList}.
     * The home node of the from list replaces the current node of this list.
     * If the current node was optional, the items added in the southern direction
     * will also be made optional.
     * The current node is set to the last node in the eastern direction.
     * 
     * @param from the other {@code ArgList}
     */
    public void transfer (ArgList from) {
        boolean optional = curr.isOptional();
        ArgNode node = from.home;
        curr.setEast(node.getEast());
        curr.setSouth(node.getSouth());
        curr.setEntry(node.getEntry());
        curr.setRepeat(node.isRepeat());
        if (optional && (curr.getSouth() != null)) {
            curr.setSpec(1);
        }
        ArgNode last = null;
        while ((node = node.getSouth()) != null) {
            last = node;
            node.setOptional(optional);
            if (node.getSpec() == 0) {
                node.setSpec(3);
            }
        }
        if (last != null) {
            last.setSpec(2);
        }
        endEast();
    }
    
    /**
     * Returns the usage text formatted according to this {@code ArgList}.
     * 
     * @return the usage text
     */
    public String buildUsage () {
        StringBuilder sb = new StringBuilder(256);
        sb.append("Usage:\n");
        String prog = home.getOperand().getName();
        int indent = prog.length() + 3;
        for (ArgNode base = home; base != null; base = base.getSouth()) {
            int slen = sb.length();
            sb.append("  ");
            sb.append(prog);
            sb.append(' ');
            buildList(sb, slen, base.getEast(), indent);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    /**
     * Returns a particular usage specification of the complete usage text.
     * 
     * @param base the base {@code ArgNode} for the usage specification
     * @return the usage specification string
     */
    public String buildUsageSpec (ArgNode base) {
        StringBuilder sb = new StringBuilder(128);
        String prog = base.getOperand().getName();
        int indent = prog.length() + 1;
        int slen = 0;
        sb.append(prog);
        sb.append(' ');
        buildList(sb, slen, base.getEast(), indent);
        return sb.toString();
    }
    
    private int buildList (StringBuilder sb, int slen, ArgNode node, int indent) {
        while (node != null) {
            String text = null;
            if (node.isOperand()) {
                text = node.getOperand().getSpecText();
            } else if (node.isOption()) {
                text = node.getOption().getSpecText();
            } else {
                boolean optionalGroup = node.isOptional();
                sb.append(optionalGroup ? '[' : '(');
                slen = buildGroup(sb, slen, node.getGroup(), indent);
                sb.append(optionalGroup ? "] " : ") ");
            }
            if (text != null) {
                if (node.isOptional()) {
                    int spec = node.getSpec();
                    if (spec == 0) {
                        text = "[" + text + "]";
                    } else if (spec == 1) {
                        text = "[" + text;
                    } else if (spec == 2) {
                        text = text + "]";
                    }
                }
                if (node.isRepeat()) {
                    text += "...";
                }
                text += ' ';
                if (sb.length() - slen + text.length() > 80) {
                    char c = remLast(sb);
                    char x = ' ';
                    while (x == ' ') {
                        x = remLast(sb);
                    }
                    sb.append(x);
                    sb.append('\n');
                    slen = sb.length();
                    for (int n = 0; n < indent; n++) {
                        sb.append(' ');
                    }
                    if (c != ' ') {
                        sb.append(c);
                    }
                }
                sb.append(text);
            }
            ArgNode south = node.getSouth();
            if (south != null) {
                sb.append("| ");
                slen = buildList(sb, slen, south, indent);
            }
            node = node.getEast();
        }
        return slen;
    }
    
    private int buildGroup (StringBuilder sb, int slen, ArgList group, int indent) {
        slen = buildList(sb, slen, group.home, indent);
        remLast(sb);
        return slen;
    }
    
    private char remLast (StringBuilder sb) {
        int last = sb.length() - 1;
        char c = sb.charAt(last);
        sb.setLength(last);
        return c;
    }
    
    private void listString (StringBuilder sb, ArgNode node, int indent) {
        for (int n = 0; n < indent; n++) {
            sb.append(' ');
        }
        for (ArgNode next = node; next != null; next = next.getEast()) {
            sb.append(next);
            sb.append('\n');
            ArgNode south = next.getSouth();
            if (south != null) {
                listString(sb, south, indent + 2);
            }
        }
    }
    
    public String brief () {
        StringBuilder sb = new StringBuilder();
        sb.append("ArgList: ");
        for (ArgNode next = home; next != null; next = next.getEast()) {
            ArgOperand operand = next.getOperand();
            if (operand != null) {
                sb.append(operand.getName());
                sb.append(' ');
            } else if (next.isGroup()) {
                sb.append("group ");
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("ArgList\n");
        listString(sb, home, 0);
        return sb.toString();
    }
    
}
