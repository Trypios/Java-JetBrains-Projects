package sorting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    
    private static String dataType = "word";
    private static String sortingType = "natural";
    private static File inputFile;
    private static File outputFile = new File("out.txt");

    private static void parseArgs(String[] args) {
        /*
        * Parses arguments from terminal for:
        * -dataType = word/long/line
        * -sortingType = natural/byCount
        * -inputFile = filepath
        * -outputFile = filepath
        *
        * Assigns values and prints invalid arguments
        * */

        for (int i = 0; i < args.length - 1; i++) {

            if ("-sortingType".equals(args[i])) {
                switch (args[i + 1]) {
                    case "natural":
                    case "byCount":
                        sortingType = args[i + 1];
                        break;
                    default:
                        System.out.println("No sorting type defined!");
                }
            } else if ("-dataType".equals(args[i])) {
                switch (args[i + 1]) {
                    case "long":
                    case "line":
                    case "word":
                        dataType = args[i + 1];
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

    private static ArrayList<String> getInput() {
        /*
        * Fetch data from File or std in
        * returns data line by line in a list
        * */

        if (inputFile == null) {
            return inputFromStdin();
        } else {
            return inputFromFile();
        }
    }

    private static ArrayList<String> inputFromStdin() {
        /*
         * Reads text line by line from System.in
         * Each line is stored in a list
         * Returns said list
         * */

        ArrayList<String> input = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                input.add(line);
            }
        }
        return input;
    }

    private static ArrayList<String> inputFromFile() {
        /*
        * Reads text from inputFile, line by line
        * Each line is stored in a list
        * Returns said list
        * */

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

    private static void saveToFile(String output) {
        /*
         * Writes string to outputFile
         * */

        System.out.println(output);

        try (PrintWriter pw = new PrintWriter(outputFile)) {
            pw.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sort(ArrayList<String> data) {
        /*
        * Sort factory
        *
        * Sorts given array based on parsed arguments
        * from terminal or the defaults
        * Saves the results to outputFile
        * */

        Sorter sorter;

        switch (dataType) {
            case "long":
                sorter = new LongSorter(data);
                break;
            case "line":
                sorter = new LineSorter(data);
                break;
            case "word":
                sorter = new WordSorter(data);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dataType);
        }

        String sortedData = sorter.run(sortingType);
        saveToFile(sortedData);
    }
    
    public static void main(final String[] args) {
        
        parseArgs(args);
        ArrayList<String> array = getInput();
        sort(array);
    }
}
