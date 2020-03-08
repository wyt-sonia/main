package seedu.address.storage;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.TimeParser;
import seedu.address.model.Module;
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
    private final String taskType;
    private final double weight;
    private final String module;
    private final String taskDescription;
    private final String estimatedTimeCost;
    private final String status;

    /**
     * Constructs a {@code JsonAdaptedTask} with the given task details.
     */
    @JsonCreator
    public JsonAdaptedTask(@JsonProperty("taskName") String taskName, @JsonProperty("taskType") String taskType,
                           @JsonProperty("taskDateTime") String taskDateTime,
                           @JsonProperty("taskDescription") String taskDescription,
                           @JsonProperty("module") String module, @JsonProperty("weight") Double weight,
                           @JsonProperty("estimatedTimeCost") String estimatedTimeCost,
                           @JsonProperty("status") String status) {

        this.taskName = taskName;
        this.taskDateTime = taskDateTime;
        this.taskDescription = taskDescription;
        this.module = module;
        this.weight = weight;
        this.estimatedTimeCost = estimatedTimeCost;
        this.status = status;
        this.taskType = taskType;
    }

    /**
     * Converts a given {@code Task} into this class for Jackson use.
     */
    public JsonAdaptedTask(Task source) {

        String descString = source.getTaskDescription();
        String estimatedTimeCostString = source.getEstimatedTimeCost();

        taskDescription = descString.isBlank() ? "" : descString;
        taskName = source.getTaskName();
        taskDateTime = source.getTimeString();
        estimatedTimeCost = estimatedTimeCostString.isBlank() ? "" : estimatedTimeCostString;
        status = source.getTaskStatus().toString();
        taskType = source.getTaskType().toString();
        weight = source.getWeight();
        module = source.getModule().getModuleCode();
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

        Task data = new Task(new Module("temp" + module, module), TaskType.getType(taskType),
            taskName, taskDescription, weight,
            TaskStatus.getStatus(status), dateTimes, estimatedTimeCost);
        return data;
    }

}
