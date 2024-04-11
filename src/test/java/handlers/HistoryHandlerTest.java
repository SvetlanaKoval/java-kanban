package handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryHandlerTest extends HttpHandlerTest {

    @Test
    void getHistoryTest() {
        Task task1 = new Task("task1", "taskDescription1");
        createTask(task1);
        Task task2 = new Task("task2", "taskDescription2", 10, "09:00 01.01.24");
        createTask(task2);
        Task task3 = new Task("task3", "taskDescription3");
        createTask(task3);

        sendHttpRequest("tasks/1", "GET");
        sendHttpRequest("tasks/2", "GET");
        sendHttpRequest("tasks/3", "GET");

        HttpResponse<String> response = sendHttpRequest("history", "GET");
        assertEquals(200, response.statusCode());

        JsonArray actualHistoryList = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(3, actualHistoryList.size());

        String actualTask2Name = actualHistoryList.get(1).getAsJsonObject().get("name").getAsString();

        assertEquals("task2", actualTask2Name);
    }

}