package tictactoe;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, Mark mark, Board board) {
        super(name, mark, board);
    }

    @Override
    public int move() {

        System.out.printf("%s making move > ", this);
        String input;
        int slot;
        while (true) {
            input = TicTacToe.input();
            if (input.matches("[1-9]")) {
                slot = Integer.parseInt(input);
                slot = Board.slots.get(slot);
                if (getBoard().getAvailableSlots().contains(slot)) {
                    return slot;
                } else {
                    System.out.println("This cell is occupied! Choose another one!");
                }
            } else {
                System.out.println("You should enter a number 1-9!");
            }
        }
    }
}