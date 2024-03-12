package service;

import model.Task;

import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    final Map<Integer, Node> history= new HashMap<>();

    Node head;
    Node tail;
    public static class Node {
        Node next;
        Node prev;
        Task task;

        public Node(Node next, Task task, Node prev) {
            this.next = next;
            this.task = task;
            this.prev  = prev;
        }
    }

    private void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null);
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
        Node next = node.next;
        Node prev = node.prev;
        next.prev = prev;
        prev.next = next;
        history.remove(node.task.getId());
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    @Override
    public void clear() {
        history.clear();
    }
    @Override
    public Map<Integer, Node> getHistory() {
        return history;
    }
}
