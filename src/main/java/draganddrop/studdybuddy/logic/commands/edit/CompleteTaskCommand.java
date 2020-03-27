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
 * Completes a task.
 */
public class CompleteTaskCommand extends Command {
    public static final String COMMAND_WORD = "Complete";
    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Complete the task based on the displayed list.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_COMPLETE_TASK_SUCCESS = "Task Completed: [task name]";

    private final Index targetIndex;


    public CompleteTaskCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToComplete = lastShownList.get(targetIndex.getZeroBased());
        model.completeTask(taskToComplete);
        model.deleteTask(taskToComplete);
        model.archiveTask(taskToComplete);

        return new CommandResult(String.format(MESSAGE_COMPLETE_TASK_SUCCESS, taskToComplete));
    }
}
