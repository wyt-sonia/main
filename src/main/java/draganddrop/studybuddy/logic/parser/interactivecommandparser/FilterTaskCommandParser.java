package draganddrop.studybuddy.logic.parser.interactivecommandparser;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.FilterTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;

/**
 * Parses the index for a filter command.
 */
public class FilterTaskCommandParser {

    /**
     * Parse the {@code userInput} for index value.
     * @param userInput
     * @return integer index
     * @throws InteractiveCommandException
     */
    public static TaskStatus parseStatusIndex(String userInput) throws FilterTaskCommandException {
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

    /**
     * Parse the {@code userInput} for index value.
     * @param userInput
     * @return integer index
     * @throws InteractiveCommandException
     */
    public static int parseOptionIndex(String userInput) throws FilterTaskCommandException {
        int result;
        if (userInput.isBlank()) {
            throw new FilterTaskCommandException("emptyInputError");
        }
        try {
            int index = Integer.parseInt(userInput);
            if (index > 2 || index <= 0) {
                throw new FilterTaskCommandException("invalidIndexRangeError");
            } else {
                result = index;
            }
        } catch (NumberFormatException ex) {
            throw new FilterTaskCommandException("wrongIndexFormatError");
        }
        return result;
    }

    /**
     * Parse the {@code userInput} for index value.
     * @param userInput
     * @return integer index
     * @throws InteractiveCommandException
     */
    public static TaskType parseTypeIndex(String userInput) throws FilterTaskCommandException {
        TaskType result;
        if (userInput.isBlank()) {
            throw new FilterTaskCommandException("emptyInputError");
        }
        try {
            int index = Integer.parseInt(userInput);
            if (index > TaskType.getTaskTypes().length || index <= 0) {
                throw new FilterTaskCommandException("invalidIndexRangeError");
            } else {
                result = TaskType.getTaskTypes()[index - 1];
            }
        } catch (NumberFormatException ex) {
            throw new FilterTaskCommandException("wrongIndexFormatError");
        }
        return result;
    }
}
