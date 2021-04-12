package battleship;

import java.util.*;

/**
 * Battlefield for the BattleshipGame
 */
public class Battlefield {

    private final InputHandler inputHandler;
    private final int gridHeight;
    private final int gridWidth;
    private boolean lost;
    private Map<String, Cell> grid;
    private List<Ship> ships;

    public Battlefield(int rows, int cols) {
        this.gridHeight = rows;
        this.gridWidth = cols;
        this.lost = false;
        initGrid();
        initShips();
        this.inputHandler = new InputHandler(this);
    }

    /**
     * @return this Battlefield's grid
     */
    public Map<String, Cell> getGrid() {
        return this.grid;
    }

    /**
     * Prints current battlefield state in a rectangle grid.
     * Shows true cell states if fogless, else masks cells with fog
     */
    public void display(boolean fogless) {
        // print column header
        StringBuilder header = new StringBuilder(" ");
        for (int c = 1; c <= gridWidth; c++) {
            header.append(' ').append(c);
        }
        System.out.println(header);

        // print each row
        for (int r = 'A'; r < gridHeight + 'A'; r++) {
            String idxLetter = Character.toString(r);
            StringBuilder line = new StringBuilder(idxLetter);
            for (int c = 1; c <= gridWidth; c++) {
                String coord = String.format("%s%d", idxLetter, c);
                line.append(" ");
                if (!fogless && grid.get(coord).isFoggy()) {
                    line.append(Mark.FOG);
                } else {
                    line.append(grid.get(coord));
                }
            }
            System.out.println(line);
        }
    }

    /**
     * Prints current battlefield state in a rectangle grid
     * Shows true cell states
     */
    public void display() {
        display(true);
    }

    /**
     * @return true if all ships are destroyed in this battlefield
     */
    public boolean hasLost() {
        return this.lost;
    }

    /**
     * User inputs coordinates to attack a cell of this battlefield.
     * Method checks whether the attacked cell contains a ship and updates grid accordingly.
     * @return an informative message of what happened at the attacked cell.
     */
    public String attacked() {
        String coord = inputHandler.inputCoordinates();
        Cell target = grid.get(coord);
//        while (target.isShot()) {
//            System.out.println("\nThis cell has been shot already! Try again:\n");
//            target = grid.get(coord);
//        }
        target.shot();
        if (target.isOccupied()) {
            if (target.occupiedBy().isDestroyed()) {
                if (allShipsDestroyed()) {
                    this.lost = true;
                    return "\nYou sank the last ship. You won. Congratulations!\n";
                }
                return "\nYou sank a ship! Specify a new target:\n";
            }
            return "\nYou hit a ship!\n";
        }
        return "\nYou missed!\n";
    }

    /**
     * Player inputs coordinates to place each of the 5 ships on the grid.
     */
    public void positionShips() {
        for (Ship ship : ships) {
            positionShip(ship);
            display();
        }
    }

    /**
     * Helper of positionShips()
     * @param ship The ship to be positioned on the grid
     */
    private void positionShip(Ship ship) {
        System.out.printf("%nEnter the coordinates of the %s (%d cells):%n", ship.getName(),
                                                                             ship.getSize());
        System.out.println("(e.g. E2 E6 or B7 E7)\n");
        String[] coords = inputHandler.inputShipCoordinates(ship);
        updatePositions(ship, coords);
    }

    /**
     * Helper of shotAftermath(). Determins if this battlefield is destroyed.
     * @return true if all ships on the grid are destroyed, else false
     */
    private boolean allShipsDestroyed() {
        boolean result = true;
        for (Ship ship : ships) {
            result &= ship.isDestroyed();
        }
        this.lost = result;
        return result;
    }

    /**
     * Populates the battlefield grid with a list of cells
     */
    private void initGrid() {
        this.grid = new HashMap<>();
        String coord;
        for (int r = 'A'; r < gridHeight + 'A'; r++) {
            String row = Character.toString(r);
            for (int c = 1; c <= gridWidth; c++) {
                coord = row + c;
                grid.put(coord, new Cell((char) r, c));
            }
        }
    }

    /**
     * Populates the list of ships with 5 predefined ships.
     */
    private void initShips() {
        this.ships = Arrays.asList(Ship.AIRCRAFT_CARRIER,
                                   Ship.BATTLESHIP,
                                   Ship.SUBMARINE,
                                   Ship.CRUISER,
                                   Ship.DESTROYER);
    }

    /**
     * updates the grid characters based on the given ship
     * @param ship One of the 5 predefined ships
     */
    private void updatePositions(Ship ship, String[] coords) {
        char startLon = coords[0].charAt(0);
        char endLon = coords[1].charAt(0);
        int startLat = Integer.parseInt(coords[0].substring(1));
        int endLat = Integer.parseInt(coords[1].substring(1));

        String coord;
        for (int r = startLon; r <= endLon; r++) {
            String row = Character.toString(r);
            for (int c = startLat; c <= endLat; c++) {
                coord = row + c;
                Cell cell = grid.get(coord);
                cell.occupy(ship);
            }
        }
    }

}
