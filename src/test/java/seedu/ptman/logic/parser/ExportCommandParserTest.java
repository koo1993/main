package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.ptman.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.ptman.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.ptman.logic.commands.ExportCommand;
import seedu.ptman.model.employee.Email;

public class ExportCommandParserTest {
    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Email expectedEmail = new Email(VALID_EMAIL_BOB);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + EMAIL_DESC_BOB, new ExportCommand(expectedEmail));

        // multiple emails - last email accepted
        assertParseSuccess(parser, EMAIL_DESC_AMY + EMAIL_DESC_BOB, new ExportCommand(expectedEmail));
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + EMAIL_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));

        assertParseFailure(parser, PREAMBLE_NON_EMPTY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

}
