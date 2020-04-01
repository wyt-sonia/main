package draganddrop.studybuddy.ui.interactiveprompt.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CreateModuleInteractivePromptTest {

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        assertEquals("Please key in the name of the module that you want to create",
                prompt.interact(""));
    }

    @Test
    public void interact_quitCommand_returnMessage() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        assertEquals(CreateModuleInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_secondInput_returnKeywordPrompt() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.interact("");
        assertEquals("The name of module is set to: " + "Software engineering" + ".\n"
                + "Now key in your module code", prompt.interact("Software engineering"));
    }

    @Test
    public void interact_thirdInput_returnKeywordPrompt() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.interact("");
        prompt.interact("a");
        String reply = "Module Code: " + "OO0000O" + "\n"
                + "Click 'Enter' again to confirm your changes";
        assertEquals(reply, prompt.interact("OO0000O"));
    }

    @Test
    public void interact_fourthInput_returnKeywordPrompt() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.interact("");
        prompt.interact("a");
        prompt.interact("OO0000O");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }

    @Test
    public void endInteract_test() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.endInteract("a");
    }

}
