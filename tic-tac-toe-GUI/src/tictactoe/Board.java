package tictactoe;

import java.util.*;

public class Board {

    protected final static Map<Integer, Integer> slots;
    private final Mark[] grid;

    static {
        int[] k = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] v = new int[] {6, 7, 8, 3, 4, 5, 0, 1, 2};
        slots = new HashMap<>();
        for (int i = 0; i < k.length; i++) {
            slots.put(k[i], v[i]);
        }
    }

    public Board() {
        this(new Mark[] {Mark.EMPTY, Mark.EMPTY, Mark.EMPTY,
                Mark.EMPTY, Mark.EMPTY, Mark.EMPTY,
                Mark.EMPTY, Mark.EMPTY, Mark.EMPTY});
    }

    public Board(Mark[] grid) {

        this.grid = grid;
    }

    /**
     * Reset all board slots to being empty.
     */
    void reset() {
        Arrays.fill(grid, Mark.EMPTY);
    }

    /**
     * Mark a slot in the grid with a player's sign (X or O).
     * @param index The index of a slot in the grid.
     * @param mark a player's mark (X or O).
     */
    public void markCellAt(int index, Mark mark) {

        grid[index] = mark;
    }

    /**
     * Check if the specified player wins.
     * @param player a TicTacToe player.
     * @return true if the player wins, else false.
     */
    public boolean isWinner(Player player) {

        Mark m = player.getMark();
        int row = 0, col = 0;
        for (int i = 0; i < 3; i++) {
            // check rows
            if (m.equals(grid[row]) &&
                    grid[row].equals(grid[row + 1]) && grid[row + 1].equals(grid[row + 2])) {
                return true;
            }
            row += 3;
            // check columns
            if (m.equals(grid[col]) &&
                    grid[col].equals(grid[col + 3]) && grid[col + 3].equals(grid[col + 6])) {
                return true;
            }
            col++;
        }
        // check main & side diagonals
        if (m.equals(grid[4])) {
            return (grid[0].equals(grid[4]) && grid[4].equals(grid[8])) ||
                    (grid[2].equals(grid[4]) && grid[4].equals(grid[6]));
        }
        return false;
    }

    /**
     * Check if board has no available slots left,
     * hence game ends in draw.
     * @return true if no slots are empty, else false
     */
    public boolean isDraw() {

        return getAvailableSlots().size() == 0;
    }

    /**
     * Gather the indexes of all empty slots in an array.
     * @return an array of grid indexes.
     */
    public List<Integer> getAvailableSlots() {

        List<Integer> availableSlots = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            if (isSlotAvailable(grid[i])) {
                availableSlots.add(i);
            }
        }
        return availableSlots;
    }

    /**
     * Check if a slot is empty (available to be marked).
     * @param slot a slot in Board's grid.
     * @return true if slot is empty, else false.
     */
    private boolean isSlotAvailable(Mark slot) {
        return slot.equals(Mark.EMPTY);
    }

    @Override
    public String toString() {

        String[] g = new String[grid.length];
        for (var entry : slots.entrySet()) {
            g[entry.getValue()] = grid[entry.getValue()].equals(Mark.EMPTY) ?
                    Integer.toString(entry.getKey()) : grid[entry.getValue()].toString();
        }
        String line = "---|---|---\n";
        return String.format("%n %s | %s | %s %n", g[0], g[1], g[2]) +
                line +
                String.format(" %s | %s | %s %n", g[3], g[4], g[5]) +
                line +
                String.format(" %s | %s | %s %n", g[6], g[7], g[8]);
    }
}