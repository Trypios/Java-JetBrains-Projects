package sorting;

public interface Sorter {

    void natural();
    void byCount();
    String getResult();

    default String run(String mode) {

        switch (mode) {
            case "natural":
                natural();
                break;
            case "byCount":
                byCount();
                break;
            default:
                System.out.println("Invalid mode");
        }

        return getResult();
    }
}
