package seedu.address.ui.interactiveprompt;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static seedu.address.ui.interactiveprompt.InteractivePromptType.DUE_SOON_TASK;

import java.text.ParseException;
import java.util.ArrayList;

import seedu.address.logic.commands.DueSoonRefreshCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.interactivecommandparser.exceptions.DueSoonRefreshCommandException;

/**
 * pending.
 */
public class DueSoonTaskInteractivePrompt extends InteractivePrompt {

    static final String END_OF_COMMAND_MSG = "Refreshed tasks that are due soon!";
    static final String QUIT_COMMAND_MSG = "Successfully quited from refresh due soon command.";

    private String reply;
    private String userInput;
    private InteractivePromptTerms currentTerm;
    private InteractivePromptTerms lastTerm;
    private ArrayList<InteractivePromptTerms> terms;

    public DueSoonTaskInteractivePrompt() {
        super();
        this.interactivePromptType = DUE_SOON_TASK;
        this.reply = "";
        this.userInput = "";
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
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
                reply = "The tasks that are due soon will be refreshed.\n "
                        + " Please press enter again to make the desired changes.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                lastTerm = InteractivePromptTerms.INIT;
                terms.add(lastTerm);
            } catch (DueSoonRefreshCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                DueSoonRefreshCommand dueSoonRefreshCommand = new DueSoonRefreshCommand();
                logic.executeCommand(dueSoonRefreshCommand);
                super.setEndOfCommand(true);
                endInteract(END_OF_COMMAND_MSG);
            } catch (CommandException | ParseException ex) {
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
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
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
