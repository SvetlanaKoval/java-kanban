package handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import tasks.Task;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskHandlerTest extends HttpHandlerTest {

    @Test
    void getTasksTest() {
        Task task1 = new Task("task1", "taskDescription1");
        createTask(task1);

        HttpResponse<String> response = sendHttpRequest("tasks", "GET");

        JsonArray actualTasksList = JsonParser.parseString(response.body()).getAsJsonArray();
        String actualTask = actualTasksList.get(0).getAsJsonObject().get("name").getAsString();

        assertEquals(200, response.statusCode());
        assertEquals(1, actualTasksList.size());
        assertEquals("task1", actualTask);
    }

    @Test
    void getTaskByIdTest() {
        Task task1 = new Task("task1", "taskDescription1");
        createTask(task1);
        Task task2 = new Task("task2", "taskDescription2", 10, "09:00 01.01.24");
        createTask(task2);
        Task task3 = new Task("task3", "taskDescription3");
        createTask(task3);

        HttpResponse<String> response = sendHttpRequest("tasks/2", "GET");
        Integer actual = JsonParser.parseString(response.body()).getAsJsonObject().get("id").getAsInt();

        assertEquals(200, response.statusCode());
        assertEquals(2, actual);
    }

    @Test
    void getTaskByIdIfNotExistsTest() {
        Task task1 = new Task("task1", "taskDescription1");
        createTask(task1);
        Task task2 = new Task("task2", "taskDescription2", 10, "09:00 01.01.24");
        createTask(task2);
        Task task3 = new Task("task3", "taskDescription3");
        createTask(task3);

        HttpResponse<String> response = sendHttpRequest("tasks/5", "GET");
        assertEquals(404, response.statusCode());
    }

    @Test
    void addTaskTest() {
        Task task = new Task("task1", "taskDescription1");

        HttpResponse<String> response = sendHttpRequest("tasks", "POST", gson.toJson(task));

        assertEquals(201, response.statusCode());
        assertEquals("Задача успешно создана!", response.body());
    }

    @Test
    void addTaskIfIntersectionExistsTest() {
        Task task1 = new Task("task1", "taskDescription1", 10, "09:00 01.01.24");
        createTask(task1);

        Task task2 = new Task("task2", "taskDescription2", 10, "09:05 01.01.24");

        HttpResponse<String> response = sendHttpRequest("tasks", "POST", gson.toJson(task2));

        assertEquals(406, response.statusCode());
        assertEquals("Попытка добавить пересекающуюся задачу", response.body());
    }

    @Test
    void updateTaskTest() {
        Task task = new Task("task1", "taskDescription1");
        createTask(task);

        task.setId(1);
        task.setDescription("newTaskDescription");
        HttpResponse<String> response = sendHttpRequest("tasks", "POST", gson.toJson(task));

        assertEquals(201, response.statusCode());
        assertEquals("Задача успешно обновлена!", response.body());
    }

    @Test
    public void deleteTaskTest() {
        Task task1 = new Task("task1", "taskDescription1");
        createTask(task1);
        task1.setId(1);

        Task task2 = new Task("task2", "taskDescription2", 10, "09:00 01.01.24");
        createTask(task2);
        task2.setId(2);

        Task task3 = new Task("task3", "taskDescription3");
        createTask(task3);
        task3.setId(3);

        HttpResponse<String> response = sendHttpRequest("tasks", "DELETE", gson.toJson(task3));

        String deletedTask = JsonParser.parseString(response.body()).getAsJsonObject().get("name").getAsString();

        assertEquals(200, response.statusCode());
        assertEquals(task3.getName(), deletedTask);

        HttpResponse<String> getAllTasksResponse = sendHttpRequest("tasks", "GET");
        int actualSize = JsonParser.parseString(getAllTasksResponse.body()).getAsJsonArray().size();

        assertEquals(2, actualSize);
    }

}