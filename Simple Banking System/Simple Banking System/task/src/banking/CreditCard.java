package banking;

import java.util.ArrayList;
import java.util.Random;

public class CreditCard {

    private static final Random rand = new Random();
    private static final String IIN = "400000";
    private final String cardNumber;
    private final String pin;
    private int balance;

    public CreditCard() {
        /*
         * Constructor for creating a new credit card
         * and saving in database
         * */

        this.cardNumber = generateCardNumber();
        this.pin = generatePin();
        this.balance = 0;
        Datasource.insertNewCard(cardNumber, pin, balance);

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
    }

    public CreditCard(String cardNumber, String pin, int balance) {
        /*
         * Constructor for fetching saved credit card
         * from database
         * */

        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void setBalance(int balance) {
        /*
         * Updates the credit card's balance
         * on field and in database
         * */

        this.balance = balance;
        Datasource.updateBalance(cardNumber, balance);
    }

    public void delete() {
        Datasource.removeCard(cardNumber);
    }

    public static boolean isValid(String cardNumber) {
        /*
         * Checks if card number is set according to Luhn's algorithm
         * */

        String lastDigit = cardNumber.substring(cardNumber.length() - 1);
        StringBuilder card15digit = new StringBuilder(cardNumber);
        card15digit.deleteCharAt(card15digit.length() - 1);
        String checksum = generateChecksum(card15digit.toString());

        return checksum.equals(lastDigit);
    }

    private String generatePin() {
        /*
         * returns a random 4-digit number
         * as string
         * */

        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                pin.append(rand.nextInt(9) + 1);
            } else {
                pin.append(rand.nextInt(10));
            }
        }

        return pin.toString();
    }

    private static String generateAccountNo() {
        /*
         * Returns a random 9-digit number
         * based on the sql-generated unique customer primary key
         * as string
         * */

        // read all account numbers from sql
        ArrayList<String> allAccounts = Datasource.getAllAccounts();
        StringBuilder accNumber;
        while (true) {
            accNumber = new StringBuilder();
            for (int i = 0; i < 9; i++) {
                if (i == 0) {
                    accNumber.append(rand.nextInt(9) + 1);
                } else {
                    accNumber.append(rand.nextInt(10));
                }
            }

            if (allAccounts == null) {
                return accNumber.toString();
            }
            // return a unique account number
            if (!allAccounts.contains(accNumber.toString())) {
                return accNumber.toString();
            }
        }
    }

    private static String generateChecksum(String card15digit) {
        /*
         * applies Luhn's algorithm on the 15 digits and
         * returns the correct checksum digit for the credit card
         * as string
         * */

        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int currentNum = card15digit.charAt(i) - '0';  // '0' = 48 = unicode diff
            // multiply odd digits by 2 (even digits in Java's zero-based indexing)
            if (i % 2 == 0) {
                currentNum *= 2;
            }
            // subtract 9 to numbers over 9
            if (currentNum > 9) {
                currentNum -= 9;
            }
            // add all numbers
            sum += currentNum;
        }

        return Integer.toString(10 - sum % 10);
    }

    private String generateCardNumber() {
        /*
         * Returns a unique and valid card number
         * as string
         * */

        String accountNo = generateAccountNo();
        String card15digit = IIN + accountNo;
        return card15digit + CreditCard.generateChecksum(card15digit);
    }

}
