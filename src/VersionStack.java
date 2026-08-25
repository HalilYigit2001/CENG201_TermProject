import java.util.NoSuchElementException;

public class VersionStack {
    private class Node {
        VersionRecord data;
        Node next;

        public Node(VersionRecord data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node top;
    private int size;

    public VersionStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(VersionRecord v) {
        Node newNode = new Node(v);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public VersionRecord pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty! No earlier version to rollback.");
        }
        VersionRecord record = top.data;
        top = top.next;
        size--;
        return record;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }
}