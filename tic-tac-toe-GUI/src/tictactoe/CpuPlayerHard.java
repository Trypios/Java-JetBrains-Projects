package tictactoe;

/**
 * AI that calculates the best possible move (similar to CpuPlayerImpossible),
 * but sometimes makes minor mistakes, as long as it won't miss a win on current round,
 * or lose the very next round.
 */
public class CpuPlayerHard extends CpuPlayer {

    public CpuPlayerHard(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int move() {

        return bestOrRandomMove();
    }
}