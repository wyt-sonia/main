package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.module.EmptyModule;
import seedu.address.model.module.Module;
import seedu.address.model.task.Task;

/**
 * module for display
 */
public class ModCard extends UiPart<Region> {
    private static final String FXML = "ModCard.fxml";
    public final Module module;

    @FXML
    private HBox cardPane;
    @FXML
    private Label moduleCode;
    @FXML
    private Label moduleName;

    public ModCard(Module module, int displayIndex) {
        super(FXML);
        this.module = module;
        moduleCode.setText(module.toString());
        moduleName.setText(module.getModuleName());
    }

    @FXML
    public void sayHi() {
        System.out.println("From Modcard " + module.toString());
    }

   // public ObservableList<Task> getFilteredList() {
   //     return module.getInternalTaskList();
   // }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModCard)) {
            return false;
        }
        // state check
        ModCard card = (ModCard) other;
        return moduleCode.getText().equals(card.moduleCode.getText())
                && module.equals(card.module);
    }



}
