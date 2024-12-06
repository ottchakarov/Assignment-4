public class SeparateChainingHashTable {
    private Node[] table;
    private int M;
    private boolean useOldHash; // determines which hash function to use

    public SeparateChainingHashTable(int M, boolean useOldHash) {
        this.M = M;
        this.useOldHash = useOldHash;
        this.table = new Node[M];
    }

    private int hash(String key) {
        int h = useOldHash ? HashFunctions.oldHashCode(key) : HashFunctions.currentHashCode(key);
        return (h & 0x7fffffff) % M;
    }

    public void put(String key, int value) {
        int i = hash(key);
        for (Node x = table[i]; x != null; x = x.next) {
            if (x.key.equals(key)) {
                x.value = value;
                return;
            }
        }
        table[i] = new Node(key, value, table[i]);
    }

    // returns the value if found, else -1.
    // comparisons.value records the number of comparisons made
    public int get(String key, IntWrapper comparisons) {
        int i = hash(key);
        Node x = table[i];
        while (x != null) {
            comparisons.value++;
            if (x.key.equals(key)) {
                return x.value;
            }
            x = x.next;
        }
        return -1;
    }
}
