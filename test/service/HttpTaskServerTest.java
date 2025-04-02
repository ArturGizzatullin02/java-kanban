package service;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    HttpTaskServer server = new HttpTaskServer();
    HttpClient httpClient;

    public HttpTaskServerTest() throws IOException {
    }

    private HttpResponse<String> createPostRequest(String uriString, String requestBody) throws IOException, InterruptedException {
        URI uri = URI.create(uriString);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, handler);
    }

    private HttpResponse<String> createGetRequest(String uriString) throws IOException, InterruptedException {
        URI uri = URI.create(uriString);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, handler);
    }

    private HttpResponse<String> createDeleteRequest(String uriString) throws IOException, InterruptedException {
        URI uri = URI.create(uriString);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, handler);
    }

    @BeforeEach
    public void start() {
        server.start();
    }

    @AfterEach
    public void stop() {
        server.stop();
    }

    @Test
    public void createTask() throws IOException, InterruptedException {
        HttpResponse<String> response = createPostRequest("http://localhost:8080/tasks", """
                {
                \t"name": "task_1",
                \t"description": "very cool task",
                \t"Status": "NEW",
                \t"duration": 30,
                \t"startTime": "24.12.2024 14:00:00"
                }""");
        assertFalse(server.taskManager.tasks.isEmpty(), "Список задач не должен быть пустым после создания задачи");
        assertEquals(201, response.statusCode(), "Код после успешного создания задачи должен быть 201");
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {
        server.taskManager.createTask(new Task("task", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/tasks/0");
        assertEquals(server.taskManager.getTaskById(0), server.gson.fromJson(response.body(), Task.class));
        assertEquals(200, response.statusCode(), "Код после успешного получения задачи должен быть 200");
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {
        server.taskManager.createTask(new Task("task_1", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        server.taskManager.createTask(new Task("task_2", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 14, 30, 0)));
        server.taskManager.createTask(new Task("task_3", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 16, 30, 0)));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/tasks/");
        assertEquals(server.taskManager.getAllTasks(), server.gson.fromJson(response.body(), new TasksListTypeToken().getType()));
        assertEquals(200, response.statusCode(), "Код после успешного получения задач должен быть 200");
    }

    @Test
    public void deleteTaskById() throws IOException, InterruptedException {
        server.taskManager.createTask(new Task("task", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        HttpResponse<String> response = createDeleteRequest("http://localhost:8080/tasks/0");
        assertTrue(server.taskManager.tasks.isEmpty(), "Список задач должен быть пустым после удаления задачи");
        assertEquals(204, response.statusCode(), "Код после успешного удаления задачи должен быть 204");
    }

    @Test
    public void getSubTasks() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic", "cool epic", Status.NEW));
        server.taskManager.createSubTask(new SubTask("task_1", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        server.taskManager.createSubTask(new SubTask("task_2", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 14, 30, 0)));
        server.taskManager.createSubTask(new SubTask("task_3", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 16, 30, 0)));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/subtasks/");
        assertEquals(server.taskManager.getAllSubtasks(), server.gson.fromJson(response.body(), new SubTasksListTypeToken().getType()));
        assertEquals(200, response.statusCode(), "Код после успешного получения подзадач должен быть 200");
    }

    @Test
    public void getSubTaskById() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic", "cool epic", Status.NEW));
        server.taskManager.createSubTask(new SubTask("task", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/subtasks/1");
        assertEquals(server.taskManager.getSubTaskById(1), server.gson.fromJson(response.body(), SubTask.class));
        assertEquals(200, response.statusCode(), "Код после успешного получения задачи должен быть 200");
    }

    @Test
    public void createSubTask() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic", "cool epic", Status.NEW));
        HttpResponse<String> response = createPostRequest("http://localhost:8080/subtasks", """
                {
                 	"name": "subtask_1",
                 	"description": "very cool subtask for epic 2",
                 	"Status": "NEW",
                 	"epicId": 0,
                 	"duration": 30,
                 	"startTime": "24.12.2024 23:00:00"
                 }""");
        assertFalse(server.taskManager.subTasks.isEmpty(), "Список подзадач не должен быть пустым после создания подзадачи");
        assertEquals(201, response.statusCode(), "Код после успешного создания подзадачи должен быть 201");
    }

    @Test
    public void deleteSubTaskById() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic", "cool epic", Status.NEW));
        server.taskManager.createSubTask(new SubTask("task", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        HttpResponse<String> response = createDeleteRequest("http://localhost:8080/subtasks/1");
        assertTrue(server.taskManager.subTasks.isEmpty(), "Список подзадач должен быть пустым после удаления подзадачи");
        assertEquals(204, response.statusCode(), "Код после успешного удаления подзадачи должен быть 204");
    }

    @Test
    public void createEpic() throws IOException, InterruptedException {
        HttpResponse<String> response = createPostRequest("http://localhost:8080/epics", """
                {
                 	"name": "epic_1",
                 	"description": "very cool epic",
                 	"Status": "NEW"
                 }""");
        assertFalse(server.taskManager.epics.isEmpty(), "Список эпиков не должен быть пустым после создания эпика");
        assertEquals(201, response.statusCode(), "Код после успешного создания эпика должен быть 201");
    }

    @Test
    public void getEpics() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic_1", "cool epic", Status.NEW));
        server.taskManager.createEpic(new Epic("epic_2", "cool epic", Status.NEW));
        server.taskManager.createEpic(new Epic("epic_3", "cool epic", Status.NEW));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/epics/");
        assertEquals(server.taskManager.getAllEpics(), server.gson.fromJson(response.body(), new EpicsListTypeToken().getType()));
        assertEquals(200, response.statusCode(), "Код после успешного получения эпиков должен быть 200");
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic", "cool epic", Status.NEW));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/epics/0");
        assertEquals(server.taskManager.getEpicById(0), server.gson.fromJson(response.body(), Epic.class));
        assertEquals(200, response.statusCode(), "Код после успешного получения задачи должен быть 200");
    }

    @Test
    public void getEpicSubtasks() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic", "cool epic", Status.NEW));
        server.taskManager.createSubTask(new SubTask("task_1", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        server.taskManager.createSubTask(new SubTask("task_2", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 14, 30, 0)));
        server.taskManager.createSubTask(new SubTask("task_3", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 16, 30, 0)));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/epics/0/subtasks");
        assertEquals(server.taskManager.getAllSubTasksByEpic(server.taskManager.getEpicById(0)), server.gson.fromJson(response.body(), new SubTasksListTypeToken().getType()));
        assertEquals(200, response.statusCode(), "Код после успешного получения задачи должен быть 200");
    }

    @Test
    public void deleteEpicById() throws IOException, InterruptedException {
        server.taskManager.createEpic(new Epic("epic", "cool epic", Status.NEW));
        server.taskManager.createSubTask(new SubTask("task", "cool task", Status.NEW, 0, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        HttpResponse<String> response = createDeleteRequest("http://localhost:8080/epics/0");
        assertFalse(server.taskManager.subTasks.containsKey(1), "Подзадача, привязанная к эпику должна удалится при удалении эпика");
        assertFalse(server.taskManager.epics.containsKey(0), "Эпик должен удалиться после метода DELETE");
        assertEquals(204, response.statusCode(), "Код после успешного удаления эпика должен быть 204");
    }

    @Test
    public void getHistory() throws IOException, InterruptedException {
        server.taskManager.createTask(new Task("task_1", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        server.taskManager.createTask(new Task("task_2", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 14, 30, 0)));
        server.taskManager.createTask(new Task("task_3", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 16, 30, 0)));
        server.taskManager.getTaskById(0);
        server.taskManager.getTaskById(2);
        HttpResponse<String> response = createGetRequest("http://localhost:8080/history");
        assertEquals(server.historyManager.getHistory(), server.gson.fromJson(response.body(), new HistoryTypeToken().getType()));
    }

    @Test
    public void getPrioritizedTasks() throws IOException, InterruptedException {
        server.taskManager.createTask(new Task("task_1", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 12, 30, 0)));
        server.taskManager.createTask(new Task("task_2", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 14, 30, 0)));
        server.taskManager.createTask(new Task("task_3", "cool task", Status.NEW, Duration.ofMinutes(30)
                , LocalDateTime.of(2024, 12, 4, 16, 30, 0)));
        HttpResponse<String> response = createGetRequest("http://localhost:8080/prioritized");
        assertEquals(server.taskManager.getPrioritizedTasks(), server.gson.fromJson(response.body(), new PrioritizedTasksTypeToken().getType()));
    }
}
