package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import service.HistoryManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends TaskTrackerHandler implements HttpHandler {
    public EpicHandler(Gson gson, HistoryManager historyManager, TaskManager taskManager) {
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
                if (splitPath.length >= uriLengthWithId) {
                    int id = Integer.parseInt(splitPath[2]);
                    if (method.equals("GET")) {
                        if (splitPath.length == uriLengthWithId) {
                            writeResponse(exchange, gson.toJson(taskManager.getEpicById(id)), 200);
                        } else {
                            writeResponse(exchange, gson.toJson(taskManager.getAllSubTasksByEpic(taskManager.getEpicById(id))), 200);
                        }
                    } else if (method.equals("DELETE")) {
                        taskManager.deleteEpicById(id);
                        writeResponse(exchange, gson.toJson("Эпик с id " + id + " удален"), 204);
                    }
                } else if (splitPath.length == uriLengthWithoutId) {
                    if (method.equals("GET")) {
                        writeResponse(exchange, gson.toJson(taskManager.getAllEpics()), 200);
                    } else if (method.equals("POST")) {
                        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Epic epic = gson.fromJson(requestBody, Epic.class);
                            taskManager.createEpic(epic);
                            writeResponse(exchange, "Эпик успешно создан", 201);
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
