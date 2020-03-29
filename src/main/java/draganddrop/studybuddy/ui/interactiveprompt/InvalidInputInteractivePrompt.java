package draganddrop.studybuddy.ui.interactiveprompt;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.INVALID_MSG;

/**
 * Represents a InvalidInputInteractivePrompt which is designed to handle invalid input from user.
 * Help message will be provided when invalid message caught.
 */
public class InvalidInputInteractivePrompt extends InteractivePrompt {
    static final String HELP_MSG = "Please enter a valid command option.\n"
        + "1. add  2. delete  3. edit  4. bye  5. sort  6. find  7. done  8. delete duplicates  "
        + "9. sort  10. archive  11. help  12. list  13. clear";

    private InteractivePromptTerms currentTerm;
    private String reply;

    public InvalidInputInteractivePrompt() {
        super();
        this.reply = "";
        this.interactivePromptType = INVALID_MSG;
        this.currentTerm = InteractivePromptTerms.INIT;
    }


    @Override
    public String interact(String userInput) {
        switch (currentTerm) {

        case INIT:
            this.reply = HELP_MSG;
            currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            break;

        case READY_TO_EXECUTE:
        default:
            endInteract("");
        }
        return reply;
    }

    @Override
    public void interruptInteract() {

    }

    @Override
    public void endInteract(String reply) {
        this.reply = reply;
        super.setEndOfCommand(true);
    }

    @Override
    public void back() {

    }

    @Override
    public void next() {

    }
}
