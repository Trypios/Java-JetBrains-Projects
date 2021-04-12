package calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Calculator {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final HashMap<String, String> variables = new HashMap<>();
    private static final HashMap<String, String> precedence = new HashMap<>();

    static {
        precedence.put("+", "1");
        precedence.put("-", "1");
        precedence.put("*", "2");
        precedence.put("/", "2");
    }

    /**
     * Run this method to start the calculator.
     */
    public static void run() {

        boolean active = true;
        while (active) {
            active = menu();
        }
    }

    /**
     * Perform a calculator operation.
     * Calculate a mathematical expresson, assign a variable or display info.
     * @return false if user wants to exit, else true.
     */
    private static boolean menu() {

        System.out.println("\nType:");
        System.out.println("\t/help\n\t/exit\n\ta mathematical expression\n");
        String input = SCANNER.nextLine();

        if (input.contains("=")) {
            parseVariables(input);
        } else if (input.startsWith("/")) {
            if ("/exit".equals(input)) {
                System.out.println("Thank you for literally counting on us!");
                return false;
            } else if ("/help".equals(input)) {
                displayHelp();
            } else {
                System.out.println("Unknown command");
            }
        } else {
            calculate(input);
        }
        return true;
    }

    /**
     * Calculate a mathematical expression and display the result.
     * @param expression A mathematical expression.
     */
    private static void calculate(String expression) {

        List<String> infix = parseToInfix(expression);

        if (infix != null && testBrackets(expression)) {
            List<String> postfix = infixToPostfix(infix);

            if (postfix != null) {
                BigDecimal result = calculatePostfix(postfix);
                if (result != null) {
                    System.out.printf("%s%n", result.toBigInteger().toString());
                }
            } else {
                System.out.println("Invalid expression");
            }
        } else {
            System.out.println("Invalid expression");
        }
    }

    /**
     * Parse expression for variable assignment and store it in Variables map.
     * @param expression an expression in the format: variable = number
     */
    private static void parseVariables(String expression) {

        String[] parts = expression.split("\\s*=\\s*");
        if (parts.length == 2) {

            String variable = parts[0].trim();
            String number = parts[1].trim();

            if (!variable.matches("[A-Za-z]+")) {
                System.out.println("Invalid identifier");
                return;
            }

            if (!number.matches("[-+]?\\d+")) {
                if (variables.containsKey(number)) {
                    variables.put(variable, variables.get(number));
                    return;
                }
                System.out.println("Invalid assignment");
                return;
            }
            // else
            variables.put(variable, number);
        } else {
            System.out.println("Invalid assignment");
        }

    }

    /**
     * Convert a conventional mathematical expression to infix notation.
     * @param expression a mathematical expression.
     * @return a List of infix notation.
     */
    private static List<String> parseToInfix(String expression) {

        ArrayDeque<String> stack = new ArrayDeque<>();

        if ("+ - * / ".contains(expression.substring(0, 2)) ||
                expression.substring(0, 1).matches("[)*/]")) {
            return null;
        }

        if (expression.endsWith("-")) {
            return null;
        }

        for (String character : expression.split("")) {
            if (stack.isEmpty()) {
                stack.offerLast(character);
            } else {

                // in case of digit
                if (character.matches("\\d")) {
                    if (stack.peekLast().matches("\\d+")) {
                        String number = stack.pollLast() + character;
                        stack.offerLast(number);
                    } else if (stack.peekLast().matches("[A-Za-z]+")) {
                        return null;
                    } else {
                        stack.offerLast(character);
                    }

                    // in case of variable
                } else if (character.matches("[A-Za-z]")) {
                    if (stack.peekLast().matches("[A-Za-z]+")) {
                        String variable = stack.pollLast() + character;
                        stack.offerLast(variable);
                    } else if (character.matches("\\d+")) {
                        return null;
                    } else {
                        stack.offerLast(character);
                    }

                    // in case of space
                } else if (character.equals(" ") && !stack.peekLast().equals(" ")) {
                    stack.offerLast(character);

                    // in case of parentheses
                } else if (character.equals("(")) {
                    if (stack.peekLast().matches("\\w+")) {
                        return null;
                    } else {
                        stack.offerLast(character);
                    }
                } else if (character.equals(")")) {
                    if (stack.peekLast().matches("[-+(*/]")) {
                        return null;
                    } else {
                        stack.offerLast(character);
                    }

                    // in case of multiple - or +
                } else if (character.equals("-")) {
                    if (stack.peekLast().equals("-")) {
                        stack.pollLast();
                        stack.offerLast("+");
                    } else if (stack.peekLast().equals("+")) {
                        stack.pollLast();
                        stack.offerLast(character);
                    } else {
                        stack.offerLast(character);
                    }
                } else if (character.equals("+")) {
                    if (!stack.peekLast().equals("+")) {
                        stack.offerLast(character);
                    }

                    // in case of multiple * or /
                } else if (character.matches("[*/]")) {
                    if (stack.peekLast().matches("[*/(]")) {
                        return null;
                    }
                    stack.offerLast(character);
                }
            }
        }

        stack.removeIf(item -> item.matches("\\s"));

        return new ArrayList<>(stack);
    }

    /**
     * Validate the brackets of a mathematical expression for appropriate opening-closing.
     * @param expression A mathematical expression.
     * @return true for valid brackets, else false.
     */
    private static boolean testBrackets(String expression) {

        ArrayDeque<String> stack = new ArrayDeque<>();

        for (String letter : expression.split("")) {
            if (letter.equals("(")) {
                stack.offerLast(letter);
            } else if (letter.equals(")")) {
                if (stack.pollLast() == null) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    /**
     * Convert a List from infix to postfix notation.
     * @param infix a List of an infix'd mathematical expression.
     * @return a List of postfix notation.
     */
    private static List<String> infixToPostfix(List<String> infix) {

        ArrayDeque<String> stack = new ArrayDeque<>();
        ArrayDeque<String> postfix = new ArrayDeque<>();

        for (String item : infix) {

            // numbers and variables
            if (item.matches("-?\\d+")) {
                postfix.offerLast(item);
            } else if (item.matches("[A-Za-z]+")) {
                if (variables.containsKey(item)) {
                    postfix.offerLast(variables.get(item));
                } else {
                    System.out.println("Unknown variable");
                    return null;
                }

                // operators
            } else if (item.matches("[-+*/]")) {
                if (stack.isEmpty()) {
                    stack.offerLast(item);
                } else if (stack.peekLast().equals("(")) {
                    stack.offerLast(item);
                } else {
                    String stackLast = stack.peekLast();
                    boolean higherPrecedence = precedence.get(item).compareTo(precedence.get(stackLast)) > 0;
                    if (higherPrecedence) {
                        stack.offerLast(item);
                    } else {
                        while (!stack.isEmpty() &&
                                !higherPrecedence &&
                                !stack.peekLast().equals("(")) {
                            postfix.offerLast(stack.pollLast());
                            higherPrecedence = precedence.get(item).compareTo(precedence.get(stackLast)) >= 0;
                        }
                        stack.offerLast(item);
                    }
                }
            } else if (item.equals("(")) {
                stack.offerLast(item);
            } else if (item.equals(")")) {
                while (!stack.isEmpty()) {
                    String opFromStack = stack.pollLast();
                    if ("(".equals(opFromStack)) {
                        break;
                    } else {
                        postfix.offerLast(opFromStack);
                    }
                }
            }
        }

        while (!stack.isEmpty()) {
            postfix.offerLast(stack.pollLast());
        }

        return new ArrayList<>(postfix);
    }

    /**
     * Calculate a mathematical expression converted to postfix deque.
     * @param postfix a List of a postfix'd mathematical expression.
     * @return the calculated result of the mathematical expression.
     */
    private static BigDecimal calculatePostfix(List<String> postfix) {

        ArrayDeque<BigDecimal> stack = new ArrayDeque<>();

        try {
            for (String item : postfix) {
                if (item.matches("[-+]?\\d+")) {
                    BigDecimal number = new BigDecimal(item);
                    stack.offerLast(number);
                } else if (item.matches("[-+*/]")) {
                    BigDecimal num2 = stack.pollLast();
                    BigDecimal num1 = stack.pollLast();
                    if (num1 == null || num2 == null) {
                        return null;
                    }

                    switch (item) {
                        case "+":
                            stack.offerLast(num1.add(num2));
                            break;
                        case "-":
                            stack.offerLast(num1.subtract(num2));
                            break;
                        case "*":
                            stack.offerLast(num1.multiply(num2));
                            break;
                        case "/":
                            if (num2.equals(BigDecimal.ZERO)) {
                                System.out.println("Cannot divide by zero");
                                return null;
                            }
                            stack.offerLast(num1.divide(num2, RoundingMode.HALF_EVEN));
                            break;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index prob");
        }

        return stack.peekFirst();
    }

    /**
     * Display available operations and examples.
     */
    private static void displayHelp() {

        System.out.println("Operations supported:");
        System.out.println("+ addition");
        System.out.println("- subtraction");
        System.out.println("* multiplication");
        System.out.println("/ division");
        System.out.println("^ exponent");
        System.out.println("() parentheses");
        System.out.println("for example: 3 * (4 + 5)^3 / 8 - 5");
        System.out.println("\nVariables can be stored in the form of: var = num");
        System.out.println("for example: x = 2, y = 4, ABC = 35\n");
    }

}
