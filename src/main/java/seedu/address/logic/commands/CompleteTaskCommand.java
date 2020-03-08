package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.task.Task;

import static java.util.Objects.requireNonNull;


/**
 * Completes a task
 */
public class CompleteTaskCommand extends Command {
    private Task toComplete;


    public CompleteTaskCommand(Task task) {
        requireNonNull(task);
        this.toComplete = task;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {

        return null;
    }
}
