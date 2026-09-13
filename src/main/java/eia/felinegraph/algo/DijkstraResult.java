package eia.felinegraph.algo;

import java.util.Collections;
import java.util.List;

public final class DijkstraResult {

    public final boolean reachable;
    public final long cost;
    public final List<Integer> path;

    private DijkstraResult(boolean reachable, long cost, List<Integer> path) {
        this.reachable = reachable;
        this.cost = cost;
        this.path = path;
    }

    public static DijkstraResult unreachable() {
        return new DijkstraResult(false, -1, Collections.emptyList());
    }

    public static DijkstraResult of(long cost, List<Integer> path) {
        return new DijkstraResult(true, cost, path);
    }
}
