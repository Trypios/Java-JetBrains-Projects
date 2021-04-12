package banking;

import java.util.ArrayList;
import java.util.Random;

public class CreditCard {

    private static final Random rand = new Random();
    private static final String IIN = "400000";  // bank's credit card identifier
    private final String number;
    private final String pin;
    private int balance;

    /**
     * Constructor for creating a new credit card
     * and saving in database
     */
    public CreditCard() {

        this.number = generateCardNumber();
        this.pin = generatePin();
        this.balance = 0;
        ControlDB.insertNewCard(this);
        System.out.println("Your card has been created");
        System.out.printf("Your card number: %s%n", number);
        System.out.printf("Your card PIN: %s%n", pin);
    }

    /**
     * Constructor for fetched credit card from database.
     * @param cardNumber credit card's 16digit number
     * @param pin credit card's pin number
     * @param balance credit card's balance (in USD)
     */
    public CreditCard(String cardNumber, String pin, int balance) {

        this.number = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    /**
     * Getter.
     * @return credit card's number (16 digits).
     */
    public String getNumber() {
        return number;
    }

    /**
     * Getter.
     * @return credit card's pin (4 digits).
     */
    public String getPin() {
        return pin;
    }

    /**
     * Getter.
     * @return credit card's balance (in USD).
     */
    public int getBalance() {

        return balance;
    }

    /**
     * Deposit money to the credit card.
     * @param amount money of money (in USD).
     */
    public void deposit(int amount) {

        if (amount > 0) {
            this.balance += amount;
        }
    }

    /**
     * Setter.
     * Update credit card's balance on field and in database.
     * @param balance amount to update (in USD).
     */
    public void setBalance(int balance) {

        this.balance = balance;
        ControlDB.updateBalance(this);
    }

    /**
     * Check if a card number is set according to Luhn's algorithm.
     * @param cardNumber a 16 digit number
     * @return true if card number is valid, else false.
     */
    public static boolean isValid(String cardNumber) {

        String lastDigit = cardNumber.substring(cardNumber.length() - 1);
        StringBuilder card15digit = new StringBuilder(cardNumber);
        card15digit.deleteCharAt(card15digit.length() - 1);
        String checksum = generateChecksum(card15digit.toString());

        return checksum.equals(lastDigit);
    }

    /**
     * Generate a random 4-digit number.
     * @return a string of 4 digits.
     */
    private String generatePin() {

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

    /**
     * Generate a random 9-digit unique number based on the sql-generated
     * unique customer primary key, to be used both as account identifier
     * and as part of the 16-digit credit card number.
     * @return a string of 9 digits.
     */
    private static String generateAccountNo() {

        // read all account numbers from sql
        ArrayList<String> allAccounts = ControlDB.getAllAccounts();
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

    /**
     * Apply Luhn's algorithm on 15 digits, to calculate the checksum digit (16th digit).
     * @param card15digit a credit card number's first 15 digits
     * @return a string of a single digit.
     */
    private static String generateChecksum(String card15digit) {

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

    /**
     * Generate a unique and valid 16-digit credit card number.
     * @return a string of 16-digits.
     */
    private static String generateCardNumber() {

        String accountNo = generateAccountNo();
        String card15digit = IIN + accountNo;
        return card15digit + generateChecksum(card15digit);
    }

}
