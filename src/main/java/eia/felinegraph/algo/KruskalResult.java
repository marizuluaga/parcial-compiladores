package eia.felinegraph.algo;

import java.util.List;


public final class KruskalResult {

    public final boolean connected;
    public final long totalCost;
    public final List<WeightedEdge> mstEdges;

    public KruskalResult(boolean connected, long totalCost, List<WeightedEdge> mstEdges) {
        this.connected = connected;
        this.totalCost = totalCost;
        this.mstEdges = mstEdges;
    }
}
