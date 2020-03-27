package seedu.address.ui.interactiveprompt.delete;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static seedu.address.ui.interactiveprompt.InteractivePromptType.DELETE_TASK;

import java.text.ParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.delete.DeleteTaskCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;
import seedu.address.model.task.Task;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;

/**
 * pending.
 */
public class DeleteTaskInteractivePrompt extends InteractivePrompt {
    static final String END_OF_COMMAND_MSG = "Task deleted successfully!";
    static final String QUIT_COMMAND_MSG = "Successfully quited from delete task command.";

    private int index;

    public DeleteTaskInteractivePrompt() {
        super();
        this.interactivePromptType = DELETE_TASK;
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        } else {
            userInput = checkForBackInput(userInput);
        }

        switch (currentTerm) {
        case INIT:
            this.reply = "Please enter the index number of task you wish to delete.";
            currentTerm = InteractivePromptTerms.TASK_INDEX;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
            break;

        case TASK_INDEX:
            try {
                index = Integer.parseInt(userInput);
                if (index > Task.getCurrentTasks().size() || index <= 0) {
                    throw new DeleteTaskCommandException("invalidIndexRangeError");
                }
                reply = "The task " + Task.getCurrentTasks().get(index - 1).getTaskName() + " will be deleted. \n "
                    + " Please click enter again to make the desired deletion.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                lastTerm = InteractivePromptTerms.TASK_DATETIME;
                terms.add(lastTerm);
            } catch (NumberFormatException ex) {
                reply = (new DeleteTaskCommandException("wrongIndexFormatError")).getErrorMessage();
            } catch (DeleteTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                DeleteTaskCommand deleteTaskCommand = new DeleteTaskCommand(Index.fromZeroBased(index - 1));
                logic.executeCommand(deleteTaskCommand);
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
