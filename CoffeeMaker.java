import java.util.Scanner;

public class CoffeeMachine {

    static Scanner scanner = new Scanner(System.in);
    int water;
    int milk;
    int beans;
    int cups;
    int money;

    public CoffeeMachine(int water, int milk, int beans, int cups, int money) {
        // constructor
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
        this.money = money;
    }

    public static void main(String[] args) {
        // INIT
        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);
        boolean operating = true;

        while (operating) {
            System.out.println("Write action (buy, fill, take, remaining, exit): ");
            String command = scanner.nextLine();
            switch (command) {
                case "buy":
                    coffeeMachine.buyCoffee();
                    break;
                case "fill":
                    coffeeMachine.fillResources();
                    break;
                case "take":
                    coffeeMachine.takeMoney();
                    break;
                case "remaining":
                    coffeeMachine.stateResources();
                    break;
                case "exit":
                    operating = false;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }
    
    private void stateResources() {
        System.out.println("\nThe coffee machine has: ");
        System.out.println(this.water + " of water");
        System.out.println(this.milk + " of milk");
        System.out.println(this.beans + " of coffee beans");
        System.out.println(this.cups + " of disposable cups");
        System.out.println("$" + this.money + " of money");
    }
    
    private boolean checkResources(int[] coffee) {
        int[] resources = new int[] {this.water, this.milk, this.beans, this.cups};
        int resourceProblem = 666;
        for (int i = 0; i < resources.length; i++) {
            if (resources[i] - coffee[i] < 0) {
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

    private void makeCoffee(int[] coffeeResources) {
        this.water -= coffeeResources[0];
        this.milk -= coffeeResources[1];
        this.beans -= coffeeResources[2];
        this.cups -= coffeeResources[3];
        this.money += coffeeResources[4];
    }

    private void buyCoffee() {
        System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
        String option = scanner.nextLine();
        switch (option) {
            case "1":
                int[] espresso = new int[] {250, 0, 16, 1, 4};  // water, milk, beans, cups, money
                if (checkResources(espresso)) {
                    makeCoffee(espresso);
                }
                break;
            case "2":
                int[] latte = new int[] {350, 75, 20, 1, 7};  // water, milk, beans, cups, money
                if (checkResources(latte)) {
                    makeCoffee(latte);
                }
                break;
            case "3":
                int[] cappuccino = new int[] {200, 100, 12, 1, 6};  // water, milk, beans, cups, money
                if (checkResources(cappuccino)) {
                    makeCoffee(cappuccino);
                }
                break;
            case "back":
            	break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
    
    private void fillResources() {
        System.out.println("\nWrite how many ml of water do you want to add: ");
        this.water += scanner.nextInt();
        System.out.println("Write how many ml of milk do you want to add: ");
        this.milk += scanner.nextInt();
        System.out.println("Write how many grams of coffee beans do you want to add: ");
        this.beans += scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee do you want to add: ");
        this.cups += scanner.nextInt();
    }
    
    private void takeMoney() {
        System.out.println("I gave you $" + this.money);
        this.money = 0;
    }

}