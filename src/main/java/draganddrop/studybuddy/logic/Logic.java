package draganddrop.studybuddy.logic;

import java.nio.file.Path;

import draganddrop.studybuddy.commons.core.GuiSettings;
import draganddrop.studybuddy.logic.commands.Command;
import draganddrop.studybuddy.logic.commands.CommandResult;
import draganddrop.studybuddy.logic.commands.exceptions.CommandException;
import draganddrop.studybuddy.logic.parser.exceptions.ParseException;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ReadOnlyStudyBuddy;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
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
     * Returns an unmodifiable view of the filtered list of tasks that are Due Soon
     */
    ObservableList<Task> getFilteredDueSoonTaskList();

    /**
     * Returns an unmodifiable view of the filtered list of archived tasks.
     */
    ObservableList<Task> getFilteredArchivedTaskList();

    ObservableList<Module> getFilteredModuleList();

    /**
     * Returns the user prefs' study buddy file path.
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
