package draganddrop.studybuddy.ui.card;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.ui.UiPart;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class DueSoonTaskCard extends UiPart<Region> {

    private static final String FXML = "DueSoonTaskCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     */

    public final Task task;

    @FXML
    private HBox cardPane;
    @FXML
    private Label taskName;
    //@FXML
    //private Label Module;
    @FXML
    private Label timeLeft;
    @FXML
    private Label dateTime;
    @FXML
    private Label weightAndModule;
    @FXML
    private Label creationDateTime;
    @FXML
    private Label type;
    @FXML
    private Label id;
    @FXML
    private HBox tags;

    public DueSoonTaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        taskName.setText(task.getTaskName());
        //Module.setText(task.getModule().toString());
        timeLeft.setText(task.getTimeLeft());
        renderTask(task);
        dateTime.setText("Deadline/Start at: " + task.getTimeString());
        creationDateTime.setText("Created at: "
            + TimeParser.getDateTimeString(task.getCreationDateTime()));
        type.setText(task.getTaskType().toString());

        if (task.getModule().equals(new EmptyModule())) {
            weightAndModule.setVisible(false);
            weightAndModule.setManaged(false);
        } else {
            weightAndModule.setText(task.getWeight() + "% " + task.getModule().toString());
        }
    }

    /**
     * Applies different css calss to different task type and status.
     *
     * @param task
     */
    private void renderTask(Task task) {

        switch (task.getTaskType()) {
        case Assignment:
            type.getStyleClass().add("assignment_lbl");
            break;
        case Quiz:
            type.getStyleClass().add("quiz_lbl");
            break;
        case Exam:
            type.getStyleClass().add("exam_lbl");
            break;
        case Meeting:
            type.getStyleClass().add("meeting_lbl");
            break;
        case Presentation:
            type.getStyleClass().add("presentation_lbl");
            break;
        case Others:
            type.getStyleClass().add("others_lbl");
            break;
        default:
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DueSoonTaskCard)) {
            return false;
        }

        // state check
        DueSoonTaskCard card = (DueSoonTaskCard) other;
        return id.getText().equals(card.id.getText())
            && task.equals(card.task);
    }
}
