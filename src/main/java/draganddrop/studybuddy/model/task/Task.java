package draganddrop.studybuddy.model.task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.statistics.Statistics;

/**
 * Represents a Task.
 */
public class Task implements Comparable<Task>, Cloneable {
    /**
     * The acceptable data and time format.
     */
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private static final int MINUTES_DIVISOR = (1000 * 60);
    private static final int HOURS_DIVISOR = (1000 * 60 * 60);
    private static final int DAYS_DIVISOR = (1000 * 60 * 60 * 24);
    private static final int MINUTES_IN_WEEK = (7 * 24 * 60);
    private static Statistics statistics;
    private static ArrayList<Task> currentTasks = new ArrayList<>();
    private static ArrayList<Task> archivedTasks = new ArrayList<>();

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
    private int duplicate = 0;

    public Task(Module module, TaskType taskType, String taskName, String taskDescription, double weight,
                TaskStatus taskStatus, LocalDateTime[] dateTimes, double estimatedTimeCost,
                LocalDateTime creationDateTime) {
        this.module = module;
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

    public static void setStatistics(Statistics statistics) {
        Task.statistics = statistics;
    }

    public static void updateCurrentTaskList(List<Task> tasks) {
        currentTasks = (ArrayList<Task>) tasks;
    }

    public static ArrayList<Task> getCurrentTasks() {
        return currentTasks;
    }

    public static void updateArchivedTaskList(List<Task> tasks) {
        archivedTasks = (ArrayList<Task>) tasks;
    }

    public static ArrayList<Task> getArchivedTasks() {
        return archivedTasks;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setStatus(String status) {
        switch (status) {
        case "Pending":
            this.taskStatus = TaskStatus.PENDING;
            break;
        case "Finished":
            this.taskStatus = TaskStatus.FINISHED;
            break;
        default:
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public LocalDateTime getDueDate() {
        return getDateTimes()[0];
    }

    /**
     * Calculates the time left for the deadline.
     * @return time left
     */
    public String getTimeLeft() {
        String timeLeft = "Due: ";
        df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        dateObj = new Date();
        long difference = 0;
        try {
            difference = df.parse(this.getTimeString()).getTime() - dateObj.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int daysBetween = (int) (difference / DAYS_DIVISOR);
        int hoursBetween = ((int) (difference / HOURS_DIVISOR)) % 24;
        int minsBetween = ((int) (difference / MINUTES_DIVISOR)) % 60;

        if (daysBetween != 0) {
            if (daysBetween == 1) {
                timeLeft = timeLeft + daysBetween + " day ";
            } else {
                timeLeft = timeLeft + daysBetween + " days ";
            }
            if (hoursBetween == 0) {
                return timeLeft;
            }
        }
        if (hoursBetween != 0) {
            if (hoursBetween == 1) {
                timeLeft = timeLeft + hoursBetween + " hour ";
            } else {
                timeLeft = timeLeft + hoursBetween + " hours ";
            }
        }
        if (minsBetween != 0) {
            if (minsBetween == 1) {
                timeLeft = timeLeft + minsBetween + " min ";
            } else {
                timeLeft = timeLeft + minsBetween + " mins ";
            }
        }

        if (daysBetween == 0 && hoursBetween == 0 && minsBetween == 0) {
            timeLeft = timeLeft + " now";
        }

        return timeLeft;
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
            if (!this.isDueSoon()) {
                if (this.dateTimes[0].isBefore(now) && !this.taskStatus.equals(TaskStatus.OVERDUE)) {
                    statistics.recordOverdueTask(this);
                    this.taskStatus = TaskStatus.OVERDUE;
                    return;
                }
                if (this.dateTimes[0].isAfter(now) && !this.taskStatus.equals(TaskStatus.PENDING)) {
                    this.taskStatus = TaskStatus.PENDING;
                }
            }
        }
    }

    /**
     * Checks whether the status of the task is expired.
     * <p>
     * Returns true if the status of the task is expired.
     */
    public boolean isStatusExpired() {
        boolean isExpired = false;
        if (this.taskStatus != null && !this.taskStatus.equals(TaskStatus.FINISHED)) {
            LocalDateTime now = LocalDateTime.now();
            if (this.dateTimes[0].isBefore(now) && !this.taskStatus.equals(TaskStatus.OVERDUE)) {
                isExpired = true;
            }
            if (this.dateTimes[0].isAfter(now) && this.taskStatus.equals(TaskStatus.OVERDUE)) {
                isExpired = true;
            }
            if (this.isDueSoon() && !this.taskStatus.equals(TaskStatus.DUE_SOON)) {
                isExpired = true;
            }
            if (!this.isDueSoon() && this.taskStatus.equals(TaskStatus.DUE_SOON)) {
                isExpired = true;
            }
        }
        return isExpired;
    }

    public LocalDateTime getFinishDateTime() {
        return finishDateTime;
    }

    public void setFinishDateTime(LocalDateTime finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public int getDuplicate() {
        return duplicate;
    }

    public void incrementDuplicate() {
        this.duplicate++;
    }

    public void zeroDuplicate() {
        this.duplicate = 0;
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
            timeString = TimeParser.getDateTimeString(dateTimes[0]) + "-" + TimeParser.getDateTimeString(dateTimes[1]);
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
     * Checks if the task is Due Soon (next 7 days).
     *
     * @return true if it is Due Soon, else false
     */
    public boolean isDueSoon() {
        if (getTaskStatus() != TaskStatus.FINISHED) {
            df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            dateObj = new Date();
            long difference = 0;
            try {
                difference = df.parse(this.getTimeString()).getTime() - dateObj.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            float minsBetween = (difference / MINUTES_DIVISOR);
            return minsBetween <= MINUTES_IN_WEEK && minsBetween >= 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(module, taskType, taskName, taskDescription, weight, taskStatus, dateTimes);
    }

    /**
     * Returns true if both tasks have the same identity and data fields.
     * This defines a stronger notion of equality between two tasks.
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
            && otherTask.getWeight() == getWeight()
            && otherTask.getEstimatedTimeCost() == getEstimatedTimeCost();
    }

    @Override
    public String toString() {
        String taskString = "";
        taskString = "Task Name :" + this.taskName + "\n"
            + "Task Code: " + this.module.getModuleCode().toString() + "\n"
            + "Task Type: " + this.getTaskType().toString() + "\n"
            + "Deadline/Duration: " + this.getTimeString() + "\n"
            + "Task Description: " + this.taskDescription + "\n"
            + "Task weight: " + this.weight + "\n"
            + "Task Estimated Time Cost: " + this.estimatedTimeCost + " hrs\n";
        return taskString;
    }

    /**
     * Returns true if both task of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two tasks.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
            && otherTask.getTaskName().equals(getTaskName())
            && otherTask.getModule().equals(getModule())
            && otherTask.getTimeString().equals(getTimeString())
            && otherTask.getWeight() == (getWeight())
            && otherTask.getEstimatedTimeCost() == (getEstimatedTimeCost())
            && otherTask.getTaskType().equals(getTaskType())
            && otherTask.getTaskDescription().equals(getTaskDescription());
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
