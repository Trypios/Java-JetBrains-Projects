package phonebook;

import java.util.List;

/**
 * Implements a few popular sorting methods
 * and tracks their operational performance.
 *
 *     Bubble sort
 *     Quick sort
 *     Hash table (not exactly a sort).
 */
public class Sorter {

    private static long timer;

    /**
     * Getter. Note: timer as set after latest sort operation.
     * @return a time of operation completion in ms.
     */
    public static long getTimer() {

        return timer;
    }

    /**
     * Does not sort the directory list, but instead hashes
     * each entry to be easily found in O(1).
     * @param directory a list of phonebook entries
     * @return a hashed map of the directory list's entries.
     */
    public static HashTable hash(List<String> directory) {

        long timerStart = System.currentTimeMillis();

        HashTable hashtable = new HashTable(directory.size());
        for (String line : directory) {
            String[] parts = line.split(" ", 2);
            hashtable.put(parts[1], parts[0]);
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;

        return hashtable;
    }

    /**
     * Sort the specified directory list in place,
     * using the bubble-sort algorithm.
     * @param directory a list of phonebook entries
     */
    public static void bubbleSort(List<String> directory) {

        long timerStart = System.currentTimeMillis();

        for (int i = 0; i < directory.size() - 1; i++) {
            for (int k = 0; k < directory.size() - 1 - i; k++) {
                String name1 = directory.get(k).split(" ", 2)[1];
                String name2 = directory.get(k + 1).split(" ", 2)[1];
                if (name1.compareTo(name2) > 0) {
                    String temp = directory.get(k);
                    directory.set(k, directory.get(k + 1));
                    directory.set(k + 1, temp);
                }
            }
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;
    }

    /**
     * Sort the specified directory list in place,
     * using the quick-sort algorithm.
     * @param directory a list of phonebook entries
     */
    public static void quickSort(List<String> directory) {

        quickSort(directory, 0, directory.size() - 1);
    }

    /**
     * Helper of quickSort(List<String>).
     * @param directory a list of phonebook entries
     * @param left leftmost index during search
     * @param right rightmost index during search
     */
    private static void quickSort(List<String> directory, int left, int right) {

        long timerStart = System.currentTimeMillis();

        if (left < right) {
            int pivotIndex = partition(directory, left, right);
            quickSort(directory, left, pivotIndex - 1);
            quickSort(directory, pivotIndex + 1, right);
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;
    }

    /**
     * Helper of quickSort(List<String>, int, int).
     * @param directory a list of phonebook entries
     * @param left leftmost index during search
     * @param right rightmost index during search
     * @return a pivot index.
     */
    private static int partition(List<String> directory, int left, int right) {

        String pivot = directory.get(right).split(" ", 2)[1];
        int partitionIndex = left;

        for (int i = left; i < right; i++) {
            String name = directory.get(i).split(" ", 2)[1];
            if (name.compareTo(pivot) <= 0) { // may be used '<' as well
                swap(directory, i, partitionIndex);
                partitionIndex++;
            }
        }

        swap(directory, partitionIndex, right);

        return partitionIndex;
    }

    /**
     * Helper of partition().
     * @param directory a list of phonebook entries
     * @param i an index to be swapped with j.
     * @param j an index to be swapped with i.
     */
    private static void swap(List<String> directory, int i, int j) {

        String temp = directory.get(i);
        directory.set(i, directory.get(j));
        directory.set(j, temp);
    }
}
