package banking;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ControlDB {

    private static String CONNECTION_URL;
    private static final String CARD_TABLE = "card";
    private static final String CARD_COLUMN_ID = "id";
    private static final String CARD_COLUMN_NUMBER = "number";
    private static final String CARD_COLUMN_PIN = "pin";
    private static final String CARD_COLUMN_BALANCE = "balance";

    /**
     * Initialize the database based on the specified database file (must already exists)
     * and create a single table for banking accounts, if not exists.
     * @param dbFile a database file
     * @return true if database file exists, else false.
     */
    public static boolean initDatabase(File dbFile) {

        boolean valid = (dbFile != null);
        if (valid) {
            CONNECTION_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            createTable();
        }
        return valid;
    }

    /**
     * Check if DB's table contains credit card entries.
     * @return true if DB has at least one entry, false if empty.
     */
    public static boolean hasCards() {

        String command = "SELECT * FROM " + CARD_TABLE;

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                ResultSet result = statement.executeQuery(command);
                return result.next();
            }
        } catch (SQLException e) {
            System.out.printf("Cannot connect to database '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Validate given account credentials.
     * @param cardNumber a 16-digit credit card number.
     * @param pin a 4-digit credit card pin.
     * @return a credit card (object) if matches credentials, else null.
     */
    public static CreditCard validateCredentials(String cardNumber, String pin) {

        String command = "SELECT * FROM " + CARD_TABLE + "\n" +
                "WHERE " + String.format("%s='%s' AND %s='%s'",
                CARD_COLUMN_NUMBER, cardNumber,
                CARD_COLUMN_PIN, pin);

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {

                ResultSet result = statement.executeQuery(command);
                if (result.next()) {
                    return new CreditCard(result.getString(CARD_COLUMN_NUMBER),
                            result.getString(CARD_COLUMN_PIN),
                            result.getInt(CARD_COLUMN_BALANCE));
                }
            }
        } catch (SQLException e) {
            System.out.printf("Cannot connect to database '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Find a credit card in the DB by its number.
     * @param cardNumber a 16-digit credit card number.
     * @return a credit card (object), or null if not found.
     */
    public static CreditCard findCardByNumber(String cardNumber) {

        String command = "SELECT * FROM " + CARD_TABLE + "\n" +
                "WHERE " + String.format("%s='%s'",
                CARD_COLUMN_NUMBER, cardNumber);

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {

                ResultSet result = statement.executeQuery(command);
                if (result.next()) {
                    return new CreditCard(result.getString(CARD_COLUMN_NUMBER),
                            result.getString(CARD_COLUMN_PIN),
                            result.getInt(CARD_COLUMN_BALANCE));
                }
            }
        } catch (SQLException e) {
            System.out.printf("Cannot connect to database '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Establish a connection to the database.
     * @return the Connection object that represents an SQL database connection.
     */
    private static Connection connect() {

        try {
            return DriverManager.getConnection(CONNECTION_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Create a table of credit cards in the database (if table not already exist).
     */
    private static void createTable() {

        String command_card = "CREATE TABLE IF NOT EXISTS " + CARD_TABLE + "(\n" +
                CARD_COLUMN_ID + " INTEGER PRIMARY KEY, \n" +
                CARD_COLUMN_NUMBER + " TEXT, \n" +
                CARD_COLUMN_PIN + " TEXT, \n" +
                CARD_COLUMN_BALANCE + " INTEGER DEFAULT 0)";

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.execute(command_card);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot create new table in database:  '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert new card into the database.
     * @param card a credit card
     */
    public static void insertNewCard(CreditCard card) {

        String number = card.getNumber();
        String pin = card.getPin();
        int balance = card.getBalance();
        String command = "INSERT INTO " + CARD_TABLE + "\n" +
                "(" + CARD_COLUMN_NUMBER + ", \n" +
                CARD_COLUMN_PIN + ", \n" +
                CARD_COLUMN_BALANCE + ") VALUES\n" +
                "(" + number + ", " + pin + ", " + balance + ")";

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.execute(command);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot create new card in database: '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update the balance of the specified credit card.
     * @param card a credit card
     */
    public static void updateBalance(CreditCard card) {

        String command = "UPDATE " + CARD_TABLE + "\n" +
                "SET " + String.format("%s=%d%n", CARD_COLUMN_BALANCE, card.getBalance()) +
                "WHERE " + String.format("%s='%s'", CARD_COLUMN_NUMBER, card.getNumber());

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(command);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot update database: '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Remove specified card from the database.
     * @param card a credit card
     */
    public static void removeCard(CreditCard card) {

        String command = "DELETE FROM " + CARD_TABLE + "\n" +
                "WHERE " + String.format("%s='%s'", CARD_COLUMN_NUMBER, card.getNumber());

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(command);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot delete card from database: '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    /**
     * Fetch all accounts stored in the database. Each account number is 9-digit,
     * which is part of its linked credit card's 16-digit number (indexes 6-14 inclusive).
     * @return a string array of all accounts in the database.
     */
    public static ArrayList<String> getAllAccounts() {

        String command = "SELECT " + CARD_COLUMN_NUMBER + " FROM " + CARD_TABLE;

        try (Connection connection = ControlDB.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(command)) {

                ArrayList<String> accountList = new ArrayList<>();
                while (rs.next()) {
                    accountList.add(rs.getString(CARD_COLUMN_NUMBER).substring(6, 15));
                }
                return accountList;
            }
        } catch (SQLException e) {
            System.out.printf("Cannot connect to database '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
        return null;
    }

}