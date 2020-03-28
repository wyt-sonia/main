package draganddrop.studdybuddy.ui.interactiveprompt;

/*
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 */

import static draganddrop.studdybuddy.ui.interactiveprompt.InteractivePromptType.EXIT_TASK;

import java.util.ArrayList;

import draganddrop.studdybuddy.logic.parser.interactivecommandparser.exceptions.ExitTaskCommandException;

/**
 * pending.
 */
public class ExitTaskInteractivePrompt extends InteractivePrompt {

    static final String END_OF_COMMAND_NO_EXIT_MSG = "Thank you for staying!";
    static final String END_OF_COMMAND_MSG = "Exit successfully!";

    private String reply;
    private InteractivePromptTerms currentTerm;
    private InteractivePromptTerms lastTerm;
    private ArrayList<InteractivePromptTerms> terms;


    public ExitTaskInteractivePrompt() {
        super();
        this.interactivePromptType = EXIT_TASK;
        this.reply = "";
        this.currentTerm = InteractivePromptTerms.INIT;
        this.lastTerm = null;
        this.terms = new ArrayList<>();
    }

    @Override
    public String interact(String userInput) {
        if (userInput.equals("back")) {
            if (lastTerm != null) { //in the beginning it is null
                terms.remove(terms.size() - 1);
                currentTerm = lastTerm;
                if (lastTerm.equals(InteractivePromptTerms.INIT)) {
                    lastTerm = null;
                } else {
                    lastTerm = terms.get(terms.size() - 1);
                }
                userInput = "";
            } else {
                this.reply = "Please type quit to exit from this command.";
            }
        }

        switch (currentTerm) {

        case INIT:
            try {
                reply = "Are you sure you want to quit?\n "
                    + "Please press enter yes if you would like to close the application.";
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
