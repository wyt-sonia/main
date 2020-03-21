package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Archives an entry in the address book.
 */
public class DueSoonRefreshCommand extends Command {

    public static final String MESSAGE_USAGE = "Refreshes the tasks that are due soon";
    public static final String MESSAGE_DUE_SOON_TASK_SUCCESS = "Tasks due soon are displayed";

    public DueSoonRefreshCommand() {

    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        for (int i = 0; i < lastShownList.size(); i++) {
            Task task = lastShownList.get(i);
            if (task.isDueSoon()) {
                if (model.getFilteredDueSoonTaskList().contains(task)) {
                    continue;
                } else {
                    model.addDueSoonTask(task);
                }
            } else {
                if (model.getFilteredDueSoonTaskList().contains(task)) {
                    model.deleteDueSoonTask(task);
                } else {
                    continue;
                }
            }
        }

        return new CommandResult(String.format(MESSAGE_DUE_SOON_TASK_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof DueSoonRefreshCommand); // state check
    }

}
