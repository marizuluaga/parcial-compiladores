package eia.felinegraph.algo;


public final class GridMaze {

    public final int rows;
    public final int cols;
    private final boolean[] bomb;

    public GridMaze(int rows, int cols) {
        if (rows < 0 || cols < 0) {
            throw new IllegalArgumentException("Grid dimensions must be non-negative");
        }
        this.rows = rows;
        this.cols = cols;
        this.bomb = new boolean[rows * cols];
    }

    public int index(int row, int col) {
        return row * cols + col;
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public void setBomb(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IllegalArgumentException(
                    "Bomb at (" + row + "," + col + ") is outside the " + rows + "x" + cols + " grid");
        }
        bomb[index(row, col)] = true;
    }

    public boolean isBomb(int row, int col) {
        return bomb[index(row, col)];
    }
}
