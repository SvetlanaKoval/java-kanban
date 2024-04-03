package main.java.managers;

import main.java.tasks.Epic;
import main.java.tasks.Status;
import main.java.tasks.Subtask;
import main.java.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {

    protected int generatorId = 1;

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>();

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

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
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
        updatePrioritizedTasks();
    }

    @Override
    public void removeAllTasks() {
        tasks.keySet()
                .forEach(historyManager::remove);
        tasks.clear();
        updatePrioritizedTasks();
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
        checkTimeIntersection(subtask);

        checkId(subtask.getEpicId(), epics.keySet());
        subtask.setId(subtask.getId() == null ? generateId() : subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        setEpicStatus(epic);
        updatePrioritizedTasks();
    }

    @Override
    public void addTask(Task task) {
        checkTimeIntersection(task);

        task.setId(task.getId() == null ? generateId() : task.getId());
        tasks.put(task.getId(), task);
        updatePrioritizedTasks();
    }

    @Override
    public void updateEpic(Epic epic) {
        checkId(epic.getId(), epics.keySet());
        epics.put(epic.getId(), epic);
        updatePrioritizedTasks();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        checkId(subtask.getId(), subtasks.keySet());
        checkId(subtask.getEpicId(), epics.keySet());
        subtasks.put(subtask.getId(), subtask);
        setEpicStatus(epics.get(subtask.getEpicId()));
        updatePrioritizedTasks();
    }

    @Override
    public void updateTask(Task task) {
        checkId(task.getId(), tasks.keySet());
        tasks.put(task.getId(), task);
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
    }

    @Override
    public void removeTaskById(int id) {
        checkId(id, tasks.keySet());
        tasks.remove(id);
        historyManager.remove(id);
        updatePrioritizedTasks();
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

    private void updatePrioritizedTasks() {
        prioritizedTasks = Stream.concat(tasks.values().stream(), subtasks.values().stream())
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
        setEpicTime(epic, subtasks);
    }

    private void setEpicTime(Epic epic, List<Subtask> subtasks) {
        epic.setDuration(subtasks.stream()
                .mapToLong(subtask -> subtask.getDuration().toMinutes())
                .sum());

        getPrioritizedTime(subtasks, Task::getStartTime, Comparator.naturalOrder()).ifPresent(epic::setStartTime);
        getPrioritizedTime(subtasks, Task::getEndTime, Comparator.reverseOrder()).ifPresent(epic::setEndTime);
    }

    private Optional<LocalDateTime> getPrioritizedTime(List<Subtask> subtasks,
                                                       Function<Task, LocalDateTime> function,
                                                       Comparator<LocalDateTime> comparator) {
        return subtasks.stream()
                .map(function)
                .filter(Objects::nonNull)
                .min(comparator);
    }

    private int generateId() {
        return generatorId++;
    }

    private void checkTimeIntersection(Task task) {
        if (task.getStartTime() == null) {
            return;
        }

        getPrioritizedTasks().stream()
                .filter(t -> isIntersectionTask(task, t))
                .findAny()
                .ifPresent(t -> {
                    throw new IllegalArgumentException("Попытка добавить пересекающуюся задачу");
                });
    }

    private boolean isIntersectionTask(Task task1, Task task2) {
        LocalDateTime maxStart = task1.getStartTime().isAfter(task2.getStartTime()) ?
                task1.getStartTime() : task2.getStartTime();
        LocalDateTime minEnd = task1.getEndTime().isBefore(task2.getEndTime()) ?
                task1.getEndTime() : task2.getEndTime();

        return maxStart.isBefore(minEnd) || maxStart.isEqual(minEnd);
    }

}