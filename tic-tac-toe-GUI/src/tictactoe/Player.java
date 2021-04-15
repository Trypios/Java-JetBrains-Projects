package tictactoe;

/**
 * Tic-Tac-Toe player
 */
public abstract class Player {

    enum Type {
        HUMAN, CPU_EASY, CPU_MEDIUM, CPU_HARD, CPU_IMPOSSIBLE
    }

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

    @Override
    public String toString() {
        return name;
    }

    /**
     * Player factory.
     * @param name Player's name
     * @param type Player's type (human or cpu)
     * @param mark Player's mark (X or O)
     * @param board a TicTacToe board
     * @return a Player object
     */
    static Player createPlayer(String name, Player.Type type, Mark mark, Board board) {

        switch (type) {
            case HUMAN:
                return new HumanPlayer(name, mark, board);
            case CPU_EASY:
                return new CpuPlayerEasy(name, mark, board);
            case CPU_MEDIUM:
                return new CpuPlayerMedium(name, mark, board);
            case CPU_HARD:
                return new CpuPlayerHard(name, mark, board);
            case CPU_IMPOSSIBLE:
                return new CpuPlayerImpossible(name, mark, board);
        }
        return null;
    }
}