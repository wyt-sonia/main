package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents a DeleteTaskCommandException.
 */
public class DeleteTaskCommandException extends InteractiveCommandException {

    /**
     * Creates a DeleteTaskCommandException.
     *
     * @param errorType
     */
    public DeleteTaskCommandException(String errorType) {
        super(errorType);
    }
}
