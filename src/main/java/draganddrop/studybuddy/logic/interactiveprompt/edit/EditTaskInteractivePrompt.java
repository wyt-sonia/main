package draganddrop.studybuddy.logic.interactiveprompt.edit;

import static draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptType.EDIT_TASK;
import static draganddrop.studybuddy.model.task.Task.getCurrentTasks;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.edit.EditTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.logic.interactiveprompt.InteractivePromptTerms;
import draganddrop.studybuddy.logic.parser.TaskParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskField;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.model.task.exceptions.DuplicateTaskException;
import javafx.collections.ObservableList;

/**
 * Interactive prompt for editing tasks
 */
public class EditTaskInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quit from the edit task command";

    private static final String LOG_TAG = "TaskParser";
    private static final Logger logger = LogsCenter.getLogger(EditTaskInteractivePrompt.class);

    private static final String REQUIRED_TASK_FIELD_MSG = "Please choose the field that you wish to edit for task: ";
    private static final String REQUIRED_MODULE_MSG = "Please choose a Module for this task or press enter to skip. "
        + "\nIndex number and module code are both acceptable.\n";
    private static final String REQUIRED_TASK_NAME_MSG = "Please enter the task name.";
    private static final String REQUIRED_TASK_TYPE_MSG = "Please choose the task type:\n";
    private static final String REQUIRED_DATE_TIME_MSG = "Please enter the deadline/duration with format: " + "\n\n"
        + "Assignment: HH:mm dd/MM/yyyy  e.g. 12:00 01/05/2020"
        + "\nRest: HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy  e.g. 12:00 01/05/2020-14:00 01/05/2020";
    private static final String REQUIRED_TASK_DESCRIPTION_MSG = "Please enter task "
        + "description or press enter to skip.\n";
    private static final String REQUIRED_TASK_WEIGHT_MSG = "Please enter the weight of the task "
        + "or press enter to skip.\n";
    private static final String REQUIRED_TASK_ESTIMATED_TIME_COST_MSG = "Please enter the "
        + "estimated number of hours cost or press enter to skip.\n";

    private static final String SUCCESS_EDIT_MSG = "Task edited successfully";


    private int taskNumber;
    private TaskField taskField;
    private String moduleListString = "";
    private ObservableList<Module> modules;

    public EditTaskInteractivePrompt() {
        super();
        this.modules = null;
        this.interactivePromptType = EDIT_TASK;
        logger.log(Level.INFO, LOG_TAG + ": Start of an edit task action.");
    }

    @Override
    public String interact(String userInput) {
        if (isQuit(userInput)) {
            this.reply = handleQuit(userInput, QUIT_COMMAND_MSG);
        } else {
            this.reply = handleEdit(userInput);
        }
        return this.reply;
    }

    /**
     * Handles the sequence of commands for task editing.
     *
     * @param userInput input from user
     * @return reply to user
     */
    public String handleEdit(String userInput) {
        switch (currentTerm) {
        case INIT:
            this.reply = "Please enter the index of the task that you wish to edit.";
            this.modules = logic.getFilteredModuleList();
            this.currentTerm = InteractivePromptTerms.TASK_NUMBER;
            break;
        case TASK_NUMBER:
            this.taskNumber = parseTaskNumber(userInput);
            break;
        case TASK_FIELD:
            this.taskField = parseTaskFieldNumber(userInput);
            break;
        case NEW_VALUE:
            this.reply = handleNewValue(userInput);
            break;
        default:
            break;
        }
        return this.reply;
    }

    /**
     * Creates and executes an edit command, with the new values provided by {@code userInput}.
     *
     * @param userInput input from user
     * @return reply to user
     */
    public String handleNewValue(String userInput) {
        Index taskIndex = Index.fromZeroBased(taskNumber - 1);
        Task taskToEdit = logic.getStudyBuddy().getTaskList().get(taskNumber - 1);
        EditTaskCommand editTaskCommand = new EditTaskCommand(taskIndex, taskField);
        boolean isParseSuccess;
        Task clone = null;

        try {
            clone = (Task) taskToEdit.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        switch (taskField) {
        case TASK_NAME:
            isParseSuccess = isHandleNewTaskNameSuccess(editTaskCommand, clone, userInput);
            break;

        case TASK_TYPE:
            isParseSuccess = isHandleNewTaskTypeSuccess(editTaskCommand, clone, userInput);
            break;

        case TASK_DATETIME:
            isParseSuccess = isHandleNewTaskDateTimeSuccess(editTaskCommand, clone, userInput, taskIndex);
            break;

        case TASK_MODULE:
            isParseSuccess = isHandleNewTaskModuleSuccess(editTaskCommand, clone, userInput, taskIndex);
            break;

        case TASK_ESTIMATED_TIME_COST:
            isParseSuccess = isHandleNewTaskEstimatedTimeCostSuccess(editTaskCommand, clone, userInput);
            break;

        case TASK_WEIGHT:
            isParseSuccess = isHandleNewTaskWeightSuccess(editTaskCommand, clone, userInput, taskIndex);
            break;

        case TASK_DESCRIPTION:
            isParseSuccess = isHandleNewTaskDescriptionSuccess(editTaskCommand, clone, userInput);
            break;

        default:
            throw new IllegalStateException("Unexpected value: " + taskField);
        }

        if (isParseSuccess) {
            try {
                logic.executeCommand(editTaskCommand);
                logger.log(Level.INFO, LOG_TAG + ": End of an edit task action.");
                this.reply = SUCCESS_EDIT_MSG;
                endInteract(this.reply);
            } catch (java.text.ParseException | CommandException | DuplicateTaskException ex) {
                logger.log(Level.WARNING, LOG_TAG + ": " + ex.getMessage());
                reply = ex.getMessage();
            }
        }

        assert !this.reply.isBlank()
            : "The reply of edit task's " + currentTerm.name() + " is blank, please check.\n";
        return reply;
    }

    /**
     * Handles the new task name.
     * <p>
     * Parses the {@code userInput} to the new task name.
     * Display error message for invalid {@code userInput}.
     *
     * @param editTaskCommand
     * @param taskToEdit
     * @param userInput
     * @return true if the task name is valid
     */
    private boolean isHandleNewTaskNameSuccess(EditTaskCommand editTaskCommand, Task taskToEdit, String userInput) {
        boolean isParseSuccess = true;

        try {
            String newName = TaskParser.parseName(userInput);

            taskToEdit.setTaskName(newName);
            checkDuplicate(taskToEdit, taskToEdit);
            editTaskCommand.provideNewTaskName(newName);

        } catch (AddOrEditTaskCommandException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n"
                + REQUIRED_TASK_NAME_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getErrorMessage());
        } catch (DuplicateTaskException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n"
                + REQUIRED_TASK_NAME_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getErrorMessage());
        }

        return isParseSuccess;
    }

    /**
     * Handles the new task type.
     * <p>
     * Parses the {@code userInput} to the new task type.
     * Display error message for invalid {@code userInput}.
     *
     * @param editTaskCommand
     * @param taskToEdit
     * @param userInput
     * @return true if the task name is valid
     */
    private boolean isHandleNewTaskTypeSuccess(EditTaskCommand editTaskCommand, Task taskToEdit, String userInput) {
        boolean isParseSuccess = true;

        try {
            TaskType newTaskType = TaskParser.parseType(userInput, TaskType.getTaskTypes().length);

            if (taskToEdit.getTaskType().equals(TaskType.Assignment) && !newTaskType.equals(TaskType.Assignment)) {
                throw new AddOrEditTaskCommandException("changeAssignmentToOtherTaskTypeError");
            }

            if (!taskToEdit.getTaskType().equals(TaskType.Assignment) && newTaskType.equals(TaskType.Assignment)) {
                throw new AddOrEditTaskCommandException("changeOtherTaskTypeToAssignmentError");
            }

            taskToEdit.setTaskType(newTaskType);
            checkDuplicate(taskToEdit, taskToEdit);
            editTaskCommand.provideNewTaskType(newTaskType);
            this.reply = "The type of task is successfully changed to: " + newTaskType + ".\n";

        } catch (NumberFormatException ex) {
            isParseSuccess = false;
            reply = (new AddOrEditTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                + "\n\n" + REQUIRED_TASK_TYPE_MSG + "\n"
                + TaskType.getTypeString();
            logger.log(Level.WARNING, LOG_TAG + ": " + ex.getMessage());
        } catch (AddOrEditTaskCommandException ex) {
            isParseSuccess = false;
            reply = ex.getErrorMessage()
                + "\n\n" + REQUIRED_TASK_TYPE_MSG + "\n"
                + TaskType.getTypeString();
            logger.log(Level.WARNING, LOG_TAG + ": " + ex.getStackTrace());
        } catch (DuplicateTaskException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n"
                + REQUIRED_TASK_TYPE_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        }

        return isParseSuccess;
    }

    /**
     * Handles the new task date time.
     * <p>
     * Parses the {@code userInput} to the new task date time.
     * Display error message for invalid {@code userInput}.
     *
     * @param editTaskCommand
     * @param taskToEdit
     * @param userInput
     * @param taskIndex
     * @return true if the task name is valid
     */
    private boolean isHandleNewTaskDateTimeSuccess(EditTaskCommand editTaskCommand, Task taskToEdit, String userInput,
                                                   Index taskIndex) {
        boolean isParseSuccess = true;

        try {
            // Parses date time
            if (userInput.isBlank()) {
                throw new AddOrEditTaskCommandException("emptyInputError");
            }
            TaskType taskType = logic.getFilteredTaskList().get(taskIndex.getZeroBased()).getTaskType();
            LocalDateTime[] newDateTimes = TaskParser.parseDateTime(userInput, taskType);
            taskToEdit.setDateTimes(newDateTimes);
            checkDuplicate(taskToEdit, taskToEdit);

            editTaskCommand.provideNewDateTime(newDateTimes);

        } catch (AddOrEditTaskCommandException e) {
            isParseSuccess = false;
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_DATE_TIME_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (DuplicateTaskException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n" + REQUIRED_DATE_TIME_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        }

        return isParseSuccess;
    }

    /**
     * Handles the new task module.
     * <p>
     * Parses the {@code userInput} to the new module.
     * Display error message for invalid {@code userInput}.
     *
     * @param editTaskCommand
     * @param taskToEdit
     * @param userInput
     * @param taskIndex
     * @return true if the task name is valid
     */
    private boolean isHandleNewTaskModuleSuccess(EditTaskCommand editTaskCommand,
                                                 Task taskToEdit, String userInput, Index taskIndex) {
        boolean isParseSuccess = true;

        try {
            Module newModule;
            if (userInput.isBlank()) {
                newModule = new EmptyModule();
            } else {
                newModule = TaskParser.parseModule(userInput, modules);
            }

            // checks the weight of the target module.
            double taskWeight = logic.getFilteredTaskList().get(taskIndex.getZeroBased()).getWeight();
            boolean isWeightSizeValid = isWeightSizeValid(taskWeight, newModule,
                logic.getStudyBuddy().getTaskList().get(taskNumber - 1));
            if (!isWeightSizeValid) {
                throw new AddOrEditTaskCommandException("moduleWeightOverloadError");
            }

            taskToEdit.setModule(newModule);
            checkDuplicate(taskToEdit, taskToEdit);
            editTaskCommand.provideNewModule(newModule);

        } catch (AddOrEditTaskCommandException e) {
            isParseSuccess = false;
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_MODULE_MSG + "\n" + moduleListString;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (DuplicateTaskException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n"
                + REQUIRED_MODULE_MSG + "\n" + moduleListString;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        }

        return isParseSuccess;
    }

    /**
     * Handles the new task estimated time cost.
     * <p>
     * Parses the {@code userInput} to the new estimated time cost.
     * Display error message for invalid {@code userInput}.
     *
     * @param editTaskCommand
     * @param taskToEdit
     * @param userInput
     * @return true if the task name is valid
     */
    private boolean isHandleNewTaskEstimatedTimeCostSuccess(EditTaskCommand editTaskCommand,
                                                            Task taskToEdit, String userInput) {
        boolean isParseSuccess = true;

        try {
            if (!userInput.isBlank()) {
                double newTimeCost = TaskParser.parseTimeCost(userInput);
                editTaskCommand.provideNewTaskTimeCost(newTimeCost);
            } else {
                editTaskCommand.provideNewTaskTimeCost(0);
            }

        } catch (NumberFormatException e) {
            isParseSuccess = false;
            this.reply = new AddOrEditTaskCommandException("wrongEstimatedTimeFormatError").getErrorMessage()
                + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (AddOrEditTaskCommandException e) {
            isParseSuccess = false;
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (DuplicateTaskException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n"
                + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        }

        return isParseSuccess;
    }

    /**
     * Handles the new task weight.
     * <p>
     * Parses the {@code userInput} to the new weight.
     * Display error message for invalid {@code userInput}.
     *
     * @param editTaskCommand
     * @param taskToEdit
     * @param userInput
     * @param taskIndex
     * @return true if the task name is valid
     */
    private boolean isHandleNewTaskWeightSuccess(EditTaskCommand editTaskCommand, Task taskToEdit,
                                                 String userInput, Index taskIndex) {
        boolean isParseSuccess = true;

        try {
            double newWeight = 0;
            boolean isWeightSizeValid = true;

            // Checks if the updating of the weight will lead to weight overflow.
            if (!userInput.isBlank()) {
                newWeight = TaskParser.parseWeight(userInput);
                if (newWeight > 0) {
                    isWeightSizeValid = isWeightSizeValid(newWeight,
                        logic.getFilteredTaskList().get(taskIndex.getZeroBased()).getModule(),
                        logic.getStudyBuddy().getTaskList().get(taskNumber - 1));
                }
                if (!isWeightSizeValid) {
                    throw new AddOrEditTaskCommandException("moduleWeightOverloadError");
                }
                taskToEdit.setWeight(newWeight);
                checkDuplicate(taskToEdit, taskToEdit);
            }

            editTaskCommand.provideNewTaskWeight(newWeight);

        } catch (AddOrEditTaskCommandException e) {
            isParseSuccess = false;
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_WEIGHT_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (DuplicateTaskException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n"
                + REQUIRED_TASK_WEIGHT_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        }

        return isParseSuccess;
    }

    /**
     * Handles the new task description.
     * <p>
     * Parses the {@code userInput} to the new description.
     * Display error message for invalid {@code userInput}.
     *
     * @param editTaskCommand
     * @param taskToEdit
     * @param userInput
     * @return true if the task name is valid
     */
    private boolean isHandleNewTaskDescriptionSuccess(EditTaskCommand editTaskCommand,
                                                      Task taskToEdit, String userInput) {
        boolean isParseSuccess = true;

        try {
            String newDescription = TaskParser.parseDescription(userInput);
            taskToEdit.setTaskDescription(newDescription);
            checkDuplicate(taskToEdit, taskToEdit);

            editTaskCommand.provideNewTaskDescription(newDescription);

        } catch (AddOrEditTaskCommandException e) {
            isParseSuccess = false;
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_DESCRIPTION_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (DuplicateTaskException e) {
            isParseSuccess = false;
            reply = e.getErrorMessage() + "\n\n"
                + REQUIRED_TASK_DESCRIPTION_MSG;
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        }

        return isParseSuccess;
    }

    /**
     * Checks the total weight of tasks under {@code targetModule}.
     *
     * @param toBeAddWeight
     * @param targetModule
     * @return true if the total weight is not larger than 100.
     */
    private boolean isWeightSizeValid(double toBeAddWeight, Module targetModule, Task task) {
        boolean isValid = true;

        // Calculates the sum of the weights of unarchived tasks under targetModule.
        double moduleWeightSumForUnarchived = logic.getStudyBuddy().getTaskList()
            .stream()
            .filter(t -> (t.getModule().equals(targetModule) && !t.equals(task)))
            .mapToDouble(Task::getWeight).sum();

        // Calculates the sum of the weights of archived tasks under targetModule.
        double moduleWeightSumForArchived = logic.getStudyBuddy().getArchivedList()
            .stream()
            .filter(t -> t.getModule().equals(targetModule))
            .mapToDouble(Task::getWeight).sum();

        if (moduleWeightSumForUnarchived + moduleWeightSumForArchived + toBeAddWeight > 100) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Parses {@code userInput} to task number.
     *
     * @param userInput user input for task number
     * @return an int of task number
     */
    public int parseTaskNumber(String userInput) {
        int taskNum = -1;

        try {

            if (userInput.isBlank()) {
                throw new AddOrEditTaskCommandException("emptyInputError");
            }
            taskNum = Integer.parseInt(userInput);
            if (taskNum > logic.getFilteredTaskList().size() || taskNum < 1) {
                throw new AddOrEditTaskCommandException("invalidIndexRangeError");
            }
            String taskName = logic.getFilteredTaskList().get(taskNum - 1).getTaskName();

            this.reply = REQUIRED_TASK_FIELD_MSG + taskName + ".\n\n"
                + TaskField.getFieldString();
            this.currentTerm = InteractivePromptTerms.TASK_FIELD;

        } catch (NumberFormatException e) {
            this.reply = (new AddOrEditTaskCommandException("wrongIndexFormatError")).getErrorMessage();
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (AddOrEditTaskCommandException ex) {
            this.reply = ex.getErrorMessage();
            logger.log(Level.WARNING, LOG_TAG + ": " + ex.getStackTrace());
        }

        return taskNum;
    }

    /**
     * Checks if the task will be duplicate if edited.
     *
     * @param clone
     * @param taskToEdit
     */
    public void checkDuplicate(Task clone, Task taskToEdit) {
        if (getCurrentTasks().contains(clone)) {
            throw new DuplicateTaskException("duplicateTask");
        }
        if (taskToEdit.getDuplicate() != 0) {
            taskToEdit.zeroDuplicate();
        }
    }

    /**
     * Parses the task field.
     *
     * @param userInput userInput for task number
     * @return a TaskField
     */
    public TaskField parseTaskFieldNumber(String userInput) {
        String taskName = logic.getFilteredTaskList().get(taskNumber - 1).getTaskName();
        TaskField taskField = null;

        try {
            int taskFieldNumber = Integer.parseInt(userInput);
            if (taskFieldNumber > 7 || taskFieldNumber < 1) {
                throw new AddOrEditTaskCommandException("invalidIndexRangeError");
            }

            taskField = TaskField.getTaskFieldFromNumber(taskFieldNumber);
            assert (taskField != null);
            this.reply = getTaskFieldMessage(taskField);
            this.currentTerm = InteractivePromptTerms.NEW_VALUE;

        } catch (NumberFormatException e) {
            this.reply = new AddOrEditTaskCommandException("wrongIndexFormatError").getErrorMessage();
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        } catch (AddOrEditTaskCommandException e) {
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_FIELD_MSG + taskName + ".\n\n"
                + TaskField.getFieldString();
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getStackTrace());
        }

        return taskField;
    }

    /**
     * Gets task field message according to {@code taskField}.
     *
     * @param taskField
     * @return
     */
    private String getTaskFieldMessage(TaskField taskField) {
        String result = "You are now editing the " + taskField.getLabel() + " field\n";

        switch (taskField) {
        case TASK_MODULE:
            moduleListString = "The Modules available are: \n";
            constructModuleList(modules);
            result += REQUIRED_MODULE_MSG + "\n"
                + moduleListString;
            break;
        case TASK_NAME:
            result += REQUIRED_TASK_NAME_MSG;
            break;
        case TASK_TYPE:
            result += REQUIRED_TASK_TYPE_MSG + "\n"
                + TaskType.getTypeString();
            break;
        case TASK_DATETIME:
            result += REQUIRED_DATE_TIME_MSG;
            break;
        case TASK_DESCRIPTION:
            result += REQUIRED_TASK_DESCRIPTION_MSG;
            break;
        case TASK_WEIGHT:
            result += REQUIRED_TASK_WEIGHT_MSG;
            break;
        case TASK_ESTIMATED_TIME_COST:
            result += REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + taskField);
        }

        return result;
    }

    /**
     * Hides empty module from the moduleList.
     *
     * @param moduleList
     */
    private void constructModuleList(ObservableList<Module> moduleList) {
        AtomicInteger counter = new AtomicInteger();
        moduleList.forEach(m -> {
            if (!m.equals(new EmptyModule())) {
                counter.getAndAdd(1);
                moduleListString += counter + "." + m.getModuleCode() + " " + m.getModuleName() + "\n";
            }
        });
    }

}

