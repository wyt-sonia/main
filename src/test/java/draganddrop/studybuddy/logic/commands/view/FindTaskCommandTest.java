package draganddrop.studybuddy.logic.commands.view;

import static draganddrop.studybuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static draganddrop.studybuddy.testutil.TypicalTasks.getSampleTasks;
import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.task.TaskNameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindTaskCommand}.
 */
public class FindTaskCommandTest {
    private Model model = new ModelManager(getTypicalTaskList(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalTaskList(), new UserPrefs());

    @Test
    public void equals() {
        TaskNameContainsKeywordsPredicate firstPredicate =
            new TaskNameContainsKeywordsPredicate(Collections.singletonList("first"));
        TaskNameContainsKeywordsPredicate secondPredicate =
            new TaskNameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindTaskCommand findFirstCommand = new FindTaskCommand(firstPredicate);
        FindTaskCommand findSecondCommand = new FindTaskCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindTaskCommand findFirstCommandCopy = new FindTaskCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand == null);

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noTaskFound() {
        String expectedMessage = String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 0);
        TaskNameContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindTaskCommand command = new FindTaskCommand(predicate);
        expectedModel.updateFilteredTaskList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredTaskList());
    }

    @Test
    public void execute_multipleKeywords_multipleTasksFound() {
        String expectedMessage = String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, 2);
        TaskNameContainsKeywordsPredicate predicate = preparePredicate("Quiz");
        FindTaskCommand command = new FindTaskCommand(predicate);
        expectedModel.updateFilteredTaskList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(getSampleTasks()[2], getSampleTasks()[4]),
                model.getFilteredTaskList());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private TaskNameContainsKeywordsPredicate preparePredicate(String userInput) {
        return new TaskNameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
