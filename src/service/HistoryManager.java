package service;

import model.Task;

import java.util.List;
import service.InMemoryHistoryManager.Node;

public interface HistoryManager {
    void add(Task task);
    void removeNode(Node node);
    void remove(int id);
    void linkLast(Task task);
    void clear();
    List<Task> getHistory();
}
