package eia.felinegraph.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import eia.felinegraph.algo.BellmanFordResult;
import eia.felinegraph.algo.BellmanFordSolver;
import eia.felinegraph.algo.FloydWarshallResult;
import eia.felinegraph.algo.FloydWarshallSolver;
import eia.felinegraph.algo.WeightedEdge;
import eia.felinegraph.io.Mission3Case;
import eia.felinegraph.io.Mission3Parser;


public final class Mission3Panel extends MissionPanelBase {

    private static final int GRAPH_DRAW_THRESHOLD = 60;
    private static final int MATRIX_DRAW_THRESHOLD = 100;

    private static final String SAMPLE = String.join("\n",
            "3",
            "5 7 0 4",
            "0 1 50",
            "0 2 10",
            "1 2 -30",
            "1 3 40",
            "2 1 -5",
            "2 3 60",
            "3 4 20",
            "4 4 0 3",
            "0 1 20",
            "1 2 30",
            "2 1 -10",
            "2 3 15",
            "3 3 0 2",
            "0 1 -40",
            "1 2 -25",
            "0 2 -80");

    private final GraphDrawPanel graphPanel = new GraphDrawPanel();
    private final JTextArea tooLargeMessage = GuiUtil.message("", Theme.TEXT_MUTED);
    private final CardLayout graphCards = new CardLayout();
    private final JPanel graphCardHost = new JPanel(graphCards);
    private final MatrixPanel matrixPanel = new MatrixPanel();
    private final JLabel answerLabel = new JLabel(" ");
    private final JLabel crossCheckLabel = new JLabel(" ");

    private List<Mission3Case> cases = new ArrayList<>();
    private List<FloydWarshallResult> fwResults = new ArrayList<>();
    private List<BellmanFordResult> bfResults = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private List<Boolean> mismatches = new ArrayList<>();

    public Mission3Panel() {
        super("Mision 3 - La reserva de churun",
                "Routes may repeat nodes and edges (a walk, not a simple path). A positive-gain cycle "
                        + "reachable from S that can also reach D means unlimited churun. Floyd-Warshall gives the "
                        + "full matrix; Bellman-Ford cross-checks node D independently.");
        initUi();
    }

    @Override
    protected JComponent buildVisualizationArea() {
        JPanel top = new JPanel(new BorderLayout(2, 2));
        top.setOpaque(false);
        top.add(Theme.sectionLabel("Ruta / ciclo de ganancia positiva"), BorderLayout.NORTH);

        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setOpaque(false);
        placeholder.add(GuiUtil.message("Load the sample or paste an input, then press Solve.", Theme.TEXT_MUTED),
                BorderLayout.CENTER);
        JPanel tooLargeCard = new JPanel(new BorderLayout());
        tooLargeCard.setOpaque(false);
        tooLargeCard.add(tooLargeMessage, BorderLayout.CENTER);

        graphCardHost.setOpaque(false);
        graphCardHost.add(placeholder, "placeholder");
        graphCardHost.add(graphPanel, "graph");
        graphCardHost.add(tooLargeCard, "toolarge");
        graphCards.show(graphCardHost, "placeholder");
        top.add(graphCardHost, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(2, 2));
        bottom.setOpaque(false);
        bottom.add(Theme.sectionLabel("Matriz Floyd-Warshall (fila i, columna j)"), BorderLayout.NORTH);
        bottom.add(matrixPanel, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, bottom);
        split.setResizeWeight(0.55);
        split.setBorder(null);
        split.setOpaque(false);

        JPanel labels = new JPanel();
        labels.setOpaque(false);
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        answerLabel.setFont(Theme.HEADER_FONT);
        answerLabel.setForeground(Theme.TEAL);
        answerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        crossCheckLabel.setFont(Theme.BODY_FONT);
        crossCheckLabel.setForeground(Theme.TEXT_MUTED);
        crossCheckLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        labels.add(answerLabel);
        labels.add(crossCheckLabel);
        labels.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        JPanel wrap = new JPanel(new BorderLayout(4, 4));
        wrap.setOpaque(false);
        wrap.add(split, BorderLayout.CENTER);
        wrap.add(labels, BorderLayout.SOUTH);
        return wrap;
    }

    @Override
    protected String getSampleInput() {
        return SAMPLE;
    }

    @Override
    protected SolveOutput solveAll(String rawInput) {
        cases = Mission3Parser.parse(rawInput);
        fwResults = new ArrayList<>(cases.size());
        bfResults = new ArrayList<>(cases.size());
        answers = new ArrayList<>(cases.size());
        mismatches = new ArrayList<>(cases.size());

        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (Mission3Case c : cases) {
            k++;
            FloydWarshallResult fw = FloydWarshallSolver.solve(c.n, c.edges);
            BellmanFordResult bf = BellmanFordSolver.solve(c.n, c.edges, c.s);
            fwResults.add(fw);
            bfResults.add(bf);

            String line;
            boolean fwReachable = fw.dist[c.s][c.d] != FloydWarshallResult.NO_ROUTE;
            if (!fwReachable) {
                line = "Limon blocked the way";
            } else if (fw.unbounded[c.s][c.d]) {
                line = "Infinite churun!";
            } else {
                line = String.valueOf(fw.dist[c.s][c.d]);
            }
            answers.add(line);

            boolean bfReachable = bf.isReachable(c.d);
            boolean mismatch = fwReachable != bfReachable
                    || (fwReachable && bfReachable && fw.unbounded[c.s][c.d] != bf.unbounded[c.d])
                    || (fwReachable && bfReachable && !fw.unbounded[c.s][c.d] && !bf.unbounded[c.d]
                        && fw.dist[c.s][c.d] != bf.dist[c.d]);
            mismatches.add(mismatch);

            sb.append("Case #").append(k).append(": ").append(line).append('\n');
        }
        return new SolveOutput(sb.toString().stripTrailing(), cases.size());
    }

    @Override
    protected void renderCase(int index) {
        Mission3Case c = cases.get(index);
        FloydWarshallResult fw = fwResults.get(index);
        String answer = answers.get(index);

        answerLabel.setText("Caso #" + (index + 1) + ":  " + answer);
        if (mismatches.get(index)) {
            crossCheckLabel.setForeground(Theme.RED_BRIGHT);
            crossCheckLabel.setText("Advertencia: Floyd-Warshall y Bellman-Ford no coinciden en el nodo D para este caso.");
        } else {
            crossCheckLabel.setForeground(Theme.TEXT_MUTED);
            crossCheckLabel.setText("Floyd-Warshall y Bellman-Ford coinciden en el nodo D.");
        }

        matrixPanel.setMatrix(fw, c.n, c.s, c.d);
        if (c.n > MATRIX_DRAW_THRESHOLD) {
            // Defensive only: Mission 3 caps N at 100, so this never actually triggers.
            matrixPanel.clear();
        }

        if (c.n > GRAPH_DRAW_THRESHOLD) {
            tooLargeMessage.setText("Esta red tiene " + c.n + " nodos, por encima del limite de " + GRAPH_DRAW_THRESHOLD
                    + "-node visualization limit from Section 2.3, so the route/cycle drawing was omitted. "
                    + "The matrix below and the numeric answer above are still exact.");
            graphCards.show(graphCardHost, "toolarge");
            return;
        }

        Set<Long> highlightPairs = new HashSet<>();
        boolean unbounded = fw.dist[c.s][c.d] != FloydWarshallResult.NO_ROUTE && fw.unbounded[c.s][c.d];
        Color highlightColor = Theme.TEAL;

        if (fw.dist[c.s][c.d] == FloydWarshallResult.NO_ROUTE) {
            // Limon blocked the way: nothing to highlight.
        } else if (unbounded) {
            highlightColor = Theme.GOLD;
            int cycleNode = findResponsibleCycleNode(fw, c.n, c.s, c.d);
            if (cycleNode >= 0) {
                List<Integer> cycle = FloydWarshallSolver.reconstructPath(fw, cycleNode, cycleNode);
                addPathEdges(highlightPairs, cycle);
            }
        } else {
            List<Integer> path = FloydWarshallSolver.reconstructPath(fw, c.s, c.d);
            addPathEdges(highlightPairs, path);
        }

        List<GraphDrawPanel.EdgeView> views = new ArrayList<>(c.edges.size());
        for (WeightedEdge e : c.edges) {
            boolean onRoute = highlightPairs.contains(orderedKey(e.from, e.to));
            views.add(new GraphDrawPanel.EdgeView(e.from, e.to, e.weight, onRoute, highlightColor));
        }
        graphPanel.setGraph(c.n, views, true, null);
        graphCards.show(graphCardHost, "graph");
    }

    /** Finds a k with d[S][k] finite, d[k][k] &gt; 0 and d[k][D] finite, per Section 5's unbounded rule. */
    private int findResponsibleCycleNode(FloydWarshallResult fw, int n, int s, int d) {
        for (int k = 0; k < n; k++) {
            if (fw.dist[s][k] != FloydWarshallResult.NO_ROUTE
                    && fw.dist[k][k] > 0
                    && fw.dist[k][d] != FloydWarshallResult.NO_ROUTE) {
                return k;
            }
        }
        return -1;
    }

    private void addPathEdges(Set<Long> pairs, List<Integer> path) {
        if (path == null) {
            return;
        }
        for (int i = 0; i + 1 < path.size(); i++) {
            pairs.add(orderedKey(path.get(i), path.get(i + 1)));
        }
    }

    private long orderedKey(int a, int b) {
        return ((long) a << 32) | (b & 0xffffffffL);
    }

    @Override
    protected void clearVisualization() {
        graphPanel.clear();
        matrixPanel.clear();
        answerLabel.setText(" ");
        crossCheckLabel.setText(" ");
        graphCards.show(graphCardHost, "placeholder");
    }
}
