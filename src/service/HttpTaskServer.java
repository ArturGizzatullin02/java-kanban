package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import controller.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        httpServer.createContext("/subtasks", new SubTaskHandler(gson, historyManager, taskManager));
        httpServer.createContext("/epics", new EpicHandler(gson, historyManager, taskManager));
        httpServer.createContext("/history", new HistoryHandler(gson, historyManager, taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(gson, historyManager, taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер на порту " + PORT + " запущен");
    }
    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер на порту " + PORT + " остановлен");
    }
    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}
