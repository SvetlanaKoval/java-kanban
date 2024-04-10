package exceptions;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String type, String id) {
        super(String.format("%s %s не найдена", type, id));
    }
}
