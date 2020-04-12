package draganddrop.studybuddy.logic.parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import draganddrop.studybuddy.commons.core.LogsCenter;
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
 * @@author wyt-sonia
 */
public class TaskParser {

    private static final String LOG_TAG = "TaskParser";
    private static final Logger logger = LogsCenter.getLogger(TaskParser.class);

    /**
     * Parse the {@code userInput} for task name.
     *
     * @param userInput
     * @return
     * @throws AddOrEditTaskCommandException
     */
    public static String parseName(String userInput) throws AddOrEditTaskCommandException {

        logger.log(Level.INFO, LOG_TAG + ": Start of parseName.");
        String result = "";
        if (userInput.isBlank()) {
            logger.log(Level.WARNING, LOG_TAG + ": Empty task name error.");
            throw new AddOrEditTaskCommandException("emptyInputError");
        }

        if (userInput.length() > 20) {
            logger.log(Level.WARNING, LOG_TAG + ": task name length error.");
            throw new AddOrEditTaskCommandException("taskNameLengthError");
        }

        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userInput);
        boolean isSpecialCharInside = matcher.find();
        if (isSpecialCharInside) {
            logger.log(Level.WARNING, LOG_TAG + ": Task name contains special char error.");
            throw new AddOrEditTaskCommandException("specialCharInputError");
        }

        result = userInput.trim();

        assert !result.isBlank()
            : "The result of parseName from TaskParser is blank, please check.\n";

        logger.log(Level.INFO, LOG_TAG + ": End of parseName with task name " + result);
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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseDescription.");

        String result = "";

        if (userInput.length() > 300) {
            logger.log(Level.WARNING, LOG_TAG + ": Task description length error.");
            throw new AddOrEditTaskCommandException("taskDescriptionLengthError");
        }

        result = userInput.trim();

        logger.log(Level.INFO, LOG_TAG + ": End of parseDescription with task description " + result);

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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseDateTimeForAssignment.");
        LocalDateTime[] result = new LocalDateTime[1];

        // Check date time format
        try {
            result[0] = TimeParser.parseDateTime(userInput);
        } catch (InteractiveCommandException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }

        // Checks passed time
        if (result[0].isBefore(LocalDateTime.now())) {
            logger.log(Level.WARNING, LOG_TAG + ": Task date time is passed time.");
            throw new AddOrEditTaskCommandException("pastDateTime");
        }
        logger.log(Level.INFO, LOG_TAG + ": End of parseDateTimeForAssignment with data time "
            + TimeParser.getDateTimeString(result[0]));
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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseDateTimeForOtherTaskType.");

        LocalDateTime[] result = new LocalDateTime[2];
        String[] tempInputDateTimes;

        // Check date time format
        if (!userInput.contains("-")) {
            logger.log(Level.WARNING, LOG_TAG + ": Task date time is passed time.");
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }
        tempInputDateTimes = userInput.trim().split("-");
        if (tempInputDateTimes.length != 2 || tempInputDateTimes[0].isBlank()) {
            logger.log(Level.WARNING, LOG_TAG + ": Task date time format error.");
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }
        try {
            result[0] = TimeParser.parseDateTime(tempInputDateTimes[0]);
            result[1] = TimeParser.parseDateTime(tempInputDateTimes[1]);
        } catch (InteractiveCommandException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
            throw new AddOrEditTaskCommandException("dataTimeFormatError");
        }

        // Check passed time and start-end order
        if (result[0].isBefore(LocalDateTime.now()) || result[1].isBefore(LocalDateTime.now())) {
            logger.log(Level.WARNING, LOG_TAG + ": Task date time is passed time.");
            throw new AddOrEditTaskCommandException("pastDateTime");
        }
        if (!result[1].isAfter(result[0])) {
            logger.log(Level.WARNING, LOG_TAG + ": Task date time is end before start.");
            throw new AddOrEditTaskCommandException("eventEndBeforeStartError");
        }

        assert result.length == 2
            : "Missing start or end date time, please check.";

        logger.log(Level.INFO, LOG_TAG + ": End of parseDateTimeForOtherTaskType with date time as "
            + TimeParser.getDateTimeString(result[0]) + "-" + TimeParser.getDateTimeString(result[1]));

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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseDateTime.");

        if (taskType.equals(TaskType.Assignment)) {
            result = parseDateTimeForAssignment(userInput);
        } else {
            result = parseDateTimeForOtherTaskType(userInput);
        }

        assert result != null
            : "The result of parseDateTime from TaskParser is null, please check.\n";
        logger.log(Level.INFO, LOG_TAG + ": End of parseDateTime.");
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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseModule.");

        Module result;

        try {
            if (ModuleCode.isModuleCode(userInput)) {
                List<Module> tempModules = modules.stream()
                    .filter(m -> m.getModuleCode().toString().equals(userInput.toUpperCase()))
                    .collect(Collectors.toList());
                if (tempModules.isEmpty()) {
                    logger.log(Level.WARNING, LOG_TAG + ": No such module error.");
                    throw new AddOrEditTaskCommandException("noSuchModuleError");
                }
                result = tempModules.get(0);
            } else {
                int index = Integer.parseInt(userInput) - 1;
                if (index < 0 || index >= modules.size()) {
                    logger.log(Level.WARNING, LOG_TAG + ": Invalid index range error.");
                    throw new AddOrEditTaskCommandException("invalidIndexRangeError");
                }
                result = modules.get(index);
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
            throw new AddOrEditTaskCommandException("wrongIndexFormatError");
        }

        assert result != null
            : "The result of parseModule from TaskParser is null, please check.\n";

        logger.log(Level.INFO, LOG_TAG + ": End of parseModule with value " + result.toString());

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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseType.");

        TaskType result;

        if (userInput.isBlank()) {
            logger.log(Level.WARNING, LOG_TAG + ": Empty input error.");
            throw new AddOrEditTaskCommandException("emptyInputError");
        }

        int index = Integer.parseInt(userInput.trim());
        if (index <= 0 || index > size) {
            logger.log(Level.WARNING, LOG_TAG + ": Invalid index range error.");
            throw new AddOrEditTaskCommandException("invalidIndexRangeError");
        }

        result = TaskType.getTaskTypes()[index - 1];
        assert result != null
            : "The result of parseType from TaskParser is null, please check.\n";

        logger.log(Level.INFO, LOG_TAG + ": End of parseType with value " + result.toString());

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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseWeight");
        try {
            result = Double.parseDouble(userInput);
            if (result < 0.0 || result > 100.0) {
                logger.log(Level.WARNING, LOG_TAG + ": Wrong weight range error.");
                throw new AddOrEditTaskCommandException("wrongWeightRangeError");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
            throw new AddOrEditTaskCommandException("wrongWeightFormatError");
        }

        logger.log(Level.INFO, LOG_TAG + ": End of parseWeight with value " + result);

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
        logger.log(Level.INFO, LOG_TAG + ": Start of parseTimeCost ");

        double result;

        try {
            result = Double.parseDouble(userInput);
            if (result < 0.0) {
                logger.log(Level.WARNING, LOG_TAG + ": Wrong estimated time range error.");
                throw new AddOrEditTaskCommandException("wrongEstimatedTimeRangeError");
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
            throw new AddOrEditTaskCommandException("wrongEstimatedTimeFormatError");
        }
        logger.log(Level.INFO, LOG_TAG + ": End of parseTimeCost with value " + result);

        return result;
    }
}
