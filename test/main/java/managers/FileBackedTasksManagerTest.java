package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    FileBackedTasksManager fileBackedTasksManager;

    public void createFileBackedTaskManager() {
        fileBackedTasksManager = new FileBackedTasksManager(new File("123.csv"));
        Task task1 = new Task("task1", "task1task1task1", AVD_DURATION_TIME, "09:00 01.01.24");
        Task task2 = new Task("task2", "task2task2task2", AVD_DURATION_TIME, "09:20 01.01.24");
        Task task3 = new Task("task3", "task3task3task3", AVD_DURATION_TIME, "09:40 01.01.24");
        Task task4 = new Task("task4", "task4task4task4", AVD_DURATION_TIME);
        fileBackedTasksManager.addTask(task1);
        fileBackedTasksManager.addTask(task2);
        fileBackedTasksManager.addTask(task3);
        fileBackedTasksManager.addTask(task4);

        Epic epic1 = new Epic("epic1", "epic1epic1epic1");
        Epic epic2 = new Epic("epic2", "epic2epic2epic2");
        Epic epic3 = new Epic("epic3", "epic3epic3epic3");
        fileBackedTasksManager.addEpic(epic1);
        fileBackedTasksManager.addEpic(epic2);
        fileBackedTasksManager.addEpic(epic3);

        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1subtask1", epic1.getId(), AVD_DURATION_TIME, "10:00 01.01.24");
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2subtask2", epic1.getId(), AVD_DURATION_TIME, "10:20 01.01.24");
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3subtask3", epic1.getId(), AVD_DURATION_TIME, "10:40 01.01.24");

        Subtask subtask4 = new Subtask("subtask4", "subtask4subtask4subtask4", epic2.getId(), AVD_DURATION_TIME, "11:00 01.01.24");
        Subtask subtask5 = new Subtask("subtask5", "subtask5subtask5subtask5", epic2.getId(), AVD_DURATION_TIME);
        fileBackedTasksManager.addSubtask(subtask1);
        fileBackedTasksManager.addSubtask(subtask2);
        fileBackedTasksManager.addSubtask(subtask3);
        fileBackedTasksManager.addSubtask(subtask4);
        fileBackedTasksManager.addSubtask(subtask5);

        fileBackedTasksManager.getHistoryManager().add(task3);
        fileBackedTasksManager.getHistoryManager().add(task2);
        fileBackedTasksManager.getHistoryManager().add(task1);
        fileBackedTasksManager.getHistoryManager().add(task4);
        fileBackedTasksManager.getHistoryManager().add(epic2);
        fileBackedTasksManager.getHistoryManager().add(epic1);
        fileBackedTasksManager.getHistoryManager().add(epic3);
        fileBackedTasksManager.getHistoryManager().add(subtask1);
        fileBackedTasksManager.getHistoryManager().add(subtask3);
        fileBackedTasksManager.getHistoryManager().add(subtask2);
        fileBackedTasksManager.getHistoryManager().add(subtask4);
        fileBackedTasksManager.getHistoryManager().add(subtask5);
    }

    @Test
    public void checkSavetoFile() {
        createFileBackedTaskManager();
        Assertions.assertDoesNotThrow(() -> fileBackedTasksManager.save());
    }

    @Test
    public void checkLoadFromNotExistFile() {
        Assertions.assertThrows(RuntimeException.class, () -> FileBackedTasksManager.loadFromFile(new File("555.csv")));
    }

    @Test
    public void checkLoadFromEmptyFile() {
        Assertions.assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(new File("222.csv")));
    }

}