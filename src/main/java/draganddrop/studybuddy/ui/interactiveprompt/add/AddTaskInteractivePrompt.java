package draganddrop.studybuddy.ui.interactiveprompt.add;

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.ADD_TASK;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import draganddrop.studybuddy.logic.commands.add.AddDuplicateTaskCommand;
import draganddrop.studybuddy.logic.commands.add.AddTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.TaskParser;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddDuplicateTaskCommandException;
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
 * A interactive prompt for adding new task.
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

    private String moduleListString = "";
    private ObservableList<Module> modules;
    private Task task;

    public AddTaskInteractivePrompt() {
        super();
        this.interactivePromptType = ADD_TASK;
        this.task = new Task();
        this.modules = null;
    }

    @Override
    public String interact(String userInput) {
        if ("quit".equalsIgnoreCase(userInput)) {
            endInteract(QUIT_COMMAND_MSG);
            return reply;
        }

        switch (currentTerm) {
        case INIT:
            this.reply = REQUIRED_MODULE_MSG;
            moduleListString = "The Modules available are: \n";
            this.modules = logic.getStudyBuddy().getModuleList();
            constructModuleList(modules);
            this.reply += moduleListString;
            currentTerm = InteractivePromptTerms.TASK_MODULE;

            task.setStatus("Pending");
            task.setTaskDescription("No Description");
            task.setWeight(0.0);
            task.setEstimatedTimeCost(0);
            break;

        case TASK_MODULE:
            try {
                Module module = null;
                if (userInput.isBlank()) {
                    module = new EmptyModule();
                } else {
                    module = TaskParser.parseModule(userInput, modules);
                }
                task.setModule(module);
                currentTerm = InteractivePromptTerms.TASK_NAME;
                this.reply = checkAndModifyReply(module) + "\n\n"
                    + REQUIRED_TASK_NAME_MSG;
            } catch (AddOrEditTaskCommandException e) {
                reply = e.getErrorMessage() + "\n\n" + REQUIRED_MODULE_MSG + "\n\n" + moduleListString;
            }
            break;

        case TASK_NAME:
            try {
                userInput = TaskParser.parseName(userInput);
                this.reply = "The name of task is set to: " + userInput + ".\n\n"
                    + REQUIRED_TASK_TYPE_MSG;
                task.setTaskName(userInput);
                currentTerm = InteractivePromptTerms.TASK_TYPE;
            } catch (InteractiveCommandException ex) {
                reply = ex.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_NAME_MSG;
            }
            break;

        case TASK_TYPE:
            try {
                TaskType taskType = TaskParser.parseType(userInput, TaskType.getTaskTypes().length);
                task.setTaskType(taskType);
                this.reply = "The type of task has been set to: " + taskType.toString() + ".\n\n"
                    + REQUIRED_DATE_TIME_MSG + getDateTimeFormat(taskType);
                currentTerm = InteractivePromptTerms.TASK_DATETIME;
            } catch (NumberFormatException ex) {
                reply = (new AddOrEditTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG;
            } catch (AddOrEditTaskCommandException ex) {
                reply = ex.getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG;
            }
            break;

        case TASK_DATETIME:
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
                this.reply = ex.getErrorMessage() + "\n\n"
                    + REQUIRED_DATE_TIME_MSG + getDateTimeFormat(task.getTaskType());
            }
            break;

        case TASK_DESCRIPTION:
            this.reply = "";
            try {
                if (!userInput.isBlank()) {
                    task.setTaskDescription(TaskParser.parseDescription(userInput));
                    this.reply = "The task description has been set as " + userInput + "\n\n";
                }
                this.reply += REQUIRED_TASK_WEIGHT_MSG;
                currentTerm = InteractivePromptTerms.TASK_WEIGHT;
            } catch (AddOrEditTaskCommandException e) {
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_DESCRIPTION_MSG;
            }
            break;

        case TASK_WEIGHT:
            try {
                this.reply = "";
                if (!userInput.isBlank()) {
                    double weight = TaskParser.parseWeight(userInput);
                    double moduleWeightSum = logic.getStudyBuddy().getTaskList()
                        .stream()
                        .filter(t -> (t.getModule().equals(task.getModule())))
                        .mapToDouble(Task::getWeight).sum();
                    double moduleWeightSumArchived = logic.getStudyBuddy().getArchivedList()
                        .stream()
                        .filter(t -> t.getModule().equals(task.getModule()))
                        .mapToDouble(Task::getWeight).sum();
                    if (moduleWeightSum + moduleWeightSumArchived + weight <= 100) {
                        task.setWeight(weight);
                    } else {
                        throw new AddOrEditTaskCommandException("moduleWeightOverloadError");
                    }

                    this.reply = "The weight of the task has been set as " + userInput + "\n\n";
                }
                this.reply += REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
                this.currentTerm = InteractivePromptTerms.TASK_ESTIMATED_TIME_COST;
            } catch (AddOrEditTaskCommandException e) {
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_WEIGHT_MSG;
            }
            break;

        case TASK_ESTIMATED_TIME_COST:
            try {
                this.reply = "";
                if (!userInput.isBlank()) {
                    task.setEstimatedTimeCost(TaskParser.parseTimeCost(userInput));
                    this.reply = "The estimated number of hours the task might take has been set as "
                        + userInput + "\n\n";
                }
                this.reply += TASK_INFO_HEADER + task.toString();
                this.currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (AddOrEditTaskCommandException e) {
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            }
            break;

        case READY_TO_EXECUTE:
            try {
                task.setCreationDateTime(LocalDateTime.now());
                if (Task.getCurrentTasks().contains(task)) {
                    reply = CONFIRM_MSG;
                    currentTerm = InteractivePromptTerms.ADD_DUPLICATE;
                } else {
                    AddTaskCommand addTaskCommand = new AddTaskCommand(task);
                    logic.executeCommand(addTaskCommand);
                    endInteract(END_OF_COMMAND_MSG);
                }
            } catch (ParseException | CommandException e) {
                e.printStackTrace();
            }
            break;

        case ADD_DUPLICATE:
            if (userInput.equalsIgnoreCase("yes")) {
                AddDuplicateTaskCommand addDuplicateTaskCommand = new AddDuplicateTaskCommand(task);
                try {
                    logic.executeCommand(addDuplicateTaskCommand);
                    endInteract(END_OF_DUPLICATE_COMMAND_MSG);
                } catch (CommandException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (userInput.equalsIgnoreCase("no")) {
                endInteract(END_OF_COMMAND_DUPLICATE_MSG);
            } else {
                //change this
                reply = (new AddDuplicateTaskCommandException("invalidInputError")).getErrorMessage()
                    + "\n\n" + CONFIRM_MSG;
            }
            break;

        default:
        }
        assert this.reply.isBlank()
            : "The reply of add task's " + currentTerm.name() + " is blank, please check.\n";
        return reply;
    }


    @Override
    public void endInteract(String msg) {
        this.reply = msg;
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
     * Modify reply if module is empty.
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
     * Get the date and time format string base on the type of the task.
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
