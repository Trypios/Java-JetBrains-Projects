package search;

import java.util.*;

public class InvertedIndex {

    private final List<String> lines = new ArrayList<>();
    private final HashMap<String, List<Integer>> map = new HashMap<>();


    public void add(String textline, int index) {

        this.lines.add(textline);

        for (String word : textline.split(" ")) {
            String wordLower = word.toLowerCase();
            if (map.containsKey(wordLower)) {
                List<Integer> listOfIndexes = map.get(wordLower);
                listOfIndexes.add(index);
                map.put(wordLower, listOfIndexes);
            } else {
                map.put(wordLower, new ArrayList<>(List.of(index)));
            }
        }
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public List<String> getLines() {
        return lines;
    }

    public List<String> matchAll(String target) {

        Set<String> foundTargets = new HashSet<>();
        HashSet<Integer> set = new HashSet<>();

        String[] targetParts = target.split(" ");
        boolean foundMatch = false;

        for (String word : targetParts) {
            String wordLower = word.toLowerCase();
            if (!foundMatch) {
                if (map.containsKey(wordLower)) {
                    foundMatch = true;
                    set.addAll(map.get(wordLower));
                }
            } else {
                if (map.containsKey(wordLower)) {
                    set.retainAll(new HashSet<>(map.get(wordLower)));
                }
            }
        }

        if (!set.isEmpty()) {
            for (int index : set) {
                foundTargets.add(lines.get(index));
            }
            return new ArrayList<>(foundTargets);
        }

        System.out.println("No matching people found.");
        System.out.println();
        return null;
    }

    public List<String> matchAny(String target) {

        Set<String> foundTargets = new HashSet<>();

        String[] targetParts = target.split(" ");
        boolean foundMatch = false;
        for (String word : targetParts) {
            String wordLower = word.toLowerCase();
            if (map.containsKey(wordLower)) {
                foundMatch = true;
                for (int index : map.get(wordLower)) {
                    foundTargets.add(lines.get(index));
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

    public List<String> matchNone(String target) {

        Set<String> foundTargets = new HashSet<>(lines);
        foundTargets.removeAll(matchAny(target));

        if (!foundTargets.isEmpty()) {
            return new ArrayList<>(foundTargets);
        }

        System.out.println("No unmatching lines found.");
        System.out.println();
        return null;
    }

}
