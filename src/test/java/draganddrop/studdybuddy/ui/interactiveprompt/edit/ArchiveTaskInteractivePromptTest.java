package draganddrop.studdybuddy.ui.interactiveprompt.edit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is the test class for clear tasks interactive prompt
 */
class ArchiveTaskInteractivePromptTest {

    @Test
    public void interact_quitCommand_returnMessage() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        assertEquals(ArchiveTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        assertEquals("Please enter the index number of task you wish to archive.",
                prompt.interact(""));
    }

}
