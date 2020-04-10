package draganddrop.studybuddy.ui.interactiveprompt.edit;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.UNARCHIVE_TASK;

import java.text.ParseException;

import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.edit.UnarchiveTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.UnarchiveTaskCommandException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * RRepresents an UnarchiveTaskInteractivePrompt, which interacts with user to unarchive a task.
 */
public class UnarchiveTaskInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quited from unarchive command.";
    private static final String END_OF_COMMAND_MSG = "Task retrieved successfully!";
    private static final String REQUIRED_INDEX_MSG = "Please enter the index number of task you wish to retrieve.";
    private int index;

    public UnarchiveTaskInteractivePrompt() {
        super();
        this.interactivePromptType = UNARCHIVE_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equalsIgnoreCase(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {

        case INIT:
            this.reply = "Please enter the index number of the archived task.";
            currentTerm = InteractivePromptTerms.TASK_INDEX;
            break;

        case TASK_INDEX:
            try {
                if (userInput.isBlank()) {
                    throw new UnarchiveTaskCommandException("emptyInputError");
                }
                index = Integer.parseInt(userInput);
                if (index > Task.getArchivedTasks().size() || index <= 0) {
                    throw new UnarchiveTaskCommandException("invalidIndexRangeError");
                }
                index = Integer.parseInt(userInput);
                reply = "The task " + Task.getArchivedTasks().get(index - 1).getTaskName() + " will be retrieved. \n\n"
                        + "Please press enter again to make the desired changes.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (NumberFormatException ex) {
                reply = (new UnarchiveTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                        + "\n\n" + REQUIRED_INDEX_MSG;
            } catch (UnarchiveTaskCommandException ex) {
                reply = ex.getErrorMessage()
                        + "\n\n" + REQUIRED_INDEX_MSG;
            }
            break;

        case READY_TO_EXECUTE:
            try {
                UnarchiveTaskCommand archiveTaskCommand = new UnarchiveTaskCommand(Index.fromZeroBased(index - 1));
                logic.executeCommand(archiveTaskCommand);
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
    public void endInteract(String reply) {
        this.reply = reply;
        super.setEndOfCommand(true);
    }

}
