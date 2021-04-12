package tictactoe;

/**
 * Tic-Tac-Toe player
 */
public abstract class Player {

    private final Mark mark;
    private final Board board;
    private final String name;
    private Player opponent;

    public Player(String name, Mark mark, Board board) {

        this.name = name;
        this.mark = mark;
        this.board = board;
    }

    /**
     * Setter
     * @param opponent This player's TicTacToe opponent.
     */
    protected void setOpponent(Player opponent) {

        this.opponent = opponent;
    }

    /**
     * Getter
     * @return this player's opponent.
     */
    protected Player getOpponent() {

        return opponent;
    }

    /**
     * Getter
     * @return this player's TicTacToe mark (X or O).
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Getter
     * @return the TicTacToe board this player plays on
     */
    protected Board getBoard() {
        return board;
    }

    /**
     * The player calculates a TicTacToe move to play.
     * @return the index of a slot in the TicTacToe's grid.
     */
    public abstract int move();

    @Override
    public String toString() {
        return name;
    }
}
