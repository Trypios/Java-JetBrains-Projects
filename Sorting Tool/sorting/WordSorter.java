package sorting;

import java.util.*;


public class WordSorter implements Sorter {

    private ArrayList<String> data;
    private String result;

    public WordSorter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void natural() {

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
        this.data = array;

        // prepare returning string
        StringBuilder result = new StringBuilder();
        result.append(String.format("Total words: %s%n", array.size()))
                .append("Sorted data: ");
        array.forEach((word) -> result.append(word).append(" "));
        result.append("\n");

        this.result = result.toString();
    }

    @Override
    public void byCount() {

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

        this.result = result.toString();
    }

}
