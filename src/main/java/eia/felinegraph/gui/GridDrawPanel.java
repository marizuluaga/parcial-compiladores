package eia.felinegraph.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import eia.felinegraph.algo.GridMaze;


public final class GridDrawPanel extends JPanel {

    private GridMaze maze;
    private List<int[]> path;
    private int startRow = -1, startCol = -1, destRow = -1, destCol = -1;
    private String caption = "";

    public GridDrawPanel() {
        setBackground(Theme.PANEL_BG);
    }

    public void setData(GridMaze maze, List<int[]> path, int sr, int sc, int dr, int dc, String caption) {
        this.maze = maze;
        this.path = path;
        this.startRow = sr;
        this.startCol = sc;
        this.destRow = dr;
        this.destCol = dc;
        this.caption = caption;
        repaint();
    }

    public void clear() {
        this.maze = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int margin = 30;
        int captionHeight = caption.isEmpty() ? 0 : 22;
        int availW = getWidth() - 2 * margin;
        int availH = getHeight() - 2 * margin - captionHeight;
        int cell = Math.max(2, Math.min(availW / maze.cols, availH / maze.rows));
        cell = Math.min(cell, 40);

        int gridW = cell * maze.cols;
        int gridH = cell * maze.rows;
        int ox = margin + Math.max(0, (availW - gridW) / 2);
        int oy = margin + captionHeight + Math.max(0, (availH - gridH) / 2);

        Set<Long> pathCells = new HashSet<>();
        if (path != null) {
            for (int[] c : path) {
                pathCells.add(((long) c[0] << 32) | (c[1] & 0xffffffffL));
            }
        }

        for (int r = 0; r < maze.rows; r++) {
            for (int c = 0; c < maze.cols; c++) {
                int x = ox + c * cell;
                int y = oy + r * cell;
                long key = ((long) r << 32) | (c & 0xffffffffL);
                if (maze.isBomb(r, c)) {
                    g2.setColor(Theme.RED);
                } else if (pathCells.contains(key)) {
                    g2.setColor(Theme.TEAL);
                } else {
                    g2.setColor(Theme.GRID_EMPTY);
                }
                g2.fillRect(x, y, cell, cell);
                if (cell >= 6) {
                    g2.setColor(Theme.GRID_LINE);
                    g2.drawRect(x, y, cell, cell);
                }
            }
        }

        if (cell >= 10) {
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.min(14, cell - 4)));
            drawCenteredLabel(g2, "S", ox + startCol * cell, oy + startRow * cell, cell, Color.WHITE);
            drawCenteredLabel(g2, "N", ox + destCol * cell, oy + destRow * cell, cell, Color.WHITE);
        }

        if (!caption.isEmpty()) {
            g2.setColor(Theme.TEXT_MUTED);
            g2.setFont(Theme.FLAVOR_FONT);
            g2.drawString(caption, margin, margin - 8);
        }
    }

    private void drawCenteredLabel(Graphics2D g2, String text, int x, int y, int cell, Color color) {
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (cell - fm.stringWidth(text)) / 2;
        int ty = y + (cell + fm.getAscent()) / 2 - 2;
        g2.setColor(color);
        g2.drawString(text, tx, ty);
    }
}
