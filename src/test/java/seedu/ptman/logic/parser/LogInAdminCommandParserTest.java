package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.ptman.logic.commands.LogInAdminCommand;
import seedu.ptman.model.Password;

//@@author koo1993
public class LogInAdminCommandParserTest {
    private LogInAdminCommandParser parser = new LogInAdminCommandParser();

    @Test
    public void parse_invalidArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LogInAdminCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "log in", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LogInAdminCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "login", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LogInAdminCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "login pw", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                LogInAdminCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        LogInAdminCommand expectedFindCommand = new LogInAdminCommand(new Password());
        assertParseSuccess(parser, "login pw/DEFAULT1", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n login \n \t pw/DEFAULT1  \t", expectedFindCommand);
    }

}
