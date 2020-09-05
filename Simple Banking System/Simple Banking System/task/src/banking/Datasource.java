package banking;

        import java.io.File;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.ArrayList;

public class Datasource {

    private static String CONNECTION_URL;

    private static final String CARD_TABLE = "card";
    private static final String CARD_COLUMN_ID = "id";
    private static final String CARD_COLUMN_NUMBER = "number";
    private static final String CARD_COLUMN_PIN = "pin";
    private static final String CARD_COLUMN_BALANCE = "balance";

    public static boolean initDatabase(File dbFile) {
        /*
         * Initializes database file and tables
         * */

        boolean valid = (dbFile != null);
        if (valid) {
            CONNECTION_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            createTable();
        }
        return valid;
    }

    public static boolean hasCards() {
        /*
         * Returns true if database has at least one entry
         * else false (if empty)
         * */

        String command = "SELECT * FROM " + CARD_TABLE;

        try (Connection connection = Datasource.connect()) {
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

    public static CreditCard validateCredentials(String cardNumber, String pin) {
        /*
         * Returns the credit card associated with said credentials
         * if correct, otherwise null
         * */

        String command = "SELECT * FROM " + CARD_TABLE + "\n" +
                "WHERE " + String.format("%s='%s' AND %s='%s'",
                CARD_COLUMN_NUMBER, cardNumber,
                CARD_COLUMN_PIN, pin);

        try (Connection connection = Datasource.connect()) {
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

    public static CreditCard getCardByNumber(String cardNumber) {
        /*
         * Returns the credit card associated with said card number
         * Returns null if card not exists
         * */

        String command = "SELECT * FROM " + CARD_TABLE + "\n" +
                "WHERE " + String.format("%s='%s'",
                CARD_COLUMN_NUMBER, cardNumber);

        try (Connection connection = Datasource.connect()) {
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

    private static Connection connect() {
        /*
         * Establishes a connection to the database file
         * and returns the Connection object that represents it
         * */

        try {
            return DriverManager.getConnection(CONNECTION_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void createTable() {
        /*
         * Creates card table in the database (if not already exist)
         * */

        String command_card = "CREATE TABLE IF NOT EXISTS " + CARD_TABLE + "(\n" +
                CARD_COLUMN_ID + " INTEGER PRIMARY KEY, \n" +
                CARD_COLUMN_NUMBER + " TEXT, \n" +
                CARD_COLUMN_PIN + " TEXT, \n" +
                CARD_COLUMN_BALANCE + " INTEGER DEFAULT 0)";

        try (Connection connection = Datasource.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.execute(command_card);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot create new table in database:  '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    public static void insertNewCard(String number, String pin, int balance) {
        /*
         * Inserts new card into database
         * */

        String command = "INSERT INTO " + CARD_TABLE + "\n" +
                "(" + CARD_COLUMN_NUMBER + ", \n" +
                CARD_COLUMN_PIN + ", \n" +
                CARD_COLUMN_BALANCE + ") VALUES\n" +
                "(" + number + ", " + pin + ", " + balance + ")";

        try (Connection connection = Datasource.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.execute(command);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot create new card in database: '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    public static void updateBalance(String number, int newBalance) {
        /*
         * Updates balance of the specified credit card
         * */

        String command = "UPDATE " + CARD_TABLE + "\n" +
                "SET " + String.format("%s=%d%n", CARD_COLUMN_BALANCE, newBalance) +
                "WHERE " + String.format("%s='%s'", CARD_COLUMN_NUMBER, number);

        try (Connection connection = Datasource.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(command);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot update database: '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    public static void removeCard(String number) {
        /* Removes specified card (by number)
         * from database
         * */

        String command = "DELETE FROM " + CARD_TABLE + "\n" +
                "WHERE " + String.format("%s='%s'", CARD_COLUMN_NUMBER, number);

        try (Connection connection = Datasource.connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(command);
            }
        } catch (SQLException e) {
            System.out.printf("Cannot delete card from database: '%s'%n", CONNECTION_URL);
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<String> getAllAccounts() {
        /*
         * Returns all accounts stored in the database
         * The accounts are 9-digit long - part of the card number
         * indeces 6-14 (inclusive)
         * */

        String command = "SELECT " + CARD_COLUMN_NUMBER + " FROM " + CARD_TABLE;

        try (Connection connection = Datasource.connect()) {
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