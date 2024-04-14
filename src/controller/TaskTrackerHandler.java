package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.HistoryManager;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class TaskTrackerHandler {
    Gson gson;
    ErrorHandler errorHandler;;
    HistoryManager historyManager;
    TaskManager taskManager;

    protected TaskTrackerHandler() {
    }

    public TaskTrackerHandler(Gson gson, HistoryManager historyManager, TaskManager taskManager) {
        this.gson = gson;
        this.historyManager = historyManager;
        this.taskManager = taskManager;
        this.errorHandler = new ErrorHandler();
    }

    public static void writeResponse(HttpExchange httpExchange, String response, int responseCode) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(responseCode, 0);
            os.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            httpExchange.close();
            System.out.println(e.getMessage());
        }
    }
}
