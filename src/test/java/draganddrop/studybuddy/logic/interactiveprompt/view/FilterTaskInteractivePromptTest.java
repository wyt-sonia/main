package draganddrop.studybuddy.logic.interactiveprompt.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Test class for FilterTaskInteractivePrompt.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class FilterTaskInteractivePromptTest {

    @Test
    public void interactquitCommandreturnMessage() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        assertEquals("Successfully quited from filter task command.", prompt.interact("quit"));
    }
    @Test
    public void interactfirstInputreturnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        assertEquals("Please enter your choice of filter:\n"
                        + "1. Status\n"
                        + "2. Type\n",
                prompt.interact("filter"));
    }

    @Test
    public void interactsecondInput1returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        assertEquals("Please enter the status you would like to filter by:\n"
                + "1. Pending\n"
                + "2. Finished\n"
                + "3. Due soon\n"
                + "4. Overdue", prompt.interact("1"));
    }

    @Test
    public void interactthirdInput1returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("1");
        assertEquals("The tasks with status PENDING will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("1"));
    }

    @Test
    public void interactthirdInput2returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("1");
        assertEquals("The tasks with status FINISHED will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("2"));
    }

    @Test
    public void interactthirdInput3returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("1");
        assertEquals("The tasks with status DUE_SOON will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("3"));
    }

    @Test
    public void interactthirdInput4returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("1");
        assertEquals("The tasks with status OVERDUE will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("4"));
    }

    @Test
    public void interactsecondInput2returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        assertEquals("Please enter the task type you would like to filter by:\n"
                + "1. Assignment\n"
                + "2. Quiz\n"
                + "3. Presentation\n"
                + "4. Meeting\n"
                + "5. Exam\n"
                + "6. Others", prompt.interact("2"));
    }

    @Test
    public void interactthirdInput5returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("2");
        assertEquals("The tasks with type Assignment will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("1"));
    }

    @Test
    public void interactthirdInput6returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("2");
        assertEquals("The tasks with type Quiz will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("2"));
    }

    @Test
    public void interactthirdInput7returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("2");
        assertEquals("The tasks with type Presentation will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("3"));
    }

    @Test
    public void interactthirdInput8returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("2");
        assertEquals("The tasks with type Meeting will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("4"));
    }

    @Test
    public void interactthirdInput9returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("2");
        assertEquals("The tasks with type Exam will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("5"));
    }

    @Test
    public void interactthirdInput10returnKeywordPrompt() {
        FilterTaskInteractivePrompt prompt = new FilterTaskInteractivePrompt();
        prompt.interact("filter");
        prompt.interact("2");
        assertEquals("The tasks with type Others will be filtered.\n"
                + "Please click enter again to filter.", prompt.interact("6"));
    }
}
