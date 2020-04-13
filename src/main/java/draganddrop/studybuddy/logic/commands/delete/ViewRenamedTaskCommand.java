package draganddrop.studybuddy.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 * Filters all the renamed tasks in the list.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class ViewRenamedTaskCommand extends Command {

    public static final String COMMAND_WORD = "view renamed";

    private final Predicate<Task> predicate;

    public ViewRenamedTaskCommand(Predicate<Task> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTaskList(predicate);
        return new CommandResult(Messages.MESSAGE_VIEW_RENAMED_TASK_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ViewRenamedTaskCommand);
    }

}
