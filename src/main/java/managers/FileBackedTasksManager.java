package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import tasks.Type;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public static final String FIELDS_NAME = "id,type,name,status,description,duration,startTime,epic" + System.lineSeparator();
    private final File memory;

    public FileBackedTasksManager(File memory) {
        this.memory = memory;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(memory)) {
            writer.write(FIELDS_NAME);
            Map<Integer, Task> allTasks = unionAllTasks();

            for (Task task : allTasks.values()) {
                String line = fromTaskToString(task);
                writer.write(line + System.lineSeparator());
            }
            if (!getHistory().isEmpty()) {
                writer.write(System.lineSeparator());
                writer.write(historyToString(getHistoryManager()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Проблема сохранения в файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            List<String> allData = Files.readAllLines(Path.of(file.getPath()));

            if (allData.size() <= 1) {
                throw new ManagerSaveException("В файл загружена некорректная информация.");
            }

            int taskIndex = 1;
            while (taskIndex < allData.size()) {
                String line = allData.get(taskIndex);
                if (line.isBlank()) {
                    taskIndex++;
                    break;
                }
                Task task = fileBackedTasksManager.fromStringToTask(line);
                fileBackedTasksManager.generatorId = task.getId();
                taskIndex++;
            }

            if (taskIndex < allData.size()) {
                String historyLine = allData.get(taskIndex);
                if (!historyLine.isBlank()) {
                    List<Integer> tasksIdList = historyFromString(historyLine);
                    fileBackedTasksManager.loadHistoryTasks(tasksIdList);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTasksManager;
    }

    private String fromTaskToString(Task task) {
        List<String> taskData = new ArrayList<>();
        taskData.add(task.getId().toString());
        taskData.add(task.getType().toString());
        taskData.add(task.getName());
        taskData.add(task.getStatus().toString());
        taskData.add(task.getDescription());
        taskData.add(String.valueOf(task.getDuration().toMinutes()));
        LocalDateTime startTime = task.getStartTime();
        taskData.add(startTime != null ? startTime.toString() : null);

        if (task instanceof Subtask) {
            taskData.add(String.valueOf(((Subtask) task).getEpicId()));
        }

        return String.join(",", taskData);
    }

    private Task fromStringToTask(String line) {
        String[] taskData = line.split(",");
        Type type = Type.valueOf(taskData[1]);
        switch (type) {
            case EPIC:
                Epic epic = new Epic();
                setAllFields(epic, taskData);
                super.addEpic(epic);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask();
                setAllFields(subtask, taskData);
                subtask.setEpicId(Integer.parseInt(taskData[7]));
                super.addSubtask(subtask);
                return subtask;
            default:
                Task task = new Task();
                setAllFields(task, taskData);
                super.addTask(task);
                return task;
        }
    }

    private static void setAllFields(Task task, String[] taskData) {
        task.setId(Integer.parseInt(taskData[0]));
        task.setName(taskData[2]);
        task.setStatus(Status.valueOf(taskData[3]));
        task.setDescription(taskData[4]);
        task.setDuration(Long.parseLong(taskData[5]));
        task.setStartTime(LocalDateTime.parse(taskData[7]));
    }

    private static String historyToString(HistoryManager manager) {
        List<Task> hystoryList = manager.getHistory();
        List<String> tasksId = new ArrayList<>();
        for (Task task : hystoryList) {
            tasksId.add(String.valueOf(task.getId()));
        }
        return String.join(",", tasksId);
    }

    private static List<Integer> historyFromString(String value) {
        String[] stringTaskId = value.split(",");
        List<Integer> taskId = new ArrayList<>();
        for (String stringId : stringTaskId) {
            taskId.add(Integer.valueOf(stringId));
        }
        return taskId;
    }

    private TreeMap<Integer, Task> unionAllTasks() {
        TreeMap<Integer, Task> allTasks = new TreeMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(subtasks);
        allTasks.putAll(epics);
        return allTasks;
    }

    private void loadHistoryTasks(List<Integer> taskIdList) {
        TreeMap<Integer, Task> allTasks = unionAllTasks();
        for (Integer taskId : taskIdList) {
            Task task = allTasks.get(taskId);
            if (task != null) {
                getHistoryManager().add(task);
            }
        }
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epicById = super.getEpicById(id);
        save();
        return epicById;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtaskById = super.getSubtaskById(id);
        save();
        return subtaskById;
    }

    @Override
    public Task getTaskById(int id) {
        Task taskById = super.getTaskById(id);
        save();
        return taskById;
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Epic removeEpicById(int id) {
        Epic deletedEpic = super.removeEpicById(id);
        save();
        return deletedEpic;
    }

    @Override
    public Subtask removeSubtaskById(int id) {
        Subtask deletedSubtask = super.removeSubtaskById(id);
        save();
        return deletedSubtask;
    }

    @Override
    public Task removeTaskById(int id) {
        Task deletedTask = super.removeTaskById(id);
        save();
        return deletedTask;
    }

}