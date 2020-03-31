package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * This is the test class for clear tasks interactive prompt
 */
class ArchiveTaskInteractivePromptTest {

    @Test
    public void interact_quitCommand_returnMessage() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        assertEquals(ArchiveTaskInteractivePrompt.QUIT_COMMAND_MSG, prompt.interact("quit"));
    }

    @Test
    public void interact_firstInput_returnKeywordPrompt() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        assertEquals("Please enter the index number of task you wish to archive.",
                prompt.interact(""));
    }

    /*@Test
    public void interact_secondInput_returnKeywordPrompt() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        prompt.interact("");
        assertEquals("The task at index 1 will be archived. \n"
                        + "Please press enter again to make the desired changes.",
                prompt.interact("1"));
    }*/

    /*@Test
    public void interact_thirdInput_returnPrompt() {
        ArchiveTaskInteractivePrompt prompt = new ArchiveTaskInteractivePrompt();
        prompt.interact("");
        prompt.interact("1");
        assertThrows(NullPointerException.class, ()->prompt.interact(""));
    }*/



}
