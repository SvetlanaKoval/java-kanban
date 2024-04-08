package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import tasks.Task;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class TaskHandler extends AbstractHttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        Gson gson = new Gson();
        String response;
        try {
            switch (method) {
                case "POST":
                    Task task = getTask(exchange);
                    if (task.getId() == null) {
                        taskManager.addTask(task);
                        response = "Задача успешно создана!";
                    } else {
                        taskManager.updateTask(task);
                        response = "Задача успешно обновлена!";
                    }
                    writeResponse(exchange, 201, response);
                    break;

                case "GET":
                    Optional<Integer> taskId = getTaskId(exchange);
                    if (taskId.isEmpty()) {
                        response = gson.toJson(taskManager.getAllTasks());
                        writeResponse(exchange, 200, response);
                        break;
                    }
                    response = gson.toJson(taskManager.getTaskById(taskId.get()));
                    writeResponse(exchange, 200, response);
                    break;

                case "DELETE":
                    Task deletedTask = getTask(exchange);
                    response = gson.toJson(taskManager.removeTaskById(deletedTask.getId()));
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

    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathInf = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathInf[2]));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        } catch (NumberFormatException e) {
            throw new RuntimeException();
        }
    }

    private Task getTask(HttpExchange exchange) throws IOException {
        String strTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        Optional<Task> optional = Optional.of(new Gson().fromJson(strTask, Task.class));
        if (optional.isEmpty()) {
            throw new RuntimeException("Задача не найдена");
        }
        return optional.get();
    }

    private void writeResponse(HttpExchange exchange, int code, String response) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        if (response == null) {
            exchange.getResponseBody().close();
            return;
        }

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
