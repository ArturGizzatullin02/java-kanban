package service;

import model.Task;

import java.util.Map;
import service.InMemoryHistoryManager.Node;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    void clear();
    Map<Integer, Node> getHistory();
}
