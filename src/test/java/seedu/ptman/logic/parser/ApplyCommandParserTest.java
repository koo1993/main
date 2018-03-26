package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;

import org.junit.Test;

import seedu.ptman.logic.commands.ApplyCommand;
import seedu.ptman.model.Password;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class ApplyCommandParserTest {

    private ApplyCommandParser parser = new ApplyCommandParser();

    @Test
    public void parse_validArgs_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1 pw/DEFAULT1",
                new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, new Password()));
    }

    @Test
    public void parse_noPassword_throwsParseException() {
        assertParseFailure(parser, "1 1", String.format(MESSAGE_INVALID_COMMAND_FORMAT, ApplyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a b pw/DEFAULT1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ApplyCommand.MESSAGE_USAGE));
    }
}
