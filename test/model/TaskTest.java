package model;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {
    Task task1 = new Task();
    Task task2 = new Task();
    @Test
    public void shouldBeEqualIfIdEqual() {
        assertEquals(task1, task2, "Задачи должны совпадать");
    }

    @Test
    public void shouldBe() {
        String name = task1.getName();
        task1.setName("Новая задача");
        String newName = task1.getName();
        assertNotEquals(name, newName, "Имя должно успешно меняться через сеттер");
    }
}
