package draganddrop.studybuddy.ui.interactiveprompt.view;

import java.text.ParseException;

import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.commands.view.ListTaskCommand;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType;

/**
 * InteractivePrompt to list the tasks. This is useful after a Find command.
 */
public class ListTaskInteractivePrompt extends InteractivePrompt {
    private static final String END_OF_COMMAND_MSG = "Here is the complete list of tasks:";

    public ListTaskInteractivePrompt() {
        super();
        this.interactivePromptType = InteractivePromptType.LIST_TASK;
    }

    @Override
    public String interact(String userInput) {
        displayList();
        endInteract(END_OF_COMMAND_MSG);
        return reply;
    }

    /**
     * displays the complete list of tasks
     */
    public void displayList() {
        try {
            ListTaskCommand listTaskCommand = new ListTaskCommand();
            logic.executeCommand(listTaskCommand);
        } catch (CommandException | ParseException ex) {
            // do nothing, this block should not be reached
        }
    }

}
