package tictactoe;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to a game of Tic Tac Toe!");
        System.out.println("You need two players to play, each can input two numbered coordinates (1-3).");
        System.out.println("   1st coordinate is on the x-axis");
        System.out.println("   2nd coordinate is on the y-axis");
        System.out.println("Good luck!");
        new TicTacToe().play();
    }
}
