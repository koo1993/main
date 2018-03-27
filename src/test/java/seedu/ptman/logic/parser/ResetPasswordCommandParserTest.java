package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import org.junit.Test;

import seedu.ptman.logic.commands.ResetPasswordCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the  ResetPasswordCommand code. For example, inputs "1 " and "1 abc" take the
 * same path through the ResetPasswordCommandParser, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class ResetPasswordCommandParserTest {

    private ResetPasswordCommandParser parser = new ResetPasswordCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // letters
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ResetPasswordCommand.MESSAGE_USAGE));

        // no index and no field specified
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ResetPasswordCommand.MESSAGE_USAGE));

        // no index and extra letters
        assertParseFailure(parser, "1 abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ResetPasswordCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, " 1",
                new ResetPasswordCommand(INDEX_FIRST_EMPLOYEE));

    }
}
