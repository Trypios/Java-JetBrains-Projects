package phonebook;

import java.io.*;
import java.util.*;

/**
 * This application receives a big datafile (such as a phonebook)
 * and another datafile of targets to be searched within the former one.
 * It tests various sorting and searching algorithms and displays the
 * performance results of each one.
 */
public class Application {

    private static final Scanner SCANNER;
    private static List<String> directory;
    private static List<String> targets;
    private static boolean active;

    static {
        SCANNER = new Scanner(System.in);
        active = true;
        initialize();
    }

    /**
     * Initialize class variables from user input,
     * or with default values if given input is invalid.
     */
    private static void initialize() {

        System.out.println("Input filename to process data from: ");
        String input = SCANNER.nextLine();
        File directoryFile = new File(input);
        if (!directoryFile.exists() || !directoryFile.isFile()) {
            System.out.println("Filename does not exists. Using default 'directory.txt'");
            directoryFile = new File("directory.txt");
        }

        System.out.println("Input filename to process data from: ");
        input = SCANNER.nextLine();
        File targetsFile = new File(input);
        if (!targetsFile.exists() || !targetsFile.isFile()) {
            System.out.println("Filename does not exists. Using default 'targets.txt'");
            targetsFile = new File("targets.txt");
        }

        directory = readFromFile(directoryFile);
        targets = readFromFile(targetsFile);
    }

    /**
     * Read content of specified file and store each line in an list.
     * @param file an existing file
     * @return a list of text-lines.
     */
    private static List<String> readFromFile(File file) {

        ArrayList<String> array = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader(file))) {

            String input = br.readLine();
            while (input != null) {
                array.add(input);
                input = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading from file");
            active = false;
        }

        return array;
    }

    /**
     * Do a linear search and display performance results.
     */
    private static void testLinearSearch() {

        List<String> found = Searcher.linearSearch(directory, targets);
        System.out.printf("Found %d / %d entries. %n", found.size(), targets.size());
        System.out.printf("Time taken: %s%n%n", getTime(Searcher.getTimer()));
        System.out.println();
    }

    /**
     * Do a bubble sort + jump search
     * and display performance results.
     */
    private static void testBubbleSortJumpSearch() {

        List<String> directoryCopy = new ArrayList<>(directory);
        List<String> found = Searcher.jumpSearch(directoryCopy, targets);
        System.out.printf("Found %d / %d entries. %n", found.size(), targets.size());
        System.out.println("Time taken: " + (getTime(Searcher.getTimer() + Sorter.getTimer())));
        System.out.println("Sorting time: " + getTime(Sorter.getTimer()));
        System.out.println("Searching time: " + getTime(Searcher.getTimer()));
        System.out.println();
    }

    /**
     * Do a quick sort + binary search
     * and display performance results.
     */
    private static void testQuickSortBinarySearch() {

        List<String> directoryCopy = new ArrayList<>(directory);
        List<String> found = Searcher.binarySearch(directoryCopy, targets);
        System.out.printf("Found %d / %d entries. %n", found.size(), targets.size());
        System.out.println("Time taken: " + (getTime(Searcher.getTimer() + Sorter.getTimer())));
        System.out.println("Sorting time: " + getTime(Sorter.getTimer()));
        System.out.println("Searching time: " + getTime(Searcher.getTimer()));
        System.out.println();
    }

    /**
     * Do a hash table and display performance results.
     */
    private static void testHashTableHashSearch() {

        List<String> found = Searcher.hashSearch(directory, targets);
        System.out.printf("Found %d / %d entries. %n", found.size(), targets.size());
        System.out.println("Time taken: " + (getTime(Searcher.getTimer() + Sorter.getTimer())));
        System.out.println("Creating time: " + getTime(Sorter.getTimer()));
        System.out.println("Searching time: " + getTime(Searcher.getTimer()));
        System.out.println();
    }

    /**
     * Converts specified time from milliseconds
     * to human readable format (minutes:seconds:ms)
     * @param time number of milliseconds
     * @return a time formatted string
     */
    private static String getTime(long time) {

        long minutes = time / 60000;
        time %= 60000;
        long seconds = time / 1000;
        long ms = time % 1000;
        return String.format("%d min. %d sec. %d ms. ",
                minutes,
                seconds,
                ms);
    }

    /**
     * Run this method to start the application.
     */
    public static void run() {

        if (!active) {
            System.out.println("Error. Program stops.");
            return;
        }

        testLinearSearch();
//        testBubbleSortJumpSearch();  // needs improvement
        testQuickSortBinarySearch();
        testHashTableHashSearch();
    }
}
