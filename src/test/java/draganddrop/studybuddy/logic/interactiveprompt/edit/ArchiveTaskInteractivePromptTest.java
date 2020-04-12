package draganddrop.studybuddy.logic.interactiveprompt.edit;

import static draganddrop.studybuddy.testutil.Assert.assertThrows;
import static draganddrop.studybuddy.testutil.TypicalTasks.getSampleTasks;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.model.task.Task;

/**
 * This is the test class for clear tasks interactive prompt
 */
class ArchiveTaskInteractivePromptTest {

    @BeforeEach
    public void setUp() {
        ArrayList<Task> temp = new ArrayList<>(Arrays.asList(getSampleTasks()));
        Task.updateCurrentTaskList(temp);
    }

    @Test
    public void interactQuitCommandReturnMessage() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        assertEquals(ArchiveTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interactFirstInputReturnKeywordPrompt() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        assertEquals("Please enter the index number of task you wish to archive.",
                prompt.interact(""));
    }

    @Test
    public void interactSecondInputReturnKeywordPrompt() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        prompt.interact("");
        assertEquals("The task " + Task.getCurrentTasks().get(0).getTaskName() + " will be archived. \n\n"
                        + "Please press enter again to make the desired changes.",
                prompt.interact("1"));
    }

    @Test
    public void interactThirdInputReturnPrompt() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        prompt.interact("");
        prompt.interact("1");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }



}
