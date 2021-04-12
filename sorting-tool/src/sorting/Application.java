package sorting;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This application receives data from either a text-file or from stdin and
 * sorts it in one of two ways:
 *    naturally = sorting elements lexicographically.
 *    by count = sorting elements by the number of occurrences.
 * The data can be tokenized in three types:
 *    word = an element is a single word
 *    line = an element is a whole line of text before a line-break
 *    long = an element is a number
 */
public class Application {

    private static Sorter.DataType dataType;
    private static Sorter.SortType sortingType;
    private static File inputFile;
    private static File outputFile;

    static {
        // set defaults
        dataType = Sorter.DataType.WORD;
        sortingType = Sorter.SortType.NATURAL;
        inputFile = new File("test-file.txt");
        outputFile = new File("out.txt");
    }

    /**
     * Parse arguments from terminal for:
     *    -datatype = word/long/line
     *    -sortingType = natural/byCount
     *    -inputFile = filepath
     *    -outputFile = filepath
     * Assign values to relevant class variables.
     * @param args arguments from terminal.
     */
    private static void parseArgs(String[] args) {

        for (int i = 0; i < args.length - 1; i++) {

            if ("-sortingType".equals(args[i])) {
                switch (args[i + 1]) {
                    case "natural":
                        sortingType = Sorter.SortType.NATURAL;
                        break;
                    case "byCount":
                        sortingType = Sorter.SortType.BY_COUNT;
                        break;
                    default:
                        System.out.println("No sorting type defined!");
                }
            } else if ("-dataType".equals(args[i])) {
                switch (args[i + 1]) {
                    case "long":
                        dataType = Sorter.DataType.LONG;
                        break;
                    case "line":
                        dataType = Sorter.DataType.LINE;
                        break;
                    case "word":
                        dataType = Sorter.DataType.WORD;
                        break;
                    default:
                        System.out.println("No data type defined!");
                }
            } else if ("-inputFile".equals(args[i])) {
                inputFile = new File(args[i + 1]);
            } else if ("-outputFile".equals(args[i])) {
                outputFile = new File(args[i + 1]);
            } else if (args[i].startsWith("-")) {
                System.out.printf("\"%s\" isn't a valid parameter. It's skipped.%n", args[i]);
            }

            // error-proof the final argument
            if (i + 1 == args.length - 1 && args[args.length - 1].startsWith("-")) {
                System.out.printf("\"%s\" isn't a valid parameter. It's skipped.%n", args[args.length - 1]);
            }
        }

        // create output file if not exists
        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetch data either from a file (inputFile)
     * or System.in.
     * @return listed data line by line.
     */
    private static ArrayList<String> getInput() {

        if (inputFile == null) {
            return inputFromStdin();
        } else {
            return inputFromFile();
        }
    }

    /**
     * Read text line by line from System.in where
     * each line is stored in a list.
     * @return listed data line by line.
     */
    private static ArrayList<String> inputFromStdin() {

        ArrayList<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                input.add(line);
            }
        }
        return input;
    }

    /**
     * Read text from File (inputFile), line by line
     * where each line is stored in a list.
     * @return listed data line by line.
     */
    private static ArrayList<String> inputFromFile() {

        ArrayList<String> input = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while (true) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                input.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading from file.");
            e.printStackTrace();
        }

        return input;
    }

    /**
     * Write given data to the file (outputFile).
     * @param data text
     */
    private static void saveToFile(String data) {

        System.out.println(data);

        try (PrintWriter pw = new PrintWriter(outputFile)) {
            pw.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sort given array of data according to
     * info stored in class variables.
     * Store the results to file (outputFile).
     * @param data text to be sorted.
     */
    private static void sort(ArrayList<String> data) {

        Sorter sorter = Sorter.factory(dataType);
        String sortedData = sorter.sort(sortingType, data);
        saveToFile(sortedData);
    }

    /**
     * Run this method to start the application.
     * @param args arguments passed from terminal.
     */
    public static void run(String[] args) {

        parseArgs(args);
        ArrayList<String> array = getInput();
        sort(array);
    }
}
