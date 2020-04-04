package draganddrop.studybuddy.logic.parser.interactivecommandparser;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.FilterTaskCommandException;
import draganddrop.studybuddy.model.task.TaskStatus;

/**
 * Parses the index for a filter command.
 */
public class FilterTaskCommandParser {

    /**
     * Parse the {@code userInput} for index value.
     * @param userInput
     * @return integer index
     * @throws AddTaskCommandException
     */
    public static TaskStatus parseIndex(String userInput) throws FilterTaskCommandException {
        TaskStatus result;
        if (userInput.isBlank()) {
            throw new FilterTaskCommandException("emptyInputError");
        }
        try {
            int index = Integer.parseInt(userInput);
            if (index > TaskStatus.getTaskStatusList().size() || index <= 0) {
                throw new FilterTaskCommandException("invalidIndexRangeError");
            } else {
                result = TaskStatus.getTaskStatusList().get(index - 1);
            }
        } catch (NumberFormatException ex) {
            throw new FilterTaskCommandException("wrongIndexFormatError");
        }
        return result;
    }
}
