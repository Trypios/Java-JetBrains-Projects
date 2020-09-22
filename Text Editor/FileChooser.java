package editor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileChooser {

    private static final JFileChooser fileChooser;

    static {
        fileChooser = new JFileChooser();
        fileChooser.setName("FileChooser");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        fileChooser.setFileFilter(filter);
    }

    public static JFileChooser getFileChooser() {
        return fileChooser;
    }

    public static void load(TextEditor textEditor) {
        /*
         * Opens dialog to select txt from file-browser,
         * then read it
         * */

        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String url = fileChooser.getSelectedFile().getPath();
            try {
                byte[] text = Files.readAllBytes(Paths.get(url));
                textEditor.setTextArea(new String(text));
            } catch (IOException e) {
                textEditor.setTextArea("");
                e.printStackTrace();
            }
        }
    }

    public static void save(TextEditor textEditor) {
        /*
         * Opens dialog to select txt from file-browser,
         * then save to it
         * */

        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter pw = new PrintWriter(fileChooser.getSelectedFile())) {
                String text = textEditor.getTextArea().getText();
                pw.print(text);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
