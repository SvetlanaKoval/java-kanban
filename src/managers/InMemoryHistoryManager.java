package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    static final int TASKS_HISTORY = 10;
    private final LinkedList<Task> tasksHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (tasksHistory.size() == TASKS_HISTORY) {
            tasksHistory.removeFirst();
        }
        tasksHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }
}