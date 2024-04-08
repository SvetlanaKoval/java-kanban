package handlers;

import com.sun.net.httpserver.HttpHandler;
import managers.Managers;
import managers.TaskManager;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class AbstractHttpHandler implements HttpHandler {

    static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    static TaskManager taskManager = Managers.getDefault();

    public static void clearManager() {
        taskManager = Managers.getDefault();
    }
}
