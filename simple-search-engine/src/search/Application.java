package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Processes some data and search for word(s), phrase(s)
 * or even the absence of them.
 */
public class Application {

    private static final Scanner SCANNER;
    private static final InvertedIndex invertedIndex;

    static {
        SCANNER = new Scanner(System.in);
        invertedIndex = new InvertedIndex();
    }

    /**
     * Run this method to start the application.
     * @param args arguments from terminal (shell).
     *             Expects to parse a data-file from arguments.
     */
    public static void run(String[] args) {

        File inputFile = parseArgs(args);
        boolean valid = readFromFile(inputFile);
        if (valid) {
            menu();
        }
    }

    /**
     * Parse arguments to retrieve a data-file.
     * @param args arguments from a terminal.
     * @return an existing file either as specified by args, or the default.
     */
    private static File parseArgs(String[] args) {

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--data")) {
                try {
                    return new File(args[i + 1]);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Error. Filename not specified.");
                    break;
                }
            }
        }
        System.out.println("No file has been parsed.\nA default test-file will be used instead.");
        return new File("default.txt");
    }

    /**
     * Read the specified file and feed each text-line to the InvertedIndex class.
     * @param file a file containing text.
     * @return true if file read successfully, else false.
     */
    private static boolean readFromFile(File file) {

        if (file == null) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            int lineIndex = 0;
            String line = br.readLine();

            while (line != null) {
                invertedIndex.add(line, lineIndex++);
                line = br.readLine();
            }
            return true;
        } catch (IOException e) {
            System.out.printf("Error reading from file '%s'%n", file.getName());
        }
        return false;
    }

    /**
     * Search-engine main menu.
     */
    private static void menu() {

        while (true) {
            System.out.println("=== Menu ===");
            System.out.println("1. Find a word");
            System.out.println("2. Print all text-lines");
            System.out.println("0. Exit");

            String input = SCANNER.nextLine();
            System.out.println();

            switch (input) {
                case "1":
                    findPerson();
                    break;
                case "2":
                    displayPeople();
                    break;
                case "0":
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Incorrect option! Try again.");
            }
        }
    }

    /**
     * Search for a word as specified by user input and using
     * a specified matching strategy (all|any|none).
     */
    private static void findPerson() {

        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = SCANNER.nextLine().toUpperCase();
        System.out.println();
        if (!strategy.matches("ALL|ANY|NONE")) {
            System.out.println("Invalid input");
            return;
        }

        System.out.println("Enter a word search all suitable text-lines.");
        String target = SCANNER.nextLine();
        System.out.println();

        List<String> matches = null;
        switch (strategy) {
            case "ALL":
                matches = invertedIndex.matchAll(target);
                break;
            case "ANY":
                matches = invertedIndex.matchAny(target);
                break;
            case "NONE":
                matches = invertedIndex.matchNone(target);
                break;
            default:
                System.out.println("Invalid input");
        }

        displayMatches(matches);

    }

    /**
     * Display the specified searching matches as found by InvertedIndex.
     * @param matches a list of words.
     */
    private static void displayMatches(List<String> matches) {

        if (matches == null || matches.isEmpty()) {
            System.out.println("No match found");
            return;
        }

        System.out.printf("%d matches found:%n", matches.size());
        for (String person : matches) {
            System.out.println(person);
        }
        System.out.println();
    }

    /**
     * Display all text-lines stored in InvertedIndex.
     */
    private static void displayPeople() {

        if (invertedIndex.isEmpty()) {
            System.out.println("No text-lines found.");
            return;
        }

        System.out.println("=== List of text-lines ===");
        for (String person : invertedIndex.getLines()) {
            System.out.println(person);
        }

        System.out.println();
    }
}
