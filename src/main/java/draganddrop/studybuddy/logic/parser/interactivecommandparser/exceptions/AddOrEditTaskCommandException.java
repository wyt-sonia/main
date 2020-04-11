package draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions;

/**
 * Represents an AddOrEditTaskCommandException which handles exceptions for
 * add and edit task commands.
 */
public class AddOrEditTaskCommandException extends InteractiveCommandException {

    /**
     * Creates an AddOrEditTaskCommandException.
     *
     * @param errorType
     */
    public AddOrEditTaskCommandException(String errorType) {
        super(errorType);
    }
}
