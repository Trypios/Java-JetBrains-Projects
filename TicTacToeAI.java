import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

public class TicTacToeAI {

    static Scanner scanner = new Scanner(System.in);
    static Player player1;
    static Player player2;
    static Player activePlayer;
    static Player nextPlayer;
    private static boolean gameOn;
    private static boolean isWinner;
    private static boolean isDraw;

    public static void main(String[] args) {

        // INIT
        TicTacToe tictactoe = new TicTacToe();
        menu();  // set the players

        while (gameOn) {
            // show board
            tictactoe.printGrid();

            // make move
            tictactoe.makeMove(activePlayer, nextPlayer);
            
            // check if current player wins
            isWinner = tictactoe.isWinner(activePlayer.getSign());
            if (isWinner) {
                tictactoe.printGrid();
                System.out.printf("%c wins%n", activePlayer.getSign());
                break;
            }

            // check for draw
            isDraw = tictactoe.isDraw();
            if (isDraw) {
                tictactoe.printGrid();
                System.out.println("Draw");
                break;
            }

            // switch player turns
            swapPlayer();
        }
        scanner.close();
    }

    private static void menu() {
        gameOn = false;
        while (true) {
            System.out.println("Input command: ");
            String[] option = scanner.nextLine().split(" ", 0);
            if (option.length == 1 && option[0].equals("exit")) {
                break;
            } else if (option.length != 3) {
                System.out.println("Bad parameters!");
                continue;
            }
            switch (option[0]) {
                case "start":
                    break;
                default:
                    System.out.println("Bad parameters!");
                    continue;
            }
            switch (option[1]) {
                case "user":
                    player1 = new humanPlayer('X');
                    break;
                case "easy": case "medium": case "hard":
                    player1 = new aiPlayer('X', option[1]);
                    break;
                default:
                    System.out.println("Bad parameters!");
                    continue;
            }
            switch (option[2]) {
                case "user":
                    player2 = new humanPlayer('O');
                    break;
                case "easy": case "medium": case "hard":
                    player2 = new aiPlayer('O', option[2]);
                    break;
                default:
                    System.out.println("Bad parameters!");
                    continue;
            }
            break;
        }
        activePlayer = player1;
        nextPlayer = player2;
        gameOn = true;
    }

    public static void swapPlayer() {
        activePlayer = (activePlayer == player1) ? player2 : player1;
        nextPlayer = (nextPlayer == player1) ? player2 : player1;
    }

}

class Player {

    private char sign;
    private boolean human;
    private String difficulty;
    private Player opponent;

    public Player(char sign) {
        this.sign = sign;
    }
    
    public char getSign() {
        return sign;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}

class humanPlayer extends Player {

    public humanPlayer(char sign) {
        super(sign);
        setHuman(true);
    }

}

class aiPlayer extends Player {

    public aiPlayer(char sign, String difficulty) {
        super(sign);
        setDifficulty(difficulty);
        setHuman(false);
    }

}

class TicTacToe {

    Random random = new Random();
    char[] grid = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
    int bestPos;

    public void printGrid() {
        System.out.println("---------");
        System.out.printf("| %s %s %s |%n", this.grid[0], this.grid[1], this.grid[2]);
        System.out.printf("| %s %s %s |%n", this.grid[3], this.grid[4], this.grid[5]);
        System.out.printf("| %s %s %s |%n", this.grid[6], this.grid[7], this.grid[8]);
        System.out.println("---------");
    }

    public void makeMove(Player currentPlayer, Player opponent) {
        if (currentPlayer.isHuman()) {
            humanMove(currentPlayer.getSign());
        } else {
            String difficulty = currentPlayer.getDifficulty();
            System.out.printf("Making move level \"%s\"%n", difficulty);
            switch (difficulty) {
                case "easy":
                    aiMoveEasy(currentPlayer);
                    break;
                case "medium":
                    aiMoveMedium(currentPlayer, opponent);
                    break;
                case "hard":
                    aiMoveHard(currentPlayer, opponent);
                    break;
            }
        }
    }

    private void aiMoveEasy(Player currentPlayer) {
        // AI makes a random move
        char plSign = currentPlayer.getSign();
        while (true) {
            int pos = random.nextInt(9);
            if (this.grid[pos] == ' ') {
                this.grid[pos] = plSign;
                break;
            }
        }
    }

    private void aiMoveMedium(Player currentPlayer, Player opponent) {
        char plSign = currentPlayer.getSign();
        char oppSign = opponent.getSign();
        TicTacToe temp = new TicTacToe();

        // check for possible win next move
        for (int pos = 0; pos < 9; pos++) {
            temp.grid = (this.grid).clone();
            if (temp.grid[pos] == ' ') {
                temp.grid[pos] = plSign;
                if (temp.isWinner(plSign)) {
                    this.grid[pos] = plSign;
                    return;
                }
            }
        }

        // block opponent's possible win next move
        for (int pos = 0; pos < 9; pos++) {
            temp.grid = (this.grid).clone();
            if (temp.grid[pos] == ' ') {
                temp.grid[pos] = oppSign;
                if (temp.isWinner(oppSign)) {
                    this.grid[pos] = plSign;
                    return;
                }
            }
        }

        // eventually make a random move
        aiMoveEasy(currentPlayer);
    }

    private void aiMoveHard(Player currentPlayer, Player opponent) {
        TicTacToe temp = new TicTacToe();
        temp.grid = this.grid.clone();
        // minimax calculates the bestPos available
        temp.miniMax(currentPlayer);
        this.grid[temp.bestPos] = currentPlayer.getSign();
    }

    private int miniMax(Player currentPlayer) {

        // get array of available slots (indexes)
        int[] freeSlots = getFreeSlots();

        // base cases
        if (isWinner(Main.activePlayer.getSign())) {
            return 10;
        } else if (isWinner(Main.nextPlayer.getSign())) {
            return -10;
        } else if (freeSlots.length == 0) {
            return 0;
        }

        // store moves & scores in arrays
        int[] moves = new int[freeSlots.length];
        int[] scores = new int[freeSlots.length];

        int score = 0;
        for (int i = 0; i < freeSlots.length; i++) {
            this.grid[freeSlots[i]] = currentPlayer.getSign();
            if (currentPlayer == Main.activePlayer) {
                score = miniMax(Main.nextPlayer);
            } else {
                score = miniMax(Main.activePlayer);
            }
            // reset grid state
            this.grid[freeSlots[i]] = ' ';
            moves[i] = freeSlots[i];
            scores[i] = score;
        }

        // evaluation of minimax
        int bestScore;
        if (currentPlayer == Main.activePlayer) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] > bestScore) {
                    bestScore = scores[i];
                    this.bestPos = moves[i];
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < scores.length; i++) {
                if (scores[i] < bestScore) {
                    bestScore = scores[i];
                    this.bestPos = moves[i];
                }
            }
        }
        return bestScore;
    }

    private void humanMove(char sign) {
        String validCoordinates = "123";
        while (true) {
            String[] coordinates = Main.scanner.nextLine().split(" ", 0);
            if (coordinates.length == 2) {
                if (validCoordinates.contains(coordinates[0]) && validCoordinates.contains(coordinates[1])) {
                    int pos = 0;
                    int col = Integer.parseInt(coordinates[0]) - 1;
                    int row = Integer.parseInt(coordinates[1]) - 1;
                    switch (row) {
                        case 0: 
                            pos = 6 + col;
                            break;
                        case 1:
                            pos = 3 + col;
                            break;
                        case 2:
                            pos = 0 + col;
                            break;
                    }
                    if (this.grid[pos] == ' ') {
                        this.grid[pos] = sign;
                        break;
                    } else {
                        System.out.println("This cell is occupied! Choose another one!");
                        continue;
                    }
                } else if (coordinates[0].length() != 1 || coordinates[0].length() != 1) {
                    System.out.println("You should enter numbers!");
                } else {
                    System.out.println("Coordinates should be from 1 to 3!");
                }
            } else {
                System.out.println("You should enter numbers!");
            }
        }
    }

    public boolean isWinner(char sign) {
        boolean winner = false;
        // check rows & cols
        int row = 0;
        int col = 0;
        for (int i = 0; i < 3; i++) {
            if (sign == this.grid[row] && this.grid[row] == this.grid[row + 1] && this.grid[row + 1] == this.grid[row + 2]) {
                winner = true;
                break;
            }
            row += 3;
            if (sign == this.grid[col] && this.grid[col] == this.grid[col + 3] && this.grid[col + 3] == this.grid[col + 6]) {
                winner = true;
                break;
            }
            col++;
        }
        // check main diag & side diag
        if (sign == this.grid[4]) {
            if ((this.grid[0] == this.grid[4] && this.grid[4] == this.grid[8]) || 
                (this.grid[2] == this.grid[4] && this.grid[4] == this.grid[6])) {
                winner = true;
            }
        }
        return winner;
    }

    public boolean isDraw() {
        // check for empty cells
        for (char slot : this.grid) {
            if (slot == ' ') {
                return false;
            }
        }
        return true;
    }

    public int[] getFreeSlots() {
        // count empty cells
        int counter = 0;
        for (char slot : this.grid) {
            if (slot == ' ') {
                counter++;
            }
        }
        int[] freeIndexes = new int[counter];
        int index = 0;
        for (int i = 0; i < this.grid.length; i++) {
            if (this.grid[i] == ' ') {
                freeIndexes[index] = i;
                index++;
            }
        }
        return freeIndexes;
    }

}
