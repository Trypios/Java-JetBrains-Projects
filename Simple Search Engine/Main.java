package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final InvertedIndex invertedIndex = new InvertedIndex();

    private static boolean parsePeople(String[] args) {

        File inputFile = null;

        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--data")) {
                inputFile = new File(args[i + 1]);
                break;
            }
        }

        if (inputFile == null) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

            int lineIndex = 0;
            String line = br.readLine();

            while (line != null) {
                invertedIndex.add(line, lineIndex++);
                line = br.readLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void menu() {

        while (true) {
            System.out.println("=== Menu ===");
            System.out.println("1. Find a person");
            System.out.println("2. Print all people");
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

    private static void findPerson() {

        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = SCANNER.nextLine();
        System.out.println();
        System.out.println("Enter a name or email to search all suitable people.");
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

    private static void displayMatches(List<String> matches) {

        if (matches == null || matches.isEmpty()) {
            System.out.println("No persons found");
            return;
        }

        System.out.printf("%d persons found:%n", matches.size());
        for (String person : matches) {
            System.out.println(person);
        }
        System.out.println();
    }

    private static void displayPeople() {

        if (invertedIndex.isEmpty()) {
            System.out.println("No people found.");
            return;
        }

        System.out.println("=== List of people ===");
        for (String person : invertedIndex.getLines()) {
            System.out.println(person);
        }

        System.out.println();
    }

    public static void main(String[] args) {

        boolean valid = parsePeople(args);
        if (valid) {
            menu();
        }
    }

}
