package draganddrop.studybuddy.model.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;

/**
 * Contains utility methods for populating {@code StudyBuddy} with sample data.
 */
public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        Module emptyModule = new EmptyModule();
        Module cs2101 = new Module("Effective Communication for Computing Professionals",
            "CS2101");
        LocalDateTime[] dateTimesOne = {TimeParser.parseDateTime("23:59 12/12/2020"),
            TimeParser.parseDateTime("23:59 21/12/2020")};
        LocalDateTime[] dateTimesTwo = {TimeParser.parseDateTime("23:59 12/04/2020"),
            TimeParser.parseDateTime("23:59 21/04/2020")};
        LocalDateTime creationDateTime = LocalDateTime.now();


        return new Task[]{
            new Task(emptyModule, TaskType.Assignment, "Ass 1", "taskDescription", 20.0,
                TaskStatus.PENDING, dateTimesOne, 5.0, creationDateTime),
            new Task(cs2101, TaskType.Presentation, "Presentation 1",
                "Presentation taskDescription", 20.0, TaskStatus.FINISHED, dateTimesOne,
                3.0, creationDateTime),
            new Task(cs2101, TaskType.Assignment, "Quiz 1", "Quiz taskDescription",
                2.0, TaskStatus.PENDING, dateTimesTwo, 1.0, creationDateTime),
            new Task(cs2101, TaskType.Meeting, "Meeting 1", "Meeting desc", 20.0,
                TaskStatus.PENDING, dateTimesTwo, 15.0, creationDateTime)
        };
    }

    public static Module[] getSampleModule() {
        Module emptyModule = new EmptyModule();
        Module cs2101 = new Module("Effective Communication for Computing Professionals",
                "CS2101");
        return new Module[]{emptyModule, cs2101};

    }


    public static ReadOnlyStudyBuddy getSampleStudyBuddy() {
        StudyBuddy sampleSb = new StudyBuddy();
        ArrayList<Task> sampleTasks = new ArrayList<>();
        for (Task sampleTask : getSampleTasks()) {
            sampleSb.addTask(sampleTask);
            sampleTasks.add(sampleTask);
        }

        for (Module mod: getSampleModule()) {
            sampleSb.addModule(mod);
        }
        Task.updateCurrentTaskList(sampleTasks);
        return sampleSb;
    }
}
