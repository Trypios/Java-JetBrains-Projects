package battleship;

/**
 * Cells of the Battlefield grid area
 */
public class Cell {

    private final String coord;
    private Mark mark;
    private Ship ship;
    private boolean shot;

    public Cell(char longitude, int latitude) {
        this.coord = String.format("%c%d", longitude, latitude);
        this.mark = Mark.FOG;
        this.shot = false;
    }

    /**
     * Marks this cell as occupied and assigns the relevant ship occupied by.
     * @param ship The ship that occupies this cell
     */
    public void occupy(Ship ship) {
        this.ship = ship;
        this.mark = Mark.SHIP;
    }

    /**
     * @return the ship that occupies this cell, else null
     */
    public Ship occupiedBy() {
        return ship;
    }

    /**
     * @return true if this cell is occupied by a ship, else false.
     */
    public boolean isOccupied() {
        return ship != null;
    }

    /**
     * @return true if this cell has been shot in a previous turn, else false.
     */
    public boolean isShot() {
        return this.shot;
    }

    /**
     * Marks cell as shot and updates its visuals.
     * Also updates the state of the ship being shot, if occupied.
     */
    public void shot() {
        this.shot = true;
        if (isOccupied()) {
            this.mark = Mark.SHOT;
            this.ship.hit();
        } else {
            this.mark = Mark.MISSED;
        }
    }

    /**
     * @return true if cell has never been shot before.
     */
    public boolean isFoggy() {
        return !isShot();
    }

    /**
     * @return the cell's visuals
     */
    @Override
    public String toString() {
        return this.mark.toString();
    }
}


/**
 * Predetermined visuals for cells
 */
enum Mark {
    FOG('~'),
    SHIP('O'),
    SHOT('X'),
    MISSED('M');

    char sign;

    Mark(char sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return Character.toString(this.sign);
    }
}