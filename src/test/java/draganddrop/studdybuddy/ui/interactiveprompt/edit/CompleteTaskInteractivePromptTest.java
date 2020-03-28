package draganddrop.studdybuddy.ui.interactiveprompt.edit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is the test class for clear tasks interactive prompt
 */
class CompleteTaskInteractivePromptTest {

    @Test
    public void interact_quitCommand_returnMessage() {
        CompleteTaskInteractivePrompt prompt = new CompleteTaskInteractivePrompt();
        assertEquals(CompleteTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        CompleteTaskInteractivePrompt prompt = new CompleteTaskInteractivePrompt();
        assertEquals("Please enter the index number of task you wish to mark as done.",
                prompt.interact(""));
    }

}
