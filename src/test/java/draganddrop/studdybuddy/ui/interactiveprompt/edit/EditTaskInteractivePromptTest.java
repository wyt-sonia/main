package draganddrop.studdybuddy.ui.interactiveprompt.edit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is the test class for clear tasks interactive prompt
 */
class EditTaskInteractivePromptTest {

    @Test
    public void interact_quitCommand_returnMessage() {
        EditTaskInteractivePrompt prompt = new EditTaskInteractivePrompt();
        assertEquals(EditTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        EditTaskInteractivePrompt prompt = new EditTaskInteractivePrompt();
        assertEquals("Please enter the index of the task that you wish to edit.",
                prompt.interact(""));
    }

}
