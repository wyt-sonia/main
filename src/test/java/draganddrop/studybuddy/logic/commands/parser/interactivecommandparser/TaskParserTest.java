package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static draganddrop.studybuddy.logic.parser.TaskParser.parseDateTime;
import static draganddrop.studybuddy.logic.parser.TaskParser.parseDescription;
import static draganddrop.studybuddy.logic.parser.TaskParser.parseName;
import static draganddrop.studybuddy.logic.parser.TaskParser.parseType;
import static draganddrop.studybuddy.logic.parser.TaskParser.parseWeight;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.AddOrEditTaskCommandException;
import draganddrop.studybuddy.model.task.TaskType;

/**
 * This is the test class for Task Parser
 *
 * @@author Souwmyaa Sabarinathann
 */
public class TaskParserTest {

    @Test
    public void taskParserTestTestParseNameCheck() {
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseName("Thistasknameismorethan20characterslong"); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseName(""); });
        assertEquals("ValidName", parseName("ValidName"));
    }

    @Test
    public void taskParserTestTestParseDescriptionCheck() {
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDescription("Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"
                    + "Thistaskdescriptionismorethan300characterslong"); });
        assertEquals("ValidDescription", parseDescription("ValidDescription"));
    }

    @Test
    public void taskParserTestTestParseDateCheck() {
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime("Thisisnotavaliddate", TaskType.Assignment); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime("", TaskType.Assignment); });
        String userInput = "18:0023/03/2000";
        //past date time
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime(userInput, TaskType.Assignment); });
    }

    @Test
    public void taskParserTestTestParseAssignmentInvalidDateCheck() {
        String userInput = "18:0023/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime(userInput, TaskType.Assignment); });
    }

    @Test
    public void taskParserTestTestParseQuizInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime(userInput, TaskType.Quiz); });
    }

    @Test
    public void taskParserTestTestParseExamInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime(userInput, TaskType.Exam); });
    }

    @Test
    public void taskParserTestTestParseMeetingInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime(userInput, TaskType.Meeting); });
    }

    @Test
    public void taskParserTestTestParsePresentationInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime(userInput, TaskType.Presentation); });
    }

    @Test
    public void taskParserTestTestParseOthersInvalidDateCheck() {
        String userInput = "18:00 23/03/2080 to 19:00 23/03/2080";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseDateTime(userInput, TaskType.Others); });
    }

    @Test
    public void taskParserTestTestParseInvalidTypeCheck() {
        String userInput = "10";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseType(userInput, TaskType.getTaskTypes().length); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseType("", TaskType.getTaskTypes().length); });
    }

    @Test
    public void taskParserTestTestParseValidTypeCheck() {
        String userInput = "1";
        assertEquals(TaskType.Assignment,
            parseType(userInput, TaskType.getTaskTypes().length));
    }

    @Test
    public void taskParserTestTestParseWeightCheck() {
        String userInput = "1000.0";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseWeight(userInput); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseWeight(""); });
    }

    @Test
    public void taskParserTestTestParseEstimatedTimeCheck() {
        String userInput = "-6.0";
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseWeight(userInput); });
        assertThrows(AddOrEditTaskCommandException.class, () -> {
            parseWeight(""); });
    }
}
