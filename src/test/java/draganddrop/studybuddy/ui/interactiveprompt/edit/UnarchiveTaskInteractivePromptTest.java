package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UnarchiveTaskInteractivePromptTest {

    @Test
    public void interact_quitCommand_returnMessage() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        assertEquals(UnarchiveTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        assertEquals("Please enter the index number of the archived task.",
                prompt.interact(""));
    }

    @Test
    public void interact_secondInput_returnKeywordPrompt() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        prompt.interact("");
        assertEquals("The task at index 1 will be retrieved. \n "
                        + " Please press enter again to make the desired changes.",
                prompt.interact("1"));
    }

    @Test
    public void interact_thirdInput_returnPrompt() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        prompt.interact("");
        prompt.interact("1");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }
}
