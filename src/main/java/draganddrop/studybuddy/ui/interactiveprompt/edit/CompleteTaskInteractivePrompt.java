package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.COMPLETE_TASK;

import java.text.ParseException;

import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.edit.CompleteTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.CompleteTaskCommandException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * A interactive prompt for completing task.
 */
public class CompleteTaskInteractivePrompt extends InteractivePrompt {

    public static final String QUIT_COMMAND_MSG = "Successfully quited from complete task command.";
    private static final String END_OF_COMMAND_MSG = "Task marked as completed successfully!\n"
            + "If you want to archive this completed task, key in 'archive'.";
    private static final String REQUEST_INDEX_MSG = "Please enter the index number "
        + "of task you wish to mark as finished.";

    private int index;

    public CompleteTaskInteractivePrompt() {
        super();
        this.interactivePromptType = COMPLETE_TASK;
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
                    throw new CompleteTaskCommandException("emptyInputError");
                }
                index = Integer.parseInt(userInput);
                if (index > Task.getCurrentTasks().size() || index <= 0) {
                    throw new CompleteTaskCommandException("invalidIndexRangeError");
                } else if (logic.getFilteredTaskList().get(index - 1).getTaskStatus().equals(TaskStatus.FINISHED)) {
                    throw new CompleteTaskCommandException("taskCompletedError");
                }
                reply = "The task " + Task.getCurrentTasks().get(index - 1).getTaskName()
                    + " will be marked as finished. \n\n"
                    + "Please click enter again to make the desired action.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (NumberFormatException ex) {
                reply = (new CompleteTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                    + "\n\n" + REQUEST_INDEX_MSG;
            } catch (CompleteTaskCommandException ex) {
                reply = ex.getErrorMessage()
                    + "\n\n" + REQUEST_INDEX_MSG;
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
            break;
        }
        return reply;
    }

}
