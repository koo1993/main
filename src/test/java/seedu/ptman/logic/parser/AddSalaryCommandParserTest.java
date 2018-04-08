package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_SALARY_DESC;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.ptman.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.ptman.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import java.util.ArrayList;

import org.junit.Test;
import seedu.ptman.logic.commands.AddSalaryCommand;
import seedu.ptman.logic.commands.ChangePasswordCommand;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Salary;

//@@author koo1993
public class AddSalaryCommandParserTest {

    private AddSalaryCommandParser parser = new AddSalaryCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        //alphabet index
        assertParseFailure(parser, "a " + INVALID_SALARY_DESC, MESSAGE_INVALID_INDEX);

        //no index
        assertParseFailure(parser, "s/123", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddSalaryCommand.MESSAGE_USAGE));

        //no salary prefix
        assertParseFailure(parser, "1 123", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddSalaryCommand.MESSAGE_USAGE));

        //wrong salary input
        assertParseFailure(parser, "1 s/abc", Salary.MESSAGE_SALARY_CONSTRAINTS);

    }

    @Test
    public void parse_validArgs_success() {
        Salary salary = new Salary("100");
        assertParseSuccess(parser, " 1 s/100", new AddSalaryCommand(INDEX_FIRST_EMPLOYEE, salary));
    }
}
