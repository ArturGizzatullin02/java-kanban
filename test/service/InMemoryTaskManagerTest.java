package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager, "rsc/tasks.csv");

    InMemoryTaskManagerTest() throws IOException {
    }

    @Test
    void shouldBeEqualWhenSetId() {
        Task task = taskManager.createTask(new Task("task", "descr", Status.NEW));
        int id = 10;
        task.setId(id);
        assertEquals(id, task.getId(), "getId должен выдавать верный id после изменения через сеттер");
    }

    @Test
    void shouldBeCreateAndSearchAnyType() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Description", Status.NEW));
        Task task = taskManager.createTask(new Task("Task", "Description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("SubTask", "Description", Status.NEW,
                epic.getId(), Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        int epicId = epic.getId();
        assertEquals(taskManager.getAllEpics().get(epicId).getId(), epicId, "ID эпиков должны " +
                "совпадать при поиске");
    }

}
