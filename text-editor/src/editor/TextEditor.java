package editor;

import javax.swing.*;
import java.awt.*;

/**
 * GUI plain text editor application like Notepad and TextEdit.
 */
public class TextEditor extends JFrame {

    private JCheckBox regexCheckbox;
    private JTextField searchField;
    private JTextArea textArea;

    /**
     * Set up the main window
     */
    public TextEditor() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setTitle("TextEditor");

        setJMenuBar(menuBar());  // add top menu-bar
        add(FileChooser.getFileChooser());

        setLayout(new BorderLayout());
        add(buttonArea(), BorderLayout.NORTH);  // add button area under menu-bar
        add(scrollableTextArea(), BorderLayout.CENTER);  // add text-area under button area

        setVisible(true);
    }

    /**
     * Getter.
     * @return the area responsible for displaying text.
     */
    public JTextArea getTextArea() {

        return textArea;
    }

    /**
     * Setter.
     * Set the content for the text-area.
     * @param text content to be displayed be this application.
     */
    public void setTextArea(String text) {
        textArea.setText(text);
    }

    /**
     * Getter. Use with Searcher class.
     * @return the search-field content
     */
    public String getSearchField() {
        return searchField.getText();
    }

    /**
     * Getter. Use with Searcher class.
     * @return the regex check-box.
     */
    public JCheckBox getRegexCheckbox() {
        return regexCheckbox;
    }

    /**
     * Top menu-bar (File Search)
     * @return a window pane with menu buttons.
     */
    private JMenuBar menuBar() {

//        FILE-TAB & COMPONENTS

        JMenuItem open = new JMenuItem("Open");
        open.setName("MenuOpen");
        open.addActionListener(actionEvent -> FileChooser.load(this));

        JMenuItem save = new JMenuItem("Save");
        save.setName("MenuSave");
        save.addActionListener(actionEvent -> FileChooser.save(this));

        JMenuItem exit = new JMenuItem("Exit");
        exit.setName("MenuExit");
        exit.addActionListener(actionEvent -> this.dispose());

        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(exit);


//        SEARCH-TAB & COMPONENTS

        JMenuItem startSearch = new JMenuItem("Start search");
        startSearch.setName("MenuStartSearch");
        startSearch.addActionListener(actionEvent -> {
            Searcher.search(this);
            Searcher.searchNext(this);
        });

        JMenuItem prevMatch = new JMenuItem("Previous match");
        prevMatch.setName("MenuPreviousMatch");
        prevMatch.addActionListener(actionEvent -> Searcher.searchPrev(this));

        JMenuItem nextMatch = new JMenuItem("Next match");
        nextMatch.setName("MenuNextMatch");
        nextMatch.addActionListener(actionEvent -> Searcher.searchNext(this));

        JMenuItem regex = new JMenuItem("Use regular expressions");
        regex.setName("MenuUseRegExp");
        regex.addActionListener(actionEvent -> regexCheckbox.doClick());

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");
        searchMenu.add(startSearch);
        searchMenu.add(prevMatch);
        searchMenu.add(nextMatch);
        searchMenu.add(regex);


//        FINAL MENU-BAR
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        return menuBar;

    }

    /**
     * Button area (under menu-bar)
     * @return a window pane with buttons and fields.
     */
    private JPanel buttonArea() {

//        LEFT-SIDE & COMPONENTS (FILE BUTTONS)

        JButton openButton = new JButton(new ImageIcon("assets/load.png"));
        openButton.setName("OpenButton");
        openButton.addActionListener(actionEvent -> FileChooser.load(this));

        JButton saveButton = new JButton(new ImageIcon("assets/save.png"));
        saveButton.setName("SaveButton");
        saveButton.addActionListener(actionEvent -> FileChooser.save(this));

        JPanel fileComps = new JPanel(new GridLayout(1, 2));
        fileComps.add(openButton);
        fileComps.add(saveButton);


//        RIGHT-SIDE & COMPONENTS (SEARCH ELEMENTS)

        this.searchField = new JTextField();
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(200, 42));

        JButton searchButton = new JButton(new ImageIcon("assets/search.png"));
        searchButton.setName("StartSearchButton");
        searchButton.addActionListener(actionEvent -> {
            Searcher.search(this);
            Searcher.searchNext(this);
        });

        JButton previousMatchButton = new JButton(new ImageIcon("assets/prev.png"));
        previousMatchButton.setName("PreviousMatchButton");
        previousMatchButton.addActionListener(actionEvent -> Searcher.searchPrev(this));

        JButton nextMatchButton = new JButton(new ImageIcon("assets/next.png"));
        nextMatchButton.setName("NextMatchButton");
        nextMatchButton.addActionListener(actionEvent -> Searcher.searchNext(this));

        this.regexCheckbox = new JCheckBox("Regex?");
        regexCheckbox.setName("UseRegExCheckbox");
        //regexCheckbox.addActionListener(actionEvent -> implement());

        JPanel searchComps = new JPanel(new FlowLayout());
        searchComps.add(searchField);
        searchComps.add(searchButton);
        searchComps.add(previousMatchButton);
        searchComps.add(nextMatchButton);
        searchComps.add(regexCheckbox);


//        FINAL BUTTON-AREA

        JPanel buttonArea = new JPanel(new FlowLayout());
        buttonArea.add(fileComps);
        buttonArea.add(searchComps);

        return buttonArea;
    }

    /**
     * Large text area (under button-area).
     * @return a window pane for text.
     */
    private JScrollPane scrollableTextArea() {

        this.textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setName("TextArea");

        JScrollPane scrollableTextArea = new JScrollPane(textArea);
        scrollableTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollableTextArea.setBounds(10, 10, 600, 300);
        scrollableTextArea.setName("ScrollPane");

        return scrollableTextArea;
    }

}
