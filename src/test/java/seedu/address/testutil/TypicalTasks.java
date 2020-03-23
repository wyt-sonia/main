package seedu.address.testutil;

import java.time.LocalDateTime;

import seedu.address.logic.parser.TimeParser;
import seedu.address.model.AddressBook;
import seedu.address.model.module.Module;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;
import seedu.address.model.task.TaskType;

/**
 * represents a typical tasklist.
 */
public class TypicalTasks {
    public static Task[] getSampleTasks() {
        Module cs2103T = new Module("Software Engineering", "CS2103T");
        Module cs2101 = new Module("Effective Communication for Computing Professionals",
            "CS2101");
        LocalDateTime[] dateTimesOne = {TimeParser.parseDateTime("23:59 12/12/2020"),
            TimeParser.parseDateTime("23:59 21/12/2020")};
        LocalDateTime[] dateTimesTwo = {TimeParser.parseDateTime("23:59 12/04/2020"),
            TimeParser.parseDateTime("23:59 21/04/2020"), LocalDateTime.now()};


        return new Task[] {
            new Task(cs2103T, TaskType.Assignment, "Ass 1", "taskDescription", 20.0,
                TaskStatus.PENDING, dateTimesOne, 5.0, LocalDateTime.now()),
            new Task(cs2101, TaskType.Presentation, "Presentation 1",
                "Presentation taskDescription", 20.0, TaskStatus.FINISHED, dateTimesOne,
                3.0, LocalDateTime.now()),
            new Task(cs2103T, TaskType.Assignment, "Quiz 1", "Quiz taskDescription",
                2.0, TaskStatus.PENDING, dateTimesTwo, 1.0, LocalDateTime.now()),
            new Task(cs2101, TaskType.Meeting, "Meeting 1", "Meeting desc", 20.0,
                TaskStatus.PENDING, dateTimesTwo, 15.0, LocalDateTime.now())
        };
    }

    public static AddressBook getTypicalTaskList() {
        AddressBook sampleAb = new AddressBook();
        for (Task sampleTask : getSampleTasks()) {
            sampleAb.addTask(sampleTask);
        }
        return sampleAb;
    }
}
