package draganddrop.studybuddy.ui.interactiveprompt.delete;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 */

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.delete.ClearTasksCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType;

/**
 * Represents a ClearTasksInteractivePrompt which interact with user to clear all tasks.
 */
public class ClearTasksInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully cleared all tasks.";
    private static final String END_OF_COMMAND_MSG = "Tasks cleared successfully!";

    public ClearTasksInteractivePrompt() {
        super();
        this.interactivePromptType = InteractivePromptType.CLEAR_TASK;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equals(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = "Please press enter to clear all your tasks.\n"
                + "Else enter quit to go back.";
            currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            break;

        case READY_TO_EXECUTE:
            try {
                ClearTasksCommand clearTaskCommand = new ClearTasksCommand();
                logic.executeCommand(clearTaskCommand);
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
