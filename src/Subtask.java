import java.util.Objects;

public class Subtask extends Task {

    private final Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        if (epic == null) {
            throw new RuntimeException("У этой подзадачи нет основной задачи. Уточните данные");
        }
        this.epic = epic;
        epic.getSubtasksId().add(getId());
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return super.equals(o) && Objects.equals(epic, subtask.epic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epic);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                " name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status= " + this.getStatus() +
                ", epic= " + this.epic.getName() +
                '}';
    }
}