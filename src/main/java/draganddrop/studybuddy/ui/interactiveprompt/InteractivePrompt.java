package draganddrop.studybuddy.ui.interactiveprompt;

import draganddrop.studybuddy.logic.Logic;

/**
 * pending.
 */
public abstract class InteractivePrompt {
    protected Logic logic;
    protected boolean isQuit;
    protected boolean isEndOfCommand;
    protected InteractivePromptType interactivePromptType;
    protected String reply;
    protected String userInput;
    protected InteractivePromptTerms currentTerm;


    public InteractivePrompt() {
        this.isQuit = false;
        this.isEndOfCommand = false;
        this.reply = "";
        this.currentTerm = InteractivePromptTerms.INIT;
    }

    public abstract String interact(String userInput);

    /**
     * ends the interaction
     *
     * @param reply message to be displayed to the user upon end of interaction
     */
    public void endInteract(String reply) {
        this.reply = reply;
        setEndOfCommand(true);
    }

    public boolean isQuit() {
        return isQuit;
    }

    public void setQuit(boolean quit) {
        isQuit = quit;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public boolean isEndOfCommand() {
        return isEndOfCommand;
    }

    public void setEndOfCommand(boolean endOfCommand) {
        isEndOfCommand = endOfCommand;
    }

    public InteractivePromptType getInteractivePromptType() {
        return interactivePromptType;
    }

    /**
     * Handles the quit and back commands
     *
     * @param userInput   the input given by the user. This should be checked to be either quit or back.
     * @param quitMessage the message that is displayed upon quitting
     * @return the reply to user
     */
    public String handleQuit(String userInput, String quitMessage) {
        if ("quit".equals(userInput)) {
            endInteract(quitMessage);
        }
        return reply;
    }

    /**
     * checks if the userInput is quit or back
     *
     * @param userInput
     * @return true if the userInput is quit or back, false otherwise
     */
    public boolean isQuit(String userInput) {
        return "quit".equals(userInput);
    }
}
