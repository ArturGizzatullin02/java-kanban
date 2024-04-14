package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.ManagerSaveException;
import exception.NotFoundException;
import exception.ValidationException;

import java.io.IOException;
import java.io.OutputStream;
import static controller.TaskTrackerHandler.writeResponse;

public class ErrorHandler implements HttpHandler {
    public ErrorHandler() {
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(400, 0);
        }
        exchange.close();
    }

    public void handle(HttpExchange exchange, ManagerSaveException e) throws IOException {
        writeResponse(exchange, e.getMessage(), 400);
    }

    public void handle(HttpExchange exchange, NotFoundException e) throws IOException {
        writeResponse(exchange, e.getMessage(), 404);
    }

    public void handle(HttpExchange exchange, ValidationException e) throws IOException {
        writeResponse(exchange, e.getMessage(), 406);
    }

    public void handle(HttpExchange exchange, Exception e) throws IOException {
        writeResponse(exchange, e.getMessage(), 500);
    }
}
