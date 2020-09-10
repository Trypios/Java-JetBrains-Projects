package phonebook;

import java.util.List;

public class Sorter {

    private static long timer;

    public static long getTimer() {
        /*
         * Returns timer as set after latest sort operation
         * (time of operation completion in ms)
         * */

        return timer;
    }

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

    public static void bubbleSort(List<String> directory) {
        /*
         * Bubble-Sorts the directory list
         * */

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

    public static void quickSort(List<String> directory) {

        quickSort(directory, 0, directory.size() - 1);
    }

    public static void quickSort(List<String> directory, int left, int right) {
        /*
         * Quick-Sorts the directory list
         * */

        long timerStart = System.currentTimeMillis();

        if (left < right) {
            int pivotIndex = partition(directory, left, right);
            quickSort(directory, left, pivotIndex - 1);
            quickSort(directory, pivotIndex + 1, right);
        }

        long timerStop = System.currentTimeMillis();
        timer = timerStop - timerStart;
    }

    private static int partition(List<String> directory, int left, int right) {
        /*
         * quickSort subroutine
         * */

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

    private static void swap(List<String> directory, int i, int j) {
        /*
         * quickSort subroutine
         * */

        String temp = directory.get(i);
        directory.set(i, directory.get(j));
        directory.set(j, temp);
    }

}
