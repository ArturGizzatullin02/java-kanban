//package controller;
//
//import com.google.gson.Gson;
//import com.sun.net.httpserver.HttpHandler;
//
//public class HistoryHandler extends TaskTrackerHandler implements HttpHandler {
//    Gson gson;
//    ErrorHandler errorHandler;
//    HistoryManager historyManager;
//    TaskManager taskManager;
//
//    public HistoryHandler(Gson gson, HistoryManager historyManager, TaskManager taskManager) {
//        this.gson = gson;
//        this.historyManager = historyManager;
//        this.taskManager = taskManager;
//        errorHandler = new ErrorHandler();
//    }
//}
