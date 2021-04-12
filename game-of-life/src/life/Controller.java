package life;

import javax.swing.*;
import java.awt.*;


/**
 * Controller for the Game of Life.
 * The main thread.
 */
public class Controller extends Thread {

    private Universe universe;
    private final GameOfLife window;
    private Long speed;
    private boolean isPaused = false;
    private final JToggleButton pauseButton;
    private final JButton resetButton;
    private final JLabel speedLabel;
    private final JSlider speedSlider;
    private final JSlider colorSlider;
    private Color color;

    Controller(Universe universe, GameOfLife window) {

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

    /**
     * Starts this thread.
     */
    @Override
    public void run() {

        window.setVisible(true);

        // Infinite loop until window closes
        while (true) {

            colorListener();
            resetListener();
            speedListener();
            pauseListener();

            if (isPaused) {
                continue;
            }

            var t = new Controller2(universe, window);  // New thread for each evolution
            t.start();

            try {
                t.join();  // Wait for thread to finish
            } catch (InterruptedException ex) {
                System.out.println("error");
            }

            universe.evolve();  // Then evolve
            goToSleep(speed);  // Pause for visuals
        }
    }

    /**
     * Color slider listener.
     * Change the color of the cells.
     */
    private void colorListener() {

        colorSlider.addChangeListener(changeEvent -> {
            this.color = colorChosen(colorSlider.getValue());
            window.getRightPanel().updateColor(color);
        });
    }

    /**
     * Reset button listener.
     * Reset the game, creating a new Universe from scratch.
     */
    private void resetListener() {

        resetButton.addActionListener(event -> {
            if (pauseButton.isSelected()) {
                pauseButton.doClick();
            }
            isPaused = false;
            Universe.reset();
            this.universe = new Universe();
        });
    }

    /**
     * Speed slider listener.
     * Control the speed of evolutions.
     */
    private void speedListener() {

        speedSlider.addChangeListener(changeEvent -> {
            this.speed = 1100L - speedSlider.getValue() * 100;
            if (speed == 1000) {
                speedLabel.setText("One evolution per second");
            } else {
                speedLabel.setText(String.format("One evolution per %d ms", speed));
            }
        });
    }

    /**
     * Pause button listener.
     * Pause evolutions.
     */
    private void pauseListener() {
        // Pause button listener
        pauseButton.addActionListener(event -> {
            if (pauseButton.isSelected()) {
                pauseButton.doClick();
                isPaused = !isPaused;
                pauseButton.setText(isPaused ? "RESUME" : "PAUSE");
            }
        });
    }

    /**
     * Make this thread sleep for the specified amount of time.
     * @param time a number in ms.
     */
    private void goToSleep(Long time) {

        try {
            sleep(time);
        } catch (InterruptedException ex) {
            System.out.println("error");
        }
    }

    /**
     * Helper of colorListener().
     * @param num a number 0-9 corresponding to a color in the following order:
     *            white, yellow, orange, pink, red, green, cyan, blue, gray
     * @return one of 10 available colors.
     */
    private Color colorChosen(int num) {

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

