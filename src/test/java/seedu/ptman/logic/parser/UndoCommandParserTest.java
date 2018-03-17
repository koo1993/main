package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.ptman.logic.commands.UndoCommand;
import seedu.ptman.model.Password;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the UndoCommand code.
 */
public class UndoCommandParserTest {

    private UndoCommandParser parser = new UndoCommandParser();
    private final Password defaultPassword = new Password();

    @Test
    public void parse_validArgs_returnsUndoCommand() {
        assertParseSuccess(parser, " pw/DEFAULT1", new UndoCommand(defaultPassword));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE));
    }
}
