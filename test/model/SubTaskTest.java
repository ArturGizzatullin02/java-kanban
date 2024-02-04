package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {
    SubTask subTask1 = new SubTask();
    SubTask subTask2 = new SubTask();
    @Test
    public void shouldBeEqualIfIdEqual() {
        assertEquals(subTask1, subTask2, "Подзадачи должны совпадать");
    }
}
