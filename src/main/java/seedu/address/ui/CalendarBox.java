package seedu.address.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.model.task.Task;

/**
 * Calendar. Still under construction.
 * Will cleanup code.
 */
public class CalendarBox extends UiPart<Region> {
    private static final String FXML = "Calendar.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

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
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month.setText(localDate.getMonth().toString());
        year.setText(String.valueOf(localDate.getYear()));

        Label label;

        //day of week of first day
        int dayOfWeek = localDate.withDayOfMonth(1).getDayOfWeek().getValue();

        for (int i = 1, j = 0; i <= localDate.lengthOfMonth(); i++) {
            if (dayOfWeek == 7) {
                dayOfWeek = 0;
                j++;
            }
            VBox temp = new VBox();
            Pane p = new Pane();
            p.setBackground(new Background(new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY)));
            monthBox.add(p, dayOfWeek, j);
            label = new Label(" " + i);
            temp.getChildren().add(label);
            LocalDate tempDate = LocalDate.of(localDate.getYear(), localDate.getMonth(), i);
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
}
