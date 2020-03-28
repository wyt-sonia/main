package draganddrop.studdybuddy.logic;

import java.nio.file.Path;

import draganddrop.studdybuddy.commons.core.GuiSettings;
import draganddrop.studdybuddy.logic.commands.Command;
import draganddrop.studdybuddy.logic.commands.CommandResult;
import draganddrop.studdybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studdybuddy.logic.parser.exceptions.ParseException;
import draganddrop.studdybuddy.model.Model;
import draganddrop.studdybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studdybuddy.model.module.Module;
import draganddrop.studdybuddy.model.task.Task;

import javafx.collections.ObservableList;


/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     *
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException   If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    CommandResult executeCommand(Command command) throws CommandException, java.text.ParseException;

    /**
     * Returns the StudyBuddy.
     *
     * @see Model#getStudyBuddy()
     */
    ReadOnlyStudyBuddy getStudyBuddy();

    /**
     * Returns an unmodifiable view of the filtered list of tasks
     */
    ObservableList<Task> getFilteredTaskList();

    /**
     * Returns an unmodifiable view of the filtered list of tasks that are due soon
     */
    ObservableList<Task> getFilteredDueSoonTaskList();

    /**
     * Returns an unmodifiable view of the filtered list of archived tasks.
     */
    ObservableList<Task> getFilteredArchivedTaskList();


    ObservableList<Module> getFilteredModuleList();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getStudyBuddyFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);
}
