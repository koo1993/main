package seedu.ptman.logic.parser;

import seedu.ptman.logic.commands.ResetAdminPasswordCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;

//@@author koo1993
/**
 * Parses input arguments and creates a new ResetAdminPasswordCommand object
 */
public class ResetAdminPasswordCommandParser implements Parser<ResetAdminPasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ResetAdminPasswordCommand
     * and returns an ResetAdminPasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ResetAdminPasswordCommand parse(String args) {
        return new ResetAdminPasswordCommand();
    }

}
