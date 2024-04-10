package handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

public class PrioritizedHandler extends AbstractHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected String getMethod(HttpExchange exchange) {
        return gson.toJson(taskManager.getPrioritizedTasks());
    }

}
