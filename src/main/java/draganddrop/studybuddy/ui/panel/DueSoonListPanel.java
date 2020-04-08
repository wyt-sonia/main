package draganddrop.studybuddy.ui.panel;

import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.ui.UiPart;
import draganddrop.studybuddy.ui.card.DueSoonTaskCard;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of tasks Due Soon.
 */
public class DueSoonListPanel extends UiPart<Region> {
    private static final String FXML = "DueSoonListPanel.fxml";

    @FXML
    private ListView<Task> dueSoonListView;

    public DueSoonListPanel(ObservableList<Task> taskList) {
        super(FXML);
        dueSoonListView.setItems(taskList);
        dueSoonListView.setCellFactory(listView -> new TaskListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Task} using a {@code DetailedTaskCard}.
     */
    class TaskListViewCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new DueSoonTaskCard(task, getIndex() + 1).getRoot());
            }
        }
    }

}
