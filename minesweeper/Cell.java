package minesweeper;

public class Cell {

    /**
     * MODEL
     */

    private char marker;
    private final int rowCoor;
    private final int colCoor;
    private boolean explosive;
    private boolean markedAsExplosive;
    private boolean open;

    public Cell (char marker, int rowCoor, int colCoor, boolean explosive) {
        this.marker = marker;
        this.rowCoor = rowCoor;
        this.colCoor = colCoor;
        this.explosive = explosive;
        this.markedAsExplosive = false;
        this.open = false;
    }

    public int getRowCoor() {
        return rowCoor;
    }

    public int getColCoor() {
        return colCoor;
    }

    public char getMarker() {
        return marker;
    }

    public void setMarker(char marker) {
        this.marker = marker;
    }

    public boolean isNumbered() {
        return (marker >= 49 && marker <= 56);
    }

    public boolean isExplosive() {
        /*
         * Returns true if cell is truly explosive
         * */

        return explosive;
    }

    public void setExplosive (boolean explosive) {
        /*
         * Game randomly marks this cell as truly explosive or not
         * in Minesweeper.initRandomMines()
         * */
        this.explosive = explosive;
    }

    public boolean isMarkedAsExplosive() {
        /*
        * Returns true if the user
        * marked this cell as explosive
        * */

        return markedAsExplosive;
    }

    public void setMarkedAsExplosive(boolean isExplosive) {
        /*
         * User marks this cell as explosive or not
         * */

        markedAsExplosive = isExplosive;
    }

    public boolean isOpen() {
        /*
         * Returns true if the user
         * opened this cell
         * */

        return open;
    }

    public void setOpen() {
        /*
         * User marks this cell as open
         * */

        if (!isNumbered()) {
            marker = '/';
        }
        markedAsExplosive = false;
        open = true;
    }

}