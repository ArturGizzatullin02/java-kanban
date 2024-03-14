package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager);

    @Test
    void shouldBeEqualWhenSetId() {
        Task task = taskManager.create(new Task());
        int id = 10;
        task.setId(id);
        assertEquals(id, task.getId(), "getId должен выдавать верный id после изменения через сеттер");
    }

    @Test
    void shouldBeCreateAndSearchAnyType() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Description", Status.NEW));
        Task task = taskManager.create(new Task("Task", "Description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("SubTask", "Description", Status.NEW,
                epic.getId()));
        int epicId = epic.getId();
        assertEquals(taskManager.getAllEpics().get(epicId).getId(), epicId, "ID эпиков должны " +
                "совпадать при поиске");
    }
}
