package model;

import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EpicTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    TaskManager taskManager = Managers.getDefaultTaskManager(historyManager);
    Epic epic1 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
    SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "Descr",
            Status.NEW, epic1.getId()));
    Epic epic2 = taskManager.createEpic(new Epic("Task", "Description", Status.NEW));
    SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask2", "Descr",
            Status.NEW, epic2.getId()));

    @Test
    public void shouldBeNotEqualIfSubTaskNotEqual() {
        assertNotEquals(epic1, epic2, "Эпики(даже, если все остальное совпадает) с " +
                "разными сабтасками не должны совпадать");
    }

    @Test
    public void shouldBeFalseWhenSubTaskRemoved() {
        int subTaskId = subTask1.getId();
        taskManager.deleteSubtaskById(subTaskId);
        assertFalse(epic1.getSubTasksId().contains(subTaskId), "Список подзадач эпика не " +
                "должен содержать подзадачу после удаления");
    }
}
