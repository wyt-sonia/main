package draganddrop.studdybuddy.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A ui for the due soon bar that is displayed in the application.
 */
public class DueSoonDisplay extends UiPart<Region> {

    private static final String FXML = "DueSoonDisplay.fxml";

    @FXML
    private TextArea dueSoonDisplay;

    public DueSoonDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        dueSoonDisplay.setText(feedbackToUser);
    }
}
