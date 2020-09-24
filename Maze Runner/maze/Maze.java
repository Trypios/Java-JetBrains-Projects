package maze;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Maze implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Random RANDOMIZER = new Random();

    private final int ROWS;
    private final int COLS;
    private final Cell[][] GRID;


    public Maze(int rows, int columns) {

        ROWS = rows;
        COLS = columns;
        GRID = new Cell[ROWS][COLS];
        create();
    }

    public Maze(Cell[][] grid) {
        ROWS = grid.length;
        COLS = grid[0].length;
        GRID = grid;
    }

    private void initGrid() {
        /*
        * Creates initial empty grid
        * Sets left, upper and lower sides of cells as walls
        * Also sets walls at even indexes (corridors)
        * The right side will be set later
        * */

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                GRID[r][c] = new Cell(r, c);
                if (r == 0 || r == ROWS - 1 || c == 0 || r % 2 == 0 && c % 2 == 0) {
                    GRID[r][c].setAsWall();
                }
            }
        }
    }

    private void create() {
        /*
        * Creates maze by iterative implementation of
        * 'randomized depth-first search' (recursive back-tracker)
        * */

        initGrid();

        Deque<Cell> uncheckedCells = new ArrayDeque<>();

        // randomly choose an entrance from the left (first col, 0), but not a corner
        int startingIndex = RANDOMIZER.nextInt(ROWS - 2) + 1;
        Cell currentPath = GRID[startingIndex][0];
        uncheckedCells.offer(currentPath);

        while (!uncheckedCells.isEmpty()) {

            currentPath = uncheckedCells.pollLast();

            if (connectsPaths(currentPath)) {
                currentPath.setAsWall();
                continue;
            } else {
                currentPath.setAsPath();

                // if cell is at the rightmost side
                if (currentPath.getY() == COLS - 1) {
                    // set rest of the side as walls
                    for (int i = 0; i < ROWS; i++) {
                        if (currentPath.getX() != i) {
                            GRID[i][COLS - 1].setAsWall();
                        }
                    }
                }
            }

            // get cell's neighbors
            // if neighbor is valid, add to stack (uncheckedCells)
            // else set as wall

            // valid neighbor = a neighboring cells which does not lead to a path
            // (thus avoid creating a loop)
            List<Cell> neighbors = cellNeighbors(currentPath);
            List<Cell> validNeighbors = new ArrayList<>();

            for (Cell neighbor : neighbors) {
                if (hasPathNear(neighbor, currentPath)) {
                    neighbor.setAsWall();
                } else if (!uncheckedCells.contains(neighbor)) {
                    uncheckedCells.offer(neighbor);
                    validNeighbors.add(neighbor);
                }
            }

            // select a random neighboring cell to continue the path
            // if none is selected, backtracking occurs
            if (!validNeighbors.isEmpty()) {
                Cell randomCell = validNeighbors.get(RANDOMIZER.nextInt(validNeighbors.size()));

                // push it last in stack, to be selected first in the next iteration
                while (uncheckedCells.contains(randomCell)) {
                    uncheckedCells.remove(randomCell);
                }
                uncheckedCells.offer(randomCell);
            }
        }
    }

    private boolean connectsPaths(Cell cell) {
        /*
        * Returns true if the cell would create a loop
        * if set as path
        * */

        int r = cell.getX();
        int c = cell.getY();
        Cell upper = getCellByPos(r - 1, c);  // upper
        Cell lower = getCellByPos(r + 1, c);  // lower
        Cell leftSide = getCellByPos(r, c - 1);  // left
        Cell rightSide = getCellByPos(r, c + 1);  // right

        boolean horizontalConnection = leftSide != null && rightSide != null && leftSide.isPath() && rightSide.isPath();
        boolean verticalConnection = upper != null && lower != null && upper.isPath() && lower.isPath();

        return  horizontalConnection || verticalConnection;
    }

    private boolean hasPathNear(Cell cell, Cell excl) {
        /*
        * returns true if any neighboring cell is visited and marked as path
        * meaning it would create an unwanted loop.
        * The cell the path is continuing from is excluded
        * */

        if (excl == null) {
            return false;
        }

        List<Cell> neighbors = cellNeighbors(cell);
        neighbors.remove(excl);
        for (Cell neighbor : neighbors) {
            if (neighbor.isPath()) {
                return true;
            }
        }
        return false;
    }

    private boolean isDeadEnd(Cell cell, Cell excl) {
        /*
         * returns true if all neighboring cells are walls
         * excluding the cell the path is continuing from
         * */

        if (excl == null) {
            return false;
        }

        List<Cell> neighbors = cellNeighbors(cell);
        neighbors.remove(excl);
        for (Cell neighbor : neighbors) {
            if (neighbor.isPath()) {
                return false;
            }
        }
        return true;
    }

    private boolean oneWayOnly(Cell cell, Cell excl) {
        /*
         * returns true if only one neighboring cell is Path
         * excluding the cell the path is continuing from
         * */

        if (excl == null) {
            return true;
        }

        List<Cell> neighbors = cellNeighbors(cell);
        neighbors.remove(excl);
        int counter = 0;
        for (Cell neighbor : neighbors) {
            if (neighbor.isPath()) {
                counter++;
            }
        }
        return counter == 1;
    }

    private List<Cell> cellNeighbors(Cell cell) {
        /*
        * Returns a list of the cell's vertical & horizontal direct neighbors
        * */

        int r = cell.getX();
        int c = cell.getY();
        List<Cell> neighbors = new ArrayList<>();
        neighbors.add(getCellByPos(r - 1, c));  // upper
        neighbors.add(getCellByPos(r + 1, c));  // lower
        neighbors.add(getCellByPos(r, c - 1));  // left
        neighbors.add(getCellByPos(r, c + 1));  // right

        neighbors.removeIf(Objects::isNull);
        neighbors.removeIf(Cell::isVisited);

        return neighbors;
    }

    private Cell getCellByPos(int x, int y) {
        /*
        * Returns cell based on its coordinates in the grid
        * or null if out of grid
        * */

        try {
            return GRID[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void display() {
        /*
        * Text-based representation of the maze
        * */

        System.out.println();
        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                System.out.print(cell.toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    public void escape() {
        /*
        * Finds the shortest escape route
        * and marks relevant cells as such
        * */

        // reset all Path cells as unvisited
        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                if (cell.isPath()) {
                    cell.setVisited(false);
                }
            }
        }

        // find entrance
        Cell entrance = null;
        for (int r = 0; r < ROWS; r++) {
            if (GRID[r][0].isPath()) {
                entrance = GRID[r][0];
            }
        }

        // find exit
        Cell exit = null;
        for (int r = 0; r < ROWS; r++) {
            if (GRID[r][COLS - 1].isPath()) {
                exit = GRID[r][COLS - 1];
            }
        }

        Cell previousCell;
        Cell currentCell = entrance;
        currentCell.setAsEscape();

        ArrayDeque<Cell> crossroads = new ArrayDeque<>();
        ArrayDeque<Cell> path = new ArrayDeque<>();
        path.offer(currentCell);

        while (currentCell != exit) {

            assert currentCell != null;

            previousCell = path.peekLast();

            // check if cell has only one way forward
            if (oneWayOnly(currentCell, previousCell)) {

                // if cell was a crossroad, but all other roads lead to a dead-end
                crossroads.remove(currentCell);

                // go to next path
                currentCell.setAsEscape();
                path.offer(currentCell);
                currentCell = cellNeighbors(currentCell).get(0);
            }

            // check if cell is a dead-end
            else if (isDeadEnd(currentCell, previousCell)) {

                // pop cells from path stack until a crossroads
                Cell lastCrossroad = crossroads.peekLast();
                while (lastCrossroad != currentCell) {

                    assert currentCell != null;

                    currentCell.setAsEscape(false);
                    currentCell = path.pollLast();
                }

            // if cell is a 2-way or 3-way crossroad
            } else {

                // push to stack, as to back-track
                crossroads.offer(currentCell);

                // get cell's neighbors
                List<Cell> neighbors = cellNeighbors(currentCell);

                // check if any neighboring cell is the exit
                if (neighbors.contains(exit)) {
                    currentCell.setAsEscape();
                    currentCell = exit;
                    exit.setAsEscape();
                // else go to a randomly selected neighbor
                } else {
                    Cell randomCell = neighbors.get(RANDOMIZER.nextInt(neighbors.size()));
                    currentCell.setAsEscape();
                    path.offer(currentCell);
                    currentCell = randomCell;
                }
            }
        }
    }

    public Maze copyOf() {

        Cell[][] newGrid = new Cell[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            if (COLS >= 0) System.arraycopy(GRID[r], 0, newGrid[r], 0, COLS);
        }
        return new Maze(newGrid);
    }
}
