package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.HistoryManager;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends TaskTrackerHandler implements HttpHandler {
    public PrioritizedHandler(Gson gson, HistoryManager historyManager, TaskManager taskManager) {
        super(gson, historyManager, taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            try {
                String method = exchange.getRequestMethod();
                if (method.equals("GET")) {
                    writeResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
                }
            } catch (Exception e) {
                errorHandler.handle(exchange, e);
            }
        } catch (Exception e) {
            errorHandler.handle(exchange, e);
        }
    }
}
