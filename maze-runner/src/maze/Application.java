package maze;

import java.io.*;
import java.util.Scanner;

public class Application {

    private static final Scanner SCANNER;
    private static boolean active;
    private static Maze maze;

    static {
        SCANNER = new Scanner(System.in);
        active = true;
    }

    /**
     * Run this method to play with a Maze.
     */
    public static void run() {

        // establish a maze
        while (active && maze == null) {
            startingMenu();
        }
        // explore the maze
        while (active) {
            mainMenu();
        }
    }

    /**
     * The application's starting menu if a Maze has not been established yet.
     * A maze will either be loaded from a file or generated at random.
     */
    private static void startingMenu() {

        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        System.out.println("0. Exit");
        String input = SCANNER.nextLine();
        switch (input) {
            case "1":
                generateMaze();
                break;
            case "2":
                loadMaze();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Incorrect option. Please try again");
        }
    }

    /**
     * The application's main menu once a maze been established.
     * Offers several options to the user:
     * generate new, load, save, display and find the escape path.
     */
    private static void mainMenu() {

        System.out.println("=== Menu ===");
        System.out.println("1. Generate a new maze");
        System.out.println("2. Load a maze");
        System.out.println("3. Save the maze");
        System.out.println("4. Display the maze");
        System.out.println("5. Find the escape");
        System.out.println("6. Remove marked escape path");
        System.out.println("0. Exit");
        String input = SCANNER.nextLine();
        switch (input) {
            case "1":
                generateMaze();
                break;
            case "2":
                loadMaze();
                break;
            case "3":
                saveMaze();
                break;
            case "4":
                displayMaze();
                break;
            case "5":
                findEscape();
                break;
            case "6":
                unmarkEscape();
                displayMaze();
                break;
            case "0":
                exit();
                break;
            default:
                System.out.println("Incorrect option. Please try again");
        }
    }

    /**
     * Request user input for the dimensions to create a maze:
     * height (number of rows) and width (number of columns),
     * separated by space.
     */
    private static void generateMaze() {

        maze = null;
        while (maze == null) {
            System.out.println("Enter the size of a square maze (odd number 9 - 49)");
            String input = SCANNER.nextLine();
            if (!input.matches("\\d+")) {
                System.out.println("Invalid input. Enter a single number");
                continue;
            }
            int size = Integer.parseInt(input);
            if (size >= 50) {
                System.out.println("Size is quite big! Enter a value smaller than 50");
                continue;
            }
            if (size < 9) {
                System.out.println("Size is too small! Enter a value larger than 8");
                continue;
            }
            if (size % 2 == 0) {
                System.out.println("Please enter an odd number.");
                continue;
            }
            maze = new Maze(size);
        }
        displayMaze();
    }

    /**
     * Load Maze object from a file, by deserialization process.
     * A backup from the previous maze is kept in case of an error.
     */
    private static void loadMaze() {

        Maze backup = maze == null ? null : maze.clone();

        System.out.print("Input filename: ");
        String filename = SCANNER.nextLine();

        File inFile = new File(filename);
        if (inFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inFile))) {
                maze = (Maze) ois.readObject();
            } catch (EOFException | NotSerializableException | ClassNotFoundException e) {
                maze = backup;
                System.out.println("Cannot load the maze. It has an invalid format");
                // e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                maze = backup;
            }
        } else {
            System.out.printf("The file '%s' does not exist%n", filename);
        }
        displayMaze();
    }

    /**
     * Save Maze object to a file, by serialization process.
     * The maze is saved without its solution (path to exit).
     */
    private static void saveMaze() {

        System.out.print("Input filename: ");
        String filename = SCANNER.nextLine();

        File outFile = new File(filename);
        try {
            if (!outFile.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.printf("Error. Could not create file '%s'. Try a different filename%n", filename);
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outFile))) {
            Maze clone = maze.clone();
            clone.unmarkEscape();
            oos.writeObject(clone);
            System.out.printf("Maze has been successfully saved to '%s'%n", filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print the maze.
     */
    private static void displayMaze() {

        if (maze != null) {
            maze.display();
        } else {
            System.out.println("Maze not found.");
        }
    }

    /**
     * Find the Maze escape path.
     */
    private static void findEscape() {

        maze.escape();
        displayMaze();
    }

    /**
     * Remove the marked escape path.
     */
    private static void unmarkEscape() {

        maze.unmarkEscape();
    }

    /**
     * Perform necessary steps to exit the application.
     */
    private static void exit() {
        active = false;
        System.out.println("Bye!");
    }

}
