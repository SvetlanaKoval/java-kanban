package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static main.java.managers.InMemoryTaskManagerTest.AVD_DURATION_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {

    private HistoryManager inMemoryHistoryManager;
    private static TaskManager inMemoryTaskManager;
    private static Task task1;
    private static Task task2;
    private static Task task3;
    private static Task task4;
    private final Task task5 = new Task("testTask5", "task5task5task5", AVD_DURATION_TIME, "11:30 01.01.24");

    private static Epic epic1;
    private static Epic epic2;
    private static Epic epic3;

    private static Subtask subtask1;
    private static Subtask subtask2;
    private static Subtask subtask3;
    private static Subtask subtask4;
    private static Subtask subtask5;

    @BeforeAll
    public static void createTaskManager() {
        inMemoryTaskManager = Managers.getDefault();
        task1 = new Task("task1", "task1task1task1", AVD_DURATION_TIME, "08:40 01.01.24");
        task2 = new Task("task2", "task2task2task2", AVD_DURATION_TIME, "09:00 01.01.24");
        task3 = new Task("task3", "task3task3task3", AVD_DURATION_TIME, "09:20 01.01.24");
        task4 = new Task("task43", "task4task4task4", AVD_DURATION_TIME, "09:40 01.01.24");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.addTask(task4);

        epic1 = new Epic("epic1", "epic1epic1epic1");
        epic2 = new Epic("epic2", "epic2epic2epic2");
        epic3 = new Epic("epic3", "epic3epic3epic3");
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addEpic(epic3);

        subtask1 = new Subtask("subtask1", "subtask1subtask1subtask1", epic1.getId(), AVD_DURATION_TIME, "10:00 01.01.24");
        subtask2 = new Subtask("subtask2", "subtask2subtask2subtask2", epic1.getId(), AVD_DURATION_TIME, "10:20 01.01.24");
        subtask3 = new Subtask("subtask3", "subtask3subtask3subtask3", epic1.getId(), AVD_DURATION_TIME, "10:40 01.01.24");

        subtask4 = new Subtask("subtask4", "subtask4subtask4subtask4", epic2.getId(), AVD_DURATION_TIME, "11:00 01.01.24");
        subtask5 = new Subtask("subtask5", "subtask5subtask5subtask5", epic2.getId(), AVD_DURATION_TIME);
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addSubtask(subtask4);
        inMemoryTaskManager.addSubtask(subtask5);
    }

    @BeforeEach
    public void createHistory() {
        inMemoryHistoryManager = Managers.getDefaultHistory();
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(epic2);
        inMemoryHistoryManager.add(epic1);
        inMemoryHistoryManager.add(epic3);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(subtask3);
        inMemoryHistoryManager.add(subtask2);
        inMemoryHistoryManager.add(subtask4);
        inMemoryHistoryManager.add(subtask5);
    }

    @Test
    public void addTaskIncreaseSize() {
        int historySizeBeforeAdd = inMemoryHistoryManager.getHistory().size();
        int expectedSize = historySizeBeforeAdd + 1;
        inMemoryHistoryManager.add(task5);
        int resultSize = inMemoryHistoryManager.getHistory().size();
        assertEquals(expectedSize, resultSize);
    }

    @Test
    public void addTaskAtEndOfHistory() {
        inMemoryTaskManager.addTask(task5);
        int expectedTaskId = task5.getId();
        inMemoryHistoryManager.add(task5);
        int historySize = inMemoryHistoryManager.getHistory().size();
        int resultTaskId = inMemoryHistoryManager.getHistory().get(historySize - 1).getId();
        assertEquals(expectedTaskId, resultTaskId);
    }

    @Test
    public void addTaskIfPresentInHistory() {
        int expectedHistorySize = inMemoryHistoryManager.getHistory().size();
        inMemoryHistoryManager.add(task1);
        int resultHistorySizeAfterAdd = inMemoryHistoryManager.getHistory().size();
        assertEquals(expectedHistorySize, resultHistorySizeAfterAdd);

        int expectedId = task1.getId();
        int resultIdAfterAdd = inMemoryHistoryManager.getHistory().get(resultHistorySizeAfterAdd - 1).getId();
        assertEquals(expectedId, resultIdAfterAdd);
    }

    @Test
    public void removeTaskFromMiddle() {
        int historySizeBeforeRemove = inMemoryHistoryManager.getHistory().size();
        int expectedSizeAfterRemove = historySizeBeforeRemove - 1;
        inMemoryHistoryManager.remove(inMemoryHistoryManager.getHistory().get(3).getId());
        int resultSizeAfterRemove = inMemoryHistoryManager.getHistory().size();
        assertEquals(expectedSizeAfterRemove, resultSizeAfterRemove);
    }

    @Test
    public void removeTaskFromStart() {
        int historySizeBeforeRemove = inMemoryHistoryManager.getHistory().size();
        int expectedSizeAfterRemove = historySizeBeforeRemove - 1;
        int middleTaskInMemoryIndex = inMemoryHistoryManager.getHistory().get(0).getId();
        inMemoryHistoryManager.remove(middleTaskInMemoryIndex);
        int resultSizeAfterRemove = inMemoryHistoryManager.getHistory().size();
        assertEquals(expectedSizeAfterRemove, resultSizeAfterRemove);
    }

    @Test
    public void removeTaskFromEnd() {
        int historySizeBeforeRemove = inMemoryHistoryManager.getHistory().size();
        int expectedSizeAfterRemove = historySizeBeforeRemove - 1;
        int lastTaskInMemoryIndex = inMemoryHistoryManager.getHistory().
                get(inMemoryHistoryManager.getHistory().size() - 1).getId();
        inMemoryHistoryManager.remove(lastTaskInMemoryIndex);
        int resultSizeAfterRemove = inMemoryHistoryManager.getHistory().size();
        assertEquals(expectedSizeAfterRemove, resultSizeAfterRemove);
    }

    @Test
    public void checkEmptyHistory() {
        List<Integer> tasksId = inMemoryHistoryManager.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        tasksId.forEach(id -> inMemoryHistoryManager.remove(id));
        assertTrue(inMemoryHistoryManager.getHistory().isEmpty());
    }

}