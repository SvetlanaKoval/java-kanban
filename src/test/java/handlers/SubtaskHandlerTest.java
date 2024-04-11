package handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import java.io.IOException;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskHandlerTest extends HttpHandlerTest {

    private Epic epic;

    @BeforeEach
    void setUp() throws IOException {
        super.setUp();
        epic = new Epic("epic", "epicDescription");
        createEpic(epic);
    }

    @Test
    void getSubtasksTest() {
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", 1, 10);
        createSubtask(subtask1);

        HttpResponse<String> response = sendHttpRequest("subtasks", "GET");

        JsonArray actualSubtasksList = JsonParser.parseString(response.body()).getAsJsonArray();
        String actualSubtask = actualSubtasksList.get(0).getAsJsonObject().get("name").getAsString();

        assertEquals(200, response.statusCode());
        assertEquals(1, actualSubtasksList.size());
        assertEquals("subtask1", actualSubtask);
    }

    @Test
    void getSubtaskByIdTest() {
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", 1, 10);
        createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", 1, 10);
        createSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "subtaskDescription3", 1, 10);
        createSubtask(subtask3);

        HttpResponse<String> response = sendHttpRequest("subtasks/2", "GET");
        Integer actual = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsInt();

        assertEquals(200, response.statusCode());
        assertEquals(2, actual);
    }

    @Test
    void getSubtaskByIdIfNotExistsTest() {
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", 1, 10);
        createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", 1, 10);
        createSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "subtaskDescription3", 1, 10);
        createSubtask(subtask3);

        HttpResponse<String> response = sendHttpRequest("subtasks/5", "GET");

        assertEquals(404, response.statusCode());
    }

    @Test
    void addSubtaskTest() {
        Subtask subtask = new Subtask("subtask1", "subtaskDescription1", 1, 10);

        HttpResponse<String> response = sendHttpRequest("subtasks", "POST", gson.toJson(subtask));

        assertEquals(201, response.statusCode());
        assertEquals("Подзадача успешно создана!", response.body());
    }

    @Test
    void addSubtaskIfIntersectionExistsTest() {
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", 1, 10, "09:00 01.01.24");
        createSubtask(subtask1);

        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", 1, 10, "09:05 01.01.24");

        HttpResponse<String> response = sendHttpRequest("subtasks", "POST", gson.toJson(subtask2));

        assertEquals(406, response.statusCode());
        assertEquals("Попытка добавить пересекающуюся задачу", response.body());
    }

    @Test
    void updateSubtaskTest() {
        Subtask subtask = new Subtask("subtask1", "subtaskDescription1", 1, 10);
        createSubtask(subtask);

        subtask.setId(2);
        subtask.setDescription("newSubtaskDescription");
        HttpResponse<String> response = sendHttpRequest("subtasks", "POST", gson.toJson(subtask));

        assertEquals(201, response.statusCode());
        assertEquals("Подзадача успешно обновлена!", response.body());
    }

    @Test
    public void deleteSubtaskTest() {
        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", 1, 10);
        createSubtask(subtask1);
        subtask1.setId(2);

        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", 1, 10);
        createSubtask(subtask2);
        subtask2.setId(3);

        Subtask subtask3 = new Subtask("subtask3", "subtaskDescription3", 1, 10);
        createSubtask(subtask3);
        subtask3.setId(4);

        HttpResponse<String> response = sendHttpRequest("subtasks", "DELETE", gson.toJson(subtask3));
        String deletedTask = JsonParser.parseString(response.body()).getAsJsonObject().get("name").getAsString();

        assertEquals(200, response.statusCode());
        assertEquals(subtask3.getName(), deletedTask);

        HttpResponse<String> getAllTasksResponse = sendHttpRequest("subtasks", "GET");
        int actualSize = JsonParser.parseString(getAllTasksResponse.body()).getAsJsonArray().size();

        assertEquals(2, actualSize);
    }

}
