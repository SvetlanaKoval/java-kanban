package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private int tasksCount = 10;
    protected LinkedList<Task> tasksHistory = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (tasksHistory.size() >= tasksCount) {
            tasksHistory.poll();
        }
        tasksHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }
}
