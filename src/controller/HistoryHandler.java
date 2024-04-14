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

public class HistoryHandler extends TaskTrackerHandler implements HttpHandler {
    Gson gson;
    ErrorHandler errorHandler;
    HistoryManager historyManager;
    TaskManager taskManager;

    public HistoryHandler(Gson gson, HistoryManager historyManager, TaskManager taskManager) {
        this.gson = gson;
        this.historyManager = historyManager;
        this.taskManager = taskManager;
        errorHandler = new ErrorHandler();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            try {
                String method = exchange.getRequestMethod();
                if (method.equals("GET")) {
                    writeResponse(exchange, gson.toJson(historyManager.getHistory()), 200);
                }
            } catch (Exception e) {
                errorHandler.handle(exchange, e);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
