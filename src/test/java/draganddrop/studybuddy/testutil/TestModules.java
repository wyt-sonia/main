package draganddrop.studybuddy.testutil;

import java.time.LocalDateTime;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A sample class for module testing. Includes modules and tasks.
 */
public class TestModules {
    private static LocalDateTime[] dateTimesOne = {TimeParser.parseDateTime("23:59 12/12/2020"),
            TimeParser.parseDateTime("23:59 21/12/2020")};
    private static LocalDateTime[] dateTimesTwo = {TimeParser.parseDateTime("23:59 12/04/2020"),
            TimeParser.parseDateTime("23:59 21/04/2020"), LocalDateTime.now()};

    public static Module[] getSampleModule() {
        Module cs2100 = new Module("Computer Organisation", "CS2100");
        Module cs2100clone1 = new Module("dasadsa", "CS2100");
        Module cs2100clone2 = new Module("Computer Organisation", "CS1111");
        return new Module[] {cs2100, cs2100clone1, cs2100clone2, new EmptyModule()};
    }

    public static ObservableList<Task> getSampleTask() {
        Module[] modArr = getSampleModule();
        ObservableList<Task> sampleTaskList = FXCollections.observableArrayList();
        sampleTaskList.add(new Task(modArr[0], TaskType.Assignment, "Ass 1", "taskDescription", 20.0,
                TaskStatus.PENDING, dateTimesOne, 5.0, LocalDateTime.now()));
        sampleTaskList.add(new Task(modArr[0], TaskType.Presentation, "Presentation 1",
                "Presentation taskDescription", 20.0, TaskStatus.FINISHED, dateTimesOne,
                3.0, LocalDateTime.now()));
        sampleTaskList.add(new Task(modArr[3], TaskType.Assignment, "Quiz 1", "Quiz taskDescription",
                2.0, TaskStatus.PENDING, dateTimesTwo, 1.0, LocalDateTime.now()));
        sampleTaskList.add(new Task(modArr[3], TaskType.Meeting, "Meeting 1", "Meeting desc", 20.0,
                TaskStatus.PENDING, dateTimesTwo, 15.0, LocalDateTime.now()));

        return sampleTaskList;
    }
}
