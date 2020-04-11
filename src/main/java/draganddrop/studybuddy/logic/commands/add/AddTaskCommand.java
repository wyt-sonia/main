package draganddrop.studybuddy.logic.commands.add;

import static java.util.Objects.requireNonNull;

import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 * Represents the command of adding new task.
 * @@author Wang Yuting
 */
public class AddTaskCommand extends Command {

    public static final String COMMAND_WORD = "add";
    public static final String MESSAGE_SUCCESS = "New Task added: %1$s";
    private final Task toAdd;

    /**
     * Creates an AddTaskCommand to add the specified {@code Task}
     */
    public AddTaskCommand(Task task) {
        requireNonNull(task);
        this.toAdd = task;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.addTask(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof AddTaskCommand // instanceof handles nulls
            && toAdd.equals(((AddTaskCommand) other).toAdd));
    }

}
