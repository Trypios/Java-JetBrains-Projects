package life;


/**
 * Another thread working inside main Controller,
 * to update the stats of the Universe:
 *     generation label,
 *     alive label,
 *     grid of cells
 */
class Controller2 extends Thread {

    private final Universe universe;
    private final GameOfLife window;

    Controller2(Universe universe, GameOfLife window) {

        this.universe = universe;
        this.window = window;
    }

    /**
     * Starts this thread.
     */
    @Override
    public void run() {

        // updates generation label
        window
                .getLeftPanel()
                .getGenerationLabel()
                .setText("Generation #" + Universe.getGenerations());
        window
                .getLeftPanel()
                .getGenerationLabel()
                .repaint();

        // updates alive label
        window
                .getLeftPanel()
                .getAliveLabel()
                .setText("Alive: " + universe.countAliveCells());
        window
                .getLeftPanel()
                .getAliveLabel()
                .repaint();

        // updates grid of cells
        window
                .getRightPanel()
                .updateCellArray(universe.getMatrix());
        window
                .getRightPanel()
                .repaint();
    }
}