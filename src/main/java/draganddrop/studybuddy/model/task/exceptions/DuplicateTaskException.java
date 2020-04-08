package draganddrop.studybuddy.model.task.exceptions;

/**
 * Represents a DuplicateTaskException.
 */
public class DuplicateTaskException extends TaskException {
    public DuplicateTaskException(String errorType) {
        super(errorType);
    }
}
