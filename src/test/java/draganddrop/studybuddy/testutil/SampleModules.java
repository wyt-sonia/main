package draganddrop.studybuddy.testutil;

import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class SampleModules {
    public static Module[] getSampleModule() {
        Module cs2100 = new Module("Computer Organisation", "CS2100");
        Module cs2100clone1 = new Module("dasadsa", "CS2100");
        Module cs2100clone2 = new Module("Computer Organisation", "CS1111");
        return new Module[] {cs2100, cs2100clone1, cs2100clone2, new EmptyModule()};
    }

    public static ObservableList<Task> getSampleTask() {
        Module[] modArr = getSampleModule();
        ObservableList<Task> SampleTaskList = FXCollections.observableArrayList();
        SampleTaskList.add(new Task(modArr[0], TaskType.Assignment, "Ass 1", "taskDescription", 20.0,
                TaskStatus.PENDING, null, 5.0, LocalDateTime.now()));
        SampleTaskList.add(new Task(modArr[0], TaskType.Assignment, "Ass 2", "taskDescription", 20.0,
                TaskStatus.PENDING, null, 5.0, LocalDateTime.now()));
        SampleTaskList.add(new Task(modArr[3], TaskType.Assignment, "Ass 3", "taskDescription", 20.0,
                TaskStatus.PENDING, null, 5.0, LocalDateTime.now()));
        SampleTaskList.add(new Task(modArr[3], TaskType.Assignment, "Ass 4", "taskDescription", 20.0,
                TaskStatus.PENDING, null, 5.0, LocalDateTime.now()));

        return SampleTaskList;
    }

    public static ObservableList<Task> getExpectedOutcomeForFilterFunction() {
        Module[] modArr = getSampleModule();
        ObservableList<Task> SampleTaskList = FXCollections.observableArrayList();
        SampleTaskList.add(new Task(modArr[3], TaskType.Assignment, "Ass 3", "taskDescription", 20.0,
                TaskStatus.PENDING, null, 5.0, LocalDateTime.now()));
        SampleTaskList.add(new Task(modArr[3], TaskType.Assignment, "Ass 4", "taskDescription", 20.0,
                TaskStatus.PENDING, null, 5.0, LocalDateTime.now()));
        return SampleTaskList;
    }
}
