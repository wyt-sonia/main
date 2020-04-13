package draganddrop.studybuddy.logic.interactiveprompt.view;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.FILTER_TASK;

import java.text.ParseException;
import java.util.function.Predicate;

import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.commands.view.FilterTaskCommand;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.FilterTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.FilterTaskCommandException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskStatusEqualPredicate;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.model.task.TaskTypeEqualPredicate;

/**
 * Interaction with user for filtering tasks.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class FilterTaskInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quited from filter task command.";
    private static final String END_OF_COMMAND_MSG = "Tasks filtered successfully!";
    private static final String REQUIRED_OPTION_MSG = "Please enter your choice of filter:\n"
            + "1. Status\n"
            + "2. Type\n";
    private static final String REQUIRED_TYPE_MSG = "Please enter the task type you would like to filter by:\n"
            + "1. Assignment\n"
            + "2. Quiz\n"
            + "3. Presentation\n"
            + "4. Meeting\n"
            + "5. Exam\n"
            + "6. Others";
    private static final String REQUIRED_STATUS_MSG = "Please enter the status you would like to filter by:\n"
            + "1. Pending\n"
            + "2. Finished\n"
            + "3. Due soon\n"
            + "4. Overdue";

    private TaskStatus status;
    private TaskType type;
    private int index;

    public FilterTaskInteractivePrompt() {
        super();
        this.interactivePromptType = FILTER_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equalsIgnoreCase(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = REQUIRED_OPTION_MSG;
            currentTerm = InteractivePromptTerms.OPTION_TYPE;
            break;

        case OPTION_TYPE:
            try {
                index = FilterTaskCommandParser.parseOptionIndex(userInput);
                if (index == 1) {
                    this.reply = REQUIRED_STATUS_MSG;
                    currentTerm = InteractivePromptTerms.STATUS_TYPE;
                } else {
                    this.reply = REQUIRED_TYPE_MSG;
                    currentTerm = InteractivePromptTerms.TASKS_TYPE;
                }
            } catch (FilterTaskCommandException ex) {
                reply = ex.getErrorMessage()
                        + "\n" + REQUIRED_OPTION_MSG;
            }
            break;

        case TASKS_TYPE:
            try {
                type = FilterTaskCommandParser.parseTypeIndex(userInput);
                reply = "The tasks with type " + type.toString() + " will be filtered.\n"
                        + "Please click enter again to filter.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (FilterTaskCommandException ex) {
                reply = ex.getErrorMessage()
                        + "\n" + REQUIRED_TYPE_MSG;
            }
            break;

        case STATUS_TYPE:
            try {
                status = FilterTaskCommandParser.parseStatusIndex(userInput);
                reply = "The tasks with status " + status.toString() + " will be filtered.\n"
                        + "Please click enter again to filter.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (FilterTaskCommandException ex) {
                reply = ex.getErrorMessage()
                        + "\n" + REQUIRED_STATUS_MSG;
            }
            break;

        case READY_TO_EXECUTE:
            try {
                Predicate<Task> predicate = null;
                if (index == 1) {
                    predicate = new TaskStatusEqualPredicate(status);
                } else {
                    predicate = new TaskTypeEqualPredicate(type);
                }
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
