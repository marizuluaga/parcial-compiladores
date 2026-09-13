package eia.felinegraph.io;

/**
 * Reads the pasted input as a stream of whitespace-separated tokens, as required
 * by the general input rules: no fixed number of tokens per line, extra blank
 * lines and trailing spaces must be tolerated. Splitting the whole trimmed input
 * on any run of whitespace (spaces, tabs, newlines) satisfies exactly that.
 */
public final class TokenScanner {

    private final String[] tokens;
    private int pos = 0;

    public TokenScanner(String input) {
        String trimmed = input == null ? "" : input.trim();
        this.tokens = trimmed.isEmpty() ? new String[0] : trimmed.split("\\s+");
    }

    public boolean hasNext() {
        return pos < tokens.length;
    }

    public String nextToken(String what) {
        if (!hasNext()) {
            throw new ParseException("The input ended before " + what + " could be read.");
        }
        return tokens[pos++];
    }

    public int nextInt(String what) {
        String t = nextToken(what);
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException ex) {
            throw new ParseException("Expected an integer for " + what + " but found \"" + t + "\".");
        }
    }

    public long nextLong(String what) {
        String t = nextToken(what);
        try {
            return Long.parseLong(t);
        } catch (NumberFormatException ex) {
            throw new ParseException("Expected an integer for " + what + " but found \"" + t + "\".");
        }
    }

    public void expectRange(long value, long min, long max, String what) {
        if (value < min || value > max) {
            throw new ParseException(what + " = " + value + " is out of the allowed range [" + min + ", " + max + "].");
        }
    }
}
