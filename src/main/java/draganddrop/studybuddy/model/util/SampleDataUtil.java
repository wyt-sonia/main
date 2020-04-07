package draganddrop.studybuddy.model.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    private static final String LONG_DESC_WITH_300_CHAR =
        "This is a long task description with 300 characters. 12345678901234567890123456789012345678901234567"
            + "This is a long task description with 300 characters. 12345678901234567890123456789012345678901234567"
            + "This is a long task description with 300 characters. 12345678901234567890123456789012345678901234567";
    private static final String SHORT_DESC = "This is a short description.";

    public static Task[] getSampleTasks() {
        Module emptyModule = new EmptyModule();
        Module cs2101 = new Module("Effective Communication for Computing Professionals",
            "CS2101");
        Module cs2103T = new Module("Software Engineering",
            "CS2103T");
        Module cs2105 = new Module("Introduction to Computer Networks",
            "CS2105");
        Module cs2106 = new Module("Introduction to Operating Systems",
            "CS2106");

        LocalDateTime[] dueSoonDuration1 = {LocalDateTime.now().plusDays(3),
            LocalDateTime.now().plusDays(7)};
        LocalDateTime[] dueSoonDuration2 = {LocalDateTime.now().plusDays(5),
            LocalDateTime.now().plusDays(13)};
        LocalDateTime[] pendingDuration1 = {LocalDateTime.now().plusDays(10),
            LocalDateTime.now().plusDays(17)};
        LocalDateTime[] pendingDuration2 = {LocalDateTime.now().plusDays(8),
            LocalDateTime.now().plusDays(12)};
        LocalDateTime[] overdueDuration1 = {LocalDateTime.now().minusDays(5),
            LocalDateTime.now().minusDays(3)};
        LocalDateTime[] overdueDuration2 = {LocalDateTime.now().minusDays(3),
            LocalDateTime.now().minusDays(1)};

        LocalDateTime[] dueSoonDateTime1 = {LocalDateTime.now().plusDays(3)};
        LocalDateTime[] dueSoonDateTime2 = {LocalDateTime.now().plusDays(5)};
        LocalDateTime[] pendingDateTime1 = {LocalDateTime.now().plusDays(10)};
        LocalDateTime[] pendingDateTime2 = {LocalDateTime.now().plusDays(9)};

        LocalDateTime pastDateTime1 = LocalDateTime.now().minusDays(6);
        LocalDateTime pastDateTime2 = LocalDateTime.now().minusDays(6);


        return new Task[]{
            new Task(emptyModule, TaskType.Others, "Homework 1", SHORT_DESC, 20.0,
                TaskStatus.PENDING, pendingDateTime1, 5.0, pastDateTime2),

            new Task(emptyModule, TaskType.Presentation, "Leadership Presentation 1",
                LONG_DESC_WITH_300_CHAR, 0.0, TaskStatus.FINISHED, dueSoonDuration1,
                3.0, pastDateTime1),

            new Task(cs2101, TaskType.Quiz, "CS2101 Quiz 1", SHORT_DESC,
                15.0, TaskStatus.PENDING, pendingDateTime2, 2.0, pastDateTime1),

            new Task(cs2101, TaskType.Meeting, "Group Meeting 1", LONG_DESC_WITH_300_CHAR, 15.0,
                TaskStatus.FINISHED, dueSoonDuration1, 5.0, pastDateTime2),

            new Task(cs2101, TaskType.Assignment, "CS2101 Ass 1", LONG_DESC_WITH_300_CHAR, 20.0,
                TaskStatus.PENDING, pendingDateTime2, 5.0, pastDateTime1),

            new Task(cs2101, TaskType.Presentation, "Presentation 1",
                LONG_DESC_WITH_300_CHAR, 20.0, TaskStatus.FINISHED, dueSoonDuration1,
                13.0, pastDateTime2),

            new Task(cs2103T, TaskType.Assignment, "CS2103T Ass 1", SHORT_DESC,
                20.0, TaskStatus.PENDING, pendingDateTime1, 5.0, pastDateTime1),

            new Task(cs2103T, TaskType.Meeting, "Group Meeting", LONG_DESC_WITH_300_CHAR, 0.0,
                TaskStatus.OVERDUE, overdueDuration1, 15.0, pastDateTime2),

            new Task(cs2103T, TaskType.Quiz, "CS2103T Quiz 1", SHORT_DESC, 2.0,
                TaskStatus.PENDING, pendingDuration2, 2.0, pastDateTime1),

            new Task(cs2103T, TaskType.Exam, "CS2103T PT",
                LONG_DESC_WITH_300_CHAR, 20.0, TaskStatus.DUE_SOON, dueSoonDuration1,
                3.0, pastDateTime1),

            new Task(cs2105, TaskType.Quiz, "CS2105 Quiz 1", LONG_DESC_WITH_300_CHAR,
                2.0, TaskStatus.PENDING, pendingDateTime2, 5.0, pastDateTime2),

            new Task(cs2105, TaskType.Others, "Lab 2", SHORT_DESC, 20.0,
                TaskStatus.FINISHED, overdueDuration2, 15.0, pastDateTime1),

            new Task(cs2105, TaskType.Assignment, "Quiz 1", SHORT_DESC,
                2.0, TaskStatus.DUE_SOON, dueSoonDateTime2, 1.0, pastDateTime1),

            new Task(cs2105, TaskType.Meeting, "Meeting 1", LONG_DESC_WITH_300_CHAR, 20.0,
                TaskStatus.FINISHED, overdueDuration2, 15.0, pastDateTime2),

            new Task(cs2106, TaskType.Assignment, "Quiz 1", SHORT_DESC,
                2.0, TaskStatus.DUE_SOON, dueSoonDateTime1, 1.0, pastDateTime1),

            new Task(cs2106, TaskType.Meeting, "Meeting 1", LONG_DESC_WITH_300_CHAR, 20.0,
                TaskStatus.OVERDUE, overdueDuration2, 15.0, pastDateTime2),

            new Task(cs2106, TaskType.Quiz, "CS2106 Quiz 1", SHORT_DESC,
                2.0, TaskStatus.PENDING, pendingDateTime1, 4.0, pastDateTime2),

            new Task(cs2106, TaskType.Others, "Lab 4", LONG_DESC_WITH_300_CHAR, 8.0,
                TaskStatus.FINISHED, overdueDuration1, 25.0, pastDateTime2)
        };
    }

    public static Module[] getSampleModule() {
        Module emptyModule = new EmptyModule();
        Module cs2101 = new Module("Effective Communication for Computing Professionals",
                "CS2101");
        Module cs2103T = new Module("Software Engineering",
            "CS2103T");
        Module cs2105 = new Module("Introduction to Computer Networks",
            "CS2105");
        Module cs2106 = new Module("Introduction to Operating Systems",
            "CS2106");
        return new Module[]{emptyModule, cs2101, cs2103T, cs2105, cs2106};
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
