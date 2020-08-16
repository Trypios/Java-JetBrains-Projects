package life;

import java.util.ArrayList;
import java.util.Random;


public class Universe {
/*
    MODEL
    =====
    Universe consists of alive and dead Cells
    arranged in a square 2D Cell array (matrix) of a certain height (rows) and width (cols)
    Cells' state (alive or dead) is random for the 1st created universe,
    then their fate is decided based on certain evolution rules ( see last method evolve() )
*/

    public static final ArrayList<Universe> allUniverses = new ArrayList<>();
    private static final Random randomizer = new Random();
    private final int rows;
    private final int cols;
    private final Cell[][] matrix;
    private int generation = 1;

    public Universe(){
        this(matrixFromInput());
    }

    public Universe(Cell[][] matrix) {
        this.matrix = copyOfMatrix(matrix);
        this.rows = matrix.length;
        this.cols = matrix[0].length;
        allUniverses.add(this);
    }

    public static Cell[][] matrixFromInput() {
//        Initializes a 2D Cell array 20x20 with cells randomly alive or dead
        int size = 40;
        Cell[][] newMatrix = new Cell[size][size];
        randomizeCellArray(newMatrix);
        return newMatrix;
    }

    public static void randomizeCellArray(Cell[][] array) {
//        takes a 2D Cell array and randomly sets cell state to either alive or dead
        for (int r = 0; r < array.length; r++) {
            for (int c = 0; c < array[0].length; c++) {
                Cell currentCell = new Cell(r, c);
                boolean cellState = randomizer.nextBoolean();
                currentCell.setState(cellState ? 'O' : ' ');
                array[r][c] = currentCell;
            }
        }
    }

    public static Universe getCurrentUniverse() {
//        returns the current Universe generation, the last in stack (allUniverses)
        if (allUniverses.size() >= 1) {
            return allUniverses.get(allUniverses.size() - 1);
        }
        return null;
    }

    public int getRows() {
//        returns current Universe width
        return rows;
    }

    public int getCols() {
//        returns current Universe height
        return cols;
    }

    public int getGeneration() {
//        returns generation No of current Universe
        return generation;
    }

    public void setGeneration(int generation) {
//        sets generation No for current Universe (after evolving)
        this.generation = generation;
    }

    private static Cell[][] copyOfMatrix(Cell[][] matrix) {
//        returns a deep copy of the 2D matrix
        int size = matrix.length;
        Cell[][] newMatrix = new Cell[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                newMatrix[r][c] = matrix[r][c].copy();
            }
        }
        return newMatrix;
    }

    public Cell[][] getMatrix() {
//        returns 2D cell array of current Universe
        return matrix;
    }

    public Cell[] getCells() {
//        returns depp copy array of all Cells in current Universe (to be able and iterate through)
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

    public void countCellNeighbors(Cell currentCell) {
/*
        sets the alive/dead cell counters
        based on each cell's coordinates and neighbors
*/

//        pinpoint surrounding cells and put to an array
        int rowCoor = currentCell.getRowPos();
        int colCoor = currentCell.getColPos();
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
//        count alive & dead neighbors
        int alive = 0;
        int dead = 0;
        for (Cell cell : neighborCells) {
            if (cell.getState() == 'O') {
                alive++;
            } else if (cell.getState() == ' ') {
                dead++;
            }
        }
        currentCell.setAliveNeighborsCount(alive);
        currentCell.setDeadNeighborsCount(dead);
    }

    public int countAliveCells() {
//       returns the number of alive cells in current Universe
        int counter = 0;
        for (Cell cell : getCells()) {
            if (cell.getState() == 'O') {
                counter++;
            }
        }
        return counter;
    }

    public void displayUniverse() {
//        prints current Universe's cells' state to the terminal
        System.out.println("Generation #" + generation);
        System.out.println("Alive: " + countAliveCells());
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.printf("%c", matrix[r][c].getState());
            }
            System.out.println();
        }
        System.out.println();
    }

    public void evolve() {
/*
        Changes Universe state based on the following evolution rules:
        1. An alive cell survives if has two or three alive neighbors
            otherwise, it dies of boredom (<2) or overpopulation (>3)
        2. A dead cell is reborn if it has exactly three alive neighbors

        The resulting Universe's state is then saved to stack (allUniverses)
*/

        generation++;

//        make sure all neighbors were counted for each cell BEFORE they evolve
        for (Cell cell : getCells()) {
            countCellNeighbors(cell);
        }

//        with all neighbors counted, change cell state
//        according to aforementioned evolution rules
        for (Cell cell : getCells()) {
            cell.evolve();
        }

//        deep-copy and save the next-generation-universe
        Universe evolvedUniverse = new Universe(copyOfMatrix(this.matrix));
        evolvedUniverse.setGeneration(generation);
    }
}
