package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static draganddrop.studybuddy.testutil.TypicalTasks.getSampleArchivedTasks;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.task.Task;

class UnarchiveTaskInteractivePromptTest {

    @BeforeEach
    public void setup() {
        ArrayList<Task> temp = new ArrayList<>(Arrays.asList(getSampleArchivedTasks()));
        Task.updateArchivedTaskList(temp);
    }

    @Test
    public void interactQuitCommandReturnMessage() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        assertEquals(UnarchiveTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interactFirstInputReturnKeywordPrompt() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        assertEquals("Please enter the index number of the archived task.",
                prompt.interact(""));
    }

    @Test
    public void interactSecondInputReturnKeywordPrompt() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        prompt.interact("");
        assertEquals("The task " + Task.getArchivedTasks().get(0).getTaskName() + " will be retrieved. \n\n"
                        + "Please press enter again to make the desired changes.",
                prompt.interact("1"));
    }

    @Test
    public void interactThirdInputReturnPrompt() {
        UnarchiveTaskInteractivePrompt prompt = new UnarchiveTaskInteractivePrompt();
        prompt.interact("");
        prompt.interact("1");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }
}
