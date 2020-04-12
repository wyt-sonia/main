package draganddrop.studybuddy.logic.interactiveprompt.view;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.HELP;

import draganddrop.studybuddy.logic.interactiveprompt.InteractivePrompt;

/**
 * The interactive prompt for the help command. Returns the help message immediately
 */
public class HelpInteractivePrompt extends InteractivePrompt {

    private static final String END_OF_COMMAND_MSG =
        "Here is the list of available commands:\n"
                + "1. add\n2. archive\n3. bye\n4. clear\n5. create mods\n6. delete\n"
                + "7. view renamed\n8. done\n9. edit\n10. filter\n11. find\n"
                + "12. help\n13. list\n14. sort\n15. refresh\n16. calendar\n17. goal\n\n"
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

