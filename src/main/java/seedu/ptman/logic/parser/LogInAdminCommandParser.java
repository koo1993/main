package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import seedu.ptman.logic.commands.LogInAdminCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Password;

/**
 * Parses input arguments and creates a new RedoCommand object
 */
public class LogInAdminCommandParser implements Parser<LogInAdminCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the LogInAdminCommand
     * and returns an LogInAdminCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    public LogInAdminCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PASSWORD)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LogInAdminCommand.MESSAGE_USAGE));
        }

        Password adminPassword = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD)).get();
        return new LogInAdminCommand(adminPassword);
    }
}
