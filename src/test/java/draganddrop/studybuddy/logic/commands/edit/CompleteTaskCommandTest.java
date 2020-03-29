package draganddrop.studybuddy.logic.commands.edit;

import static draganddrop.studybuddy.logic.commands.CommandTestUtil.assertCommandSuccess;
import static draganddrop.studybuddy.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static draganddrop.studybuddy.testutil.TypicalTasks.getSampleTasks;
import static draganddrop.studybuddy.testutil.TypicalTasks.getTypicalTaskList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.commons.core.Messages;
import draganddrop.studybuddy.model.Model;
import draganddrop.studybuddy.model.ModelManager;
import draganddrop.studybuddy.model.UserPrefs;
import draganddrop.studybuddy.model.task.Task;

public class CompleteTaskCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalTaskList(), new UserPrefs());
        expectedModel = new ModelManager(model.getStudyBuddy(), new UserPrefs());
    }

//    @Test
//    public void execute_checkCompleted_success() {
//        Task task = model.getFilteredTaskList().get(0);
//        String expectedMessage = String.format(Messages.MESSAGE_COMPLETE_TASK_SUCCESS, task.getTaskName());
//        CompleteTaskCommand command = new CompleteTaskCommand(INDEX_FIRST_TASK);
//        expectedModel.completeTask(task);
//        expectedModel.deleteTask(task);
//        expectedModel.archiveTask(task);
//        assertCommandSuccess(command, model, expectedMessage, expectedModel);
//        assertEquals(Arrays.asList(getSampleTasks()[1], getSampleTasks()[2],
//                getSampleTasks()[3], getSampleTasks()[4]),
//                model.getFilteredTaskList());
//    }
}
