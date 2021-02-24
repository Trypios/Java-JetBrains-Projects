package battleship;

/**
 * Models the classic board game BattleShip
 */
public class BattleshipGame {

    private static Player p1;
    private static Player p2;
    private static Player currentPlayer;

    /**
     * Main method. Initiates a game.
     */
    public static void runGame() {
        // game setup
        boolean gameover = false;
        p1 = initPlayer(Player.P1);
        currentPlayer = p1;
        passTurn();
        p2 = initPlayer(Player.P2);

        // game on
        while (!gameover) {
            passTurn();
            displayBoth();
            System.out.printf("\n%s, it's your turn:\n", currentPlayer.getName());
            playTurn();
            swapPlayers();
            gameover = currentPlayer.battlefield().hasLost();
        }
        currentPlayer.battlefield().display();
    }

    /**
     * Initialize a player and their ships and battlefield
     * @param p The player
     * @return The initialized player
     */
    private static Player initPlayer(Player p) {
        System.out.printf("%s, place your ships on the game field%n%n", p.getName());
        p.battlefield().display();
        p.battlefield().positionShips();
        return p;
    }

    /**
     * Assigns the next turn's player
     */
    private static void swapPlayers() {
        currentPlayer = (currentPlayer == p1) ? p2 : p1;
    }

    /**
     * @return The current player's opponent
     */
    private static Player opponent() {
        return (currentPlayer == p1) ? p2 : p1;
    }

    /**
     * Play one of the game's turns.
     * Current player fires at a cell, cell is checked and board is updated.
     * Informs the players of the aftermath.
     */
    private static void playTurn() {
        Player victim = opponent();
        String msg = victim.battlefield().attacked();
        System.out.println(msg);
    }

    /**
     * Awaits user to press enter (before next game phase)
     */
    private static void passTurn() {
        InputHandler.inputEnter();
    }

    /**
     * Displays both boards,
     * the current player's (lower) and their opponent's (upper).
     */
    private static void displayBoth() {
        opponent().battlefield().display(false);
        System.out.println("---------------------");
        currentPlayer.battlefield().display();
    }
}

/**
 * Predefined players for BattleshipGame.
 * Each player has its own Battlefield.
 */
enum Player {

    P1(new Battlefield(10, 10), "Player 1"),
    P2(new Battlefield(10, 10), "Player 2");

    private final Battlefield field;
    private final String name;

    Player(Battlefield field, String name) {
        this.field = field;
        this.name = name;
    }

    public Battlefield battlefield() {
        return this.field;
    }

    public String getName() {
        return this.name;
    }
}
