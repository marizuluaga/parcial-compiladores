package eia.felinegraph.algo;


public final class BellmanFordResult {


    public static final long NO_ROUTE = Long.MIN_VALUE / 4;

    public final long[] dist;
    public final boolean[] unbounded;

    public BellmanFordResult(long[] dist, boolean[] unbounded) {
        this.dist = dist;
        this.unbounded = unbounded;
    }

    public boolean isReachable(int v) {
        return dist[v] != NO_ROUTE;
    }
}
