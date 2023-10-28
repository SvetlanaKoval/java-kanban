import taskManager.Manager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("adc", "afgjabgJMADGBj");
        Task task2 = new Task("efg", "shgjksrjghakrjgh");
        manager.addTask(task1);
        manager.addTask(task2);

        Epic epic1 = new Epic("qaz", "qazxcvb");
        Epic epic2 = new Epic("wsx", "wsxcvbn");
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask1 = new Subtask("123", "sfdvsdvsgv", epic1.getId());
        Subtask subtask2 = new Subtask("456", "qwertyu", epic1.getId());
        Subtask subtask3 = new Subtask("987", "lkjlihl", epic2.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println("_____________________________");

        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        task2.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        subtask3.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask3);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println("_____________________________");

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        System.out.println(manager.getAllEpics());
        System.out.println("_____________________________");

        manager.removeTaskById(1);
        manager.removeEpicById(3);
        manager.removeSubtaskById(7);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println("_____________________________");
    }
}