package draganddrop.studybuddy.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class FeedbackDisplayBox extends UiPart<Region> {

    private static final String FXML = "FeedbackDisplayBox.fxml";

    @FXML
    private TextArea resultDisplay;

    public FeedbackDisplayBox() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }

}
