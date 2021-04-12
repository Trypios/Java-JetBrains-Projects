package minesweeper;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Model of a Minesweeper field.
 */
public class Minesweeper {

    private static final Random randomizer = new Random();
    private Cell[][] minefield;
    private ArrayList<Cell> allCells;
    private final int rows;
    private final int cols;
    private final int landmineCount;
    private boolean gameOver = false;
    private boolean won = false;
    private boolean lost = false;

    public Minesweeper(int width, int height, int bombCount) {
        this.rows = height;
        this.cols = width;
        this.landmineCount = bombCount;
        initEmptyField();
    }

    /**
     * Initialize the minefield 2D-array minefield with safe cells only.
     * Store said cells to the list allCells.
     */
    private void initEmptyField() {

        this.minefield = new Cell[rows][cols];
        this.allCells = new ArrayList<>();

        for (int r = 0; r < minefield.length; r++) {
            for (int c = 0; c < minefield[0].length; c++) {
                minefield[r][c] = new Cell('.', r, c,false);
                allCells.add(minefield[r][c]);
            }
        }
    }

    /**
     * Assign random cells to be explosive landmines.
     * Minefield gets updated as it is linked to 'allCells'
     */
    public void initRandomMines() {

        int index;
        int counter = 0;
        while (counter < landmineCount) {
            index = randomizer.nextInt(rows * cols);
            Cell currentCell = allCells.get(index);
            // if current cell is NOT explosive already,
            // or not marked as free (initial move), then it can be marked
            if (!currentCell.isExplosive() && !currentCell.isOpen()) {
                currentCell.setExplosive(true);
                counter++;
            }
        }
    }

    /**
     * Place character marks on each Cell of the game.
     *    Landmine = 'X'
     *    Unopened safe cell without adjacent landmines = '.'
     *    Unopened safe cell with adjacent landmines = '1-8' (number of of neighboring bombs)
     */
    public void initMarkCells() {

        for (Cell currentCell : allCells) {
            if (currentCell.isExplosive()) {
                currentCell.setMarker('X');
            } else {
                int counter = 0;
                for (Cell neighborCell : getNeighborsOf(currentCell)) {
                    counter += neighborCell.isExplosive() ? 1 : 0;
                }
                currentCell.setMarker(counter == 0 ? '.' : (char) (counter + 48));
            }
        }
    }

    /**
     * Getter
     * @return Minefield's count of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Getter
     * @return Minefield's count of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Find the adjacent cells surrounding a specified cell.
     * @param cell a Cell object
     * @return list of all cell's neighbors (adjacent cells)
     */
    public ArrayList<Cell> getNeighborsOf(Cell cell) {

        int rowCoor = cell.getRow();
        int colCoor = cell.getColumn();
        ArrayList<Cell> adjacent = new ArrayList<>();

        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) {
                    continue;
                }
                if (isValidCoord(rowCoor + r, colCoor + c)) {
                    adjacent.add(minefield[rowCoor + r][colCoor + c]);
                }
            }
        }
        return adjacent;
    }

    /**
     * Helper of getNeighbors().
     * Check given coordinates are valid (i.e. within the minefield).
     * @param rowCoor row coordinate
     * @param colCoor column coordinate
     * @return true if coordinates are within the minefield, else false.
     */
    private boolean isValidCoord(int rowCoor, int colCoor) {

        return rowCoor >= 0 && rowCoor < minefield.length
                && colCoor >= 0 && colCoor < minefield[0].length;
    }

    /**
     * Fetch a cell from the minefield.
     * @param rowCoor row coordinate
     * @param colCoor column coordinate
     * @return a Cell object
     */
    public Cell getCellByCoor(int rowCoor, int colCoor) {

        return minefield[rowCoor][colCoor];
    }

    /**
     * Mark/un-mark specified cell as landmine.
     * @param cell a cell from the minefield.
     */
    public void markMine(Cell cell) {

        if (cell.isOpen() && cell.isNumbered()) {
            System.out.println("There is a number here!");
        } else if (cell.isOpen()){
            System.out.println("Cell is open thus can't be marked as bomb");
        } else {
            cell.setMarkedAsExplosive(!cell.isMarkedAsExplosive());
        }
    }

    /**
     * Open a specified cell.
     * @param cell a cell from the minefield.
     */
    public void openCell(Cell cell) {

        if (cell.isOpen()){
            System.out.println("Cell already free!");
        } else if (cell.isMarkedAsExplosive()) {
            System.out.println("Cannot open this cell, It is marked as bomb!");
        } else if (cell.isExplosive()) {
            cell.setMarker('#');
            setLost();
        } else {
            List<Cell> openedCells = new LinkedList<>();
            openCellsRecur(cell, openedCells);
        }
    }

    /**
     * Helper of openCell().
     * Open all adjacent safe cells of the specified cell
     * @param cell a cell from the minefield.
     * @param openedCells a list to contain all safe adjacent cells already found by recursion.
     */
    public void openCellsRecur(Cell cell, List<Cell> openedCells) {

        // stop if cell was previously checked
        if (openedCells.contains(cell)) {
            return;
        }

        // stop if it's a number
        if (cell.isNumbered()) {
            cell.setOpen();
            openedCells.add(cell);
            return;
        }

        ArrayList<Cell> neighbors = getNeighborsOf(cell);

        // stop if a neighbor is explosive
        for (Cell neighbor : neighbors) {
            if (neighbor.isMarkedAsExplosive()) {
                neighbor.setMarkedAsExplosive(false);
            }
            if (neighbor.isExplosive()) {
                openedCells.add(cell);
                return;
            }
        }

        // mark cell as free and recursively check all its neighbors
        cell.setOpen();
        openedCells.add(cell);
        for (Cell neighbor : neighbors) {
            openCellsRecur(neighbor, openedCells);
        }
    }

    /**
     * Check if gameover whether because user won (marked correctly all landmines).
     * or lost (stepped on a landmine).
     * @return true if gameover, else false.
     */
    public boolean isGameOver() {

        // in case a openCell() used setLost()
        // after opening a bomb cell
        if (gameOver) {
            return true;
        }

        // Checks if user correctly marked all bombs
        int correctlyMarkedMines = 0;
        int badlyMarkedMines = 0;
        for (Cell cell : allCells) {
            if (cell.isMarkedAsExplosive()) {
                if (cell.isExplosive()) {
                    correctlyMarkedMines++;
                } else {
                    badlyMarkedMines++;
                }
            }
        }

        if (badlyMarkedMines != 0) {
            return false;
        }

        if (correctlyMarkedMines == landmineCount) {
            setWon();
            return true;
        }

        // Checks if user opened all non-explosive cells
        int openCellsCounter = 0;
        for (Cell cell : allCells) {
            openCellsCounter += cell.isOpen() ? 1 : 0;
        }
        if (openCellsCounter == rows * cols - landmineCount) {
            setWon();
            return true;
        }

        return gameOver;
    }

    /**
     * Setter
     * Mark game as won and end it.
     */
    private void setWon() {
        gameOver = true;
        won = true;
    }

    /**
     * Setter
     * Mark game as lost and end it.
     */
    public void setLost() {
        gameOver = true;
        lost = true;
    }

    /**
     * Getter
     * Check if game is won.
     * @return true if user won, else false.
     */
    public boolean isWon() {
        return won;
    }

    /**
     * Getter
     * Check if game is lost.
     * @return true if user lost, else false.
     */
    public boolean isLost() {
        return lost;
    }

    /**
     * Display the minefield as a rectangle grid or rows and columns,
     * where:
     *    Landmine = 'X'
     *    Unopened cell = '.'
     *    Opened cell without adjacent landmines = '/'
     *    Opened cell with adjacent landmines = '1-8' (number of of neighboring bombs)
     *    Opened cell that exploded = '#'
     * @param trueState Set to false to hind the game's unopened cells (marks them as '.').
     */
    public void displayField(boolean trueState) {

        StringBuilder topline1 = new StringBuilder("  |");
        StringBuilder topline2 = new StringBuilder("  |");
        StringBuilder underline = new StringBuilder("--|");
        StringBuilder grid = new StringBuilder();

        int tensDigit = 0;
        for (int c = 1; c <= minefield[0].length; c++) {
            tensDigit += (c % 10 == 0) ? 1 : 0;
            if (tensDigit == 0) {
                topline1.append("  ");
            } else {
                topline1.append(tensDigit).append(" ");
            }
            topline2.append(c % 10).append(" ");
            underline.append("--");
        }
        topline2.append("|");
        underline.append("|");

        int rowNumber = 1;
        char cellMark;
        for (Cell[] row : minefield) {
            if (rowNumber < 10) {
                grid.append(' ');
            }
            grid.append(rowNumber).append("|");
            for (Cell currentCell : row) {
                if (trueState) {
                    cellMark = currentCell.getMarker();
                } else {
                    cellMark = currentCell.isMarkedAsExplosive() ?
                            '*' : currentCell.isExplosive() ?
                            '.' : currentCell.isOpen() ?
                            currentCell.getMarker() : '.';
                }
                grid.append(cellMark).append(" ");
            }
            grid.append("|\n");
            rowNumber++;
        }

        System.out.println();
        System.out.println();
        System.out.println(topline1);
        System.out.println(topline2);
        System.out.println(underline);
        System.out.print(grid);
        System.out.println(underline);
    }

    /**
     * Bad representation of the field just to pass Hyperskill's tests.
     * Don't use this, unless for the Jetbrains academy project.
     * For more info see displayField().
     */
    public void displayFieldSmall(boolean trueState) {


        StringBuilder topline = new StringBuilder(" |");
        StringBuilder underline = new StringBuilder("-|");
        StringBuilder grid = new StringBuilder();

        for (int c = 1; c <= minefield[0].length; c++) {
            topline.append(c);
            underline.append("-");
        }
        topline.append("|");
        underline.append("|");

        int rowNumber = 1;
        char cellMark;
        for (Cell[] row : minefield) {
            grid.append(rowNumber).append("|");
            for (Cell currentCell : row) {
                if (trueState) {
                    cellMark = currentCell.getMarker();
                } else {
                    cellMark = currentCell.isMarkedAsExplosive() ?
                            '*' : currentCell.isExplosive() ?
                            '.' : currentCell.isOpen() ?
                            currentCell.getMarker() : '.';
                }
                grid.append(cellMark);
            }
            grid.append("|\n");
            rowNumber++;
        }

        System.out.println();
        System.out.println(topline);
        System.out.println(underline);
        System.out.print(grid);
        System.out.println(underline);
    }
}
