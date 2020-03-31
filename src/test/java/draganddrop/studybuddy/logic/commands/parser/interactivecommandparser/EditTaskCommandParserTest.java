package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.EditTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.EditTaskCommandException;
import draganddrop.studybuddy.model.task.TaskType;

public class EditTaskCommandParserTest {
    private EditTaskCommandParser parser = new EditTaskCommandParser();

    @Test
    public void parse_name_check() {
        assertThrows(EditTaskCommandException.class, () -> {
            parser.parseName("Thistasknameismorethan20characterslong"); });
        assertThrows(EditTaskCommandException.class, () -> {
            parser.parseName(""); });
        assertEquals("ValidName", parser.parseName("ValidName"));
    }

    @Test
    public void parse_invalidType_check() {
        String userInput = "10";
        assertThrows(EditTaskCommandException.class, () -> {
            parser.parseType(userInput, TaskType.getTaskTypes().length); });
        assertThrows(EditTaskCommandException.class, () -> {
            parser.parseType("", TaskType.getTaskTypes().length); });
    }

    @Test
    public void parse_validType_check() {
        String userInput = "1";
        assertEquals(TaskType.Assignment,
                parser.parseType(userInput, TaskType.getTaskTypes().length));
    }
}
