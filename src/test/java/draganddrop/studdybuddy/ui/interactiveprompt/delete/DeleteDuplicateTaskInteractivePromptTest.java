package draganddrop.studdybuddy.ui.interactiveprompt.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DeleteDuplicateTaskInteractivePromptTest {

    @Test
    public void interact_quitCommand_returnMessage() {
        DeleteDuplicateTaskInteractivePrompt prompt = new DeleteDuplicateTaskInteractivePrompt();
        assertEquals(DeleteDuplicateTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        String reply = "The duplicate tasks will be deleted\n "
                + " Please press enter again to make the desired changes.";
        DeleteDuplicateTaskInteractivePrompt prompt = new DeleteDuplicateTaskInteractivePrompt();
        assertEquals(reply, prompt.interact(""));
    }

    @Test
    public void interact_secondInput_returnKeywordPrompt() {
        DeleteDuplicateTaskInteractivePrompt prompt = new DeleteDuplicateTaskInteractivePrompt();
        prompt.interact("");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }

}
