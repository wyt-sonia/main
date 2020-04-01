package draganddrop.studybuddy.logic.commands.edit;

import static java.util.Objects.requireNonNull;

import java.util.List;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 * Archives an entry in the study buddy.
 */
public class ArchiveTaskCommand extends Command {

    final public static String COMMAND_WORD = "archive";

    final public static String MESSAGE_USAGE = COMMAND_WORD + ": Archives the selected entry";

    final public static String MESSAGE_ARCHIVE_TASK_SUCCESS = "Archived Task: %1$s";

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
