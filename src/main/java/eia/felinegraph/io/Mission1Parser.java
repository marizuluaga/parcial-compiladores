package eia.felinegraph.io;

import eia.felinegraph.algo.GridMaze;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses Mission 1 input: a sequence of grid test cases terminated by "0 0".
 * See Section 3 of the project statement for the exact format.
 */
public final class Mission1Parser {

    private Mission1Parser() {
    }

    public static List<Mission1Case> parse(String input) {
        TokenScanner sc = new TokenScanner(input);
        List<Mission1Case> cases = new ArrayList<>();
        int caseNo = 0;

        while (sc.hasNext()) {
            caseNo++;
            int rows = sc.nextInt("R (case #" + caseNo + ")");
            int cols = sc.nextInt("C (case #" + caseNo + ")");

            if (rows == 0 && cols == 0) {
                return cases; // terminator: stop, do not process further
            }
            if (rows < 1 || rows > 1000 || cols < 1 || cols > 1000) {
                throw new ParseException("Case #" + caseNo + ": R and C must be between 1 and 1000 (got R=" + rows + ", C=" + cols + ").");
            }

            GridMaze maze = new GridMaze(rows, cols);
            int rowsWithBombs = sc.nextInt("the number of bomb-carrying rows (case #" + caseNo + ")");
            sc.expectRange(rowsWithBombs, 0, rows, "the number of bomb-carrying rows in case #" + caseNo);

            for (int i = 0; i < rowsWithBombs; i++) {
                int rowNumber = sc.nextInt("a bomb row number (case #" + caseNo + ")");
                sc.expectRange(rowNumber, 0, rows - 1, "a bomb row number in case #" + caseNo);
                int bombCount = sc.nextInt("the bomb count for row " + rowNumber + " (case #" + caseNo + ")");
                if (bombCount < 0) {
                    throw new ParseException("Case #" + caseNo + ": bomb count for row " + rowNumber + " cannot be negative.");
                }
                for (int b = 0; b < bombCount; b++) {
                    int col = sc.nextInt("a bomb column in row " + rowNumber + " (case #" + caseNo + ")");
                    sc.expectRange(col, 0, cols - 1, "a bomb column in row " + rowNumber + " of case #" + caseNo);
                    maze.setBomb(rowNumber, col);
                }
            }

            int sr = sc.nextInt("the start row (case #" + caseNo + ")");
            int scol = sc.nextInt("the start column (case #" + caseNo + ")");
            int dr = sc.nextInt("the destination row (case #" + caseNo + ")");
            int dc = sc.nextInt("the destination column (case #" + caseNo + ")");
            sc.expectRange(sr, 0, rows - 1, "the start row of case #" + caseNo);
            sc.expectRange(scol, 0, cols - 1, "the start column of case #" + caseNo);
            sc.expectRange(dr, 0, rows - 1, "the destination row of case #" + caseNo);
            sc.expectRange(dc, 0, cols - 1, "the destination column of case #" + caseNo);

            cases.add(new Mission1Case(maze, sr, scol, dr, dc));
        }

        throw new ParseException("The input is missing the terminating test case \"0 0\".");
    }
}
