package draganddrop.studybuddy.ui.interactiveprompt.add;

import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;

class CreateModuleInteractivePromptTest {

    private Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());

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
        String reply = "Module Code: " + "CS1101S" + "\n"
                + "Click 'Enter' again to confirm your changes";
        assertEquals(reply, prompt.interact("CS1101S"));
    }


    @Test
    public void interact_fourthInput_returnKeywordPrompt() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.interact("");
        prompt.interact("a");
        prompt.interact("CS1101S");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }

    @Test
    public void interact_noModuleName_returnCorrectReply() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.interact("");
        assertEquals("Please key in something as your module name", prompt.interact(""));
    }

    @Test
    public void interact_invalidModuleCode_returnCorrectReply() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.interact("");
        prompt.interact("a");
        assertEquals(CreateModuleInteractivePrompt.MODULE_CODE_FORMAT, prompt.interact("Csdsdsa"));
        assertEquals(CreateModuleInteractivePrompt.MODULE_CODE_FORMAT, prompt.interact("C111"));
        assertEquals(CreateModuleInteractivePrompt.MODULE_CODE_FORMAT, prompt.interact("C1110"));
    }

    @Test
    public void endInteract_test() {
        CreateModuleInteractivePrompt prompt = new CreateModuleInteractivePrompt();
        prompt.endInteract("a");
    }

}
