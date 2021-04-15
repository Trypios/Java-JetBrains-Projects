package tictactoe;

/**
 * Impossible to beat AI. Always calculates the best possible move.
 */
public class CpuPlayerImpossible extends CpuPlayer {

    public CpuPlayerImpossible(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int move() {

        return calcBestMove();
    }
}
