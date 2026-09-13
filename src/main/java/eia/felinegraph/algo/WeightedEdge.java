package eia.felinegraph.algo;


public final class WeightedEdge {

    public final int from;
    public final int to;
    public final long weight;

    public WeightedEdge(int from, int to, long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
