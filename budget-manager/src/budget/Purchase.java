package budget;

/**
 * Simple class for storing info about a purchase.
 * Attributes and methods are self-explanatory.
 */
public class Purchase {

    private final String name;
    private final double price;
    private final Category category;

    public Purchase(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }
}
