package editor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class responsible for selecting files from the OS file manager and
 *    either read content from the file to display on the text-editor,
 *    or write content from the text-editor to a file.
 */
public class FileChooser {

    private static final JFileChooser fileChooser;

    static {
        fileChooser = new JFileChooser();
        fileChooser.setName("FileChooser");
        fileChooser.setFileFilter(
                new FileNameExtensionFilter(
                "Text files", "txt")
        );
    }

    /**
     * Getter.
     * @return the file handler of this text-editor application.
     */
    public static JFileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * Open dialog to select a txt file from the OS file manager
     * and read content from inside. Pass read content to the textEditor.
     * @param textEditor the text-editor application.
     */
    public static void load(TextEditor textEditor) {

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

    /**
     * Open dialog to select a txt file from the OS file manager
     * and save textEditor content inside that file.
     * @param textEditor the text-editor application.
     */
    public static void save(TextEditor textEditor) {

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
