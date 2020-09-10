package phonebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private static List<String> readFromFile(String filePath) {
        /*
        * Reads contents of specified file
        * saves each line in ArrayList and returns it
        * */

        ArrayList<String> array = new ArrayList<>();

        File file = new File(filePath);
        try (BufferedReader br = new BufferedReader(
                                    new FileReader(file))) {

            String input = br.readLine();
            while (input != null) {
                array.add(input);
                input = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    private static String getResults(int foundEntries, int totalEntries) {
        /*
        * returns results as string
        * */

        return String.format("Found %d / %d entries. ", foundEntries,
                                                        totalEntries);
    }

    private static String getTime(long time) {
        /*
         * returns time given in milliseconds
         * as string, in human readable format (min, sec, ms)
         * */

        long minutes = time / 60000;
        time %= 60000;
        long seconds = time / 1000;
        long ms = time % 1000;
        return String.format("%d min. %d sec. %d ms. ",
                                                        minutes,
                                                        seconds,
                                                        ms);
    }

    public static void main(String[] args) {

        List<String> directory = readFromFile("directory.txt");
        List<String> targets = readFromFile("find.txt");
        List<String> targetsFound;

        // linear
        targetsFound = Searcher.linearSearch(directory, targets);
        System.out.print(getResults(targetsFound.size(), targets.size()));
        System.out.printf("Time taken: %s%n%n", getTime(Searcher.getTimer()));
        System.out.println();


        // bubble + jump
        targetsFound = Searcher.jumpSearch(directory, targets);
        System.out.print(getResults(targetsFound.size(), targets.size()));
        System.out.println("Time taken: " + (getTime(Searcher.getTimer() + Sorter.getTimer())));
        System.out.println("Sorting time: " + getTime(Sorter.getTimer()));
        System.out.println("Searching time: " + getTime(Searcher.getTimer()));
        System.out.println();

        // quick + binary
        directory = readFromFile("/home/trypios/Downloads/directory.txt");  // reset directory state to unsorted
        targetsFound = Searcher.binarySearch(directory, targets);
        System.out.print(getResults(targetsFound.size(), targets.size()));
        System.out.println("Time taken: " + (getTime(Searcher.getTimer() + Sorter.getTimer())));
        System.out.println("Sorting time: " + getTime(Sorter.getTimer()));
        System.out.println("Searching time: " + getTime(Searcher.getTimer()));
        System.out.println();

        // hash table
        targetsFound = Searcher.hashSearch(directory, targets);
        System.out.print(getResults(targetsFound.size(), targets.size()));
        System.out.println("Time taken: " + (getTime(Searcher.getTimer() + Sorter.getTimer())));
        System.out.println("Creating time: " + getTime(Sorter.getTimer()));
        System.out.println("Searching time: " + getTime(Searcher.getTimer()));
        System.out.println();

    }
}
