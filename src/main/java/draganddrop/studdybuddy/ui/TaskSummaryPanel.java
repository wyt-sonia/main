package draganddrop.studdybuddy.ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import draganddrop.studdybuddy.commons.core.LogsCenter;
import draganddrop.studdybuddy.logic.parser.TimeParser;
import draganddrop.studdybuddy.model.task.Task;
import draganddrop.studdybuddy.model.task.TaskStatus;
import draganddrop.studdybuddy.model.task.TaskType;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Region;

/**
 * Panel containing the summary charts of tasks.
 */
public class TaskSummaryPanel extends UiPart<Region> {

    private static final String FXML = "TaskSummaryPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private ObservableList<Task> tempTasks = FXCollections.observableArrayList();
    private ObservableList<Task> archivedTasks;
    private ObservableList<Task> aliveTasks;

    @javafx.fxml.FXML
    private PieChart taskSummaryPieChart;

    @javafx.fxml.FXML
    private AreaChart taskSummaryAreaChart;

    @javafx.fxml.FXML
    private BarChart taskSummaryBarChart;

    @javafx.fxml.FXML
    private LineChart taskSummaryLineChart;

    public TaskSummaryPanel(ObservableList<Task> currentTaskList,
                            ObservableList<Task> archivedTaskList) {
        super(FXML);
        archivedTasks = archivedTaskList;
        aliveTasks = currentTaskList;
        tempTasks.addAll(currentTaskList);
        tempTasks.addAll(archivedTaskList);
        setUpAreaChart();
        setUpPieChart();
        setUpBarChart();
        setUpLineChart();

        currentTaskList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                tempTasks.clear();
                tempTasks.addAll(currentTaskList);
                tempTasks.addAll(archivedTaskList);
                setUpAreaChart();
                setUpPieChart();
                setUpBarChart();
                setUpLineChart();
            }
        });
    }

    /**
     * Aim to visualize the portion of different type, status etc.
     */
    private void setUpPieChart() {

        if (!taskSummaryPieChart.getData().isEmpty()) {
            taskSummaryPieChart.getData().clear();
        }
        for (TaskStatus ts : TaskStatus.getTaskStatusList()) {
            long count = tempTasks.stream().filter(t -> t.getTaskStatus().equals(ts)).count();
            taskSummaryPieChart.getData().add(new PieChart.Data(ts.convertToString(), count));
        }

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
     * Aim to compare time estimated and actual time used.
     */
    private void setUpAreaChart() {

        if (!taskSummaryAreaChart.getData().isEmpty()) {
            taskSummaryAreaChart.getData().clear();
        }

        XYChart.Series taskTypeDataSeries = new XYChart.Series();
        XYChart.Series taskStatusDataSeries = new XYChart.Series();
        taskTypeDataSeries.setName("Task Type");
        taskStatusDataSeries.setName("Task Status");

        Map<TaskType, Long> taskTypeAndCounts = tempTasks.stream()
            .collect(Collectors.groupingBy(Task::getTaskType, Collectors.counting()));

        Map<TaskStatus, Long> taskStatusAndCounts = tempTasks.stream()
            .collect(Collectors.groupingBy(Task::getTaskStatus, Collectors.counting()));

        taskTypeAndCounts.forEach((type, count) -> {
            taskTypeDataSeries.getData().add(new XYChart.Data(type.name(), count));
        });

        taskStatusAndCounts.forEach((status, count) -> {
            taskStatusDataSeries.getData().add(new XYChart.Data(status.convertToString(), count));
        });

        taskSummaryAreaChart.getData().addAll(taskStatusDataSeries, taskTypeDataSeries);
    }

    /**
     * Aim to compare different module.
     * Pending update to make it show different modules/semesters.
     */
    private void setUpBarChart() {

        if (!taskSummaryBarChart.getData().isEmpty()) {
            taskSummaryBarChart.getData().clear();
        }

        HashMap<String, XYChart.Series> moduleDataSeries = new HashMap<>();

        Map<String, Map<TaskType, Long>> currentTaskTypeAndCountUnderModules = tempTasks.stream()
            .collect(Collectors.groupingBy(t -> t.getModule().getModuleCode().toString(),
                Collectors.groupingBy(Task::getTaskType, Collectors.counting())
            ));

        currentTaskTypeAndCountUnderModules.forEach((moduleCode, typeAndCount) -> {
            XYChart.Series series;
            if (!moduleDataSeries.containsKey(moduleCode)) {
                series = new XYChart.Series();
                moduleDataSeries.put(moduleCode, series);
                series.setName(moduleCode);
            } else {
                series = moduleDataSeries.get(moduleCode);
            }
            typeAndCount.forEach((type, count) -> {
                series.getData().add(new XYChart.Data(type.name(), count));
            });
        });

        /*
        Map<Module, Map<TaskStatus, Long>> taskStatusAndCountUnderModules = taskList.stream()
            .collect(Collectors.groupingBy(Task::getModule,
                Collectors.groupingBy(Task::getTaskStatus, Collectors.counting())));

        taskStatusAndCountUnderModules.forEach((module, statusAndCount)->{
            XYChart.Series series = new XYChart.Series();
            series.setName(module.getModuleName());
            moduleDataSeries.add(series);

            statusAndCount.forEach((status, count) -> {
                series.getData().add(new XYChart.Data(status.convertToString(), count));
            });
        });
        */

        taskSummaryBarChart.getData().addAll(moduleDataSeries.values());
    }

    /**
     * Aims to show the changing of number of creation/ finishing/ due task of each day.
     */
    private void setUpLineChart() {

        if (!taskSummaryLineChart.getData().isEmpty()) {
            taskSummaryLineChart.getData().clear();
        }

        taskSummaryLineChart.getYAxis().setLabel("No of Tasks");
        taskSummaryLineChart.getXAxis().setLabel("Date & Time");

        XYChart.Series creationInfoSeries = new XYChart.Series();
        creationInfoSeries.setName("Task creation count");
        Map<LocalDate, Long> creationInfoList = tempTasks.stream()
            .collect(Collectors.groupingBy(t -> t.getCreationDateTime().toLocalDate(), Collectors.counting()));
        creationInfoList.forEach((date, count) -> {
            creationInfoSeries.getData().add(new XYChart.Data(TimeParser.getDateString(date), count));
        });

        XYChart.Series dueInfoSeries = new XYChart.Series();
        dueInfoSeries.setName("Task deadline/start date count");
        Map<LocalDate, Long> dueInfoList = tempTasks.stream()
            .collect(Collectors.groupingBy(t -> t.getDateTimes()[0].toLocalDate(), Collectors.counting()));
        dueInfoList.forEach((date, count) -> {
            dueInfoSeries.getData().add(new XYChart.Data(TimeParser.getDateString(date), count));
        });

        XYChart.Series finishInfoSeries = new XYChart.Series();
        finishInfoSeries.setName("Task completed count");
        Map<LocalDate, Long> finishInfoList = archivedTasks.stream()
            .collect(Collectors.groupingBy(t -> t.getCreationDateTime().toLocalDate(), Collectors.counting()));
        finishInfoList.forEach((date, count) -> {
            finishInfoSeries.getData().add(new XYChart.Data(TimeParser.getDateString(date), count));
        });

        taskSummaryLineChart.getData().addAll(creationInfoSeries, dueInfoSeries, finishInfoSeries);
    }


}
