package flashcards;

import java.io.*;
import java.util.*;

public class FlashcardPack {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOMIZER = new Random();
    private final ArrayList<Flashcard> ALL_FLASHCARDS;
    private final ArrayList<String> LOG;
    private File importFile;
    private File exportFile;
    private boolean active;


    public FlashcardPack() {
        this.ALL_FLASHCARDS = new ArrayList<>();
        this.LOG = new ArrayList<>();
        this.importFile = null;
        this.exportFile = null;
        this.active = true;
    }
    
    public FlashcardPack(String[] args) {

        this();

        if (args.length % 2 != 0) {
            System.out.println("Invalid terminal input");
            this.active = false;
        }

        for (int i = 0; i < args.length - 1; i += 2) {
            switch (args[i]) {
                case "-import":
                    this.importFile = new File(String.format(".%s%s", File.separator, args[i + 1]));
                    break;
                case "-export":
                    this.exportFile = new File(String.format(".%s%s", File.separator, args[i + 1]));
                    break;
            }
        }

        if (importFile != null) {
            importFlashcards(importFile);
        }
    }

    public void run() {
        while (active) {
            menu();
        }
    }

    private void displayOptions() {
        String msg = "'add' a card\n" +
                    "'remove' a card\n" +
                    "'import' cards from file\n" +
                    "'export' cards to file\n" +
                    "'ask' for a definition of some random cards\n" +
                    "'exit' the program";

        printAndLog(msg);
    }

    private void menu() {
        // String msg = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
        displayOptions();
        printAndLog(msg);

        String input = SCANNER.nextLine();
        log(input);

        switch (input) {
            case "add":
                addFlashcard();
                break;
            case "remove":
                removeFlashcard();
                break;
            case "import":
                importFlashcards();
                break;
            case "export":
                exportFlashcards();
                break;
            case "ask":
                ask();
                break;
            case "log":
                getLog();
                break;
            case "hardest card":
                hardestCard();
                break;
            case "reset stats":
                resetStats();
                break;
            case "exit":
                exit();
                break;
            default:
                String response = "Invalid command\n";
                printAndLog(response);
        }
    }

    private void addFlashcard() {

        String term;
        String definition;

        String msg = "The card:";
        printAndLog(msg);
        term = SCANNER.nextLine();
        log(term);

        for (Flashcard card: ALL_FLASHCARDS) {
            if (card.getTerm().equals(term)) {
                msg = String.format("The card \"%s\" already exists.%n%n", term);
                printAndLog(msg);
                return;
            }
        }

        msg = "The definition of the card:";
        printAndLog(msg);
        definition = SCANNER.nextLine();
        log(definition);

        for (Flashcard card: ALL_FLASHCARDS) {
            if (card.getDefinition().equals(definition)) {
                msg = String.format("The definition \"%s\" already exists.%n", definition);
                printAndLog(msg);
                return;
            }
        }

        ALL_FLASHCARDS.add(new Flashcard(term, definition, 0));
        msg = String.format("The pair (\"%s\":\"%s\") has been added.%n", term, definition);
        printAndLog(msg);
    }

    private void removeFlashcard() {

        String term;

        String msg = "The card:";
        printAndLog(msg);
        term = SCANNER.nextLine();
        log(term);

        for (Flashcard card : ALL_FLASHCARDS) {
            if (card.getTerm().equals(term)) {
                ALL_FLASHCARDS.remove(card);
                msg = "The card has been removed.\n";
                printAndLog(msg);
                return;
            }
        }

        msg = String.format("Can't remove \"%s\" : there is no such card.%n%n", term);
        printAndLog(msg);
    }

    private void importFlashcards(File inFile) {

        overwriteFile(inFile);

        FileReader inputFile;
        try {
            inputFile = new FileReader(inFile);
        } catch (FileNotFoundException e) {
            msg = "File not found.\n";
            printAndLog(msg);
            return;
        }

        String term;
        String definition;
        int errorCount;
        int counter = 0;
        try (BufferedReader br = new BufferedReader(inputFile)) {
            String line = br.readLine();
            while (line != null) {
                String[] lineParts = line.split(":");
                term = lineParts[0];
                definition = lineParts[1];
                errorCount = Integer.parseInt(lineParts[2]);

                boolean cardExists = false;
                for (Flashcard card : ALL_FLASHCARDS) {
                    if (card.getTerm().equals(term)) {
                        card = new Flashcard(term, definition, errorCount);
                        cardExists = true;
                        break;
                    }
                }

                if (!cardExists) {
                    ALL_FLASHCARDS.add(new Flashcard(term, definition, errorCount));
                }

                counter++;
                line = br.readLine();
            }
        } catch (IOException e) {
            msg = "Error while getting input from file.\n";
            printAndLog(msg);
        }

        if (counter > 0) {
            msg = String.format("%d cards have been loaded.%n", counter);
            printAndLog(msg);
        }
    }

    private void importFlashcards() {

        String msg = "File name:";
        printAndLog(msg);
        String input = SCANNER.nextLine();
        log(input);
        String filename = String.format("\\.%s%s", File.separator, input);

        this.importFile = new File(filename);
        importFlashcards(importFile);
    }

    private void exportFlashcards(File outFile) {

        String msg;

        overwriteFile(outFile);

        try (PrintWriter pw = new PrintWriter(outFile)) {
            for (Flashcard card : ALL_FLASHCARDS) {
                String line = String.format("%s:%s:%d", card.getTerm(), card.getDefinition(), card.getErrorCount());
                pw.println(line);
            }
            msg = String.format("%d cards have been saved.", ALL_FLASHCARDS.size());
            printAndLog(msg);
        } catch (FileNotFoundException e) {
            msg = "File not found.\n";
            printAndLog(msg);
        }
    }

    private void exportFlashcards() {

        String msg = "File name:";
        printAndLog(msg);
        String input = SCANNER.nextLine();
        log(input);
        String filename = String.format(".%s%s", File.separator, input);

        this.exportFile = new File(filename);
        exportFlashcards(exportFile);
    }

    private void overwriteFile(File file) {
        String msg;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            msg = "Error while creating file\n";
            printAndLog(msg);
        }
    }

    private void ask() {

        int timesToAsk;
        while (true) {
            String msg = "How many times to ask?";
            printAndLog(msg);
            try {
                String input = SCANNER.nextLine();
                log(input);
                timesToAsk = Integer.parseInt(input);
                break;
            } catch (Exception e) {
                msg = "Invalid input\n";
                printAndLog(msg);
            }
        }

        if (ALL_FLASHCARDS.size() == 0) {
            String msg = "No flashcards have been found.\n";
            printAndLog(msg);
            return;
        }

        for (int i = 0; i < timesToAsk; i++) {

            // get random flashcard
            int index = RANDOMIZER.nextInt(ALL_FLASHCARDS.size());
            Flashcard testCard = ALL_FLASHCARDS.get(index);
            String term = testCard.getTerm();
            String definition = testCard.getDefinition();

            String msg = String.format("Print the definition of \"%s\":", testCard.getTerm());
            printAndLog(msg);

            String userDef = SCANNER.nextLine();
            log(userDef);

            // if user is correct
            if (userDef.equals(testCard.getDefinition())) {
                msg = "Correct!\n";
                printAndLog(msg);
            } else {
                // checks if the user is wrong, but the given answer is right for another flashcard
                boolean completelyWrong = true;
                for (Flashcard otherCard : ALL_FLASHCARDS) {
                    if (otherCard.getDefinition().equals(userDef)) {
                        completelyWrong = false;
                        msg = String.format("Wrong. The right answer is \"%s\", " +
                                "but your definition is correct for \"%s\".%n", testCard.getDefinition(), otherCard.getTerm());
                        printAndLog(msg);
                        testCard.increaseErrorCount();
                        break;
                    }
                }

                // if user is completely wrong
                if (completelyWrong) {
                    msg = String.format("Wrong. The right answer is \"%s\".%n", definition);
                    printAndLog(msg);
                    testCard.increaseErrorCount();
                }

            }
        }
    }

    private void printAndLog(String msg) {
        System.out.println(msg);
        LOG.add(String.format("%s%n", msg));
    }

    private void log(String msg) {
        LOG.add(String.format("%s%n", msg));
    }

    private void getLog() {

        String msg = "File name:";
        printAndLog(msg);

        String input = SCANNER.nextLine();
        log(input);

        String filename = "./" + input;
        File logFile = new File(filename);

        try {
            if (logFile.exists()) {
                logFile.delete();
            }
            logFile.createNewFile();        }
        catch (IOException e) {
            msg = "Error while creating file\n";
            printAndLog(msg);
        }

        try (PrintWriter pw = new PrintWriter(logFile)) {
            for (String entry : LOG) {
                pw.println(entry);
            }
            msg = "The log has been saved.\n";
            printAndLog(msg);
        } catch (FileNotFoundException e) {
            msg = "File not found.\n";
            printAndLog(msg);
        }
    }

    private void hardestCard() {

        // check if there are hard words
        boolean hasHardCards = false;
        for (Flashcard card : ALL_FLASHCARDS) {
            if (card.getErrorCount() != 0) {
                hasHardCards = true;
                break;
            }
        }

        if (!hasHardCards) {
            String msg = "There are no cards with errors.\n";
            printAndLog(msg);
            return;
        }

        // find the the highest error-count
        int highestValue = Integer.MIN_VALUE;
        for (Flashcard card : ALL_FLASHCARDS) {
            if (card.getErrorCount() > highestValue) {
                highestValue = card.getErrorCount();
            }
        }

        // save hard words in a new array
        ArrayList<Flashcard> hardestWords = new ArrayList<>();
        for (Flashcard card : ALL_FLASHCARDS) {
            if (card.getErrorCount() == highestValue) {
                hardestWords.add(card);
            }
        }

        // show hardest word(s)
        String msg = "";
        StringBuilder hardestWordsString = new StringBuilder("");
        if (hardestWords.size() == 1) {
            for (Flashcard card : hardestWords) {
                hardestWordsString.append(String.format("\"%s\"", card.getTerm()));
            }
            if (highestValue == 1) {
                msg = String.format("The hardest card is %s. You have %d error answering it.\n",
                        hardestWordsString, highestValue);
            } else {
                msg = String.format("The hardest card is %s. You have %d errors answering it.\n",
                        hardestWordsString, highestValue);
            }
        } else {
            for (Flashcard card : hardestWords) {
                hardestWordsString.append(String.format("\"%s\", ", card.getTerm()));
            }
            hardestWordsString.delete(hardestWordsString.length() - 2, hardestWordsString.length());
            msg = String.format("The hardest cards are %s. You have %d errors answering them.\n",
                    hardestWordsString, highestValue);
        }

        printAndLog(msg);

    }

    private void resetStats() {

        for (Flashcard card : ALL_FLASHCARDS) {
            card.setErrorCount(0);
        }
        String msg = "Card statistics has been reset.\n";
        printAndLog(msg);
    }

    private void exit() {
        String msg = "Bye bye!";
        printAndLog(msg);
        active = false;
        if (exportFile != null && ALL_FLASHCARDS.size() > 0) {
            exportFlashcards(exportFile);
        }
    }

}
