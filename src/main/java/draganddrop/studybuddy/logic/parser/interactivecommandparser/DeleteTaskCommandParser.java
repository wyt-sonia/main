package draganddrop.studybuddy.logic.parser.interactivecommandparser;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;
import draganddrop.studybuddy.model.task.Task;

/**
 * Parses the index for a delete command.
 */
public class DeleteTaskCommandParser {

    /**
     * Parse the {@code userInput} for index value.
     * @param userInput
     * @return integer index
     * @throws InteractiveCommandException
     */
    public static int parseIndex(String userInput) throws DeleteTaskCommandException {
        int result;
        if (userInput.isBlank()) {
            throw new DeleteTaskCommandException("emptyInputError");
        }
        try {
            int index = Integer.parseInt(userInput);
            if (index > Task.getCurrentTasks().size() || index <= 0) {
                throw new DeleteTaskCommandException("invalidIndexRangeError");
            } else {
                result = index;
            }
        } catch (NumberFormatException ex) {
            throw new DeleteTaskCommandException("wrongIndexFormatError");
        }
        return result;
    }
}
