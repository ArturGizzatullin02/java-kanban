package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager);

    @Test
    void shouldBeEqualWhenSetId() {
        Task task = taskManager.createTask(new Task());
        int id = 10;
        task.setId(id);
        assertEquals(id, task.getId(), "getId должен выдавать верный id после изменения через сеттер");
    }

    @Test
    void shouldBeCreateAndSearchAnyType() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Description", Status.NEW));
        Task task = taskManager.createTask(new Task("Task", "Description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("SubTask", "Description", Status.NEW,
                epic.getId()));
        int epicId = epic.getId();
        assertEquals(taskManager.getAllEpics().get(epicId).getId(), epicId, "ID эпиков должны " +
                "совпадать при поиске");
    }

    @Test
    public void test() throws IOException {
        File emptyFile = File.createTempFile("test", ".csv");
        File notEmptyFile = new File("C:\\Users\\1\\IdeaProjects\\java-kanban\\rsc\\tasks.csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(notEmptyFile);
        assertNotNull(fileBackedTaskManager, "Менеджер должен быть инициализирован");
    }
}
