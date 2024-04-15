package handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;
import java.io.IOException;
import java.util.Optional;

public class EpicHandler extends AbstractHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected String postMethod(HttpExchange exchange) throws IOException {
        taskManager.addEpic(getTask(exchange, Epic.class));
        return "Эпик успешно создан";
    }

    @Override
    protected String getMethod(HttpExchange exchange) {
        Optional<Integer> epicId = getTaskId(exchange, Epic.class);

        if (epicId.isEmpty()) {
            return gson.toJson(taskManager.getAllEpics());
        }
        int id = epicId.get();

        Epic epic = taskManager.getEpicById(id);
        if (exchange.getRequestURI().getPath().contains("subtasks")) {
            return gson.toJson(taskManager.getSubtasksByEpic(epic));
        }
        return gson.toJson(taskManager.getEpicById(id));
    }

    @Override
    protected String deleteMethod(HttpExchange exchange) throws IOException {
        Epic deletedEpic = getTask(exchange, Epic.class);
        return gson.toJson(taskManager.removeEpicById(deletedEpic.getId()));
    }

}
