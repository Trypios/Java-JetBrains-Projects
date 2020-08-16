package life;

import javax.swing.*;


public class GameGridPanel extends JPanel {

    private boolean alive;

    public GameGridPanel() {
        super();
        setVisible(false);
    }

    public GameGridPanel(boolean alive) {
        super();
        setAlive(alive);
    }

    //sets living/dead status of cell
    public void setAlive(boolean alive) {
        this.alive = alive;
        setVisible(this.alive);
    }

    //returns living/dead status of cell
    public boolean isAlive() {
        return alive;
    }
}
