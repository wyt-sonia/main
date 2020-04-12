package draganddrop.studybuddy.ui.interactiveprompt.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

class CalendarViewInteractivePromptTest {

    //test date
    private LocalDate selectedDate = LocalDate.of(2020, Month.APRIL, 1);

    @Test
    public void interactFirstInputReturnKeywordPrompt() {
        CalendarViewInteractivePrompt prompt = new CalendarViewInteractivePrompt();
        assertEquals(CalendarViewInteractivePrompt.REQUIRED_DATE_MSG, prompt.interact("calendar"));
    }

    @Test
    public void interactSecondInputReturnKeywordPrompt() {
        CalendarViewInteractivePrompt prompt = new CalendarViewInteractivePrompt();
        prompt.interact("");
        String reply = "The tasks with date " + selectedDate.toString()
                + " will be shown.\nPress enter to continue.";
        assertEquals(reply, prompt.interact("01/04/2020"));
    }

    @Test
    public void interactQuitCommandReturnQuitMessage() {
        CalendarViewInteractivePrompt prompt = new CalendarViewInteractivePrompt();
        assertEquals(CalendarViewInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

}
