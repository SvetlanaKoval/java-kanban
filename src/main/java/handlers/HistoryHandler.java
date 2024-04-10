package handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

public class HistoryHandler extends AbstractHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected String getMethod(HttpExchange exchange) {
        return gson.toJson(taskManager.getHistory());
    }

}
