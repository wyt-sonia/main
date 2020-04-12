package draganddrop.studybuddy.ui.card;

import java.util.logging.Level;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.ui.UiPart;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * An UI component that displays the detail information of a {@code Task}.
 *
 * @@author wyt-sonia
 */
public class DetailedTaskCard extends UiPart<Region> {

    private static final String LOG_TAG = "DetailedTaskCard";
    private static final Logger logger = LogsCenter.getLogger(DetailedTaskCard.class);
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
        renderTaskCard(task, displayedIndex);
    }

    /**
     * Renders task's text information and badges.
     *
     * @param task
     */
    private void renderTaskCard(Task task, int displayedIndex) {
        logger.log(Level.INFO, LOG_TAG + ": Start to render task card.");
        renderTaskTextFields(task, displayedIndex);
        renderTaskBadges(task);
        logger.log(Level.INFO, LOG_TAG + ": End of rendering task card.");
    }

    /**
     * Renders task's text information.
     *
     * @param task
     * @param displayedIndex
     */
    private void renderTaskTextFields(Task task, int displayedIndex) {
        logger.log(Level.INFO, LOG_TAG + ": Start to render task text fields.");

        id.setText(displayedIndex + ". ");
        taskName.setText(task.getTaskName());
        description.setText("Task Description: " + task.getTaskDescription());
        weight.setText("Task Weight: " + task.getWeight() + "%");
        estimatedTimeCost.setText("Estimated Time Needed: " + task.getEstimatedTimeCost() + " hr/hrs");
        status.setText(task.getTaskStatus().convertToString());
        dateTime.setText("Deadline/Duration: " + task.getTimeString());
        creationDateTime.setText("Created at: "
            + TimeParser.getDateTimeString(task.getCreationDateTime()));
        type.setText(task.getTaskType().toString());

        logger.log(Level.INFO, LOG_TAG + ": End of rendering task text fields.");
    }

    /**
     * Renders task's badges.
     *
     * @param task
     */
    private void renderTaskBadges(Task task) {
        logger.log(Level.INFO, LOG_TAG + ": Start to render task badges.");

        renderTaskModuleBadges(task.getModule());
        renderTaskStatusBadges(task.getTaskStatus());
        renderTaskTypeBadges(task.getTaskType());

        logger.log(Level.INFO, LOG_TAG + ": End of rendering task badges.");
    }

    /**
     * Renders the task module badges according to {@code taskModule}.
     *
     * @param taskModule
     */
    private void renderTaskModuleBadges(Module taskModule) {
        logger.log(Level.INFO, LOG_TAG + ": Start to render task module badges.");

        // Omit module Badges if the task is not related to any module.
        if (taskModule.equals(new EmptyModule())) {
            module.setVisible(false);
            module.setManaged(false);
        } else {
            module.setText(taskModule.toString());
            module.getStyleClass().add("module_lbl");
        }

        logger.log(Level.INFO, LOG_TAG + ": End of rendering task module badges.");
    }


    /**
     * Renders the task status badges according to {@code taskStatus}.
     *
     * @param taskStatus
     */
    private void renderTaskStatusBadges(TaskStatus taskStatus) {
        logger.log(Level.INFO, LOG_TAG + ": Start to render task status badges.");

        switch (taskStatus) {
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
            status.getStyleClass().add("Overdue_status");
            break;
        default:
        }

        logger.log(Level.INFO, LOG_TAG + ": End of rendering task status badges.");
    }

    /**
     * Renders the task type badges according to {@code taskType}.
     *
     * @param taskType
     */
    private void renderTaskTypeBadges(TaskType taskType) {
        logger.log(Level.INFO, LOG_TAG + ": Start to render task type badges.");

        switch (taskType) {
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

        logger.log(Level.INFO, LOG_TAG + ": End of rendering task type badges.");
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
