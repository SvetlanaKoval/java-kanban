package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.BadRequestException;
import managers.TaskManager;
import exceptions.TaskNotFoundException;
import exceptions.TimeIntersectionException;
import exceptions.UnsupportedMethodException;
import tasks.Task;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class AbstractHandler implements HttpHandler {

    static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final Gson gson = new Gson();
    protected final TaskManager taskManager;

    public AbstractHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MethodName methodName = MethodName.valueOf(exchange.getRequestMethod().toUpperCase());
        try {
            switch (methodName) {
                case POST:
                    writeResponse(exchange, 201, postMethod(exchange));
                    break;
                case GET:
                    writeResponse(exchange, 200, getMethod(exchange));
                    break;
                case DELETE:
                    writeResponse(exchange, 200, deleteMethod(exchange));
                    break;
                default:
                    unsupportedMethod(exchange);
            }
        } catch (TimeIntersectionException e) {
            writeResponse(exchange, 406, e.getMessage());
        } catch (TaskNotFoundException e) {
            writeResponse(exchange, 404, e.getMessage());
        } catch (UnsupportedMethodException e) {
            writeResponse(exchange, 405, e.getMessage());
        } catch (BadRequestException e) {
            writeResponse(exchange, 400, e.getMessage());
        } catch (Throwable e) {
            writeResponse(exchange, 500, "Ошибка сервера");
        }
    }

    protected String postMethod(HttpExchange exchange) throws IOException {
        throw unsupportedMethod(exchange);
    }

    protected String getMethod(HttpExchange exchange) {
        throw unsupportedMethod(exchange);
    }

    protected String deleteMethod(HttpExchange exchange) throws IOException {
        throw unsupportedMethod(exchange);
    }

    protected UnsupportedMethodException unsupportedMethod(HttpExchange exchange) {
        return new UnsupportedMethodException("Метод" + exchange.getRequestMethod() + " не доступен!");
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

    protected <T extends Task> Optional<Integer> getTaskId(HttpExchange exchange, Class<T> clazz) {
        String[] pathInf = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathInf[2]));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        } catch (NumberFormatException e) {
            throw new TaskNotFoundException(clazz.getSimpleName(), pathInf[2]);
        }
    }

    protected <T extends Task> T getTask(HttpExchange exchange, Class<T> clazz) throws IOException {
        String strTask = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        T task = gson.fromJson(strTask, clazz);
        if (task == null) {
            throw new BadRequestException(strTask);
        }
        return task;
    }
}
