import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();

    public List<Epic> getAllEpics() {
        List<Epic> allEpics = new ArrayList<>();
        for (Task epic : tasks.values()) {
            if (epic instanceof Epic) {
                allEpics.add((Epic) epic);
            }
        }
        return allEpics;
    }

    public List<Subtask> getAllSubtasks() {
        List<Subtask> allSubtask = new ArrayList<>();
        for (Task subtask : tasks.values()) {
            if (subtask instanceof Subtask) {
                allSubtask.add((Subtask) subtask);
            }
        }
        return allSubtask;
    }

    public List<Task> getAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getClass() == Task.class) {
                allTasks.add(task);
            }
        }
        return allTasks;
    }

    public void removeAllEpics() {
        for (Epic epic : getAllEpics()) {
            List<Subtask> subtasks = getSubtasksByEpic(epic);
            for (Subtask subtask : subtasks) {
                tasks.remove(subtask.getId());
            }
            removeTaskById(epic.getId());
        }
    }

    public void removeAllSubtasks() {
        for (Subtask subtask : getAllSubtasks()) {
            removeTaskById(subtask.getId());
        }
    }

    public void removeAllTasks() {
        for (Task task : getAllTasks()) {
            removeTaskById(task.getId());
        }
    }

    public Epic getEpicById(int id) {
        return (Epic) getTaskById(id);
    }

    public Subtask getSubtaskById(int id) {
        return (Subtask) getTaskById(id);
    }

    public Task getTaskById(int id) {
        checkTask(id);
        return tasks.get(id);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(int id, Task task) {
        checkTask(id);
        tasks.put(id, task);
    }

    public void removeTaskById(int id) {
        checkTask(id);
        Task task = tasks.get(id);
        if (task.getClass() == Epic.class) {
            List<Integer> subtasksId = ((Epic) task).getSubtasksId();
            for (Integer i : subtasksId) {
                tasks.remove(i);
            }
        }
        tasks.remove(id);
    }

    public List<Subtask> getSubtasksByEpic(Epic epic) {
        List<Subtask> subtasks = new ArrayList<>();
        for (Integer id : epic.getSubtasksId()) {
            subtasks.add((Subtask) tasks.get(id));
        }
        return subtasks;
    }

    public void setTaskStatus(Status status, Task task) {
        if (task instanceof Subtask) {
            Epic epic = ((Subtask) task).getEpic();
            task.setStatus(status);
            setEpicStatus(status, epic);
        } else if (task instanceof Epic) {
            setEpicStatus(status, (Epic) task);
        } else {
            task.setStatus(status);
        }
    }

    private void checkTask(int id) {
        if (!tasks.containsKey(id)) {
            throw new RuntimeException("Задача не найдена");
        }
    }

    private void setEpicStatus(Status status, Epic epic) {
        List<Subtask> subtasks = getSubtasksByEpic(epic);
        int counter = statusCounter(status, subtasks);
        Status epicStatus = epic.getStatus();

        switch (status) {
            case NEW:
            case DONE:
                if (subtasks.isEmpty() || counter == subtasks.size()) {
                    epicStatus = status;
                    break;
                }
            case IN_PROGRESS:
                if (subtasks.isEmpty() || counter > 0) {
                    epicStatus = status;
                    break;
                }
        }
        epic.setStatus(epicStatus);
    }

    private int statusCounter(Status status, List<Subtask> tasks) {
        int counter = 0;
        for (Task task : tasks) {
            if (task.getStatus() == status) {
                counter++;
            }
        }
        return counter;
    }
}