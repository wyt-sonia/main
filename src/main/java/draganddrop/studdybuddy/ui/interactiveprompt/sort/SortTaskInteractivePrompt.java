package draganddrop.studdybuddy.ui.interactiveprompt.sort;

import static draganddrop.studdybuddy.ui.interactiveprompt.InteractivePromptType.SORT_TASK;

import java.text.ParseException;

import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studdybuddy.logic.commands.sort.SortTaskCommand;
import draganddrop.studdybuddy.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;
import draganddrop.studdybuddy.logic.parser.interactivecommandparser.exceptions.SortTaskCommandException;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studdybuddy.ui.interactiveprompt.InteractivePromptTerms;

/**
 * A interactive prompt for sorting task list.
 */
public class SortTaskInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG = "Task sorted successfully!";
    private static final String QUIT_COMMAND_MSG = "Successfully quited from sort task command.";
    private static final String[] sort_option = {"Deadline / Task Start Date", "Task Name", "Creation DateTime"};

    private int option;
    private String userInput;

    public SortTaskInteractivePrompt() {
        super();
        this.interactivePromptType = SORT_TASK;
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
            this.reply = "Please choose the sort keyword: \n"
                + "1. Deadline / Task Start Date\n"
                + "2. Task Name\n"
                + "3. Creation Date & Time";
            currentTerm = InteractivePromptTerms.SORT_KEYWORD;
            terms.add(lastTerm);
            break;

        case SORT_KEYWORD:
            try {
                option = Integer.parseInt(userInput);
                if (option > 3 || option <= 0) {
                    throw new SortTaskCommandException("invalidOptionRangeError");
                }
                reply = "The task  will be sorted by " + sort_option[option - 1] + ". \n "
                    + " Please click enter again to check the sorted list.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (NumberFormatException ex) {
                reply = (new DeleteTaskCommandException("wrongOptionFormatError")).getErrorMessage();
            } catch (SortTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            try {
                SortTaskCommand sortTaskCommand = new SortTaskCommand(sort_option[option - 1]);
                logic.executeCommand(sortTaskCommand);
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
