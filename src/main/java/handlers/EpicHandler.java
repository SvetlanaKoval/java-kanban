package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tasks.Epic;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class EpicHandler extends AbstractHttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        Gson gson = new Gson();
        String response;
        try {
            switch (method) {
                case "POST":
                    taskManager.addEpic(getEpic(exchange));
                    response = "Эпик успешно создан";
                    writeResponse(exchange, 201, response);
                    break;

                case "GET":
                    String[] pathInf = exchange.getRequestURI().getPath().split("/");
                    Optional<Integer> epicId = getEpicId(exchange, pathInf);

                    if (epicId.isEmpty()) {
                        response = gson.toJson(taskManager.getAllEpics());
                        writeResponse(exchange, 200, response);
                        break;
                    }
                    int id = epicId.get();

                    Epic epic = taskManager.getEpicById(id);
                    if (exchange.getRequestURI().getPath().contains("subtasks")) {
                        response = gson.toJson(taskManager.getSubtasksByEpic(epic));
                        writeResponse(exchange, 200, response);
                        break;
                    }
                    response = gson.toJson(taskManager.getEpicById(id));
                    writeResponse(exchange, 200, response);
                    break;

                case "DELETE":
                    Epic deletedEpic = getEpic(exchange);
                    response = gson.toJson(taskManager.removeEpicById(deletedEpic.getId()));
                    writeResponse(exchange, 200, response);
                    break;

                default:
                    response = "Метод " + method + " не доступен!";
                    writeResponse(exchange, 405, response);
            }
        } catch (IllegalArgumentException e) {
            writeResponse(exchange, 406, e.getMessage());
        } catch (RuntimeException e) {
            writeResponse(exchange, 404, e.getMessage());
        }
    }

    private Optional<Integer> getEpicId(HttpExchange exchange, String[] pathInf) {
        try {
            return Optional.of(Integer.parseInt(pathInf[2]));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        } catch (NumberFormatException e) {
            throw new RuntimeException();
        }
    }

    private Epic getEpic(HttpExchange exchange) throws IOException {
        String strTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Optional<Epic> optional = Optional.of(new Gson().fromJson(strTask, Epic.class));
        if (optional.isEmpty()) {
            throw new RuntimeException("Задача не найдена");
        }
        return optional.get();
    }

    private void writeResponse(HttpExchange exchange, int code, String response) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        if (!response.isBlank()) {
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
