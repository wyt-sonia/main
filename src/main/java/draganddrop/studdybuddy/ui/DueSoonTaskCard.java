package draganddrop.studdybuddy.ui;

import draganddrop.studdybuddy.logic.parser.TimeParser;
import draganddrop.studdybuddy.model.task.Task;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class DueSoonTaskCard extends UiPart<Region> {

    private static final String FXML = "DueSoonTaskListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
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
    private Label creationDateTime;
    @FXML
    private Label type;
    @FXML
    private Label id;
    @FXML
    private FlowPane tags;

    public DueSoonTaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        taskName.setText(task.getTaskName());
        //Module.setText(task.getModule().toString());
        timeLeft.setText(task.getTimeLeft());
        renderTask(task);
        dateTime.setText("\tDeadline: " + task.getTimeString());
        creationDateTime.setText("\tCreated: "
                + TimeParser.getDateTimeString(task.getCreationDateTime()));
        type.setText(task.getTaskType().toString());
    }

    /**
     * Applies different css calss to different task type and status.
     *
     * @param task
     */
    private void renderTask(Task task) {

        timeLeft.getStyleClass().add("time_left");

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
