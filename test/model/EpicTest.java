package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    Epic epic1 = new Epic("Task1", "Description1", Status.NEW);
    Epic epic2 = new Epic("Task1", "Description1", Status.NEW);
    @Test
    public void shouldBeEqualIfIdEqual() {
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1, epic2, "Одинаковые эпики должны совпадать");
    }
}
