package draganddrop.studybuddy.logic.interactiveprompt.view;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.VIEW_DUPLICATE_TASK;

import java.text.ParseException;
import java.util.function.Predicate;

import draganddrop.studybuddy.logic.commands.delete.ViewRenamedTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.ViewRenamedTaskCommandException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskRenamedPredicate;

/**
 * Represents a ViewRenamedTaskInteractivePrompt, which interacts with user to View duplicate tasks.
 *
 * @@author souwmyaa
 */
public class ViewRenamedTaskInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quited from view renamed command.";
    private static final String END_OF_COMMAND_MSG = "Filtered renamed tasks successfully!";

    public ViewRenamedTaskInteractivePrompt() {
        super();
        this.interactivePromptType = VIEW_DUPLICATE_TASK;
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
                reply = "The renamed tasks will be filtered\n"
                    + "Please press enter again to make the desired changes.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (ViewRenamedTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                Predicate<Task> predicate = new TaskRenamedPredicate();
                ViewRenamedTaskCommand viewRenamedTaskCommand = new ViewRenamedTaskCommand(predicate);
                logic.executeCommand(viewRenamedTaskCommand);
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
