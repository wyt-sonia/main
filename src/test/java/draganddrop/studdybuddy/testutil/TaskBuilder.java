package draganddrop.studdybuddy.testutil;

import java.time.LocalDateTime;

import draganddrop.studdybuddy.logic.parser.TimeParser;
import draganddrop.studdybuddy.model.module.Module;
import draganddrop.studdybuddy.model.task.Task;
import draganddrop.studdybuddy.model.task.TaskStatus;
import draganddrop.studdybuddy.model.task.TaskType;

/**
 * Utility class to help build tasks.
 */
public class TaskBuilder {

    public static final String DEFAULT_MODULE = "CS2103T";
    public static final String DEFAULT_NAME = "Team project";
    public static final String DEFAULT_TASKDESCRIPTION = "Team project";
    public static final double DEFAULT_ESTIMATEDTIME = 45.5;

    public static final TaskType DEFAULT_TASKTYPE = TaskType.Others;
    public static final TaskStatus DEFAULT_TASKSTATUS = TaskStatus.PENDING;
    public static final double DEFAULT_TASKWEIGHT = 45.0;
    public static final LocalDateTime[] DEFAULT_DATETIME = {TimeParser.parseDateTime("23:59 12/12/2020"),
        TimeParser.parseDateTime("23:59 10/10/2020")};
    public static final LocalDateTime DEFAULT_CREATION_DATETIME = LocalDateTime.now();

    private Module module;
    private TaskType taskType;
    private String taskName;
    private String taskDescription;
    private double weight;
    private TaskStatus taskStatus;
    private LocalDateTime[] dateTimes;
    private double estimatedTimeCost;
    private LocalDateTime creationDateTime;

    public TaskBuilder() {
        module = new Module(DEFAULT_MODULE, DEFAULT_MODULE);
        taskType = DEFAULT_TASKTYPE;
        taskName = DEFAULT_NAME;
        taskDescription = DEFAULT_TASKDESCRIPTION;
        weight = DEFAULT_TASKWEIGHT;
        taskStatus = DEFAULT_TASKSTATUS;
        dateTimes = DEFAULT_DATETIME;
        estimatedTimeCost = DEFAULT_ESTIMATEDTIME;
        creationDateTime = DEFAULT_CREATION_DATETIME;
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
        creationDateTime = taskToCopy.getCreationDateTime();
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
            dateTimes, estimatedTimeCost, creationDateTime);
    }

}
