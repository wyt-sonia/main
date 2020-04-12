package draganddrop.studybuddy.logic.commands.add;

import static java.util.Objects.requireNonNull;

import java.util.List;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.task.Task;

/**
 * Adds a duplicate task to the list.
 */
public class AddDuplicateTaskCommand extends Command {

    public static final String COMMAND_WORD = "add";

    private final Task toAdd;

    /**
     * Creates an AddDuplicateTaskCommand to add the specified {@code Task}
     */
    public AddDuplicateTaskCommand(Task task) {
        requireNonNull(task);
        this.toAdd = task;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        List<Task> lastShownList = model.getFilteredTaskList();

        for (Task task : lastShownList) {
            if (task.equals(toAdd)) {
                model.addDuplicateTask(toAdd, task);
                break;
            }
        }

        return new CommandResult(String.format(Messages.MESSAGE_DUPLICATE_ADD_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddDuplicateTaskCommand // instanceof handles nulls
                && toAdd.equals(((AddDuplicateTaskCommand) other).toAdd));
    }

}
