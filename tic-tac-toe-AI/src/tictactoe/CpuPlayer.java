package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class CpuPlayer extends Player {

    private final static Random random = new Random();
    private int bestMove;  // established by minimax algorithm

    public CpuPlayer(String name, Mark mark, Board board) {

        super(name, mark, board);
    }

    /**
     * Make a random TicTacToe move.
     * @return the index of a random available TicTacToe slot.
     */
    protected int randomMove() {

        int index = random.nextInt(getBoard().getAvailableSlots().size());
        return getBoard().getAvailableSlots().get(index);
    }

    /**
     * Check if possible to win next move,
     * or possible to block the win of the opponent's next move.
     * If none is possible, move at random.
     * @return the index of an available TicTacToe slot.
     */
    protected int calcNextMove() {

        // check for possible win next move
        for (int slot : getBoard().getAvailableSlots()) {
            getBoard().markCellAt(slot, getMark());
            if (getBoard().isWinner(this)) {
                getBoard().markCellAt(slot, Mark.EMPTY);
                return slot;
            }
            getBoard().markCellAt(slot, Mark.EMPTY);  // undo test
        }
        // check for blocking opponent's possible win (at next move)
        for (int slot : getBoard().getAvailableSlots()) {
            getBoard().markCellAt(slot, getOpponent().getMark());
            if (getBoard().isWinner(getOpponent())) {
                getBoard().markCellAt(slot, Mark.EMPTY);
                return slot;
            }
            getBoard().markCellAt(slot, Mark.EMPTY);  // undo test
        }
        // eventually make a random move
        return randomMove();
    }

    /**
     * Calculate the best possible move to make.
     * @return the index of an available TicTacToe slot.
     */
    protected int calcBestMove() {

        miniMax(true);
        return bestMove;
    }

    /**
     * Minimax algorithm. Establishes bestMove by recursion and backtracking.
     * @param maximize true for the active player, false for their opponent.
     * @return a value -10, 0, 10 depending on the outcome of the recursion's base case.
     *    the return does not matter as much as that the bestMove variable is established.
     */
    private int miniMax(boolean maximize) {

        // get array of available slots (indexes)
        List<Integer> freeSlots = getBoard().getAvailableSlots();

        // base cases
        if (getBoard().isWinner(this)) {
            return 10;
        } else if (getBoard().isWinner(getOpponent())) {
            return -10;
        } else if (getBoard().isDraw()) {
            return 0;
        }

        int score;
        List<int[]> moves = new ArrayList<>();  // to store tuples of [moves, scores]
        for (int slot : freeSlots) {
            Mark mark = maximize ? getMark() : getOpponent().getMark();
            getBoard().markCellAt(slot, mark);
            score = maximize ? miniMax(false) : miniMax(true);
            moves.add(new int[]{slot, score});
            getBoard().markCellAt(slot, Mark.EMPTY);  // reset board state
        }

        // unwind: evaluation of minimax
        int bestScore;
        if (maximize) {
            bestScore = Integer.MIN_VALUE;
            for (int[] tuple : moves) {
                if (tuple[1] > bestScore) {
                    bestScore = tuple[1];
                    bestMove = tuple[0];
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int[] tuple : moves) {
                if (tuple[1] < bestScore) {
                    bestScore = tuple[1];
                    bestMove = tuple[0];
                }
            }
        }
        return bestScore;
    }

}