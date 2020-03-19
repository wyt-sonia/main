package seedu.address.ui.interactiveprompt;

import static seedu.address.ui.interactiveprompt.InteractivePromptType.HELP;

/**
 * The interactive prompt for the help command. Returns the help message immediately
 */
public class HelpInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG =
        "Here are the list of available commands:" + System.lineSeparator()
            + "1. add  2. delete  3. edit  4. bye  5. sort  6. find  7. done  8. delete duplicates  "
            + "9. sort  10. archive  11. help" + System.lineSeparator() + System.lineSeparator()
            + "User Guide: https://ay1920s2-cs2103t-w16-3.github.io/main/UserGuide.html";

    private String reply;

    public HelpInteractivePrompt() {
        super();
        this.interactivePromptType = HELP;
        this.reply = "";
    }

    public static String getHelpMessage() {
        return END_OF_COMMAND_MSG;
    }

    /**
     * Returns the help message to the user immediately
     *
     * @param userInput the help keyword
     * @return the help message
     */
    @Override
    public String interact(String userInput) {
        endInteract(END_OF_COMMAND_MSG);
        return reply;
    }

    /**
     * Ends the interactive prompt
     *
     * @param msg the help message to be displayed to the user
     */
    @Override
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
    }

    /**
     * As this is a single line command, the methods below do not have any functionality
     */
    @Override
    public void interruptInteract() {
        // no possibility of interrupt
    }

    @Override
    public void back() {
        // no previous interaction
    }

    @Override
    public void next() {
        // no next interaction
    }
}

