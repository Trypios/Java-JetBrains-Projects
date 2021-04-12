package life;

import javax.swing.*;
import java.awt.*;

/**
 * VIEW of the Game.
 * Main frame, contains all other panels.
 */
public class GameOfLife extends JFrame {

    private ControlPanel leftPanel;
    private UniversePanel rightPanel;

    public GameOfLife() {

        super("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Game Of Life");
        setName("GameOfLife");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initComponents();
        setVisible(false);
    }

    private void initComponents() {

        // Leftmost panel for labels and buttons
        leftPanel = new ControlPanel();
        add(leftPanel, BorderLayout.WEST);

        // Rightmost panel - big for main game - GridLayout (rows x cols)
        rightPanel = new UniversePanel();
        rightPanel.setName("Grid Panel");
        add(rightPanel, BorderLayout.CENTER);
    }

    /**
     * Getter.
     * @return the Game's control panel.
     */
    ControlPanel getLeftPanel() {
        return leftPanel;
    }

    /**
     * Getter.
     * @return the Game's main panel (that displays the Universe).
     */
    UniversePanel getRightPanel() {
        return rightPanel;
    }
}