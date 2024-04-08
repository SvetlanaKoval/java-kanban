package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Task> getAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void removeAllTasks();

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    Task getTaskById(int id);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void addTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateTask(Task task);

    void removeEpicById(int id);

    void removeSubtaskById(int id);

    void removeTaskById(int id);

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getHistory();

    void setEpicStatus(Epic epic);

    TreeSet<Task> getPrioritizedTasks();

}
