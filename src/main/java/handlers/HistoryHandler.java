package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class HistoryHandler extends AbstractHttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response;
        if ("GET".equals(method)) {
            response = new Gson().toJson(taskManager.getHistory());
            exchange.sendResponseHeaders(200, 0);
        } else {
            response = "Метод " + method + " не доступен!";
            exchange.sendResponseHeaders(405, 0);
        }

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
