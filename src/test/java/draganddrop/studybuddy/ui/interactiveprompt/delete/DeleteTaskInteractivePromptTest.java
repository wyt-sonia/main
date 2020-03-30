package draganddrop.studybuddy.ui.interactiveprompt.delete;

import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;

class DeleteTaskInteractivePromptTest {

    private Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());
    @Test
    public void interact_quitCommand_returnMessage() {
        DeleteTaskInteractivePrompt prompt = new DeleteTaskInteractivePrompt();
        assertEquals(DeleteTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        DeleteTaskInteractivePrompt prompt = new DeleteTaskInteractivePrompt();
        assertEquals("Please enter the index number of task you wish to delete.",
                prompt.interact(""));
    }

    /*@Test
    public void interact_secondInput_returnKeywordPrompt() {
        DeleteTaskInteractivePrompt prompt = new DeleteTaskInteractivePrompt();
        prompt.interact("");
        String reply = "The task " + model.getAddressBook().getTaskList().get(0).getTaskName()
                + " will be deleted. \n"
                + " Please click enter again to make the desired deletion.";
        assertEquals(reply, reply);
    }*/

}
