package draganddrop.studdybuddy.ui.interactiveprompt.view;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static draganddrop.studdybuddy.ui.interactiveprompt.InteractivePromptType.DUE_SOON_TASK;

import java.text.ParseException;
import java.util.ArrayList;

import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studdybuddy.logic.commands.view.RefreshCommand;
import draganddrop.studdybuddy.logic.parser.interactivecommandparser.exceptions.DueSoonRefreshCommandException;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * pending.
 */
public class RefreshTaskInteractivePrompt extends InteractivePrompt {

    static final String END_OF_COMMAND_MSG = "Refreshed tasks' status and tasks due soon list is updated!";
    static final String QUIT_COMMAND_MSG = "Successfully quited from refresh command.";

    private String reply;
    private String userInput;
    private InteractivePromptTerms currentTerm;
    private InteractivePromptTerms lastTerm;
    private ArrayList<InteractivePromptTerms> terms;

    public RefreshTaskInteractivePrompt() {
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
        }

        switch (currentTerm) {
        case INIT:
            try {
                reply = "The tasks list will be refreshed.\n "
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
                RefreshCommand dueSoonRefreshCommand = new RefreshCommand();
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
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
    }

    @Override
    public void interruptInteract() {

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
