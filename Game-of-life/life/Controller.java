package life;

/*
    CONTROLLER
    ==========
*/


import javax.swing.*;
import java.awt.*;

public class Controller extends Thread {

    private Universe universe;
    private GameOfLife window;
    private Long speed;
    private boolean isPaused = false;
    private JToggleButton pauseButton;
    private JButton resetButton;
    private JLabel speedLabel;
    private JSlider speedSlider;
    private JSlider colorSlider;
    private Color color;

    public Controller(Universe universe, GameOfLife window) {
        this.universe = universe;
        this.window = window;
        this.window
                .getRightPanel()
                .initialize(universe.getRows(), universe.getCols());
        this.pauseButton = window.getLeftPanel().getPauseButton();
        this.resetButton = window.getLeftPanel().getResetButton();
        this.speedLabel = window.getLeftPanel().getSpeedLabel();
        this.speedSlider = window.getLeftPanel().getSpeedSlider();
        this.colorSlider = window.getLeftPanel().getColorSlider();
        this.speed = 1100L - speedSlider.getValue() * 100L;
    }

    @Override
    public void run() {

        window.setVisible(true);

        /*
         * Infinite loop until window closes
         */

        while (true) {

            colorListener();
            resetListener();
            speedListener();
            pauseListener();

            if (isPaused) {
                continue;
            }

//            New thread for each evolution
            var t = new Controller2(universe, window);
            t.start();

//            Wait for thread to finish
            try {
                t.join();
            } catch (InterruptedException ex) {
                System.out.println("error");
            }

//            Then evolve
            universe.evolve();

//            Pause for visuals
            getToSleep(speed);
        }
    }

    public void colorListener() {
        // Color slider listener
        colorSlider.addChangeListener(changeEvent -> {
            this.color = colorChosen(colorSlider.getValue());
            window.getRightPanel().updateColor(color);
        });
    }

    public void resetListener() {
        // Reset button listener
        resetButton.addActionListener(event -> {
            if (pauseButton.isSelected()) {
                pauseButton.doClick();
            }
            isPaused = false;
            this.universe = new Universe();
        });
    }

    public void speedListener() {
        // Speed slider listener
        speedSlider.addChangeListener(changeEvent -> {
            this.speed = 1100L - speedSlider.getValue() * 100;
            if (speed == 1000) {
                speedLabel.setText("One evolution per second");
            } else {
                speedLabel.setText(String.format("One evolution per %d ms", speed));
            }
        });
    }

    public void pauseListener() {
        // Pause button listener
        pauseButton.addActionListener(event -> {
            if (pauseButton.isSelected()) {
                pauseButton.doClick();
                isPaused = !isPaused;
                pauseButton.setText(isPaused ? "RESUME" : "PAUSE");
            }
        });
    }

    public void getToSleep(Long time) {
        try {
            sleep(time);
        } catch (InterruptedException ex) {
            System.out.println("error");
        }
    }

    public Color colorChosen(int num) {
        switch (num) {
            case 0:
                return Color.WHITE;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.ORANGE;
            case 3:
                return Color.PINK;
            case 4:
                return Color.RED;
            case 5:
                return Color.GREEN;
            case 6:
                return Color.CYAN;
            case 7:
                return Color.BLUE;
            case 8:
                return Color.GRAY;
            case 9:
                break;
        }
        return Color.BLACK;
    }
}


class Controller2 extends Thread {
    /*
    * This thread works inside the Controller
    * to update the state of the universe
    */

    private Universe universe;
    private GameOfLife window;

    public Controller2(Universe universe, GameOfLife window) {
        this.universe = universe;
        this.window = window;
    }

    @Override
    public void run() {
//        updates generation label
        window
                .getLeftPanel()
                .getGenerationLabel()
                .setText("Generation #" + universe.getGeneration());
        window
                .getLeftPanel()
                .getGenerationLabel()
                .repaint();

//        updates alive label
        window
                .getLeftPanel()
                .getAliveLabel()
                .setText("Alive: " + universe.countAliveCells());
        window
                .getLeftPanel()
                .getAliveLabel()
                .repaint();

//        updates grid of cells
        window
                .getRightPanel()
                .updateCellArray(universe.getMatrix());
        window
                .getRightPanel()
                .repaint();
    }
}