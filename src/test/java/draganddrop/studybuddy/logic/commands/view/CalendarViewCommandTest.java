package draganddrop.studybuddy.logic.commands.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertEquals(cvFirstCommand, cvFirstCommand);

        // same values -> returns true
        CalendarViewCommand calendarViewCommandCopy = new CalendarViewCommand(selectedDate1);
        assertEquals(cvFirstCommand, calendarViewCommandCopy);

        // different types -> returns false
        assertNotEquals(1, cvFirstCommand);

        // null -> returns false
        assertNotNull(cvFirstCommand);

        // different task -> returns false
        assertNotEquals(cvFirstCommand, cvSecondCommand);
    }

}
