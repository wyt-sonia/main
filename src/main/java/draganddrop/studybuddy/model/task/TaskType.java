package draganddrop.studybuddy.model.task;

/**
 * Represents the task types.
 *
 * @@author wyt-sonia
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

    public static String getTypeString() {
        return "1. Assignment\n"
            + "2. Quiz\n"
            + "3. Presentation\n"
            + "4. Meeting\n"
            + "5. Exam\n"
            + "6. Others";
    }

    /**
     * Gets related task type according to the {@code status} string.
     *
     * @param status
     * @return
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
        assert result != null
            : "The input string does not match with any task type, please check.\n";
        return result;
    }
}

