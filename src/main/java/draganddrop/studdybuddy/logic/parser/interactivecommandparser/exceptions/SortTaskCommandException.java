package draganddrop.studdybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represent a SortTaskCommandException that will be throw when there is
 * index format and range error.
 */
public class SortTaskCommandException extends InteractiveCommandException {
    /**
     * pending.
     */
    public SortTaskCommandException(String errorType) {
        super(errorType);
    }
}
