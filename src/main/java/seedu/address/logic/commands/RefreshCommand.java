package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.model.Model;
import seedu.address.model.task.Task;

/**
 * Archives an entry in the address book.
 */
public class RefreshCommand extends Command {

    public static final String MESSAGE_USAGE = "Refreshes the tasks and update their status";
    public static final String MESSAGE_DUE_SOON_TASK_SUCCESS = "Tasks' status are updated and "
        + "tasks due soon are displayed";

    public RefreshCommand() {

    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        for (int i = 0; i < lastShownList.size(); i++) {
            Task task = lastShownList.get(i);
            boolean isStatusExpired = task.isStatusExpired();
            if (isStatusExpired) {
                Task temp = task;
                model.deleteTask(task);
                temp.freshStatus();
                model.addTask(temp);
                model.sortTasks("Creation DateTime");
            }
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
        model.sortDueSoonTasks();
        return new CommandResult(String.format(MESSAGE_DUE_SOON_TASK_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof RefreshCommand); // state check
    }

}
