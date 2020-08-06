import java.util.Scanner;

public class NumeralSystemConverter {

    public static int sourceRadix;
    public static int targetRadix;

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        boolean allGood = true;
        
        try {
            // get input
            sourceRadix = Integer.parseInt(scanner.nextLine());
            String inputNumber = scanner.nextLine();
            inputNumber = inputNumber.toUpperCase();
            targetRadix = Integer.parseInt(scanner.nextLine());
            
            if (sourceRadix < 1 || sourceRadix > 36 || targetRadix < 1 || targetRadix > 36) {
                allGood = false;
            }
            
            if (allGood) {
                // 1st step: convert from source base to decimal
                double result = 0;
                if (sourceRadix == 1) {
                    result = base1ToDecimal(inputNumber);
                } else if (sourceRadix == 10) {
                    result = Double.parseDouble(inputNumber);
                } else {
                    try {
                        String[] parts = inputNumber.split("\\.");
                        result = otherBaseToDecimal(parts[0], parts[1]);
                    } catch (Exception e) {
                        result = otherBaseToDecimal(inputNumber, "0");
                    }
                }
                
                //System.out.printf("Decimal: %.5f %n", result);

                // 2nd step: convert from decimal to target base
                if (targetRadix == 1) {
                    System.out.println(decimalToBase1(result));
                } else if (targetRadix == 10) {
                    System.out.printf("%.5f %n", result);
                } else {
                    System.out.printf("%s %n", decimalToOtherBase(result));
                }
            }
        } catch (Exception e) {
            allGood = false;
        } finally {
            scanner.close();
            if (!allGood) {
                System.out.println("error");
            }
        }
    }


    public static int base1ToDecimal(String number) {
        return number.length();
    }


    public static String decimalToBase1(double number) {
        String output = "";
        for (int i = 0; i < (int) number; i++) {
            output += "1";
        }
        return output;
    }

    
    public static double otherBaseToDecimal(String integerPart, String fractionPart) {
        // deal with integer part:
        int leftPart = 0;
        if (sourceRadix == 1) {
            leftPart = integerPart.length();
        } else {
            leftPart = Integer.parseInt(integerPart, sourceRadix);
        }

        // deal with fraction part:
        double rightPart = 0d;
        String[] fractionDigits = fractionPart.split("");

        for (int i = 0; i < fractionDigits.length; i++) {
            int currentDigit = (int) fractionDigits[i].charAt(0);
            if (currentDigit >= 65) {
                currentDigit -= 55;
            } else {
                currentDigit -= 48;
            }
            rightPart += (double) currentDigit / Math.pow(sourceRadix, i + 1);
        }

        return leftPart + rightPart;
    }


    public static String decimalToOtherBase(double num) {
        // split to two parts
        String[] parts = Double.toString(num).split("\\.");

        // deal with integer part:
        String leftPart = Integer.toString(Integer.parseInt(parts[0]), targetRadix);

        // deal with fraction part:
        double fraction = Double.parseDouble("0." + parts[1]);
        String rightPart = ".";
        for (int i = 0; i < 5; i++) {
            int integerPart = (int) (fraction * targetRadix);
            char digit = (char) integerPart;
            if (integerPart > 9) {
                rightPart += (char) (digit + (int) '7');
            } else {
                rightPart += Integer.toString(integerPart);
            }
            fraction = Double.parseDouble("0." + Double.toString(fraction * targetRadix).split("\\.")[1]);
        }

        // pad with zeros
        for (int i = 0; i < 6 - rightPart.length(); i++) {
            rightPart += "0";
        }

        return leftPart + rightPart.toLowerCase();
    }
}
