package draganddrop.studybuddy.model.task;

/**
 * The fields of a task
 */
public enum TaskField {
    TASK_MODULE("Task module"),
    TASK_NAME("Task name"),
    TASK_TYPE("Task type"),
    TASK_DATETIME("Task due date and time/ duration"),
    TASK_DESCRIPTION("Task description"),
    TASK_WEIGHT("Task weight"),
    TASK_ESTIMATED_TIME_COST("Estimated number of hour cost");

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
            return TASK_MODULE;
        case 2:
            return TASK_NAME;
        case 3:
            return TASK_TYPE;
        case 4:
            return TASK_DATETIME;
        case 5:
            return TASK_DESCRIPTION;
        case 6:
            return TASK_WEIGHT;
        case 7:
            return TASK_ESTIMATED_TIME_COST;
        default:
            return null;
        }
    }

    public static String getFieldString() {
        return "1. " + TASK_MODULE.label + "\n"
            + "2. " + TASK_NAME.label + "\n"
            + "3. " + TASK_TYPE.label + "\n"
            + "4. " + TASK_DATETIME.label + "\n"
            + "5. " + TASK_DESCRIPTION.label + "\n"
            + "6. " + TASK_WEIGHT.label + "\n"
            + "7. " + TASK_ESTIMATED_TIME_COST.label + "\n";
    }
}
