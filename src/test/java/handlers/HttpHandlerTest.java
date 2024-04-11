package handlers;

import com.google.gson.Gson;
import httpserver.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class HttpHandlerTest {

    protected HttpTaskServer httpTaskServer;
    protected final Gson gson = new Gson();

    @BeforeEach
    void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stopServer();
    }

    HttpResponse<String> sendHttpRequest(String path, String method, String body) {
        HttpRequest.BodyPublisher bodyPublisher = body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/" + path))
            .method(method, bodyPublisher)
            .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    HttpResponse<String> sendHttpRequest(String path, String method) {
        return sendHttpRequest(path, method, null);
    }

    void createTask(Task task) {
        sendHttpRequest("tasks", "POST", gson.toJson(task));
    }

    void createEpic(Epic epic) {
        sendHttpRequest("epics", "POST", gson.toJson(epic));
    }

    void createSubtask(Subtask subtask) {
        sendHttpRequest("subtasks", "POST", gson.toJson(subtask));
    }
}
