package seedu.address.ui.interactiveprompt;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HelpInteractivePromptTest {
    HelpInteractivePrompt hip = new HelpInteractivePrompt();

    @Test
    void interact() {
        assertEquals(hip.interact("help"), HelpInteractivePrompt.END_OF_COMMAND_MSG);
    }
}