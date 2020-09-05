package banking;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static boolean running = false;


    private static File parseDBfromTerminal(String[] args) {
        /*
         * Parse arguments from terminal for database filepath
         * Creates the file (if non already exists) and returns it
         * */

        for (int i = 0; i < args.length; i += 2) {

            if ("-fileName".equals(args[i])) {
                String filePath = args[i + 1];
                File dbFile = new File(filePath);

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
            }
        }

        System.out.println("No DB provided");
        return null;
    }

    private static void mainMenu() {
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

    private static CreditCard logIn() {
        /*
         * Returns the credit card instance of the customer trying to log-in
         * (if s/he inputs correct credentials, else returns null)
         * */

        if (!Datasource.hasCards()) {
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
        CreditCard card = Datasource.validateCredentials(inputCardNo, inputPin);
        if (card != null) {
            return card;
        }

        System.out.println("Wrong card number or PIN!");
        return null;
    }

    private static void customerMenu(CreditCard card) {
        /*
         * Post-login menu
         * */

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
                        doTransfer(card);
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

    private static void doTransfer(CreditCard sourceCard) {

        // get input of target credit card number
        String inputCardNo;
        CreditCard targetCard;
        System.out.println("Enter card number:");
        while (true) {
            try {
                inputCardNo = scanner.nextLine();
                targetCard = Datasource.getCardByNumber(inputCardNo);
                if (inputCardNo.length() != 16 || !inputCardNo.matches("\\d{16}")) {
                    System.out.println("Invalid input");
                    continue;
                } else if (!CreditCard.isValid(inputCardNo)) {
                    System.out.println("Probably you made mistake in the card number. Please try again!");
                    return;
                } else if (targetCard == null) {
                    System.out.println("Such a card does not exist.");
                    return;
                } else if (sourceCard.getCardNumber().equals(targetCard.getCardNumber())) {
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

    private static void closeAccount(CreditCard card) {
        card.delete();
        System.out.println("The account has been closed!");
    }

    private static void exit() {
        System.out.println("Bye!");
        running = false;
    }

    public static void main(String[] args) {

        File dbFile = parseDBfromTerminal(args);
        running = Datasource.initDatabase(dbFile);

        while (running) {
            mainMenu();
        }
    }
}
