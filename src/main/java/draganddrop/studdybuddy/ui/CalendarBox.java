package draganddrop.studdybuddy.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import draganddrop.studdybuddy.commons.core.LogsCenter;
import draganddrop.studdybuddy.model.task.Task;
import draganddrop.studdybuddy.model.task.UniqueTaskList;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Calendar. Still under construction.
 * Will cleanup code.
 */
public class CalendarBox extends UiPart<Region> {
    private static final String FXML = "Calendar.fxml";
    private final Logger logger = LogsCenter.getLogger(CalendarBox.class);
    private ObservableList<Task> taskList;
    private int calendarYear;
    private Month calendarMonth;
    private LocalDate localDate;
    private StackPane dueSoonListPanelPlaceholder;

    @FXML
    private Label month;

    @FXML
    private Label year;

    @FXML
    private GridPane monthBox;

    @FXML
    private Button prev;

    @FXML
    private Button next;

    public CalendarBox(ObservableList<Task> taskList, StackPane dueSoonListPanelPlaceholder) {
        super(FXML);
        this.taskList = taskList;
        Date date = new Date();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.dueSoonListPanelPlaceholder = dueSoonListPanelPlaceholder;
        generateCalendar(localDate.getYear(), localDate.getMonth());

        taskList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> t) {
                generateCalendar(calendarYear, calendarMonth);
            }
        });
    }

    /**
     * Generates a calendarBox with give year and month
     * @param calYear calendar year
     * @param calMonth calendar month
     */
    private void generateCalendar(int calYear, Month calMonth) {

        month.setText(calMonth.toString());
        year.setText(String.valueOf(calYear));
        calendarMonth = calMonth;
        calendarYear = calYear;

        Label label;
        LocalDate newDate = LocalDate.of(calYear, calMonth, 1);

        //day of week of first day
        int dayOfWeek = newDate.getDayOfWeek().getValue();

        for (int i = 1, j = 0; i <= newDate.lengthOfMonth(); i++) {
            if (dayOfWeek == 7) {
                dayOfWeek = 0;
                j++;
            }
            LocalDate tempDate = LocalDate.of(newDate.getYear(), newDate.getMonth(), i);
            VBox temp = new VBox();
            Pane p = new Pane();
            p.setBackground(new Background(new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY)));
            if (tempDate.equals(localDate)) {
                p.setBorder(new Border(new BorderStroke(Color.CYAN,
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
            monthBox.add(p, dayOfWeek, j);
            temp.getChildren().add(new Label(" " + i));
            int count = 0;
            for (Task task : taskList) {
                LocalDateTime[] ldt = task.getDateTimes();
                LocalDateTime tempTaskDueDate = ldt[0];
                LocalDate taskDueDate = LocalDate.of(tempTaskDueDate.getYear(),
                        tempTaskDueDate.getMonth(), tempTaskDueDate.getDayOfMonth());
                if (taskDueDate.equals(tempDate)) {
                    count++;
                }
            }
            if (count > 0) {
                Label dayTasksLabel = new Label();
                if (count == 1) {
                    dayTasksLabel.setText("Task: " + count);
                } else {
                    dayTasksLabel.setText("Tasks: " + count);
                }
                dayTasksLabel.setPadding(new Insets(0, 0, 0, 10));
                temp.getChildren().add(dayTasksLabel);
            }
            monthBox.add(temp, dayOfWeek, j);
            dayOfWeek++;
        }
    }

    /**
     * Generates calendar from previous month.
     */
    public void onClickPrevious() {
        monthBox.getChildren().clear();
        if (calendarMonth.getValue() == 1) {
            generateCalendar(this.calendarYear - 1, Month.DECEMBER);
        } else {
            generateCalendar(this.calendarYear, this.calendarMonth.minus(1));
        }
    }

    /**
     * Generates calendar for next month.
     */
    public void onClickNext() {
        monthBox.getChildren().clear();
        if (calendarMonth.getValue() == 12) {
            generateCalendar(this.calendarYear + 1, Month.JANUARY);
        } else {
            generateCalendar(this.calendarYear, this.calendarMonth.plus(1));
        }
    }

    /**
     * Home button. Generates calendar for current month.
     */
    public void onClickHome() {
        monthBox.getChildren().clear();
        generateCalendar(localDate.getYear(), localDate.getMonth());
    }

    /**
     * Generates task list when clicked on cell.
     * @param event mouse click event
     */
    public void onClickDate(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != monthBox) {
            Node parent = clickedNode.getParent();
            while (parent != monthBox) {
                clickedNode = parent;
                parent = clickedNode.getParent();
            }
            Integer colIndex = GridPane.getColumnIndex(clickedNode);
            Integer rowIndex = GridPane.getRowIndex(clickedNode);
            int firstDayOfWeek = LocalDate.of(calendarYear, calendarMonth, 1)
                    .getDayOfWeek().getValue();
            if (firstDayOfWeek == 7) {
                firstDayOfWeek = 0;
                rowIndex--;
            }
            int date = rowIndex * 7 + colIndex - firstDayOfWeek + 1;
            LocalDate clickedDate = LocalDate.of(calendarYear, calendarMonth, date);
            ObservableList<Task> taskByDay = generateTaskList(clickedDate);
            DueSoonListPanel dueSoonListPanel = new DueSoonListPanel(taskByDay);
            dueSoonListPanelPlaceholder.getChildren().clear();
            dueSoonListPanelPlaceholder.getChildren().add(dueSoonListPanel.getRoot());
            //System.out.println(clickedDate);
            //taskByDay.forEach(System.out::println);
        }

    }

    /**
     * @param date
     * @return
     */
    public ObservableList<Task> generateTaskList(LocalDate date) {
        UniqueTaskList taskByDay = new UniqueTaskList();
        for (Task task : taskList) {
            LocalDateTime[] ldt = task.getDateTimes();
            LocalDateTime tempTaskDueDate = ldt[0];
            LocalDate taskDueDate = LocalDate.of(tempTaskDueDate.getYear(),
                    tempTaskDueDate.getMonth(), tempTaskDueDate.getDayOfMonth());
            if (taskDueDate.equals(date)) {
                taskByDay.add(task);
            }
        }
        return taskByDay.asUnmodifiableObservableList();
    }

}
