package draganddrop.studybuddy.ui;

import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.model.task.Task;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.Region;

/**
 * Profile page that contains usage statistics
 */
public class ProfilePage extends UiPart<Region> {
    private static final String FXML = "ProfilePage.fxml";
    private final Logger logger = LogsCenter.getLogger(ProfilePage.class);
    private ObservableList<Task> taskList;

    @javafx.fxml.FXML
    private LineChart<Integer, Integer> profileLineChart;

    public ProfilePage(ObservableList<Task> taskList) {
        super(FXML);
        this.taskList = taskList;
        taskList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                generateProfilePage();
            }
        });
    }
    /**
     * Sets up the line chart which shows the number of task completed over the week
     */
    private void setUpLineChart() {

        if (!profileLineChart.getData().isEmpty()) {
            profileLineChart.getData().clear();
        }

        profileLineChart.getYAxis().setLabel("Task completed");
        profileLineChart.getXAxis().setLabel("Day");
    }

    public void generateProfilePage() {

    }
}
