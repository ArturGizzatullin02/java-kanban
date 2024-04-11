package model;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SubTaskTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager, "rsc/tasks.csv");
    Epic epic1 = taskManager.createEpic(new Epic("Task1", "Description1", Status.NEW));
    SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask", "Descr", Status.NEW, epic1.getId()
            , Duration.ofMinutes(90)
            , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));
    Epic epic2 = taskManager.createEpic(new Epic("Task2", "Description2", Status.NEW));
    SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask", "Descr", Status.NEW, epic2.getId()
            , Duration.ofMinutes(90)
            , LocalDateTime.of(2024, 4, 17, 17, 16, 0)));

    @Test
    public void shouldBeNotEqualIfEpicsNotEqual() {
        assertNotEquals(subTask1, subTask2, "Сабтаски(даже, если все остальное совпадает) с разными эпиками не должны совпадать");
    }
}
