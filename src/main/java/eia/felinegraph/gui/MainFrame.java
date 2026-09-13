package eia.felinegraph.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public final class MainFrame extends JFrame {

    public MainFrame() {
        super("Las cronicas felinas de grafos - Pola y Minerva vs. Limon - Universidad EIA");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());

        add(buildBanner(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.PANEL_BG);
        tabs.setForeground(Theme.TEXT);
        tabs.setFont(Theme.HEADER_FONT.deriveFont(13f));
        tabs.addTab("Historia", buildStoryPanel());
        tabs.addTab("Mision 1 - Campo minado", new Mission1Panel());
        tabs.addTab("Mision 2 - Dijkstra", new Mission2Panel());
        tabs.addTab("Mision 3 - Reserva de comida", new Mission3Panel());
        tabs.addTab("Mision 4 - Kruskal", new Mission4Panel());
        add(tabs, BorderLayout.CENTER);

        setSize(1320, 880);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
    }

    private JComponent buildBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(Theme.PANEL_BG);
        banner.setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel title = new JLabel("Las cronicas felinas de grafos");
        title.setFont(Theme.TITLE_FONT.deriveFont(Font.BOLD, 22f));
        title.setForeground(Theme.TEAL);

        JLabel subtitle = new JLabel("Pola y Minerva vs. Limon y Nero  -  Lenguajes y Compiladores, Universidad EIA");
        subtitle.setFont(Theme.FLAVOR_FONT);
        subtitle.setForeground(Theme.TEXT_MUTED);

        JPanel textStack = new JPanel();
        textStack.setOpaque(false);
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        textStack.add(title);
        textStack.add(subtitle);

        banner.add(textStack, BorderLayout.WEST);
        JLabel emblem = new JLabel("🐱 vs 🐈");
        emblem.setFont(Theme.TITLE_FONT.deriveFont(20f));
        emblem.setForeground(Theme.GOLD);
        banner.add(emblem, BorderLayout.EAST);
        return banner;
    }

    private JComponent buildStoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JTextArea story = new JTextArea(STORY);
        story.setEditable(false);
        story.setLineWrap(true);
        story.setWrapStyleWord(true);
        story.setOpaque(false);
        story.setForeground(Theme.TEXT);
        story.setFont(Theme.BODY_FONT.deriveFont(14f));
        story.setFocusable(false);

        JScrollPane scroll = new JScrollPane(story);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private static final String STORY =
            "Limon, lider de los villanos, junto con Nero, robo las cuentas de Claude Pro de los estudiantes "
            + "del curso y secuestro a Nina de la casa de Sebas.\n\n"
            + "Pola y Minerva encuentran varias pistas que, en realidad, son problemas de grafos. Para avanzar "
            + "deben resolver cuatro misiones usando distintos algoritmos.\n\n"
            + "Mision 1 - Rescate de Nina: recorrer un campo minado con BFS y DFS, moviendose arriba, abajo, "
            + "izquierda y derecha.\n\n"
            + "Mision 2 - Recuperar las cuentas: encontrar la ruta de menor costo con Dijkstra.\n\n"
            + "Mision 3 - Reserva de churun: calcular la mayor cantidad posible con Floyd-Warshall y "
            + "Bellman-Ford, teniendo en cuenta pesos negativos y ciclos de ganancia positiva.\n\n"
            + "Mision 4 - Reconectar la red: usar Kruskal y union-find para conectar las intersecciones con "
            + "el menor costo total.\n\n"
            + "Selecciona una mision, carga el ejemplo o escribe una entrada y presiona Resolver.";
}
