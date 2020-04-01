package draganddrop.studybuddy.logic.parser.interactivecommandparser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.ModuleCode;
import draganddrop.studybuddy.model.task.TaskType;
import javafx.collections.ObservableList;

/**
 * pending.
 */
public class AddTaskCommandParser {

    /**
     * Parse the {@code userInput} for task name.
     *
     * @param userInput
     * @return
     * @throws AddTaskCommandException
     */
    public static String parseName(String userInput) throws AddTaskCommandException {
        String result = "";
        if (userInput.isBlank()) {
            throw new AddTaskCommandException("emptyInputError");
        }
        if (userInput.length() > 20) {
            throw new AddTaskCommandException("taskNameLengthError");
        } else {
            result = userInput.trim();
        }
        return result;
    }

    /**
     * Parse the {@code userInput} for task description.
     *
     * @param userInput
     * @return
     * @throws AddTaskCommandException
     */
    public static String parseDescription(String userInput) throws AddTaskCommandException {
        String result = "";
        if (userInput.isBlank()) {
            throw new AddTaskCommandException("emptyInputError");
        } else if (userInput.length() > 300) {
            throw new AddTaskCommandException("taskDescriptionLengthError");
        } else {
            result = userInput.trim();
        }
        return result;
    }

    /**
     * Parse the {@code userInput} for task date and time.
     *
     * @param userInput
     * @param taskType
     * @return
     * @throws AddTaskCommandException
     */
    public static LocalDateTime[] parseDateTime(String userInput, TaskType taskType) throws AddTaskCommandException {
        LocalDateTime[] result = null;
        switch (taskType) {
        case Assignment:
            try {
                result = new LocalDateTime[1];
                result[0] = TimeParser.parseDateTime(userInput);
            } catch (InteractiveCommandException e) {
                throw new AddTaskCommandException("dataTimeFormatError");
            }
            // filter out the input with correct format but invalid date time
            if (result[0].isBefore(LocalDateTime.now())) {
                throw new AddTaskCommandException("pastDateTime");
            }
            break;

        case Quiz:
        case Exam:
        case Meeting:
        case Presentation:
        case Others:
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
            break;
        default:
        }
        return result;
    }

    /**
     * Parse the {@code userInput} for the module of the task.
     *
     * @param userInput
     * @return
     * @throws AddTaskCommandException
     */
    public static Module parseModule(String userInput, ObservableList<Module> modules) throws AddTaskCommandException {
        Module result = null;
        try {
            if (ModuleCode.isModuleCode(userInput)) {
                List<Module> tempModules = modules.stream()
                    .filter(m -> m.getModuleCode().toString().equals(userInput)).collect(Collectors.toList());
                if (!tempModules.isEmpty()) {
                    result = tempModules.get(0);
                } else {
                    throw new AddTaskCommandException("noSuchModuleError");
                }
            } else {
                int index = Integer.parseInt(userInput) - 1;
                if (index < 0 || index >= modules.size()) {
                    throw new AddTaskCommandException("invalidIndexRangeError");
                }
                result = modules.get(index);
            }
        } catch (NumberFormatException e) {
            throw new AddTaskCommandException("wrongIndexFormatError");
        }
        return result;
    }

    /**
     * Parse the {@code userInput} for the type of the task.
     *
     * @param userInput
     * @param size
     * @return
     * @throws AddTaskCommandException
     * @throws NumberFormatException
     */
    public static TaskType parseType(String userInput, int size) throws AddTaskCommandException, NumberFormatException {
        TaskType result;
        if (userInput.isBlank()) {
            throw new AddTaskCommandException("emptyInputError");
        }
        int index = Integer.parseInt(userInput.trim());
        if (index <= 0 || index > size) {
            throw new AddTaskCommandException("invalidIndexRangeError");
        } else {
            result = TaskType.getTaskTypes()[index - 1];
        }
        return result;
    }

    /**
     * Parse the {@code userInput} for weight of the task.
     *
     * @param userInput
     * @return
     * @throws AddTaskCommandException
     */
    public static double parseWeight(String userInput) throws AddTaskCommandException {
        double result = 0.0;
        try {
            result = Double.parseDouble(userInput);
            if (result < 0.0 || result > 100.0) {
                throw new AddTaskCommandException("wrongWeightRangeError");
            }
        } catch (NumberFormatException e) {
            throw new AddTaskCommandException("wrongWeightFormatError");
        }
        return result;
    }

    /**
     * Parse the {@code userInput} for the estimated time needed to complete the task.
     *
     * @param userInput
     * @return
     * @throws AddTaskCommandException
     */
    public static double parseTimeCost(String userInput) throws AddTaskCommandException {
        double result = 0.0;
        try {
            result = Double.parseDouble(userInput);
            if (result < 0.0) {
                throw new AddTaskCommandException("wrongEstimatedTimeRangeError");
            }
        } catch (NumberFormatException e) {
            throw new AddTaskCommandException("wrongEstimatedTimeFormatError");
        }
        return result;
    }
}
