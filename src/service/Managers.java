package service;

public class Managers {
    public static FileBackedTaskManager getDefaultTaskManager(HistoryManager historyManager, String filePath) {
        return new FileBackedTaskManager(historyManager, filePath);
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
