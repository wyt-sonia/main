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
public class ProductivityPage extends UiPart<Region> {
    private static final String FXML = "ProductivityPage.fxml";
    private final Logger logger = LogsCenter.getLogger(ProductivityPage.class);
    private ObservableList<Task> taskList;

    @javafx.fxml.FXML
    private LineChart<Integer, Integer> productivityLineChart;

    public ProductivityPage(ObservableList<Task> taskList) {
        super(FXML);
        this.taskList = taskList;
        taskList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                generateProductivityPage();
            }
        });
    }
    /**
     * Sets up the line chart which shows the number of task completed over the week
     */
    private void setUpLineChart() {

        if (!productivityLineChart.getData().isEmpty()) {
            productivityLineChart.getData().clear();
        }

        productivityLineChart.getYAxis().setLabel("Task completed");
        productivityLineChart.getXAxis().setLabel("Day");
    }

    public void generateProductivityPage() {

    }
}
