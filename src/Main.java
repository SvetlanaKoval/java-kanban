import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = getTaskManager();

        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllSubtasks());
        System.out.println("_____________________________");

        inMemoryTaskManager.getEpicById(6);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubtaskById(16);
        inMemoryTaskManager.getEpicById(7);
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubtaskById(17);
        inMemoryTaskManager.getEpicById(8);
        inMemoryTaskManager.getTaskById(4);
        inMemoryTaskManager.getSubtaskById(10);
        inMemoryTaskManager.getEpicById(9);
        inMemoryTaskManager.getTaskById(5);
        inMemoryTaskManager.getSubtaskById(11);

        System.out.println("История просмотров: " + inMemoryTaskManager.getHistory());
    }

    private static TaskManager getTaskManager() {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("task1", "task1task1task1");
        Task task2 = new Task("task2", "task2task2task2");
        Task task3 = new Task("task3", "task3task3task3");
        Task task4 = new Task("task4", "task4task4task4");
        Task task5 = new Task("task5", "task5task5task5");
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);
        inMemoryTaskManager.addTask(task4);
        inMemoryTaskManager.addTask(task5);

        Epic epic1 = new Epic("epic1", "epic1epic1epic1");
        Epic epic2 = new Epic("epic2", "epic2epic2epic2");
        Epic epic3 = new Epic("epic3", "epic3epic3epic3");
        Epic epic4 = new Epic("epic4", "epic4epic4epic4");
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);
        inMemoryTaskManager.addEpic(epic3);
        inMemoryTaskManager.addEpic(epic4);

        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1subtask1", epic1.getId());
        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2subtask2", epic1.getId());
        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3subtask3", epic2.getId());
        Subtask subtask4 = new Subtask("subtask4", "subtask4subtask4subtask4", epic3.getId());
        Subtask subtask5 = new Subtask("subtask5", "subtask5subtask5subtask5", epic3.getId());
        Subtask subtask6 = new Subtask("subtask6", "subtask6subtask6subtask6", epic3.getId());
        Subtask subtask7 = new Subtask("subtask7", "subtask7subtask7subtask7", epic3.getId());
        Subtask subtask8 = new Subtask("subtask8", "subtask8subtask8subtask8", epic3.getId());
        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        inMemoryTaskManager.addSubtask(subtask4);
        inMemoryTaskManager.addSubtask(subtask5);
        inMemoryTaskManager.addSubtask(subtask6);
        inMemoryTaskManager.addSubtask(subtask7);
        inMemoryTaskManager.addSubtask(subtask8);
        return inMemoryTaskManager;
    }
}