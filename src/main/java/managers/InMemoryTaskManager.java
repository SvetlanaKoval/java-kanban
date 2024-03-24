package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Status;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        removeAllSubtasks();

        epics.keySet()
                .forEach(historyManager::remove);
        epics.clear();

    }

    @Override
    public void removeAllSubtasks() {
        epics.values().stream()
                .peek(Epic::removeSubtasks)
                .forEach(this::setEpicStatus);

        subtasks.keySet()
                .forEach(historyManager::remove);
        subtasks.clear();
    }

    @Override
    public void removeAllTasks() {
        tasks.keySet()
                .forEach(historyManager::remove);
        tasks.clear();
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
        if (subtask.getStartTime() != null && isIntersectExist(subtask)) {
            return;
        }

        checkId(subtask.getEpicId(), epics.keySet());
        subtask.setId(subtask.getId() == null ? generateId() : subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        setEpicStatus(epic);
    }

    @Override
    public void addTask(Task task) {
        if (task.getStartTime() != null && isIntersectExist(task)) {
            return;
        }

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
        return epic.getSubtasks().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return Stream.concat(tasks.values().stream(), subtasks.values().stream())
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private void checkId(int id, Set<Integer> idSet) {
        if (!idSet.contains(id)) {
            throw new RuntimeException("Задача не найдена");
        }
    }

    public void setEpicStatus(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
            epic.setDuration(0);
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }

        List<Subtask> subtasks = getSubtasksByEpic(epic);
        Set<Status> statusSet = subtasks.stream()
                .map(Task::getStatus)
                .collect(Collectors.toSet());

        if (statusSet.size() == 1) {
            epic.setStatus(subtasks.get(0).getStatus());
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        setEpicDuration(epic, subtasks);
        setEpicStartTime(epic, subtasks);
        setEpicEndTime(epic, subtasks);
    }

    private void setEpicDuration(Epic epic, List<Subtask> subtasks) {
        epic.setDuration(subtasks.stream()
                .mapToLong(subtask -> subtask.getDuration().toMinutes())
                .sum());
    }

    private void setEpicStartTime(Epic epic, List<Subtask> subtasks) {
        Optional<LocalDateTime> start = subtasks.stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder());
        start.ifPresent(epic::setStartTime);
    }

    private void setEpicEndTime(Epic epic, List<Subtask> subtasks) {
        Optional<LocalDateTime> start = subtasks.stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder());
        start.ifPresent(epic::setEndTime);
    }

    private int generateId() {
        return generatorId++;
    }

    private boolean isIntersectExist(Task task) {
        return getPrioritizedTasks().stream()
                .anyMatch(t -> isIntersectionTask(task, t));
    }

    private boolean isIntersectionTask(Task task1, Task task2) {
        LocalDateTime maxStart = task1.getStartTime().isAfter(task2.getStartTime()) ?
                task1.getStartTime() : task2.getStartTime();
        LocalDateTime minEnd = task1.getEndTime().isBefore(task2.getEndTime()) ?
                task1.getEndTime() : task2.getEndTime();

        return maxStart.isBefore(minEnd) || maxStart.isEqual(minEnd);
    }

}