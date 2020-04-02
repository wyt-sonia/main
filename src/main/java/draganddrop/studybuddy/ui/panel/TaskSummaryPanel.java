package draganddrop.studybuddy.ui.panel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import draganddrop.studybuddy.commons.core.LogsCenter;
import draganddrop.studybuddy.logic.parser.TimeParser;
import draganddrop.studybuddy.model.module.EmptyModule;
import draganddrop.studybuddy.model.module.Module;
import draganddrop.studybuddy.model.task.Task;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;
import draganddrop.studybuddy.ui.UiPart;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Panel containing the summary charts of tasks.
 */
public class TaskSummaryPanel extends UiPart<Region> {

    private static final String FXML = "TaskSummaryPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private ObservableList<Task> tempTasks = FXCollections.observableArrayList();
    private ObservableList<Task> archivedTasks;
    private ObservableList<Task> aliveTasks;
    private ObservableList<Module> modules;
    private ObservableList<Task> selectedTasks;
    private StackPane selectedTaskListPanelPlaceholder;
    private Label selectedTaskListPanelTitle;

    @javafx.fxml.FXML
    private PieChart taskSummaryPieChart;

    @javafx.fxml.FXML
    private AreaChart taskSummaryAreaChart;

    @javafx.fxml.FXML
    private StackedBarChart taskSummaryStackedBarChart;

    public TaskSummaryPanel(ObservableList<Task> observableCurrentTasks,
                            ObservableList<Task> observableArchivedTasks, ObservableList<Module> observableModules,
                            StackPane selectedTaskListPanelPlaceholder,
                            Label selectedTaskListPanelTitle) {
        super(FXML);
        archivedTasks = observableArchivedTasks;
        aliveTasks = observableCurrentTasks;
        modules = observableModules;
        tempTasks.addAll(observableCurrentTasks);
        tempTasks.addAll(observableArchivedTasks);
        this.selectedTaskListPanelPlaceholder = selectedTaskListPanelPlaceholder;
        this.selectedTaskListPanelTitle = selectedTaskListPanelTitle;
        selectedTaskListPanelPlaceholder.setBackground(
            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        if (tempTasks != null && tempTasks.size() > 0) {
            setUpAreaChart();
            setUpPieChart();
            setUpBarChart();
        }

        observableCurrentTasks.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                tempTasks.clear();
                tempTasks.addAll(observableCurrentTasks);
                tempTasks.addAll(observableArchivedTasks);
                setUpAreaChart();
                setUpPieChart();
                setUpBarChart();
            }
        });

        observableModules.addListener(new ListChangeListener<Module>() {
            @Override
            public void onChanged(Change<? extends Module> c) {
                modules = observableModules;
                setUpAreaChart();
            }
        });
    }

    /**
     * Renders the selected task list panel accordingly.
     */
    public void renderSelectedListPanel() {
        selectedTaskListPanelTitle.getStyleClass().add("summary_sub_header");
        selectedTaskListPanelTitle.setText("Click on Chart to View Related Tasks");
        selectedTaskListPanelPlaceholder.getChildren().clear();
    }

    /**
     * Aim to visualize the portion of different type, status etc.
     */
    private void setUpPieChart() {
        ArrayList<PieChart.Data> datas = new ArrayList<>();
        if (!taskSummaryPieChart.getData().isEmpty()) {
            taskSummaryPieChart.getData().clear();
        }
        for (TaskStatus ts : TaskStatus.getTaskStatusList()) {
            long count = tempTasks.stream().filter(t -> t.getTaskStatus().equals(ts)).count();
            PieChart.Data tempData = new PieChart.Data(ts.convertToString(), count);
            datas.add(tempData);
            taskSummaryPieChart.getData().add(tempData);
        }
        datas.forEach(d -> d.getNode().setOnMouseClicked(e -> {
            String statusName = d.getName().split(":")[0].trim();
            selectedTasks = tempTasks.filtered(task ->
                task.getTaskStatus().equals(TaskStatus.getStatus(statusName)));
            TaskListPanel taskListPanel = new TaskListPanel(selectedTasks);
            selectedTaskListPanelTitle.setText(d.getName().toUpperCase() + " Tasks");
            selectedTaskListPanelPlaceholder.getChildren().clear();
            selectedTaskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());
        }));
        taskSummaryPieChart.setTitle("Summary of Tasks' Status");
        taskSummaryPieChart.getData().forEach(data ->
            data.nameProperty().bind(
                Bindings.concat(
                    data.getName(), " : ", data.pieValueProperty().longValue()
                )
            )
        );
    }

    /**
     * Shows the changing of number of dues.
     */
    private void setUpAreaChart() {
        taskSummaryAreaChart.setTitle("Summary of Module Related Tasks' Deadline/Start Date");
        taskSummaryAreaChart.getYAxis().setLabel("No of Tasks");
        taskSummaryAreaChart.getYAxis().setAutoRanging(true);
        taskSummaryAreaChart.getXAxis().setLabel("Deadline/Start Date");

        if (!taskSummaryAreaChart.getData().isEmpty()) {
            taskSummaryAreaChart.getData().clear();
        }
        ArrayList<XYChart.Data> datas = new ArrayList<>();
        ArrayList<XYChart.Series> dataSeries = new ArrayList<>();

        ObservableList<Task> sortedTasks = tempTasks.sorted(Comparator.comparing(t -> t.getDateTimes()[0]));
        LocalDate startDate = sortedTasks.get(0).getDateTimes()[0].toLocalDate();
        LocalDate endDate = sortedTasks.get(sortedTasks.size() - 1).getDateTimes()[0].toLocalDate();

        modules.forEach(m -> {
            XYChart.Series dueDateDataSeries = new XYChart.Series();
            if (m.getModuleCode().equals(new EmptyModule().getModuleCode())) {
                dueDateDataSeries.setName("Not Module Related");
            } else {
                dueDateDataSeries.setName(m.getModuleCode().toString());
            }

            for (LocalDate d = startDate; d.isBefore(endDate.plusDays(1)); d = d.plusDays(1)) {
                LocalDate finalD = d;
                long numOfTasksDue = tempTasks.stream()
                    .filter(t -> t.getDateTimes()[0].toLocalDate().equals(finalD) && t.getModule().equals(m)).count();
                XYChart.Data tempData = new XYChart.Data(TimeParser.getDateString(d), numOfTasksDue);
                tempData.setExtraValue(m.getModuleCode());
                dueDateDataSeries.getData().add(tempData);
                datas.add(tempData);
            }
            dataSeries.add(dueDateDataSeries);
        });

        taskSummaryAreaChart.getData().addAll(dataSeries);

        datas.forEach(d -> d.getNode().setOnMouseClicked(e -> {
            String moduleCode = d.getExtraValue().toString();
            String dateString = d.getXValue().toString();
            LocalDate date = TimeParser.parseDate(dateString);
            selectedTasks = tempTasks.filtered(task ->
                task.getModule().getModuleCode().toString().equals(moduleCode)
                    && task.getDateTimes()[0].toLocalDate().isEqual(date));
            TaskListPanel taskListPanel = new TaskListPanel(selectedTasks);
            if (moduleCode.equals(new EmptyModule().getModuleCode().toString())) {
                selectedTaskListPanelTitle.setText("Tasks due/start on " + dateString);
            } else {
                selectedTaskListPanelTitle.setText("Tasks of " + moduleCode + " due/start on " + dateString);
            }
            selectedTaskListPanelPlaceholder.getChildren().clear();
            selectedTaskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());
        }));

        dataSeries.forEach(d -> d.getNode().setOnMouseClicked(e -> {
            String moduleCode = d.getName().equals("Not Module Related")
                ? new EmptyModule().getModuleCode().toString() : d.getName();
            selectedTasks = tempTasks.filtered(task ->
                task.getModule().getModuleCode().toString().equals(moduleCode));
            TaskListPanel taskListPanel = new TaskListPanel(selectedTasks);
            if (moduleCode.equals(new EmptyModule().getModuleCode().toString())) {
                selectedTaskListPanelTitle.setText("Tasks without related module");
            } else {
                selectedTaskListPanelTitle.setText("Tasks under " + moduleCode);
            }
            selectedTaskListPanelPlaceholder.getChildren().clear();
            selectedTaskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());
        }));

    }

    /**
     * Aim to compare different module.
     * Pending update to make it show different modules/semesters.
     */
    private void setUpBarChart() {

        ArrayList<XYChart.Data> datas = new ArrayList<>();
        ArrayList<XYChart.Series> dataSeries = new ArrayList<>();

        taskSummaryStackedBarChart.setTitle("Summary of Module Related Tasks' Weight");
        taskSummaryStackedBarChart.getYAxis().setLabel("Weight %");
        taskSummaryStackedBarChart.getYAxis().setAutoRanging(false);
        taskSummaryStackedBarChart.getXAxis().setLabel("Modules");

        if (!taskSummaryStackedBarChart.getData().isEmpty()) {
            taskSummaryStackedBarChart.getData().clear();
        }

        for (TaskType taskType : Arrays.asList(TaskType.getTaskTypes())) {
            XYChart.Series weightDataSeries = new XYChart.Series();
            weightDataSeries.setName(taskType.toString());
            modules.forEach(m -> {
                if (!m.equals(new EmptyModule())) {
                    ObservableList<Task> filteredTasks = tempTasks
                        .filtered(t -> t.getModule().equals(m) && t.getTaskType().equals(taskType));
                    XYChart.Data tempData = new XYChart.Data(m.getModuleCode().toString(),
                        filteredTasks.stream().mapToDouble(Task::getWeight).sum());
                    tempData.setExtraValue(m.getModuleCode().toString() + "//" + taskType.toString());
                    weightDataSeries.getData().add(tempData);
                    datas.add(tempData);
                }
            });
            dataSeries.add(weightDataSeries);
        }

        taskSummaryStackedBarChart.getData().addAll(dataSeries);

        datas.forEach(d -> {
            String moduleCode = (d.getExtraValue().toString().split("//"))[0];
            String taskType = (d.getExtraValue().toString().split("//"))[1];
            d.getNode().setOnMouseClicked(e -> {
                selectedTasks = tempTasks
                    .filtered(task -> task.getModule().getModuleCode().toString().equals(moduleCode)
                        && task.getTaskType().toString().equals(taskType));
                TaskListPanel taskListPanel = new TaskListPanel(selectedTasks);
                selectedTaskListPanelTitle.setText(taskType + " under " + moduleCode);
                selectedTaskListPanelPlaceholder.getChildren().clear();
                selectedTaskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());
            });
        });
    }
}
