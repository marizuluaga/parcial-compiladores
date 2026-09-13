package eia.felinegraph.algo;


public final class FloydWarshallResult {

    
    public static final long NO_ROUTE = Long.MIN_VALUE / 4;

    public final long[][] dist;
    public final boolean[][] unbounded;
    /** next[i][j] = the node visited right after i on the reconstructed max-churun walk to j, or -1 if none is known. */
    public final int[][] next;

    public FloydWarshallResult(long[][] dist, boolean[][] unbounded, int[][] next) {
        this.dist = dist;
        this.unbounded = unbounded;
        this.next = next;
    }
}
