package draganddrop.studybuddy.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 * Deletes all repeated tasks from the list.
 */
public class ViewDuplicateTaskCommand extends Command {

    public static final String COMMAND_WORD = "view duplicates";

    private final Predicate<Task> predicate;

    public ViewDuplicateTaskCommand(Predicate<Task> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredTaskList(predicate);
        return new CommandResult(String.format(Messages.MESSAGE_VIEW_DUPLICATE_TASK_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ViewDuplicateTaskCommand);
    }

}
