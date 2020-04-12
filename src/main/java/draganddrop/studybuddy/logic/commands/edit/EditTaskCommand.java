package draganddrop.studybuddy.logic.commands.edit;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.module.exceptions.ModuleException;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskField;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.model.task.exceptions.DuplicateTaskException;


/**
 * Edits the details of an existing task in the study buddy.
 */
public class EditTaskCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String EDIT_TASK_SUCCESS = "Task has been edited successfully";
    private static final String LOG_TAG = "EditTaskCommand";
    private static final Logger logger = LogsCenter.getLogger(EditTaskCommand.class);
    private final Index targetIndex;
    private final TaskField taskField;
    private String newTaskName = null;
    private String newTaskDescription = "";
    private double newTaskWeight = 0.0;
    private double newTaskTimeCost = 0.0;
    private TaskType newTaskType = null;
    private LocalDateTime[] newDateTimes = null;
    private Module newModule = null;

    /**
     * Creates an edit task command.
     *
     * @param targetIndex index of target task to be edited
     * @param taskField the task field to be edited
     */
    public EditTaskCommand(Index targetIndex, TaskField taskField) {
        requireNonNull(targetIndex);
        requireNonNull(taskField);
        this.targetIndex = targetIndex;
        this.taskField = taskField;
    }

    public void provideNewTaskName(String newTaskName) {
        this.newTaskName = newTaskName;
    }
    public void provideNewTaskWeight(double newTaskWeight) {
        this.newTaskWeight = newTaskWeight;
    }
    public void provideNewTaskDescription(String newTaskDescription) {
        this.newTaskDescription = newTaskDescription;
    }
    public void provideNewTaskTimeCost(double newTaskTimeCost) {
        this.newTaskTimeCost = newTaskTimeCost;
    }
    public void provideNewTaskType(TaskType newTaskType) {
        this.newTaskType = newTaskType;
    }
    public void provideNewDateTime(LocalDateTime[] newDateTimes) {
        this.newDateTimes = newDateTimes;
    }
    public void provideNewModule(Module newModule) {
        this.newModule = newModule;
    }

    @Override
    public CommandResult execute(Model model) throws DuplicateTaskException, CommandException {
        logger.log(Level.INFO, LOG_TAG + ": Start to execute edit task command.");
        List<Task> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(targetIndex.getZeroBased());
        switch (taskField) {
        case TASK_NAME:
            requireNonNull(newTaskName);
            model.setTaskName(taskToEdit, newTaskName);
            assert taskToEdit.getTaskName().equals(newTaskName)
                : "The task name is not updated to the correct new value, please check.\n";
            break;
        case TASK_DESCRIPTION:
            requireNonNull(newTaskDescription);
            model.setTaskDescription(taskToEdit, newTaskDescription);
            assert taskToEdit.getTaskDescription().equals(newTaskDescription)
                : "The task description is not updated to the correct new value, please check.\n";
            break;
        case TASK_WEIGHT:
            requireNonNull(newTaskWeight);
            model.setTaskWeight(taskToEdit, newTaskWeight);
            assert taskToEdit.getWeight() == newTaskWeight
                : "The task weight is not updated to the correct new value, please check.\n";
            break;
        case TASK_ESTIMATED_TIME_COST:
            requireNonNull(newTaskTimeCost);
            model.setTaskTimeCost(taskToEdit, newTaskTimeCost);
            assert taskToEdit.getEstimatedTimeCost() == newTaskTimeCost
                : "The task estimated time needed is not updated to the correct new value, please check.\n";
            break;
        case TASK_TYPE:
            requireNonNull(newTaskType);
            model.setTaskType(taskToEdit, newTaskType);
            assert taskToEdit.getTaskType().equals(newTaskType)
                : "The task type is not updated to the correct new value, please check.\n";
            break;
        case TASK_DATETIME:
            requireNonNull(newDateTimes);
            model.setTaskDateTime(taskToEdit, newDateTimes);
            assert taskToEdit.getDateTimes().equals(newDateTimes)
                : "The task date time info is not updated to the correct new value, please check.\n";
            break;
        case TASK_MODULE:
            requireNonNull(newModule);
            try {
                model.setTaskMod(taskToEdit, newModule);
                assert taskToEdit.getModule().equals(newModule)
                    : "The task module is not updated to the correct new value, please check.\n";
            } catch (ModuleException ex) {
                logger.log(Level.INFO, LOG_TAG + ": module code invalid/does not exist!!!");
                throw new CommandException("module code invalid/does not exist!!!");
            }
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + taskField);
        }
        updateDueSoon(taskToEdit, model);

        logger.log(Level.INFO, LOG_TAG + ": End of executing edit task command.");
        return new CommandResult(EDIT_TASK_SUCCESS);
    }

    /**
     * Updates Due Soon list as well.
     *
     * @param taskToEdit
     * @param model
     */
    public void updateDueSoon(Task taskToEdit, Model model) {
        model.deleteDueSoonTask(taskToEdit);
        List<Task> lastShownList = model.getFilteredTaskList();
        model.addDueSoonTask(lastShownList.get(targetIndex.getZeroBased()));
    }
}
