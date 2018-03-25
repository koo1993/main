package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import java.util.ArrayList;

import org.junit.Test;

import seedu.ptman.logic.commands.ChangePasswordCommand;
import seedu.ptman.model.Password;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the ChangePasswordCommandParser, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class ChangePasswordCommandParserTest {

    private ChangePasswordCommandParser parser = new ChangePasswordCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // letters
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangePasswordCommand.MESSAGE_USAGE));

        // no index and no field specified
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangePasswordCommand.MESSAGE_USAGE));

        // no index and extra letters
        assertParseFailure(parser, "1 abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangePasswordCommand.MESSAGE_USAGE));

        // missing one confirm password
        assertParseFailure(parser, "1 pw/default1 pw/default1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangePasswordCommand.MESSAGE_USAGE));

        // new password that fulfill the 8 characters
        assertParseFailure(parser, "1 pw/default pw/newnot8 pw/newnot8", Password.MESSAGE_PASSWORD_CONSTRAINTS);


    }

    @Test
    public void parse_validArgs_success() {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("default1");
        passwords.add("newpassword");
        passwords.add("newpassword");

        assertParseSuccess(parser, " 1 pw/default1 pw/newpassword pw/newpassword",
                new ChangePasswordCommand(INDEX_FIRST_EMPLOYEE, passwords));

    }
}
