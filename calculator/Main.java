package calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final HashMap<String, String> variables = new HashMap<>();
    private static final HashMap<String, String> precedence = new HashMap<>();


    private static void initPrecedence() {

        precedence.put("+", "1");
        precedence.put("-", "1");
        precedence.put("*", "2");
        precedence.put("/", "2");
    }

    private static void inputNumbers() {

        while (true) {

            String input = SCANNER.nextLine();

            if (input.contains("=")) {
                parseVariables(input);
                continue;
            }

            switch (input) {
                case "/exit":
                    System.out.println("Bye!");
                    return;
                case "/help":
                    displayHelp();
                    break;
                case "":
                    continue;
                default:
                    if (input.startsWith("/")) {
                        System.out.println("Unknown command");
                        continue;
                    }

                    List<String> infix = parseToInfix(input);

                    if (infix != null && testBrackets(input)) {
                        List<String> postfix = infixToPostfix(infix);

                        if (postfix != null) {
                            BigDecimal result = calculation(postfix);
                            if (result != null) {
                                System.out.printf("%s%n", result.toBigInteger().toString());
                            }
                        } else {
                            System.out.println("Invalid expression");
                        }
                    } else {
                        System.out.println("Invalid expression");
                    }

                    break;
            }
        }
    }

    private static void parseVariables(String textline) {

        String[] parts = textline.split("\\s*=\\s*");
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

    private static List<String> parseToInfix(String textline) {

        ArrayDeque<String> stack = new ArrayDeque<>();

        if ("+ - * / ".contains(textline.substring(0, 2)) ||
                textline.substring(0, 1).matches("[)*/]")) {
            return null;
        }

        if (textline.endsWith("-")) {
            return null;
        }

        for (String character : textline.split("")) {
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

    private static boolean testBrackets(String textline) {

        ArrayDeque<String> stack = new ArrayDeque<>();

        for (String letter : textline.split("")) {
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

    private static BigDecimal calculation(List<String> postfix) {

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

    public static void main(String[] args) {

        initPrecedence();
        inputNumbers();
    }

}
