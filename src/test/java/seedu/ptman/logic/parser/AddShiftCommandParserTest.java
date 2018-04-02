package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.commands.CommandTestUtil.CAPACITY_DESC_1;
import static seedu.ptman.logic.commands.CommandTestUtil.CAPACITY_DESC_2;
import static seedu.ptman.logic.commands.CommandTestUtil.DATE_DESC_12MAR;
import static seedu.ptman.logic.commands.CommandTestUtil.DATE_DESC_13MAR;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_CAPACITY_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_TIME_END_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.INVALID_TIME_START_DESC;
import static seedu.ptman.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.ptman.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_END_DESC_10PM;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_END_DESC_8PM;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_START_DESC_10AM;
import static seedu.ptman.logic.commands.CommandTestUtil.TIME_START_DESC_12PM;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_CAPACITY_1;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_DATE_12MAR;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_TIME_END_8PM;
import static seedu.ptman.logic.commands.CommandTestUtil.VALID_TIME_START_10AM;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.ptman.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.ptman.logic.commands.AddShiftCommand;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.Time;
import seedu.ptman.testutil.ShiftBuilder;

//@@author shanwpf
public class AddShiftCommandParserTest {
    private AddShiftCommandParser parser = new AddShiftCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Shift expectedShift = new ShiftBuilder().withDate(VALID_DATE_12MAR).withStartTime(VALID_TIME_START_10AM)
                .withEndTime(VALID_TIME_END_8PM).withCapacity(VALID_CAPACITY_1).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple dates - last day accepted
        assertParseSuccess(parser, DATE_DESC_13MAR + DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple start times - last start time accepted
        assertParseSuccess(parser, DATE_DESC_12MAR + TIME_START_DESC_12PM + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple end times - last end time accepted
        assertParseSuccess(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM +  TIME_END_DESC_10PM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

        // multiple capacities - last capacity accepted
        assertParseSuccess(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM +  TIME_END_DESC_8PM
                + CAPACITY_DESC_2 + CAPACITY_DESC_1, new AddShiftCommand(expectedShift));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE);

        // missing date prefix
        assertParseFailure(parser,  VALID_DATE_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, expectedMessage);

        // missing start time prefix
        assertParseFailure(parser,  DATE_DESC_12MAR + VALID_TIME_START_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, expectedMessage);
        // missing end time prefix
        assertParseFailure(parser,  DATE_DESC_12MAR + TIME_START_DESC_10AM + VALID_TIME_END_8PM
                + CAPACITY_DESC_1, expectedMessage);
        // missing capacity prefix
        assertParseFailure(parser,  DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + VALID_CAPACITY_1, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_DATE_12MAR + VALID_TIME_START_10AM + VALID_TIME_END_8PM + VALID_CAPACITY_1,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid date
        assertParseFailure(parser, INVALID_DATE_DESC + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                + CAPACITY_DESC_1, Date.MESSAGE_DATE_CONSTRAINTS);

        // invalid start time
        assertParseFailure(parser, DATE_DESC_12MAR + INVALID_TIME_START_DESC + TIME_END_DESC_8PM
                        + CAPACITY_DESC_1, Time.MESSAGE_TIME_CONSTRAINTS);

        // invalid end time
        assertParseFailure(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM + INVALID_TIME_END_DESC
                        + CAPACITY_DESC_1, Time.MESSAGE_TIME_CONSTRAINTS);

        // invalid capacity
        assertParseFailure(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                        + INVALID_CAPACITY_DESC, Capacity.MESSAGE_CAPACITY_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, DATE_DESC_12MAR + TIME_START_DESC_10AM + INVALID_TIME_END_DESC
                        + INVALID_CAPACITY_DESC, Time.MESSAGE_TIME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + DATE_DESC_12MAR + TIME_START_DESC_10AM + TIME_END_DESC_8PM
                        + INVALID_CAPACITY_DESC,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE));
    }
}
