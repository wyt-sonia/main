package draganddrop.studdybuddy.ui;

import java.util.logging.Logger;

import draganddrop.studdybuddy.commons.core.LogsCenter;
import draganddrop.studdybuddy.model.module.EmptyModule;
import draganddrop.studdybuddy.model.module.Module;
import draganddrop.studdybuddy.model.task.Task;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * FXML switched to ModuleListPanelv2. Can show task under each module.
 */
public class ModuleListPanel extends UiPart<Region> {
    private static final String FXML = "ModuleListPanelv2.fxml";
    private final Logger logger = LogsCenter.getLogger(ModuleListPanel.class);
    private ObservableList<Module> moduleObservableList;
    private ObservableList<Task> tasks;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab overview;

    @FXML
    private ListView<Module> moduleListView;

    public ModuleListPanel(ObservableList<Module> moduleList, ObservableList<Task> tasks) {
        super(FXML);
        this.moduleObservableList = moduleList;
        this.tasks = tasks;
        moduleListView.setItems(moduleList);
        moduleListView.setCellFactory(listView -> new ModuleListViewCell());
        moduleObservableList.forEach(x -> {
            x.filterAndSetInternalTaskList(tasks);
            String tabName;
            if (x.equals(new EmptyModule())) {
                tabName = "No Module Allocated";
            } else {
                tabName = x.toString();
            }
            Tab newTab = new Tab(tabName);
            if (!x.getInternalTaskList().isEmpty()) {
                ListView<Task> taskListView = new ListView<>();
                taskListView.setItems(x.getInternalTaskList());
                taskListView.setCellFactory(listView -> new TaskListPanel.TaskListViewCell());
                newTab.setContent(taskListView);
            } else {
                Label emptyLabel = new Label("No task in this module");
                emptyLabel.setWrapText(true);
                emptyLabel.setAlignment(Pos.CENTER);
                emptyLabel.setTextAlignment(TextAlignment.CENTER);
                emptyLabel.setContentDisplay(ContentDisplay.CENTER);
                emptyLabel.setTextFill(Color.GRAY);
                emptyLabel.setFont(new Font("Arial", 40));
                emptyLabel.setPrefWidth(940);
                emptyLabel.setPrefHeight(600);
                newTab.setContent(emptyLabel);
            }
            tabPane.getTabs().add(newTab);

        });
    }

    /**
     * Pending.
     */
    class ModuleListViewCell extends ListCell<Module> {
        /**
         * pending.
         * @param module
         * @param empty
         */
        @Override
        protected void updateItem(Module module, boolean empty) {
            super.updateItem(module, empty);

            if (empty || module == null || module.equals(new EmptyModule())) {
                setGraphic(null);
                setText(null);
            } else {
                ModCard modcard = new ModCard(module, getIndex() + 1);
                setGraphic(modcard.getRoot());
            }
        }
    }



}
