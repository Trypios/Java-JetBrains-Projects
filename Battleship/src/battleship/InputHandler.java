package battleship;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * Class responsible for handling user-input,
 * for various methods of Battlefield and BattleshipGame.
 */
public class InputHandler {

    private final static Scanner sc = new Scanner(System.in);
    private final Map<String, Cell> grid;

    public InputHandler(Battlefield field) {
        this.grid = field.getGrid();
    }

    /**
     * Persistently asks the user for input until given coordinates are valid.
     * @return validated coordinates
     */
    public String[] inputShipCoordinates(Ship ship) {

        String[] coords;
        boolean valid = false;
        while (true) {
            String input = sc.nextLine();

            // check if two coords are given
            valid = validateShipInput(input);
            if (!valid) { continue; }

            coords = input.split(" ");
            Arrays.sort(coords);

            // check if each coord is within the grid
            valid = validateBothCoordinates(coords);
            if (!valid) { continue; }

            // check if ship fits
            valid = validateShipCoords(ship, coords);
            if (!valid) { continue; }

            return coords;
        }
    }

    /**
     * Persistently asks the user for input until given coordinates are valid.
     * @return validated coordinates
     */
    public String inputCoordinates() {
        while (true) {
            String input = sc.nextLine();
            if (isValidCoord(input)) {
                return input;
            }
            System.out.println("\nError! Invalid coordinates! Try again:\n");
        }
    }

    /**
     * Persistently asks the user to press enter.
     */
    public static void inputEnter() {
        while (true) {
            System.out.println("\nPress Enter and pass the move to another player\n...");
            String input = sc.nextLine();
            if (input.isEmpty()) {
                return;
            }
            System.out.println("\nError! Invalid input. Try again:\n");
        }
    }

    /**
     * Helper of inputShipCoordinates()
     * @param input two coordinates separated by a space
     * @return true if input matches coordinates regex, else false
     */
    private static boolean validateShipInput(String input) {
        String regex = "([A-Z]\\d+) ([A-Z]\\d+)";
        if (input.matches(regex)) {
            return true;
        }
        System.out.println("\nError! Invalid input! Try again:\n");
        return false;
    }

    /**
     * Helper of inputShipCoordinates()
     * @param coords an array containing a pair of coordinates
     * @return true if both coordinates exists in the grid, else false
     */
    private boolean validateBothCoordinates(String[] coords) {
        if (isValidCoord(coords[0]) && isValidCoord(coords[1])) {
            return true;
        }
        System.out.println("\nError! Invalid coordinates! Try again:\n");
        return false;
    }

    /**
     * Concatenates two coordinate values
     * @param longitude represents the vertical axis, or rows
     * @param latitude represents the horizontal axis, or columns
     * @return a concatenated string of the two coordinates
     */
    private static String coordsToString(char longitude, int latitude) {
        return String.format("%c%d", longitude, latitude);
    }

    /**
     * @param coord a coordinate
     * @return true if coord exists in the grid, else false
     */
    private boolean isValidCoord(String coord) {
        return grid.get(coord) != null;
    }

    /**
     * Validates if given coordinates can correctly position a ship on the grid.
     * The ship must be completely vertical or horizontal, of the right size,
     * and must not touch another ship.
     * @param ship The ship to be positioned by coordinates
     * @param coords The coordinates to position a ship
     * @return true if coordinates are valid, else false.
     */
    private boolean validateShipCoords(Ship ship, String[] coords) {
        // vertical
        char startLongitude = coords[0].charAt(0);
        char endLongitude = coords[1].charAt(0);
        // horizontal
        int startLatitude = Integer.parseInt(coords[0].substring(1));
        int endLatitude = Integer.parseInt(coords[1].substring(1));

        // sort ASC
        if (startLatitude > endLatitude) {
            int temp = startLatitude;
            startLatitude = endLatitude;
            endLatitude = temp;
            String tempCoords = coords[0];
            coords[0] = coords[1];
            coords[1] = tempCoords;
        }

        boolean isHorizontal = startLongitude == endLongitude;
        boolean isVertical = startLatitude == endLatitude;

        // check if fully horizontal or vertical
        if (!isHorizontal && !isVertical) {
            System.out.println("\nError! Wrong ship location! Try again:\n");
            return false;
        }

        // check if correct ship size
        if (isHorizontal && endLatitude - startLatitude + 1 != ship.getSize()) {
            System.out.println("\nError! Wrong horizontal length of the Submarine! Try again:\n");
            return false;
        } else if (isVertical && endLongitude - startLongitude + 1 != ship.getSize()) {
            System.out.println("\nError! Wrong vertical length of the Submarine! Try again:\n");
            return false;
        }

        // check if touching another ship
        int col = startLatitude;
        char row = startLongitude;
        boolean isTouching = false;
        if (isHorizontal) {
            isTouching = !isCellFree(coordsToString(row, col - 1)) ||
                    !isCellFree(coordsToString(row, col + ship.getSize()));
            while (!isTouching && col <= endLatitude) {
                isTouching = !isCellFree(coordsToString((char) (row - 1), col)) ||
                        !isCellFree(coordsToString((char) (row + 1), col));
                col++;
            }
        } else {  // isVertical
            isTouching = !isCellFree(coordsToString((char) (row - 1), col)) ||
                    !isCellFree(coordsToString((char) (row + ship.getSize()), col));
            while (!isTouching && row <= endLongitude) {
                isTouching = !isCellFree(coordsToString(row, col - 1)) ||
                        !isCellFree(coordsToString(row, col + 1));
                row++;
            }
        }
        if (isTouching) {
            System.out.println("\nError! You placed it too close to another one. Try again:\n");
            return false;
        }

        return true;
    }

    /**
     * @param coord coordinates
     * @return true if cell has no ship on it, false otherwise
     */
    private boolean isCellFree(String coord) {
        Cell cell = grid.get(coord);
        if (cell == null) { return true; }
        return !cell.isOccupied();
    }
}
