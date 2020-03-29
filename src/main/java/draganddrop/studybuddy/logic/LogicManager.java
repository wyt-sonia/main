package draganddrop.studybuddy.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.GuiSettings;
import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.StudyBuddyParser;
import draganddrop.studybuddy.logic.parser.exceptions.ParseException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.storage.Storage;

import javafx.collections.ObservableList;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final StudyBuddyParser studyBuddyParser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        studyBuddyParser = new StudyBuddyParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult = null;
        Command command = studyBuddyParser.parseCommand(commandText);
        try {
            commandResult = command.execute(model);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        } catch (CommandException ex) {
            ex.getLocalizedMessage();
        }

        try {
            storage.saveStudyBuddy(model.getStudyBuddy());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public CommandResult executeCommand(Command command) throws CommandException, java.text.ParseException {
        CommandResult commandResult;
        commandResult = command.execute(model);
        try {
            storage.saveStudyBuddy(model.getStudyBuddy());
            // keep track of task list
            Task.updateCurrentTaskList(new ArrayList<Task>(getStudyBuddy().getTaskList()));
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }
        return commandResult;
    }

    @Override
    public ReadOnlyStudyBuddy getStudyBuddy() {
        return model.getStudyBuddy();
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    @Override
    public ObservableList<Task> getFilteredArchivedTaskList() {
        return model.getFilteredArchivedTaskList();
    }

    @Override
    public ObservableList<Module> getFilteredModuleList() {
        return model.getFilteredModuleList();
    }

    @Override
    public ObservableList<Task> getFilteredDueSoonTaskList() {
        return model.getFilteredDueSoonTaskList();
    }

    @Override
    public Path getStudyBuddyFilePath() {
        return model.getStudyBuddyFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
