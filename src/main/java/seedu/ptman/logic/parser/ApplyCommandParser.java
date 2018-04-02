package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.ptman.logic.parser.ParserUtil.clearPasswordFromCommand;

import java.util.Optional;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.ApplyCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Password;

//@@author shanwpf
/**
 * Parses input arguments and creates a new ApplyCommand object
 */
public class ApplyCommandParser implements Parser<ApplyCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ApplyCommand
     * and returns an ApplyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ApplyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);
        try {
            Optional<Password> password = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD));
            Index employeeIndex = ParserUtil.parseFirstIndex(clearPasswordFromCommand(args));
            Index shiftIndex = ParserUtil.parseSecondIndex(clearPasswordFromCommand(args));
            return new ApplyCommand(employeeIndex, shiftIndex, password);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ApplyCommand.MESSAGE_USAGE));
        }
    }
}
