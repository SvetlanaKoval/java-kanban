package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(new File("123.csv"));
    }

    @Test
    public void checkSavetoFile() {
        FileBackedTasksManager fileBackedTasksManager = createTaskManager();
        Assertions.assertDoesNotThrow(fileBackedTasksManager::save);
    }

    @Test
    public void checkLoadFromNotExistFile() {
        Assertions.assertThrows(RuntimeException.class, () -> FileBackedTasksManager.loadFromFile(new File("555.csv")));
    }

    @Test
    public void checkLoadFromEmptyFile() {
        Assertions.assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(new File("222.csv")));
    }

}