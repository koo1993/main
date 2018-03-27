package seedu.ptman.logic.parser;

import seedu.ptman.logic.commands.ResetMasterPasswordCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ResetMasterPasswordCommand object
 */
public class ResetMasterPasswordCommandParser implements Parser<ResetMasterPasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ResetMasterPasswordCommand
     * and returns an ResetMasterPasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ResetMasterPasswordCommand parse(String args) throws ParseException {
            return new ResetMasterPasswordCommand();
    }

}
