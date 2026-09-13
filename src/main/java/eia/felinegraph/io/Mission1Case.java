package eia.felinegraph.io;

import eia.felinegraph.algo.GridMaze;

/** One parsed Mission 1 test case: a grid, a start cell and a destination cell. */
public final class Mission1Case {
    public final GridMaze maze;
    public final int startRow;
    public final int startCol;
    public final int destRow;
    public final int destCol;

    public Mission1Case(GridMaze maze, int startRow, int startCol, int destRow, int destCol) {
        this.maze = maze;
        this.startRow = startRow;
        this.startCol = startCol;
        this.destRow = destRow;
        this.destCol = destCol;
    }
}
