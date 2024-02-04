package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    Epic epic1 = new Epic();
    Epic epic2 = new Epic();
    @Test
    public void shouldBeEqualIfIdEqual() {
        assertEquals(epic1, epic2, "Эпики должны совпадать");
    }
}
