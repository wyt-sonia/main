package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.model.task.Task.getCurrentTasks;
import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.EDIT_TASK;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.edit.EditTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.TaskParser;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskField;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.model.task.exceptions.DuplicateTaskException;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import javafx.collections.ObservableList;

/**
 * Interactive prompt for editing tasks
 *
 * @@author Hong Wen, Wang Yuting
 */
public class EditTaskInteractivePrompt extends InteractivePrompt {
    public static final String QUIT_COMMAND_MSG = "Successfully quit from the edit task command";
    private static final String END_OF_COMMAND_MSG = "Task edited successfully";

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


    private int taskNumber;
    private TaskField taskField;
    private String moduleListString = "";
    private ObservableList<Module> modules;

    public EditTaskInteractivePrompt() {
        super();
        this.modules = null;
        this.interactivePromptType = EDIT_TASK;
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
     * handles the sequence of commands for edit
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
     * Creates and executes an edit command, with the new values provided by the user
     *
     * @param userInput input from user
     * @return reply to user
     */
    public String handleNewValue(String userInput) {
        Index taskIndex = Index.fromZeroBased(taskNumber - 1);
        Task taskToEdit = getCurrentTasks().get(taskNumber - 1);
        EditTaskCommand editTaskCommand = new EditTaskCommand(taskIndex, taskField);
        boolean parseSuccess = true;
        String successMessage = END_OF_COMMAND_MSG;
        Task clone = null;
        try {
            clone = (Task) taskToEdit.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        switch (taskField) {
        case TASK_NAME:
            try {
                String newName = TaskParser.parseName(userInput);
                clone.setTaskName(newName);
                checkDuplicate(clone, taskToEdit);
                editTaskCommand.provideNewTaskName(newName);
            } catch (AddOrEditTaskCommandException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_NAME_MSG;
            } catch (DuplicateTaskException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_NAME_MSG;
            }
            break;

        case TASK_TYPE:
            try {
                TaskType newTaskType = TaskParser.parseType(userInput, TaskType.getTaskTypes().length);
                clone.setTaskType(newTaskType);
                checkDuplicate(clone, taskToEdit);
                editTaskCommand.provideNewTaskType(newTaskType);
                successMessage = "The type of task is successfully changed to: " + newTaskType + ".\n";
            } catch (NumberFormatException ex) {
                parseSuccess = false;
                reply = (new AddOrEditTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG + "\n"
                    + TaskType.getTypeString();
            } catch (AddOrEditTaskCommandException ex) {
                parseSuccess = false;
                reply = ex.getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG + "\n"
                    + TaskType.getTypeString();
            } catch (DuplicateTaskException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_TYPE_MSG;
            }
            break;

        case TASK_DATETIME:
            try {
                if (userInput.isBlank()) {
                    throw new AddOrEditTaskCommandException("emptyInputError");
                }
                TaskType taskType = logic.getFilteredTaskList().get(taskIndex.getZeroBased()).getTaskType();
                LocalDateTime[] newDateTimes = TaskParser.parseDateTime(userInput, taskType);
                clone.setDateTimes(newDateTimes);
                checkDuplicate(clone, taskToEdit);
                if (newDateTimes.length == 1) {
                    userInput = TimeParser.getDateTimeString(newDateTimes[0]);
                } else {
                    userInput = TimeParser.getDateTimeString(newDateTimes[0])
                        + "-" + TimeParser.getDateTimeString(newDateTimes[1]);
                }
                editTaskCommand.provideNewDateTime(newDateTimes);
            } catch (AddOrEditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_DATE_TIME_MSG;
            } catch (DuplicateTaskException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n" + REQUIRED_DATE_TIME_MSG;
            }
            break;

        case TASK_MODULE:
            try {
                if (userInput.isBlank()) {
                    editTaskCommand.provideNewModule(new EmptyModule());
                } else {
                    Module newModule = TaskParser.parseModule(userInput, modules);
                    double taskWeight = logic.getFilteredTaskList().get(taskIndex.getZeroBased()).getWeight();
                    boolean isWeightSizeValid = isWeightSizeValid(taskWeight, newModule,
                        logic.getStudyBuddy().getTaskList().get(taskNumber - 1));
                    if (!isWeightSizeValid) {
                        throw new AddOrEditTaskCommandException("moduleWeightOverloadError");
                    }
                    clone.setModule(newModule);
                    checkDuplicate(clone, taskToEdit);
                    editTaskCommand.provideNewModule(newModule);
                }
            } catch (AddOrEditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_MODULE_MSG + "\n" + moduleListString;
            } catch (DuplicateTaskException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_MODULE_MSG + "\n" + moduleListString;
            }
            break;

        case TASK_ESTIMATED_TIME_COST:
            try {
                if (!userInput.isBlank()) {
                    double newTimeCost = Double.parseDouble(userInput);
                    clone.setEstimatedTimeCost(newTimeCost);
                    checkDuplicate(clone, taskToEdit);
                    if (newTimeCost < 0) {
                        throw new AddOrEditTaskCommandException("wrongEstimatedTimeRangeError");
                    }
                    editTaskCommand.provideNewTaskTimeCost(newTimeCost);
                } else {
                    editTaskCommand.provideNewTaskTimeCost(0);
                }
            } catch (NumberFormatException e) {
                parseSuccess = false;
                this.reply = new AddOrEditTaskCommandException("wrongEstimatedTimeFormatError").getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            } catch (AddOrEditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            } catch (DuplicateTaskException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            }
            break;

        case TASK_WEIGHT:
            try {
                double newWeight = 0;
                boolean isWeightSizeValid = true;
                if (!userInput.isBlank()) {
                    newWeight = TaskParser.parseWeight(userInput);
                    if (newWeight > 0) {
                        isWeightSizeValid = isWeightSizeValid(newWeight, logic.getFilteredTaskList()
                                .get(taskIndex.getZeroBased()).getModule(),
                            logic.getStudyBuddy().getTaskList().get(taskNumber - 1));
                    }
                    if (!isWeightSizeValid) {
                        throw new AddOrEditTaskCommandException("moduleWeightOverloadError");
                    }
                    clone.setWeight(newWeight);
                    checkDuplicate(clone, taskToEdit);
                }
                editTaskCommand.provideNewTaskWeight(newWeight);
            } catch (AddOrEditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_WEIGHT_MSG;
            } catch (DuplicateTaskException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_WEIGHT_MSG;
            }
            break;

        case TASK_DESCRIPTION:
            try {
                String newDescription = TaskParser.parseDescription(userInput);
                clone.setTaskDescription(newDescription);
                checkDuplicate(clone, taskToEdit);
                editTaskCommand.provideNewTaskDescription(newDescription);
            } catch (AddOrEditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_DESCRIPTION_MSG;
            } catch (DuplicateTaskException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_DESCRIPTION_MSG;
            }
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + taskField);
        }

        if (parseSuccess) {
            try {
                logic.executeCommand(editTaskCommand);
                endInteract(successMessage);
            } catch (java.text.ParseException | CommandException | DuplicateTaskException ex) {
                reply = ex.getMessage();
            }
        }
        assert this.reply.isBlank()
            : "The reply of edit task's "+ currentTerm.name() +" is blank, please check.\n";
        return reply;
    }

    /**
     * Checks the total weight of tasks under targetModule.
     *
     * @param toBeAddWeight
     * @param targetModule
     * @return true if the total weight is not larger than 100.
     */
    private boolean isWeightSizeValid(double toBeAddWeight, Module targetModule, Task task) {
        boolean isValid = true;
        double moduleWeightSum = logic.getStudyBuddy().getTaskList()
            .stream()
            .filter(t -> (t.getModule().equals(targetModule) && !t.equals(task)))
            .mapToDouble(Task::getWeight).sum();
        double moduleWeightSumArchived = logic.getStudyBuddy().getArchivedList()
            .stream()
            .filter(t -> t.getModule().equals(targetModule))
            .mapToDouble(Task::getWeight).sum();
        if (moduleWeightSum + moduleWeightSumArchived + toBeAddWeight > 100) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Parses task number.
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
        } catch (AddOrEditTaskCommandException ex) {
            this.reply = ex.getErrorMessage();
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
        } catch (AddOrEditTaskCommandException e) {
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_FIELD_MSG + taskName + ".\n\n"
                + TaskField.getFieldString();
        }

        return taskField;
    }

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

