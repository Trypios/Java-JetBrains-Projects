package life;

import javax.swing.*;
import java.awt.*;

/*
    VIEW
    ====
*/

public class GameOfLife extends JFrame {

    private LeftPanel leftPanel;
    private RightPanel rightPanel;

    public GameOfLife() {
        // Main container
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


    public void initComponents() {
//        Leftmost panel for labels and buttons
        leftPanel = new LeftPanel();
        add(leftPanel, BorderLayout.WEST);

//        Rightmost panel - big for main game - GridLayout (rows x cols)
        rightPanel = new RightPanel();
        rightPanel.setName("Grid Panel");
        add(rightPanel, BorderLayout.CENTER);
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    //    methods for controller
    public RightPanel getRightPanel() {
        return rightPanel;
    }
}