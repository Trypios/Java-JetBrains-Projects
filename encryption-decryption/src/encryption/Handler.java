package encryption;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class responsible for getting data meant for encryption/decryption
 * and requesting info on how to handle said data.
 */
public class Handler {

    private static final Scanner scanner;
    private static final Map<String, String> info;

    static {
        scanner = new Scanner(System.in);
        // initialize defaults
        info = new HashMap<>(Map.of(
                "mode", "enc",
                "key", "0",
                "data", "",
                "alg", "shift",
                "modData", "",
                "in", "",
                "out", ""
        ));
    }

    /**
     * Run this method to start the Encryption/Decryption.
     * @param args arguments passed from terminal.
     */
    public static void run(String[] args) {

        parseArgs(args);
        process();
    }

    /**
     * Overloaded method
     */
    public static void run() {

        requestInput();
        process();
    }

    /**
     * Encrypt or decrypt data.
     * All information have been collected from parseArgs() or requestInput().
     */
    private static void process() {

        // use selected encryption algorithm
        Cipher cipher = Cipher.getAlgorithm(info.get("alg"));
        if (cipher == null) {
            System.out.println("Unknown encryption algorithm.");
            return;
        }

        // process data
        String modifiedData = cipher.process(
                info.get("mode"),
                info.get("data"),
                Integer.parseInt(info.get("key"))
        );
        if (modifiedData == null) {
            System.out.println("Unknown command.");
            return;
        }

        info.put("modData", modifiedData);

        // output data to either file or terminal
        if (info.get("out").isEmpty()) {
            System.out.println(info.get("modData"));
        } else {
            writeToFile(info.get("out"), info.get("modData"));
            System.out.printf("Open %s to find the processed data.%n", info.get("out"));
        }
    }

    /**
     * Parse arguments to gather information
     * on how to process data for encryption or decryption.
     * @param args arguments from terminal
     */
    private static void parseArgs(String[] args) {

        for (int i = 0; i < args.length; i += 2) {
            String command = args[i];
            String value;
            try {
                value = args[i + 1];
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            switch (command) {
                case "-mode":
                    if (value.toLowerCase().matches("enc|dec")) {
                        info.put("mode", args[i + 1]);
                    }
                    break;
                case "-data":
                    if (!value.startsWith("-")) {
                        info.put("data", args[i + 1]);
                    }
                    break;
                case "-key":
                    if (args[i + 1].matches("[-+]?\\d+")) {
                        info.put("key", args[i + 1]);
                    }
                    break;
                case "-in":
                    if (!args[i + 1].startsWith("-")) {
                        info.put("in", args[i + 1]);
                    }
                    break;
                case "-out":
                    if (!args[i + 1].startsWith("-")) {
                        info.put("out", args[i + 1]);
                    }
                    break;
                case "-alg":
                    if (value.toLowerCase().matches("shift|unicode")) {
                        info.put("alg", args[i + 1]);
                    }
                    break;
            }
        }

        // give priority to data passed from terminal over input file
        if (!info.get("in").isEmpty() && info.get("data").isEmpty()) {
            info.put("data", readFromFile(info.get("in")));
        }
    }

    /**
     * Request user input to gather information
     * on how to process data for encryption or decryption.
     */
    private static void requestInput() {

        System.out.println("Select mode:\n   'enc' - encryption\n   'dec' - decryption\n");
        String mode = scanner.nextLine();
        if (mode.matches("enc|dec")) {
            info.replace("mode", mode);
        }

        System.out.println("Select mode:\n   'shift' - Caesar-like monoalphabetic substitution\n   " +
                "'unicode' - substitution beyond basic latin characters");
        String alg = scanner.nextLine();
        if (alg.matches("shift|unicode")) {
            info.replace("alg", alg);
        }

        System.out.println("Select the cipher key:");
        String key = scanner.nextLine();
        if (key.matches("[-+]?\\d+")) {
            info.replace("key", key);
        }

        System.out.println("Input data to be processed, " +
                "or press enter to load data from a file:");
        String data = scanner.nextLine();
        info.replace("data", data);
        if (info.get("data").isEmpty()) {
            System.out.println("Select the path/filename of the file to load data from, " +
                    "or press enter to print result here:");
            String in = scanner.nextLine();
            info.replace("in", in);
            info.replace("data", readFromFile(info.get("in")));
        }

        System.out.println("Select the path/filename of the file to save data to:");
        String out = scanner.nextLine();
        info.replace("out", out);
    }

    /**
     * Read data intended for encryption or decryption, from a specified file.
     * @param filename An input file.
     * @return a string of loaded data.
     */
    private static String readFromFile(String filename) {

        StringBuilder text = new StringBuilder();
        try (FileReader reader = new FileReader(filename)) {
            int data = reader.read();
            while (data != -1) {
                text.append((char) data);
                data = reader.read();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
        System.out.println("Loaded data from file.");
        return text.toString();
    }

    /**
     * Save data to a specified file.
     * @param filename An output file.
     * @param text a string of data to be saved.
     */
    private static void writeToFile(String filename, String text) {

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(text);
            System.out.println("Saved to file.");
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

}
