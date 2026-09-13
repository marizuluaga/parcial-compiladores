package eia.felinegraph.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import eia.felinegraph.algo.BfsSolver;
import eia.felinegraph.algo.DfsSolver;
import eia.felinegraph.algo.GridPathResult;
import eia.felinegraph.io.Mission1Case;
import eia.felinegraph.io.Mission1Parser;


public final class Mission1Panel extends MissionPanelBase {

    private static final int DRAW_THRESHOLD = 50; // grids up to 50x50 are mandatory to draw

    private static final String SAMPLE = String.join("\n",
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

    private final GridDrawPanel gridPanel = new GridDrawPanel();
    private final JTextArea tooLargeMessage = GuiUtil.message("", Theme.TEXT_MUTED);
    private final CardLayout cards = new CardLayout();
    private final JPanel cardHost = new JPanel(cards);
    private final JLabel answerLabel = new JLabel(" ");
    private final JRadioButton bfsRadio = new JRadioButton("Mostrar ruta BFS", true);
    private final JRadioButton dfsRadio = new JRadioButton("Mostrar ruta DFS");

    private List<Mission1Case> cases = new ArrayList<>();
    private List<GridPathResult> bfsResults = new ArrayList<>();
    private List<GridPathResult> dfsResults = new ArrayList<>();

    public Mission1Panel() {
        super("Mision 1 - Rescate de Nina en el campo minado",
                "Limon has trapped Nina in a minefield. Pola and Minerva must find a safe path: "
                        + "BFS always finds the shortest one, DFS just finds one that works. "
                        + "Movement is 4-directional; DFS expands neighbours in the fixed order up, down, left, right.");

        ButtonGroup group = new ButtonGroup();
        group.add(bfsRadio);
        group.add(dfsRadio);
        bfsRadio.setOpaque(false);
        dfsRadio.setOpaque(false);
        bfsRadio.setForeground(Theme.TEXT);
        dfsRadio.setForeground(Theme.TEXT);
        java.awt.event.ActionListener toggle = e -> {
            int idx = caseSelector.getSelectedIndex();
            if (idx >= 0) {
                renderCase(idx);
            }
        };
        bfsRadio.addActionListener(toggle);
        dfsRadio.addActionListener(toggle);
        initUi();
    }

    @Override
    protected JComponent buildVisualizationArea() {
        JPanel wrap = new JPanel(new BorderLayout(4, 4));
        wrap.setOpaque(false);

        JPanel toggleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        toggleRow.setOpaque(false);
        toggleRow.add(bfsRadio);
        toggleRow.add(dfsRadio);

        JPanel placeholder = new JPanel(new BorderLayout());
        placeholder.setOpaque(false);
        placeholder.add(GuiUtil.message("Load the sample or paste an input, then press Solve to see the grid here.",
                Theme.TEXT_MUTED), BorderLayout.CENTER);

        JPanel tooLargeCard = new JPanel(new BorderLayout());
        tooLargeCard.setOpaque(false);
        tooLargeCard.add(tooLargeMessage, BorderLayout.CENTER);

        cardHost.setOpaque(false);
        cardHost.add(placeholder, "placeholder");
        cardHost.add(gridPanel, "grid");
        cardHost.add(tooLargeCard, "toolarge");
        cards.show(cardHost, "placeholder");

        answerLabel.setFont(Theme.HEADER_FONT);
        answerLabel.setForeground(Theme.TEAL);
        answerLabel.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        wrap.add(toggleRow, BorderLayout.NORTH);
        wrap.add(cardHost, BorderLayout.CENTER);
        wrap.add(answerLabel, BorderLayout.SOUTH);
        return wrap;
    }

    @Override
    protected String getSampleInput() {
        return SAMPLE;
    }

    @Override
    protected SolveOutput solveAll(String rawInput) {
        cases = Mission1Parser.parse(rawInput);
        bfsResults = new ArrayList<>(cases.size());
        dfsResults = new ArrayList<>(cases.size());

        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (Mission1Case c : cases) {
            k++;
            GridPathResult bfs = BfsSolver.solve(c.maze, c.startRow, c.startCol, c.destRow, c.destCol);
            GridPathResult dfs = DfsSolver.solve(c.maze, c.startRow, c.startCol, c.destRow, c.destCol);
            bfsResults.add(bfs);
            dfsResults.add(dfs);
            sb.append("Case #").append(k).append(": ");
            if (!bfs.reachable) {
                sb.append("Nina is unreachable");
            } else {
                sb.append("BFS ").append(bfs.moves).append(" DFS ").append(dfs.moves);
            }
            sb.append('\n');
        }
        return new SolveOutput(sb.toString().stripTrailing(), cases.size());
    }

    @Override
    protected void renderCase(int index) {
        Mission1Case c = cases.get(index);
        GridPathResult bfs = bfsResults.get(index);
        GridPathResult dfs = dfsResults.get(index);
        GridPathResult chosen = bfsRadio.isSelected() ? bfs : dfs;

        if (!bfs.reachable) {
            answerLabel.setText("Caso #" + (index + 1) + ": Nina no es alcanzable");
        } else {
            answerLabel.setText("Caso #" + (index + 1) + ":  BFS = " + bfs.moves + " movimientos    DFS = " + dfs.moves + " movimientos");
        }

        boolean withinLimit = c.maze.rows <= DRAW_THRESHOLD && c.maze.cols <= DRAW_THRESHOLD;
        if (!withinLimit) {
            tooLargeMessage.setText("La cuadricula " + c.maze.rows + " x " + c.maze.cols + " supera el limite de "
                    + DRAW_THRESHOLD + " x " + DRAW_THRESHOLD + " visualization limit from Section 2.3, "
                    + "so the drawing was omitted. The numeric answer above is still exact and was computed "
                    + "over the full grid.");
            cards.show(cardHost, "toolarge");
            return;
        }

        String caption = bfsRadio.isSelected()
                ? "BFS - shortest path" + (bfs.reachable ? " (" + bfs.moves + " moves)" : "")
                : "DFS - up, down, left, right order" + (dfs.reachable ? " (" + dfs.moves + " moves)" : "");
        gridPanel.setData(c.maze, chosen.reachable ? chosen.path : null, c.startRow, c.startCol, c.destRow, c.destCol, caption);
        cards.show(cardHost, "grid");
    }

    @Override
    protected void clearVisualization() {
        gridPanel.clear();
        answerLabel.setText(" ");
        cards.show(cardHost, "placeholder");
    }
}
