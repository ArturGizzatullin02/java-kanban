package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    final static int PORT = 8080;
    private static HttpServer httpServer;
    Gson gson;
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);
    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
        httpServer.createContext("/tasks", new TaskHandler(gson, historyManager, taskManager));
//        httpServer.createContext("/subtasks", new SubTaskHandler(gson));
//        httpServer.createContext("/epics", new EpicHandler(gson));
//        httpServer.createContext("/history", new HistoryHandler(gson));
//        httpServer.createContext("/prioritized", new PrioritizedHandler(gson));
    }

    public static void start() {
        httpServer.start();
        System.out.println("Сервер на порту " + PORT + " запущен");
    }
    public static void stop() {
        httpServer.stop(0);
        System.out.println("Сервер на порту " + PORT + " остановлен");
    }
    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer();
            start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
