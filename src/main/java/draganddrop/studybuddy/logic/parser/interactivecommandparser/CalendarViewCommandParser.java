package draganddrop.studybuddy.logic.parser.interactivecommandparser;

import java.time.LocalDate;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.CalendarViewCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;

/**
 * Parses the date for calendar view command
 */
public class CalendarViewCommandParser {

    /**
     * @param userInput input
     * @return date
     * @throws InteractiveCommandException too long away/ incorrect format
     */

    public static LocalDate parseDate(String userInput) throws InteractiveCommandException {
        LocalDate selectedDate;
        try {
            selectedDate = TimeParser.parseDate(userInput);
        } catch (InteractiveCommandException e) {
            throw new CalendarViewCommandException("dateFormatError");
        }
        if (Math.abs(selectedDate.getYear() - LocalDate.now().getYear()) > 50) {
            throw new CalendarViewCommandException("tooLongAway");
        }
        return selectedDate;
    }
}
