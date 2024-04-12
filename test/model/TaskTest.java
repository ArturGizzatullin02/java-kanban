package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    Task task1 = new Task("task", "descr", Status.NEW);
    Task task2 = new Task("task", "descr", Status.NEW);

    @Test
    public void shouldBeEqualIfIdEqual() {
        assertEquals(task1, task2, "Задачи должны совпадать");
    }
}
