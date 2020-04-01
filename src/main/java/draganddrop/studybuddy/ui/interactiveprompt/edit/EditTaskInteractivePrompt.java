package draganddrop.studybuddy.ui.interactiveprompt.edit;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.EDIT_TASK;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import draganddrop.studybuddy.commons.core.index.Index;
import draganddrop.studybuddy.logic.commands.edit.EditTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.exceptions.ParseException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.EditTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.EditTaskCommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskField;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import javafx.collections.ObservableList;

/**
 * Interactive prompt for editing tasks
 */
public class EditTaskInteractivePrompt extends InteractivePrompt {
    static final String END_OF_COMMAND_MSG = "Task edited successfully";
    static final String QUIT_COMMAND_MSG = "Successfully quit from the edit task command";

    static final String REQUIRED_MODULE_MSG = "Please choose a Module for this task or press enter to skip. "
        + "Index number and module code are both acceptable.\n";
    static final String REQUIRED_TASK_NAME_MSG = "Please enter the task name.";
    static final String REQUIRED_TASK_TYPE_MSG = "Please choose the task type:\n";
    static final String REQUIRED_DATE_TIME_MSG = "Please enter the deadline/duration with format: ";
    static final String REQUIRED_TASK_DESCRIPTION_MSG = "Please enter task description or press enter to skip.\n";
    static final String REQUIRED_TASK_WEIGHT_MSG = "Please enter the weight of the task or press enter to skip.\n";
    static final String REQUIRED_TASK_ESTIMATED_TIME_COST_MSG = "Please enter the estimated number of hours cost "
        + "or press enter to skip.\n";


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
        if (isQuitOrBack(userInput)) {
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
        if (userInput.equals("quit")) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

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
        EditTaskCommand editTaskCommand = new EditTaskCommand(taskIndex, taskField);
        boolean parseSuccess = true;
        String successMessage = END_OF_COMMAND_MSG;

        switch (taskField) {

        case TASK_NAME:
            try {
                String newName = EditTaskCommandParser.parseName(userInput);
                editTaskCommand.provideNewTaskName(newName);
            } catch (EditTaskCommandException e) {
                parseSuccess = false;
                reply = e.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_NAME_MSG;
            }
            break;

        case TASK_TYPE:
            try {
                TaskType newTaskType = EditTaskCommandParser.parseType(userInput, TaskType.getTaskTypes().length);
                editTaskCommand.provideNewTaskType(newTaskType);
                successMessage = "The type of task is successfully changed to: " + newTaskType + ".\n";
            } catch (NumberFormatException ex) {
                parseSuccess = false;
                reply = (new EditTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG + "\n"
                    + TaskType.getTypeString();
            } catch (EditTaskCommandException ex) {
                parseSuccess = false;
                reply = ex.getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG + "\n"
                    + TaskType.getTypeString();
            }
            break;

        case TASK_DATETIME:
            try {
                TaskType taskType = logic.getFilteredTaskList().get(taskIndex.getZeroBased()).getTaskType();
                LocalDateTime[] newDateTimes = EditTaskCommandParser.parseDateTime(userInput, taskType);

                if (newDateTimes.length == 1) {
                    userInput = TimeParser.getDateTimeString(newDateTimes[0]);
                } else {
                    userInput = TimeParser.getDateTimeString(newDateTimes[0])
                        + "-" + TimeParser.getDateTimeString(newDateTimes[1]);
                }
                editTaskCommand.provideNewDateTime(newDateTimes);
            } catch (EditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage();
            }
            break;

        case TASK_MODULE:
            try {
                if (userInput.isBlank()) {
                    editTaskCommand.provideNewModule(new EmptyModule());
                } else {
                    Module newModule = EditTaskCommandParser.parseModule(userInput, modules);
                    double taskWeight = logic.getFilteredTaskList().get(taskIndex.getZeroBased()).getWeight();
                    boolean isWeightSizeValid = isWeightSizeValid(taskWeight, newModule);
                    if (!isWeightSizeValid) {
                        throw new EditTaskCommandException("moduleWeightOverloadError");
                    }
                    editTaskCommand.provideNewModule(newModule);
                }
            } catch (EditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_MODULE_MSG + "\n" + moduleListString;
            }
            break;

        case TASK_ESTIMATED_TIME_COST:
            try {
                if (!userInput.isBlank()) {
                    double newTimeCost = Double.parseDouble(userInput);
                    if (newTimeCost < 0) {
                        throw new EditTaskCommandException("wrongEstimatedTimeRangeError");
                    }
                    editTaskCommand.provideNewTaskTimeCost(newTimeCost);
                } else {
                    editTaskCommand.provideNewTaskTimeCost(0);
                }
            } catch (EditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            }
            break;

        case TASK_WEIGHT:
            try {
                double newWeight = 0;
                if (!userInput.isBlank()) {
                    newWeight = EditTaskCommandParser.parseWeight(userInput);
                    boolean isWeightSizeValid = isWeightSizeValid(newWeight, logic.getFilteredTaskList()
                        .get(taskIndex.getZeroBased()).getModule());
                    if (!isWeightSizeValid) {
                        throw new EditTaskCommandException("moduleWeightOverloadError");
                    }
                }
                editTaskCommand.provideNewTaskWeight(newWeight);
            } catch (EditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_WEIGHT_MSG;
            }
            break;

        case TASK_DESCRIPTION:
            try {
                String newDescription = EditTaskCommandParser.parseDescription(userInput);
                editTaskCommand.provideNewTaskDescription(newDescription);
            } catch (EditTaskCommandException e) {
                parseSuccess = false;
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_DESCRIPTION_MSG;
            }
            break;

        default:
            throw new IllegalStateException("Unexpected value: " + taskField);
        }

        if (parseSuccess) {
            try {
                logic.executeCommand(editTaskCommand);
                endInteract(successMessage);
            } catch (java.text.ParseException | CommandException ex) {
                reply = ex.getMessage();
            }
        }
        return reply;
    }

    /**
     * Checks the total weight of tasks under targetModule.
     *
     * @param toBeAddWeight
     * @param targetModule
     * @return true if the total weight is not larger than 100.
     */
    private boolean isWeightSizeValid(double toBeAddWeight, Module targetModule) {
        boolean isValid = true;
        ObservableList<Task> tempTasks = logic.getFilteredTaskList();
        tempTasks.addAll(logic.getFilteredArchivedTaskList());
        double moduleWeightSum = tempTasks
            .stream()
            .filter(t -> t.getModule().equals(targetModule))
            .mapToDouble(Task::getWeight).sum();
        if (moduleWeightSum + toBeAddWeight > 100) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * parses task number
     *
     * @param userInput user input for task number
     * @return an int of task number
     */
    public int parseTaskNumber(String userInput) {
        boolean isParseSuccessful = true;
        int taskNum = -1;

        try {
            if (userInput.isBlank()) {
                throw new EditTaskCommandException("emptyInputError");
            }
            taskNum = Integer.parseInt(userInput);
            if (taskNum > Task.getCurrentTasks().size() || taskNum < 1) {
                throw new EditTaskCommandException("invalidIndexRangeError");
            }
            String taskName = Task.getCurrentTasks().get(taskNum - 1).getTaskName();
            this.reply = "Please choose the field that you wish to edit for task: " + taskName + ".\n\n"
                + TaskField.getFieldString();
            this.currentTerm = InteractivePromptTerms.TASK_FIELD;
        } catch (NumberFormatException e) {
            this.reply = (new EditTaskCommandException("wrongIndexFormatError")).getErrorMessage();
        } catch (EditTaskCommandException ex) {
            this.reply = ex.getErrorMessage();
        }
        return taskNum;
    }

    /**
     * parses the task field.
     *
     * @param userInput userInput for task number
     * @return a TaskField
     */
    public TaskField parseTaskFieldNumber(String userInput) {
        boolean isParseSuccessful = true;
        TaskField taskField = null;

        try {
            int taskFieldNumber = Integer.parseInt(userInput);
            if (taskFieldNumber > 7 || taskFieldNumber < 1) {
                throw new ParseException("task field number not in range");
            }
            taskField = TaskField.getTaskFieldFromNumber(taskFieldNumber);
        } catch (NumberFormatException | ParseException ex) {
            isParseSuccessful = false;
        }

        if (isParseSuccessful) {
            assert (taskField != null);
            this.reply = getTaskFieldMessage(taskField);
            this.currentTerm = InteractivePromptTerms.NEW_VALUE;
        } else {
            // prompt for a new value
            this.reply = "Please choose a valid task field index";
            this.currentTerm = InteractivePromptTerms.TASK_FIELD;
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
            result += REQUIRED_TASK_NAME_MSG + "\n";
            break;
        case TASK_TYPE:
            result += REQUIRED_TASK_TYPE_MSG + "\n"
                + TaskType.getTypeString();
            break;
        case TASK_DATETIME:
            result += REQUIRED_DATE_TIME_MSG + "\n\n"
                + "Assignment: HH:mm dd/MM/yyyy \nRest: HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy";
            break;
        case TASK_DESCRIPTION:
            result += REQUIRED_TASK_DESCRIPTION_MSG + "\n";
            break;
        case TASK_WEIGHT:
            result += REQUIRED_TASK_WEIGHT_MSG + "\n";
            break;
        case TASK_ESTIMATED_TIME_COST:
            result += REQUIRED_TASK_ESTIMATED_TIME_COST_MSG + "\n";
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + taskField);
        }
        return result;
    }

    /**
     * hides empty module from the moduleList.
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

