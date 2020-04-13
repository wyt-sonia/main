package draganddrop.studybuddy.logic.interactiveprompt;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.INVALID_MSG;

/**
 * Represents a InvalidInputInteractivePrompt which is designed to handle invalid input from user.
 * Help message will be provided when invalid message caught.
 */
public class InvalidInputInteractivePrompt extends InteractivePrompt {
    static final String HELP_MSG = "Here is the list of available commands:\n"
        + "1. add\n2. archive\n3. bye\n4. clear\n5. create mods\n6. delete\n"
        + "7. view renamed\n8. done\n9. edit\n10. filter\n11. find\n"
        + "12. help\n13. list\n14. sort\n15. refresh\n16. calendar\n17. goal\n\n";

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
