package draganddrop.studybuddy.ui.interactiveprompt.view;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.DUE_SOON_TASK;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.commands.view.RefreshCommand;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DueSoonRefreshCommandException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * pending.
 */
public class RefreshTaskInteractivePrompt extends InteractivePrompt {

    static final String END_OF_COMMAND_MSG = "Tasks' status and due soon list is refreshed!";
    static final String QUIT_COMMAND_MSG = "Successfully quited from refresh command.";

    public RefreshTaskInteractivePrompt() {
        super();
        this.interactivePromptType = DUE_SOON_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equals(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            try {
                reply = "The tasks list will be refreshed.\n"
                    + " Please press enter again to make the desired changes.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
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

    /**
     * pending.
     */
    private String dateTime() {
        String result = "";


        return result;
    }
}
