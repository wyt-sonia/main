package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.module.ModuleList;
import seedu.address.model.module.Module;
import seedu.address.model.task.Task;
import javafx.scene.layout.Region;

import javafx.collections.ObservableList;


public class ModuleListPanel extends UiPart<Region> {
    private static final String FXML = "ModuleListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ModuleListPanel.class);

    @FXML
    private ListView<Module> moduleListView; //Strange. Have to add this while TaskListPanel doesn't need this

    public ModuleListPanel(ObservableList<Module> moduleList) {
        super(FXML);
        moduleListView.setItems(moduleList);
        moduleListView.setCellFactory(listView -> new ModuleListViewCell());
    }

        class ModuleListViewCell extends ListCell<Module> {
            @Override
            protected void updateItem(Module module, boolean empty) {
                super.updateItem(module, empty);

                if (empty || module == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ModCard modcard = new ModCard(module, getIndex() + 1);
                    setGraphic(modcard.getRoot());
                }
            }

        }
}

