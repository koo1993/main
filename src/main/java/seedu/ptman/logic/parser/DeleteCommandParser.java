package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.DeleteCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Password;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PASSWORD)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        try {
            Password adminPassword = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD)).get();
            Index index = ParserUtil.parseIndex(clearPasswordFromCommand(args));
            return new DeleteCommand(index, adminPassword);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
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
