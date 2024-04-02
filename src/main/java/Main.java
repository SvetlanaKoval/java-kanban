package main.java;

public class Main {

    public static final String FILE_NAME = "123.csv";

//    public static void main(String[] args) {
//        FileBackedTasksManager fileBackedTasksManager = getFileBackedTaskManager();
//        fileBackedTasksManager.getTaskById(2);
//        fileBackedTasksManager.getSubtaskById(6);
//        fileBackedTasksManager.getEpicById(3);
//        fileBackedTasksManager.getTaskById(1);
//        fileBackedTasksManager.getSubtaskById(7);
//        fileBackedTasksManager.getSubtaskById(5);
//        fileBackedTasksManager.getEpicById(4);
//
//        fileBackedTasksManager.save();
//
//        FileBackedTasksManager fileBackedTasksManagerAfterLoad = FileBackedTasksManager.loadFromFile(new File(FILE_NAME));
//
//        List<Task> beforeLoadTasks = fileBackedTasksManager.getAllTasks();
//        List<Epic> beforeLoadEpics = fileBackedTasksManager.getAllEpics();
//        List<Subtask> beforeLoadSubtask = fileBackedTasksManager.getAllSubtasks();
//        List<Task> beforeLoadHistory = fileBackedTasksManager.getHistory();
//
//        List<Task> afterLoadTasks = fileBackedTasksManagerAfterLoad.getAllTasks();
//        List<Epic> afterLoadEpics = fileBackedTasksManagerAfterLoad.getAllEpics();
//        List<Subtask> afterLoadSubtasks = fileBackedTasksManagerAfterLoad.getAllSubtasks();
//        List<Task> afterLoadHistory = fileBackedTasksManagerAfterLoad.getHistory();
//
//        System.out.println("Количество задач после выгрузки из файла  не изменилось - " +
//                (beforeLoadTasks.size() == afterLoadTasks.size()));
//        printCompareTasks(beforeLoadTasks, afterLoadTasks);
//        System.out.println("Количество эпиков после выгрузки из файла не изменилось - " +
//                (beforeLoadEpics.size() == afterLoadEpics.size()));
//        printCompareTasks(beforeLoadEpics, afterLoadEpics);
//        System.out.println("Количество подзадач после выгрузки из файла не изменилось - " +
//                (beforeLoadSubtask.size() == afterLoadSubtasks.size()));
//        printCompareTasks(beforeLoadSubtask, afterLoadSubtasks);
//        System.out.println("Количество просмотров после выгрузки из файла не изменилось - " +
//                (beforeLoadHistory.size() == afterLoadHistory.size()));
//        printCompareTasks(beforeLoadHistory, afterLoadHistory);
//    }
//
//    private static void printCompareTasks(List<? extends Task> beforeLoadTasks, List<? extends Task> afterLoadTasks) {
//        System.out.println("До загрузки - После загрузки");
//        for (int i = 0; i < beforeLoadTasks.size(); i++) {
//            System.out.println(beforeLoadTasks.get(i).getName() + " - " + afterLoadTasks.get(i).getName());
//        }
//        System.out.println("______________________________________");
//    }
//
//    private static FileBackedTasksManager getFileBackedTaskManager() {
//        FileBackedTasksManager fileBackedTaskManager = new FileBackedTasksManager(new File(FILE_NAME));
//        Task task1 = new Task("task1", "task1task1task1");
//        Task task2 = new Task("task2", "task2task2task2");
//        fileBackedTaskManager.addTask(task1);
//        fileBackedTaskManager.addTask(task2);
//
//        Epic epic1 = new Epic("epic1", "epic1epic1epic1");
//        Epic epic2 = new Epic("epic2", "epic2epic2epic2");
//        fileBackedTaskManager.addEpic(epic1);
//        fileBackedTaskManager.addEpic(epic2);
//
//        Subtask subtask1 = new Subtask("subtask1", "subtask1subtask1subtask1", epic1.getId());
//        Subtask subtask2 = new Subtask("subtask2", "subtask2subtask2subtask2", epic1.getId());
//        Subtask subtask3 = new Subtask("subtask3", "subtask3subtask3subtask3", epic1.getId());
//        fileBackedTaskManager.addSubtask(subtask1);
//        fileBackedTaskManager.addSubtask(subtask2);
//        fileBackedTaskManager.addSubtask(subtask3);
//        return fileBackedTaskManager;
//    }

}