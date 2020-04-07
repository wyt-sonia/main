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
 * Panel containing the list of persons.
 */
public class TaskListPanel extends UiPart<Region> {
    private static final String FXML = "TaskListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @FXML
    private ListView<Task> taskListView;

    public TaskListPanel(ObservableList<Task> taskList) {
        super(FXML);
        logger.fine(FXML + " : Start to set up the Task List Panel.");
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        logger.fine(FXML + " : End of setting up the Task List Panel.");
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code DetailedTaskCard}.
     */
    static class TaskListViewCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DetailedTaskCard(task, getIndex() + 1).getRoot());
            }
        }
    }
}
