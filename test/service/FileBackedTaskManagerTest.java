package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager, "rsc/tasks.csv");

    FileBackedTaskManagerTest() throws IOException {
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

    @Test
    public void test() throws IOException {
        File emptyFile = File.createTempFile("test", ".csv");
        File notEmptyFile = new File("C:\\Users\\1\\IdeaProjects\\java-kanban\\rsc\\tasks.csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(notEmptyFile);
        assertNotNull(fileBackedTaskManager, "Менеджер должен быть инициализирован");
    }

    @Test
    public void loadTest() {
        try {
            File testFile = File.createTempFile("temp", ".csv");
            TaskManager taskManagerReload = FileBackedTaskManager.loadFromFile(testFile);
            System.out.println("Все задачи тестового менеджера: " + taskManagerReload.getAllTasks());
            taskManagerReload.createTask(new Task("testTask1", "description", Status.NEW));
            taskManagerReload.createTask(new Task("testTask2", "description", Status.NEW));
            taskManagerReload.createTask(new Task("testTask3", "description", Status.NEW));
            System.out.println("Все задачи тестового менеджера: " + taskManagerReload.getAllTasks());
            TaskManager taskManagerReloadFromTestFile = FileBackedTaskManager.loadFromFile(testFile);
            System.out.println("Все задачи нового тестового менеджера, загруженные из тестового файла: "
                    + taskManagerReloadFromTestFile.getAllTasks());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
