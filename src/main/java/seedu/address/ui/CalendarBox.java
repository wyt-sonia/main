package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

public class CalendarBox extends UiPart<Region> {
    private static final String FXML = "Calendar.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);
    private final ObservableList<Task> taskList;

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

    public CalendarBox(ObservableList<Task> taskList) {
        super(FXML);
        this.taskList = taskList;
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month.setText(localDate.getMonth().toString());
        year.setText(String.valueOf(localDate.getYear()));

        Label label;

        //day of week of first day
        int dayOfWeek = localDate.withDayOfMonth(1).getDayOfWeek().getValue();

        for(int i = 1, j= 1; i<= localDate.lengthOfMonth(); i++) {
            if(dayOfWeek == 7) {
                dayOfWeek = 0;
                j++;
            }
            VBox temp = new VBox();
            label = new Label(" " + i);
            temp.getChildren().add(label);
            LocalDate tempDate = LocalDate.of(localDate.getYear(), localDate.getMonth(), i);
            for(Task task : taskList) {
                LocalDateTime[] ldt = task.getDateTimes();
                LocalDateTime tempTaskDueDate = ldt[0];
                LocalDate taskDueDate = LocalDate.of(tempTaskDueDate.getYear(), tempTaskDueDate.getMonth(), tempTaskDueDate.getDayOfMonth());
                if(taskDueDate.equals(tempDate)) {
                    temp.getChildren().add(new Label(task.getTaskName()));
                }
            }
            monthBox.add(temp, dayOfWeek, j);
            dayOfWeek++;
        }
    }
}
