package draganddrop.studybuddy.logic.commands.parser.interactivecommandparser;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import draganddrop.studybuddy.logic.parser.interactivecommandparser.DeleteTaskCommandParser;
import draganddrop.studybuddy.logic.parser.interactivecommandparser.exceptions.DeleteTaskCommandException;

public class DeleteTaskCommandParserTest {

    private DeleteTaskCommandParser parser = new DeleteTaskCommandParser();

    @Test
    public void parse_invalidIndex_check() {
        assertThrows(DeleteTaskCommandException.class, () -> {
            parser.parseIndex("-1"); });
        assertThrows(DeleteTaskCommandException.class, () -> {
            parser.parseIndex(""); });
        assertThrows(DeleteTaskCommandException.class, () -> {
            parser.parseIndex("WrongInput"); });
    }

}
