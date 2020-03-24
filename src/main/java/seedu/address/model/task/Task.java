package seedu.address.model.task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import seedu.address.logic.parser.TimeParser;
import seedu.address.model.module.Module;

/**
 * pending.
 */
public class Task implements Comparable<Task> {
    /**
     * The acceptable data and time format.
     */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private static final int DIVISOR = (1000 * 60);
    private static ArrayList<Task> currentTasks = new ArrayList<>();

    private Module module;
    private TaskType taskType;
    private String taskName;
    private String taskDescription;
    private double weight;
    private TaskStatus taskStatus;
    private LocalDateTime[] dateTimes;
    private LocalDateTime creationDateTime;
    private LocalDateTime finishDateTime;
    private double estimatedTimeCost;
    private DateFormat df = null;
    private Date dateObj = null;
    private boolean duplicate = false;

    public Task(Module module, TaskType taskType, String taskName, String taskDescription, double weight,
                TaskStatus taskStatus, LocalDateTime[] dateTimes, double estimatedTimeCost,
                LocalDateTime creationDateTime) {
        this.module = module; //not covered yet
        this.taskType = taskType;
        this.taskName = taskName;
        this.taskDescription = taskDescription; //not covered yet
        this.weight = weight; //not covered yet
        this.taskStatus = taskStatus;
        this.dateTimes = dateTimes;
        this.finishDateTime = null;
        this.estimatedTimeCost = estimatedTimeCost; //not covered yet
        this.creationDateTime = creationDateTime;
    }

    public Task() {
        dateTimes = new LocalDateTime[2];
    }

    public static void updateCurrentTaskList(List<Task> tasks) {
        currentTasks = (ArrayList<Task>) tasks;
    }

    public static ArrayList<Task> getCurrentTasks() {
        return currentTasks;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setStatus(String status) {
        switch (status) {
        case "pending":
            this.taskStatus = TaskStatus.PENDING;
            break;
        case "finished":
            this.taskStatus = TaskStatus.FINISHED;
            break;
        default:
        }
    }

    /**
     * Updates the tasks status accordingly.
     */
    public void freshStatus() {
        if (this.taskStatus != null && !this.taskStatus.equals(TaskStatus.FINISHED)) {
            LocalDateTime now = LocalDateTime.now();
            if (this.isDueSoon() && !this.taskStatus.equals(TaskStatus.DUE_SOON)) {
                this.taskStatus = TaskStatus.DUE_SOON;
                return;
            }
            if (!this.isDueSoon() && this.taskStatus.equals(TaskStatus.DUE_SOON)) {
                if (this.dateTimes[0].isBefore(now) && !this.taskStatus.equals(TaskStatus.OVERDUE)) {
                    this.taskStatus = TaskStatus.OVERDUE;
                    return;
                }
                if (this.dateTimes[0].isAfter(now) && !this.taskStatus.equals(TaskStatus.PENDING)) {
                    this.taskStatus = TaskStatus.PENDING;
                    return;
                }
            }

        }
    }

    /**
     * Checks whether the status of the task is expired.
     *
     * Returns true if the status of the task is expired.
     */
    public boolean isStatusExpired() {
        boolean result = false;
        if (this.taskStatus != null && !this.taskStatus.equals(TaskStatus.FINISHED)) {
            LocalDateTime now = LocalDateTime.now();
            if (this.dateTimes[0].isBefore(now) && !this.taskStatus.equals(TaskStatus.OVERDUE)) {
                result = true;
            }
            if (this.dateTimes[0].isAfter(now) && this.taskStatus.equals(TaskStatus.OVERDUE)) {
                result = true;
            }
            if (this.isDueSoon() && !this.taskStatus.equals(TaskStatus.DUE_SOON)) {
                result = true;
            }
            if (!this.isDueSoon() && this.taskStatus.equals(TaskStatus.DUE_SOON)) {
                result = true;
            }
        }
        return result;
    }

    public LocalDateTime getFinishDateTime() {
        return finishDateTime;
    }

    public void setFinishDateTime(LocalDateTime finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public TaskStatus getTaskStatus() {
        if (taskStatus == null) {
            taskStatus = TaskStatus.PENDING;
            if (isStatusExpired()) {
                freshStatus();
            }
        }
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTimeString() {
        String timeString = "";
        if (dateTimes.length == 1) {
            timeString = TimeParser.getDateTimeString(dateTimes[0]);
        } else if (dateTimes.length == 2) {
            timeString = TimeParser.getDateTimeString(dateTimes[0]) + "-" + TimeParser.getDateTimeString(dateTimes[0]);
        }
        return timeString;
    }

    public LocalDateTime[] getDateTimes() {
        return dateTimes;
    }

    //need to add the outOfRangeExceptionHandler
    public void setDateTimes(LocalDateTime[] dateTimes) {
        this.dateTimes = dateTimes;
    }

    public double getEstimatedTimeCost() {
        return estimatedTimeCost;
    }

    public void setEstimatedTimeCost(double estimatedTimeCost) {
        this.estimatedTimeCost = estimatedTimeCost;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    /**
     * Checks if the task is due soon (next 7 days).
     *
     * @return true if it is due soon, else false
     */
    public boolean isDueSoon() {
        df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        dateObj = new Date();
        long difference = 0;
        try {
            difference = df.parse(this.getTimeString()).getTime() - dateObj.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float daysBetween = (difference / DIVISOR);
        return daysBetween <= 7 * 24 * 60 && daysBetween >= 0;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(module, taskType, taskName, taskDescription, weight, taskStatus, dateTimes);
    }

    /**
     * pending change
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Task)) {
            return false;
        }

        Task otherTask = (Task) other;
        return otherTask.getTaskName().equals(getTaskName())
            && otherTask.getModule().equals(getModule())
            && otherTask.getTimeString().equals(getTimeString())
            && otherTask.getTaskType().equals(getTaskType())
            && otherTask.getTaskDescription().equals(getTaskDescription())
            && otherTask.getTaskStatus().equals(getTaskStatus());
    }

    @Override
    public String toString() {
        String string = "";
        return string;
    }

    /**
     * Pending change
     * Returns true if both task of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
            && otherTask.getTaskName().equals(getTaskName())
            && otherTask.getModule().equals(getModule())
            && otherTask.getTimeString().equals(getTimeString())
            && otherTask.getTaskType().equals(getTaskType())
            && otherTask.getTaskDescription().equals(getTaskDescription())
            && otherTask.getTaskStatus().equals(getTaskStatus());
    }

    /**
     * Compare tasks by deadline/ start date.
     * Comparison by task name is handled in sort command class.
     */
    @Override
    public int compareTo(Task t) {
        int result = 0;
        if (this.dateTimes[0].isBefore(t.dateTimes[0])) {
            result = -1;
        } else if (!this.dateTimes[0].isBefore(t.dateTimes[0])) {
            result = 1;
        } else {
            if (this.taskType.equals(t.taskType)) {
                result = this.taskName.compareTo(t.taskName);
            } else {
                result = this.taskType.compareTo(t.taskType);
            }
        }
        return result;
    }
}
