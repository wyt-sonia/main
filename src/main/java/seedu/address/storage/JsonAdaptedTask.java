package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.TimeParser;
import seedu.address.model.module.Module;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;
import seedu.address.model.task.TaskType;

/**
 * Jackson-friendly version of {@link Task}.
 */
class JsonAdaptedTask {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Task's %s field is missing!";

    private final String taskName;
    private final String taskDateTime;
    private final String taskFinishDateTime;
    private final String taskType;
    private final double weight;
    private final String moduleCode;
    private final String taskDescription;
    private final double estimatedTimeCost;
    private final String status;
    private final String taskCreationDateTime;

    /**
     * Constructs a {@code JsonAdaptedTask} with the given task details.
     */
    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("taskName") String taskName, @JsonProperty("taskType") String taskType,
                           @JsonProperty("taskDateTime") String taskDateTime,
                           @JsonProperty("finishDateTime") String taskFinishDateTime,
                           @JsonProperty("taskDescription") String taskDescription,
                           @JsonProperty("module") String module, @JsonProperty("weight") Double weight,
                           @JsonProperty("estimatedTimeCost") Double estimatedTimeCost,
                           @JsonProperty("status") String status,
                           @JsonProperty("taskCreationDateTime") String taskCreationDateTime) {

        this.taskName = taskName;
        this.taskDateTime = taskDateTime;
        this.taskFinishDateTime = taskFinishDateTime;
        this.taskDescription = taskDescription;
        this.moduleCode = module;
        this.weight = weight;
        this.estimatedTimeCost = estimatedTimeCost;
        this.status = status;
        this.taskType = taskType;
        this.taskCreationDateTime = taskCreationDateTime;
    }

    /**
     * Converts a given {@code Task} into this class for Jackson use.
     */
    public JsonAdaptedTask(Task source) {

        String descString = source.getTaskDescription();

        taskDescription = descString.isBlank() ? "" : descString;
        taskName = source.getTaskName();
        taskDateTime = source.getTimeString();
        taskFinishDateTime = source.getFinishDateTime() != null
            ? TimeParser.getDateTimeString(source.getFinishDateTime())
            : "";
        estimatedTimeCost = source.getEstimatedTimeCost();
        status = source.getTaskStatus().toString();
        taskType = source.getTaskType().toString();
        weight = source.getWeight();
        moduleCode = source.getModule().toString();
        taskCreationDateTime = TimeParser.getDateTimeString(source.getCreationDateTime());
    }

    /**
     * Converts this Jackson-friendly adapted task object into the model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task.
     */
    public Task toModelType() throws IllegalValueException {

        // for datetime
        LocalDateTime[] dateTimes;
        String[] timeTerms = taskDateTime.split("-");
        int count = timeTerms.length;
        if (timeTerms.length == 2) {
            dateTimes = new LocalDateTime[2];
            dateTimes[0] = TimeParser.parseDateTime(timeTerms[0]);
            dateTimes[1] = TimeParser.parseDateTime(timeTerms[1]);
        } else {
            dateTimes = new LocalDateTime[1];
            dateTimes[0] = TimeParser.parseDateTime(timeTerms[0]);
        }

        LocalDateTime creationDateTime = TimeParser.parseDateTime(taskCreationDateTime);

        Task data = new Task(new Module(moduleCode), TaskType.getType(taskType),
            taskName, taskDescription, weight,
            TaskStatus.getStatus(status), dateTimes, estimatedTimeCost, creationDateTime);
        if (!taskFinishDateTime.isBlank()) {
            LocalDateTime finishDateTime = TimeParser.parseDateTime(taskFinishDateTime);
            data.setFinishDateTime(finishDateTime);
        }
        return data;
    }

}
