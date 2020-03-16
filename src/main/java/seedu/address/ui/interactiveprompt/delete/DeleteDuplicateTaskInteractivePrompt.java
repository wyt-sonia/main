package seedu.address.ui.interactiveprompt.delete;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static seedu.address.ui.interactiveprompt.InteractivePromptType.DELETE_DUPLICATE_TASK;

import java.util.ArrayList;

import seedu.address.logic.commands.delete.DeleteDuplicateTaskCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.interactivecommandparser.exceptions.DeleteDuplicateTaskCommandException;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;

/**
 * pending.
 */
public class DeleteDuplicateTaskInteractivePrompt extends InteractivePrompt {
    public DeleteDuplicateTaskInteractivePrompt() {
        super();
        this.interactivePromptType = DELETE_DUPLICATE_TASK;
        this.reply = "";
        this.userInput = "";
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
            if (lastTerm != null) { //in the beginning it is null
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

        switch (currentTerm) {

        case INIT:
            try {
                reply = "The duplicate tasks will be deleted\n "
                            + " Please press enter again to make the desired changes.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                lastTerm = InteractivePromptTerms.INIT;
                terms.add(lastTerm);
            } catch (DeleteDuplicateTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                DeleteDuplicateTaskCommand deleteDuplicateTaskCommand = new DeleteDuplicateTaskCommand();
                logic.executeCommand(deleteDuplicateTaskCommand);
                super.setEndOfCommand(true);
                reply = "Duplicate tasks have been deleted.";
            } catch (CommandException ex) {
                reply = ex.getMessage();
            }
            break;

        default:
        }
        return reply;
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

    /**
     * pending.
     */
    private String dateTime() {
        String result = "";


        return result;
    }
}
