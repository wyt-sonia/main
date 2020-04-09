package draganddrop.studybuddy.logic.commands.add;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 *
 */
public class AddDuplicateTaskCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a duplicate task to the task list. "
            + "Parameters: TaskName, TaskType, TaskDateTime";

    public static final String MESSAGE_SUCCESS = "New Duplicate Task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This Task already exists. Are you sure you want to proceed?";

    private final Task toAdd;

    /**
     * Creates an AddTaskCommand to add the specified {@code Task}
     */
    public AddDuplicateTaskCommand(Task task) {
        requireNonNull(task);
        this.toAdd = task;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.addDuplicateTask(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddTaskCommand // instanceof handles nulls
                && toAdd.equals(((AddDuplicateTaskCommand) other).toAdd));
    }

}
