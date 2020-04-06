package draganddrop.studybuddy.ui.panel;

import static java.time.temporal.ChronoUnit.DAYS;

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
import javafx.scene.control.TabPane;
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
    private ObservableList<Module> modules;
    private ObservableList<Task> selectedTasks;
    private StackPane selectedTaskListPanelPlaceholder;
    private Label selectedTaskListPanelTitle;

    @javafx.fxml.FXML
    private TabPane chartsTabPane;

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

        // render nodes.

        logger.fine(FXML + ": Start to render charts from task summary panel.");
        renderCharts();
        bindCharts(observableCurrentTasks, observableArchivedTasks, observableModules);
        this.selectedTaskListPanelPlaceholder = selectedTaskListPanelPlaceholder;
        this.selectedTaskListPanelTitle = selectedTaskListPanelTitle;
        selectedTaskListPanelPlaceholder.setBackground(
            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        renderSelectedListPanel();

        // set up the listener for each chart.
        logger.fine(FXML + ": Start to set onchange listener for data source.");
        observableCurrentTasks.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                renderCharts();
                bindCharts(observableCurrentTasks, observableArchivedTasks, observableModules);
            }
        });

        observableArchivedTasks.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> c) {
                renderCharts();
                bindCharts(observableCurrentTasks, observableArchivedTasks, observableModules);
            }
        });

        observableModules.addListener(new ListChangeListener<Module>() {
            @Override
            public void onChanged(Change<? extends Module> c) {
                renderCharts();
                bindCharts(observableCurrentTasks, observableArchivedTasks, observableModules);
            }
        });
        logger.fine(FXML + ": End of setting onchange listener for data source.");
        logger.fine(FXML + ": End of rendering charts from task summary panel.");
    }

    /**
     * Binds data and set up charts.
     *
     * @param observableCurrentTasks
     * @param observableArchivedTasks
     * @param observableModules
     */
    private void bindCharts(ObservableList<Task> observableCurrentTasks,
                            ObservableList<Task> observableArchivedTasks, ObservableList<Module> observableModules) {

        logger.fine(FXML + ": Start to bind data to charts.");
        if (tempTasks != null) {
            tempTasks.clear();
        }

        modules = observableModules;
        tempTasks.addAll(observableCurrentTasks);
        tempTasks.addAll(observableArchivedTasks);

        setUpPieChart();
        setUpAreaChart();
        setUpBarChart();
        logger.fine(FXML + ": End of binding data to charts.");
    }

    /**
     * Renders the selected task list panel accordingly.
     */
    public void renderSelectedListPanel() {
        logger.fine(FXML + ": Start to render selected task list panel.");
        selectedTaskListPanelPlaceholder.getChildren().clear();
        logger.fine(FXML + ": End of rendering selected task list panel.");
    }

    /**
     * Renders the charts.
     */
    private void renderCharts() {
        // Pie chart
        taskSummaryPieChart.setTitle("Summary of Tasks' Status");

        // Area chart
        taskSummaryAreaChart.setTitle("Summary of Module Related Tasks' Deadline/Start Date");
        taskSummaryAreaChart.getYAxis().setLabel("No of Tasks");
        taskSummaryAreaChart.getYAxis().setAutoRanging(true);
        taskSummaryAreaChart.getXAxis().setLabel("Deadline/Start Date");

        // Bar chart
        taskSummaryStackedBarChart.setTitle("Summary of Module Related Tasks' Weight");
        taskSummaryStackedBarChart.getYAxis().setLabel("Weight %");
        taskSummaryStackedBarChart.getYAxis().setAutoRanging(false);
        taskSummaryStackedBarChart.getXAxis().setLabel("Modules");
    }

    /**
     * Sets up the pie chart's pie chart and data.
     * The pie chart is used to show the summary of numbers of different tasks' statuses.
     */
    private void setUpPieChart() {
        logger.fine(FXML + " PieChart: Start to sets up the pie chart's pie chart and data.");
        ArrayList<PieChart.Data> datas = new ArrayList<>();
        if (!taskSummaryPieChart.getData().isEmpty()) {
            taskSummaryPieChart.getData().clear();
        }
        logger.fine(FXML + " PieChart: Start to bind data.");
        for (TaskStatus ts : TaskStatus.getTaskStatusList()) {
            long count = tempTasks.stream().filter(t -> t.getTaskStatus().equals(ts)).count();
            PieChart.Data tempData = new PieChart.Data(ts.convertToString(), count);
            datas.add(tempData);
            taskSummaryPieChart.getData().add(tempData);
        }
        logger.fine(FXML + " PieChart: End of binding bind data.");

        logger.fine(FXML + " PieChart: Start to set up the pie chart's pie chart and data.");
        datas.forEach(d -> d.getNode().setOnMouseClicked(e -> {
            String statusName = d.getName().split(":")[0].trim();
            selectedTasks = tempTasks.filtered(task ->
                task.getTaskStatus().equals(TaskStatus.getStatus(statusName)));
            TaskListPanel taskListPanel = new TaskListPanel(selectedTasks);
            selectedTaskListPanelTitle.setText(d.getName().toUpperCase() + " Tasks");
            selectedTaskListPanelPlaceholder.getChildren().clear();
            selectedTaskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());
        }));
        logger.fine(FXML + " PieChart: End of setting up the pie chart's pie chart and data.");

        taskSummaryPieChart.getData().forEach(data ->
            data.nameProperty().bind(
                Bindings.concat(
                    data.getName(), " : ", data.pieValueProperty().longValue()
                )
            )
        );
        logger.fine(FXML + " PieChart: End of setting up the pie chart's pie chart and data.");
    }

    /**
     * Sets up the area chart's data and onclick action.
     * The area chart is used to show summary of the module related dues/start dates.
     * Restricted to the future half year.
     */
    private void setUpAreaChart() {

        logger.fine(FXML + " AreaChart: Start to set up the area chart's data and onclick action.");
        if (!taskSummaryAreaChart.getData().isEmpty()) {
            taskSummaryAreaChart.getData().clear();
        }
        ArrayList<XYChart.Data<String, Number>> datas = new ArrayList<>();
        ArrayList<XYChart.Series<String, Number>> dataSeries = new ArrayList<>();

        ObservableList<Task> sortedTasks = tempTasks.sorted(Comparator.comparing(t -> t.getDateTimes()[0]));
        logger.fine(FXML + " AreaChart: Start to bind data.");
        if (!sortedTasks.isEmpty()) {

            // to restrict the source of data to the next 2 months
            LocalDate startDate = LocalDate.now();
            ObservableList<Task> filteredTasks = sortedTasks
                .filtered(task -> task.getDateTimes()[0].isBefore(startDate.plusDays(2 * 30).atStartOfDay()));
            LocalDate endDate = filteredTasks.get(filteredTasks.size() - 1).getDateTimes()[0].toLocalDate();

            if (DAYS.between(startDate, endDate) > 30 * 2) {
                endDate = startDate.plusMonths(2);
                taskSummaryAreaChart.getXAxis().setAutoRanging(true);
            }
            LocalDate finalEndDate = endDate;

            modules.forEach(m -> {
                XYChart.Series<String, Number> dueDateDataSeries = new XYChart.Series<>();
                if (m.getModuleCode().equals(new EmptyModule().getModuleCode())) {
                    dueDateDataSeries.setName("Not Module Related");
                } else {
                    dueDateDataSeries.setName(m.getModuleCode().toString());
                }

                for (LocalDate d = startDate; d.isBefore(finalEndDate.plusDays(1)); d = d.plusDays(1)) {
                    LocalDate finalD = d;
                    long numOfTasksDue = tempTasks.stream()
                        .filter(t ->
                            t.getDateTimes()[0].toLocalDate().equals(finalD) && t.getModule().equals(m)).count();
                    XYChart.Data<String, Number> tempData =
                        new XYChart.Data<>(TimeParser.getDateString(d), numOfTasksDue);
                    tempData.setExtraValue(m.getModuleCode());
                    dueDateDataSeries.getData().add(tempData);
                    datas.add(tempData);
                }
                dataSeries.add(dueDateDataSeries);
            });
            taskSummaryAreaChart.getData().addAll(dataSeries);
            logger.fine(FXML + " AreaChart: End of binding data.");

            logger.fine(FXML + " AreaChart: Start to set up the on click action for each data area.");
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
            logger.fine(FXML + " AreaChart: End of setting up the on click action for each data area.");

            logger.fine(FXML + " AreaChart: Start to set up the on click action for each data point.");
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
            logger.fine(FXML + " AreaChart: End of setting up the on click action for each data point.");
        }
        logger.fine(FXML + " AreaChart: End of setting up the area chart's data and onclick action.");
    }

    /**
     * Sets up the stacked bar chart's data and onclick action.
     * The bar chart is used to show the summary of module related weight status of different task types.
     */
    private void setUpBarChart() {

        logger.fine(FXML + " BarChart: Start to set up the stacked bar chart's data and onclick action.");
        ArrayList<XYChart.Data<String, Number>> datas = new ArrayList<>();
        ArrayList<XYChart.Series<String, Number>> dataSeries = new ArrayList<>();

        if (!taskSummaryStackedBarChart.getData().isEmpty()) {
            taskSummaryStackedBarChart.getData().clear();
        }
        logger.fine(FXML + " BarChart: Start to bind data.");
        for (TaskType taskType : Arrays.asList(TaskType.getTaskTypes())) {
            XYChart.Series<String, Number> weightDataSeries = new XYChart.Series<>();
            weightDataSeries.setName(taskType.toString());
            modules.forEach(m -> {
                if (!m.equals(new EmptyModule())) {
                    ObservableList<Task> filteredTasks = tempTasks
                        .filtered(t -> t.getModule().equals(m) && t.getTaskType().equals(taskType));
                    XYChart.Data<String, Number> tempData = new XYChart.Data<>(m.getModuleCode().toString(),
                        filteredTasks.stream().mapToDouble(Task::getWeight).sum());
                    tempData.setExtraValue(m.getModuleCode().toString() + "//" + taskType.toString());
                    weightDataSeries.getData().add(tempData);
                    datas.add(tempData);
                }
            });
            dataSeries.add(weightDataSeries);
        }

        taskSummaryStackedBarChart.getData().addAll(dataSeries);
        logger.fine(FXML + " BarChart: End of binding data.");

        logger.fine(FXML + " BarChart: Start to setting up the on click action for each data area");
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
        logger.fine(FXML + " BarChart: End of setting up the on click action for each data area");
        logger.fine(FXML + " BarChart: End of setting up the stacked bar chart's data and onclick action.");
    }
}
