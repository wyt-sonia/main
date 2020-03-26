package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.module.EmptyModule;
import seedu.address.model.module.Module;
import seedu.address.model.task.Task;

import java.util.logging.Logger;

public class ModuleListPanelv2 extends UiPart<Region> {
    private static final String FXML = "ModuleListPanelv2.fxml";
    private final Logger logger = LogsCenter.getLogger(ModuleListPanelv2.class);
    ObservableList<Module> moduleObservableList;
    ObservableList<Task> tasks;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab overview;

    @FXML
    private ListView<Module> moduleListView;

    public ModuleListPanelv2(ObservableList<Module> moduleList, ObservableList<Task> tasks) {
        super(FXML);
        this.moduleObservableList = moduleList;
        this.tasks = tasks;
        moduleListView.setItems(moduleList);
        moduleListView.setCellFactory(listView -> new ModuleListViewCell());
        moduleObservableList.forEach(x->{
            x.filterAndSetInternalTaskList(tasks);
            String tabName;
            if (x.equals(new EmptyModule())) {
               tabName = "No Module Allocated";
            } else {
                tabName = x.toString();
            }
            Tab newTab = new Tab(tabName);
            if(!x.getInternalTaskList().isEmpty()){
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


    class ModuleListViewCell extends ListCell<Module> {
        @Override
        protected void updateItem(Module module, boolean empty) {
            super.updateItem(module, empty);

            if (empty || module == null || module.equals(new EmptyModule()) ) {
                setGraphic(null);
                setText(null);
            } else {
                ModCard modcard = new ModCard(module, getIndex() + 1);
                setGraphic(modcard.getRoot());
            }
        }
    }



}
