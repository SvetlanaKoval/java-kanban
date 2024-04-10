package handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Subtask;
import java.io.IOException;
import java.util.Optional;

public class SubtaskHandler extends AbstractHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected String postMethod(HttpExchange exchange) throws IOException {
        Subtask subtask = getTask(exchange, Subtask.class);
        if (subtask.getId() == null) {
            taskManager.addSubtask(subtask);
            return "Подзадача успешно создана!";
        } else {
            taskManager.updateSubtask(subtask);
            return "Подзадача успешно обновлена!";
        }
    }

    @Override
    protected String getMethod(HttpExchange exchange) {
        Optional<Integer> subtaskId = getTaskId(exchange, Subtask.class);
        if (subtaskId.isEmpty()) {
            return gson.toJson(taskManager.getAllSubtasks());
        }
        return gson.toJson(taskManager.getSubtaskById(subtaskId.get()));
    }

    @Override
    protected String deleteMethod(HttpExchange exchange) throws IOException {
        Subtask deletedSubtask = getTask(exchange, Subtask.class);
        return gson.toJson(taskManager.removeSubtaskById(deletedSubtask.getId()));
    }
}
