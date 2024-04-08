package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class PrioritizedHandler extends AbstractHttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Gson gson = new Gson();
        String response;
        if ("GET".equals(method)) {
            exchange.sendResponseHeaders(200, 0);
            response = gson.toJson(taskManager.getPrioritizedTasks());
        } else {
            exchange.sendResponseHeaders(405, 0);
            response = "Метод " + method + " не доступен!";
        }

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
