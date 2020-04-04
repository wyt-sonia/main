package draganddrop.studybuddy.ui.interactiveprompt;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.EXIT_TASK;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.ExitTaskCommandException;

/**
 * pending.
 */
public class ExitTaskInteractivePrompt extends InteractivePrompt {

    private static final String END_OF_COMMAND_NO_EXIT_MSG = "Thank you for staying!";
    private static final String END_OF_COMMAND_MSG = "Exit successfully!";

    private String reply;
    private InteractivePromptTerms currentTerm;


    public ExitTaskInteractivePrompt() {
        super();
        this.interactivePromptType = EXIT_TASK;
        this.reply = "";
        this.currentTerm = InteractivePromptTerms.INIT;
    }

    @Override
    public String interact(String userInput) {

        switch (currentTerm) {

        case INIT:
            try {
                reply = "Are you sure you want to quit?\n"
                    + "Please press enter if you would like to close the application.";
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (ExitTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            if (userInput.equalsIgnoreCase("yes")) {
                super.setQuit(true);
                endInteract(END_OF_COMMAND_MSG);
            } else {
                endInteract(END_OF_COMMAND_NO_EXIT_MSG);
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
