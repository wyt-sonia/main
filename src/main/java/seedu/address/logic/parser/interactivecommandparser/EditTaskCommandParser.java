package seedu.address.logic.parser.interactivecommandparser;

import java.time.LocalDateTime;

import seedu.address.logic.parser.TimeParser;
import seedu.address.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import seedu.address.logic.parser.interactivecommandparser.exceptions.EditTaskCommandException;
import seedu.address.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import seedu.address.model.module.Module;
import seedu.address.model.module.exceptions.ModuleCodeException;
import seedu.address.model.task.TaskType;

/**
 * Parser for the edit command
 */
public class EditTaskCommandParser {
    /**
     * pending.
     */
    public static String parseName(String userInput) throws AddTaskCommandException {
        String result = "";
        if (userInput.isBlank()) {
            throw new EditTaskCommandException("emptyInputError");
        } else {
            result = userInput.trim();
        }
        return result;
    }

    /**
     * pending.
     */
    public static LocalDateTime[] parseDateTime(String userInput) throws AddTaskCommandException {
        LocalDateTime[] result = null;
        result = new LocalDateTime[2];
        String[] tempInputDateTimes;

        if (!userInput.contains("-")) {
            throw new EditTaskCommandException("dataTimeFormatError");
        }

        // filter out the invalid input with wrong "start-end" format
        tempInputDateTimes = userInput.trim().split("-");
        if (tempInputDateTimes.length != 2 || tempInputDateTimes[0].isBlank()) {
            throw new EditTaskCommandException("dataTimeFormatError");
        }

        try {
            result[0] = TimeParser.parseDateTime(tempInputDateTimes[0]);
            result[1] = TimeParser.parseDateTime(tempInputDateTimes[1]);
        } catch (InteractiveCommandException e) {
            throw new EditTaskCommandException("dataTimeFormatError");
        }

        if (result[0].isBefore(LocalDateTime.now()) || result[1].isBefore(LocalDateTime.now())) {
            throw new EditTaskCommandException("pastDateTime");
        }
        if (result[1].isBefore(result[0])) {
            throw new EditTaskCommandException("eventEndBeforeStartError");
        }
        return result;
    }

    /**
     * pending.
     */
    public static TaskType parseType(String userInput, int size)
        throws EditTaskCommandException, NumberFormatException {
        TaskType result;
        int index = Integer.parseInt(userInput.trim());
        if (index <= 0 || index > size) {
            throw new EditTaskCommandException("invalidIndexRangeError");
        } else {
            result = TaskType.getTaskTypes()[index - 1];
        }
        return result;
    }

    /**
     * throws a (converted) error when the userinput is an invalid moduleCode
     * @param userInput
     * @return module
     * @throws AddTaskCommandException
     */
    public static Module parseModule(String userInput) throws AddTaskCommandException {
        try {
            return new Module(userInput);
        } catch (ModuleCodeException ex) {
            throw new AddTaskCommandException(ex.getLocalizedMessage());
        }
    }
}
