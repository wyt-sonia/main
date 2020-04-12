package draganddrop.studybuddy.ui.interactiveprompt.add;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.ADD_TASK;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.commands.add.AddDuplicateTaskCommand;
import draganddrop.studybuddy.logic.commands.add.AddTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.TaskParser;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.InteractiveCommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import javafx.collections.ObservableList;

/**
 * An interactive prompt for adding a new task.
 *
 * @@author Wang Yuting
 */
public class AddTaskInteractivePrompt extends InteractivePrompt {
    public static final String REQUIRED_MODULE_MSG = "Please choose a module for this task or press enter to skip. "
        + "\nIndex number and module code are both acceptable.\n";

    public static final String QUIT_COMMAND_MSG = "Successfully quited from add task command.";

    private static final String END_OF_COMMAND_MSG = "Task added successfully!";
    private static final String END_OF_DUPLICATE_COMMAND_MSG = "Task added successfully! "
        + "We have changed the name slightly for your convenience.";
    private static final String END_OF_COMMAND_DUPLICATE_MSG = "Task will not be added! Key in your next command :)";

    private static final String REQUIRED_TASK_NAME_MSG = "Please enter the task name.";
    private static final String REQUIRED_TASK_TYPE_MSG = "Please choose the task type:\n" + TaskType.getTypeString();
    private static final String REQUIRED_DATE_TIME_MSG = "Please enter the deadline/duration with format: ";
    private static final String REQUIRED_TASK_DESCRIPTION_MSG = "Please enter task description "
        + "or press enter to skip.\n";
    private static final String REQUIRED_TASK_WEIGHT_MSG = "Please enter the weight of the task "
        + "or press enter to skip.\n";
    private static final String REQUIRED_TASK_ESTIMATED_TIME_COST_MSG = "Please enter the estimated "
        + "number of hours cost or press enter to skip.\n";

    private static final String TASK_INFO_HEADER = "The task is ready to be added, press enter "
        + "again to add the task:\n\n=========== TASK INFO ===========\n";
    private static final String CONFIRM_MSG = "This is a duplicate task. Are you sure you would like to proceed?\n"
        + "Please enter yes to continue or no to go back.";

    private static final String LOG_TAG = "AddTaskInteractivePrompt";
    private final Logger logger = LogsCenter.getLogger(AddTaskInteractivePrompt.class);

    private String moduleListString = "";
    private ObservableList<Module> modules;
    private Task task;

    public AddTaskInteractivePrompt() {
        super();
        logger.log(Level.INFO, LOG_TAG + ": Start of an add task action.");
        this.interactivePromptType = ADD_TASK;
        this.task = new Task();
        this.modules = null;
    }

    @Override
    public String interact(String userInput) {

        if ("quit".equalsIgnoreCase(userInput)) {
            logger.log(Level.INFO, LOG_TAG + ": User quite from add task action.");
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            initTermHandler();
            break;

        case TASK_MODULE:
            taskModuleTermHandler(userInput);
            break;

        case TASK_NAME:
            handleTaskNameTerm(userInput);
            break;

        case TASK_TYPE:
            handleTaskTypeTerm(userInput);
            break;

        case TASK_DATETIME:
            handleTaskDateTimeTerm(userInput);
            break;

        case TASK_DESCRIPTION:
            handleTaskDescriptionTerm(userInput);
            break;

        case TASK_WEIGHT:
            handleTaskWeightTerm(userInput);
            break;

        case TASK_ESTIMATED_TIME_COST:
            handleTaskEstimatedTimeCostTerm(userInput);
            break;

        case READY_TO_EXECUTE:
            handleTaskReadyToExecuteTerm();
            break;

        case ADD_DUPLICATE:
            handleTaskAddDuplicateTerm(userInput);
            break;

        default:
        }

        assert !this.reply.isBlank()
            : "The reply of add task's " + currentTerm.name() + " is blank, please check.\n";
        return reply;
    }

    /**
     * Handles the INIT term of add task interaction.
     * <p>
     * Sets default value for the new task.
     * Prepares the module request message for user.
     */
    private void initTermHandler() {
        logger.log(Level.INFO, LOG_TAG + ": Start tof init term.");
        // Set default value for the new task.
        task.setStatus("Pending");
        task.setTaskDescription("No Description");
        task.setWeight(0.0);
        task.setEstimatedTimeCost(0);

        // Init the module request message for user.
        this.reply = REQUIRED_MODULE_MSG;
        moduleListString = "The Modules available are: \n";
        this.modules = logic.getStudyBuddy().getModuleList();
        constructModuleList(modules);
        this.reply += moduleListString;

        currentTerm = InteractivePromptTerms.TASK_MODULE;

        logger.log(Level.INFO, LOG_TAG + ": End of init term.");
    }

    /**
     * Handles the TASK_MODULE term of add task interaction.
     * <p>
     * Parses the module from {@code userInput}.
     * Prepares the task name request message for user.
     * Displays error message with invalid {@code userInput}
     *
     * @param userInput
     */
    private void taskModuleTermHandler(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of task module term.");

        try {
            Module module;
            if (userInput.isBlank()) {
                module = new EmptyModule();
            } else {
                module = TaskParser.parseModule(userInput, modules);
            }
            task.setModule(module);

            this.reply = checkAndModifyReply(module) + "\n\n"
                + REQUIRED_TASK_NAME_MSG;
            currentTerm = InteractivePromptTerms.TASK_NAME;

        } catch (AddOrEditTaskCommandException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getErrorMessage());
            reply = e.getErrorMessage() + "\n\n" + REQUIRED_MODULE_MSG + "\n\n" + moduleListString;
        }

        logger.log(Level.INFO, LOG_TAG + ": End of task module term.");
    }

    /**
     * Handles the TASK_NAME term of add task interaction.
     * <p>
     * Parses the task name from {@code userInput}.
     * Prepares the task type request message for user.
     * Displays error message with invalid {@code userInput}.
     *
     * @param userInput
     */
    private void handleTaskNameTerm(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of task name term.");

        try {
            userInput = TaskParser.parseName(userInput);
            task.setTaskName(userInput);

            this.reply = "The name of task is set to: " + userInput + ".\n\n"
                + REQUIRED_TASK_TYPE_MSG;
            currentTerm = InteractivePromptTerms.TASK_TYPE;

        } catch (InteractiveCommandException ex) {
            logger.log(Level.WARNING, LOG_TAG + ": " + ex.getErrorMessage());
            reply = ex.getErrorMessage() + "\n\n"
                + REQUIRED_TASK_NAME_MSG;
        }

        logger.log(Level.INFO, LOG_TAG + ": End of task name term.");
    }

    /**
     * Handles the TASK_TYPE term of add task interaction.
     * <p>
     * Parses the task type from {@code userInput}.
     * Prepares the task date time request message for user.
     * Displays error message with invalid {@code userInput}.
     *
     * @param userInput
     */
    private void handleTaskTypeTerm(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of task type term.");
        try {
            TaskType taskType = TaskParser.parseType(userInput, TaskType.getTaskTypes().length);
            task.setTaskType(taskType);

            this.reply = "The type of task has been set to: " + taskType.toString() + ".\n\n"
                + REQUIRED_DATE_TIME_MSG + getDateTimeFormat(taskType);
            currentTerm = InteractivePromptTerms.TASK_DATETIME;

        } catch (NumberFormatException ex) {

            logger.log(Level.WARNING, LOG_TAG + ": " + ex.getMessage());
            reply = (new AddOrEditTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                + "\n\n" + REQUIRED_TASK_TYPE_MSG;
        } catch (AddOrEditTaskCommandException ex) {

            logger.log(Level.WARNING, LOG_TAG + ": " + ex.getMessage());
            reply = ex.getErrorMessage()
                + "\n\n" + REQUIRED_TASK_TYPE_MSG;
        }
        logger.log(Level.INFO, LOG_TAG + ": End of task type term.");
    }

    /**
     * Handles the TASK_DATETIME term of add task interaction.
     * <p>
     * Parses the task date time from {@code userInput}.
     * Prepares the task description request message for user.
     * Displays error message with invalid {@code userInput}.
     *
     * @param userInput
     */
    private void handleTaskDateTimeTerm(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of task date time term.");

        try {
            if (userInput.isBlank()) {
                throw new AddOrEditTaskCommandException("emptyInputError");
            }

            LocalDateTime[] dateTimes = TaskParser.parseDateTime(userInput, task.getTaskType());
            task.setDateTimes(dateTimes);
            if (dateTimes.length == 1) {
                userInput = TimeParser.getDateTimeString(dateTimes[0]);
            } else {
                userInput = TimeParser.getDateTimeString(dateTimes[0])
                    + "-" + TimeParser.getDateTimeString(dateTimes[1]);
            }

            this.reply = "The date and time is set to: " + userInput + "\n\n"
                + REQUIRED_TASK_DESCRIPTION_MSG;
            currentTerm = InteractivePromptTerms.TASK_DESCRIPTION;

        } catch (AddOrEditTaskCommandException ex) {

            logger.log(Level.WARNING, LOG_TAG + ": " + ex.getMessage());
            this.reply = ex.getErrorMessage() + "\n\n"
                + REQUIRED_DATE_TIME_MSG + getDateTimeFormat(task.getTaskType());
        }

        logger.log(Level.INFO, LOG_TAG + ": End of task date time term.");
    }

    /**
     * Handles the TASK_DESCRIPTION term of add task interaction.
     * <p>
     * Parses the task description from {@code userInput}.
     * Prepares the task weight request message for user.
     * Displays error message with invalid {@code userInput}.
     *
     * @param userInput
     */
    private void handleTaskDescriptionTerm(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of task description time term.");

        this.reply = "";

        try {
            if (!userInput.isBlank()) {
                task.setTaskDescription(TaskParser.parseDescription(userInput));
                this.reply = "The task description has been set as " + userInput + "\n\n";
            }
            this.reply += REQUIRED_TASK_WEIGHT_MSG;
            currentTerm = InteractivePromptTerms.TASK_WEIGHT;

        } catch (AddOrEditTaskCommandException e) {

            logger.log(Level.WARNING, LOG_TAG + ": " + e.getMessage());
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_DESCRIPTION_MSG;
        }

        logger.log(Level.INFO, LOG_TAG + ": End of task description term.");
    }

    /**
     * Handles the TASK_WEIGHT term of interaction.
     * <p>
     * Parses the task weight from {@code userInput}.
     * Prepares the task estimated time cost request message for user.
     * Displays error message with invalid {@code userInput}.
     *
     * @param userInput
     */
    private void handleTaskWeightTerm(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of task weight term.");

        this.reply = "";

        try {
            if (!userInput.isBlank()) {
                double weight = TaskParser.parseWeight(userInput);

                // Calculates the sum of the weights of unarchived tasks under targetModule.
                double moduleWeightSumForUnarchived = logic.getStudyBuddy().getTaskList()
                    .stream()
                    .filter(t -> (t.getModule().equals(task.getModule())))
                    .mapToDouble(Task::getWeight).sum();

                // Calculates the sum of the weights of archived tasks under targetModule.
                double moduleWeightSumForArchivedTasks = logic.getStudyBuddy().getArchivedList()
                    .stream()
                    .filter(t -> t.getModule().equals(task.getModule()))
                    .mapToDouble(Task::getWeight).sum();

                if (moduleWeightSumForUnarchived + moduleWeightSumForArchivedTasks + weight > 100) {
                    throw new AddOrEditTaskCommandException("moduleWeightOverloadError");
                }

                task.setWeight(weight);
                this.reply = "The weight of the task has been set as " + userInput + "\n\n";
            }
            this.reply += REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            this.currentTerm = InteractivePromptTerms.TASK_ESTIMATED_TIME_COST;

        } catch (AddOrEditTaskCommandException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getMessage());
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_WEIGHT_MSG;
        }

        logger.log(Level.INFO, LOG_TAG + ": End of task weight term.");
    }

    /**
     * Handles the TASK_ESTIMATED_TIME_COST term of add task interaction.
     * <p>
     * Parses the estimated time cost of the new task from {@code userInput}.
     * Prepares the insertion confirmation request message for user.
     * Displays error message with invalid {@code userInput}.
     *
     * @param userInput
     */
    private void handleTaskEstimatedTimeCostTerm(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of task estimated time cost term.");

        this.reply = "";

        try {
            if (!userInput.isBlank()) {
                task.setEstimatedTimeCost(TaskParser.parseTimeCost(userInput));
                this.reply = "The estimated number of hours the task might take has been set as "
                    + userInput + "\n\n";
            }
            this.reply += TASK_INFO_HEADER + task.toString();
            this.currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;

        } catch (AddOrEditTaskCommandException e) {

            logger.log(Level.WARNING, LOG_TAG + ": " + e.getMessage());
            this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
        }

        logger.log(Level.INFO, LOG_TAG + ": End of task estimated time cost term.");
    }


    /**
     * Handles the READY_TO_EXECUTE term of add task interaction.
     * <p>
     * Checks the duplication of the new task and forwards the interaction accordingly.
     * Displays error message with any exception caused.
     */
    private void handleTaskReadyToExecuteTerm() {
        logger.log(Level.INFO, LOG_TAG + ": Start of ready to execute term.");

        try {
            if (logic.getStudyBuddy().getTaskList().contains(task)) {
                reply = CONFIRM_MSG;
                currentTerm = InteractivePromptTerms.ADD_DUPLICATE;
            } else {
                task.setCreationDateTime(LocalDateTime.now());
                AddTaskCommand addTaskCommand = new AddTaskCommand(task);
                logic.executeCommand(addTaskCommand);
                logger.log(Level.INFO, LOG_TAG + ": User added a unique new task.");
                endInteract(END_OF_COMMAND_MSG);
            }

        } catch (ParseException | CommandException e) {

            logger.log(Level.WARNING, LOG_TAG + ": " + e.getMessage());
            this.reply = new InteractiveCommandException("unKnownException").getErrorMessage();
        }

        logger.log(Level.INFO, LOG_TAG + ": End of ready to execute term.");
    }

    /**
     * Handles the ADD_DUPLICATE term of add task interaction.
     * <p>
     * Parses user option for adding duplicate task and response accordingly.
     * Displays error message with any exception caused.
     *
     * @param userInput
     */
    private void handleTaskAddDuplicateTerm(String userInput) {
        logger.log(Level.INFO, LOG_TAG + ": Start of  add duplicated task term.");

        try {
            if ("yes".equalsIgnoreCase(userInput)) {
                AddDuplicateTaskCommand addDuplicateTaskCommand = new AddDuplicateTaskCommand(task);
                logic.executeCommand(addDuplicateTaskCommand);
                logger.log(Level.INFO, LOG_TAG + ": User added a duplicated task.");
                endInteract(END_OF_DUPLICATE_COMMAND_MSG);
            } else if ("no".equalsIgnoreCase(userInput)) {
                endInteract(END_OF_COMMAND_DUPLICATE_MSG);
            } else {
                throw new AddOrEditTaskCommandException("invalidInputError");
            }

        } catch (ParseException | CommandException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getMessage());
            this.reply = new InteractiveCommandException("unKnownException").getErrorMessage();
        } catch (AddOrEditTaskCommandException e) {
            logger.log(Level.WARNING, LOG_TAG + ": " + e.getMessage());
            reply = e.getErrorMessage() + "\n\n" + CONFIRM_MSG;
        }

        logger.log(Level.INFO, LOG_TAG + ": End of add duplicated task term.");
    }

    @Override
    public void endInteract(String msg) {
        this.reply = msg;
        logger.log(Level.INFO, LOG_TAG + ": End of add task action.");
        super.setEndOfCommand(true);
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

    /**
     * Modifies reply if module is empty.
     *
     * @param module
     * @return
     */
    private String checkAndModifyReply(Module module) {
        if (!module.equals(new EmptyModule())) {
            return "The module has been set as: " + module.getModuleCode() + " "
                + module.getModuleName();
        } else {
            return "This task is not assigned to any modules.";
        }
    }

    /**
     * Gets the date and time format string base on the type of the task.
     *
     * @param taskType
     * @return
     */
    private String getDateTimeFormat(TaskType taskType) {
        String format = "";
        if (taskType.equals(TaskType.Assignment)) {
            format = "HH:mm dd/MM/yyyy e.g. 12:00 01/05/2020";
        } else {
            format = "HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy e.g. 12:00 01/05/2020-12:00 10/05/2020";
        }
        return format;
    }
}
