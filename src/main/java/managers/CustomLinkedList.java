package main.java.managers;

import main.java.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList {
    private Node<Task> first;
    private Node<Task> last;
    private final HashMap<Integer, Node<Task>> tasksKeeper;
    private int size;

    public CustomLinkedList() {
        tasksKeeper = new HashMap<>();
    }

    public boolean add(Task task) {
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

    public int size() {
        return size;
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

    public boolean contains(int id) {
        return tasksKeeper.containsKey(id);
    }

    private static class Node<E> {
        E cur;
        Node<E> prev;
        Node<E> next;

        public Node(Node<E> prev, E cur, Node<E> next) {
            this.prev = prev;
            this.cur = cur;
            this.next = next;
        }

    }

}