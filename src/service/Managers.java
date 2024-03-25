package service;

public class Managers {
    public static FileBackedTaskManager getDefaultTaskManager(HistoryManager historyManager) {
        return new FileBackedTaskManager(historyManager);
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
