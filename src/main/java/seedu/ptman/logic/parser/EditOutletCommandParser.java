package seedu.ptman.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_MASTER_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OPERATING_HOURS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_OUTLET_NAME;

import java.util.NoSuchElementException;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.EditOutletCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Password;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletName;

/**
 * Parses input arguments and creates a new EditOutletCommand object
 */
public class EditOutletCommandParser implements Parser<EditOutletCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditOutletCommand
     * and returns an EditOutletCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditOutletCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MASTER_PASSWORD, PREFIX_OUTLET_NAME,
                        PREFIX_OPERATING_HOURS);

        Password masterPassword;
        OutletName outletName;
        OperatingHours operatingHours;
        try {
            masterPassword = ParserUtil.parseMasterPassword(argMultimap.getValue(PREFIX_MASTER_PASSWORD)).get();
            outletName = ParserUtil.parseOutletName(argMultimap.getValue(PREFIX_OUTLET_NAME)).get();
            operatingHours = ParserUtil.parseOperatingHours(argMultimap.getValue(PREFIX_OPERATING_HOURS)).get();
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        } catch (NoSuchElementException nsee) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditOutletCommand.MESSAGE_USAGE));
        }

        return new EditOutletCommand(masterPassword, outletName, operatingHours);
    }
}
