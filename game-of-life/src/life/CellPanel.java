package life;

import javax.swing.*;

/**
 * VIEW of the Cell.
 * Main panel, displays all the Universe's cells on a 2D grid.
 */
public class CellPanel extends JPanel {

    private boolean alive;

    CellPanel() {
        super();
        setVisible(false);
    }

    CellPanel(boolean alive) {
        super();
        setAlive(alive);
    }

    /**
     * Setter.
     * @param alive true/false if represented cell is alive/dead.
     */
    void setAlive(boolean alive) {
        this.alive = alive;
        setVisible(this.alive);
    }

    /**
     * Getter.
     * Check the status of the represented cell.
     * @return true/false if represented cell is alive/dead.
     */
    boolean isAlive() {
        return alive;
    }
}
