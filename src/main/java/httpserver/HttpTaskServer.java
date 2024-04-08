package httpserver;

import com.sun.net.httpserver.HttpServer;
import handlers.AbstractHttpHandler;
import handlers.EpicHandler;
import handlers.HistoryHandler;
import handlers.PrioritizedHandler;
import handlers.SubtaskHandler;
import handlers.TaskHandler;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.createContext("/epic", new EpicHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

    public void startServer() {
        httpServer.start();
    }

    public void stopServer() {
        AbstractHttpHandler.clearManager();
        httpServer.stop(1);
    }
}
