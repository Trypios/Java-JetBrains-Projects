package budget;

import java.io.*;

/**
 * Class responsible for saving/loading purchases from and to a predefined file.
 */
public class FileManager {

    private static final File file = new File("purchases.txt");

    /**
     * Writes a given text (formatted purchases) to the file.
     * Overwrites file contents, if any.
     * @param text Content to be written on file.
     */
    public static void save(String text) {

        try (FileWriter fr = new FileWriter(file, false)) {
            fr.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads content (formatted purchases) from the file.
     * @return a formatted string of purchases.
     */
    public static String load() {

        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                text.append(line).append("\n");
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }
}
