package draganddrop.studybuddy.logic.interactiveprompt.sort;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.SORT_TASK;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.commands.sort.SortTaskCommand;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.SortTaskCommandException;

/**
 * A interactive prompt for sorting task list.
 */
public class SortTaskInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG = "Task sorted successfully!";
    private static final String QUIT_COMMAND_MSG = "Successfully quited from sort task command.";
    private static final String[] sort_option = {"Deadline / Task Start Date", "Task Name", "Creation DateTime"};

    private static final String LOG_TAG = "TaskListPanel";
    private final Logger logger = LogsCenter.getLogger(SortTaskInteractivePrompt.class);

    private int option;

    public SortTaskInteractivePrompt() {
        super();
        logger.info(LOG_TAG + ": Start of the sort task action.");
        this.interactivePromptType = SORT_TASK;
    }

    @Override
    public String interact(String userInput) {

        if ("quit".equalsIgnoreCase(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = "Please choose the sort keyword: \n"
                + "1. Deadline / Task Start Date\n"
                + "2. Task Name\n"
                + "3. Creation Date & Time";
            currentTerm = InteractivePromptTerms.SORT_KEYWORD;
            break;

        case SORT_KEYWORD:
            try {
                option = Integer.parseInt(userInput);
                if (option > 3 || option <= 0) {
                    throw new SortTaskCommandException("invalidOptionRangeError");
                }
                reply = "The task  will be sorted by " + sort_option[option - 1] + ". \n"
                    + "Please click enter again to check the sorted list.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (NumberFormatException ex) {
                logger.log(Level.WARNING, LOG_TAG + ": " + ex.getStackTrace());
                reply = (new DeleteTaskCommandException("wrongOptionFormatError")).getErrorMessage();
            } catch (SortTaskCommandException ex) {
                logger.log(Level.WARNING, LOG_TAG + ": " + ex.getStackTrace());
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                SortTaskCommand sortTaskCommand = new SortTaskCommand(sort_option[option - 1]);
                logic.executeCommand(sortTaskCommand);
                endInteract(END_OF_COMMAND_MSG);
            } catch (CommandException | ParseException ex) {
                logger.log(Level.WARNING, LOG_TAG + ": " + ex.getStackTrace());
                reply = ex.getMessage();
            }
            break;

        default:
        }
        assert !this.reply.isBlank()
            : "The reply of sort " + currentTerm.name() + " is blank, please check.\n";
        logger.info(LOG_TAG + ": End of the sort task interaction.");

        return reply;
    }

    @Override
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
        logger.info(LOG_TAG + ": End of the sort task action.");
    }

}
