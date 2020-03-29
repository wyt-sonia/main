package draganddrop.studybuddy.ui.interactiveprompt;

import java.util.ArrayList;

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
    protected InteractivePromptTerms lastTerm;
    protected ArrayList<InteractivePromptTerms> terms;


    public InteractivePrompt() {
        this.isQuit = false;
        this.isEndOfCommand = false;
        this.reply = "";
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }

    public abstract String interact(String userInput);

    /**
     * handles user input when user keys in 'back'
     * @param userInput
     * @return the userInput
     */
    protected String checkForBackInput(String userInput) {
        if (userInput.equals("back")) {
            if (lastTerm != null) {
                terms.remove(terms.size() - 1);
                currentTerm = lastTerm;
                if (lastTerm.equals(InteractivePromptTerms.INIT)) {
                    lastTerm = null;
                } else {
                    lastTerm = terms.get(terms.size() - 1);
                }
                userInput = "";
            } else {
                this.reply = "Please type quit to exit from this command.";
            }
        }
        return userInput;
    }
    /**
     * ends the interaction
     * @param reply message to be displayed to the user upon end of interaction
     */
    public void endInteract(String reply) {
        this.reply = reply;
        setEndOfCommand(true);
    };

    public void interruptInteract() {
        // empty
    };

    public void back() {
        // empty
    };

    public void next() {
        // empty
    };

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
    public String handleQuitAndBack(String userInput, String quitMessage) {
        if (userInput.equals("quit")) {
            endInteract(quitMessage);
        } else if (userInput.equals("back")) {
            if (lastTerm != null) { //in the beginning it is null
                terms.remove(terms.size() - 1);
                currentTerm = lastTerm;
                if (lastTerm.equals(InteractivePromptTerms.INIT)) {
                    lastTerm = null;
                } else {
                    lastTerm = terms.get(terms.size() - 1);
                }
            } else {
                this.reply = "Please type quit to exit from this command.";
            }
        }
        return reply;
    }

    /**
     * checks if the userInput is quit or back
     * @param userInput
     * @return true if the userInput is quit or back, false otherwise
     */
    public boolean isQuitOrBack(String userInput) {
        return userInput.equals("quit") || userInput.equals("back");
    }
}
