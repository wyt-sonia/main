package draganddrop.studybuddy.logic.parser.interactivecommandparser;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;
import draganddrop.studybuddy.model.task.Task;

/**
 * pending.
 */
public class DeleteTaskCommandParser {

    /**
     * Parse the {@code userInput} for task name.
     *
     * @param userInput
     * @return
     * @throws AddTaskCommandException
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

