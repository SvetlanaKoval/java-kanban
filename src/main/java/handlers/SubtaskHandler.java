package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tasks.Subtask;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class SubtaskHandler extends AbstractHttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Gson gson = new Gson();
        String response;
        try {
            switch (method) {
                case "POST":
                    Subtask subtask = getSubtask(exchange);
                    if (subtask.getId() == null) {
                        taskManager.addSubtask(subtask);
                        response = "Подзадача успешно создана!";
                    } else {
                        taskManager.updateSubtask(subtask);
                        response = "Подзадача успешно обновлена!";
                    }
                    writeResponse(exchange, 201, response);
                    break;

                case "GET":
                    Optional<Integer> subtaskId = getSubtaskId(exchange);
                    if (subtaskId.isEmpty()) {
                        response = gson.toJson(taskManager.getAllSubtasks());
                        writeResponse(exchange, 200, response);
                        break;
                    }
                    response = gson.toJson(taskManager.getSubtaskById(subtaskId.get()));
                    writeResponse(exchange, 200, response);
                    break;

                case "DELETE":
                    Subtask deletedSubtask = getSubtask(exchange);
                    response = gson.toJson(taskManager.removeSubtaskById(deletedSubtask.getId()));
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

    private Optional<Integer> getSubtaskId(HttpExchange exchange) {
        String[] pathInf = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathInf[2]));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        } catch (NumberFormatException e) {
            throw new RuntimeException();
        }
    }

    private Subtask getSubtask(HttpExchange exchange) throws IOException {
        String strTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Optional<Subtask> optional = Optional.of(new Gson().fromJson(strTask, Subtask.class));
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
