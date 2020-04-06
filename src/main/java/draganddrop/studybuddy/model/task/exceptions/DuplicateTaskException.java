package draganddrop.studybuddy.model.task.exceptions;

/**
 * Represents a DuplicateTaskException.
 */
public class DuplicateTaskException extends RuntimeException {
    public DuplicateTaskException() {
        super("Operation would result in duplicate tasks");
    }
}
