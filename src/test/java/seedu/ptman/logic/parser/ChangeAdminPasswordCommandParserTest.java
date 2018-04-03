package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.ArrayList;

import org.junit.Test;

import seedu.ptman.logic.commands.ChangeAdminPasswordCommand;
import seedu.ptman.model.Password;

//@@author koo1993
public class ChangeAdminPasswordCommandParserTest {

    private ChangeAdminPasswordCommandParser parser = new ChangeAdminPasswordCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // letters
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeAdminPasswordCommand.MESSAGE_USAGE));

        //no field specified
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeAdminPasswordCommand.MESSAGE_USAGE));


        // missing one confirm password
        assertParseFailure(parser, " pw/default1 pw/default1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeAdminPasswordCommand.MESSAGE_USAGE));

        // new password that fulfill the 8 characters
        assertParseFailure(parser, " pw/default1 pw/newnot8 pw/newnot8", Password.MESSAGE_PASSWORD_CONSTRAINTS);

        // extra letters
        assertParseFailure(parser, " pw/default1 pw/default2 abc pw/default2  ", Password.MESSAGE_PASSWORD_CONSTRAINTS);

    }

    @Test
    public void parse_validArgs_success() {
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("default1");
        passwords.add("newpassword");
        passwords.add("newpassword");

        assertParseSuccess(parser, " pw/default1 pw/newpassword pw/newpassword",
                new ChangeAdminPasswordCommand(passwords));

    }
}
