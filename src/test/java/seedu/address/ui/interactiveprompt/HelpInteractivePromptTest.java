package seedu.address.ui.interactiveprompt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.ui.interactiveprompt.view.HelpInteractivePrompt;

class HelpInteractivePromptTest {
    private HelpInteractivePrompt hip = new HelpInteractivePrompt();

    @Test
    public void interact() {
        assertEquals(hip.interact("help"), HelpInteractivePrompt.getHelpMessage());
    }
}
