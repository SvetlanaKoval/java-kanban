package httpserver;

import com.sun.net.httpserver.HttpServer;
import handlers.EpicHandler;
import handlers.HistoryHandler;
import handlers.PrioritizedHandler;
import handlers.SubtaskHandler;
import handlers.TaskHandler;
import managers.Managers;
import managers.TaskManager;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

    public void startServer() {
        TaskManager taskManager = Managers.getDefault();
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epic", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start();
    }

    public void stopServer() {
        httpServer.stop(1);
    }
}
