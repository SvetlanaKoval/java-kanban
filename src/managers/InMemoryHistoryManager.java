package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    static final int TASKS_HISTORY = 10;
    private final CustomLinkedList tasksHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        tasksHistory.add(task);
    }

    @Override
    public void remove(int id) {
        tasksHistory.removeNode(tasksHistory.getValue(id));
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks();
    }

    public static class CustomLinkedList {
        private Node<Task> first;
        private Node<Task> last;
        private final HashMap<Integer, Node<Task>> tasksKeeper;

        private int size;

        public CustomLinkedList() {
            tasksKeeper = new HashMap<>(TASKS_HISTORY);
        }

        public boolean add(Task task) {
            if (size >= TASKS_HISTORY) {
                removeNode(first.cur);
            }
            if (!tasksKeeper.isEmpty() && tasksKeeper.containsKey(task.getId())) {
                removeNode(task);
            }
            linkLast(task);
            return true;
        }

        public List<Task> getTasks() {
            List<Task> tasks = new ArrayList<>();
            for (Node<Task> node = first; node != null; ) {
                tasks.add(node.cur);
                node = node.next;
            }
            return tasks;
        }

        public void removeNode(Task task) {
            Node<Task> node = tasksKeeper.get(task.getId());
            Node<Task> prev = node.prev;
            Node<Task> next = node.next;

            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
            }

            tasksKeeper.remove(node.cur.getId());
            size--;
        }

        public Task getValue(Integer key) {
            return tasksKeeper.get(key).cur;
        }

        private void linkLast(Task task) {
            final Node<Task> l = last;
            final Node<Task> newTask = new Node<>(l, task, null);
            last = newTask;
            if (l == null) {
                first = newTask;
            } else {
                l.next = newTask;
            }
            tasksKeeper.put(task.getId(), newTask);
            size++;
        }
    }
}