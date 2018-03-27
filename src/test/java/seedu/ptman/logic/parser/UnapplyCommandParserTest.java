package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;

import java.util.Optional;

import org.junit.Test;

import seedu.ptman.logic.commands.UnapplyCommand;
import seedu.ptman.model.Password;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the UnapplyCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the UnapplyCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class UnapplyCommandParserTest {

    private UnapplyCommandParser parser = new UnapplyCommandParser();

    @Test
    public void parse_userModeValidArgs_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1 pw/DEFAULT1",
                new UnapplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())));
    }

    @Test
    public void parse_adminModeValidArgs_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1",
                new UnapplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.empty()));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a b pw/DEFAULT1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnapplyCommand.MESSAGE_USAGE));
    }
}
