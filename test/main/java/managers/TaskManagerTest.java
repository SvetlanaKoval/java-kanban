package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {

    public static final Long AVD_DURATION_TIME = 15L;

    private T taskManager;

    private Epic epic1;
    private Epic epic2;
    private Epic epic3;

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;
    private Subtask subtask4;
    private Subtask subtask5;

    abstract T createTaskManager();

    @BeforeEach
    public void beforeEach() {
        taskManager = createTaskManager();
        task1 = new Task("task1", "task1task1task1", AVD_DURATION_TIME, "09:00 01.01.24");
        task2 = new Task("task2", "task2task2task2", AVD_DURATION_TIME, "09:20 01.01.24");
        task3 = new Task("task3", "task3task3task3", AVD_DURATION_TIME, "09:40 01.01.24");
        task4 = new Task("task4", "task4task4task4", AVD_DURATION_TIME);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);

        epic1 = new Epic("epic1", "epic1epic1epic1");
        epic2 = new Epic("epic2", "epic2epic2epic2");
        epic3 = new Epic("epic3", "epic3epic3epic3");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        subtask1 = new Subtask("subtask1", "subtask1subtask1subtask1", epic1.getId(), AVD_DURATION_TIME, "10:00 01.01.24");
        subtask2 = new Subtask("subtask2", "subtask2subtask2subtask2", epic1.getId(), AVD_DURATION_TIME, "10:20 01.01.24");
        subtask3 = new Subtask("subtask3", "subtask3subtask3subtask3", epic1.getId(), AVD_DURATION_TIME, "10:40 01.01.24");
        subtask4 = new Subtask("subtask4", "subtask4subtask4subtask4", epic2.getId(), AVD_DURATION_TIME, "11:00 01.01.24");
        subtask5 = new Subtask("subtask5", "subtask5subtask5subtask5", epic2.getId(), AVD_DURATION_TIME);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addSubtask(subtask4);
        taskManager.addSubtask(subtask5);
    }

    @Test
    public void setEpicStatusIfAllSubtasksNew() {
        taskManager.setEpicStatus(epic1);
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    public void setEpicStatusIfAllSubtasksDone() {
        taskManager.getSubtasksByEpic(epic1)
            .forEach(subtask -> subtask.setStatus(Status.DONE));
        taskManager.setEpicStatus(epic1);
        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    public void setEpicStatusIfSubtasksNewAndDone() {
        List<Subtask> subtasks = taskManager.getSubtasksByEpic(epic1);
        subtasks.get(0).setStatus(Status.DONE);
        for (int i = 1; i < subtasks.size(); i++) {
            subtasks.get(i).setStatus(Status.NEW);
        }
        taskManager.setEpicStatus(epic1);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void setEpicStatusIfAllSubtasksInProgress() {
        taskManager.getSubtasksByEpic(epic1)
            .forEach(subtask -> subtask.setStatus(Status.IN_PROGRESS));
        taskManager.setEpicStatus(epic1);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void checkEpicInSubtask() {
        taskManager.getAllSubtasks()
            .forEach(subtask -> assertNotNull(subtask.getEpicId()));
    }

    @Test
    public void checkEpicStatusExists() {
        taskManager.getAllEpics()
            .forEach(epic -> assertNotNull(epic.getStatus()));
    }

    @Test
    public void checkCorrectionTimeIntersectionByTask() {
        int expected = taskManager.getAllTasks().size();
        Task task5 = new Task("task5", "task5task5task5", AVD_DURATION_TIME, "09:50 01.01.24");

        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(task5));
        assertEquals(expected, taskManager.getAllTasks().size());
    }

    @Test
    public void checkEpicStartTime() {
        Subtask subtask6 = new Subtask("subtask6", "subtask6subtask6subtask6", epic1.getId(), AVD_DURATION_TIME, "08:30 01.01.24");
        taskManager.addSubtask(subtask6);
        LocalDateTime expected = LocalDateTime.parse("08:30 01.01.24", Task.FORMATTER);

        assertEquals(expected, epic1.getStartTime());
    }

    @Test
    public void checkEpicEndTime() {
        Subtask subtask6 = new Subtask("subtask6", "subtask6subtask6subtask6", epic1.getId(), AVD_DURATION_TIME, "11:30 01.01.24");
        taskManager.addSubtask(subtask6);
        LocalDateTime expected = LocalDateTime.parse("11:30 01.01.24", Task.FORMATTER).plusMinutes(subtask6.getDuration().toMinutes());

        assertEquals(expected, epic1.getEndTime());
    }

    @Test
    public void checkPrioritizedTasksSize() {
        List<Task> expected = Stream.concat(taskManager.getAllSubtasks().stream(), taskManager.getAllTasks().stream())
            .filter(t -> t.getStartTime() != null)
            .sorted()
            .collect(Collectors.toList());

        List<Task> actual = new ArrayList<>(taskManager.getPrioritizedTasks());

        assertEquals(expected, actual);
    }

    @Test
    public void checkPrioritizedTasks() {
        Subtask subtask6 = new Subtask("subtask6", "subtask6subtask6subtask6", epic1.getId(), AVD_DURATION_TIME, "08:30 01.01.24");
        taskManager.addSubtask(subtask6);
        Subtask subtask7 = new Subtask("subtask7", "subtask7subtask7subtask7", epic1.getId(), AVD_DURATION_TIME, "11:30 01.01.24");
        taskManager.addSubtask(subtask7);

        assertEquals(subtask6, taskManager.getPrioritizedTasks().first());
        assertEquals(subtask7, taskManager.getPrioritizedTasks().last());
    }

    @Test
    void removeAllEpics() {
        taskManager.removeAllEpics();

        assertTrue(taskManager.getAllEpics().isEmpty());
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void removeAllSubtasks() {
        taskManager.removeAllSubtasks();

        List<Epic> allEpics = taskManager.getAllEpics();
        allEpics.forEach(epic -> {
            assertTrue(epic.getSubtasks().isEmpty());
            assertSame(epic.getStatus(), Status.NEW);
        });
        assertTrue(taskManager.getAllSubtasks().isEmpty());
        taskManager.getHistory().forEach(t -> assertNotSame(t.getClass(), Subtask.class));
        taskManager.getPrioritizedTasks().forEach(t -> assertNotSame(t.getClass(), Subtask.class));
    }

    @Test
    void removeAllTasks() {
        taskManager.removeAllTasks();

        assertTrue(taskManager.getAllTasks().isEmpty());
        taskManager.getHistory().forEach(t -> assertNotSame(t.getClass(), Task.class));
        taskManager.getPrioritizedTasks().forEach(t -> assertNotSame(t.getClass(), Task.class));
    }

    @Test
    void getSubtaskByIdWhenNotExists() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskManager.getSubtaskById(123));
        assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void getSubtaskById() {
        Subtask actualSubtask = taskManager.getSubtaskById(subtask1.getId());

        assertEquals(subtask1, actualSubtask);
        assertTrue(taskManager.getHistory().contains(actualSubtask));
    }

    @Test
    void getTaskByIdWhenNotExists() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskManager.getTaskById(123));
        assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void getTaskById() {
        Task actualTask = taskManager.getTaskById(task1.getId());

        assertEquals(task1, actualTask);
        assertTrue(taskManager.getHistory().contains(actualTask));
    }

    @Test
    void updateEpicWhenNotExists() {
        Epic nonExistentEpic = new Epic();
        nonExistentEpic.setId(123);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskManager.updateEpic(nonExistentEpic));
        assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void updateEpic() {
        epic2.setName("newEpic2Name");
        taskManager.updateEpic(epic2);

        Epic epicById = taskManager.getEpicById(epic2.getId());
        assertEquals("newEpic2Name", epicById.getName());
    }

    @Test
    void updateTaskWhenNotExists() {
        Task nonExistentTask = new Task();
        nonExistentTask.setId(123);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskManager.updateTask(nonExistentTask));
        assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void updateTask() {
        task2.setName("newTask2Name");
        taskManager.updateTask(task2);

        Task taskById = taskManager.getTaskById(task2.getId());
        assertEquals("newTask2Name", taskById.getName());
    }

    @Test
    void removeEpicByIdWhenNotExists() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskManager.removeEpicById(123));
        assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void removeEpicById() {
        taskManager.removeEpicById(epic3.getId());

        assertTrue(taskManager.getSubtasksByEpic(epic3).isEmpty());
        taskManager.getHistory().stream()
            .filter(t -> t instanceof Subtask)
            .map(task -> (Subtask) task)
            .forEach(subtask -> assertNotEquals(subtask.getEpicId(), epic3.getId()));

        taskManager.getAllEpics().forEach(e -> assertNotEquals(e.getId(), epic3.getId()));
        taskManager.getHistory().forEach(e -> assertNotEquals(e.getId(), epic3.getId()));
    }

    @Test
    void removeTaskByIdWhenNotExists() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskManager.removeTaskById(123));
        assertEquals("Задача не найдена", exception.getMessage());
    }

    @Test
    void removeTaskById() {
        taskManager.removeTaskById(task3.getId());

        taskManager.getAllTasks().forEach(t -> assertNotEquals(t.getId(), task3.getId()));
        taskManager.getHistory().forEach(e -> assertNotEquals(e.getId(), task3.getId()));
    }

    @Test
    public void removeSubtaskAndCorrectEpic() {
        List<Subtask> subtasksOfEpic1BeforeRemove = taskManager.getSubtasksByEpic(epic1);
        int removedSubtaskId = subtasksOfEpic1BeforeRemove.get(0).getId();

        taskManager.removeSubtaskById(removedSubtaskId);

        int expectedCountOfEpic1Subtasks = subtasksOfEpic1BeforeRemove.size() - 1;
        int resultCountOdEpic1Subtasks = taskManager.getSubtasksByEpic(epic1).size();
        assertEquals(expectedCountOfEpic1Subtasks, resultCountOdEpic1Subtasks, "Подзадача не удалена из эпика");
    }

    @Test
    public void setSubtaskStatusAndCorrectEpicStatus() {
        Integer epicId = epic1.getId();
        Epic currentEpic = taskManager.getEpicById(epicId);
        List<Subtask> allSubtaskByEpic = taskManager.getSubtasksByEpic(currentEpic);
        Status epicStatusBefore = currentEpic.getStatus();
        Subtask updatedSubtask = allSubtaskByEpic.get(0);
        updatedSubtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(updatedSubtask);
        Status epicStatusAfter = currentEpic.getStatus();
        assertNotEquals(epicStatusBefore, epicStatusAfter, "Статус не поменялся");
    }

}
