package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents a ClearTasksCommandException.
 */
public class ClearTasksCommandException extends InteractiveCommandException {

    /**
     * Creates an ClearTasksCommandException with {@code errorType}.
     *
     * @param errorType
     */
    public ClearTasksCommandException(String errorType) {
        super(errorType);
    }
}
