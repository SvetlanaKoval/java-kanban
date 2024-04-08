package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Integer> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public List<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(int id) {
        subtasks.add(id);
    }

    public void removeSubtask(int id) {
        subtasks.remove((Integer) id);
    }

    public void removeSubtasks() {
        subtasks.clear();
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return super.equals(o) && Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                " name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                " ,status=" + this.getStatus() +
                ", subtasks=" + subtasks +
                '}';
    }

}