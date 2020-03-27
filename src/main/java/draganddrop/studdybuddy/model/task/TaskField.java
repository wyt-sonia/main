package draganddrop.studdybuddy.model.task;

/**
 * The fields of a task
 */
public enum TaskField {
    TASK_NAME("Task name"),
    TASK_TYPE("Task type"),
    TASK_DATETIME("Task due date and time");

    private final String label;

    TaskField(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TaskField getTaskFieldFromNumber(int number) {
        switch (number) {
        case 1:
            return TASK_NAME;
        case 2:
            return TASK_TYPE;
        case 3:
            return TASK_DATETIME;
        default:
            return null;
        }
    }

    public static String getFieldString() {
        return "1. Task name\n"
            + "2. Task type\n"
            + "3. Task due date and time\n";
    }
}
