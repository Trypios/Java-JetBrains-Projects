package converter;

import java.util.Scanner;

/**
 * Convert a number of any base to any other base.
 * Deal with bases from 1 to 36 inclusive.
 */
public class NumeralSystemConverter {

    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Run this method to start the numeral system converter.
     * Request user input (a source base, a number, a target base)
     * and calculate the request.
     */
    public static void run() {

        int sourceRadix, targetRadix;

        try {
            // get input
            System.out.println("Enter source radix: ");
            sourceRadix = Integer.parseInt(SCANNER.nextLine());

            System.out.println("Enter source number: ");
            String inputNumber = SCANNER.nextLine().toUpperCase();

            System.out.println("Enter target radix: ");
            targetRadix = Integer.parseInt(SCANNER.nextLine());

            if (sourceRadix < 1 || sourceRadix > 36 || targetRadix < 1 || targetRadix > 36) {
                System.out.println("Invalid radixes");
            } else {
                calculate(sourceRadix, inputNumber, targetRadix);
            }
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    /**
     * Convert a number of some source base to a number of some target base.
     * Display the result.
     * @param sourceRadix the source base
     * @param sourceNumber a source number to convert
     * @param targetRadix the target base
     */
    private static void calculate(int sourceRadix, String sourceNumber, int targetRadix) {

        try {
            // 1st step: convert from source base to decimal
            double decimal = 0;
            if (sourceRadix == 1) {
                decimal = base1ToDecimal(sourceNumber);
            } else if (sourceRadix == 10) {
                decimal = Double.parseDouble(sourceNumber);
            } else {
                decimal = otherBaseToDecimal(sourceNumber, sourceRadix);
            }

            // 2nd step: convert from decimal to target base
            String result;
            if (targetRadix == 1) {
                result = decimalToBase1(decimal);
            } else if (targetRadix == 10) {
                if (decimal % 1 == 0) {
                    result = Integer.toString((int) decimal);
                } else {
                    result = String.format("%.5f", decimal);  // round to 5 d.p.
                }
            } else {
                result = decimalToOtherBase(decimal, targetRadix);
            }
            System.out.printf("Result: %s (base%d) = %s (base%d)%n", sourceNumber,
                                                                     sourceRadix,
                                                                     result,
                                                                     targetRadix);
        } catch (Exception e) {
            System.out.println("error");
        }
    }


    /**
     * Convert a source base1 (unary) number to a base10 number (decimal).
     * @param unaryNum a unary number
     * @return a decimal number (integer)
     */
    private static int base1ToDecimal(String unaryNum) {

        System.out.println(unaryNum);
        return unaryNum.length();
    }

    /**
     * Convert a base10 (decimal) number to base 1 (unary).
     * @param decimalNum a base 10 number
     * @return a String of ones, eg "111111"
     */
    private static String decimalToBase1(double decimalNum) {

        StringBuilder unaryNum = new StringBuilder();
        for (int i = 0; i < (int) decimalNum; i++) {
            unaryNum.append("1");
        }
        return unaryNum.toString();
    }

    /**
     * Convert a number of a specified base, to a base10 number (decimal).
     * @param num a number of some base.
     * @param sourceRadix the base of the given number.
     * @return a base10 decimal number
     */
    private static double otherBaseToDecimal(String num, int sourceRadix) {

        String integerRegex = "\\w+";
        String decimalRegex = "\\w+\\.\\w+";
        if (!(num.matches(integerRegex) || num.matches(decimalRegex))) {
            throw new NumberFormatException();
        }

        // split number to two parts
        String integerPart;
        String fractionPart;
        if (num.matches(decimalRegex)) {
            String[] parts = num.split("\\.");
            integerPart = parts[0];
            fractionPart = parts[1];
        } else {
            integerPart = num;
            fractionPart = "0";
        }

        // deal with integer part:
        int leftPart;
        if (sourceRadix == 1) {
            leftPart = integerPart.length();
        } else {
            leftPart = Integer.parseInt(integerPart, sourceRadix);
        }

        // deal with fraction part:
        double rightPart = 0d;
        String[] fractionDigits = fractionPart.split("");

        for (int i = 0; i < fractionDigits.length; i++) {
            int currentDigit = fractionDigits[i].charAt(0);
            if (currentDigit >= 65) {
                currentDigit -= 55;
            } else {
                currentDigit -= 48;
            }
            rightPart += (double) currentDigit / Math.pow(sourceRadix, i + 1);
        }

        if (rightPart == 0) {
            return leftPart;
        }
        return leftPart + rightPart;
    }

    /**
     * Convert a source base10 (decimal) number to a number of a specified target base.
     * @param decimalNum a decimal number
     * @param targetRadix the base of the resulting number.
     * @return a number of some base.
     */
    private static String decimalToOtherBase(double decimalNum, int targetRadix) {

        // split to two parts
        String[] parts = Double.toString(decimalNum).split("\\.");

        // deal with integer part:
        String leftPart = Integer.toString(Integer.parseInt(parts[0]), targetRadix);

        // deal with fraction part:
        double fraction = Double.parseDouble("0." + parts[1]);

        StringBuilder rightPart = new StringBuilder(".");
        for (int i = 0; i < 5; i++) {
            int integerPart = (int) (fraction * targetRadix);
            char digit = (char) integerPart;
            if (integerPart > 9) {
                rightPart.append((char) (digit + (int) '7'));
            } else {
                rightPart.append(integerPart);
            }
            fraction = Double.parseDouble("0." + Double.toString(fraction * targetRadix).split("\\.")[1]);
        }

        if (fraction == 0) {
            return leftPart;
        }

        // pad with zeros
        for (int i = 0; i < 6 - rightPart.length(); i++) {
            rightPart.append("0");
        }

        return leftPart + rightPart.toString();
    }
}
