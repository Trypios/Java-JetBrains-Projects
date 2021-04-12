package tictactoe;

public class CpuPlayerMedium extends CpuPlayer {

    public CpuPlayerMedium(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int move() {

        System.out.printf("%s making move (moderate level cpu)%n", this);
        return calcNextMove();
    }
}
