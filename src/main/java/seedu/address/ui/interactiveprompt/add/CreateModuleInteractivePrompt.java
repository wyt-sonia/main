package seedu.address.ui.interactiveprompt.add;

import seedu.address.model.task.Task;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;
import seedu.address.ui.interactiveprompt.InteractivePromptType;


import java.util.ArrayList;

public class CreateModuleInteractivePrompt extends InteractivePrompt {

    private ArrayList<InteractivePromptTerms> terms;

    public CreateModuleInteractivePrompt() {
        super();
        this.interactivePromptType = InteractivePromptType.CREATE_MODULE;
        this.reply = "";
        this.userInput = "";
        this.task = new Task();
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }
    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            // exit the command
            super.setQuit(true);
        } else if (userInput.equals("back")) {
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
