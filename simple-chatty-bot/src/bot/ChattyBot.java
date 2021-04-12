package bot;

import java.util.Scanner;

public class ChattyBot {

    final static Scanner scanner = new Scanner(System.in); // Do not change this line
    private final String name;
    private final int year;

    public ChattyBot(String name, int year) {
        this.name = name;
        this.year = year;
    }

    /**
     * Run this method to activate the bot's cycle.
     */
    public void run() {

        greet();
        remindName();
        guessAge();
        count();
        test();
        end();
    }

    /**
     * Bot introduces itself.
     */
    private void greet() {
        System.out.printf("Hello! My name is %s.%n", name);
        System.out.printf("I was created in %d.%n", year);
    }

    /**
     * Bot asks user's name, then responds.
     */
    private void remindName() {
        System.out.println("Please, remind me your name.");
        String uname = scanner.nextLine();
        System.out.printf("What a great name you have, %s!%n", uname);
    }

    /**
     * Bot guesses user's age by asking a few math questions.
     */
    private void guessAge() {
        System.out.println("Let me guess your age.");
        System.out.println("Tell me the remainders of dividing your age by 3, 5 and 7.");
        int rem3 = scanner.nextInt();
        int rem5 = scanner.nextInt();
        int rem7 = scanner.nextInt();
        int age = (rem3 * 70 + rem5 * 21 + rem7 * 15) % 105;
        System.out.println("Your age is " + age + "; that's a good time to start programming!");
    }

    /**
     * Bot asks user for a number and starts counting from 0 towards that number.
     */
    private void count() {
        System.out.println("Now I will prove to you that I can count to any number you want.");
        System.out.println("Your number: ");
        int num = scanner.nextInt();
        for (int i = 0; i <= num; i++) {
            System.out.printf("    %d !%n", i);
        }
    }

    /**
     * Asks the user a question and confirm their answer.
     */
    private void test() {
        System.out.println("Let's test your programming knowledge.");
        System.out.println("Why do we use methods?");
        System.out.println("   1. To repeat a statement multiple times.");
        System.out.println("   2. To decompose a program into several small subroutines.");
        System.out.println("   3. To determine the execution time of a program.");
        System.out.println("   4. To interrupt the execution of a program.");
        
        int ans;
        while (true) {
            ans = scanner.nextInt();
            if (ans == 2) {
                System.out.println("Correct!");
                break;
            } else {
                System.out.println("Please, try again.");
            }
        }
    }

    private void end() {
        System.out.println("Have a nice day!");
    }
}
