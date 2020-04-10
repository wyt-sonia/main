package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents a CompleteTaskCommandException.
 */
public class CompleteTaskCommandException extends InteractiveCommandException {

    /**
     * Creates a CompleteTaskCommandException.
     *
     * @param errorType
     */
    public CompleteTaskCommandException(String errorType) {
        super(errorType);
    }
}
