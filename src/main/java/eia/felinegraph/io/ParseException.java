package eia.felinegraph.io;

/**
 * Thrown whenever the pasted input does not match a mission's expected format.
 * The message is meant to be shown directly in the GUI (never as a raw stack
 * trace), so it always names what was expected and, where possible, which test
 * case triggered the problem.
 */
public final class ParseException extends RuntimeException {
    public ParseException(String message) {
        super(message);
    }
}
