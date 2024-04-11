package handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrioritizedHandlerTest extends HttpHandlerTest {

    @Test
    void getPrioritizedTaskTest() {
        Task task1 = new Task("task1", "taskDescription1", 10, "23:00 01.01.24");
        createTask(task1);
        Task task2 = new Task("task2", "taskDescription2", 10, "15:00 01.01.24");
        createTask(task2);
        Task task3 = new Task("task3", "taskDescription3", 10, "18:00 01.01.24");
        createTask(task3);

        HttpResponse<String> response = sendHttpRequest("prioritized", "GET");
        assertEquals(200, response.statusCode());

        JsonArray actualPrioritizedTaskList = JsonParser.parseString(response.body()).getAsJsonArray();
        assertEquals(3, actualPrioritizedTaskList.size());

        List<String> actualNamesList = new ArrayList<>();
        for (JsonElement jsonElement : actualPrioritizedTaskList) {
            String name = jsonElement.getAsJsonObject().get("name").getAsString();
            actualNamesList.add(name);
        }

        assertEquals(List.of("task2", "task3", "task1"), actualNamesList);
    }

}