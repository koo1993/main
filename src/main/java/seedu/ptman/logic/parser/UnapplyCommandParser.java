package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.Optional;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.UnapplyCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Password;

/**
 * Parses input arguments and creates a new ApplyCommand object
 */
public class UnapplyCommandParser implements Parser<UnapplyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ApplyCommand
     * and returns an ApplyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UnapplyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);
        try {
            Optional<Password> password = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD));
            Index employeeIndex = ParserUtil.parseFirstIndex(clearPasswordFromCommand(args));
            Index shiftIndex = ParserUtil.parseSecondIndex(clearPasswordFromCommand(args));
            return new UnapplyCommand(employeeIndex, shiftIndex, password);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnapplyCommand.MESSAGE_USAGE));
        }
    }

    /**
     * cut away password at the end of the command
     */
    private String clearPasswordFromCommand(String args) {
        int startIndex = args.indexOf(PREFIX_PASSWORD.getPrefix());
        if (startIndex == -1) {
            return args;
        } else {
            return args.substring(0, startIndex);
        }
    }
}
