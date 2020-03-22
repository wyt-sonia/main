package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;
import seedu.address.model.task.TaskType;

/**
 * Panel containing the summary charts of tasks.
 */
public class TaskSummaryPanel extends UiPart<Region> {

    private static final String FXML = "TaskSummaryPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @javafx.fxml.FXML
    private PieChart taskTypeSummary;

    @javafx.fxml.FXML
    private PieChart taskStatusSummary;

    public TaskSummaryPanel(ObservableList<Task> taskList) {
        super(FXML);

        for (TaskStatus ts : TaskStatus.getTaskStatusList()) {
            long count = taskList.stream().filter(t -> t.getTaskStatus().equals(ts)).count();
            taskStatusSummary.getData().add(new PieChart.Data(ts.convertToString(), count));
        }

        for (TaskType tt : TaskType.getTaskTypes()) {
            long count = taskList.stream().filter(t -> t.getTaskType().equals(tt)).count();
            taskTypeSummary.getData().add(new PieChart.Data(tt.name(), count));
        }
    }
}
