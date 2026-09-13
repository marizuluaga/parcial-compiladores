package eia.felinegraph;

import javax.swing.SwingUtilities;

import eia.felinegraph.gui.MainFrame;
import eia.felinegraph.gui.Theme;


public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Theme.applyLookAndFeel();
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
