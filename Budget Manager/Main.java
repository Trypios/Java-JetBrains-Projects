package budget;

import java.util.*;

import static budget.Category.*;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        menu(new BudgetManager());
        SCANNER.close();
    }

    private static void menu(BudgetManager budgetManager) {

        while (true) {

            System.out.println("Choose your action:");
            System.out.println("1) Add income");
            System.out.println("2) Add purchase");
            System.out.println("3) Show list of purchases");
            System.out.println("4) Balance");
            System.out.println("5) Save");
            System.out.println("6) Load");
            System.out.println("7) Analyze (Sort)");
            System.out.println("0) Exit");
            String command = SCANNER.nextLine();
            System.out.println();

            switch (command) {
                case "1":
                    addIncome(budgetManager);
                    break;
                case "2":
                    addPurchase(budgetManager);
                    break;
                case "3":
                    displayPurchases(budgetManager);
                    break;
                case "4":
                    showBalance(budgetManager);
                    break;
                case "5":
                    save(budgetManager);
                    break;
                case "6":
                    load(budgetManager);
                    break;
                case "7":
                    analyze(budgetManager);
                    break;
                case "0":
                    // exit
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Invalid input");
            }
            System.out.println();
        }
    }

    private static void addIncome(BudgetManager budgetManager) {

        System.out.println("Enter income:");
        double amount = Double.parseDouble(SCANNER.nextLine());
        budgetManager.addIncome(amount);
    }

    private static void addPurchase(BudgetManager budgetManager) {

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
                    category = FOOD;
                    break;
                case "2":
                    category = CLOTH;
                    break;
                case "3":
                    category = ENTERTAINMENT;
                    break;
                case "4":
                    category = OTHER;
                    break;
                case "5":
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

            budgetManager.addPurchase(name, price, category, true);
            System.out.println("Purchase was added!\n");
        }
    }

    private static void displayPurchases(BudgetManager budgetManager) {

        List<Purchase> purchases = budgetManager.getPurchases();
        if (purchases.isEmpty()) {
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
                    list = budgetManager.getPurchasesByType(FOOD);
                    break;
                case "2":
                    System.out.println("Clothes:");
                    list = budgetManager.getPurchasesByType(CLOTH);
                    break;
                case "3":
                    System.out.println("Entertainment:");
                    list = budgetManager.getPurchasesByType(ENTERTAINMENT);
                    break;
                case "4":
                    System.out.println("Other:");
                    list = budgetManager.getPurchasesByType(OTHER);
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

    private static void displayList(List<Purchase> list) {

        if (list.isEmpty()) {
            System.out.println("Purchase list is empty\n");
            return;
        }

        for (Purchase purchase : list) {
            System.out.printf("%s $%.2f%n", purchase.getName(), purchase.getPrice());
        }
        System.out.printf("Total sum: $%.2f%n%n",
                            BudgetManager.getPurchaseTotal(list));
    }

    private static void showBalance(BudgetManager budgetManager) {

        double balance = budgetManager.getBalance();
        System.out.printf("Balance: $%.2f%n", balance);
    }

    private static void save(BudgetManager budgetManager) {

        List<Purchase> purchases = budgetManager.getPurchases();

        StringBuilder text = new StringBuilder();

        for (Purchase purchase : purchases) {
            text.append(String.format("%s: %s%n%f%n", purchase.getCategory().toString(),
                                                        purchase.getName(),
                                                        purchase.getPrice()));
        }

        text.append("BALANCE: ").append(budgetManager.getBalance());
        FileManager.save(text.toString());

        System.out.println("Purchases were saved!");
    }

    private static void load(BudgetManager budgetManager) {

        budgetManager.getPurchases().clear();

        String currentPurchase = "";
        Category currentCategory = null;

        for (String line : FileManager.load().split("\n")) {

            if (line.length() == 0) {
                return;
            }

            if (line.startsWith(FOOD.toString())) {
                currentCategory = FOOD;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith(CLOTH.toString())) {
                currentCategory = CLOTH;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith(ENTERTAINMENT.toString())) {
                currentCategory = ENTERTAINMENT;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith(OTHER.toString())) {
                currentCategory = OTHER;
                currentPurchase = line.replaceFirst(currentCategory.toString() + ": ","");
            } else if (line.startsWith("BALANCE")) {
                line = line.replaceFirst("BALANCE: ", "");
                double balance = Double.parseDouble(line);
                budgetManager.setBalance(balance);
            } else {
                double currentPrice = Double.parseDouble(line);
                budgetManager.addPurchase(currentPurchase,
                                            currentPrice,
                                            currentCategory,
                                false);
            }
        }

        System.out.println("Purchases were loaded!");
    }

    private static void analyze(BudgetManager budgetManager) {

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
                    sortAll(budgetManager);
                    break;
                case "2":
                    sortByType(budgetManager);
                    break;
                case "3":
                    sortCertainType(budgetManager);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    private static void sortByType(BudgetManager budgetManager) {

        Map<Category, Double> map = new HashMap<>();

        for (Category category : Category.values()) {
            map.put(category,
                    BudgetManager.getPurchaseTotal(budgetManager.getPurchasesByType(category)));
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

    private static void sortCertainType(BudgetManager budgetManager) {

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
                    list = budgetManager.getPurchasesByType(FOOD);
                    break;
                case "2":
                    list = budgetManager.getPurchasesByType(CLOTH);
                    break;
                case "3":
                    list = budgetManager.getPurchasesByType(ENTERTAINMENT);
                    break;
                case "4":
                    list = budgetManager.getPurchasesByType(OTHER);
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

    private static void sortAll(BudgetManager budgetManager) {

        List<Purchase> purchases = budgetManager.getPurchases();
        purchases.sort(Comparator.comparing(Purchase::getPrice).reversed());
        displayList(purchases);
    }

}
