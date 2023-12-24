package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int generatorId = 1;

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public void removeAllEpics() {
        for (Integer epicId : epics.keySet()) {
            for (Integer subtaskId : epics.get(epicId).getSubtasks()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.removeSubtasks();
            setEpicStatus(epic);
        }

        for (Integer subtaskId : subtasks.keySet()) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void removeAllTasks() {
        for (Integer taskId : tasks.keySet()) {
            tasks.remove(taskId);
            historyManager.remove(taskId);
        }
    }

    @Override
    public Epic getEpicById(int id) {
        checkId(id, epics.keySet());
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        checkId(id, subtasks.keySet());
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Task getTaskById(int id) {
        checkId(id, tasks.keySet());
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(epic.getId() == null ? generateId() : epic.getId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        checkId(subtask.getEpicId(), epics.keySet());
        subtask.setId(subtask.getId() == null ? generateId() : subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        setEpicStatus(epic);
    }

    @Override
    public void addTask(Task task) {
        task.setId(task.getId() == null ? generateId() : task.getId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        checkId(epic.getId(), epics.keySet());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        checkId(subtask.getId(), subtasks.keySet());
        checkId(subtask.getEpicId(), epics.keySet());
        subtasks.put(subtask.getId(), subtask);
        setEpicStatus(epics.get(subtask.getEpicId()));
    }

    @Override
    public void updateTask(Task task) {
        checkId(task.getId(), tasks.keySet());
        tasks.put(task.getId(), task);
    }

    @Override
    public void removeEpicById(int id) {
        checkId(id, epics.keySet());
        List<Integer> subtaskList = epics.get(id).getSubtasks();
        for (Integer subtaskId : subtaskList) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        checkId(id, subtasks.keySet());
        int epicId = subtasks.get(id).getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubtask(id);
        subtasks.remove(id);
        setEpicStatus(epic);
        historyManager.remove(id);
    }

    @Override
    public void removeTaskById(int id) {
        checkId(id, tasks.keySet());
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasks()) {
            subtaskList.add(subtasks.get(subtaskId));
        }
        return subtaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
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