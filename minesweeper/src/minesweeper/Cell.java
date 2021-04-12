package minesweeper;


/**
 * Model of a Minesweeper field's single cell.
 * It can be either an explosive landmine or a safe spot.
 * It's marker can signify if surrounded by landmines.
 */
public class Cell {

    private char marker;
    private final int ROW;  // coordinate
    private final int COL;  // coordinate
    private boolean explosive;
    private boolean markedAsExplosive;
    private boolean open;

    public Cell (char marker, int rowCoor, int colCoor, boolean explosive) {
        this.marker = marker;
        this.ROW = rowCoor;
        this.COL = colCoor;
        this.explosive = explosive;
        this.markedAsExplosive = false;
        this.open = false;
    }

    /**
     * Getter
     * @return row coordinate
     */
    public int getRow() {
        return ROW;
    }

    /**
     * Getter
     * @return column coordinate
     */
    public int getColumn() {
        return COL;
    }

    /**
     * Getter
     * @return cell's current marker
     */
    public char getMarker() {
        return marker;
    }

    /**
     * Setter
     */
    public void setMarker(char marker) {
        this.marker = marker;
    }

    /**
     * Check if cell is marked with a number 1-8,
     * meaning it is surrounded with as many mines.
     * @return true if the cell's marker is a number char from 1-8
     */
    public boolean isNumbered() {
        // in unicode: 49 = '0', 56 = '8'
        return (marker >= 49 && marker <= 56);
    }

    /**
     * Check if cell is an actual landmine,
     * whether marked as such or not by the user.
     * @return true if cell is truly explosive
     */
    public boolean isExplosive() {

        return explosive;
    }

    /**
     * Set this cell as either landmine or safe.
     * @param explosive set true to turn this cell to landmine.
     */
    public void setExplosive (boolean explosive) {
        /*
         * Game randomly marks this cell as truly explosive or not
         * in Minesweeper.initRandomMines()
         * */
        this.explosive = explosive;
    }

    /**
     * Check if this cell is marked as a potential landmine.
     * @return true if the user marked this cell as a potential landmine.
     */
    public boolean isMarkedAsExplosive() {

        return markedAsExplosive;
    }

    /**
     * User marks this cell as potential landmine
     * @param isExplosive set to true to mark this cell as a potential landmine.
     */
    public void setMarkedAsExplosive(boolean isExplosive) {

        markedAsExplosive = isExplosive;
    }

    /**
     * Check if the user opened this cell.
     * @return true if cell is opened
     */
    public boolean isOpen() {

        return open;
    }

    /**
     * Mark this cell as opened by the user.
     */
    public void setOpen() {

        if (!isNumbered()) {
            marker = '/';
        }
        markedAsExplosive = false;
        open = true;
    }

}