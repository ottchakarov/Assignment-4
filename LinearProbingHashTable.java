public class LinearProbingHashTable {
    private String[] keys;
    private int[] values;
    private int M;
    private boolean useOldHash;

    public LinearProbingHashTable(int M, boolean useOldHash) {
        this.M = M;
        this.useOldHash = useOldHash;
        keys = new String[M];
        values = new int[M];
    }

    private int hash(String key) {
        int h = useOldHash ? HashFunctions.oldHashCode(key) : HashFunctions.currentHashCode(key);
        return (h & 0x7fffffff) % M;
    }

    public void put(String key, int value) {
        int i = hash(key);
        while (keys[i] != null) {
            if (keys[i].equals(key)) {
                values[i] = value;
                return;
            }
            i = (i + 1) % M;
        }
        keys[i] = key;
        values[i] = value;
    }

    public int get(String key, IntWrapper comparisons) {
        int i = hash(key);
        while (keys[i] != null) {
            comparisons.value++;
            if (keys[i].equals(key)) {
                return values[i];
            }
            i = (i + 1) % M;
        }
        return -1;
    }
}
