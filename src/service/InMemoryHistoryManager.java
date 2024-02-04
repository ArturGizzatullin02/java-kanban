package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    final ArrayList<Task> history= new ArrayList<>(10);
    @Override
    public void addToHistory(Task task) {
        if (history.size() == 10) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
        history.add(task);
    }
    @Override
    public ArrayList<Task> getHistory() {
//        for (Task elem : history) {
//            System.out.println(elem.getName());
//        }
        return history;
    }
}
