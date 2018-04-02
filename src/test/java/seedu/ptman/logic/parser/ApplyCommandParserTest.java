package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_SHIFT;

import java.util.Optional;

import org.junit.Test;

import seedu.ptman.logic.commands.ApplyCommand;
import seedu.ptman.model.Password;

//@@author shanwpf
public class ApplyCommandParserTest {

    private ApplyCommandParser parser = new ApplyCommandParser();

    @Test
    public void parse_validArgs_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1 pw/DEFAULT1",
                new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.of(new Password())));
    }

    @Test
    public void parse_noPassword_returnsApplyCommand() {
        assertParseSuccess(parser, "1 1", new ApplyCommand(INDEX_FIRST_EMPLOYEE, INDEX_FIRST_SHIFT, Optional.empty()));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a b pw/DEFAULT1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ApplyCommand.MESSAGE_USAGE));
    }
}
