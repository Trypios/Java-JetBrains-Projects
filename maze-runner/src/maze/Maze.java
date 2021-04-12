package maze;

import java.io.Serializable;
import java.util.*;


/**
 * Model of a maze. It is of a square size,
 * consisting of various cells.
 * Each cell can either be a wall or a path.
 */
public class Maze implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private static final Random RANDOMIZER = new Random();

    private final int ROWS;
    private final int COLS;
    private final Cell[][] GRID;

    /**
     * Create a randomized maze according to specified dimensions.
     * @param size count of rows (x-axis) and columns (y-axis)
     */
    public Maze(int size) {

        ROWS = size;
        COLS = size;
        GRID = new Cell[ROWS][COLS];
        create();
    }

    /**
     * Create maze by iterative implementation of
     * 'randomized depth-first search'
     * (instead of a recursive back-tracker).
     */
    private void create() {

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
                if (makesLoop(neighbor, currentPath)) {
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

    /**
     * Helper of create(). Create initial empty grid.
     * Set its left, upper and lower sides of cells as walls
     * (right side will be set later).
     * Also set all even grid indexes as walls (pillars).
     */
    private void initGrid() {

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                GRID[r][c] = new Cell(r, c);
                if (r == 0 || r == ROWS - 1 || c == 0 || r % 2 == 0 && c % 2 == 0) {
                    GRID[r][c].setAsWall();
                }
            }
        }
    }

    /**
     * Helper of create(). Check if marking the specified cell as path,
     * would create a loop on the already concrete maze path.
     * The maze does not allow loops.
     * @param cell a Cell object.
     * @return true if the cell would create a path-loop, else false
     */
    private boolean connectsPaths(Cell cell) {

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

    /**
     * Helper of create(). While a maze is being created,
     * check if any neighboring cell (excluding one) is marked as a visited path,
     * meaning it would create an unwanted loop.
     * @param currentCell a cell currently being traversed.
     * @param excl the cell that lead to the current one.
     * @return true if the currentCell does not lead to a path-loop, else false.
     */
    private boolean makesLoop(Cell currentCell, Cell excl) {

        if (excl == null) {
            return false;
        }

        List<Cell> neighbors = cellNeighbors(currentCell);
        neighbors.remove(excl);
        for (Cell neighbor : neighbors) {
            if (neighbor.isPath()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper of escape(). Use while traversing a Maze path, to determine if all
     * neighboring cells (excluding the one coming from) are walls.
     * @param currentCell a cell currently being traversed.
     * @param excl the cell that lead to the current one.
     * @return true if the currentCell is not a dead end, else false.
     */
    private boolean isDeadEnd(Cell currentCell, Cell excl) {

        if (excl == null) {
            return false;
        }

        List<Cell> neighbors = cellNeighbors(currentCell);
        neighbors.remove(excl);
        for (Cell neighbor : neighbors) {
            if (neighbor.isPath()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper of escape(). Use while traversing a Maze path, to determine if out
     * of all neighboring cells (excluding the one coming from) only one is a path.
     * @param currentCell a cell currently being traversed.
     * @param excl the cell that lead to the current one.
     * @return true if the currentCell is a corridor, else false.
     */
    private boolean oneWayOnly(Cell currentCell, Cell excl) {
        /*
         * returns true if only one neighboring cell is Path
         * excluding the cell the path is continuing from
         * */

        if (excl == null) {
            return true;
        }

        List<Cell> neighbors = cellNeighbors(currentCell);
        neighbors.remove(excl);
        int counter = 0;
        for (Cell neighbor : neighbors) {
            if (neighbor.isPath()) {
                counter++;
            }
        }
        return counter == 1;
    }

    /**
     * Gather all cells adjacent to the specified cell.
     * @param cell a Maze cell.
     * @return a list of cells
     */
    private List<Cell> cellNeighbors(Cell cell) {

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

    /**
     * Fetch a cell from the Maze grid according to the specified coordinates.
     * @param x a column coordinate (x-axis)
     * @param y a row coordinate (y-axis)
     * @return a Maze cell if found, else null.
     */
    private Cell getCellByPos(int x, int y) {

        try {
            return GRID[x][y];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Print the maze as a 2D grid.
     */
    public void display() {

        System.out.println();
        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                System.out.print(cell.toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Find the shortest escape route and mark relevant cells as such.
     * The route is found using a queue by iteration and backtracking.
     */
    public void escape() {

        // reset all Path cells as 'unvisited'
        setCellsUnvisited();

        // find entrance
        Cell entrance = findEntrance();

        // find exit
        Cell exit = findExit();

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

    /**
     * Helper of escape().
     * Reset all cells being paths as unvisited.
     */
    private void setCellsUnvisited() {

        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                if (cell.isPath()) {
                    cell.setVisited(false);
                }
            }
        }
    }

    /**
     * Helper of escape(). Find the maze entrance.
     * @return a cell of the maze if found, else null.
     */
    private Cell findEntrance() {

        // search the left wall
        for (int r = 0; r < ROWS; r++) {
            if (GRID[r][0].isPath()) {
                return GRID[r][0];
            }
        }
        return null;
    }

    /**
     * Helper of escape(). Find the maze exit.
     * @return a cell of the maze if found, else null.
     */
    private Cell findExit() {

        // search the right wall
        for (int r = 0; r < ROWS; r++) {
            if (GRID[r][COLS - 1].isPath()) {
                return GRID[r][COLS - 1];
            }
        }
        return null;
    }

    /**
     * Remove marked escape path.
     */
    public void unmarkEscape() {

        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                cell.reset();
            }
        }
    }

    /**
     * Create a deep copy of this maze.
     * @return a new Maze object
     */
    @Override
    public Maze clone() {

        final Maze clone;
        try {
            clone = (Maze) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException("Maze cloning failed", e);
        }
        return clone;

//        Cell[][] newGrid = new Cell[ROWS][COLS];
//        for (int r = 0; r < ROWS; r++) {
//            for (int c = 0; c < COLS; c++) {
//                newGrid[r][c] = GRID[r][c].clone();
//            }
//        }
//        return new Maze(newGrid);
    }

    /**
     * @return A 2D visual representation of this maze.
     */
    @Override
    public String toString() {

        StringBuilder maze = new StringBuilder("\n");
        for (Cell[] row : GRID) {
            for (Cell cell : row) {
                maze.append(cell);
            }
            maze.append("\n");
        }
        maze.append("\n");
        return maze.toString();
    }
}
