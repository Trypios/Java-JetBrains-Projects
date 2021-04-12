package coffeemaker;

/**
 * Each coffee requires resources to be made 
 * and has a certain cost in USD.
 * Resources are saved in int[] array as follows:
 * [water (ml), milk (ml), beans (g), cups]
 */
public enum Coffee {
    ESPRESSO(new int[] {250, 0, 16, 1}, 4),
    LATTE(new int[] {350, 75, 20, 1}, 7),
    CAPPUCCINO(new int[] {200, 100, 12, 1}, 6);

    private final int[] RESOURCES;
    private final int COST;

    Coffee(int[] resources, int cost) {
        this.RESOURCES = resources;
        this.COST = cost;
    }

    public int[] resources() {
        return this.RESOURCES;
    }

    public int cost() {
        return this.COST;
    }
}
