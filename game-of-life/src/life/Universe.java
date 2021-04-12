package life;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * MODEL of a universe consisting of simple cells, arranged in a square 2D Cell array (matrix)
 * of a certain height (rows) and width (cols). Each cell can either be alive or dead.
 * The first time a universe is created, each cell's state is decided at random,
 * where for further generations their fate is decided upon certain evolution rules:
 *     - An alive cell survives if has 2-3 alive neighbors.
 *         Less than 2 neighbors and the cell dies from boredom.
 *         More than 3 neighbors and the cell dies from overpopulation
 *     - A dead cell is reborn if it has exactly 3 alive neighbors.
 * Each generation is saved in the ALL_GENERATIONS stack.
 *
 *
 */
 class Universe {

    private static final List<Universe> ALL_GENERATIONS;
    private static final Random randomizer;
    private static int generations;
    private final int rows;
    private final int cols;
    private final Cell[][] matrix;

    static {
        ALL_GENERATIONS = new ArrayList<>();
        randomizer = new Random();
        generations = 0;
    }

    Universe(){

        this(matrixFromInput());
    }

    Universe(Cell[][] matrix) {

        generations++;
        this.matrix = copyOfMatrix(matrix);
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        ALL_GENERATIONS.add(this);
    }

    /**
     * Initialize a 2D Cell array of size 20*20,
     * with cells randomly assigned as alive or dead.
     * @return a new Cell 2D array.
     */
    static Cell[][] matrixFromInput() {

        int size = 40;
        Cell[][] newMatrix = new Cell[size][size];
        randomizeCellArray(newMatrix);
        return newMatrix;
    }

    /**
     * Randomly set the state of each of the cells in the given array
     * as to either being alive or dead
     * @param array an array of cells.
     */
    static void randomizeCellArray(Cell[][] array) {

        for (int r = 0; r < array.length; r++) {
            for (int c = 0; c < array[0].length; c++) {
                Cell currentCell = new Cell(r, c);
                boolean cellState = randomizer.nextBoolean();
                currentCell.setState(cellState ? 'O' : ' ');
                array[r][c] = currentCell;
            }
        }
    }

    /**
     * Getter of the latest Universe generation.
     * @return the last Universe in the stack ALL_UNIVERSES.
     */
    static Universe getCurrentUniverse() {

        if (ALL_GENERATIONS.size() >= 1) {
            return ALL_GENERATIONS.get(ALL_GENERATIONS.size() - 1);
        }
        return null;
    }

    /**
     * Getter.
     * @return Universe height
     */
    int getRows() {

        return rows;
    }

    /**
     * Getter.
     * @return Universe width
     */
    int getCols() {

        return cols;
    }

    /**
     * Getter.
     * @return the number of Universe generations
     *     i.e. the generation number of current Universe.
     */
    static int getGenerations() {

        return generations;
    }

    /**
     * Getter.
     * @return the 2D cell array of current Universe
     */
    Cell[][] getMatrix() {

        return matrix;
    }

    /**
     * Create an array of all cells of the current Universe,
     * for the purposes of iteration. Shallow copy.
     * @return an array of cells.
     */
    private Cell[] cellsToArray() {

        int size = rows * cols;
        Cell[] allCells = new Cell[size];
        int i = 0;
        for (Cell[] row : matrix) {
            for (Cell cell : row) {
                allCells[i] = cell;
                i++;
            }
        }
        return allCells;
    }

    /**
     * Helper of evolve().
     * Out of all the cells surrounding the targetCell,
     * count how many are alive and how many are dead.
     * Update the relevant Cell's attributes.
     * @param targetCell a cell of this Universe.
     */
    private void countCellNeighbors(Cell targetCell) {

        // pinpoint surrounding cells and put to an array
        int rowCoor = targetCell.getRowPos();
        int colCoor = targetCell.getColPos();
        Cell[] neighborCells = {
                matrix[(rowCoor - 1 + rows) % rows][(colCoor - 1 + cols) % cols],  // NW
                matrix[(rowCoor - 1 + rows) % rows][colCoor],  // N
                matrix[(rowCoor - 1 + rows) % rows][(colCoor + 1) % cols],  // NE
                matrix[rowCoor][(colCoor - 1 + cols) % cols],  // W
                matrix[rowCoor][(colCoor + 1) % cols],  // E
                matrix[(rowCoor + 1) % rows][(colCoor - 1 + cols) % cols],  // SW
                matrix[(rowCoor + 1) % rows][colCoor],  // S
                matrix[(rowCoor + 1) % rows][(colCoor + 1) % cols]  // SE
        };
        // count alive & dead neighbors
        int alive = 0;
        int dead = 0;
        for (Cell cell : neighborCells) {
            if (cell.getState() == 'O') {
                alive++;
            } else if (cell.getState() == ' ') {
                dead++;
            }
        }
        targetCell.setAliveNeighborsCount(alive);
        targetCell.setDeadNeighborsCount(dead);
    }

    /**
     * Count the number of alive cells in current Universe.
     * @return the number of alive cells.
     */
    int countAliveCells() {

        int counter = 0;
        for (Cell cell : cellsToArray()) {
            if (cell.getState() == 'O') {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Deep-copy the specified matrix of cells.
     * @param matrix a 2D array of cells.
     * @return a deep copy of a matrix.
     */
    private static Cell[][] copyOfMatrix(Cell[][] matrix) {

        int size = matrix.length;
        Cell[][] newMatrix = new Cell[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                newMatrix[r][c] = matrix[r][c].copy();
            }
        }
        return newMatrix;
    }

    /**
     * Reset the static fields back to the beginning.
     */
    static void reset() {
        ALL_GENERATIONS.clear();
        generations = 0;
    }

    /**
     * Changes Universe generation and state of Cells based on evolution rules
     * (see class description).
     */
    void evolve() {

        // make sure all neighbors were counted for each cell BEFORE they evolve
        for (Cell cell : cellsToArray()) {
            countCellNeighbors(cell);
        }

        // with all neighbors counted, change cell state
        // according to aforementioned evolution rules
        for (Cell cell : cellsToArray()) {
            cell.evolve();
        }

        // deep-copy and save the next-generation-universe
        new Universe(copyOfMatrix(this.matrix));
    }

    /**
     * Create a visual representation of this Universe,
     * as a 2D rectangle grid of characters.
     * @return a string consisting of characters arranged in rows and columns.
     */
    @Override
    public String toString() {

        StringBuilder universe = new StringBuilder(
                String.format("Generation #%d%nAlive: %d%n", generations, countAliveCells())
        );
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                universe.append(String.format("%c", matrix[r][c].getState()));
            }
            universe.append("\n");
        }
        universe.append("\n");
        return universe.toString();
    }
}
