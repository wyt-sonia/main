package draganddrop.studybuddy.ui.interactiveprompt.delete;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.delete.ClearTasksCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.ClearTasksCommandException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType;

/**
 * Represents a ClearTasksInteractivePrompt which interact with user to clear all tasks.
 */
public class ClearTasksInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quit from clear command.";
    private static final String END_OF_COMMAND_MSG = "Tasks cleared successfully!";
    private static final String ENTER_NO_MSG = "Tasks will not be cleared! Key in your next command :)";
    private static final String CONFIRM_MSG = "Are you sure you want to clear all your tasks?\n"
            + "Please enter yes to continue and no to go back.";

    public ClearTasksInteractivePrompt() {
        super();
        this.interactivePromptType = InteractivePromptType.CLEAR_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equalsIgnoreCase(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = CONFIRM_MSG;
            currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            break;

        case READY_TO_EXECUTE:
            if (userInput.equalsIgnoreCase("yes")) {
                try {
                    ClearTasksCommand clearTaskCommand = new ClearTasksCommand();
                    logic.executeCommand(clearTaskCommand);
                    endInteract(END_OF_COMMAND_MSG);
                } catch (CommandException | ParseException e) {
                    e.printStackTrace();
                }
            } else if (userInput.equalsIgnoreCase("no")) {
                endInteract(ENTER_NO_MSG);
            } else {
                reply = (new ClearTasksCommandException("invalidInputError")).getErrorMessage()
                        + "\n\n" + CONFIRM_MSG;
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
