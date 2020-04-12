package draganddrop.studybuddy.logic.interactiveprompt.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HelpInteractivePromptTest {
    private HelpInteractivePrompt hip = new HelpInteractivePrompt();

    @Test
    public void interact() {
        assertEquals(hip.interact("help"), HelpInteractivePrompt.getHelpMessage());
    }
}
