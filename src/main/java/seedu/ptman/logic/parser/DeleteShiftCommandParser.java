package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.ParserUtil.clearPasswordFromCommand;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.DeleteShiftCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;

//@@author shanwpf
/**
 * Parses input arguments and creates a new DeleteShiftCommand object
 */
public class DeleteShiftCommandParser implements Parser<DeleteShiftCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteShiftCommand
     * and returns an DeleteShiftCommand object for execution.
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
}
