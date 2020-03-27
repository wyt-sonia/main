package draganddrop.studdybuddy.model.task;

/**
 * pending.
 */
public enum TaskType {
    Assignment,
    Quiz,
    Presentation,
    Meeting,
    Exam,
    Others;

    private static TaskType[] taskTypes = {
        Assignment,
        Quiz,
        Presentation,
        Meeting,
        Exam,
        Others
    };

    public static TaskType[] getTaskTypes() {
        return taskTypes;
    }

    public static void setTaskTypes(TaskType[] taskTypes) {
        TaskType.taskTypes = taskTypes;
    }

    public static String getTypeString() {
        return "1. Assignment\n"
            + "2. Quiz\n"
            + "3. Presentation\n"
            + "4. Meeting\n"
            + "5. Exam\n"
            + "6. Others";
    }

    /**
     * pending.
     */
    public static TaskType getType(String status) {
        TaskType result = null;
        switch (status.toLowerCase()) {
        case "assignment":
            result = Assignment;
            break;
        case "quiz":
            result = Quiz;
            break;
        case "presentation":
            result = Presentation;
            break;
        case "meeting":
            result = Meeting;
            break;
        case "exam":
            result = Exam;
            break;
        case "others":
            result = Others;
            break;
        default:
        }
        return result;
    }
}

