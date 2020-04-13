package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents a DeleteTaskCommandException.
 *
 * @@author Souwmyaa Sabarinathann
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
