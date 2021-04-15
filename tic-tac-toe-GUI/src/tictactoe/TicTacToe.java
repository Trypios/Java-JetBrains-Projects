package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.lang.Thread;

public class TicTacToe extends JFrame {

    private JButton p1Button;
    private JButton startResetButton;
    private JButton p2Button;
    private JLabel labelStatus;
    private final java.util.List<JButton> slots;
    private final Map<String, Integer> slotIndexes;
    private Board board;
    private String p1Difficulty;
    private String p2Difficulty;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player opponent;
    private boolean active;

    {
        slots = new ArrayList<>();
        slotIndexes = new HashMap<>();
        board = new Board();
        active = false;
    }

    public TicTacToe() {

        // initialize frame and panels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 3, screenSize.height / 2);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        createControls();
        p1Difficulty = p1Button.getText();
        p2Difficulty = p2Button.getText();
        createMenu();
        createGrid();
        setVisible(true);
    }

    /**
     * Initialize players according to the two buttons (p1Button, p2Button).
     */
    private void initPlayers() {

        player1 = Player.createPlayer(
                p1Button.getText(), playerType(p1Button.getText()), Mark.X, this.board
        );
        player2 = Player.createPlayer(
                p2Button.getText(), playerType(p2Button.getText()), Mark.O, this.board
        );
        assert player1 != null && player2 != null;
        player1.setOpponent(player2);
        player2.setOpponent(player1);
        currentPlayer = player1;
        opponent = player2;
    }

    /**
     * Helper of initPlayers(). Determine the correct player type
     * by the text provided on one of the two player buttons.
     * @param type "Human" or "CPU DifficultyLevel"
     * @return a player type, by default human.
     */
    private Player.Type playerType(String type) {

        switch (type) {
            case "Human":
                return Player.Type.HUMAN;
            case "CPU Easy":
                return Player.Type.CPU_EASY;
            case "CPU Medium":
                return Player.Type.CPU_MEDIUM;
            case "CPU Hard":
                return Player.Type.CPU_HARD;
            case "CPU Impossible":
                return Player.Type.CPU_IMPOSSIBLE;
        }
        return Player.Type.HUMAN;
    }

    /**
     * Create the TicTacToe menu-bar. It has 2 submenus:
     * one with 4 menu items corresponding to the type of
     * players the game will start with and an exit item,
     * and another submenu with 4 menu items setting the
     * cpu level of difficulty. All listeners are assigned here.
     */
    private void createMenu() {

        // menus
        JMenu menuGame = new JMenu("Game");
        menuGame.setName("MenuGame");

        // game menu items
        JMenuItem menuHumanHuman = new JMenuItem("Human vs Human");
        menuHumanHuman.setName("MenuHumanHuman");
        JMenuItem menuHumanRobot = new JMenuItem("Human vs CPU");
        menuHumanRobot.setName("MenuHumanRobot");
        JMenuItem menuRobotHuman = new JMenuItem("CPU vs Human");
        menuRobotHuman.setName("MenuRobotHuman");
        JMenuItem menuRobotRobot = new JMenuItem("CPU vs CPU");
        menuRobotRobot.setName("MenuRobotRobot");
        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.setName("MenuExit");

        // assembly
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(menuGame);
        menuGame.add(menuHumanHuman);
        menuGame.add(menuHumanRobot);
        menuGame.add(menuRobotHuman);
        menuGame.add(menuRobotRobot);
        menuGame.addSeparator();
        menuGame.add(menuExit);

        // listeners
        gameMenuListener(menuHumanHuman);
        gameMenuListener(menuHumanRobot);
        gameMenuListener(menuRobotHuman);
        gameMenuListener(menuRobotRobot);
        menuExit.addActionListener(event -> System.exit(0));
    }

    /**
     * Create the 9 TicTacToe cells on the 3x3 GridLayout.
     * Each has a name and a label following the algebraic chess notation.
     * Add the resulting panel to the main layout manager.
     */
    private void createGrid() {

        JPanel p = new JPanel(new GridLayout(3, 3));
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 40);
        int i = 0;
        for (int r = 3; r >= 1; r--) {
            for (char c = 65; c <= 67; c++) {
                JButton button = new JButton();
                button.setEnabled(false);
                button.setFont(font);
                String name = String.format("Button%c%d", c, r);
                button.setName(name);
                button.setText(Mark.EMPTY.toString());
                button.setFocusPainted(false);
                slots.add(button);
                slotIndexes.put(name, i++);
                p.add(button);
            }
        }
        add(p, BorderLayout.CENTER);  // add to the main layout manager
    }

    /**
     * Create the TicTacToe controls:
     *    start/reset button
     *    p1, p2 buttons
     *    game-status field
     * and add them in relevant created panels.
     * Add resulting panels to the main layout manager.
     */
    private void createControls() {

        JPanel top = new JPanel(new GridLayout(1, 3));
        add(top, BorderLayout.NORTH);
        JPanel bottom = new JPanel(new FlowLayout());
        add(bottom, BorderLayout.SOUTH);

        p1Button = new JButton("Human");
        p1Button.setName("ButtonPlayer1");
        playerTypeListener(p1Button);
        top.add(p1Button);

        startResetButton = new JButton("Start");
        startResetButton.setName("ButtonStartReset");
        startListener(startResetButton);
        top.add(startResetButton);

        p2Button = new JButton("Human");
        p2Button.setName("ButtonPlayer2");
        playerTypeListener(p2Button);
        top.add(p2Button);

        labelStatus = new JLabel("Game is not started");
        labelStatus.setName("LabelStatus");
        bottom.add(labelStatus);
    }

    /**
     * Game-menu-item listener for the 4 player-selection options.
     * Initializes specified players and starts the game.
     * @param menuItem a menu item from the menu-bar's game submenu.
     */
    private void gameMenuListener(JMenuItem menuItem) {

        menuItem.addActionListener(actionEvent -> {
            p1Button.setText(menuItem.getName().startsWith("MenuHuman") ? "Human" : p1Difficulty);
            p2Button.setText(menuItem.getName().endsWith("Human") ? "Human" : p2Difficulty);
            if ("Reset".equals(startResetButton.getText())) {
                startResetButton.doClick();
            }
            startResetButton.doClick();
        });
    }

    /**
     * Button event listener for all 9 TicTacToe slots.
     * After being clicked, set the current player's mark on the slot,
     * check the game-state, and swap players.
     * @param slot a TicTacToe slot.
     */
    private void gameplayListener(JButton slot) {

        slot.addActionListener(event -> {
            boolean available = board.getAvailableSlots().contains(slots.indexOf(slot));
            if (!active || !available) {
                return;
            }

            slot.setText(currentPlayer.getMark().toString());
            board.markCellAt(slotIndexes.get(slot.getName()),
                    currentPlayer.getMark());
            checkGameState();

            // click a button automatically if CpuPlayer and re-trigger this listener
            if (currentPlayer instanceof CpuPlayer) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cpuMove();
            }
        });
    }

    /**
     * Event listener for the start button.
     * After being clicked, start a game according to specified players.
     * @param button intended for the start button.
     */
    private void startListener(JButton button) {

        ActionListener start = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                button.removeActionListener(this);
                button.setText("Reset");
                resetListener(button);
                startGame();
            }
        };
        button.addActionListener(start);
    }

    /**
     * Event listener for the reset button.
     * After being clicked, reset the game from the start.
     * @param button intended for the reset button.
     */
    private void resetListener(JButton button) {

        ActionListener reset = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                button.removeActionListener(this);
                button.setText("Start");
                startListener(startResetButton);
                resetGame();
            }
        };
        button.addActionListener(reset);
    }

    /**
     * Event listener for the players buttons. After being clicked,
     * the text of the button changes. This text will be used by the
     * start/reset button to initialize relevant players.
     * @param button intended for the p1 & p2 buttons.
     */
    private void playerTypeListener(JButton button) {

        button.addActionListener(actionEvent -> {
                button.setText("Human".equals(button.getText()) ? "CPU Easy"
                        : "CPU Easy".equals(button.getText()) ? "CPU Medium"
                        : "CPU Medium".equals(button.getText()) ? "CPU Hard"
                        : "CPU Hard".equals(button.getText()) ? "CPU Impossible" : "Human");
                if (button.getName().endsWith("1")) {
                    p1Difficulty = button.getText();
                } else {
                    p2Difficulty = button.getText();
                }
        });
    }

    /**
     * Swap current player and opponent (use after a turn).
     */
    private void swapPlayers() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        opponent = (opponent == player1) ? player2 : player1;
    }

    /**
     * Check if game has a winner, is a draw, or to continue.
     * If game ends, display an informative message.
     */
    private void checkGameState() {

        if (board.isWinner(currentPlayer)) {
            String name = currentPlayer instanceof CpuPlayer ? "CPU" : "Human";
            labelStatus.setText(String.format("The %s Player (%s) wins", name, currentPlayer.getMark()));
            disableGame();
        } else if (board.isDraw()) {
            labelStatus.setText("Draw");
            disableGame();
        } else {  // game goes on  (set turn to next player)
            swapPlayers();
            String name = currentPlayer instanceof CpuPlayer ? "CPU" : "Human";
            labelStatus.setText(String.format("The turn of %s Player (%s)", name, currentPlayer.getMark()));
        }
    }

    /**
     * Initialize players according to selection,
     * enable the 9 clickable TicTacToe slots
     * and start the game.
     */
    private void startGame() {

        initPlayers();
        for (JButton button : slots) {
            button.setEnabled(true);
            gameplayListener(button);
        }
        String name = currentPlayer instanceof CpuPlayer ? "CPU" : "Human";
        labelStatus.setText(String.format("The turn of %s Player (%s)", name, currentPlayer.getMark()));
        p1Button.setEnabled(false);
        p2Button.setEnabled(false);
        // start the game if the first player is cpu
        active = true;
        if (currentPlayer instanceof CpuPlayer) {
            cpuMove();
        }
    }

    /**
     * Disable the 9 clickable TicTacToe slots.
     */
    private void disableGame() {

        active = false;
        for (JButton b : slots) {
            b.setEnabled(false);
        }
    }

    /**
     * Reset the game with a clear deactivated board.
     */
    private void resetGame() {

        active = false;
        board = new Board();
        for (JButton slot : slots) {
            slot.setText(Mark.EMPTY.toString());
            slot.setEnabled(false);
        }
        labelStatus.setText("Game is not started");
        p1Button.setEnabled(true);
        p2Button.setEnabled(true);
    }

    /**
     * Use this method if currentPlayer is the cpu.
     */
    private void cpuMove() {

        assert currentPlayer instanceof CpuPlayer;
        int place = ((CpuPlayer) currentPlayer).move();
        JButton button = slots.get(place);
        button.doClick();
    }

}
