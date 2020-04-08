package draganddrop.studybuddy.logic.parser.interactivecommandparser;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.CalendarViewCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;

import java.time.LocalDate;

public class CalendarViewCommandParser {
    public static LocalDate parseDate(String userInput) throws InteractiveCommandException {
        LocalDate selectedDate;
        try {
            selectedDate = TimeParser.parseDate(userInput);
        } catch (InteractiveCommandException e) {
            throw new CalendarViewCommandException("dateFormatError");
        }
        if (Math.abs(selectedDate.getYear() - LocalDate.now().getYear()) > 50 ) {
            throw new CalendarViewCommandException("tooLongAway");
        }
        return selectedDate;
    }
}
