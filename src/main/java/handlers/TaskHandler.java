package handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;
import java.io.IOException;
import java.util.Optional;

public class TaskHandler extends AbstractHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected String getMethod(HttpExchange exchange) {
        Optional<Integer> taskId = getTaskId(exchange, Task.class);
        if (taskId.isEmpty()) {
            return gson.toJson(taskManager.getAllTasks());
        }
        return gson.toJson(taskManager.getTaskById(taskId.get()));
    }

    @Override
    protected String postMethod(HttpExchange exchange) throws IOException {
        Task task = getTask(exchange, Task.class);
        if (task.getId() == null) {
            taskManager.addTask(task);
            return "Задача успешно создана!";
        } else {
            taskManager.updateTask(task);
            return "Задача успешно обновлена!";
        }
    }

    @Override
    protected String deleteMethod(HttpExchange exchange) throws IOException {
        Task deletedTask = getTask(exchange, Task.class);
        return gson.toJson(taskManager.removeTaskById(deletedTask.getId()));
    }

}
