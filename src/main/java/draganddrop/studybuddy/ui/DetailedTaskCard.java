package draganddrop.studybuddy.ui;

import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.task.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class DetailedTaskCard extends UiPart<Region> {

    private static final String FXML = "DetailedTaskCard.fxml";

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
    private Label weight;
    @FXML
    private Label estimatedTimeCost;
    @FXML
    private Label description;
    @FXML
    private Label dateTime;
    @FXML
    private Label creationDateTime;
    @FXML
    private Label type;
    @FXML
    private Label id;
    @FXML
    private HBox tags;


    public DetailedTaskCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        taskName.setText(task.getTaskName());
        description.setText("Task Description: " + task.getTaskDescription());
        weight.setText("Task Weight: " + task.getWeight() + "%");
        estimatedTimeCost.setText("Estimated Time Needed: " + task.getEstimatedTimeCost() + " hr/hrs");

        if (task.getModule().equals(new EmptyModule())) {
            module.setVisible(false);
            module.setManaged(false);
        } else {
            module.setText(task.getModule().toString());
            module.getStyleClass().add("module_lbl");
        }

        status.setText(task.getTaskStatus().convertToString());
        renderTask(task);
        dateTime.setText("Deadline/Duration: " + task.getTimeString());
        creationDateTime.setText("Created at: "
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
        if (!(other instanceof DetailedTaskCard)) {
            return false;
        }

        // state check
        DetailedTaskCard detailedTaskCard = (DetailedTaskCard) other;
        return id.getText().equals(detailedTaskCard.id.getText())
            && task.equals(detailedTaskCard.task);
    }
}
