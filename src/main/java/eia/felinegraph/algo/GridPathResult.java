package eia.felinegraph.algo;

import java.util.Collections;
import java.util.List;

public final class GridPathResult {

    public final boolean reachable;
    public final int moves;
    public final List<int[]> path;

    private GridPathResult(boolean reachable, int moves, List<int[]> path) {
        this.reachable = reachable;
        this.moves = moves;
        this.path = path;
    }

    public static GridPathResult unreachable() {
        return new GridPathResult(false, -1, Collections.emptyList());
    }

    public static GridPathResult of(int moves, List<int[]> path) {
        return new GridPathResult(true, moves, path);
    }
}
