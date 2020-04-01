package draganddrop.studybuddy.ui.interactiveprompt.edit;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.ARCHIVE_TASK;

import java.text.ParseException;

import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.edit.ArchiveTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.ArchiveTaskCommandException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * pending.
 */
public class ArchiveTaskInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG = "Task archived successfully!";
    public static final String QUIT_COMMAND_MSG = "Successfully quited from archive command.";
    private static final String REQUEST_INDEX_MSG = "Please enter the index number of task you wish to archive.";
    private int index;

    public ArchiveTaskInteractivePrompt() {
        super();
        this.interactivePromptType = ARCHIVE_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equals(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {

        case INIT:
            this.reply = REQUEST_INDEX_MSG;
            currentTerm = InteractivePromptTerms.TASK_INDEX;
            break;

        case TASK_INDEX:
            try {
                if (userInput.isBlank()) {
                    throw new ArchiveTaskCommandException("emptyInputError");
                }
                index = Integer.parseInt(userInput);
                if (index > Task.getCurrentTasks().size() || index <= 0) {
                    throw new ArchiveTaskCommandException("invalidIndexRangeError");
                }
                index = Integer.parseInt(userInput);
                reply = "The task " + Task.getCurrentTasks().get(index - 1).getTaskName() + " will be archived. \n\n"
                    + "Please press enter again to make the desired changes.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (NumberFormatException ex) {
                reply = (new ArchiveTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                    + "\n\n" + REQUEST_INDEX_MSG;
            } catch (ArchiveTaskCommandException ex) {
                reply = ex.getErrorMessage()
                    + "\n\n" + REQUEST_INDEX_MSG;
            }
            break;

        case READY_TO_EXECUTE:
            try {
                ArchiveTaskCommand archiveTaskCommand = new ArchiveTaskCommand(Index.fromZeroBased(index - 1));
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
