package sorting;

import java.util.*;


public class LineSorter implements Sorter {

    private ArrayList<String> data;
    private String result;

    public LineSorter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public void natural() {

        data.sort(null);

        // prepare returning string
        StringBuilder result = new StringBuilder();
        result.append(String.format("Total lines: %s%n", data.size()))
                .append("Sorted data: \n");
        data.forEach((line) -> result.append(line).append("\n"));

        this.result = result.toString();
    }

    @Override
    public void byCount() {

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

        this.result = result.toString();
    }

}
