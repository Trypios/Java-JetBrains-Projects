package minesweeper;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Minesweeper game controller.
 */
public class GameController {

    private static final Scanner scanner = new Scanner(System.in);
    private static Minesweeper game;

    /**
     * Run this method to start the game.
     */
    public static void play() {

        // initialize game
        game = createNewGame();
        game.displayField(false);
        initialPlay();

        // play in a loop until gameover
        while (!game.isGameOver()) {
            game.displayField(false);
            makeMove();
        }

        // show results of gameover
        game.displayField(true);
        if (game.isWon()) {
            System.out.println("Congratulations! You found all bombs!");
        } else if (game.isLost()) {
            System.out.println("You stepped on a bomb and failed!");
        }

        scanner.close();
    }

    /**
     * Request input from user (level of difficulty) to create a new game.
     * @return a new Minesweeper object
     */
    private static Minesweeper createNewGame() {

        int width = 0, height = 0, bombs = 0;
        int mode = getGameMode();

        switch (mode) {
            case 1:
                height = 9;
                width = 9;
                bombs = 10;
                break;
            case 2:
                height = 16;
                width = 16;
                bombs = 40;
                break;
            case 3:
                height = 16;
                width = 30;
                bombs = 99;
                break;
            case 4:
                int[] size = customGameSize();
                height = size[0];
                width = size[1];
                bombs = customGameMineCount(height, width);
                break;
        }

        return new Minesweeper(width, height, bombs);
    }

    /**
     * Helper of createNewGame().
     * Request user input concerning the game's difficulty
     * (easy, medium, hard, custom).
     * @return a number 1-4 signifying difficulty.
     */
    private static int getGameMode() {

        while (true) {
            int mode;
            System.out.println("Select game mode: ");
            System.out.println("1 - Easy: 9 x 9 grid, 10 bombs");
            System.out.println("2 - Medium: 16 x 16 grid, 40 bombs");
            System.out.println("3 - Hard: 16 x 30 grid, 99 bombs");
            System.out.println("4 - Custom (select grid size and number of bombs)");
            System.out.print("Select game mode: ");

            String input = scanner.nextLine();

            switch (input.toLowerCase()) {
                case "easy":
                    return 1;
                case "medium":
                    return 2;
                case "hard":
                    return 3;
                case "custom":
                    return 4;
                default:
                    try {
                        mode = Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selection...");
                        continue;
                    }
            }

            if (mode >= 1 && mode <= 4) {
                return mode;
            } else {
                System.out.println("Invalid selection...");
            }
        }
    }

    /**
     * Helper of createNewGame().
     * Request user input concerning the minefield's dimensions
     * (height and width of the grid).
     * @return an integer array of two numbers
     *     1st number = count of rows
     *     2nd number = count of columns
     */
    private static int[] customGameSize() {

        int height, width;
        while (true) {
            System.out.print("Select grid size (height x width) ");
            String[] input = scanner.nextLine().split("\\s");
            if (input.length != 2) {
                System.out.println("Invalid input. Enter 2 numbers (height and width) separated by space");
                continue;
            }
            height = Integer.parseInt(input[0]);
            width = Integer.parseInt(input[1]);
            if (height < 9 || width < 9 || height > 100 || width > 100) {
                System.out.println("Sorry, grid must be bigger than 9 x 9 and smaller than 100 x 100");
                continue;
            }
            return new int[]{height, width};
        }
    }

    /**
     * Helper of createNewGame().
     * Request user input concerning the count of landmines.
     * The minefield can be covered by at most 80% of landmines and
     * must have at least one.
     * @param height count of rows in minefield.
     * @param width count of columns in minefield.
     * @return the count of landmines.
     */
    private static int customGameMineCount(int height, int width) {

        int bombs;
        while (true) {
            System.out.print("How many bombs do you want on the field? ");
            try {
                bombs = Integer.parseInt(scanner.nextLine().split("\\s")[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input...");
                continue;
            }

            if (bombs >= (height * width * 80) / 100) {
                System.out.println("You selected more bombs than the field can hold!");
                continue;
            } else if (bombs == 0) {
                System.out.println("The field must have at least 1 bomb!");
                continue;
            }
            return bombs;
        }
    }

    /**
     * Display information about gameplay.
     */
    private static void howToPlay() {
        System.out.println("Input horizontal and vertical coordinates,");
        System.out.println("then 'free' or 'bomb', separated by space.");
        System.out.println("For example: '5 5 free' or '6 3 bomb'\n");
        System.out.println("If a tile is marked incorrectly as bomb, input 'bomb' again to un-mark it");
        System.out.println("For example: if tile 3 7 is marked, input '3 7 bomb'\n");
    }

    /**
     * Request user input for the coordinates of their next move
     * and the action to be taken (open a cell or mark it as landmine).
     * @return an array of two coordinates.
     */
    private static String[] inputMove() {
        int row;
        int col;
        String mode;

        while (true) {
            System.out.print("Set/unset bombs marks or claim a cell as free: ");
            try {
                String[] input = scanner.nextLine().split("\\s");
                row = Integer.parseInt(input[0]) - 1;
                col = Integer.parseInt(input[1]) - 1;
                mode = input[2];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e1) {
                System.out.println("Invalid input...\n");
                howToPlay();
                continue;
            }

            boolean outOfBoundsX = col > game.getCols() - 1 || col < 0;
            boolean outOfBoundsY = row > game.getCols() - 1 || row < 0;
            if (outOfBoundsX && outOfBoundsY) {
                System.out.println("Both coordinates exceeded grid bounds");
                continue;
            } else if (outOfBoundsX) {
                System.out.println("Horizontal coordinate exceeded grid bounds");
                continue;
            } else if (outOfBoundsY) {
                System.out.println("Vertical coordinate exceeded grid bounds");
                continue;
            }

            if ("free".equals(mode) || "bomb".equals(mode)) {
                return new String[]{Integer.toString(row), Integer.toString(col), mode};
            } else {
                System.out.println("Invalid mode selection\n");
                howToPlay();
            }
        }
    }

    /**
     * Method similar to play().
     * It will only be used for the user's initial move.
     * The user will start the game by opening a cell blindly
     * and this method prevents that cell from being a landmine.
     */
    private static void initialPlay() {

        boolean breakLoop = false;

        while (!breakLoop) {

            // get user input
            howToPlay();
            String[] input = inputMove();
            int col = Integer.parseInt(input[0]);
            int row = Integer.parseInt(input[1]);
            String mode = input[2];

            Cell selectedCell = game.getCellByCoor(row, col);

            switch (mode) {
                case "bomb":
                    game.markMine(selectedCell);
                    game.displayField(false);
                    break;
                case "free":
                    selectedCell.setOpen();
                    game.initRandomMines();
                    game.initMarkCells();
                    List<Cell> openedCells = new LinkedList<>();
                    game.openCellsRecur(selectedCell, openedCells);
                    breakLoop = true;
                    break;
            }
        }
    }

    /**
     * User makes a move by inputting coordinates (two numbers).
     * They may either open a cell, or mark a landmine.
     */
    private static void makeMove() {

        // get user input
        String[] input = inputMove();
        int col = Integer.parseInt(input[0]);
        int row = Integer.parseInt(input[1]);
        String mode = input[2];

        Cell selectedCell = game.getCellByCoor(row, col);

        switch (mode) {
            case "bomb":
                game.markMine(selectedCell);
                break;
            case "free":
                game.openCell(selectedCell);
                break;
        }
    }
}
