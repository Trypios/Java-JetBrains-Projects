package banking;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class BankingSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static boolean running = false;

    /**
     * Run this method to operate the banking system.
     * @param args argument(s) from terminal.
     */
    public static void run(String[] args) {

        File dbFile = parseDBfromTerminal(args);
        running = ControlDB.initDatabase(dbFile);
        while (running) {
            loginMenu();
        }
    }

    /**
     * Parse arguments from terminal for a filepath
     * and create a DB file, if not already exists.
     *
     * @param args terminal arguments
     * @return a File object referencing the database
     */
    private static File parseDBfromTerminal(String[] args) {

        File db;

        for (int i = 0; i < args.length; i += 2) {
            if ("-fileName".equals(args[i])) {
                String filepath = args[i + 1];
                db = establishDB(filepath);
                return db;
            }
        }

        System.out.println("A database filename must be provided from terminal to run this program,");
        System.out.println("for example:");
        System.out.println("java banking.Main -fileName db.s3db");
        System.out.println("With no DB provided, a default file is selected.");
        return new File("db.s3db");
    }

    /**
     * Create a DB file if not already exists, by the specified filepath.
     * @param filepath path pointing to a database.
     * @return a File object referring to a database, or null if something goes wrong.
     */
    private static File establishDB(String filepath) {

        File dbFile = new File(filepath);

        // creates new DB if does not exist already
        if (dbFile.exists()) {
            return dbFile;
        } else {
            try {
                if (dbFile.createNewFile()) {
                    return dbFile;
                }
            } catch (IOException e) {
                System.out.println("Error creating file");
            }
        }
        return null;
    }

    /**
     * The banking system's login menu.
     */
    private static void loginMenu() {

        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");

        // get input from user, repeat process if invalid
        boolean inputReceived = false;
        while (!inputReceived) {
            int input;
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input (main menu)");
                continue;
            }

            // with a valid user input:
            switch (input) {
                case 1:
                    new CreditCard();
                    break;
                case 2:
                    customerMenu(logIn());
                    break;
                case 0:
                    exit();
                    break;
                default:
                    System.out.println("Invalid input (main menu)");
                    continue;
            }
            inputReceived = true;
        }
    }

    /**
     * Request credentials from the user as
     * to validate their account and log in to the system.
     * @return the customer's credit card (object) if credentials are valid, else null.
     */
    private static CreditCard logIn() {

        if (!ControlDB.hasCards()) {
            System.out.println("There are currently no active accounts");
            return null;
        }

        System.out.println("Enter your card number:");
        String inputCardNo = scanner.nextLine();

        if (inputCardNo.length() != 16 || !inputCardNo.matches("\\d{16}")) {
            System.out.println("Invalid input");
            return null;
        }

        System.out.println("Enter your PIN:");
        String inputPin = scanner.nextLine();

        // check database for a match of card + pin credentials
        CreditCard card = ControlDB.validateCredentials(inputCardNo, inputPin);
        if (card != null) {
            return card;
        }

        System.out.println("Wrong card number or PIN!");
        return null;
    }

    /**
     * Banking system's main menu (post-login).
     * Only existing customers can view this menu.
     * @param card a credit card used for menu access.
     */
    private static void customerMenu(CreditCard card) {

        if (card == null) {
            return;
        }

        System.out.println("You have successfully logged in!");

        while (true) {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");

            boolean inputReceived = false;
            while (!inputReceived) {
                // get valid user input
                int input;
                try {
                    input = scanner.nextInt();
                    scanner.nextLine();
                } catch (Exception e) {
                    System.out.println("Invalid input (customer menu)");
                    continue;
                }

                // process user input
                switch (input) {
                    case 1:
                        System.out.println("Balance: " + card.getBalance());
                        break;
                    case 2:
                        addIncome(card);
                        break;
                    case 3:
                        transferFunds(card);
                        break;
                    case 4:
                        closeAccount(card);
                        return;
                    case 5:
                        System.out.println("You have successfully logged out!");
                        return;
                    case 0:
                        exit();
                        return;
                    default:
                        System.out.println("Invalid input (customer menu)");
                        continue;
                }
                inputReceived = true;
            }
        }
    }

    /**
     * Add income to the account associated with the specified credit card.
     * @param card a credit card.
     */
    private static void addIncome(CreditCard card) {

        System.out.println("Enter income:");
        while (true) {
            try {
                int amount = Integer.parseInt(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("You are trying to add negative funds");
                    continue;
                }
                card.deposit(amount);
                System.out.println("Income was added!");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    /**
     * Transfer funds from the account of the specified card, to another account.
     * Request user input to find the other account.
     * @param sourceCard a credit card.
     */
    private static void transferFunds(CreditCard sourceCard) {

        // get input of target credit card number
        String inputCardNo;
        CreditCard targetCard;
        System.out.println("Enter card number:");
        while (true) {
            try {
                inputCardNo = scanner.nextLine();
                targetCard = ControlDB.findCardByNumber(inputCardNo);
                if (inputCardNo.length() != 16 || !inputCardNo.matches("\\d{16}")) {
                    System.out.println("Invalid input");
                    continue;
                } else if (!CreditCard.isValid(inputCardNo)) {
                    System.out.println("Probably you made mistake in the card number. Please try again!");
                    return;
                } else if (targetCard == null) {
                    System.out.println("Such a card does not exist.");
                    return;
                } else if (sourceCard.getNumber().equals(targetCard.getNumber())) {
                    System.out.println("You can't transfer money to the same account!");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }

        // if passes all checks, get amount to transfer
        int transferAmount;
        while (true) {
            try {
                System.out.println("Enter how much money you want to transfer:");
                transferAmount = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }

        int currentAmount = sourceCard.getBalance();
        if (transferAmount > currentAmount) {
            System.out.println("Not enough money!");
            return;
        } else if (transferAmount <= 0) {
            System.out.println("You are trying to transfer non existing funds");
            return;
        }

        // finally do the transfer
        sourceCard.setBalance(currentAmount - transferAmount);
        targetCard.setBalance(targetCard.getBalance() + transferAmount);
        System.out.println("Success!");
    }

    /**
     * Remove a card from the database, effectively closing its account.
     * @param card a credit card linked to the account being close.
     */
    private static void closeAccount(CreditCard card) {

        ControlDB.removeCard(card);
        System.out.println("The account has been closed!");
    }

    /**
     * Exit banking system.
     */
    private static void exit() {

        running = false;
        System.out.println("Thank you for using a Cypriot bank, the go-to place for Russian money!");
    }

}
