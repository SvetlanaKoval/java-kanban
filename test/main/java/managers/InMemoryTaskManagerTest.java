package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Status;
import main.java.tasks.Subtask;
import main.java.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager inMemoryTaskManager;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    private Epic epic1;
    private Epic epic2;
    private Epic epic3;

    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;
    private Subtask subtask4;
    private Subtask subtask5;

    public static final Long AVD_DURATION_TIME = 15L;

    @BeforeEach
    public void createTaskManager() {
        inMemoryTaskManager = new InMemoryTaskManager();
        task1 = new Task("task1", "task1task1task1", AVD_DURATION_TIME, "09:00 01.01.24");
        task2 = new Task("task2", "task2task2task2", AVD_DURATION_TIME, "09:20 01.01.24");
        task3 = new Task("task3", "task3task3task3", AVD_DURATION_TIME, "09:40 01.01.24");
        task4 = new Task("task4", "task4task4task4", AVD_DURATION_TIME);
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

    @Test
    public void removeSubtaskAndCorrectEpic() {
        List<Subtask> subtasksOfEpic1BeforeRemove = inMemoryTaskManager.getSubtasksByEpic(epic1);
        int removedSubtaskId = subtasksOfEpic1BeforeRemove.get(0).getId();
        inMemoryTaskManager.removeSubtaskById(removedSubtaskId);
        int expectedCountOfEpic1Subtasks = subtasksOfEpic1BeforeRemove.size() - 1;
        int resultCountOdEpic1Subtasks = inMemoryTaskManager.getSubtasksByEpic(epic1).size();
        assertEquals(expectedCountOfEpic1Subtasks, resultCountOdEpic1Subtasks, "Подзадача не удалена из эпика");
    }

    @Test
    public void setSubtaskStatusAndCorrectEpicStatus() {
        Integer epicId = epic1.getId();
        Epic currentEpic = inMemoryTaskManager.getEpicById(epicId);
        List<Subtask> allSubtaskByEpic = inMemoryTaskManager.getSubtasksByEpic(currentEpic);
        String epicStatusBefore = currentEpic.getStatus().toString();
        Subtask updatedSubtask = allSubtaskByEpic.get(0);
        updatedSubtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(updatedSubtask);
        String epicStatusAfter = currentEpic.getStatus().toString();
        assertNotEquals(epicStatusBefore, epicStatusAfter, "Статус не поменялся");
    }

    @Test
    public void setEpicStatusIfAllSubtasksNew() {
        inMemoryTaskManager.setEpicStatus(epic1);
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    public void setEpicStatusIfAllSubtasksDone() {
        inMemoryTaskManager.getSubtasksByEpic(epic1)
                .forEach(subtask -> subtask.setStatus(Status.DONE));
        inMemoryTaskManager.setEpicStatus(epic1);
        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    public void setEpicStatusIfSubtasksNewAndDone() {
        List<Subtask> subtasks = inMemoryTaskManager.getSubtasksByEpic(epic1);
        subtasks.get(0).setStatus(Status.DONE);
        for (int i = 1; i < subtasks.size(); i++) {
            subtasks.get(i).setStatus(Status.NEW);
        }
        inMemoryTaskManager.setEpicStatus(epic1);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void setEpicStatusIfAllSubtasksInProgress() {
        inMemoryTaskManager.getSubtasksByEpic(epic1)
                .forEach(subtask -> subtask.setStatus(Status.IN_PROGRESS));
        inMemoryTaskManager.setEpicStatus(epic1);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    public void checkEpicInSubtask() {
        inMemoryTaskManager.getAllSubtasks()
                .forEach(subtask -> assertNotNull(subtask.getEpicId()));
    }

    @Test
    public void checkEpicStatusExists() {
        inMemoryTaskManager.getAllEpics()
                .forEach(epic -> assertNotNull(epic.getStatus()));
    }

    @Test
    public void checkCorrectionTimeIntersectionByTask() {
        Task task5 = new Task("task5", "task5task5task5", AVD_DURATION_TIME, "09:50 01.01.24");
        inMemoryTaskManager.addTask(task5);
        assertEquals(4, inMemoryTaskManager.getAllTasks().size());
    }

    @Test
    public void checkEpicStartTime() {
        LocalDateTime epic1Start = epic1.getStartTime();
        Subtask subtask6 = new Subtask("subtask6", "subtask6subtask6subtask6", epic1.getId(), AVD_DURATION_TIME, "08:30 01.01.24");
        inMemoryTaskManager.addSubtask(subtask6);
        LocalDateTime expected = LocalDateTime.parse("08:30 01.01.24", Task.FORMATTER);
        assertEquals(expected, epic1.getStartTime());
    }

    @Test
    public void checkEpicEndTime() {
        LocalDateTime epic1End = epic1.getEndTime();
        Subtask subtask6 = new Subtask("subtask6", "subtask6subtask6subtask6", epic1.getId(), AVD_DURATION_TIME, "11:30 01.01.24");
        inMemoryTaskManager.addSubtask(subtask6);
        LocalDateTime expected = LocalDateTime.parse("11:30 01.01.24", Task.FORMATTER).plusMinutes(subtask6.getDuration().toMinutes());
        assertEquals(expected, epic1.getEndTime());
    }

    @Test
    public void checkPrioritizedTasksSize() {
        int taskCount = inMemoryTaskManager.getAllTasks().size();
        int subtaskCount = inMemoryTaskManager.getAllSubtasks().size();
        int tasksSubtasksCount = taskCount + subtaskCount;
        assertEquals(tasksSubtasksCount - 2, inMemoryTaskManager.getPrioritizedTasks().size());
    }

    @Test
    public void checkPrioritizedTasks() {
        Subtask subtask6 = new Subtask("subtask6", "subtask6subtask6subtask6", epic1.getId(), AVD_DURATION_TIME, "08:30 01.01.24");
        inMemoryTaskManager.addSubtask(subtask6);
        Subtask subtask7 = new Subtask("subtask7", "subtask7subtask7subtask7", epic1.getId(), AVD_DURATION_TIME, "11:30 01.01.24");
        inMemoryTaskManager.addSubtask(subtask7);
        assertEquals(subtask6, inMemoryTaskManager.getPrioritizedTasks().first());
        assertEquals(subtask7, inMemoryTaskManager.getPrioritizedTasks().last());
    }

}