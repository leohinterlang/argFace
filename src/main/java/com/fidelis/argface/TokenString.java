/**
 *+
 *  TokenString.java
 *  1.0.0   Mar 12, 2014  Leo Hinterlang
 *-
 */
package com.fidelis.argface;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.StringTokenizer;

/**
 * An implementation of {@code TokenSource} based on a String.
 * 
 * @version 1.0.0
 * @author Leo Hinterlang
 *
 */
public class TokenString implements TokenSource {
    private String          text;
    private String          delimiters;
    private StringTokenizer tokenizer;
    private String          token;
    private String          meta;
    private Deque<String>   stack            = new ArrayDeque<String>();
    private boolean         metaQuotes       = true;
    private boolean         backslash;
    private boolean         filterLineBreaks = true;
    private boolean         trimSpaces       = true;
    
    /**
     * Creates a new {@code TokenString} given the text string
     * and the delimiter set.
     * 
     * @param text
     * @param delimiters
     */
    public TokenString (String text, String delimiters) {
        this.text = text;
        this.delimiters = delimiters;
        this.tokenizer = new StringTokenizer(this.text, this.delimiters, true);
    }

    /**
     * @return the filterLineBreaks
     */
    public boolean isFilterLineBreaks () {
        return filterLineBreaks;
    }

    /**
     * @param filterLineBreaks the filterLineBreaks to set
     */
    public void setFilterLineBreaks (boolean filterLineBreaks) {
        this.filterLineBreaks = filterLineBreaks;
    }

    /**
     * @return the trimSpaces
     */
    public boolean isTrimSpaces () {
        return trimSpaces;
    }

    /**
     * @param trimSpaces the trimSpaces to set
     */
    public void setTrimSpaces (boolean trimSpaces) {
        this.trimSpaces = trimSpaces;
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#close()
     */
    public void close () {
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#getToken()
     */
    public String getToken () {
        return token;
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#next()
     */
    public String next () {
        token = pop();
        if (token != null) {
            if (isMeta(token)) {
                meta = token;
                token = stack.pop();
                return meta;
            }
        } else {
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken();
                if (filterLineBreaks == false) {
                    if (token.equals("\n")) {
                        return token;
                    }
                }
                if (trimSpaces) {
                    token = token.trim();
                }
                if (!token.isEmpty()) {
                    return metaFilter();
                }
            }
        }
        return token;
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#open()
     */
    public boolean open () {
        return true;
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#peek()
     */
    public String peek () {
        token = next();
        push();
        return token;
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#pop()
     */
    public String pop () {
        if (stack.size() > 0) {
            return stack.pop();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#push(java.lang.String)
     */
    public void push (String token) {
        stack.push(token);
    }

    /* (non-Javadoc)
     * @see com.fidelis.fsm.TokenSource#push()
     */
    public void push () {
        stack.push(token);
        if (meta != null) {
            stack.push(meta);
            meta = null;
        }
    }
    
    private boolean isMeta (String token) {
        if (token.startsWith("<") && token.endsWith(">")) {
            return true;
        }
        return false;
    }

    /**
     * Filters inputs that return meta-production tokens. When the metaFilter
     * option is enabled, this method checks for special input specifications
     * and returns a meta-production token as an indicator of the content. In
     * particular, a double quoted string will return {@code <dQuote>} and a
     * single quoted string will return {@code <sQuote>}. To retrieve the text,
     * use {@link #getToken()}.
     * 
     * @return the meta-production token or the original token unchanged
     */
    private String metaFilter () {
        if (metaQuotes) {
            if (token.startsWith("\"")) {
                token = stringToken();
                return meta = "<dQuote>";
            } else if (token.startsWith("\'")) {
                token = stringToken();
                return meta = "<sQuote>";
            } else {
                meta = null;
            }
        }
        return token;
    }
    
    /**
     * Returns the complete text of a quoted string but without the quotation marks.
     * If there are any quotation marks that are escaped with the backslash, and that
     * match the starting quote, the slash is removed but the quote remains and the scan
     * continues.
     * <p><pre>
     *  "this has an \"escaped\" set of quotes"
     *  this has an "escaped" set of quotes
     * </pre></p>
     * 
     * @return the processed text
     */
    private String stringToken () {
        String text = token;
        backslash = false;
        char start = text.charAt(0);
        StringBuilder sb = new StringBuilder(64);
        boolean done = false;
        if (text.length() > 1) {
            text = text.substring(1);
            done = escapeFilter(text, sb, start);
        }
        while (! done) {
            if (tokenizer.hasMoreTokens()) {
                text = tokenizer.nextToken();
                done = escapeFilter(text, sb, start);
            } else {
                done = true;
            }
        }
        return sb.toString();
    }
    
    /**
     * Filters escaped quotes from tokens added to a {@code StringBuilder} until a closing
     * quote is encountered. The return value is {@code true} when the closing quote is found
     * without a backslash escape character before it.
     * 
     * @param text the next token to add to the {@code StringBuilder}
     * @param sb the {@code StringBuilder}
     * @param start the starting quote
     * @return {@code true} if the closing quote is found
     */
    private boolean escapeFilter (String text, StringBuilder sb, char start) {
        boolean status = false;
        char [] copy = text.toCharArray();
        for (int n = 0; n < copy.length; n++) {
            char c = copy[n];
            if (backslash) {
                backslash = false;
                if (c == start) {
                    sb.setLength(sb.length() - 1);      // overwrite backslash
                }
            } else if (c == '\\') {
                backslash = true;
            } else if (c == start) {
                status = true;
                break;
            }
            sb.append(c);
        }
        return status;
    }
}
