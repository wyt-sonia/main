package draganddrop.studybuddy.logic.interactiveprompt;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.EXIT_TASK;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.ExitTaskCommandException;

/**
 * Represents a ExitTaskInteractivePrompt, which allows user to quit from each interactive stage.
 */
public class ExitTaskInteractivePrompt extends InteractivePrompt {

    public static final String QUIT_COMMAND_MSG = "Successfully quited from exit command.";
    public static final String CONFIRM_MSG = "Are you sure you want to quit?\n"
            + "Please press enter yes if you would like to exit the application, and no if you would like to go back.";
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

        if ("quit".equals(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {

        case INIT:
            try {
                reply = CONFIRM_MSG;
                currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (ExitTaskCommandException ex) {
                reply = ex.getErrorMessage();
            }
            break;

        case READY_TO_EXECUTE:
            if (userInput.equalsIgnoreCase("yes")) {
                super.setQuit(true);
                endInteract(END_OF_COMMAND_MSG);
            } else if (userInput.equalsIgnoreCase("no")) {
                endInteract(END_OF_COMMAND_NO_EXIT_MSG);
            } else {
                reply = (new ExitTaskCommandException("invalidInputError")).getErrorMessage()
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
