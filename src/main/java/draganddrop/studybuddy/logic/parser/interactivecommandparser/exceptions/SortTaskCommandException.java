package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represent a SortTaskCommandException that will be throw when there is
 * index format and range error.
 */
public class SortTaskCommandException extends InteractiveCommandException {
    /**
     * Creates a SortTaskCommandException.
     */
    public SortTaskCommandException(String errorType) {
        super(errorType);
    }
}
