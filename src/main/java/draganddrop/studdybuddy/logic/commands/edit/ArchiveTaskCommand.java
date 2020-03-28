package draganddrop.studdybuddy.logic.commands.edit;

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
 * Archives an entry in the address book.
 */
public class ArchiveTaskCommand extends Command {

    public static final String COMMAND_WORD = "archive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Archives the selected entry";

    public static final String MESSAGE_ARCHIVE_TASK_SUCCESS = "Archived Task: %1$s";

    private final Index targetIndex;

    public ArchiveTaskCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToArchive = lastShownList.get(targetIndex.getZeroBased());
        model.deleteTask(taskToArchive);
        model.archiveTask(taskToArchive);

        return new CommandResult(String.format(MESSAGE_ARCHIVE_TASK_SUCCESS, taskToArchive));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ArchiveTaskCommand // instanceof handles nulls
            && targetIndex.equals(((ArchiveTaskCommand) other).targetIndex)); // state check
    }

}
