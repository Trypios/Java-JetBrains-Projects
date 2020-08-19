package minesweeper;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Minesweeper {

    /**
     * MODEL
     */

    private static final Random randomizer = new Random();
    private Cell[][] minefield;
    private ArrayList<Cell> allCells;
    private final int rows;
    private final int cols;
    private final int bombCount;
    private boolean gameOver = false;
    private boolean won = false;
    private boolean lost = false;

    public Minesweeper(int width, int height, int bombCount) {
        this.rows = height;
        this.cols = width;
        this.bombCount = bombCount;
        initEmptyField();
    }

    private void initEmptyField() {
        /*
        * Initializes the 2D-array 'minefield' with safe cells only
        * Also initializes the 'allCells' dynamic array and stores all cells in
        */

        this.minefield = new Cell[rows][cols];
        this.allCells = new ArrayList<>();

        for (int r = 0; r < minefield.length; r++) {
            for (int c = 0; c < minefield[0].length; c++) {
                minefield[r][c] = new Cell('.', r, c,false);
                allCells.add(minefield[r][c]);
            }
        }
    }

    public void initRandomMines() {
        /*
        * Assigns random cells from 'allCells' to be bombs
        *  Minefield gets updated as it is linked to 'allCells'
        */

        int index = 0;
        int counter = 0;
        while (counter < bombCount) {
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

    public void initMarkCells() {
        /*
        * Places character marks on each Cell of the game
        * Bomb = 'X'
        * Safe cell without neighboring bombs = '.'
        * Safe cell with neighboring bombs = 'number of of neighboring bombs'
        */

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

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public ArrayList<Cell> getNeighborsOf(Cell cell) {
        /*
        * Returns a list of a cell's valid neighbors
        * Has a helper function isValidNeighbor()
        * */

        int rowCoor = cell.getRowCoor();
        int colCoor = cell.getColCoor();
        ArrayList<Cell> validNeighbors = new ArrayList<>();

        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) {
                    continue;
                }
                if (isValidNeighbor(rowCoor + r, colCoor + c)) {
                    validNeighbors.add(minefield[rowCoor + r][colCoor + c]);
                }
            }
        }
        return validNeighbors;
    }

    private boolean isValidNeighbor(int rowCoor, int colCoor) {
        /*
        * helper of getNeighbors() method which checks Cells by coordinates
        * returns true if neighboring cell is valid
        * else returns false
        */

        return rowCoor >= 0 && rowCoor < minefield.length
                && colCoor >= 0 && colCoor < minefield[0].length;
    }

    public Cell getCellByCoor(int rowCoor, int colCoor) {
        /*
        * returns a cell from the 2D grid, by coordinates
        * */

        return minefield[rowCoor][colCoor];
    }

    public boolean markMine(int row, int col) {
        /*
         * Mark/un-mark a cell as bomb by given coordinates
         * */

        Cell selectedCell = getCellByCoor(row, col);
        return markMine(selectedCell);
    }

    public boolean markMine(Cell selectedCell) {
        /*
         * Mark/un-mark specified cell as bomb
         * */

        if (selectedCell.isOpen() && selectedCell.isNumbered()) {
            System.out.println("There is a number here!");
            return false;
        } else if (selectedCell.isOpen()){
            System.out.println("Cell is open thus can't be marked as bomb");
            return false;
        } else {
            selectedCell.setMarkedAsExplosive(!selectedCell.isMarkedAsExplosive());
            return true;
        }
    }

    public boolean openCell(int row, int col) {
        /*
         * Open specified cell
         * */

        Cell selectedCell = getCellByCoor(row, col);
        return openCell(selectedCell);
    }

    public boolean openCell(Cell selectedCell) {
        /*
         * Open a cell by given coordinates
         * */

        if (selectedCell.isOpen()){
            System.out.println("Cell already free!");
            return false;
        } else if (selectedCell.isMarkedAsExplosive()) {
            System.out.println("Cannot open this cell, It is marked as bomb!");
            return false;
        } else if (selectedCell.isExplosive()) {
            selectedCell.setMarker('#');
            setLost();
            return true;
        } else {
            List<Cell> openedCells = new LinkedList<>();
            openCellsRecur(selectedCell, openedCells);
        }
        return true;
    }

    public void openCellsRecur(Cell centerCell, List<Cell> openedCells) {
        /*
         * Recursively opens all neighboring free cells
         * to the one initially opened by the user (in openCell() method)
         * */


        // stop if cell was previously checked
        if (openedCells.contains(centerCell)) {
            return;
        }

        // stop if it's a number
        if (centerCell.isNumbered()) {
            centerCell.setOpen();
            openedCells.add(centerCell);
            return;
        }

        ArrayList<Cell> neighbors = getNeighborsOf(centerCell);

        // stop if a neighbor is explosive
        for (Cell neighbor : neighbors) {
            if (neighbor.isMarkedAsExplosive()) {
                neighbor.setMarkedAsExplosive(false);
            }
            if (neighbor.isExplosive()) {
                openedCells.add(centerCell);
                return;
            }
        }

        // mark cell as free and recursively check all its neighbors
        centerCell.setOpen();
        openedCells.add(centerCell);
        for (Cell neighbor : neighbors) {
            openCellsRecur(neighbor, openedCells);
        }
    }

    public boolean isGameOver() {
        /*
        * Returns true if the user marked correctly
        * all explosive cells (User Wins)
        * Or if the user opened a bomb (User Loses)
        * */


//        in case a openCell() used setLost()
//        after opening a bomb cell
        if (gameOver) {
            return true;
        }

//        Checks if user correctly marked all bombs
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

        if (correctlyMarkedMines == bombCount) {
            setWon();
            return true;
        }

//        Checks if user opened all non-explosive cells
        int openCellsCounter = 0;
        for (Cell cell : allCells) {
            openCellsCounter += cell.isOpen() ? 1 : 0;
        }
        if (openCellsCounter == rows * cols - bombCount) {
            setWon();
            return true;
        }

        return gameOver;
    }

    private void setWon() {
        gameOver = true;
        won = true;
    }

    public void setLost() {
        gameOver = true;
        lost = true;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isLost() {
        return lost;
    }

    public void displayField(boolean trueState) {
        /*
        * prints the character (marker) of each position of the 2D array minefield
        * where:
        * Bomb = 'X'
        * Unopened cell = '.'
        * Opened cell without neighboring bombs = '/'
        * Opened with neighboring bombs = 'number of of neighboring bombs'
        * Opened cell with bomb = '#'
        *
        * If trueState is set to false,
        * then unopened cells are shown as '.' to be hidden from the player
        */

        StringBuilder topline1 = new StringBuilder("  |");
        StringBuilder topline2 = new StringBuilder("  |");
        StringBuilder underline = new StringBuilder("--|");
        StringBuilder grid = new StringBuilder("");

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

    public void displayFieldSmall(boolean trueState) {
        /*
         * Bad representation of the field
         * just to pass Hyperskill's tests.
         * Don't use this, unless for the Jetbrains academy project
         * */


        StringBuilder topline = new StringBuilder(" |");
        StringBuilder underline = new StringBuilder("-|");
        StringBuilder grid = new StringBuilder("");

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
