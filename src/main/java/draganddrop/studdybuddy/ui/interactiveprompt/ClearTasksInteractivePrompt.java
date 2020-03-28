<<<<<<< HEAD:src/main/java/draganddrop/studdybuddy/ui/interactiveprompt/delete/ClearTasksInteractivePrompt.java
package seedu.address.ui.interactiveprompt.delete;
=======
package draganddrop.studdybuddy.ui.interactiveprompt;
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/main/java/draganddrop/studdybuddy/ui/interactiveprompt/ClearTasksInteractivePrompt.java

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 * */

import java.text.ParseException;

<<<<<<< HEAD:src/main/java/draganddrop/studdybuddy/ui/interactiveprompt/delete/ClearTasksInteractivePrompt.java
import seedu.address.logic.commands.delete.ClearTasksCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.ui.interactiveprompt.InteractivePrompt;
import seedu.address.ui.interactiveprompt.InteractivePromptTerms;
=======
import draganddrop.studdybuddy.logic.commands.ClearTasksCommand;
import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/main/java/draganddrop/studdybuddy/ui/interactiveprompt/ClearTasksInteractivePrompt.java

/**
 * pending.
 */
public class ClearTasksInteractivePrompt extends InteractivePrompt {
    static final String END_OF_COMMAND_MSG = "Tasks cleared successfully!";
    static final String QUIT_COMMAND_MSG = "Successfully cleared all tasks.";

    public ClearTasksInteractivePrompt() {
        super();
<<<<<<< HEAD:src/main/java/draganddrop/studdybuddy/ui/interactiveprompt/delete/ClearTasksInteractivePrompt.java
        this.interactivePromptType = CLEAR_TASK;
=======
        this.interactivePromptType = InteractivePromptType.CLEAR_TASK;
        this.reply = "";
        this.userInput = "";
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/main/java/draganddrop/studdybuddy/ui/interactiveprompt/ClearTasksInteractivePrompt.java
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
            this.reply = "Please press enter to clear all your tasks.\n"
                    + " Else enter quit to go back.";
            currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            lastTerm = InteractivePromptTerms.INIT;
            terms.add(lastTerm);
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
