package draganddrop.studybuddy.logic.parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.ModuleCode;
import draganddrop.studybuddy.model.task.TaskType;
import javafx.collections.ObservableList;

/**
 * Represents a TaskParser, which can parse all fields for tasks during interactive communication.
 * Especially for interactions of add task and edit task.
 *
 * @@author Wang Yuting
 */
public class TaskParser {

    /**
     * Parse the {@code userInput} for task name.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    public static String parseName(String userInput) throws AddOrEditTaskCommandException {
        String result = "";

        if (userInput.isBlank()) {
            throw new AddOrEditTaskCommandException("emptyInputError");
        }

        if (userInput.length() > 20) {
            throw new AddOrEditTaskCommandException("taskNameLengthError");
        }

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userInput);
        boolean isSpecialCharInside = matcher.find();
        if (isSpecialCharInside) {
            throw new AddOrEditTaskCommandException("specialCharInputError");
        }

        result = userInput.trim();

        assert !result.isBlank()
            : "The result of parseName from TaskParser is blank, please check.\n";
        return result;
    }

    /**
     * Parse the {@code userInput} for task description.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    public static String parseDescription(String userInput) throws AddOrEditTaskCommandException {
        String result = "";

        if (userInput.length() > 300) {
            throw new AddOrEditTaskCommandException("taskDescriptionLengthError");
        }

        result = userInput.trim();
        return result;
    }

    /**
     * Parse the {@code userInput} to assignment's date and time.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    private static LocalDateTime[] parseDateTimeForAssignment(String userInput)
        throws AddOrEditTaskCommandException {
        LocalDateTime[] result = new LocalDateTime[1];

        // Check date time format
        try {
            result[0] = TimeParser.parseDateTime(userInput);
        } catch (InteractiveCommandException e) {
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }

        // Checks passed time
        if (result[0].isBefore(LocalDateTime.now())) {
            throw new AddOrEditTaskCommandException("pastDateTime");
        }

        return result;
    }

    /**
     * Parse the {@code userInput} to other task type's (except assignment) date and time.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    private static LocalDateTime[] parseDateTimeForOtherTaskType(String userInput)
        throws AddOrEditTaskCommandException {
        LocalDateTime[] result = new LocalDateTime[2];
        String[] tempInputDateTimes;

        // Check date time format
        if (!userInput.contains("-")) {
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }
        tempInputDateTimes = userInput.trim().split("-");
        if (tempInputDateTimes.length != 2 || tempInputDateTimes[0].isBlank()) {
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }
        try {
            result[0] = TimeParser.parseDateTime(tempInputDateTimes[0]);
            result[1] = TimeParser.parseDateTime(tempInputDateTimes[1]);
        } catch (InteractiveCommandException e) {
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }

        // Check passed time and start-end order
        if (result[0].isBefore(LocalDateTime.now()) || result[1].isBefore(LocalDateTime.now())) {
            throw new AddOrEditTaskCommandException("pastDateTime");
        }
        if (!result[1].isAfter(result[0])) {
            throw new AddOrEditTaskCommandException("eventEndBeforeStartError");
        }

        return result;
    }

    /**
     * Parse the {@code userInput} to task date and time.
     *
     * @param userInput
     * @param taskType
     * @return
     * @throws AddOrEditTaskCommandException
     */
    public static LocalDateTime[] parseDateTime(String userInput, TaskType taskType)
        throws AddOrEditTaskCommandException {
        LocalDateTime[] result;

        if (taskType.equals(TaskType.Assignment)) {
            result = parseDateTimeForAssignment(userInput);
        } else {
            result = parseDateTimeForOtherTaskType(userInput);
        }

        assert result != null
            : "The result of parseDateTime from TaskParser is null, please check.\n";
        return result;
    }

    /**
     * Parse the {@code userInput} for the module of the task.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    public static Module parseModule(String userInput, ObservableList<Module> modules)
        throws AddOrEditTaskCommandException {
        Module result;

        try {
            if (ModuleCode.isModuleCode(userInput)) {
                List<Module> tempModules = modules.stream()
                    .filter(m -> m.getModuleCode().toString().equals(userInput.toUpperCase()))
                    .collect(Collectors.toList());
                if (tempModules.isEmpty()) {
                    throw new AddOrEditTaskCommandException("noSuchModuleError");
                }
                result = tempModules.get(0);
            } else {
                int index = Integer.parseInt(userInput) - 1;
                if (index < 0 || index >= modules.size()) {
                    throw new AddOrEditTaskCommandException("invalidIndexRangeError");
                }
                result = modules.get(index);
            }
        } catch (NumberFormatException e) {
            throw new AddOrEditTaskCommandException("wrongIndexFormatError");
        }

        assert result != null
            : "The result of parseModule from TaskParser is null, please check.\n";
        return result;
    }

    /**
     * Parse the {@code userInput} for the type of the task.
     *
     * @param userInput
     * @param size
     * @return
     * @throws AddOrEditTaskCommandException
     * @throws NumberFormatException
     */
    public static TaskType parseType(String userInput, int size)
        throws AddOrEditTaskCommandException, NumberFormatException {
        TaskType result;

        if (userInput.isBlank()) {
            throw new AddOrEditTaskCommandException("emptyInputError");
        }

        int index = Integer.parseInt(userInput.trim());
        if (index <= 0 || index > size) {
            throw new AddOrEditTaskCommandException("invalidIndexRangeError");
        }

        result = TaskType.getTaskTypes()[index - 1];
        assert result != null
            : "The result of parseType from TaskParser is null, please check.\n";
        return result;
    }

    /**
     * Parse the {@code userInput} for weight of the task.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    public static double parseWeight(String userInput) throws AddOrEditTaskCommandException {
        double result;

        try {
            result = Double.parseDouble(userInput);
            if (result < 0.0 || result > 100.0) {
                throw new AddOrEditTaskCommandException("wrongWeightRangeError");
            }
        } catch (NumberFormatException e) {
            throw new AddOrEditTaskCommandException("wrongWeightFormatError");
        }

        return result;
    }

    /**
     * Parse the {@code userInput} for the estimated time needed to complete the task.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    public static double parseTimeCost(String userInput) throws AddOrEditTaskCommandException {
        double result;

        try {
            result = Double.parseDouble(userInput);
            if (result < 0.0) {
                throw new AddOrEditTaskCommandException("wrongEstimatedTimeRangeError");
            }
        } catch (NumberFormatException e) {
            throw new AddOrEditTaskCommandException("wrongEstimatedTimeFormatError");
        }

        return result;
    }
}
