/**
 *+
 *  ArgPattern.java
 *	1.0.0	May 6, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ArgFace Pattern Generation and Matching.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class ArgPattern {

    private ArgList           argList;
    private List<String>      nonOptionList;
    private boolean           patternWatch;
    private String            patternMatch;
    private ArgHelp           help;

    private Deque<ArgList>    listStack      = new ArrayDeque<ArgList>();

    private List<ArgNode>     patternList    = new ArrayList<ArgNode>();
    private List<ArgNode>     saveList       = new ArrayList<ArgNode>();
    private int               prevIndex;
    private ArgNode           failBase;
    private ArgNode           failNode;
    private ArgNode           lastBase;
    private ArgNode           lastFail;
    private int               patternMin;
    private int               patternMax;

    private List<ArgOperand>  targetOperands = new ArrayList<ArgOperand>();
    
    /**
     * Sets the reference to the {@code ArgHelp} object.
     * 
     * @param help the ArgHelp reference
     */
    public void setHelp (ArgHelp help) {
    	this.help = help;
    }
    
    /**
     * Sets the arguments list.
     * 
     * @param argList the argument list
     */
    public void setArgList (ArgList argList) {
        this.argList = argList;
    }

    /**
     * Sets the non option list.
     * 
     * @param nonOptionList the non option list
     */
    public void setNonOptionList (List<String> nonOptionList) {
        this.nonOptionList = nonOptionList;
    }

    /**
     * Sets the pattern watch operating mode.
     * 
     * @param patternWatch {@code true} enables pattern watch mode
     */
    public void setPatternWatch (boolean patternWatch) {
        this.patternWatch = patternWatch;
    }
    
    /**
     * Returns the target operands list.
     * 
     * @return the target operands list
     */
    public List<ArgOperand> getTargetOperands () {
        return targetOperands;
    }
    
    /**
     * Returns the matching pattern text.
     * 
     * @return the matching pattern text
     */
    public String getPatternMatch () {
        return patternMatch;
    }
    
    /**
     * Returns true if the non options match a usage pattern.
     * 
     * @param usageNode the node that starts the usage definition
     * @return {@code true} if a match is found
     */
    public boolean matchUsage (ArgNode usageNode) {
        
        // Get the number of operands.
        // If zero, return success.
        int argCount = nonOptionList.size();
        if (argCount == 0) {
            return true;
        }
        
        // Pattern loop.
        patternMatch = null;
        ArgNode start = usageNode.getEast();
        for (String pat = first(start); pat != null; pat = next(start)) {
            if (pat.isEmpty()) {
                break;
            }
            String text = String.format("%d-%d (%d) %-30.30s %s", patternMin,
                                  patternMax, argCount, patternSpec(pat),
                                  pat);
            trace("pattern", text);
            if (patternWatch) {
                if (patternMin <= argCount && argCount <= patternMax) {
                    System.out.println("Try pattern: " + patternSpec(pat));
                }
            }
            if (match(pat)) {
                patternMatch = patternSpec(pat);
                if (patternWatch) {
                        System.out.println("Pattern match found");
                }
                addTargetOperands(pat);
                return true;
            } else if (failBase == null) {
                break;
            }
        }
        help.addProblem(usageNode, "No matching operand pattern");
        return false;
    }
    
    private String first (ArgNode start) {
        StringBuilder sb = new StringBuilder();
        patternList.clear();
        patternMin = 0;
        patternMax = 0;
        String pattern = buildPattern(start, sb);
        if (patternMax == 0) {
            patternMax = patternMin;
        }
        return pattern;
    }
    
    private String next (ArgNode start) { 
        StringBuilder sb = new StringBuilder();
        String pattern = null;
        nextInit();
        for (boolean done = false; !done; ) {
            pattern = rebuildPattern(start, sb);
            if (pattern == null) {
                if (lastBase == null) {
                    return null;
                }
                trace("next no pat", lastFail.brief());
                failBase = lastBase;
                failNode = lastFail;         
                sb.setLength(0);
                nextInit();
            } else {
                done = true;
            }
        }
        if (patternMax == 0) {
            patternMax = patternMin;
        }
        return pattern;
    }
    
    private void nextInit () {
        saveList.clear();
        saveList.addAll(patternList);
        patternList.clear();
        patternMin = 0;
        patternMax = 0;
        lastBase = null;
        lastFail = null;
        prevIndex = 0;
    }
    
    private boolean match (String pattern) {
        trace("match", pattern);
        for (ArgNode node : patternList) {
            if (node == null) {
                trace("patternList", "null");
            } else if (node.isOperand()) {
                trace("patternList", node.toString());
            } else if (node.isGroup()) {
                trace("patternList", node.brief());
            }
        }
        int argIndex = 0;
        int argCount = nonOptionList.size();
        ArgNode node = null;
        String arg = null;
        failBase = null;
        failNode = null;
        int nodeIndex = 0;
        for (int n = 0; n < pattern.length(); n++) {
            char c = pattern.charAt(n);

            // Literal.
            if (c == 'L') {

                // Get the literal text.
                node = patternList.get(nodeIndex++);
                ArgOperand operand = node.getOperand();
                String literal = operand.getName();

                // Next non-option argument.
                if (argIndex >= argCount) {
                    failNode = node;
                    if (failBase == null) {
                        failBase = failNode;
                    }
                    return false;
                }
                arg = nonOptionList.get(argIndex++);
                trace("compare", literal + " to " + arg);

                // Ignore case. Not a match.
                if (!arg.equalsIgnoreCase(literal)) {
                    failNode = node;
                    if (failBase == null) {
                        failBase = failNode;
                    }
                    return false;
                }
            }

            // Variable.
            else if (c == 'V') {
            	
            	// Get the variable node.
            	node = patternList.get(nodeIndex++);

                // Accept the next non-option argument.
                if (argIndex >= argCount) {
                    failNode = node;
                    if (failBase == null) {
                        failBase = failNode;
                    }
                    return false;
                }
                arg = nonOptionList.get(argIndex++);
                trace("Variable", arg);
            }
            
            // Base node.
            else if (c == 'b') {
                failBase = patternList.get(nodeIndex++);
            }
            
            // Group node.
            else if (c == 'g') {
                ++nodeIndex;
            }
            
            // Reduced optional argument.
            else if (c == 'x') {
                ++nodeIndex;
            }
        }
        if (patternMin > argCount || argCount > patternMax) {
            failBase = patternList.get(0);
            failNode = failBase;
            return false;
        }
        return true;
    }
    
    private String buildPattern (ArgNode start, StringBuilder sb) {
        argList.setCurrent(start);
        for (ArgNode node = start; node != null; node = argList.goEast()) {
            if (node.isOperand()) {
                buildOperand(node, sb);
            } else if (node.isGroup()) {
                buildGroup(node, sb, false);
            }
        }
        return sb.toString();
    }

    private String rebuildPattern (ArgNode start, StringBuilder sb) {
        if (failBase == null) {
            trace("rebuild no base", "none");
        } else {
            trace("rebuild Base", failBase.brief());
        }
        if (failNode == null) {
            trace("rebuild no fail", "none");
        } else {
            trace("rebuild Fail", failNode.brief());
        }
        boolean showNext = false;
        argList.setCurrent(start);
        ArgOperand operand = null;
        for (ArgNode node = start; node != null; node = argList.goEast()) {
            if (showNext) {
                showNext = false;
                trace("next node", node.brief());
            }
            if (node == failBase) {
                trace("fail Base", failBase.brief());
                if (failNode == null) {
                    trace("fail Node", "NULL");
                } else {
                    trace("fail Node", failNode.brief());
                }
                ArgNode south = failNode.getSouth();
                if (south != null) {
                    trace("fail south", south.brief());
                    if (south.isOperand()) {
                        buildOperand(south, sb, failBase);
                    } else if (south.isGroup()) {
                        trace("fail group", sb.toString());
                        buildGroup(south, sb, true);
                    }
                }
                
                // South is null.
                else {
                	trace("south is NULL", failNode.brief());
                	
                	// Fail node optional?
                    if (failNode.isOptional()) {
                        trace("end alternatives", sb.toString());
                        showNext = true;
                        buildBase(failBase, sb);
                        buildReduce(failNode, sb);
                        continue;
                    }
                    return null;
                }
            }
            
            // Not the fail base node.
            else {
               if (node.isOperand()) {
                   trace("not fail base", node.brief());
                   operand = node.getOperand();
                   if (operand.isLiteral()) {
                       if (operand.getName().equalsIgnoreCase("options")) {
                           continue;
                       }
                       lastBase = node;
                       lastFail = node;
                       ArgNode prevBase = null;
                       ArgNode prevNode = null;
                       trace("prevIndex", "" + prevIndex);
                       if (saveList.size() > prevIndex + 1) {
                           prevBase = saveList.get(prevIndex);
                           prevNode = saveList.get(prevIndex + 1);
                           if (prevBase == null) {
                               trace("prev Base", null);
                           } else {
                               trace("prev Base", prevBase.brief());
                           }
                           if (prevNode == null) {
                               trace("prev Node", null);
                           } else {
                               trace("prev Node", prevNode.brief());
                           }
                       }
                       if (prevBase == node) {
                           buildBase(prevBase, sb);
                           buildLiteral(prevNode, sb);
                       } else {
                           buildBase(node, sb);
                           buildLiteral(node, sb);
                       }
                   } else {
                	   if (node.isOptional()) {
                		   trace("optional", node.brief());
                		   lastBase = node;
                		   lastFail = node;
                	   }
                       buildVariable(node, sb);
                   }
               } else if (node.isGroup()) {
                   lastBase = node;
                   lastFail = node;
                   ArgNode prevGroup = null;
                   if (saveList.size() > prevIndex) {
                       prevGroup = saveList.get(prevIndex);
                       lastFail = prevGroup;
                       trace("prev Group", prevGroup.brief());
                   }
                   String pattern = null;
                   if (prevGroup == node) {
                       pattern = buildGroup(node, sb, true);
                   } else {
                       pattern = buildGroup(prevGroup, sb, true);
                   }
                   trace("rebuild group", pattern);
                   if (pattern == null) {
                       return null;
                   }
               }
            }
        }
        return sb.toString();
    }
    
    private void buildOperand (ArgNode node, StringBuilder sb) {
        
        // Get the operand.
        ArgOperand operand = node.getOperand();
        
        // Literal operands are preceded by a base node.
        // Initially, the base node is the same as the literal node that follows.
        if (operand.isLiteral()) {
            
            // Literal operand named "options" is not part of the pattern.
            if (operand.getName().equalsIgnoreCase("options")) {
                return;
            }
            buildBase(node, sb);
            buildLiteral(node, sb);
        } else {
            buildVariable(node, sb);
        }
    }
    
    private void buildOperand (ArgNode node, StringBuilder sb, ArgNode base) {
        
        // Get the operand.
        ArgOperand operand = node.getOperand();
        
        // Literal operands are preceded by a base node.
        // On a rebuild, the base may not be the same as the node.
        if (operand.isLiteral()) {
            
            // Literal operand named "options" is not part of the pattern.
            if (operand.getName().equalsIgnoreCase("options")) {
                return;
            }
            buildBase(base, sb);
            buildLiteral(node, sb);
        } else {
            buildVariable(node, sb);
        }
    }
    
    private void buildBase (ArgNode node, StringBuilder sb) {
        patternList.add(node);
        sb.append('b');
        ++prevIndex;
    }
    
    private void buildLiteral (ArgNode node, StringBuilder sb) {
        ArgOperand operand = node.getOperand();
        boolean ender = false;
        if (operand.getName().equals("$END$")) {
            ender = true;
            trace("literal end", node.toString());
        }
        patternList.add(node);
        if (ender) {
            sb.append('x');
        } else {
            sb.append('L');
            adjustCount(node);
        }
        ++prevIndex;
    }
    
    private void buildReduce (ArgNode node, StringBuilder sb) {
        ArgOperand operand = new ArgOperand().literal();
        operand.setName("$END$");
        ArgNode reduce = new ArgNode(operand);
        patternList.add(reduce);
        sb.append('x');
        ++prevIndex;
    }
    
    private void buildVariable (ArgNode node, StringBuilder sb) {
        patternList.add(node);
        sb.append('V');
        adjustCount(node);
        ++prevIndex;
    }
    
    private void adjustCount (ArgNode node) {
    	++patternMin;
        if (node.isRepeat()) {
            patternMax = 999;
        }
    }
    
    private String buildGroup (ArgNode node, StringBuilder sb, boolean rebuild) {
        patternList.add(node);
        sb.append('g');
        ++prevIndex;
        ArgList group = node.getGroup();
        listStack.push(argList);
        argList = group;
        String pattern = null;
        if (rebuild) {
            pattern = rebuildPattern(argList.goHome(), sb);
        } else {
            pattern = buildPattern(argList.goHome(), sb);
        }
        argList = listStack.pop();
        return pattern;
    }

    private char remLast (StringBuilder sb) {
        int last = sb.length() - 1;
        if (last < 0) {
            return '\0';
        }
        char c = sb.charAt(last);
        sb.setLength(last);
        return c;
    }

    private String patternSpec (String pattern) {
        StringBuilder sb = new StringBuilder();
        ArgOperand operand = null;
        int n = 0;
        for (ArgNode node : patternList) {
            char c = pattern.charAt(n++);
            boolean trailers = false;
            if (c == 'L') {
                if (node.isOptional()) {
                    sb.append('[');
                }
                operand = node.getOperand();
                String literal = operand.getName();
                sb.append(literal);
                trailers = true;
            } else if (c == 'V') {
                if (node.isOptional()) {
                    sb.append('[');
                }
                operand = node.getOperand();
                String name = operand.getName();
                sb.append('<');
                sb.append(name);
                sb.append('>');
                trailers = true;
            }
            if (trailers) {
                if (node.isOptional()) {
                    sb.append(']');
                }
                if (node.isRepeat()) {
                    sb.append("...");
                }
                sb.append(' ');
            }
        }
        remLast(sb);
        return sb.toString();
    }
    
    private void addTargetOperands (String pattern) {
        int argIndex = 0;
        String arg = null;
        ArgNode node = null;
        Debug.trace("PATTERN: " + pattern);
        for (int n = 0; n < pattern.length(); n++) {
            node = null;
            char c = pattern.charAt(n);
            if (c == 'L') {
                node = patternList.remove(0);
            } else if (c == 'V') {
                node = patternList.remove(0);
            } else if (c == 'b' || c == 'x' || c == 'g') {
                patternList.remove(0);
            }
            if (node != null) {
                ArgOperand operand = node.getOperand();
                if (operand != null) {
                    if (operand.isRepeat()) {
                        int argsLeft = nonOptionList.size() - argIndex;
                        int operandsLeft = patternMin - argIndex;
                        while (argsLeft >= operandsLeft) {
                            --argsLeft;
                            arg = nonOptionList.get(argIndex++);
                            Debug.trace(String.format("rep: %15.15s -> %s",
                                    arg, operand));
                            takeOperand(operand, arg);
                        }
                    } else {
                        arg = nonOptionList.get(argIndex++);
                        Debug.trace(String.format("arg: %15.15s -> %s", arg,
                                operand));
                        takeOperand(operand, arg);
                    }
                }
            }
        }
    }

    private boolean takeOperand (ArgOperand operand, String arg) {
        if (operand.isVariable()) {
            operand.setHas(true);
            operand.setValue(arg);
            operand.setCount(operand.getCount() + 1);
            if (operand.isRepeat()) {
                operand.addList(arg);
            }
            targetOperands.add(operand);
            return true;
        } else if (operand.isLiteral()) {
            if (operand.getName().equalsIgnoreCase(arg)) {
                operand.setHas(true);
                operand.setValue(arg);
                operand.setCount(operand.getCount() + 1);
                targetOperands.add(operand);
                return true;
            }
        }
        return false;
    }

    
    private void trace (String id, String text) {
        Debug.trace(String.format("%15.15s : %s", id, text));
    }
}
