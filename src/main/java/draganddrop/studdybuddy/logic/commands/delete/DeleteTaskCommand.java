package draganddrop.studdybuddy.logic.commands.delete;

import static java.util.Objects.requireNonNull;

import java.util.List;

import draganddrop.studdybuddy.commons.core.Messages;
import draganddrop.studdybuddy.commons.core.index.Index;
import draganddrop.studdybuddy.logic.commands.Command;
import draganddrop.studdybuddy.logic.commands.CommandResult;
import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.task.Task;

/**
 * Deletes a task identified using it's displayed index from the list.
 */
public class DeleteTaskCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the task identified by the index number used in the displayed task list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: %1$s";

    private final Index targetIndex;

    public DeleteTaskCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteTask(taskToDelete);

        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DeleteTaskCommand // instanceof handles nulls
            && targetIndex.equals(((DeleteTaskCommand) other).targetIndex)); // state check
    }
}
