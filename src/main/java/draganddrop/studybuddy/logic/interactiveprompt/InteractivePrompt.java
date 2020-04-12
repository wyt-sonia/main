package draganddrop.studybuddy.logic.interactiveprompt;

import draganddrop.studybuddy.logic.Logic;

/**
 * Represents an InteractivePrompt.
 *
 * @@author Wang Yuting
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
     * Ends the interaction.
     *
     * @param reply message to be displayed to the user upon end of interaction
     */
    public void endInteract(String reply) {
        this.reply = reply;
        setEndOfCommand(true);
    }

    /**
     * Checks if the {@code userInput} is quit.
     *
     * @param userInput
     * @return true if the userInput is quit, false otherwise
     */
    public boolean isQuit(String userInput) {
        return "quit".equalsIgnoreCase(userInput);
    }

    public boolean isQuit() {
        return isQuit;
    }

    /**
     * Handles the quit commands.
     *
     * @param userInput   the input given by the user. This should be checked to be either quit or back.
     * @param quitMessage the message that is displayed upon quitting
     * @return the reply to user
     */
    public String handleQuit(String userInput, String quitMessage) {
        if ("quit".equalsIgnoreCase(userInput)) {
            endInteract(quitMessage);
        }
        return reply;
    }

    public void setQuit(boolean isQuitCommand) {
        isQuit = isQuitCommand;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public boolean isEndOfCommand() {
        return isEndOfCommand;
    }

    public void setEndOfCommand(boolean isEnd) {
        isEndOfCommand = isEnd;
    }

    public InteractivePromptType getInteractivePromptType() {
        return interactivePromptType;
    }
}
