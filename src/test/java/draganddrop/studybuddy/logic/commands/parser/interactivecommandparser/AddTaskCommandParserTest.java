package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.AddTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddTaskCommandException;
import draganddrop.studybuddy.model.task.TaskType;

public class AddTaskCommandParserTest {
    private AddTaskCommandParser parser = new AddTaskCommandParser();

    @Test
    public void parse_name_check() {
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseName("Thistasknameismorethan20characterslong"); });
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseName(""); });
        assertEquals("ValidName", parser.parseName("ValidName"));
    }

    @Test
    public void parse_description_check() {
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDescription("Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"); });
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDescription(""); });
        assertEquals("ValidDescription", parser.parseDescription("ValidDescription"));
    }

    @Test
    public void parse_date_check() {
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime("Thisisnotavaliddate", TaskType.Assignment); });
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime("", TaskType.Assignment); });
        String userInput = "18:0023/03/2000";
        //past date time
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Assignment); });
    }

    @Test
    public void parse_assignmentInvalidDate_check() {
        String userInput = "18:0023/03/2080";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Assignment); });
    }

    @Test
    public void parse_quizInvalidDate_check() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Quiz); });
    }

    @Test
    public void parse_examInvalidDate_check() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Exam); });
    }

    @Test
    public void parse_meetingInvalidDate_check() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Meeting); });
    }

    @Test
    public void parse_presentationInvalidDate_check() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Presentation); });
    }

    @Test
    public void parse_othersInvalidDate_check() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Others); });
    }

    @Test
    public void parse_invalidType_check() {
        String userInput = "10";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseType(userInput, TaskType.getTaskTypes().length); });
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseType("", TaskType.getTaskTypes().length); });
    }

    @Test
    public void parse_validType_check() {
        String userInput = "1";
        assertEquals(TaskType.Assignment,
            parser.parseType(userInput, TaskType.getTaskTypes().length));
    }

    @Test
    public void parse_weight_check() {
        String userInput = "1000.0";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseWeight(userInput); });
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseWeight(""); });
    }

    @Test
    public void parse_estimatedTime_check() {
        String userInput = "-6.0";
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseWeight(userInput); });
        assertThrows(AddTaskCommandException.class, () -> {
            parser.parseWeight(""); });
    }
}
