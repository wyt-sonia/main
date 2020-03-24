package seedu.address.logic.parser.interactivecommandparser;

import java.time.LocalDateTime;

import seedu.address.logic.parser.TimeParser;
import seedu.address.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import seedu.address.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
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
            throw new AddTaskCommandException("emptyInputError");
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
            throw new AddTaskCommandException("dataTimeFormatError");
        }

        // filter out the invalid input with wrong "start-end" format
        tempInputDateTimes = userInput.trim().split("-");
        if (tempInputDateTimes.length != 2 || tempInputDateTimes[0].isBlank()) {
            throw new AddTaskCommandException("dataTimeFormatError");
        }

        try {
            result[0] = TimeParser.parseDateTime(tempInputDateTimes[0]);
            result[1] = TimeParser.parseDateTime(tempInputDateTimes[1]);
        } catch (InteractiveCommandException e) {
            throw new AddTaskCommandException("dataTimeFormatError");
        }

        if (result[0].isBefore(LocalDateTime.now()) || result[1].isBefore(LocalDateTime.now())) {
            throw new AddTaskCommandException("pastDateTime");
        }
        if (result[1].isBefore(result[0])) {
            throw new AddTaskCommandException("eventEndBeforeStartError");
        }
        return result;
    }

    /**
     * pending.
     */
    public static TaskType parseType(String userInput, int size) throws AddTaskCommandException, NumberFormatException {
        TaskType result;
        int index = Integer.parseInt(userInput.trim());
        if (index <= 0 || index > size) {
            throw new AddTaskCommandException("invalidIndexRange");
        } else {
            result = TaskType.getTaskTypes()[index - 1];
        }
        return result;
    }
}
