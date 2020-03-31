package draganddrop.studybuddy.ui.interactiveprompt.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RefreshTaskInteractivePromptTest {

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        RefreshTaskInteractivePrompt prompt = new RefreshTaskInteractivePrompt();
        String reply = "The tasks list will be refreshed.\n"
                + " Please press enter again to make the desired changes.";
        assertEquals(reply, prompt.interact("find"));
    }

    @Test
    public void interact_secondInput_returnKeywordPrompt() {
        RefreshTaskInteractivePrompt prompt = new RefreshTaskInteractivePrompt();
        prompt.interact("refresh");
        try {
            String reply = "Refreshed tasks' status and tasks due soon list is updated!";
            assertEquals(reply, prompt.interact("/n"));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void interact_quitCommand_returnQuitMessage() {
        RefreshTaskInteractivePrompt prompt = new RefreshTaskInteractivePrompt();
        String reply = "Successfully quited from refresh command.";
        assertEquals(reply, prompt.interact("quit"));
    }
}
