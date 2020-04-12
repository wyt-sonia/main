package draganddrop.studybuddy.logic.interactiveprompt.view;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.DUE_SOON_TASK;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.commands.view.RefreshCommand;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DueSoonRefreshCommandException;

/**
 * Represents a RefreshTaskInteractivePrompt, which interacts with user to refresh the tasks.
 */
public class RefreshTaskInteractivePrompt extends InteractivePrompt {

    private static final String END_OF_COMMAND_MSG = "Tasks' status and Due Soon list is refreshed!";
    private static final String QUIT_COMMAND_MSG = "Successfully quited from refresh command.";

    public RefreshTaskInteractivePrompt() {
        super();
        this.interactivePromptType = DUE_SOON_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equalsIgnoreCase(userInput)) {
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

}
