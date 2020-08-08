/*
Run from terminal with args:
-mode mode (enc or dec) -key integer -data string -alg algorithm (shift or unicode) -in sourceFilePath -out destinationFilePath
*/

import java.util.Scanner;
import java.io.*;

public class Main {

    static String MODE = "enc";
    static String algorithm = "shift";
    static int KEY = 0;
    static String data = "";
    static String modifiedData = "";
    static String sourceFilepath = "";
    static String destinationFilepath = "";

    public static void main(String[] args) {

        // assign values to fields
        parseArgs(args);

        // use selected encryption algorithm and process data
        Cipher cipher = CipherFactory.getAlgorithm(algorithm);
        modifiedData = cipher.process(MODE, data, KEY);

        // output data to file or terminal
        if (destinationFilepath.isEmpty()) {
            System.out.println(modifiedData);
        } else {
            writeToFile(destinationFilepath, modifiedData);
        }
    }

    public static void parseArgs(String[] args) {
        boolean dataFromTerminal = false;
        boolean dataFromFile = false;
        for (int i = 0; i < args.length; i += 2) {
            String command = args[i];
            switch (command) {
                case "-mode":
                    MODE = args[i + 1];
                    break;
                case "-data":
                    data = args[i + 1];
                    dataFromTerminal = true;
                    break;
                case "-key":
                    KEY = Integer.parseInt(args[i + 1]);
                    break;
                case "-in":
                    sourceFilepath = args[i + 1];
                    dataFromFile = true;
                    break;
                case "-out":
                    destinationFilepath = args[i + 1];
                    break;
                case "-alg":
                    algorithm = args[i + 1];
                    break;
            }
        }

        if (dataFromTerminal && dataFromFile) {
            return;
        } else if (dataFromFile) {
            data = readFromFile(sourceFilepath);
        }
    }
    
    public static String readFromFile(String filepath) {
        String text = "";
        try (FileReader reader = new FileReader(filepath)) {
            int data = reader.read();
            while (data != -1) {
                text += (char) data;
                data = reader.read();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
        return text;
    }

    public static void writeToFile(String filepath, String text) {
        try (FileWriter writer = new FileWriter(filepath)) {
            writer.write(text);
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

}

interface ICipher {
    String process(String mode, String data, int key);
    String encode(String data, int key);
    String decode(String data, int key);
}

abstract class Cipher implements ICipher {

    public String process(String mode, String data, int key) {
        switch (mode) {
            case "enc":
                return encode(data, key);
            case "dec":
                return decode(data, key);
            default:
                System.out.println("Unknown command");
                return null;
        }
    }
}

class ShiftCipher extends Cipher {

    public String encode(String data, int key) {
        String cipherText = "";
        for (int i = 0; i < data.length(); i++) {
            char currentLetter = data.charAt(i);
            // for lowercase letters:
            if (currentLetter >= 97 && currentLetter <= 122) {
                char editedLetter = (char) ((int) currentLetter + key);
                if (editedLetter > 122) {
                    editedLetter -= 26;
                } else if (editedLetter < 97) {
                    editedLetter += 26;
                }
                cipherText += editedLetter;
            // for uppercase letters:
            } else if (currentLetter >= 65 && currentLetter <= 90) {
                char editedLetter = (char) ((int) currentLetter + key);
                if (editedLetter > 90) {
                    editedLetter -=26;
                } else if (editedLetter < 65) {
                    editedLetter += 26;
                }
                cipherText += editedLetter;
            // all other characters:
            } else {
                cipherText += currentLetter;
            }
        }
        return cipherText;
    }

    public String decode(String data, int key) {
        return encode(data, -key);        
    }
}

class UnicodeCipher extends Cipher {

    public String encode(String data, int key) {
        String cipherText = "";
        for (int i = 0; i < data.length(); i++) {
            char editedLetter = (char) ((int) data.charAt(i) + key);
            cipherText += editedLetter;
        }
        return cipherText;
    }

    public String decode(String data, int key) {
        return encode(data, -key);     
    }
}

class CipherFactory {

    public static Cipher getAlgorithm(String algorithmType) {
        switch (algorithmType) {
            case "shift":
                return new ShiftCipher();
            case "unicode":
                return new UnicodeCipher();
            default:
                return null;
        }
    }
}
