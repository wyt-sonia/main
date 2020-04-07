package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.FilterTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.FilterTaskCommandException;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;

public class FilterTaskCommandParserTest {
    private FilterTaskCommandParser parser = new FilterTaskCommandParser();

    @Test
    public void filterTaskCommandParserTestParseStatusIndexCheck() {
        assertThrows(FilterTaskCommandException.class, () -> {
            parser.parseStatusIndex("10"); });
        assertThrows(FilterTaskCommandException.class, () -> {
            parser.parseStatusIndex(""); });
        TaskStatus status = TaskStatus.getTaskStatusList().get(0);
        assertEquals(status, parser.parseStatusIndex("1"));
    }


    @Test
    public void filterTaskCommandParserTestParseOptionIndex() {
        assertThrows(FilterTaskCommandException.class, () -> {
            parser.parseOptionIndex("10"); });
        assertThrows(FilterTaskCommandException.class, () -> {
            parser.parseOptionIndex(""); });
        assertEquals(1, parser.parseOptionIndex("1"));
    }


    @Test
    public void filterTaskCommandParserTestParseTypeIndex() {
        assertThrows(FilterTaskCommandException.class, () -> {
            parser.parseTypeIndex("10"); });
        assertThrows(FilterTaskCommandException.class, () -> {
            parser.parseTypeIndex(""); });
        TaskType type = TaskType.getTaskTypes()[0];
        assertEquals(type, parser.parseTypeIndex("1"));
    }
}
