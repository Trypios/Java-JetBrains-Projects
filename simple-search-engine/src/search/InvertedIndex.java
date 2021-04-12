package search;

import java.util.*;

/**
 * Maps each word to all positions/lines/documents in which the word occurs.
 * As a result, when we receive a query, we can immediately find the answer without any comparisons.
 */
public class InvertedIndex {

    private final List<String> LINES = new ArrayList<>();
    private final Map<String, List<Integer>> MAP = new HashMap<>();

    /**
     * Add an entry to InvertedIndex.
     * The entry is added both to LINES and to the MAP of indexes.
     * @param entry a line of text
     * @param index the number of index of the entry
     */
    public void add(String entry, int index) {

        this.LINES.add(entry);

        for (String word : entry.split(" ")) {
            String wordLower = word.toLowerCase();
            if (MAP.containsKey(wordLower)) {
                List<Integer> listOfIndexes = MAP.get(wordLower);
                listOfIndexes.add(index);
                MAP.put(wordLower, listOfIndexes);
            } else {
                MAP.put(wordLower, new ArrayList<>(List.of(index)));
            }
        }
    }

    /**
     * Check if InvertedIndex consists of any data.
     * @return true if no data exist, else false.
     */
    public boolean isEmpty() {
        return LINES.isEmpty();
    }

    /**
     * Getter
     */
    public List<String> getLines() {
        return LINES;
    }

    /**
     * Search for line(s) in LINES matching all words of the query.
     * @param query word(s) separated by space
     * @return a list of matching lines from LINES.
     */
    public List<String> matchAll(String query) {

        Set<String> foundTargets = new HashSet<>();
        Set<Integer> set = new HashSet<>();

        String[] targetParts = query.split(" ");
        boolean foundMatch = false;

        for (String word : targetParts) {
            String wordLower = word.toLowerCase();
            if (!foundMatch) {
                if (MAP.containsKey(wordLower)) {
                    foundMatch = true;
                    set.addAll(MAP.get(wordLower));
                }
            } else {
                if (MAP.containsKey(wordLower)) {
                    set.retainAll(new HashSet<>(MAP.get(wordLower)));
                }
            }
        }

        if (!set.isEmpty()) {
            for (int index : set) {
                foundTargets.add(LINES.get(index));
            }
            return new ArrayList<>(foundTargets);
        }

        System.out.println("No matching people found.");
        System.out.println();
        return null;
    }

    /**
     * Search for line(s) in LINES matching any words of the query.
     * @param query word(s) separated by space
     * @return a list of matching lines from LINES.
     */
    public List<String> matchAny(String query) {

        Set<String> foundTargets = new HashSet<>();

        String[] targetParts = query.split(" ");
        boolean foundMatch = false;
        for (String word : targetParts) {
            String wordLower = word.toLowerCase();
            if (MAP.containsKey(wordLower)) {
                foundMatch = true;
                for (int index : MAP.get(wordLower)) {
                    foundTargets.add(LINES.get(index));
                }
            }
        }

        if (foundMatch) {
            return new ArrayList<>(foundTargets);
        }

        System.out.println("No matching people found.");
        System.out.println();
        return null;
    }

    /**
     * Search for line(s) in LINES not matching any of words of the query.
     * @param target a name to exclude from the resulting list.
     * @return a list of people.
     */
    public List<String> matchNone(String target) {

        Set<String> foundTargets = new HashSet<>(LINES);
        foundTargets.removeAll(matchAny(target));

        if (!foundTargets.isEmpty()) {
            return new ArrayList<>(foundTargets);
        }

        System.out.println("No unmatching lines found.");
        System.out.println();
        return null;
    }
}
