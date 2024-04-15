package handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicHandlerTest extends HttpHandlerTest {

    @Test
    void getEpicsTest() {
        Epic epic = new Epic("epic", "epicDescription");
        createEpic(epic);

        HttpResponse<String> response = sendHttpRequest("epics", "GET");
        JsonArray actualEpicsList = JsonParser.parseString(response.body()).getAsJsonArray();
        String actualEpic = actualEpicsList.get(0).getAsJsonObject().get("name").getAsString();

        assertEquals(200, response.statusCode());
        assertEquals(1, actualEpicsList.size());
        assertEquals("epic", actualEpic);
    }

    @Test
    void getEpicByIdTest() {
        Epic epic1 = new Epic("epic1", "epicDescription1");
        createEpic(epic1);
        Epic epic2 = new Epic("epic2", "epicDescription2");
        createEpic(epic2);
        Epic epic3 = new Epic("epic3", "epicDescription3");
        createEpic(epic3);

        HttpResponse<String> response = sendHttpRequest("epics/2", "GET");
        Integer actual = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsInt();

        assertEquals(200, response.statusCode());
        assertEquals(2, actual);
    }

    @Test
    void getEpicByIdIfNotExistsTest() {
        Epic epic1 = new Epic("epic1", "epicDescription1");
        createEpic(epic1);
        Epic epic2 = new Epic("epic2", "epicDescription2");
        createEpic(epic2);
        Epic epic3 = new Epic("epic3", "epicDescription3");
        createEpic(epic3);

        HttpResponse<String> response = sendHttpRequest("tasks/5", "GET");

        assertEquals(404, response.statusCode());
    }

    @Test
    void getEpicSubtasksByIdTest() {
        Epic epic = new Epic("epic", "epicDescription");
        createEpic(epic);

        Subtask subtask1 = new Subtask("subtask1", "subtaskDescription1", 1, 10);
        Subtask subtask2 = new Subtask("subtask2", "subtaskDescription2", 1, 10);
        createSubtask(subtask1);
        createSubtask(subtask2);

        HttpResponse<String> response = sendHttpRequest("epics/1/subtasks", "GET");
        Integer actualSubtasksSize = JsonParser.parseString(response.body()).getAsJsonArray().size();

        assertEquals(200, response.statusCode());
        assertEquals(2, actualSubtasksSize);
    }

    @Test
    void getEpicSubtasksByIdIfEpicNotExistsTest() {
        HttpResponse<String> response = sendHttpRequest("epics/2/subtasks", "GET");

        assertEquals(404, response.statusCode());
    }

    @Test
    void addEpicTest() {
        Epic epic = new Epic("epic", "epicDescription");

        HttpResponse<String> response = sendHttpRequest("epics", "POST", gson.toJson(epic));

        assertEquals(201, response.statusCode());
        assertEquals("Эпик успешно создан", response.body());
    }

    @Test
    public void deleteEpicTest() {
        Epic epic1 = new Epic("epic1", "epicDescription1");
        createEpic(epic1);
        epic1.setId(1);

        Epic epic2 = new Epic("epic2", "epicDescription2");
        createEpic(epic2);
        epic2.setId(2);

        Epic epic3 = new Epic("epic3", "epicDescription3");
        createEpic(epic3);
        epic3.setId(3);

        HttpResponse<String> response = sendHttpRequest("epics", "DELETE", gson.toJson(epic3));
        String deletedEpic = JsonParser.parseString(response.body()).getAsJsonObject().get("name").getAsString();

        assertEquals(200, response.statusCode());
        assertEquals(epic3.getName(), deletedEpic);

        HttpResponse<String> getAllTasksResponse = sendHttpRequest("epics", "GET");
        int actualSize = JsonParser.parseString(getAllTasksResponse.body()).getAsJsonArray().size();

        assertEquals(2, actualSize);
    }

}