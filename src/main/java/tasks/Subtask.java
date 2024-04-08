package tasks;

import java.util.Objects;

public class Subtask extends Task {

    private Integer epicId;

    public Subtask() {
    }

    public Subtask(String name, String description, int epicId, long duration) {
        this(name, description, epicId, duration, null);
    }

    public Subtask(String name, String description, int epicId, long duration, String startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subtask subtask = (Subtask) o;
        return super.equals(o) && Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
            " name='" + this.getName() + '\'' +
            ", description='" + this.getDescription() + '\'' +
            ", id=" + this.getId() +
            ", status= " + this.getStatus() +
            ", epicId= " + this.epicId +
            '}';
    }

}