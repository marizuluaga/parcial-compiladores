package eia.felinegraph.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

public final class GraphDrawPanel extends JPanel {

   
    public static final class EdgeView {
        public final int from;
        public final int to;
        public final long weight;
        public final boolean highlight;
        public final Color highlightColor;

        public EdgeView(int from, int to, long weight, boolean highlight, Color highlightColor) {
            this.from = from;
            this.to = to;
            this.weight = weight;
            this.highlight = highlight;
            this.highlightColor = highlightColor;
        }
    }

    private int nodeCount;
    private List<EdgeView> edges;
    private boolean directed;
    private String[] nodeLabels;

    public GraphDrawPanel() {
        setBackground(Theme.PANEL_BG);
    }

    public void setGraph(int nodeCount, List<EdgeView> edges, boolean directed, String[] nodeLabels) {
        this.nodeCount = nodeCount;
        this.edges = edges;
        this.directed = directed;
        this.nodeLabels = nodeLabels;
        repaint();
    }

    public void clear() {
        this.edges = null;
        this.nodeCount = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (edges == null || nodeCount == 0) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int radius = Math.max(20, Math.min(w, h) / 2 - 40);
        int nodeRadius = nodeCount > 40 ? 8 : (nodeCount > 20 ? 12 : 16);

        Point2D[] pos = new Point2D[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            double angle = -Math.PI / 2 + 2 * Math.PI * i / nodeCount;
            pos[i] = new Point2D.Double(cx + radius * Math.cos(angle), cy + radius * Math.sin(angle));
        }

      
        Map<Long, Integer> pairSeen = new HashMap<>();
        Map<Long, Integer> pairTotal = new HashMap<>();
        for (EdgeView e : edges) {
            long key = pairKey(e.from, e.to);
            pairTotal.merge(key, 1, Integer::sum);
        }

        boolean drawLabels = edges.size() <= 150;
        g2.setFont(Theme.BODY_FONT.deriveFont(11f));

       
        for (int pass = 0; pass < 2; pass++) {
            for (EdgeView e : edges) {
                if ((pass == 0) == e.highlight) {
                    continue;
                }
                long key = pairKey(e.from, e.to);
                int idx = pairSeen.merge(key, 1, Integer::sum) - 1;
                int total = pairTotal.get(key);
                drawEdge(g2, pos[e.from], pos[e.to], idx, total, e, drawLabels);
            }
        }

        for (int i = 0; i < nodeCount; i++) {
            Point2D p = pos[i];
            g2.setColor(Theme.PANEL_BG_LIGHT);
            g2.fillOval((int) (p.getX() - nodeRadius), (int) (p.getY() - nodeRadius), 2 * nodeRadius, 2 * nodeRadius);
            g2.setColor(Theme.TEAL_DARK);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval((int) (p.getX() - nodeRadius), (int) (p.getY() - nodeRadius), 2 * nodeRadius, 2 * nodeRadius);
            String label = nodeLabels != null ? nodeLabels[i] : String.valueOf(i);
            g2.setColor(Theme.TEXT);
            g2.setFont(Theme.BODY_FONT.deriveFont(Font.BOLD, nodeCount > 40 ? 9f : 11f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, (float) (p.getX() - fm.stringWidth(label) / 2.0), (float) (p.getY() + fm.getAscent() / 2.0 - 2));
        }
    }

    private long pairKey(int a, int b) {
        int lo = Math.min(a, b), hi = Math.max(a, b);
        return ((long) lo << 32) | (hi & 0xffffffffL);
    }

    private void drawEdge(Graphics2D g2, Point2D p1, Point2D p2, int idx, int total, EdgeView e, boolean drawLabels) {
        double offset = 0;
        if (total > 1) {
            double step = 18.0;
            offset = (idx - (total - 1) / 2.0) * step;
        }

        double mx = (p1.getX() + p2.getX()) / 2;
        double my = (p1.getY() + p2.getY()) / 2;
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double len = Math.max(1e-6, Math.hypot(dx, dy));
        double nx = -dy / len;
        double ny = dx / len;
        double ctrlX = mx + nx * offset;
        double ctrlY = my + ny * offset;

        Shape shape;
        if (offset == 0) {
            shape = new Line2D.Double(p1, p2);
        } else {
            shape = new QuadCurve2D.Double(p1.getX(), p1.getY(), ctrlX, ctrlY, p2.getX(), p2.getY());
        }

        Color color = e.highlight ? e.highlightColor : Theme.TEXT_MUTED;
        g2.setColor(color);
        if (e.highlight) {
            g2.setStroke(new BasicStroke(3f));
        } else {
            g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f,
                    new float[]{5f, 5f}, 0f));
        }
        g2.draw(shape);

        if (directed) {
            Point2D end = offset == 0 ? p2 : quadPointNear(p1, ctrlX, ctrlY, p2, 0.92);
            Point2D before = offset == 0 ? p1 : quadPointNear(p1, ctrlX, ctrlY, p2, 0.80);
            drawArrowHead(g2, before, end, color);
        }

        if (drawLabels) {
            g2.setColor(Theme.TEXT_MUTED);
            String label = String.valueOf(e.weight);
            g2.drawString(label, (float) (ctrlX + 4), (float) (ctrlY - 4));
        }
    }

    private Point2D quadPointNear(Point2D p0, double ctrlX, double ctrlY, Point2D p2, double t) {
        double x = (1 - t) * (1 - t) * p0.getX() + 2 * (1 - t) * t * ctrlX + t * t * p2.getX();
        double y = (1 - t) * (1 - t) * p0.getY() + 2 * (1 - t) * t * ctrlY + t * t * p2.getY();
        return new Point2D.Double(x, y);
    }

    private void drawArrowHead(Graphics2D g2, Point2D from, Point2D to, Color color) {
        double angle = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
        int size = 9;
        int nodeRadius = nodeCount > 40 ? 8 : (nodeCount > 20 ? 12 : 16);
        double tx = to.getX() - Math.cos(angle) * nodeRadius;
        double ty = to.getY() - Math.sin(angle) * nodeRadius;
        Polygon arrow = new Polygon();
        arrow.addPoint((int) tx, (int) ty);
        arrow.addPoint((int) (tx - size * Math.cos(angle - Math.PI / 7)), (int) (ty - size * Math.sin(angle - Math.PI / 7)));
        arrow.addPoint((int) (tx - size * Math.cos(angle + Math.PI / 7)), (int) (ty - size * Math.sin(angle + Math.PI / 7)));
        g2.setColor(color);
        g2.fillPolygon(arrow);
    }
}
