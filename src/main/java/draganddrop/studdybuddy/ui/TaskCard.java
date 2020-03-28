package draganddrop.studdybuddy.ui;

import draganddrop.studdybuddy.logic.parser.TimeParser;
import draganddrop.studdybuddy.model.task.Task;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
<<<<<<< HEAD:src/main/java/seedu/address/ui/TaskCard.java
import seedu.address.logic.parser.TimeParser;
import seedu.address.model.module.EmptyModule;
import seedu.address.model.task.Task;
=======
>>>>>>> 59df6a049d5ca9c9b37e32113b04c7493b0d395b:src/main/java/draganddrop/studdybuddy/ui/TaskCard.java

/**
 * An UI component that displays information of a {@code Person}.
 */
public class TaskCard extends UiPart<Region> {

    private static final String FXML = "TaskListCard.fxml";

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
    @FXML
    private Label module;
    @FXML
    private Label status;
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


    public TaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        taskName.setText(task.getTaskName());

        if (task.getModule().equals(new EmptyModule())) {
            module.setVisible(false);
            module.setManaged(false);
        } else {
            module.setText(task.getModule().toString());
        }

        status.setText(task.getTaskStatus().convertToString());
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
        switch (task.getTaskStatus()) {
        case PENDING:
            status.getStyleClass().add("pending_status");
            break;
        case FINISHED:
            status.getStyleClass().add("done_status");
            break;
        case DUE_SOON:
            status.getStyleClass().add("due_soon_status");
            break;
        case OVERDUE:
            status.getStyleClass().add("overdue_status");
            break;
        default:
        }

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
        if (!(other instanceof TaskCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return id.getText().equals(card.id.getText())
            && task.equals(card.task);
    }
}
