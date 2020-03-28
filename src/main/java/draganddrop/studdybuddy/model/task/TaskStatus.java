package draganddrop.studdybuddy.model.task;

import java.util.ArrayList;

/**
 * pending.
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
        case "pending":
            result = PENDING;
            break;
        case "finished":
            result = FINISHED;
            break;
        case "due soon":
            result = DUE_SOON;
            break;
        case "overdue":
            result = OVERDUE;
            break;
        default:
        }
        return result;
    }

    /**
     * Converts a TaskStatus enum back into a string, primarily for printing purposes.
     *
     * @return string, either "pending" or "finished"
     */
    public String convertToString() {
        String result = "";
        switch (this) {
        case PENDING:
            result = "pending";
            break;
        case FINISHED:
            result = "finished";
            break;
        case DUE_SOON:
            result = "due soon";
            break;
        case OVERDUE:
            result = "overdue";
            break;
        default:
        }
        return result;
    }
}
