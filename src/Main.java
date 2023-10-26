public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("adc", "afgjabgJMADGBj");
        Task task2 = new Task("efg", "shgjksrjghakrjgh");

        Epic epic1 = new Epic("qaz", "qazxcvb");
        Epic epic2 = new Epic("wsx", "wsxcvbn");

        Subtask subtask1 = new Subtask("123", "sfdvsdvsgv", epic1);
        Subtask subtask2 = new Subtask("456", "qwertyu", epic1);
        Subtask subtask3 = new Subtask("987", "lkjlihl", epic2);

        Manager manager = new Manager();
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(epic1);
        manager.addTask(subtask1);
        manager.addTask(subtask2);
        manager.addTask(epic2);
        manager.addTask(subtask3);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println("_____________________________");

        manager.setTaskStatus(Status.IN_PROGRESS, task1);
        manager.setTaskStatus(Status.IN_PROGRESS, task2);
        manager.setTaskStatus(Status.IN_PROGRESS, subtask1);
        manager.setTaskStatus(Status.IN_PROGRESS, subtask3);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println("_____________________________");

        manager.setTaskStatus(Status.DONE, subtask1);
        manager.setTaskStatus(Status.DONE, subtask2);

        System.out.println(manager.getAllEpics());
        System.out.println("_____________________________");

        manager.removeTaskById(1);
        manager.removeTaskById(3);

        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllSubtasks());
        System.out.println("_____________________________");
    }
}
