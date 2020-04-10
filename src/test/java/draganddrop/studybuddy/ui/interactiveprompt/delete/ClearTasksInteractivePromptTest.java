package draganddrop.studybuddy.ui.interactiveprompt.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is the test class for clear tasks interactive prompt
 */
class ClearTasksInteractivePromptTest {

    @Test
    public void interact_quitCommand_returnMessage() {
        ClearTasksInteractivePrompt prompt = new ClearTasksInteractivePrompt();
        assertEquals(ClearTasksInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        ClearTasksInteractivePrompt prompt = new ClearTasksInteractivePrompt();
        assertEquals("Are you sure you want to clear all your tasks?\n"
                        + "Please enter yes to continue and no to go back.",
                prompt.interact(""));
    }

}
