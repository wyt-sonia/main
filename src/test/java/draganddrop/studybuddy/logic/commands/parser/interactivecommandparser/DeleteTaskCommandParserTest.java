package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static draganddrop.studybuddy.logic.parser.interactivecommandparser.DeleteTaskCommandParser.parseIndex;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;

/**
 * Test class for DeleteTaskCommandParser.
 *
 * @@author Souwmyaa Sabarinathann
 */
public class DeleteTaskCommandParserTest {

    @Test
    public void parse_invalidIndex_check() {
        assertThrows(DeleteTaskCommandException.class, () -> {
            parseIndex("-1"); });
        assertThrows(DeleteTaskCommandException.class, () -> {
            parseIndex(""); });
        assertThrows(DeleteTaskCommandException.class, () -> {
            parseIndex("WrongInput"); });
    }

}
