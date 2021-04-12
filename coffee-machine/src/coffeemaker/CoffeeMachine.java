package coffeemaker;

import java.util.Scanner;


/**
 * coffeemaker.Coffee machine simulator
 */
public class CoffeeMachine {

    private final static Scanner scanner = new Scanner(System.in);
    private final int[] resources;  // [water (ml), milk (ml), beans (g), cups]
    private int money;  // amount in USD
    private boolean active;  // true while machine is operating

    /**
     * Constructor of coffee machine.
     * @param water Amount of water resource in ml
     * @param milk Amount of milk resource in ml
     * @param beans Amount of beans resource in g
     * @param cups Quantity of drinking cups
     * @param money Amount of money (USD)
     */
    public CoffeeMachine(int water, int milk, int beans, int cups, int money) {
        this.resources = new int[] {water, milk, beans, cups};
        this.money = money;
        this.active = true;
    }

    /**
     * Run this method to start the coffee machine.
     */
    public void operate() {
        while (active) {
            menu();
        }
    }

    /**
     * coffeemaker.Coffee machine's menu. Expects a command.
     */
    private void menu() {
        System.out.println("Available operations:\n\t1 - buy coffee\n\t2 - fill resources\n\t3 - take money"
                             + "\n\t4 - check remaining resources\n\t0 - exit");
        String command = scanner.nextLine();
        switch (command) {
            case "1":
                buyCoffee();
                break;
            case "2":
                fillResources();
                break;
            case "3":
                takeMoney();
                break;
            case "4":
                stateResources();
                break;
            case "0":
                active = false;
                break;
            default:
                System.out.println("Invalid input");
        }
    }
    
    /**
     * Display the coffee machine's levels of resources.
     */
    private void stateResources() {
        System.out.println("\nThe coffee machine has: ");
        System.out.println("\n\t" + resources[0] + " of water");
        System.out.println("\n\t" + resources[1] + " of milk");
        System.out.println("\n\t" + resources[2] + " of coffee beans");
        System.out.println("\n\t" + resources[3] + " of disposable cups");
        System.out.println("\n\t$" + money + " of money");
    }
    
    /**
     * Check if the coffee machine has enough resources to produce the specified coffee.
     * Display informative message if a resource is insufficient.
     * @param coffee The coffee in queue to be made.
     * @return true for sufficient resources to make, else false
     */
    private boolean checkResources(Coffee coffee) {
        int resourceProblem = 666;
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] - coffee.resources()[i] < 0) {
                resourceProblem = i;
            }
        }
        switch (resourceProblem) {
        	case 0:
        		System.out.println("Sorry, not enough water!");
        		return false;
        	case 1:
        		System.out.println("Sorry, not enough milk!");
        		return false;
        	case 2:
        		System.out.println("Sorry, not enough coffee beans!");
        		return false;
        	case 3:
        		System.out.println("Sorry, not enough cups!");
        		return false;
        	default:
        		System.out.println("I have enough resources, making you a coffee!");
        		return true;
        }
    }

    /**
     * Decrement relevant resources to create specified coffee.
     * @param coffee A coffee in queue to be made.
     */
    private void makeCoffee(Coffee coffee) {
        for (int i = 0; i < resources.length; i++) {
            resources[i] -= coffee.resources()[i];
        }
        this.money -= coffee.cost();
    }

    private void buyCoffee() {
        System.out.println("\nAvailable coffee options:\n\t1 - espresso\n\t2 - latte\n\t3 - cappuccino\n\t0 - back to main menu: ");
        while (true) {
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    if (checkResources(Coffee.ESPRESSO)) {
                        makeCoffee(Coffee.ESPRESSO);
                    }
                    return;
                case "2":
                    if (checkResources(Coffee.LATTE)) {
                        makeCoffee(Coffee.LATTE);
                    }
                    return;
                case "3":
                    if (checkResources(Coffee.CAPPUCCINO)) {
                        makeCoffee(Coffee.CAPPUCCINO);
                    }
                    return;
                case "0":
                    return;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
    
    /**
     * Replenish the coffee machine's resources with amounts specified by user input.
     */
    private void fillResources() {
        System.out.println("\nWrite how many ml of water do you want to add: ");
        resources[0] += Integer.parseInt(scanner.nextLine());
        System.out.println("Write how many ml of milk do you want to add: ");
        resources[1] += Integer.parseInt(scanner.nextLine());
        System.out.println("Write how many grams of coffee beans do you want to add: ");
        resources[2] += Integer.parseInt(scanner.nextLine());
        System.out.println("Write how many disposable cups of coffee do you want to add: ");
        resources[3] += Integer.parseInt(scanner.nextLine());
    }
    
    /**
     * Extract all the money from the coffee machine.
     */
    private void takeMoney() {
        System.out.println("I gave you $" + this.money);
        this.money = 0;
    }
}
