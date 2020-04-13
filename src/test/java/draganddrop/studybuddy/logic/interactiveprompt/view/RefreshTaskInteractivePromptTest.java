package draganddrop.studybuddy.logic.interactiveprompt.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test class for RefreshTaskInteractivePrompt
 *
 * @@author Souwmyaa Sabarinathann
 */
class RefreshTaskInteractivePromptTest {

    @Test
    public void interactfirstInputreturnKeywordPrompt() {
        RefreshTaskInteractivePrompt prompt = new RefreshTaskInteractivePrompt();
        String reply = "The tasks list will be refreshed.\n"
                + " Please press enter again to make the desired changes.";
        assertEquals(reply, prompt.interact("refresh"));
    }

    @Test
    public void interactquitCommandreturnQuitMessage() {
        RefreshTaskInteractivePrompt prompt = new RefreshTaskInteractivePrompt();
        String reply = "Successfully quited from refresh command.";
        assertEquals(reply, prompt.interact("quit"));
    }
}
