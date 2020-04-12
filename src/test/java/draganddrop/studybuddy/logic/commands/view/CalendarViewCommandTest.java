package draganddrop.studybuddy.logic.commands.view;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

class CalendarViewCommandTest {

    @Test
    public void equals() {

        LocalDate selectedDate1 = LocalDate.of(2020, Month.APRIL, 1);
        LocalDate selectedDate2 = LocalDate.of(2020, Month.APRIL, 2);

        CalendarViewCommand cvFirstCommand = new CalendarViewCommand(selectedDate1);
        CalendarViewCommand cvSecondCommand = new CalendarViewCommand(selectedDate2);

        // same object -> returns true
        assertTrue(cvFirstCommand.equals(cvFirstCommand));

        // same values -> returns true
        CalendarViewCommand calendarViewCommandCopy = new CalendarViewCommand(selectedDate1);
        assertTrue(cvFirstCommand.equals(calendarViewCommandCopy));

        // different types -> returns false
        assertFalse(cvFirstCommand.equals(1));

        // null -> returns false
        assertFalse(cvFirstCommand == null);

        // different person -> returns false
        assertFalse(cvFirstCommand.equals(cvSecondCommand));
    }

}
