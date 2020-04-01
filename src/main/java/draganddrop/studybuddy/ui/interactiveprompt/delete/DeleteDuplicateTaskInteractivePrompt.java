package draganddrop.studybuddy.ui.interactiveprompt.delete;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.DELETE_DUPLICATE_TASK;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.delete.DeleteDuplicateTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DeleteDuplicateTaskCommandException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * pending.
 */
public class DeleteDuplicateTaskInteractivePrompt extends InteractivePrompt {
    static final String END_OF_COMMAND_MSG = "Duplicated task deleted successfully!";
    static final String QUIT_COMMAND_MSG = "Successfully quited from delete duplication command.";

    public DeleteDuplicateTaskInteractivePrompt() {
        super();
        this.interactivePromptType = DELETE_DUPLICATE_TASK;
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
                reply = "The duplicate tasks will be deleted\n"
                    + "Please press enter again to make the desired changes.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (DeleteDuplicateTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                DeleteDuplicateTaskCommand deleteDuplicateTaskCommand = new DeleteDuplicateTaskCommand();
                logic.executeCommand(deleteDuplicateTaskCommand);
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
