package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.DeleteShiftCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteShiftCommand object
 */
public class DeleteShiftCommandParser implements Parser<DeleteShiftCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteShiftCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(clearPasswordFromCommand(args));
            return new DeleteShiftCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteShiftCommand.MESSAGE_USAGE));
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
