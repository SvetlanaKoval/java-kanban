package managers;

import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File memory;

    public FileBackedTasksManager(File memory) {
        this.memory = memory;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(memory)) {
            writer.write("id,type,name,status,description,epic" + System.lineSeparator());
            Map<Integer, Task> allTasks = unionAllTasks();

            for (Task task : allTasks.values()) {
                String line = fromTaskToString(task);
                writer.write(line + System.lineSeparator());
            }
            writer.write(System.lineSeparator());
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Проблема сохранения в файл");
        }
    }

    private String fromTaskToString(Task task) {
        List<String> taskData = new ArrayList<>();
        taskData.add(String.valueOf(task.getId()));
        taskData.add(String.valueOf(task.getType()));
        taskData.add(task.getName());
        taskData.add(String.valueOf(task.getStatus()));
        taskData.add(task.getDescription());
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
                subtask.setEpicId(Integer.parseInt(taskData[5]));
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
    }

    static String historyToString(HistoryManager manager) {
        List<Task> hystoryList = manager.getHistory();
        List<String> tasksId = new ArrayList<>();
        for (Task task : hystoryList) {
            tasksId.add(String.valueOf(task.getId()));
        }
        return String.join(",", tasksId);
    }

    static List<Integer> historyFromString(String value) {
        String[] stringTaskId = value.split(",");
        List<Integer> taskId = new ArrayList<>();
        for (String stringId : stringTaskId) {
            taskId.add(Integer.valueOf(stringId));
        }
        return taskId;
    }

    static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            List<String> allData = Files.readAllLines(Path.of(file.getPath()));
            for (int taskIndex = 1; taskIndex < allData.size() - 2; taskIndex++) {
                fileBackedTasksManager.fromStringToTask(allData.get(taskIndex));
            }
            int historyLineIndex = allData.size() - 1;
            List<Integer> tasksId = historyFromString(allData.get(historyLineIndex));
            fileBackedTasksManager.loadHistoryTasks(tasksId);
            fileBackedTasksManager.generatorId = fileBackedTasksManager.unionAllTasks().lastKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTasksManager;
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
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    private TreeMap<Integer, Task> unionAllTasks() {
        TreeMap<Integer, Task> allTasks = new TreeMap<>();
        allTasks.putAll(tasks);
        allTasks.putAll(subtasks);
        allTasks.putAll(epics);
        return allTasks;
    }

    private void loadHistoryTasks(List<Integer> tasks) {
        TreeMap<Integer, Task> allTasks = unionAllTasks();
        for (Integer taskId : tasks) {
            Task task = allTasks.get(taskId);
            if (task != null) {
                getHistoryManager().add(task);
            }
        }
    }
}