package tictactoe;

/**
 * Plays at random.
 */
public class CpuPlayerEasy extends CpuPlayer {

    public CpuPlayerEasy(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int move() {

        return randomMove();
    }
}