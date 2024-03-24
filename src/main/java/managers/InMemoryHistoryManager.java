package main.java.managers;

import main.java.tasks.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList tasksHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
    }

    @Override
    public void remove(int id) {
        if (tasksHistory.contains(id)) {
            tasksHistory.removeNode(tasksHistory.getValue(id));
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks();
    }

}