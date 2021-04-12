package life;

import javax.swing.*;
import java.awt.*;

/**
 * VIEW of the Game and its controls.
 * Has several buttons, sliders and labels.
 */
public class ControlPanel extends JPanel {

    private final JToggleButton pauseButton;
    private final JButton resetButton;
    private final JTextPane textPane;
    private final JLabel generationLabel;
    private final JLabel aliveLabel;
    private final JLabel speedLabel;
    private final JSlider speedSlider;
    private final JSlider colorSlider;

    ControlPanel() {

        super();
        setName("Control Panel");
        setLayout(new BorderLayout(5, 0));

//        Button #1:
        pauseButton = new JToggleButton();
        pauseButton.setText("PAUSE");
        pauseButton.setName("PlayToggleButton");
//        Button #2:
        resetButton = new JButton();
        resetButton.setText("RESET");
        resetButton.setName("ResetButton");
//        Text pane:
        textPane = new JTextPane();
        textPane.setText(description());
//        Label #1:
        generationLabel = new JLabel("Generation #0");
        generationLabel.setName("GenerationLabel");
//        Label #2:
        aliveLabel = new JLabel("Alive: 0");
        aliveLabel.setName("AliveLabel");
//        Slider (for speed)
        speedSlider = new JSlider(1, 10, 5);
        speedSlider.setPaintTrack(true);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setName("SpeedSlider");
//        Label #3:
        speedLabel = new JLabel();
        speedLabel.setText(String.format("One evolution per %d ms",
                1100 - speedSlider.getValue() * 100));
        speedLabel.setName("SpeedLabel");
//        Slider (for colors)
        colorSlider = new JSlider(0, 9, 9);
        colorSlider.setPaintTrack(true);
        colorSlider.setPaintTicks(true);
        colorSlider.setPaintLabels(true);
        colorSlider.setMinorTickSpacing(1);
        colorSlider.setName("ColorSlider");

//        Button panel for the previously created buttons
        JPanel buttonSection = new JPanel(new GridLayout(1, 2, 10, 20));
        buttonSection.add(pauseButton);
        buttonSection.add(resetButton);

//        Label panel for the previously created labels
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.add(generationLabel);
        labelPanel.add(aliveLabel);

//        Middle section stores text pane and labels
        JPanel middleSection = new JPanel(new GridLayout(2, 1));
        middleSection.add(textPane);
        middleSection.add(labelPanel);

//        Slider panel for the previously created sliders
        JPanel sliderPanel = new JPanel (new GridLayout(3, 1));
        sliderPanel.add(speedLabel);
        sliderPanel.add(speedSlider);
        sliderPanel.add(colorSlider);

//        Add components to main layout
        add(buttonSection, BorderLayout.NORTH);
        add(middleSection, BorderLayout.CENTER);
        add(sliderPanel, BorderLayout.SOUTH);

    }

    /**
     * A how-to-play description, to be viewed from the panel.
     * @return text
     */
    private String description() {
        String description = "A 50 x 50 universe of\n";
        description += "randomly created cells\n";
        description += "constantly evolves according\n";
        description += "to the following rules:\n";
        description += " * An alive cell survives\n";
        description += "if has 2-3 alive neighbors\n";
        description += "otherwise it dies of either\n";
        description += "boredom or overpopulation\n";
        description += " * A dead cell is reborn\n";
        description += "if it has exactly three\n";
        description += "alive neighbors";
        return description;
    }

    /**
     * Getter
     * @return Control panel's pause-game button.
     */
    JToggleButton getPauseButton() {

        return pauseButton;
    }

    /**
     * Getter
     * @return Control panel's reset-game button.
     */
    JButton getResetButton() {
        return resetButton;
    }

    /**
     * Getter
     * @return Control panel's Universe-#generation label.
     */
    JLabel getGenerationLabel() {

        return generationLabel;
    }

    /**
     * Getter
     * @return Control panel's alive-Cells-count label.
     */
    JLabel getAliveLabel() {
        return aliveLabel;
    }

    /**
     * Getter
     * @return Control panel's generation-speed label.
     */
    JLabel getSpeedLabel() {
        return speedLabel;
    }

    /**
     * Getter
     * @return Control panel's generation-speed slider.
     */
    JSlider getSpeedSlider() {
        return speedSlider;
    }

    /**
     * Getter
     * @return Control panel's alive-cell-color slider.
     */
    JSlider getColorSlider() {
        return colorSlider;
    }

}
