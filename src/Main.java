import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
//        TaskManager inMemoryTaskManager = getTaskManager();
//        inMemoryTaskManager.getTaskById(2);
//        inMemoryTaskManager.getSubtaskById(6);
//        inMemoryTaskManager.getEpicById(3);
//        inMemoryTaskManager.getTaskById(1);
//        inMemoryTaskManager.getSubtaskById(7);
//        inMemoryTaskManager.getSubtaskById(5);
//        inMemoryTaskManager.getEpicById(4);
//        System.out.println("История просмотров: " + inMemoryTaskManager.getHistory());
//        System.out.println("_____________________________");
//
//        inMemoryTaskManager.getSubtaskById(6);
//        inMemoryTaskManager.getTaskById(1);
//        inMemoryTaskManager.getSubtaskById(7);
//        inMemoryTaskManager.getTaskById(1);
//        inMemoryTaskManager.getEpicById(4);
//        inMemoryTaskManager.getSubtaskById(5);
//        inMemoryTaskManager.getTaskById(2);
//        inMemoryTaskManager.getTaskById(2);
//        inMemoryTaskManager.getEpicById(3);
//        System.out.println("История просмотров: " + inMemoryTaskManager.getHistory());
//        System.out.println("_____________________________");
//
//        inMemoryTaskManager.getTaskById(1);
//        inMemoryTaskManager.getTaskById(2);
//        inMemoryTaskManager.getEpicById(3);
//        inMemoryTaskManager.getEpicById(4);
//        inMemoryTaskManager.getSubtaskById(5);
//        inMemoryTaskManager.getSubtaskById(6);
//        inMemoryTaskManager.getSubtaskById(7);
//        System.out.println("История просмотров: " + inMemoryTaskManager.getHistory());
//        System.out.println("_____________________________");
//
//        inMemoryTaskManager.removeTaskById(2);
//        System.out.println("История просмотров после удаления задачи: " + inMemoryTaskManager.getHistory());
//        System.out.println("_____________________________");
//
//        inMemoryTaskManager.removeEpicById(3);
//        System.out.println("История просмотров после удаления эпика: " + inMemoryTaskManager.getHistory());
//        System.out.println("_____________________________");
//    }
//
//    private static TaskManager getTaskManager() {
//        TaskManager inMemoryTaskManager = Managers.getDefault();
//        Task task1 = new Task("task1", "task1task1task1");
//        Task task2 = new Task("task2", "task2task2task2");
//        inMemoryTaskManager.addTask(task1);
//        inMemoryTaskManager.addTask(task2);
//
//        Epic epic1 = new Epic("epic1", "epic1epic1epic1");
//        Epic epic2 = new Epic("epic2", "epic2epic2epic2");
//        inMemoryTaskManager.addEpic(epic1);
//        inMemoryTaskManager.addEpic(epic2);
//
//        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1subtask1", epic1.getId());
//        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2subtask2", epic1.getId());
//        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3subtask3", epic1.getId());
//        inMemoryTaskManager.addSubtask(subtask1);
//        inMemoryTaskManager.addSubtask(subtask2);
//        inMemoryTaskManager.addSubtask(subtask3);
//        return inMemoryTaskManager;
    }
}