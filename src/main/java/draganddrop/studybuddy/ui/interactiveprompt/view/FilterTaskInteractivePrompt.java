package draganddrop.studybuddy.ui.interactiveprompt.view;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.FILTER_TASK;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.commands.view.FilterTaskCommand;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.FilterTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.FilterTaskCommandException;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskStatusEqualPredicate;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * Interaction with user for filtering tasks.
 */
public class FilterTaskInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quited from filter task command.";
    private static final String END_OF_COMMAND_MSG = "Tasks filtered successfully!";
    private static final String REQUEST_STATUS_MSG = "Please enter the status you would like to filter by:\n"
            + "1. Pending\n"
            + "2. Finished\n"
            + "3. Due soon\n"
            + "4. Overdue";

    private TaskStatus status;

    public FilterTaskInteractivePrompt() {
        super();
        this.interactivePromptType = FILTER_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equals(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = REQUEST_STATUS_MSG;
            currentTerm = InteractivePromptTerms.STATUS_TYPE;
            break;

        case STATUS_TYPE:
            try {
                status = FilterTaskCommandParser.parseIndex(userInput);
                reply = "The tasks with status " + status.toString() + " will be filtered.\n"
                        + "Please click enter again to filter.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (FilterTaskCommandException ex) {
                reply = ex.getErrorMessage()
                        + "\n" + REQUEST_STATUS_MSG;
            }
            break;

        case READY_TO_EXECUTE:
            try {
                TaskStatusEqualPredicate predicate = new TaskStatusEqualPredicate(status);
                FilterTaskCommand filterTaskCommand = new FilterTaskCommand(predicate);
                logic.executeCommand(filterTaskCommand);
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
