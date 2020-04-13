package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents a AddDuplicateTaskCommandException.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class AddDuplicateTaskCommandException extends InteractiveCommandException {

    /**
     * Creates an AddDuplicateTaskCommandException with {@code errorType}.
     *
     * @param errorType type of error
     */
    public AddDuplicateTaskCommandException(String errorType) {
        super(errorType);
    }
}
