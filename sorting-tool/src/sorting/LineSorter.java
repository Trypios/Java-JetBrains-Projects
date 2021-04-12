package sorting;

import java.util.*;

/**
 * Class responsible for sorting data line by line.
 * Each of its methods takes a list of data
 * and treats every list element as a token to be sorted.
 */
public class LineSorter implements Sorter {

    /**
     * {@inheritdoc}
     * Tokenize each list element.
     */
    @Override
    public String natural(List<String> data) {

        data.sort(null);

        // prepare returning string
        StringBuilder result = new StringBuilder();
        result.append(String.format("Total lines: %s%n", data.size()))
                .append("Sorted data: \n");
        data.forEach((line) -> result.append(line).append("\n"));

        return result.toString();
    }

    /**
     * {@inheritdoc}
     * Tokenize each list element.
     */
    @Override
    public String byCount(List<String> data) {

        Map<String, Integer> map = new HashMap<>();
        int counter = 0;

        for (String line : data) {
            counter++;
            if (map.containsKey(line)) {
                map.put(line, map.get(line) + 1);
            } else {
                map.put(line, 1);
            }
        }

        List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<>(map.entrySet());

        // sort by keys
        listOfEntries.sort(Map.Entry.comparingByKey());
        // then sort by values
        listOfEntries.sort(Map.Entry.comparingByValue());

        // prepare returning string
        StringBuilder result = new StringBuilder();
        int finalCounter = counter;
        result.append(String.format("Total lines: %s%n", counter));
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
