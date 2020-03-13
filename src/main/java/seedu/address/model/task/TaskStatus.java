package seedu.address.model.task;

/**
 * pending.
 */
public enum TaskStatus {
    PENDING,
    FINISHED;

    public static TaskStatus getStatus(String status) {
        TaskStatus result = null;
        switch (status.toLowerCase()) {
        case "pending":
            result = PENDING;
            break;
        case "finished":
            result = FINISHED;
            break;
        default:
        }
        return result;
    }

    public String convertToString() {
        if(this.equals(PENDING)){
            return "Pending";
        } else {
            return "Finished";
        }
    }
}
