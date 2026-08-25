public class SubmissionRegistry {

    private class Node {
        Submission data;
        Node next;

        public Node(Submission data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node[] table;
    private int size;
    private final double LOAD_FACTOR = 0.75;

    public SubmissionRegistry(int capacity) {
        table = new Node[capacity];
        size = 0;
    }

    private int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    public void put(Submission s) {
        if ((double) size / table.length >= LOAD_FACTOR) {
            resize();
        }

        int index = hash(s.getStudentId());
        Node current = table[index];

        while (current != null) {
            if (current.data.getStudentId().equals(s.getStudentId())) {
                current.data = s;
                return;
            }
            current = current.next;
        }

        Node newNode = new Node(s);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    public Submission lookup(String studentId) {
        int index = hash(studentId);
        Node current = table[index];

        while (current != null) {
            if (current.data.getStudentId().equals(studentId)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    public int updateVersion(String studentId, String fileName, int sizeKb, long timestampMs) {
        Submission s = lookup(studentId);
        if (s != null) {
            s.replaceFile(fileName, sizeKb, timestampMs);
            return s.getVersion();
        }
        return -1;
    }

    public int size() {
        return size;
    }

    private void resize() {
        Node[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;

        for (int i = 0; i < oldTable.length; i++) {
            Node current = oldTable[i];
            while (current != null) {
                put(current.data);
                current = current.next;
            }
        }
    }
}