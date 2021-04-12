package life;

/**
 * MODEL of a Universe's single cell.
 * It has a certain position (coordinates):
 *    rowPos = Universe's matrix X-axis position
 *    colPos = Universe's matrix Y-axis position
 * It may either be alive [designated 'O'] or dead [designated ' ']
 * (see Schrödinger :P), and it can evolve, forming the shape of
 * the next Universe generation. based on certain evolution rules
 * relative to its neighboring cells' state.
 */
public class Cell {

    private final int rowPos;
    private final int colPos;
    private char state;
    private int aliveNeighborsCount = -1;
    private int deadNeighborsCount = -1;

    Cell(int rowPos, int colPos) {
        this.rowPos = rowPos;
        this.colPos = colPos;
    }

    Cell(int rowPos, int colPos, char state) {
        this(rowPos, colPos);
        this.state = state;
    }

    /**
     * Deep copy this cell.
     * @return a new Cell object.
     */
    public Cell copy() {

        Cell newCell = new Cell(rowPos, colPos);
        newCell.setAliveNeighborsCount(aliveNeighborsCount);
        newCell.setDeadNeighborsCount(deadNeighborsCount);
        newCell.setState(state);
        return newCell;
    }

    /**
     * Setter.
     * @param count amount of alive neighbors.
     */
    void setAliveNeighborsCount(int count) {

        this.aliveNeighborsCount = count;
    }

    /**
     * Setter.
     * @param count amount of alive neighbors.
     */
    void setDeadNeighborsCount(int count) {

        this.deadNeighborsCount = count;
    }

    /**
     * Getter.
     * @return this Cell's state (Alive: 'O' or Dead: ' ')
     */
    char getState() {

        return state;
    }

    /**
     * Setter.
     * @param state the current Cell's state (Alive: 'O' or Dead: ' ').
     */
    void setState(char state) {

        this.state = state;
    }

    /**
     * Getter
     * @return this cell's position based on its Universe X-axis
     */
    int getRowPos() {

        return rowPos;
    }

    /**
     * Getter
     * @return this cell's position based on its Universe Y-axis
     */
    int getColPos() {

        return colPos;
    }

    /**
     * Change the state of this Cell according to the Universe evolution rules.
     * See class Universe.
     */
    void evolve() {

        if (state == 'O') {
            setState(aliveNeighborsCount == 2 || aliveNeighborsCount == 3 ? 'O' : ' ');
        } else {
            setState(aliveNeighborsCount == 3 ? 'O' : ' ');
        }
    }
}