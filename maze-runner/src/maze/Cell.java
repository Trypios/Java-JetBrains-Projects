package maze;

import java.io.Serializable;

/**
 * Model of a Maze's single cell.
 * It has coordinates x, y (on the x and y-axis respectively),
 * and can represent one of three things:
 *    a path, a wall, an escape exit
 */
public class Cell implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    private static final String WALL_SYMBOL = "\u2588\u2588";
    private static final String PATH_SYMBOL = "  ";
    private static final String ESCAPE_SYMBOL = "//";
    private final int x;
    private final int y;
    private boolean isPath;
    private boolean escapePath;
    private boolean visited;

    public Cell(int x, int y, boolean isPath) {
        this.x = x;
        this.y = y;
        this.isPath = isPath;
        this.escapePath = false;
        this.visited = false;
    }

    public Cell(int x, int y) {
        this(x, y, false);
    }

    /**
     * Getter
     * @return the x-axis coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter
     * @return the y-axis coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Check if cell represents a maze path.
     * @return true if isPath, else false.
     */
    public boolean isPath() {
        return isPath;
    }

    /**
     * Use when traversing the Maze.
     * @param isEscape set true if the cell belongs in the Maze's escape path,
     *                 false otherwise
     */
    public void setAsEscape(boolean isEscape) {
        visited = true;
        escapePath = isEscape;
    }

    /**
     * Overloaded method.
     * Mark the cell as part of the Maze's escape path.
     */
    public void setAsEscape() {
        setAsEscape(true);
    }

    /**
     * Getter
     * Check if cell has been visited before. A cell is marked as visited
     * for the purposes of finding the way ouf of the maze.
     * @return true if visited, else false.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Setter
     * @param visited set true if the cell has been visited.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Set the cell as a path (i.e. not wall).
     * Useful while creating a Maze and traversing it.
     */
    public void setAsPath() {
        this.isPath = true;
        this.visited = true;
    }

    /**
     * Set the cell as a wall (i.e. not path).
     * Useful while creating a Maze and traversing it.
     */
    public void setAsWall() {
        this.isPath = false;
        this.visited = true;
    }

    /**
     * Mark the cell as unknown if path to exit.
     * Regardless if visited or not.
     */
    public void reset() {

        this.escapePath = false;
    }

    /**
     * Deep copy this cell.
     * @return a new Cell object.
     */
    @Override
    public Cell clone() {

        final Cell clone;
        try {
            clone = (Cell) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cell cloning failed", e);
        }
        return clone;
    }

    /**
     * A cell can be either a wall or a path. If a path, it may be the escape path.
     * The method returns the correct visual representation accordingly.
     * @return a 2-character string.
     */
    @Override
    public String toString() {

        if (escapePath) {
            return ESCAPE_SYMBOL;
        }
        if (isPath) {
            return PATH_SYMBOL;
        }
        return WALL_SYMBOL;
    }
}
