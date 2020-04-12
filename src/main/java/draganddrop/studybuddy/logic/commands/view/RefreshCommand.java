package draganddrop.studybuddy.logic.commands.view;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.StudyBuddy;
import draganddrop.studybuddy.model.task.Task;

/**
 * Refreshes the Due Soon list as well as status tags.
 */
public class RefreshCommand extends Command {

    //logging
    private final Logger logger = LogsCenter.getLogger(RefreshCommand.class);

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        logger.info("Attempting to refresh due soon list and tags");

        List<Task> lastShownList = model.getFilteredTaskList();

        //resetting the due soon list
        model.clearDueSoonList(new StudyBuddy());

        for (Task task : lastShownList) {
            if (task.isDueSoon()) {
                model.forceAddDueSoonTask(task);
            }
        }
        model.sortDueSoonTasks();

        //refreshing the status tags
        final int size = lastShownList.size();

        for (int i = 0; i < size; i++) {
            Task task = lastShownList.get(0);
            refreshStatus(task, model);
        }

        model.sortTasks("deadline / task start date");

        return new CommandResult(Messages.MESSAGE_DUE_SOON_TASK_SUCCESS);
    }

    /**
     * This function refreshes the status tags.
     * @param task a task
     * @param model Model
     */
    private void refreshStatus(Task task, Model model) {
        model.deleteTaskFromMainList(task);
        task.freshStatus();
        model.addTaskToMainList(task);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof RefreshCommand); // state check
    }

}
