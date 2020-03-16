package seedu.address.logic.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import seedu.address.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import seedu.address.model.task.Task;

/**
 * pending.
 */
public class TimeParser {

    /**
     * pending.
     */
    public static LocalDateTime parseDateTime(String userInput) throws InteractiveCommandException {
        LocalDateTime inputTime = null;
        try {
            inputTime = LocalDateTime.parse(userInput.trim(), Task.DATETIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InteractiveCommandException("dataTimeFormatError");
        }
        return inputTime;
    }

    public static String getDateTimeString(LocalDateTime dateTime) throws InteractiveCommandException {
        String min = dateTime.getMinute() < 10 ? "0" + dateTime.getMinute()
            : "" + dateTime.getMinute();
        String hour = dateTime.getHour() < 10 ? "0" + dateTime.getHour()
            : "" + dateTime.getHour();
        String day = dateTime.getDayOfMonth() < 10 ? "0" + dateTime.getDayOfMonth()
            : "" + dateTime.getDayOfMonth();
        String month = dateTime.getMonthValue() < 10 ? "0" + dateTime.getMonthValue()
            : "" + dateTime.getMonthValue();
        return hour + ":" + min
            + " " + day + "/" + month + "/" + dateTime.getYear();
    }
}
