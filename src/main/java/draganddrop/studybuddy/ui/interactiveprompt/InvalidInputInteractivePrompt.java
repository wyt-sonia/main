package draganddrop.studybuddy.ui.interactiveprompt;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.INVALID_MSG;

/**
 * Represents a InvalidInputInteractivePrompt which is designed to handle invalid input from user.
 * Help message will be provided when invalid message caught.
 */
public class InvalidInputInteractivePrompt extends InteractivePrompt {
    static final String HELP_MSG = "Please enter a valid command option.\n"
        + "1. add\n2. delete\n3. edit\n4. bye\n5. sort\n6. find\n7. done\n8. view duplicates\n"
        + "9. sort\n10. archive\n11. help\n12. list\n13. clear";

    private InteractivePromptTerms currentTerm;
    private String reply;

    public InvalidInputInteractivePrompt() {
        super();
        this.interactivePromptType = INVALID_MSG;
    }


    @Override
    public String interact(String userInput) {
        endInteract(HELP_MSG);
        return reply;
    }

    @Override
    public void endInteract(String reply) {
        this.reply = reply;
        super.setEndOfCommand(true);
    }
}
