package editor;

import java.util.*;
import java.util.regex.*;

/**
 * Class responsible for searching text within the text-editor application.
 */
public class Searcher {

    private static TreeMap<Integer, String> searchMatches;
    private static Matcher matcher;
    private static ListIterator<Integer> iterator;
    private static boolean forwardSearch = true;

    /**
     * Search the text-editor for the specified text found
     * on its relevant text-area. Results according to the
     * status of the regex check-box.
     * @param textEditor the text-editor application
     */
    public static void search(TextEditor textEditor) {

        String target = textEditor.getSearchField();
        String text = textEditor.getTextArea().getText();
        Pattern pattern = textEditor.getRegexCheckbox().isSelected() ?
                Pattern.compile(target, Pattern.CASE_INSENSITIVE) :
                Pattern.compile(target, Pattern.LITERAL | Pattern.CASE_INSENSITIVE);

        matcher = pattern.matcher(text);
        searchMatches = new TreeMap<>();

        while (matcher.find()) {
            searchMatches.put(matcher.start(), matcher.group());
        }

        iterator = new ArrayList<>(searchMatches.keySet()).listIterator();
    }

    /**
     * Once a match has been found by the search() method,
     * more matches may be found after the current match.
     * Once the last match has been found, the search cycles from the beginning.
     * @param textEditor the text-editor application
     */
    public static void searchNext(TextEditor textEditor) {

        if (iterator == null) {
            search(textEditor);
        }

        if (!forwardSearch && iterator.hasNext()) {
            iterator.next();
            forwardSearch = true;
        }

        if (searchMatches.size() == 0) {
            textEditor.getTextArea().setCaretPosition(textEditor.getTextArea().getText().length());
            return;
        }

        if (iterator.hasNext()) {

            int index = iterator.next();
            String foundText = searchMatches.get(index);

            if (!foundText.matches(matcher.pattern().pattern())) {
                search(textEditor);
                searchNext(textEditor);
            }
            textEditor.getTextArea().setCaretPosition(index + foundText.length());
            textEditor.getTextArea().select(index, index + foundText.length());
            textEditor.getTextArea().grabFocus();
        } else {
            search(textEditor);
            searchNext(textEditor);
        }
    }

    /**
     * Once a match has been found by the search() method,
     * more matches may be found before the current match.
     * Once the first match has been found, the search cycles from the end.
     * @param textEditor the text-editor application
     */
    public static void searchPrev(TextEditor textEditor) {

        if (iterator == null) {
            search(textEditor);
            while (iterator.hasNext()) {
                iterator.next();
            }
        }

        if (forwardSearch) {
            if (iterator.hasPrevious()) {
                iterator.previous();
            }
            forwardSearch = false;
        }

        if (searchMatches.size() == 0) {
            textEditor.getTextArea().setCaretPosition(0);
            return;
        }

        if (iterator.hasPrevious()) {
            int index = iterator.previous();
            String foundText = searchMatches.get(index);

            if (!foundText.matches(matcher.pattern().pattern())) {
                search(textEditor);
                searchPrev(textEditor);
            }

            textEditor.getTextArea().setCaretPosition(index + foundText.length());
            textEditor.getTextArea().select(index, index + foundText.length());
            textEditor.getTextArea().grabFocus();
        } else {
            iterator = null;
            searchPrev(textEditor);
        }
    }

}
