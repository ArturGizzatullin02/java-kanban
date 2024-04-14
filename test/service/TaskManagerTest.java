package service;

import exception.NotFoundException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    File testFile = File.createTempFile("temp", ".csv");
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager, testFile.getAbsolutePath());

    TaskManagerTest() throws IOException {
    }

    @Test
    void shouldBeEqualWhenSetId() {
        Task task = taskManager.createTask(new Task("task", "descr", Status.NEW));
        int id = 10;
        task.setId(id);
        assertEquals(id, task.getId(), "getId должен выдавать верный id после изменения через сеттер");
    }

    @Test
    public void shouldBeNotNullTask() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        assertNotNull(task);
    }

    @Test
    public void shouldBeNotNullEpic() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        assertNotNull(epic);
    }

    @Test
    public void shouldBeNotNullSubTask() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("name", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 23, 10)));
        assertNotNull(subTask);
    }

    @Test
    public void shouldBeNotEmptyTasksList() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        Task task2 = taskManager.createTask(new Task("name2", "description", Status.NEW));
        Task task3 = taskManager.createTask(new Task("name3", "description", Status.NEW));
        assertNotNull(taskManager.getAllTasks(), "Список задач должен корректно возвращаться");
    }

    @Test
    public void shouldBeNotEmptySubTasksList() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("name", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 23, 10)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("name2", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 10, 10)));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("name3", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 15, 10)));
        assertNotNull(taskManager.getAllSubtasks(), "Список подзадач должен корректно возвращаться");
    }

    @Test
    public void shouldBeNotEmptyEpicsList() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        Epic epic2 = taskManager.createEpic(new Epic("epic2", "description", Status.NEW));
        Epic epic3 = taskManager.createEpic(new Epic("epic3", "description", Status.NEW));
        assertNotNull(taskManager.getAllEpics(), "Список эпиков должен корректно возвращаться");
    }

    @Test
    public void shouldBeNotNullByGettingTask() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        assertNotNull(taskManager.getTaskById(task.getId()), "Задача должна корректно возвращаться методом getById");
    }

    @Test
    public void shouldBeNotNullByGettingEpic() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        assertNotNull(taskManager.getEpicById(epic.getId()), "Эпик должен корректно возвращаться методом getById");
    }

    @Test
    public void shouldBeNotNullByGettingSubTask() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("name", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 23, 10)));
        assertNotNull(taskManager.getSubTaskById(subTask.getId()), "Сабтаск должен корректно возвращаться методом getById");
    }

    @Test
    public void shouldBeEqualsWhenUpdatedTask() {
        Task task = taskManager.createTask(new Task("Новая задача", "описание", Status.NEW, Duration.ofMinutes(30)
        , LocalDateTime.of(2024, 4, 11, 12, 30 ,0)));
        Task taskFromManager = taskManager.getTaskById(task.getId());
        taskFromManager.setName("New name");
        taskManager.updateTask(taskFromManager);
        assertEquals(taskManager.getTaskById(task.getId()), taskManager.getTaskById(taskFromManager.getId()));
    }

    @Test
    public void shouldBeEqualsWhenUpdatedSubTask() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("Новая задача", "описание", Status.NEW
                , epic.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 11, 23, 0, 0)));
        SubTask subTaskFromManager = taskManager.getSubTaskById(subTask.getId());
        subTaskFromManager.setName("New name");
        taskManager.updateSubTask(subTaskFromManager);
        assertEquals(taskManager.getSubTaskById(subTask.getId()), taskManager.getSubTaskById(subTaskFromManager.getId()));
    }

    @Test
    public void shouldBeEqualsWhenUpdatedEpic() {
        Epic epic = taskManager.createEpic(new Epic("Новая задача", "описание", Status.NEW));
        Epic epicFromManager = taskManager.getEpicById(epic.getId());
        epicFromManager.setName("New name");
        taskManager.updateEpic(epicFromManager);
        assertEquals(taskManager.getEpicById(epic.getId()), taskManager.getEpicById(epicFromManager.getId()));
    }

    @Test
    public void shouldBeNullTaskAfterDeleteById() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        taskManager.deleteTaskById(task.getId());
        assertThrows(NotFoundException.class, () -> {
            taskManager.getTaskById(task.getId());
        }, "Удаление задачи должно выбросить исключении при последующем поиске этой задачи");
    }

    @Test
    public void shouldBeNullEpicAfterDeleteById() {
        Epic epic = taskManager.createEpic(new Epic("name", "description", Status.NEW));
        taskManager.deleteEpicById(epic.getId());
        assertThrows(NotFoundException.class, () -> {
            taskManager.getEpicById(epic.getId());
        }, "Удаление задачи должно выбросить исключении при последующем поиске этой задачи");
    }

    @Test
    public void shouldBeNullSubTaskAfterDeleteById() {
        Epic epic = taskManager.createEpic(new Epic("name", "description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("name", "description", Status.NEW
                , epic.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 12, 12, 12, 0, 0)));
        taskManager.deleteSubtaskById(subTask.getId());
        assertThrows(NotFoundException.class, () -> {
            taskManager.getTaskById(subTask.getId());
        }, "Удаление задачи должно выбросить исключении при последующем поиске этой задачи");
    }

    @Test
    public void shouldBeEmptyTasksListAfterDeleteAllTasks() {
        Task task = taskManager.createTask(new Task("name", "description", Status.NEW));
        Task task2 = taskManager.createTask(new Task("name2", "description", Status.NEW));
        Task task3 = taskManager.createTask(new Task("name3", "description", Status.NEW));
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Список задач должен быть пустым после удаления всех задач");
    }

    @Test
    public void shouldBeEmptySubTasksListAfterDeleteAllSubTasks() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        SubTask subTask = taskManager.createSubTask(new SubTask("name", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 23, 10)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("name2", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 10, 10)));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("name3", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 15, 10)));
        taskManager.deleteAllSubTasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Список подзадач должен быть пустым после удаления всех задач");
    }

    @Test
    public void shouldBeEmptyEpicListAfterDeleteAllEpics() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        Epic epic2 = taskManager.createEpic(new Epic("epic2", "description", Status.NEW));
        Epic epic3 = taskManager.createEpic(new Epic("epic3", "description", Status.NEW));
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Список эпиков должен быть пустым после удаления всех задач");
    }

    @Test
    public void shouldBeTrueIfGetAllSubTasksByEpic() {
        Epic epic = taskManager.createEpic(new Epic("epic", "description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("name", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 23, 10)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("name2", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 10, 10)));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("name3", "description", Status.NEW, epic.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 12, 4, 15, 10)));
        for (SubTask subTask : taskManager.getAllSubTasksByEpic(epic)) {
            assertEquals(epic.getId(), subTask.getEpicId(), "Метод должен возвращать подзадачи, которые все привязаны " +
                    "к одному эпику");
        }
    }
}
