package phonebook;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a few popular searching methods
 * and tracks their operational performance.
 *
 *     Linear search
 *     Jump search (after bubble sort)
 *     Binary search (after quick sort)
 *     Hash search (after creating hash table)
 */
public class Searcher {

    private static long timer;

    /**
     * Getter. Note: timer as set after latest search operation.
     * @return a time of operation completion in ms.
     */
    public static long getTimer() {

        return timer;
    }

    /**
     * Search the unsorted specified directory list for entries of the specified targets list,
     * using the linear-search algorithm.
     * @param directory a list of phonebook entries
     * @param targets a list of targets
     * @return a list of found entries
     */
    public static List<String> linearSearch(List<String> directory, List<String> targets) {

        List<String> entriesFound = new ArrayList<>();

        System.out.println("Start searching (linear search)...");

        long timerStart = System.currentTimeMillis();

        for (String name : targets) {
            for (String line : directory) {
                if (line.contains(name)) {
                    entriesFound.add(name);
                    break;
                    // String phoneNo = line.split(" ")[0];
                }
            }
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;

        return entriesFound;
    }

    /**
     * Search the unsorted specified directory list for entries of the specified targets list,
     * using the jump-search algorithm. Since jump-search requires a sorted dataset,
     * the list is bubble-sorted first.
     * @param directory a list of phonebook entries
     * @param targets a list of targets
     * @return a list of found entries
     */
    public static List<String> jumpSearch(List<String> directory, List<String> targets) {

        List<String> entriesFound = new ArrayList<>();

        System.out.println("Start searching (bubble sort + jump search)...");

        // bubble sort array first
        Sorter.bubbleSort(directory);

        // then perform actual jumpSearch
        long timerStart = System.currentTimeMillis();

        int jump = (int) Math.sqrt(directory.size());
        for (String name : targets) {
            int right = 0;
            int left = 0;
            boolean found = false;

            while (right < directory.size() - 1 && !found) {

                if (directory.get(right).contains(name)) {
                    entriesFound.add(name);
                    break;
                }

                right = Math.min(right + jump, directory.size() - 1);

                if (directory.get(right).compareTo(name) < 0) {
                    for (int i = right - 1; i >= left; i--) {
                        if (directory.get(i).contains(name)) {
                            entriesFound.add(name);
                            found = true;
                            break;
                        }
                    }
                }

                left = right;
            }
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;

        return entriesFound;
    }

    /**
     * Search the unsorted specified directory list for entries of the specified targets list,
     * using the binary-search algorithm. Since binary-search requires a sorted dataset,
     * the list is quick-sorted first.
     * @param directory a list of phonebook entries
     * @param targets a list of targets
     * @return a list of found entries
     */
    public static List<String> binarySearch(List<String> directory, List<String> targets) {

        List<String> entriesFound = new ArrayList<>();

        System.out.println("Start searching (quick sort + binary search)...");

        // first quick-sort directory
        Sorter.quickSort(directory);

        // then perform actual binarySearch
        long timerStart = System.currentTimeMillis();

        for (String name : targets) {
            int left = 0;
            int right = directory.size() - 1;

            while (left <= right) {

                int mid = left + (right - left) / 2;
                String nameDir = directory.get(mid).split(" ", 2)[1];

                if (nameDir.equals(name)) {
                    entriesFound.add(name);
                    break;
                } else if (nameDir.compareTo(name) > 0) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;

        return entriesFound;
    }

    /**
     * Search the unsorted specified directory list for entries of the specified targets list,
     * using hashes. A hash table is created first.
     * @param directory a list of phonebook entries
     * @param targets a list of targets
     * @return a list of found entries
     */
    public static List<String> hashSearch(List<String> directory, List<String> targets) {

        List<String> entriesFound = new ArrayList<>();

        System.out.println("Start searching (hash table)...");

        // first create a hash table
        HashTable hashtable = Sorter.hash(directory);

        // then perform actual binarySearch
        long timerStart = System.currentTimeMillis();

        for (String name : targets) {
            if (hashtable.containsKey(name)) {
                entriesFound.add(hashtable.get(name));
            }
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;

        return entriesFound;
    }
}
