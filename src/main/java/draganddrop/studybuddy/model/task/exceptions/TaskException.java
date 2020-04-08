package draganddrop.studybuddy.model.task.exceptions;

/**
 * Represents an InteractiveCommandException.
 */
public class TaskException extends RuntimeException {
    protected String errorType;
    protected String errorMessage;

    public TaskException(String errorType) {
        this.errorType = errorType;

        switch (errorType) {
            case "duplicateTask":
                errorMessage = "Duplicate task! Please try again.";
                break;

            default:
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public String getErrorType() {
        return errorType;
    }
}

