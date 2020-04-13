package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static draganddrop.studybuddy.logic.parser.interactivecommandparser.FilterTaskCommandParser.parseOptionIndex;
import static draganddrop.studybuddy.logic.parser.interactivecommandparser.FilterTaskCommandParser.parseStatusIndex;
import static draganddrop.studybuddy.logic.parser.interactivecommandparser.FilterTaskCommandParser.parseTypeIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.FilterTaskCommandException;
import draganddrop.studybuddy.model.task.TaskStatus;
import draganddrop.studybuddy.model.task.TaskType;

/**
 * This is the test class for filter task parser.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class FilterTaskCommandParserTest {

    @Test
    public void filterTaskCommandParserTestParseStatusIndexCheck() {
        assertThrows(FilterTaskCommandException.class, () -> {
            parseStatusIndex("10"); });
        assertThrows(FilterTaskCommandException.class, () -> {
            parseStatusIndex(""); });
        TaskStatus status = TaskStatus.getTaskStatusList().get(0);
        assertEquals(status, parseStatusIndex("1"));
    }


    @Test
    public void filterTaskCommandParserTestParseOptionIndex() {
        assertThrows(FilterTaskCommandException.class, () -> {
            parseOptionIndex("10"); });
        assertThrows(FilterTaskCommandException.class, () -> {
            parseOptionIndex(""); });
        assertEquals(1, parseOptionIndex("1"));
    }


    @Test
    public void filterTaskCommandParserTestParseTypeIndex() {
        assertThrows(FilterTaskCommandException.class, () -> {
            parseTypeIndex("10"); });
        assertThrows(FilterTaskCommandException.class, () -> {
            parseTypeIndex(""); });
        TaskType type = TaskType.getTaskTypes()[0];
        assertEquals(type, parseTypeIndex("1"));
    }
}
