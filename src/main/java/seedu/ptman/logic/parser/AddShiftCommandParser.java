package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_CAPACITY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_END;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TIME_START;
import static seedu.ptman.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.AddShiftCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Shift;
import seedu.ptman.model.shift.Time;

//@@author shanwpf
/**
 * Parses input arguments and creates a new AddShiftCommand object
 */
public class AddShiftCommandParser implements Parser<AddShiftCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddShiftCommand
     * and returns an AddShiftCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddShiftCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME_START,
                        PREFIX_TIME_END, PREFIX_CAPACITY);

        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_TIME_START,
                PREFIX_TIME_END, PREFIX_CAPACITY)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddShiftCommand.MESSAGE_USAGE));
        }

        try {
            Date date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE)).get();
            Time startTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME_START)).get();
            Time endTime = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME_END)).get();
            Capacity capacity = ParserUtil.parseCapacity(argMultimap.getValue(PREFIX_CAPACITY)).get();

            Shift shift = new Shift(date, startTime, endTime, capacity);

            return new AddShiftCommand(shift);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
