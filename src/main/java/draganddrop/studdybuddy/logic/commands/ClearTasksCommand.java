package draganddrop.studdybuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.StudyBuddy;

/**
 * Clears the address book.
 */
public class ClearTasksCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Study Buddy has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setStudyBuddy(new StudyBuddy());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
