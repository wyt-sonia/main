package draganddrop.studybuddy.ui.panel;

import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.ui.UiPart;
import draganddrop.studybuddy.ui.card.ModCard;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * FXML switched to ModuleListPanel. Can show task under each module.
 */
public class ModuleListPanel extends UiPart<Region> {
    private static final String FXML = "ModuleListPanel.fxml";
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
        moduleListView.setCellFactory(listView -> new ModuleListViewCell());
        bindTabs(moduleList);

        moduleObservableList.addListener(new ListChangeListener<Module>() {
            @Override
            public void onChanged(Change<? extends Module> c) {
                bindTabs(moduleList);
            }
        });

        tasks.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> c) {
                bindTabs(moduleList);
            }
        });
    }

    /**
     * Bind tabs when tab list updated.
     * @param moduleList
     */
    private void bindTabs(ObservableList<Module> moduleList) {
        Tab overviewTab = tabPane.getTabs().get(0);
        tabPane.getTabs().clear();
        tabPane.getTabs().add(overviewTab);
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
                taskListView.setPrefHeight(600);
                newTab.setContent(taskListView);
            } else {
                Label emptyLabel = new Label("No task in this module");
                emptyLabel.setWrapText(true);
                emptyLabel.setAlignment(Pos.CENTER);
                emptyLabel.setTextAlignment(TextAlignment.CENTER);
                emptyLabel.setContentDisplay(ContentDisplay.CENTER);
                emptyLabel.setTextFill(Color.GRAY);
                emptyLabel.setFont(new Font("Arial", 40));
                emptyLabel.setBackground(new Background(
                    new BackgroundFill(Color.WHITE,
                        new CornerRadii(0, 0, 10, 10, false),
                        Insets.EMPTY)));
                emptyLabel.setPrefHeight(600);
                emptyLabel.prefWidthProperty().bind(tabPane.widthProperty());
                newTab.setContent(emptyLabel);
            }
            tabPane.getTabs().add(newTab);
        });
        moduleListView.setItems(moduleList);
    }

    /**
     * Represents a ModuleListViewCell.
     */
    class ModuleListViewCell extends ListCell<Module> {
        /**
         * Updates item.
         *
         * @param module
         * @param isEmpty
         */
        @Override
        protected void updateItem(Module module, boolean isEmpty) {
            super.updateItem(module, isEmpty);

            if (isEmpty || module == null || module.equals(new EmptyModule())) {
                setGraphic(null);
                setText(null);
            } else {
                ModCard modcard = new ModCard(module, getIndex() + 1);
                setGraphic(modcard.getRoot());
            }
        }
    }


}
