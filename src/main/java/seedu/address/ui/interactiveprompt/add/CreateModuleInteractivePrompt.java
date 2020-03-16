package seedu.address.ui.interactiveprompt.add;

import seedu.address.model.task.Task;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;

import java.util.ArrayList;

public class createNewModuleInteractivePrompt extends InteractivePrompt {

    private String reply;
    private String userInput;
    private Task task;
    private InteractivePromptTerms currentTerm;
    private InteractivePromptTerms lastTerm;
    private ArrayList<InteractivePromptTerms> terms;

    public AddTaskInteractivePrompt() {
        super();
        this.interactivePromptType = ADD_TASK;
        this.reply = "";
        this.userInput = "";
        this.task = new Task();
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }
    @Override
    public String interact(String userInput) {
        return null;
    }

    @Override
    public void interruptInteract() {

    }

    @Override
    public void endInteract() {

    }

    @Override
    public void back() {

    }

    @Override
    public void next() {

    }
}
