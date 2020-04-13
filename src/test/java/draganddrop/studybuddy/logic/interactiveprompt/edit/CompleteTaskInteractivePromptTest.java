package draganddrop.studybuddy.logic.interactiveprompt.edit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is the test class for complete tasks interactive prompt.
 *
 * @@author Souwmyaa Sabarinathann
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
        assertEquals("Please enter the index number of task you wish to mark as Finished.",
                prompt.interact(""));
    }
}
