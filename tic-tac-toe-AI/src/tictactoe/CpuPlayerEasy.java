package tictactoe;

public class CpuPlayerEasy extends CpuPlayer {

    public CpuPlayerEasy(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int move() {
        System.out.printf("%s making move (easy level cpu)%n", this);
        return randomMove();
    }
}
