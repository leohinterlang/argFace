package com.fidelis.argface;

/**
 * <p>
 * A {@code TokenSource} is a resource that provides a sequence of tokens.
 * 
 * @author Leo Hinterlang
 *
 */
public interface TokenSource {

    /**
     * Opens this {@code TokenSource}.
     * 
     * @return {@code true} if the open was successful
     */
    public boolean open ();

    /**
     * Closes this {@code TokenSource}.
     * 
     */
    public void close ();

    /**
     * Produces the next token from this {@code TokenSource}.
     * If there are any tokens on the stack, they are delivered first.
     * 
     * @return the next token
     */
    public String next ();

    /**
     * Pushes a token on the stack.
     * 
     * @param token the item to push
     */
    public void push (String token);

    /**
     * Pushes the last token delivered on the stack.
     */
    public void push ();

    /**
     * Pops a token from the stack.
     * If the stack is empty, a {@code null} value is returned.
     * 
     * @return a token or {@code null} if the stack is empty
     */
    public String pop ();

    /**
     * Returns the token at the top of the stack without removing it.
     * If the stack is empty, a {@code null} value is returned.
     * 
     * @return a token or {@code null} if the stack is empty
     */
    public String peek ();

    /**
     * Returns the last token.
     * If there are any tokens on the stack, they will be delivered first.
     * Otherwise, the last token from a {@code next} or {@code pop} operation
     * is returned.
     * 
     * @return the last token
     */
    public String getToken ();

}
