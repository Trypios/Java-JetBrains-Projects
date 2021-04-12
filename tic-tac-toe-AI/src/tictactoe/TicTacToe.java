package tictactoe;

import java.util.Scanner;

public class TicTacToe {

    static Scanner scanner = new Scanner(System.in);
    private final Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player opponent;
    private boolean active;

    public TicTacToe() {
        this.board = new Board();
        initPlayers();
        this.active = true;
    }

    /**
     * Initialize two players.
     */
    private void initPlayers() {
        player1 = createPlayer("player1", Mark.X, getBoard());
        player2 = createPlayer("player2", Mark.O, getBoard());
        player1.setOpponent(player2);
        player2.setOpponent(player1);
        currentPlayer = player1;
        opponent = player2;
    }

    /**
     * Getter
     * @return TicTacToe's board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Print TicTacToe logo.
     */
    private void displayLogo() {
        String logo = "\n".repeat(40) + "X O X" + " ".repeat(13) + "X O X\n" +
                "O X O TIC-TAC-TOE O X O\n" +
                "X O X" + " ".repeat(13) + "X O X\n";
        System.out.println(logo);
    }

    /**
     * Swap current player and opponent (use after a turn).
     */
    private void swapPlayers() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        opponent = (opponent == player1) ? player2 : player1;
    }

    /**
     * Check if game has a winner, is a draw, or to continue.
     * If game ends, display an informative message.
     */
    private void checkGamestate() {

        if (board.isWinner(currentPlayer)) {
            System.out.printf("%s WINS!%n", currentPlayer);
            active = false;
        } else if (board.isDraw()) {
            System.out.println("Draw...");
            active = false;
        }
    }

    /**
     * Run this method to start the game.
     */
    public void play() {

        displayLogo();
        System.out.println(board);
        while (active) {
            int move = currentPlayer.move();
//            System.out.println(board.getAvailableSlots());
//            System.out.println(move);
            board.markCellAt(move, currentPlayer.getMark());
            System.out.println(board);
            checkGamestate();
            swapPlayers();
        }
        System.out.println("Thank you for playing !!");
    }

    /**
     * Player factory.
     * Request user input for options to determine a type of player to be created
     * (human or cpu, and difficulty level if cpu).
     * @param name Player's name
     * @param mark Player's mark
     * @param board a TicTacToe board
     * @return a Player object
     */
    private static Player createPlayer(String name, Mark mark, Board board) {

        while (true) {
            String msg = "\n\nSelect " + name + ": \n" +
                    "  [Hu]man\n  [E]asy difficulty CPU\n  [M]edium difficulty CPU\n   [Ha]rd difficulty CPU\n";
            System.out.println(msg);
            String mode = scanner.nextLine().toLowerCase();
            switch (mode) {
                case "hu":
                    return new HumanPlayer(name, mark, board);
                case "e":
                    return new CpuPlayerEasy(name, mark, board);
                case "m":
                    return new CpuPlayerMedium(name, mark, board);
                case "ha":
                    return new CpuPlayerHard(name, mark, board);
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    public static String input() {

        return scanner.nextLine();
    }
}
