package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Repreents a DeleteDuplicateTaskCommandException.
 */
public class DeleteDuplicateTaskCommandException extends InteractiveCommandException {

    /**
     * Creates a DeleteDuplicateTaskCommandException.
     *
     * @param errorType
     */
    public DeleteDuplicateTaskCommandException(String errorType) {
        super(errorType);
    }
}
