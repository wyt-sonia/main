package seedu.address.ui.interactiveprompt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.ui.interactiveprompt.view.FindTaskInteractivePrompt;

class FindTaskInteractivePromptTest {

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        FindTaskInteractivePrompt prompt = new FindTaskInteractivePrompt();
        assertEquals(prompt.getKeywordPrompt(), prompt.interact("find"));
    }

    @Test
    public void interact_secondInput_returnKeywordPrompt() {
        FindTaskInteractivePrompt prompt = new FindTaskInteractivePrompt();
        prompt.interact("find");
        assertEquals(prompt.getConfirmationPrompt("randomKeyword"), prompt.interact("randomKeyword"));
    }

    @Test
    public void interact_quitCommand_returnQuitMessage() {
        FindTaskInteractivePrompt prompt = new FindTaskInteractivePrompt();
        assertEquals(prompt.getQuitMessage(), prompt.interact("quit"));
    }
}
