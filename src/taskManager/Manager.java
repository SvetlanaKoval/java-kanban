package taskManager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class Manager {

    private int generatorId = 1;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            setEpicStatus(epic);
        }
        subtasks.clear();
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Epic getEpicById(int id) {
        checkId(id, epics.keySet());
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        checkId(id, subtasks.keySet());
        return subtasks.get(id);
    }

    public Task getTaskById(int id) {
        checkId(id, tasks.keySet());
        return tasks.get(id);
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        checkId(subtask.getEpicId(), epics.keySet());
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        setEpicStatus(epic);
    }

    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        checkId(epic.getId(), epics.keySet());
        epics.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        checkId(subtask.getId(), subtasks.keySet());
        checkId(subtask.getEpicId(), epics.keySet());
        subtasks.put(subtask.getId(), subtask);
        setEpicStatus(epics.get(subtask.getEpicId()));
    }

    public void updateTask(Task task) {
        checkId(task.getId(), tasks.keySet());
        tasks.put(task.getId(), task);
    }

    public void removeEpicById(int id) {
        checkId(id, epics.keySet());
        List<Integer> subtaskList = epics.get(id).getSubtasks();
        for (Integer subtaskId : subtaskList) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void removeSubtaskById(int id) {
        checkId(id, subtasks.keySet());
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubtask(id);
        subtasks.remove(id);
        setEpicStatus(epic);
    }

    public void removeTaskById(int id) {
        checkId(id, tasks.keySet());
        tasks.remove(id);
    }

    public List<Subtask> getSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasks()) {
            subtaskList.add(subtasks.get(subtaskId));
        }
        return subtaskList;
    }

    private void checkId(int id, Set<Integer> idSet) {
        if (!idSet.contains(id)) {
            throw new RuntimeException("Задача не найдена");
        }
    }

    private void setEpicStatus(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        List<Subtask> subtasks = getSubtasksByEpic(epic);
        Set<Status> statusSet = new HashSet<>();
        for (Subtask subtask : subtasks) {
            statusSet.add(subtask.getStatus());
        }

        if (statusSet.size() == 1) {
            epic.setStatus(subtasks.get(0).getStatus());
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private int generateId() {
        return generatorId++;
    }
}