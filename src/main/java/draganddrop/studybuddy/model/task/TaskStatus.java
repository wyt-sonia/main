package draganddrop.studybuddy.model.task;

import java.util.ArrayList;

/**
 * Represents the Task status.
 */
public enum TaskStatus {
    PENDING,
    FINISHED,
    DUE_SOON,
    OVERDUE;

    public static ArrayList<TaskStatus> getTaskStatusList() {
        ArrayList<TaskStatus> taskStatuses = new ArrayList<>();
        taskStatuses.add(PENDING);
        taskStatuses.add(FINISHED);
        taskStatuses.add(DUE_SOON);
        taskStatuses.add(OVERDUE);
        return taskStatuses;
    }

    public static TaskStatus getStatus(String status) {
        TaskStatus result = null;
        switch (status.toLowerCase()) {
        case "Pending":
            result = PENDING;
            break;
        case "Finished":
            result = FINISHED;
            break;
        case "Due Soon":
            result = DUE_SOON;
            break;
        case "Overdue":
            result = OVERDUE;
            break;
        default:
        }
        return result;
    }

    /**
     * Converts a TaskStatus enum back into a string, primarily for printing purposes.
     *
     * @return string, either "Pending" or "Finished"
     */
    public String convertToString() {
        String result = "";
        switch (this) {
        case PENDING:
            result = "Pending";
            break;
        case FINISHED:
            result = "Finished";
            break;
        case DUE_SOON:
            result = "Due Soon";
            break;
        case OVERDUE:
            result = "Overdue";
            break;
        default:
        }
        return result;
    }
}
