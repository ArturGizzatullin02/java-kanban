package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.HistoryManager;
import service.TaskManager;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
public class TaskHandler extends TaskTrackerHandler implements HttpHandler {
    public TaskHandler(Gson gson, HistoryManager historyManager, TaskManager taskManager) {
        super(gson, historyManager, taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            try {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String path = uri.getPath();
                String[] splitPath = path.split("/");
                int uriLengthWithId = 3;
                int uriLengthWithoutId = 2;
                if (splitPath.length == uriLengthWithId) {
                    int id = Integer.parseInt(splitPath[2]);
                    if (method.equals("GET")) {
                        writeResponse(exchange, gson.toJson(taskManager.getTaskById(id)), 200);
                    } else if (method.equals("DELETE")) {
                        taskManager.deleteTaskById(id);
                        writeResponse(exchange, gson.toJson("Задача с id " + id + " удалена"), 204);
                    }
                } else if (splitPath.length == uriLengthWithoutId) {
                    if (method.equals("GET")) {
                        writeResponse(exchange, gson.toJson(taskManager.getAllTasks()), 200);
                    } else if (method.equals("POST")) {
                        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        String[] splitBody = requestBody.split("\n");
                        if (splitBody[1].startsWith("\t\"id\"")) {
                            int id = Integer.parseInt(splitBody[1].substring(7, splitBody[1].length() - 1));
                            taskManager.updateTask(gson.fromJson(requestBody, Task.class));
                            writeResponse(exchange, "Задача с id " + id + " обновлена", 200);
                        } else {
                            Task task = gson.fromJson(requestBody, Task.class);
                            taskManager.createTask(task);
                            writeResponse(exchange, "Задача с id " + task.getId() +" успешно создана", 201);
                        }
                    }
                }
            } catch (Exception e) {
                errorHandler.handle(exchange, e);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
