package draganddrop.studybuddy.model.task.exceptions;

/**
 * pending.
 */
public class DuplicateTaskException extends RuntimeException {
    public DuplicateTaskException() {
        super("Operation would result in duplicate tasks");
    }
}
