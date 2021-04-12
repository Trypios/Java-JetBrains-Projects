package sorting;

import java.util.*;

/**
 * Class responsible for sorting alphabetical data (words).
 * Each of its methods takes a list of data
 * and treats every word of each list element
 * as a token to be sorted.
 */
public class WordSorter implements Sorter {

    /**
     * {@inheritdoc}
     * Tokenize every word of each list element.
     */
    @Override
    public String natural(List<String> data) {

        ArrayList<String> array = new ArrayList<>();

        for (String line : data) {
            for (String word : line.split("\\s+")) {
                if (word.matches("[\\w-]+")) {
                    array.add(word);
                } else {
                    System.out.printf("\"%s\" isn't a word. It's skipped.%n", word);
                }
            }
        }

        array.sort(null);

        // prepare returning string
        StringBuilder result = new StringBuilder();
        result.append(String.format("Total words: %s%n", array.size()))
                .append("Sorted data: ");
        array.forEach((word) -> result.append(word).append(" "));
        result.append("\n");

        return result.toString();
    }

    /**
     * {@inheritdoc}
     * Tokenize every word of each list element.
     */
    @Override
    public String byCount(List<String> data) {

        Map<String, Integer> map = new HashMap<>();
        int counter = 0;

        for (String line : data) {
            for (String word : line.split("\\s+")) {
                if (word.matches("[\\w-]+")) {
                    counter++;
                    if (map.containsKey(word)) {
                        map.put(word, map.get(word) + 1);
                    } else {
                        map.put(word, 1);
                    }
                } else {
                    System.out.printf("\"%s\" isn't a word. It's skipped.%n", word);
                }
            }
        }

        List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<>(map.entrySet());

        // sort by keys
        listOfEntries.sort(Map.Entry.comparingByKey());
        // then sort by values
        listOfEntries.sort(Map.Entry.comparingByValue());

        // prepare returning string
        final int finalCounter = counter;
        StringBuilder result = new StringBuilder();
        result.append(String.format("Total words: %s%n", counter));
        listOfEntries.forEach((entry) -> {
                        long percentage = Math.round(entry.getValue() / (double) finalCounter * 100);
                        result.append(String.format("%s: %d time(s), %d%%%n",
                                                                            entry.getKey(),
                                                                            entry.getValue(),
                                                                            percentage));
                    });

        return result.toString();
    }

}
