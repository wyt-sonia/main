package draganddrop.studybuddy.logic.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.model.task.Task;

/**
 * Represents a TimeParser, which can parse String to dateTime or parse dateTime to String.
 *
 * @@author Wang Yuting
 */
public class TimeParser {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Parses {@code userInput} to LocalDateTime variable.
     *
     * @param userInput
     * @return LocalDateTime when {@code userInput} format is valid.
     * @throws InteractiveCommandException when {@code userInput} format is invalid.
     */
    public static LocalDateTime parseDateTime(String userInput) throws InteractiveCommandException {
        LocalDateTime inputTime;
        try {
            inputTime = LocalDateTime.parse(userInput.trim(), Task.DATETIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InteractiveCommandException("dataTimeFormatError");
        }
        assert inputTime != null
            : "The result of time parsing is null, please check.\n";
        return inputTime;
    }

    /**
     * Parses {@code userInput} to LocalDate variable.
     *
     * @param userInput
     * @return LocalDate when {@code userInput} format is valid.
     * @throws InteractiveCommandException when {@code userInput} format is invalid.
     */
    public static LocalDate parseDate(String userInput) throws InteractiveCommandException {
        LocalDate inputDate = null;
        try {
            inputDate = LocalDate.parse(userInput.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InteractiveCommandException("dataFormatError");
        }
        assert inputDate != null
            : "The result of time parsing is null, please check.\n";
        return inputDate;
    }


    /**
     * Converts LocalDateTime variable {@code dateTime} to a String as HH:mm dd/MM/yyyy format.
     *
     * @param dateTime
     * @return String as HH:mm dd/MM/yyyy format
     * @throws InteractiveCommandException
     */
    public static String getDateTimeString(LocalDateTime dateTime) throws InteractiveCommandException {
        String min = dateTime.getMinute() < 10 ? "0" + dateTime.getMinute()
            : "" + dateTime.getMinute();
        String hour = dateTime.getHour() < 10 ? "0" + dateTime.getHour()
            : "" + dateTime.getHour();
        String day = dateTime.getDayOfMonth() < 10 ? "0" + dateTime.getDayOfMonth()
            : "" + dateTime.getDayOfMonth();
        String month = dateTime.getMonthValue() < 10 ? "0" + dateTime.getMonthValue()
            : "" + dateTime.getMonthValue();

        assert !(min.isBlank() || hour.isBlank() || day.isBlank() || month.isBlank())
            : "There is blank value in min, hour, day and/or month when get DateTimeString, please check.\n";

        return hour + ":" + min
            + " " + day + "/" + month + "/" + dateTime.getYear();
    }

    /**
     * Converts LocalDateTime variable {@code date} to a String as dd/MM/yyyy format.
     *
     * @param date
     * @return String as dd/MM/yyyy format
     * @throws InteractiveCommandException
     */
    public static String getDateString(LocalDate date) throws InteractiveCommandException {
        String day = date.getDayOfMonth() < 10 ? "0" + date.getDayOfMonth()
            : "" + date.getDayOfMonth();
        String month = date.getMonthValue() < 10 ? "0" + date.getMonthValue()
            : "" + date.getMonthValue();

        assert !(day.isBlank() || month.isBlank())
            : "There is blank value in day and/or month when get DateTimeString, please check.\n";
        return day + "/" + month + "/" + date.getYear();
    }
}
