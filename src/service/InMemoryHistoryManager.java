package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    final Map<Integer, Node> history= new HashMap<>();

    Node head;
    Node tail;
    public static class Node {
        static Node next;
        static Node prev;
        static Task task;

        public Node(Node next, Task task, Node prev) {
            this.next = next;
            this.task = task;
            this.prev  = prev;
        }
    }

    @Override
    public void linkLast(Task task) {
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

    @Override
    public void removeNode(Node node) {
        Node next = node.next;
        Node prev = node.prev;
        next.prev = prev;
        prev.next = next;
        history.remove(node.task.getId());
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public void clear() {
        history.clear();
    }
    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }
}
