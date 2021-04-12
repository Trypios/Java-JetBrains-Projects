package readability;

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

/**
 * Controller of ReadabilityScore
 */
public class Controller {

    private static final Scanner scanner = new Scanner(System.in);
    private static String text;

    /**
     * Run this method to start a readability test.
     * @param args argument(s) passed from terminal.
     *             expecting a filename to read text from.
     */
    public static void run(String[] args) {

        String filename = "dostoevsky-excerpt.txt";
        if (args.length > 0) {
            filename = args[0];
        }
        text = readFromFile(filename);
        menu();
        scanner.close();
    }

    private static void menu() {

        while (true) {
            System.out.println("Type the Readability Test you want to calculate the score: ");
            System.out.println("    ARI - Automated Readability index");
            System.out.println("    FK - Flesch-Kincaid");
            System.out.println("    SMOG - Simple Measure of Gobbledygook (SMOG index)");
            System.out.println("    CL - Coleman-Liau index");
            System.out.println("    ALL - All four above");
            String userInput = scanner.nextLine().toUpperCase();
            if (userInput.matches("ARI|FK|SMOG|CL|ALL")) {
                ReadabilityScore.applyAlgorithm(userInput, text);
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Read text from the file specified.
     * @param filename a file to read text from.
     * @return a string of text data.
     */
    private static String readFromFile(String filename) {

        StringBuilder data = new StringBuilder();
        try (FileReader reader = new FileReader(filename)) {
            int unicode = reader.read();
            while (unicode != -1) {
                data.append(Character.toString(unicode));
                unicode = reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
}
