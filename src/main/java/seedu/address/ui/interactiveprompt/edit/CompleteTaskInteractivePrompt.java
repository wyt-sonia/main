package seedu.address.ui.interactiveprompt.edit;

import static seedu.address.ui.interactiveprompt.InteractivePromptType.COMPLETE_TASK;

import java.text.ParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.edit.CompleteTaskCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;

/**
 * A interactive prompt for completing task.
 */
public class CompleteTaskInteractivePrompt extends InteractivePrompt {

    static final String END_OF_COMMAND_MSG = "Task marked as completed successfully!";
    static final String QUIT_COMMAND_MSG = "Successfully quited from complete task command.";

    private int index;

    public CompleteTaskInteractivePrompt() {
        super();
        this.interactivePromptType = COMPLETE_TASK;
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
            this.reply = "Please enter the index number of task you wish to mark as done.";
            currentTerm = InteractivePromptTerms.TASK_INDEX;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
            break;

        case TASK_INDEX:
            try {
                index = Integer.parseInt(userInput);
                reply = "The task at index " + userInput + " will be mark as Done. \n "
                    + " Please click enter again to make the desired deletion.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
                lastTerm = InteractivePromptTerms.TASK_DATETIME;
                terms.add(lastTerm);
            } catch (DeleteTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                CompleteTaskCommand completeTaskCommand = new CompleteTaskCommand(Index.fromZeroBased(index - 1));
                logic.executeCommand(completeTaskCommand);
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

}
