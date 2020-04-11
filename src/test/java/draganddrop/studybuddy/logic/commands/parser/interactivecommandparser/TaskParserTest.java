package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.TaskParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.model.task.TaskType;

public class TaskParserTest {
    private TaskParser parser = new TaskParser();

    @Test
    public void taskParserTestTestParseNameCheck() {
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseName("Thistasknameismorethan20characterslong"); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseName(""); });
        assertEquals("ValidName", parser.parseName("ValidName"));
    }

    @Test
    public void taskParserTestTestParseDescriptionCheck() {
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDescription("Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDescription(""); });
        assertEquals("ValidDescription", parser.parseDescription("ValidDescription"));
    }

    @Test
    public void taskParserTestTestParseDateCheck() {
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime("Thisisnotavaliddate", TaskType.Assignment); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime("", TaskType.Assignment); });
        String userInput = "18:0023/03/2000";
        //past date time
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Assignment); });
    }

    @Test
    public void taskParserTestTestParseAssignmentInvalidDateCheck() {
        String userInput = "18:0023/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Assignment); });
    }

    @Test
    public void taskParserTestTestParseQuizInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Quiz); });
    }

    @Test
    public void taskParserTestTestParseExamInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Exam); });
    }

    @Test
    public void taskParserTestTestParseMeetingInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Meeting); });
    }

    @Test
    public void taskParserTestTestParsePresentationInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Presentation); });
    }

    @Test
    public void taskParserTestTestParseOthersInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseDateTime(userInput, TaskType.Others); });
    }

    @Test
    public void taskParserTestTestParseInvalidTypeCheck() {
        String userInput = "10";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseType(userInput, TaskType.getTaskTypes().length); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseType("", TaskType.getTaskTypes().length); });
    }

    @Test
    public void taskParserTestTestParseValidTypeCheck() {
        String userInput = "1";
        assertEquals(TaskType.Assignment,
            parser.parseType(userInput, TaskType.getTaskTypes().length));
    }

    @Test
    public void taskParserTestTestParseWeightCheck() {
        String userInput = "1000.0";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseWeight(userInput); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseWeight(""); });
    }

    @Test
    public void taskParserTestTestParseEstimatedTimeCheck() {
        String userInput = "-6.0";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseWeight(userInput); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parser.parseWeight(""); });
    }
}
