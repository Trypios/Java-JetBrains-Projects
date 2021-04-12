package phonebook;

/**
 * Personal implementation of a HashMap.
 */
public class HashTable {

    private static class TableEntry {

        private final String key;
        private final String value;

        public TableEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }

    private final int size;
    private TableEntry[] table;
    private int idx = 0;

    public HashTable(int size) {
        this.size = size;
        table = new TableEntry[size];
    }

    private int findKeyIndex(String key) {

        int hash = idx % size;
        while (!(table[hash] == null || table[hash].getKey().equals(key))) {
            hash = (hash + 1) % size;
            if (hash == idx % size) {
                return -1;
            }
        }
        idx++;
        return hash;
    }

    public boolean put(String key, String value) {

        int i = findKeyIndex(key);
        if (i == -1) {
            return false;
        }
        table[i] = new TableEntry(key, value);
        return true;
    }

    public String get(String key) {

        int i = findKeyIndex(key);
        if (i == -1 || table[i] == null) {
            return null;
        }
        return table[i].getValue();
    }


    public int length() {

        return size;
    }

    public boolean containsKey(String key) {
        return findKeyIndex(key) != -1;
    }

    @Override
    public String toString() {

        StringBuilder tableStringBuilder = new StringBuilder();

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                tableStringBuilder.append("null");
            } else {
                tableStringBuilder.append(table[i].getValue())
                                    .append(" ")
                                    .append(table[i].getKey());
            }

            if (i < table.length - 1) {
                tableStringBuilder.append("\n");
            }
        }

        return tableStringBuilder.toString();
    }

}
