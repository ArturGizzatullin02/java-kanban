package model;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EpicTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager, "rsc/tasks.csv");


    @Test
    public void shouldBeNotEqualIfSubTaskNotEqual() {
        Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr", Status.NEW, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        Epic epic2 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr", Status.NEW, epic2.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        assertNotEquals(epic1, epic2, "Эпики(даже, если все остальное совпадает) с разными сабтасками не должны совпадать");
    }

    @Test
    public void shouldBeNew() {
        Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr", Status.NEW, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr", Status.NEW, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 20, 16, 0)));
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    public void shouldBeDone() {
        Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr", Status.DONE, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr", Status.DONE, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 20, 16, 0)));
        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    public void shouldBeInProgressIfDoneAndNew() {
        Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr", Status.DONE, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr", Status.NEW, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 20, 16, 0)));
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void shouldBeInProgressIfDoneAndInProgress() {
        Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr", Status.DONE, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr", Status.IN_PROGRESS, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 20, 16, 0)));
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void shouldBeInProgressIfInProgressAndNew() {
        Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr", Status.IN_PROGRESS, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr", Status.NEW, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 20, 16, 0)));
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void shouldBeInProgressIfInProgress() {
        Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr", Status.IN_PROGRESS, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr", Status.IN_PROGRESS, epic1.getId()
                , Duration.ofMinutes(90)
                , LocalDateTime.of(2024, 4, 17, 20, 16, 0)));
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }
}
