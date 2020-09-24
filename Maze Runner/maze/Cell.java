package maze;

import java.io.Serializable;

public class Cell implements Serializable {

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isPath() {
        return isPath;
    }

    public void setAsEscape() {
        setAsEscape(true);
    }

    public void setAsEscape(boolean isEscape) {
        visited = true;
        escapePath = isEscape;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setAsPath() {
        this.isPath = true;
        this.visited = true;
    }

    public void setAsWall() {
        this.isPath = false;
        this.visited = true;
    }

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
