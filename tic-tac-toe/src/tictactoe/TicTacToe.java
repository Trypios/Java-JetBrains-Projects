package tictactoe;

import java.util.Scanner;

public class TicTacToe {

    private final char[][] grid;
    private char currentPlayer;

    public TicTacToe() {
        this.grid = createGrid();
        this.currentPlayer = 'X';
    }

    /**
     * Run this method to play the game.
     */
    public void play() {

        int[] coordinates;
        boolean active = true;

        while (active) {
            displayBoard();
            coordinates = getCoordinates();
            editBoard(coordinates);
            if (hasWinner()) {
                System.out.printf("%s wins!%n", currentPlayer);
                active = false;
            } else if (isDraw()) {
                System.out.println("Draw...");
                active = false;
            } else {
                switchPlayer();
            }
        }
    }

    /**
     * Create a 3x3 empty grid for the game.
     * @return a 3x3 2D char array, where each character is a space.
     */
    private static char[][] createGrid() {
        // returns a 3x3 empty matrix
        char[][] matrix = new char[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                matrix[row][col] = ' ';
            }
        }
        return matrix;
    }

    /**
     * Pretty-print game's grid.
     */
    private void displayBoard() {
        char[][] g = this.grid;
        
        String border = "---------";
        System.out.println(border);
        System.out.println("| " + g[0][0] + " " + g[0][1] + " " + g[0][2] + " |");
        System.out.println("| " + g[1][0] + " " + g[1][1] + " " + g[1][2] + " |");
        System.out.println("| " + g[2][0] + " " + g[2][1] + " " + g[2][2] + " |");
        System.out.println(border);
        
    }

    /**
     * Ask user for coordinates as to make a move.
     * Expected: two numbers from 1-3.
     * @return an integer array of two numbers from 1-3.
     */
    private int[] getCoordinates() {

        Scanner scanner = new Scanner(System.in);
        boolean isCorrect;
        String input;
        String[] coords;
        int row;
        int col;
        while (true) {
            System.out.print("Enter the coordinates: ");
            input = scanner.nextLine();
            coords = input.split(" ", 0);
            isCorrect = validateCoordinates(coords);
            if (isCorrect) {
                // convert coordinates to matrix format
                row = (3 - Integer.parseInt(coords[1])) % 3;
                col = Integer.parseInt(coords[0]) - 1;
                // check if cell is empty
                if (grid[row][col] == ' ') {
                    return new int[] {row, col};
                }
                System.out.println("This cell is occupied! Choose another one!");
            }
        }
    }

    /**
     * Helper of getCoordinates(). Validates given coordinates.
     * @param coords a string array of two coordinates.
     * @return true if both coordinates in range 1-3 inclusive, else false.
     */
    private boolean validateCoordinates(String[] coords) {
        // check for valid input
        if (coords.length != 2) {
            System.out.println("You should enter numbers!");
            return false;
        }
        String validInput = "123";
        for (int i = 0; i < 2; i++) {
            if ((coords[i]).length() != 1) {
                System.out.println("You should enter single digits!");
                return false;
            } else if (!validInput.contains(coords[i])) {
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            }
        }
        return true;
    }

    /**
     * Change the grid based on current player's mark and their given coordinates.
     * @param coords Two coordinates from 1-3, in integer array form.
     */
    private void editBoard(int[] coords) {
    	/* change the board based on active player's sign and given coordinates */

        int row = coords[0];
        int col = coords[1];
        grid[row][col] = currentPlayer;
    }

    /**
     * Check board state for winner.
     * @return true if current player won, else false.
     */
    private boolean hasWinner() {
        char[][] g = grid;
        char m = currentPlayer;

        // check rows & cols
        for (int i = 0; i < 3; i++) {
            if ((m == g[i][0] && g[i][0] == g[i][1] && g[i][1] == g[i][2]) ||
                    (m == g[0][i] && g[0][i] == g[1][i] && g[1][i] == g[2][i])) {
                return true;
            }
        }
        // check main & side diagonal
        if ((m == g[0][0] && g[0][0] == g[1][1] && g[1][1] == g[2][2]) ||
                (m == g[0][2] && g[0][2] == g[1][1] && g[1][1] == g[2][0])) {
            return true;
        }
        return false;
    }

    /**
     * Check board state for draw.
     * @return false if any slot is empty, else true.
     */
    private boolean isDraw() {

        for (char[] row : grid) {
            for (int elem : row) {
                if (elem == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Swap current player's sign.
     */
    private void switchPlayer() {

        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

}
