package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();


    @Test
    public void shouldBeNotNullAfterAdd() {
        Task task = new Task("Task", "Description", Status.NEW);
        historyManager.add(task);
        assertNotNull(historyManager.getHistory(), "История не должна быть пустой после добавления");
    }

    @Test
    public void shouldBeNullIfRemove() throws IOException {
        File testFile = File.createTempFile("temp", ".csv");
        TaskManager taskManager = Managers.getDefaultTaskManager(historyManager, testFile.getAbsolutePath());
        Task task = taskManager.createTask(new Task("Task", "Description", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Task2", "Description", Status.NEW));
        Task task3 = taskManager.createTask(new Task("Task3", "Description", Status.NEW));
        Task task4 = taskManager.createTask(new Task("Task4", "Description", Status.NEW));
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.remove(task.getId());
        historyManager.remove(task2.getId());
        historyManager.remove(task3.getId());
        historyManager.remove(task4.getId());
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой после удаления через remove");
    }

    @Test
    public void shouldBeEmptyIfClear() {
        Task task = new Task("Task", "Description", Status.NEW);
        historyManager.add(task);
        historyManager.clear();
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой после очищения");
    }

    @Test
    public void shouldBeNotEmpty() {
        Task task = new Task("Task", "Description", Status.NEW);
        historyManager.add(task);
        assertFalse(historyManager.getHistory().isEmpty(), "История не должна быть пустой, если есть задачи");
    }
}
