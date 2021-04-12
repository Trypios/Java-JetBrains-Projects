package sorting;

import java.util.List;

/**
 * Sort data in one of two ways:
 *    natural = sort elements lexicographically.
 *    byCount = sort elements by count of occurrence.
 */
public interface Sorter {

    enum DataType {LONG, LINE, WORD}
    enum SortType {NATURAL, BY_COUNT}

    /**
     * Sort data tokens lexicographically.
     * @param data listed text line by line.
     * @return sorted data.
     */
    String natural(List<String> data);

    /**
     * Sort data tokens by count of occurrence.
     * @param data listed text line by line.
     * @return sorted data.
     */
    String byCount(List<String> data);

    /**
     * Run the specified sorting method, to sort the specified data
     * @param mode a sorting mode (natural|byCount).
     * @param data text to be sorted.
     * @return sorted data
     */
    default String sort(SortType mode, List<String> data) {

        switch (mode) {
            case NATURAL:
                return natural(data);
            case BY_COUNT:
                return byCount(data);
            default:
                System.out.println("Invalid mode");
        }
        return null;
    }

    /**
     * Sorter factory.
     * @param type what type of data is to be sorted.
     * @return a new Sorter object (based on dataType).
     */
    static Sorter factory(DataType type) {

        switch (type) {
            case LONG:
                return new LongSorter();
            case LINE:
                return new LineSorter();
            case WORD:
                return new WordSorter();
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
