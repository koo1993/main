package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.commands.CommandTestUtil.ADMINPASSWORD_DESC_DEFAULT;
import static seedu.ptman.logic.commands.CommandTestUtil.CAPACITY_DESC_1;
import static seedu.ptman.logic.commands.CommandTestUtil.CAPACITY_DESC_2;
import static seedu.ptman.logic.commands.CommandTestUtil.DAY_DESC_MONDAY;
import static seedu.ptman.logic.commands.CommandTestUtil.DAY_DESC_TUESDAY;
import static seedu.ptman.logic.commands.CommandTestUtil.DEFAULT_PASSWORD;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_CAPACITY_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_DAY_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_TIME_END_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_TIME_START_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.ptman.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_END_DESC_10PM;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_END_DESC_8PM;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_START_DESC_10AM;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_START_DESC_12PM;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_CAPACITY_1;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_DAY_MONDAY;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_TIME_END_8PM;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_TIME_START_10AM;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.ptman.logic.commands.AddShiftCommand;
import seedu.ptman.model.outlet.Capacity;
import seedu.ptman.model.outlet.Day;
import seedu.ptman.model.outlet.Shift;
import seedu.ptman.model.outlet.Time;
import seedu.ptman.testutil.ShiftBuilder;

public class AddShiftCommandParserTest {
    private AddShiftCommandParser parser = new AddShiftCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Shift expectedShift = new ShiftBuilder().withDay(VALID_DAY_MONDAY).withStartTime(VALID_TIME_START_10AM)
                .withEndTime(VALID_TIME_END_8PM).withCapacity(VALID_CAPACITY_1).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + DAY_DESC_MONDAY + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, new AddShiftCommand(expectedShift));

        // multiple days - last day accepted
        assertParseSuccess(parser, DAY_DESC_TUESDAY + DAY_DESC_MONDAY + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, new AddShiftCommand(expectedShift));

        // multiple start times - last start time accepted
        assertParseSuccess(parser, DAY_DESC_MONDAY + TIME_START_DESC_12PM + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, new AddShiftCommand(expectedShift));

        // multiple end times - last end time accepted
        assertParseSuccess(parser, DAY_DESC_MONDAY + TIME_START_DESC_10AM +  TIME_END_DESC_10PM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, new AddShiftCommand(expectedShift));

        // multiple capacities - last capacity accepted
        assertParseSuccess(parser, DAY_DESC_MONDAY + TIME_START_DESC_10AM +  TIME_END_DESC_8PM
                + CAPACITY_DESC_2 + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, new AddShiftCommand(expectedShift));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE);

        // missing day prefix
        assertParseFailure(parser,  VALID_DAY_MONDAY + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, expectedMessage);

        // missing start time prefix
        assertParseFailure(parser,  DAY_DESC_MONDAY + VALID_TIME_START_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, expectedMessage);
        // missing end time prefix
        assertParseFailure(parser,  DAY_DESC_MONDAY + TIME_START_DESC_10AM + VALID_TIME_END_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT, expectedMessage);
        // missing capacity prefix
        assertParseFailure(parser,  DAY_DESC_MONDAY + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + VALID_CAPACITY_1 + ADMINPASSWORD_DESC_DEFAULT, expectedMessage);
        // missing password prefix
        assertParseFailure(parser,  DAY_DESC_MONDAY + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + DEFAULT_PASSWORD, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_DAY_MONDAY + VALID_TIME_START_10AM + VALID_TIME_END_8PM + VALID_CAPACITY_1
                        + DEFAULT_PASSWORD, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid day
        assertParseFailure(parser, INVALID_DAY_DESC + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT,
                Day.MESSAGE_DAY_CONSTRAINTS);

        // invalid start time
        assertParseFailure(parser, DAY_DESC_MONDAY + INVALID_TIME_START_DESC + TIME_END_DESC_8PM
                        + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT,
                Time.MESSAGE_TIME_CONSTRAINTS);

        // invalid end time
        assertParseFailure(parser, DAY_DESC_MONDAY + TIME_START_DESC_10AM + INVALID_TIME_END_DESC
                        + CAPACITY_DESC_1 + ADMINPASSWORD_DESC_DEFAULT,
                Time.MESSAGE_TIME_CONSTRAINTS);

        // invalid capacity
        assertParseFailure(parser, DAY_DESC_MONDAY + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                        + INVALID_CAPACITY_DESC + ADMINPASSWORD_DESC_DEFAULT,
                Capacity.MESSAGE_CAPACITY_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, DAY_DESC_MONDAY + TIME_START_DESC_10AM + INVALID_TIME_END_DESC
                        + INVALID_CAPACITY_DESC + ADMINPASSWORD_DESC_DEFAULT,
                Time.MESSAGE_TIME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + DAY_DESC_MONDAY + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                        + INVALID_CAPACITY_DESC + ADMINPASSWORD_DESC_DEFAULT,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE));
    }
}
