package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {
    @Test
    void shouldBeNotNullTaskManager() {
        assertNotNull(Managers.getDefaultTaskManager(Managers.getDefaultHistoryManager()), "Метод должен возвращать объект");
    }

    @Test
    void shouldBeNotNullHistoryManager() {
        assertNotNull(Managers.getDefaultHistoryManager(), "Метод должен возвращать объект");
    }
}
