package budget;

import java.util.*;

/**
 * Class responsible for storing various purchases in a List,
 * along with a certain budget, for sorting and analyzing expenses.
 */
public class BudgetManager {

    private static final Scanner SCANNER = new Scanner(System.in);
    private final List<Purchase> purchases = new ArrayList<>();
    private double balance = 0d;
    private boolean active = true;

    /**
     * Run this method to start the budget manager.
     */
    public void run() {

        while (active) {
            menu();
        }
    }

    /**
     * Getter, only used internally
     */
    private double getBalance() {
        return balance;
    }

    /**
     * Setter, only used internally
     */
    private void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Budget manager's main menu. User may select an operation.
     */
    private void menu() {

        System.out.println("Choose your action:");
        System.out.println("1 - Add income");
        System.out.println("2 - Add purchase");
        System.out.println("3 - Show list of purchases");
        System.out.println("4 - Balance");
        System.out.println("5 - Save");
        System.out.println("6 - Load");
        System.out.println("7 - Sort purchases");
        System.out.println("0 - Exit");
        String command = SCANNER.nextLine();
        System.out.println();

        switch (command) {
            case "1":
                addIncome();
                break;
            case "2":
                addPurchase();
                break;
            case "3":
                displayPurchases();
                break;
            case "4":
                displayBalance();
                break;
            case "5":
                save();
                break;
            case "6":
                load();
                break;
            case "7":
                sortMenu();
                break;
            case "0":  // exit
                active = false;
                System.out.println("Bye!");
                break;
            default:
                System.out.println("Invalid input");
        }
        System.out.println();
    }

    /**
     * Ask user for input as to
     * increase the balance in USD.
     */
    private void addIncome() {
        System.out.println("Enter income:");
        double amount = Double.parseDouble(SCANNER.nextLine());
        setBalance(getBalance() + amount);
    }

    /**
     * Add a new purchase to the list of purchases.
     * @param name see class Purchase.
     * @param price see class Purchase.
     * @param category see class Purchase.
     * @param changeBalance see class Purchase.
     */
    private void addPurchase(String name, double price, Category category, boolean changeBalance) {
        purchases.add(new Purchase(name, price, category));
        balance -= changeBalance ? price : 0;
    }

    /**
     * Getter.
     * @return a list of purchases.
     */
    private List<Purchase> getPurchases() {
        return purchases;
    }

    /**
     * Reduce the list of purchases by a given type.
     * @param type a type of purchase (food, clothes, entertainment, other).
     * @return a reduced list of purchases.
     */
    private List<Purchase> getPurchasesByType(Category type) {
        List<Purchase> list = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if (purchase.getCategory().equals(type)) {
                list.add(purchase);
            }
        }
        return list;
    }

    /**
     * Calculate the total cost of a certain category of purchases.
     * @param type What kind of purchases
     * @return The total cost of a category of purchases.
     */
    private double getPurchaseTotal(Category type) {
        double totalSpent = 0;
        for (Purchase purchase : getPurchasesByType(type)) {
            totalSpent += purchase.getPrice();
        }
        return totalSpent;
    }

    /**
     * Calculate the total cost of a given list of purchases.
     * @param list A list of purchases
     * @return The total cost of all made purchases.
     */
    private double getPurchaseTotal(List<Purchase> list) {
        double totalSpent = 0;
        for (Purchase purchase : list) {
            totalSpent += purchase.getPrice();
        }
        return totalSpent;
    }

    /**
     * Ask user for input, to add a new purchase.
     */
    private void addPurchase() {

        while (true) {
            System.out.println("Choose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) Back");
            String input = SCANNER.nextLine();
            System.out.println();

            boolean invalidInput = false;
            Category category = null;
            switch (input) {
                case "1":
                    category = Category.FOOD;
                    break;
                case "2":
                    category = Category.CLOTH;
                    break;
                case "3":
                    category = Category.ENTERTAINMENT;
                    break;
                case "4":
                    category = Category.OTHER;
                    break;
                case "5":
                    System.out.println("Aborted...");
                    return;
                default:
                    System.out.println("Invalid input");
                    invalidInput = true;
                    break;
            }

            if (invalidInput) {
                continue;
            }

            System.out.println("Enter purchase name:");
            String name = SCANNER.nextLine();

            System.out.println("Enter its price:");
            double price = Double.parseDouble(SCANNER.nextLine());

            addPurchase(name, price, category, true);
            System.out.println("Purchase was added!\n");
        }
    }

    /**
     * Display purchases, either altogether or specific type group.
     */
    private void displayPurchases() {

        if (getPurchases().isEmpty()) {
            System.out.println("\nPurchase list is empty");
            return;
        }

        while (true) {

            System.out.println("Choose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) All");
            System.out.println("6) Back");
            String input = SCANNER.nextLine();
            System.out.println();

            List<Purchase> list = new ArrayList<>();
            boolean invalidInput = false;

            switch (input) {
                case "1":
                    System.out.println("Food:");
                    list = getPurchasesByType(Category.FOOD);
                    break;
                case "2":
                    System.out.println("Clothes:");
                    list = getPurchasesByType(Category.CLOTH);
                    break;
                case "3":
                    System.out.println("Entertainment:");
                    list = getPurchasesByType(Category.ENTERTAINMENT);
                    break;
                case "4":
                    System.out.println("Other:");
                    list = getPurchasesByType(Category.OTHER);
                    break;
                case "5":
                    System.out.println("All:");
                    list = purchases;
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid input...");
                    invalidInput = true;
            }

            if (!invalidInput) {
                displayList(list);
            }

        }
    }

    /**
     * Helper method to display a specified list of purchases.
     * @param list a list of purchases.
     */
    private void displayList(List<Purchase> list) {

        if (list.isEmpty()) {
            System.out.println("Purchase list is empty\n");
            return;
        }

        for (Purchase purchase : list) {
            System.out.printf("%s $%.2f%n", purchase.getName(), purchase.getPrice());
        }
        System.out.printf("Total sum: $%.2f%n%n", getPurchaseTotal(list));
    }

    /**
     * Display the current balance to 2 decimal points.
     */
    private void displayBalance() {

        double balance = getBalance();
        System.out.printf("Balance: $%.2f%n", balance);
    }

    /**
     * Save purchase list and budget to a file.
     */
    private void save() {

        List<Purchase> purchases = getPurchases();

        StringBuilder text = new StringBuilder();

        for (Purchase purchase : purchases) {
            text.append(String.format("%s: %s%n%f%n", purchase.getCategory().toString(),
                    purchase.getName(),
                    purchase.getPrice()));
        }

        text.append("BALANCE: ").append(getBalance());
        FileManager.save(text.toString());

        System.out.println("Purchases were saved!");
    }

    /**
     * Load a purchase list and a budget from a file.
     */
    private void load() {

        getPurchases().clear();

        String currentPurchase = "";
        Category currentCategory = null;

        for (String line : FileManager.load().split("\n")) {

            if (line.length() == 0) {
                return;
            }

            if (line.startsWith(Category.FOOD.toString())) {
                currentCategory = Category.FOOD;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith(Category.CLOTH.toString())) {
                currentCategory = Category.CLOTH;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith(Category.ENTERTAINMENT.toString())) {
                currentCategory = Category.ENTERTAINMENT;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith(Category.OTHER.toString())) {
                currentCategory = Category.OTHER;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith("BALANCE")) {
                line = line.replaceFirst("BALANCE: ", "");
                double balance = Double.parseDouble(line);
                setBalance(balance);
            } else {
                double currentPrice = Double.parseDouble(line);
                addPurchase(currentPurchase,
                        currentPrice,
                        currentCategory,
                        false);
            }
        }

        System.out.println("Purchases were loaded!");
    }

    /**
     * Options menu for sorting purchases by price descending.
     */
    private void sortMenu() {

        while (true) {
            System.out.println("How do you want to sort?");
            System.out.println("1) Sort all purchases");
            System.out.println("2) Sort by type");
            System.out.println("3) Sort certain type");
            System.out.println("4) Back");

            String input = SCANNER.nextLine();
            System.out.println();

            switch (input) {
                case "1":
                    sortAll();
                    break;
                case "2":
                    sortByType();
                    break;
                case "3":
                    sortCertainType();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    /**
     * Sort each type of purchases by price descending.
     */
    private void sortByType() {

        Map<Category, Double> map = new HashMap<>();

        for (Category category : Category.values()) {
            map.put(category, getPurchaseTotal(getPurchasesByType(category)));
        }

        Comparator<Category> comp = (cat1, cat2) -> {
            Double cat1Value = map.get(cat1);
            Double cat2Value = map.get(cat2);
            return cat1Value.compareTo(cat2Value);
        };

        TreeMap<Category, Double> sortedMap = new TreeMap<>(comp.reversed());

        sortedMap.putAll(map);

        double grandTotal = 0;

        System.out.println("Types:");

        for (var entry : sortedMap.entrySet()) {
            double price = entry.getValue();
            grandTotal += price;
            switch (entry.getKey()) {
                case FOOD:
                    System.out.printf("Food - $%.2f%n", price);
                    break;
                case CLOTH:
                    System.out.printf("Clothes - $%.2f%n", price);
                    break;
                case ENTERTAINMENT:
                    System.out.printf("Entertainment - $%.2f%n", price);
                    break;
                case OTHER:
                    System.out.printf("Other - $%.2f%n", price);
                    break;
            }
        }

        System.out.printf("Total sum: $%.2f%n%n", grandTotal);
    }

    /**
     * Sort the list of a certain type of purchases by price descending.
     * Ask user for input to determine purchase category.
     */
    private void sortCertainType() {

        List<Purchase> list;

        while (true) {
            System.out.println("Choose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");

            String input2 = SCANNER.nextLine();
            System.out.println();

            switch (input2) {
                case "1":
                    list = getPurchasesByType(Category.FOOD);
                    break;
                case "2":
                    list = getPurchasesByType(Category.CLOTH);
                    break;
                case "3":
                    list = getPurchasesByType(Category.ENTERTAINMENT);
                    break;
                case "4":
                    list = getPurchasesByType(Category.OTHER);
                    break;
                default:
                    System.out.println("Invalid input");
                    continue;
            }
            break;
        }

        list.sort(Comparator.comparing(Purchase::getPrice).reversed());
        displayList(list);
    }

    /**
     * Sort the list of purchases by price descending.
     */
    private void sortAll() {

        List<Purchase> purchases = getPurchases();
        purchases.sort(Comparator.comparing(Purchase::getPrice).reversed());
        displayList(purchases);
    }
}
