package draganddrop.studybuddy.ui.interactiveprompt.add;

/**
 * Logic of implementation:
 * IP will handle all interaction btw user and the window to get the final version of command
 * - FSM
 * Parser will handle to parsing of the command and create a command
 * command will execute the action
 * server display the response if needed
 */

import static draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptType.ADD_TASK;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import draganddrop.studybuddy.logic.commands.add.AddTaskCommand;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.AddTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePrompt;
import draganddrop.studybuddy.ui.interactiveprompt.InteractivePromptTerms;
import javafx.collections.ObservableList;

/**
 * A interactive prompt for adding new task.
 */
public class AddTaskInteractivePrompt extends InteractivePrompt {
    static final String END_OF_COMMAND_MSG = "Task added successfully!";
    static final String END_OF_COMMAND_DUPLICATE_MSG = "Task will not be added! Key in your next command :)";
    static final String REQUIRED_MODULE_MSG = "Please choose a Module for this task or press enter to skip. "
        + "Index number and module code are both acceptable.\n";
    static final String REQUIRED_TASK_NAME_MSG = "Please enter the task name.";
    static final String REQUIRED_TASK_TYPE_MSG = "Please choose the task type:\n" + TaskType.getTypeString();
    static final String REQUIRED_DATE_TIME_MSG = "Please enter the deadline/duration with format: ";
    static final String REQUIRED_TASK_DESCRIPTION_MSG = "Please enter task description or press enter to skip.\n";
    static final String REQUIRED_TASK_WEIGHT_MSG = "Please enter the weight of the task or press enter to skip.\n";
    static final String REQUIRED_TASK_ESTIMATED_TIME_COST_MSG = "Please enter the estimated number of hours cost "
        + "or press enter to skip.\n";
    static final String TASK_INFO_HEADER = "The task is ready to be added, press enter again to add the task:\n\n"
        + "=========== TASK INFO ===========\n";

    static final String QUIT_COMMAND_MSG = "Successfully quited from add task command.";

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
        if ("quit".equals(userInput)) {
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

            task.setStatus("pending");
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
                    module = AddTaskCommandParser.parseModule(userInput, modules);
                }
                task.setModule(module);
                currentTerm = InteractivePromptTerms.TASK_NAME;
                this.reply = checkAndModifyReply(module) + "\n\n"
                    + REQUIRED_TASK_NAME_MSG;
            } catch (AddTaskCommandException e) {
                reply = e.getErrorMessage() + "\n\n" + REQUIRED_MODULE_MSG + "\n\n" + moduleListString;
            }
            break;

        case TASK_NAME:
            try {
                userInput = AddTaskCommandParser.parseName(userInput);
                this.reply = "The name of task is set to: " + userInput + ".\n\n"
                    + REQUIRED_TASK_TYPE_MSG;
                task.setTaskName(userInput);
                currentTerm = InteractivePromptTerms.TASK_TYPE;
            } catch (AddTaskCommandException ex) {
                reply = ex.getErrorMessage() + "\n\n"
                    + REQUIRED_TASK_NAME_MSG;
            }
            break;

        case TASK_TYPE:
            try {
                TaskType taskType = AddTaskCommandParser.parseType(userInput, TaskType.getTaskTypes().length);
                task.setTaskType(taskType);

                this.reply = "The type of task has been set to: " + taskType.toString() + ".\n"
                    + REQUIRED_DATE_TIME_MSG;
                if (taskType.equals(TaskType.Assignment)) {
                    this.reply += "HH:mm dd/MM/yyyy";
                } else {
                    this.reply += "HH:mm dd/MM/yyyy-HH:mm dd/MM/yyyy";
                }
                currentTerm = InteractivePromptTerms.TASK_DATETIME;
            } catch (NumberFormatException ex) {
                reply = (new AddTaskCommandException("wrongIndexFormatError")).getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG;
            } catch (AddTaskCommandException ex) {
                reply = ex.getErrorMessage()
                    + "\n\n" + REQUIRED_TASK_TYPE_MSG;
            }
            break;

        case TASK_DATETIME:
            try {
                LocalDateTime[] dateTimes = AddTaskCommandParser.parseDateTime(userInput, task.getTaskType());
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
            } catch (AddTaskCommandException ex) {
                this.reply = ex.getErrorMessage();
            }
            break;

        case TASK_DESCRIPTION:
            this.reply = "";
            try {
                if (!userInput.isBlank()) {
                    task.setTaskDescription(AddTaskCommandParser.parseDescription(userInput));
                    this.reply = "The task description has been set as " + userInput + "\n\n";
                }
                this.reply += REQUIRED_TASK_WEIGHT_MSG;
                currentTerm = InteractivePromptTerms.TASK_WEIGHT;
            } catch (AddTaskCommandException e) {
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_DESCRIPTION_MSG;
            }
            break;

        case TASK_WEIGHT:
            try {
                this.reply = "";
                if (!userInput.isBlank()) {
                    double weight = AddTaskCommandParser.parseWeight(userInput);
                    double moduleWeightSum = logic.getStudyBuddy().getTaskList()
                        .stream()
                        .filter(t -> t.getModule().equals(task.getModule()))
                        .mapToDouble(Task::getWeight).sum();
                    double moduleWeightSumArchived = logic.getStudyBuddy().getArchivedList()
                        .stream()
                        .filter(t -> t.getModule().equals(task.getModule()))
                        .mapToDouble(Task::getWeight).sum();
                    if (moduleWeightSum + moduleWeightSumArchived + weight <= 100) {
                        task.setWeight(weight);
                    } else {
                        throw new AddTaskCommandException("moduleWeightOverloadError");
                    }

                    this.reply = "The weight of the task has been set as " + userInput + "\n\n";
                }
                this.reply += REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
                this.currentTerm = InteractivePromptTerms.TASK_ESTIMATED_TIME_COST;
            } catch (AddTaskCommandException e) {
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_WEIGHT_MSG;
            }

            break;

        case TASK_ESTIMATED_TIME_COST:
            try {
                this.reply = "";
                if (!userInput.isBlank()) {
                    task.setEstimatedTimeCost(AddTaskCommandParser.parseTimeCost(userInput));
                    this.reply = "The estimated number of hours the task might take has been set as "
                        + userInput + "\n\n";
                }
                this.reply += TASK_INFO_HEADER + task.toString();
                this.currentTerm = InteractivePromptTerms.READY_TO_EXECUTE;
            } catch (AddTaskCommandException e) {
                this.reply = e.getErrorMessage() + "\n\n" + REQUIRED_TASK_ESTIMATED_TIME_COST_MSG;
            }

            break;

        case READY_TO_EXECUTE:
            try {
                task.setCreationDateTime(LocalDateTime.now());
                AddTaskCommand addTaskCommand = new AddTaskCommand(task);
                logic.executeCommand(addTaskCommand);
                if (task.isDuplicate()) {
                    reply = "This is a duplicate task. Are you sure you would like to proceed?\n"
                        + "Please enter yes or no.";
                    currentTerm = InteractivePromptTerms.ADD_DUPLICATE;
                } else {
                    endInteract(END_OF_COMMAND_MSG);
                }
            } catch (ParseException | CommandException e) {
                e.printStackTrace();
            }
            break;

        case ADD_DUPLICATE:
            if (userInput.equalsIgnoreCase("yes")) {
                AddTaskCommand addDuplicateTaskCommand = new AddTaskCommand(task);
                try {
                    logic.executeCommand(addDuplicateTaskCommand);
                    endInteract(END_OF_COMMAND_MSG);
                } catch (CommandException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (userInput.equalsIgnoreCase("no")) {
                endInteract(END_OF_COMMAND_DUPLICATE_MSG);
            } else {
                reply = (new AddTaskCommandException("wrongDuplicateFormat")).getErrorMessage();
            }
            break;

        default:
        }
        return reply;
    }

    @Override
    public void endInteract(String msg) {
        this.reply = msg;
        super.setEndOfCommand(true);
    }

    /**
     * hides empty module from the moduleList.
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
     * modify reply if module is empty.
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
     * pending.
     */
    private String dateTime() {
        return "";
    }
}
