import java.util.Scanner;

public class TicTacToe {
    
    public static void main(String[] args) {
    	/* MAIN */

        // init
        int[] coordinates = new int[2];
        char[][] board = getGrid();
        char player = 'X';
        String gameState;
        
        // game on
        while (true) {
            printBoard(board);
            coordinates = getCoordinates(board);
            board = editBoard(player, coordinates, board);
            gameState = checkBoard(player, board);
            if (gameState.equals("unfinished")) {
                player = switchPlayer(player);
            } else {
                printBoard(board);
                System.out.println(gameState);
                break;
            }
        }
    }
    
    public static char[][] getGrid() {
        // returns a 3x3 empty matrix
        char[][] matrix = new char[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                matrix[row][col] = ' ';
            }
        }
        return matrix;
    }
    
    public static String checkBoard(char sign, char[][] matrix) {
        /* check board state (winner, draw, unfinished) */
        
        boolean winner = false;

        // check rows & cols
        for (int i = 0; i < 3; i++) {
            if ((sign == matrix[i][0] && matrix[i][0] == matrix[i][1] && matrix[i][1] == matrix[i][2]) ||
                (sign == matrix[0][i] && matrix[0][i] == matrix[1][i] && matrix[1][i] == matrix[2][i])) {
                winner = true;
            }
        }
        // check main & side diag
        if ((sign == matrix[0][0] && matrix[0][0] == matrix[1][1] && matrix[1][1] == matrix[2][2]) || 
            (sign == matrix[0][2] && matrix[0][2] == matrix[1][1] && matrix[1][1] == matrix[2][0])) {
            winner = true;
        }
        if (winner) {
            return sign + " wins";
        } else {
            // check for empty cells
            int counterEmpty = 0;
            for (char[] row : matrix) {
                for (int elem : row) {
                    if (elem == ' ') {
                        counterEmpty++;
                    }
                }
            }
            if (counterEmpty > 0) {
                return "unfinished";
            } else {
                return "Draw";
            }
        }
        
    }

    public static void printBoard(char[][] array) {
    	/* pretty-prints board as grid*/
        
        String border = "---------";
        System.out.println(border);
        System.out.println("| " + array[0][0] + " " + array[0][1] + " " + array[0][2] + " |");
        System.out.println("| " + array[1][0] + " " + array[1][1] + " " + array[1][2] + " |");
        System.out.println("| " + array[2][0] + " " + array[2][1] + " " + array[2][2] + " |");
        System.out.println(border);
        
    }
    
    public static int[] getCoordinates(char[][] matrix) {
    	/* get user inputted coordinates (two numbers from 1-3) for next move */

        Scanner scanner = new Scanner(System.in);
        String validInput = "123";
        boolean isCorrect;
        String input;
        String[] array;
        int row;
        int col;
        while (true) {
            isCorrect = true;
            System.out.print("Enter the coordinates: ");
            input = scanner.nextLine();
            array = input.split(" ", 0);
            // check for valid input
            if (array.length != 2) {
                System.out.println("You should enter numbers!");
            } else {
                for (int i = 0; i < 2; i++) {
                    if ((array[i]).length() != 1) {
                        System.out.println("You should enter numbers!");
                        isCorrect = false;
                        break;
                    } else if (!validInput.contains(array[i])) {
                        System.out.println("Coordinates should be from 1 to 3!");
                        isCorrect = false;
                        break;
                    }
                }
                if (isCorrect) {
                    // convert coordinates to matrix format
                    row = (3 - Integer.parseInt(array[1])) % 3;
                    col = Integer.parseInt(array[0]) - 1;
                    // check if cell is empty
                    if (matrix[row][col] == ' ') {
                        return new int[] {row, col};
                    }
                    System.out.println("This cell is occupied! Choose another one!");

                }
            }

        }
    }
    
    public static char[][] editBoard(char sign, int[] coords, char[][] matrix) {
    	/* change the board based on active player's sign and given coordinates */

        int row = coords[0];
        int col = coords[1];
        matrix[row][col] = sign;
        return matrix;
    }
    
    public static char switchPlayer(char sign) {
    	/* switch player's sign */
    	
        if (sign == 'X') {
            return 'O';
        } else {
            return 'X';
        }  
    }

}
