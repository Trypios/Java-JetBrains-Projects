package tictactoe;

/**
 * Only assesses the next move, if possible to win or block opponent,
 * else plays at random.
 */
public class CpuPlayerMedium extends CpuPlayer {

    public CpuPlayerMedium(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int move() {

        return calcNextMove();
    }
}