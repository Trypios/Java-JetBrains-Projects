package sorting;

import java.util.*;

/**
 * Class responsible for sorting numeric data (integers).
 * Each of its methods takes a list of data
 * and treats every number of each list element
 * as a token to be sorted.
 */
public class LongSorter implements Sorter {

    /**
     * {@inheritdoc}
     * Tokenize every number (integer/long) of each list element.
     */
    @Override
    public String natural(List<String> data) {

        ArrayList<Long> array = new ArrayList<>();

        for (String line : data) {
            for (String number : line.split("\\s+")) {
                if (number.matches("-?\\d+")) {
                    array.add(Long.parseLong(number));
                } else {
                    System.out.printf("\"%s\" isn't a number. It's skipped.%n", number);
                }
            }
        }

        array.sort(null);

        // prepare returning string
        StringBuilder result = new StringBuilder();
        result.append(String.format("Total numbers: %s%n", array.size()))
                .append("Sorted data: ");
        array.forEach((number) -> result.append(number).append(" "));
        result.append("\n");

        return result.toString();
    }

    /**
     * {@inheritdoc}
     * Tokenize every number (integer/long) of each list element.
     */
    @Override
    public String byCount(List<String> data) {

        Map<Long, Integer> map = new HashMap<>();
        int counter = 0;

        for (String line : data) {
            for (String text : line.split("\\s+")) {
                if (text.matches("-?\\d+")) {
                    counter++;
                    Long number = Long.parseLong(text);
                    if (map.containsKey(number)) {
                        map.put(number, map.get(number) + 1);
                    } else {
                        map.put(number, 1);
                    }
                } else {
                    System.out.printf("\"%s\" isn't a number. It's skipped.%n", text);
                }
            }
        }

        List<Map.Entry<Long, Integer>> listOfEntries = new ArrayList<>(map.entrySet());

        // sort by keys
        listOfEntries.sort(Map.Entry.comparingByKey());
        // then sort by values
        listOfEntries.sort(Map.Entry.comparingByValue());

        // prepare returning string
        final int finalCounter = counter;
        StringBuilder result = new StringBuilder();
        result.append(String.format("Total numbers: %s%n", finalCounter));
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
