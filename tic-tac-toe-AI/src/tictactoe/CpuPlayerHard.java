package tictactoe;

public class CpuPlayerHard extends CpuPlayer {

    public CpuPlayerHard(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int move() {
        System.out.printf("%s making move (hard level cpu)%n", this);
        return calcBestMove();
    }
}
