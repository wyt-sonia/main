package seedu.address.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Calendar. Still under construction.
 * Will cleanup code.
 */
public class CalendarBox extends UiPart<Region> {
    private static final String FXML = "Calendar.fxml";
    private final Logger logger = LogsCenter.getLogger(CalendarBox.class);
    private Logic logic;
    private int calendarYear;
    private Month calendarMonth;
    private LocalDate localDate;

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

    public CalendarBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        Date date = new Date();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        generateCalendar(localDate.getYear(), localDate.getMonth());
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
            label = new Label(" " + i);
            temp.getChildren().add(label);
            for (Task task : logic.getFilteredTaskList()) {
                LocalDateTime[] ldt = task.getDateTimes();
                LocalDateTime tempTaskDueDate = ldt[0];
                LocalDate taskDueDate = LocalDate.of(tempTaskDueDate.getYear(),
                        tempTaskDueDate.getMonth(), tempTaskDueDate.getDayOfMonth());
                if (taskDueDate.equals(tempDate)) {
                    temp.getChildren().add(new Label(task.getTaskName()));
                }
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
            System.out.println(clickedDate);
            System.out.println(taskByDay.get(0).getTaskName());
        }

    }


    public ObservableList<Task> generateTaskList(LocalDate date) {
        UniqueTaskList taskByDay = new UniqueTaskList();
        for (Task task : logic.getFilteredTaskList()) {
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
