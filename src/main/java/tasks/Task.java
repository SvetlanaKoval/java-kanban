package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {

    private Integer id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

    public Task() {
    }

    public Task(String name, String description) {
        this(name, description, 0, null);
    }

    public Task(String name, String description, long duration) {
        this(name, description, duration, null);
    }

    public Task(String name, String description, long duration, String startTime) {
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime != null ? LocalDateTime.parse(startTime, FORMATTER) : null;
        this.status = Status.NEW;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return Type.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public int compareTo(Task o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                " name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

}