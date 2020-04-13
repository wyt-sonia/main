package draganddrop.studybuddy.testutil;

import java.time.LocalDateTime;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;

/**
 * represents a typical task list.
 */
public class TypicalTasks {

    private static final String LONG_DESC_WITH_300_CHAR =
        "This is a long task description with 300 characters. 12345678901234567890123456789012345678901234567"
            + "This is a long task description with 300 characters. 12345678901234567890123456789012345678901234567"
            + "This is a long task description with 300 characters. 12345678901234567890123456789012345678901234567";
    private static final String SHORT_DESC = "This is a short description.";

    private static final Module emptyModule = new EmptyModule();
    private static final Module cs2103T = new Module("Software Engineering", "CS2103T");
    private static final Module cs2101 = new Module("Effective Communication for Computing Professionals",
        "CS2101");

    private static final LocalDateTime pastDateTime1 = TimeParser.parseDateTime("20:22 02/04/2020");

    private static final LocalDateTime[] dateTimesOne = {TimeParser.parseDateTime("23:59 12/12/2020"),
        TimeParser.parseDateTime("23:59 21/12/2020")};
    private static final LocalDateTime[] dateTimesTwo = {TimeParser.parseDateTime("23:59 12/04/2020"),
        TimeParser.parseDateTime("23:59 21/04/2020")};

    private static final LocalDateTime creationDateTime = LocalDateTime.now().minusDays(6);

    private static final LocalDateTime[] dueSoonDuration1 = {LocalDateTime.now().plusDays(3),
        LocalDateTime.now().plusDays(7)};
    private static final LocalDateTime[] pendingDuration1 = {LocalDateTime.now().plusDays(10),
        LocalDateTime.now().plusDays(17)};


    public static Task[] getSampleTasks() {

        return new Task[]{
            new Task(emptyModule, TaskType.Others, "Homework 1", SHORT_DESC, 20.0,
                TaskStatus.PENDING, pendingDuration1, 5.0, pastDateTime1),
            new Task(emptyModule, TaskType.Presentation, "Leadership Presentation 1",
                LONG_DESC_WITH_300_CHAR, 0.0, TaskStatus.FINISHED, dueSoonDuration1,
                3.0, pastDateTime1),
            new Task(cs2101, TaskType.Assignment, "CS2101 Quiz 1", SHORT_DESC,
                15.0, TaskStatus.PENDING, dateTimesOne, 2.0, pastDateTime1),
            new Task(cs2101, TaskType.Meeting, "Group Meeting 1", LONG_DESC_WITH_300_CHAR, 15.0,
                TaskStatus.FINISHED, dueSoonDuration1, 5.0, pastDateTime1),
            new Task(cs2101, TaskType.Quiz, "CS2101 Quiz 1", SHORT_DESC,
                15.0, TaskStatus.PENDING, pendingDuration1, 2.0, pastDateTime1)
        };
    }

    public static Task[] getSampleArchivedTasks() {

        return new Task[]{
            new Task(cs2103T, TaskType.Assignment, "Ass 2", "taskDescription", 20.0,
                TaskStatus.PENDING, dateTimesOne, 5.0, creationDateTime),
            new Task(cs2101, TaskType.Presentation, "Presentation 2",
                "Presentation taskDescription", 20.0, TaskStatus.FINISHED, dateTimesOne,
                3.0, creationDateTime),
            new Task(cs2103T, TaskType.Assignment, "Quiz 2", "Quiz taskDescription",
                2.0, TaskStatus.PENDING, dateTimesTwo, 1.0, creationDateTime),
            new Task(cs2101, TaskType.Meeting, "Meeting 2", "Meeting desc", 20.0,
                TaskStatus.PENDING, dateTimesTwo, 15.0, creationDateTime),
            new Task(cs2103T, TaskType.Assignment, "Quiz 2", "Quiz taskDescription",
                2.0, TaskStatus.PENDING, dateTimesTwo, 1.0, creationDateTime),
        };
    }

    public static StudyBuddy getTypicalTaskList() {
        StudyBuddy sampleAb = new StudyBuddy();
        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        return sampleAb;
    }

    public static StudyBuddy getOnlyTaskList() {
        StudyBuddy sampleAb = new StudyBuddy();
        Task testTask1 = new Task(emptyModule, TaskType.Presentation, "Leadership Presentation 1",
                LONG_DESC_WITH_300_CHAR, 0.0, TaskStatus.FINISHED, dateTimesOne,
                3.0, pastDateTime1);
        Task testTask2 = new Task(emptyModule, TaskType.Presentation, "Leadership Presentation 2",
                LONG_DESC_WITH_300_CHAR, 0.0, TaskStatus.FINISHED, dateTimesOne,
                3.0, pastDateTime1);
        sampleAb.addTask(testTask1);
        sampleAb.addArchiveTask(testTask2);
        return sampleAb;
    }
}
