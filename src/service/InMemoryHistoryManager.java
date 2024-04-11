package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    final Map<Integer, Node> history = new HashMap<>();

    Node head;
    Node tail;

    public static class Node {
        Node next;
        Node prev;
        Task task;

        public Node(Node next, Task task, Node prev) {
            this.next = next;
            this.task = task;
            this.prev = prev;
        }
    }

    private void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(null, task, oldTail);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = tail;
        }
    }

    @Override
    public void add(Task task) {
        linkLast(task);
        history.put(task.getId(), tail);
    }

    private void removeNode(Node node) {
        if (node.next == null && node.prev == null) {
            tail = null;
            head = null;
        } else if (node.next == null) {
            Node prev = node.prev;
            prev.next = null;
            tail = prev;
        } else if (node.prev == null) {
            Node next = node.next;
            next.prev = null;
            head = node;
        } else {
            Node next = node.next;
            Node prev = node.prev;
            next.prev = prev;
            prev.next = next;
        }
    }

    @Override
    public void remove(int id) {
        if (!history.isEmpty()) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public void clear() {
        history.clear();
    }

    @Override
    public List<Integer> getHistory() {
        return new ArrayList<>(history.keySet());
    }
}
