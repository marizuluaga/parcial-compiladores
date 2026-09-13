package eia.felinegraph.algo;

import eia.felinegraph.io.Mission1Case;
import eia.felinegraph.io.Mission1Parser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Mission 1 tests: BFS and DFS on the minefield grid, using the statement's own sample. */
class BfsDfsSolverTest {

    private static final String SAMPLE_INPUT = String.join("\n",
            "10 10",
            "9",
            "0 1 2",
            "1 1 2",
            "2 2 2 9",
            "3 2 1 7",
            "5 3 3 6 9",
            "6 4 0 1 2 7",
            "7 3 0 3 8",
            "8 2 7 9",
            "9 3 2 3 4",
            "0 0",
            "9 9",
            "0 0");

    @Test
    void sampleFromStatement_bfsIsOptimal_dfsIsValidButLonger() {
        List<Mission1Case> cases = Mission1Parser.parse(SAMPLE_INPUT);
        assertEquals(1, cases.size());
        Mission1Case c = cases.get(0);

        GridPathResult bfs = BfsSolver.solve(c.maze, c.startRow, c.startCol, c.destRow, c.destCol);
        GridPathResult dfs = DfsSolver.solve(c.maze, c.startRow, c.startCol, c.destRow, c.destCol);

        assertTrue(bfs.reachable);
        assertTrue(dfs.reachable);
        assertEquals(18, bfs.moves, "BFS must find the statement's optimal 18-move path");
        assertEquals(32, dfs.moves, "DFS with up/down/left/right order must reproduce the statement's 32-move path");
        assertTrue(dfs.moves >= bfs.moves, "DFS can never beat BFS's shortest distance");
    }

    @Test
    void startEqualsDestination_costIsZero() {
        GridMaze maze = new GridMaze(5, 5);
        GridPathResult bfs = BfsSolver.solve(maze, 2, 2, 2, 2);
        GridPathResult dfs = DfsSolver.solve(maze, 2, 2, 2, 2);
        assertEquals(0, bfs.moves);
        assertEquals(0, dfs.moves);
    }

    @Test
    void bombAtStartOrDestination_isUnreachable() {
        GridMaze maze = new GridMaze(3, 3);
        maze.setBomb(0, 0);
        assertFalseReachable(BfsSolver.solve(maze, 0, 0, 2, 2));
        assertFalseReachable(DfsSolver.solve(maze, 0, 0, 2, 2));

        GridMaze maze2 = new GridMaze(3, 3);
        maze2.setBomb(2, 2);
        assertFalseReachable(BfsSolver.solve(maze2, 0, 0, 2, 2));
        assertFalseReachable(DfsSolver.solve(maze2, 0, 0, 2, 2));
    }

    @Test
    void fullyWalledOff_isUnreachable() {
        // A 3x3 grid where the destination corner is sealed by bombs on both approaches.
        GridMaze maze = new GridMaze(3, 3);
        maze.setBomb(1, 2);
        maze.setBomb(2, 1);
        GridPathResult bfs = BfsSolver.solve(maze, 0, 0, 2, 2);
        assertFalseReachable(bfs);
    }

    private void assertFalseReachable(GridPathResult r) {
        assertTrue(!r.reachable, "expected unreachable");
    }
}
