package draganddrop.studybuddy.ui.panel;

import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.ui.UiPart;
import draganddrop.studybuddy.ui.card.DetailedTaskCard;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * An UI component which represents the panel containing the list of tasks.
 *
 * @@author wyt-sonia
 */
public class TaskListPanel extends UiPart<Region> {
    private static final String FXML = "TaskListPanel.fxml";
    private static final String LOG_TAG = "TaskListPanel";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @FXML
    private ListView<Task> taskListView;

    public TaskListPanel(ObservableList<Task> taskList) {
        super(FXML);
        logger.info(LOG_TAG + " : Start to set up the Task List Panel.");
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        logger.info(LOG_TAG + " : End of setting up the Task List Panel.");
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Task} using a {@code DetailedTaskCard}.
     */
    static class TaskListViewCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean isEmpty) {
            super.updateItem(task, isEmpty);

            if (isEmpty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DetailedTaskCard(task, getIndex() + 1).getRoot());
                //setGraphic(new DetailedTaskCard(task, getIndex()).getRoot());
            }
        }
    }
}
