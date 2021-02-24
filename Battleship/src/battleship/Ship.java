package battleship;

/**
 * Predefined ships for BattleshipGame
 */
public enum Ship {

    AIRCRAFT_CARRIER(5, "Aircraft Carrier"),
    BATTLESHIP(4, "Battleship"),
    SUBMARINE(3, "Submarine"),
    CRUISER(3, "Cruiser"),
    DESTROYER(2, "Destroyer");

    private final int size;
    private final String name;
    private int hitCounter;

    Ship(int size, String name) {
        this.size = size;
        this.name = name;
        this.hitCounter = 0;
    }

    /**
     * @return length of ship in number of cells occupied
     */
    public int getSize() {
        return this.size;
    }

    /**
     * @return name of the ship
     */
    public String getName() {
        return this.name;
    }

    /**
     * Ship got shot. Increments hitcounter
     */
    public void hit() {
        if (hitCounter < size) {
            hitCounter++;
        }
    }

    /**
     * @return true if ship is completely destroyed, else false.
     */
    public boolean isDestroyed() {
        return hitCounter == size;
    }

}
