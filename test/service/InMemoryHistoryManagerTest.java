package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    Task task = new Task("Task", "Description", Status.NEW);
    @Test
    void shouldBeNotNullAfterAddTask() {
        historyManager.add(task);
        assertNotNull(historyManager.getHistory(), "История должна быть не пустой после" +
                " добавления");
    }

    @Test
    void shouldBeFalseWhenRemove() {
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertFalse(historyManager.getHistory().contains(task));
    }
}
