package draganddrop.studybuddy.ui.interactiveprompt.view;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.HELP;

import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;

/**
 * The interactive prompt for the help command. Returns the help message immediately
 */
public class HelpInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG1 =
        "Here are the list of available commands:" + System.lineSeparator()
            + "1. add  2. delete  3. edit  4. bye  5. sort  6. find  7. done  8. delete duplicates  "
            + "9. sort  10. archive  11. help  12. list  13. clear 14. create mods 15. refresh"
            + System.lineSeparator() + System.lineSeparator()
            + "User Guide: https://ay1920s2-cs2103t-w16-3.github.io/main/UserGuide.html";

    private static final String END_OF_COMMAND_MSG = "1. add  2. edit  3. done  4. clear  5. delete"
            + "  6. delete duplicates  7. archive  8. unarchive  9. sort  10. find  11. list  12. refresh  13.exit"
            + System.lineSeparator() + System.lineSeparator()
            + "For other commands, please visit my "
            + "User Guide: https://ay1920s2-cs2103t-w16-3.github.io/main/UserGuide.html";

    public HelpInteractivePrompt() {
        super();
        this.interactivePromptType = HELP;
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

}

