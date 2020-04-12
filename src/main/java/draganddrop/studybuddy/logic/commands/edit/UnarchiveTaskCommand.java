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
public class UnarchiveTaskCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": retrieves the selected entry from archived";

    public static final String MESSAGE_ARCHIVE_TASK_SUCCESS = "retrieved Task: %1$s";

    private final Index targetIndex;

    public UnarchiveTaskCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredArchivedTaskList();
        List<Task> lastShownMainList = model.getFilteredTaskList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToArchive = lastShownList.get(targetIndex.getZeroBased());
        if (lastShownMainList.contains(taskToArchive)) {
            for (Task task : lastShownList) {
                if (task.equals(taskToArchive)) {
                    model.addDuplicateTask(taskToArchive, task);
                    model.unarchiveDuplicateTask(taskToArchive);
                    break;
                }
            }
        } else {
            model.unarchiveTask(taskToArchive);
        }

        return new CommandResult(String.format(MESSAGE_ARCHIVE_TASK_SUCCESS, taskToArchive));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnarchiveTaskCommand // instanceof handles nulls
                && targetIndex.equals(((UnarchiveTaskCommand) other).targetIndex)); // state check
    }

}
