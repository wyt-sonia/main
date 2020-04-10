package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents a AddDuplicateTaskCommandException.
 */
public class AddDuplicateTaskCommandException extends InteractiveCommandException {

    /**
     * Creates an AddDuplicateTaskCommandException with {@code errorType}.
     *
     * @param errorType
     */
    public AddDuplicateTaskCommandException(String errorType) {
        super(errorType);
    }
}
