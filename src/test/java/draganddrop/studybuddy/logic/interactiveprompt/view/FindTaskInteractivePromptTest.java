package draganddrop.studybuddy.logic.interactiveprompt.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FindTaskInteractivePromptTest {

    @Test
    public void interactFirstInputReturnKeywordPrompt() {
        FindTaskInteractivePrompt prompt = new FindTaskInteractivePrompt();
        assertEquals(prompt.getKeywordPrompt(), prompt.interact("find"));
    }

    @Test
    public void interactSecondInputReturnKeywordPrompt() {
        FindTaskInteractivePrompt prompt = new FindTaskInteractivePrompt();
        prompt.interact("find");
        assertEquals(prompt.getConfirmationPrompt("randomKeyword"), prompt.interact("randomKeyword"));
    }

    @Test
    public void interactQuitCommandReturnQuitMessage() {
        FindTaskInteractivePrompt prompt = new FindTaskInteractivePrompt();
        assertEquals(prompt.getQuitMessage(), prompt.interact("quit"));
    }
}
