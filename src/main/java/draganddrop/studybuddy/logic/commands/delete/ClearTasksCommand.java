package draganddrop.studybuddy.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.StudyBuddy;

/**
 * Clears all data in study buddy.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class ClearTasksCommand extends Command {

    public static final String COMMAND_WORD = "clear";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setStudyBuddy(new StudyBuddy());
        return new CommandResult(Messages.MESSAGE_CLEAR_SUCCESS);
    }
}
