package seedu.address.testutil;

import java.time.LocalDateTime;

import seedu.address.logic.parser.TimeParser;
import seedu.address.model.module.Module;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;
import seedu.address.model.task.TaskType;

/**
 * Utility class to help build tasks.
 */
public class TaskBuilder {

    public static final String DEFAULT_MODULE = "CS2103T";
    public static final String DEFAULT_NAME = "Team project";
    public static final String DEFAULT_TASKDESCRIPTION = "Team project";
    public static final String DEFAULT_ESTIMATEDTIME = "45 hours";

    public static final TaskType DEFAULT_TASKTYPE = TaskType.Others;
    public static final TaskStatus DEFAULT_TASKSTATUS = TaskStatus.PENDING;
    public static final double DEFAULT_TASKWEIGHT = 45.0;
    public static final LocalDateTime[] DEFAULT_DATETIME = {TimeParser.parseDateTime("23:59 12/12/2020"),
            TimeParser.parseDateTime("23:59 10/10/2020")};

    private Module module;
    private TaskType taskType;
    private String taskName;
    private String taskDescription;
    private double weight;
    private TaskStatus taskStatus;
    private LocalDateTime[] dateTimes;
    private String estimatedTimeCost;

    public TaskBuilder() {
        module = new Module(DEFAULT_MODULE, DEFAULT_MODULE);
        taskType = DEFAULT_TASKTYPE;
        taskName = DEFAULT_NAME;
        taskDescription = DEFAULT_TASKDESCRIPTION;
        weight = DEFAULT_TASKWEIGHT;
        taskStatus = DEFAULT_TASKSTATUS;
        dateTimes = DEFAULT_DATETIME;
        estimatedTimeCost = DEFAULT_ESTIMATEDTIME;

    }

    /**
     * init new Task with parameter TaskToCopy
     */
    public TaskBuilder(Task taskToCopy) {
        module = taskToCopy.getModule();
        taskType = taskToCopy.getTaskType();
        taskName = taskToCopy.getTaskName();
        taskDescription = taskToCopy.getTaskDescription();
        weight = taskToCopy.getWeight();
        taskStatus = taskToCopy.getTaskStatus();
        dateTimes = taskToCopy.getDateTimes();
        estimatedTimeCost = taskToCopy.getEstimatedTimeCost();
    }

    /**
     * init taskBuilder with name as parameter.
     */
    public TaskBuilder withName(String name) {
        this.taskName = name;
        return this;
    }

    /**
     * builds task.
     */
    public Task build() {
        return new Task(module, taskType, taskName, taskDescription, weight, taskStatus,
                dateTimes, estimatedTimeCost);
    }

}
