package budget;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager {

    private final List<Purchase> purchases = new ArrayList<>();
    private double balance = 0d;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addIncome(double amount) {
        balance += amount;
    }

    public void addPurchase(String name, double price, Category category, boolean changeBalance) {

        purchases.add(new Purchase(name, price, category));
        balance -= changeBalance ? price : 0;
    }

    public List<Purchase> getPurchases() {

        return purchases;
    }

    public List<Purchase> getPurchasesByType(Category type) {

        List<Purchase> list = new ArrayList<>();

        for (Purchase purchase : purchases) {
            if (purchase.getCategory().equals(type)) {
                list.add(purchase);
            }
        }

        return list;
    }

    public double getPurchaseTotal(Category type) {

        double totalSpent = 0;
        for (Purchase purchase : getPurchasesByType(type)) {
            totalSpent += purchase.getPrice();
        }
        return totalSpent;
    }

    public static double getPurchaseTotal(List<Purchase> list) {

        double totalSpent = 0;
        for (Purchase purchase : list) {
            totalSpent += purchase.getPrice();
        }
        return totalSpent;
    }
}
