package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents an ExitTaskCommandException.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class ExitTaskCommandException extends InteractiveCommandException {

    /**
     * Creates an ExitTaskCommandException.
     *
     * @param errorType
     */
    public ExitTaskCommandException(String errorType) {
        super(errorType);
    }
}
