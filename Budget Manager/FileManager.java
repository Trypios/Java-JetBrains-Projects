package budget;

import java.io.*;

public class FileManager {

    private static final File file = new File("purchases.txt");

    public static void save(String text) {

        try (FileWriter fr = new FileWriter(file, false)) {
            fr.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
