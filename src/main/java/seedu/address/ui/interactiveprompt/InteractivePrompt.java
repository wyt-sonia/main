package seedu.address.ui.interactiveprompt;

import seedu.address.logic.Logic;
import seedu.address.model.task.Task;

import java.util.ArrayList;

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
    }

    public abstract String interact(String userInput);

    public abstract void interruptInteract();

    public abstract void endInteract();

    public abstract void back();

    public abstract void next();

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
}
