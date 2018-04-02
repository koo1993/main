package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.ResetPasswordCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;

//@@author koo1993
/**
 * Parses input arguments and creates a new ResetPasswordCommand object
 */
public class ResetPasswordCommandParser implements Parser<ResetPasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ResetPasswordCommand
     * and returns an ResetPasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ResetPasswordCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ResetPasswordCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResetPasswordCommand.MESSAGE_USAGE));
        }
    }

}
