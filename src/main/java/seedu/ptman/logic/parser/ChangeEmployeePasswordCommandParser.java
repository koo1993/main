package seedu.ptman.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.commons.util.AppUtil.checkArgument;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.AddCommand;
import seedu.ptman.logic.commands.ChangeEmployeePasswordCommand;
import seedu.ptman.logic.commands.EditCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Employee;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.tag.Tag;

/**
 * Parses input arguments and creates a new ChangeEmployeePasswordCommand object
 */
public class ChangeEmployeePasswordCommandParser implements Parser<ChangeEmployeePasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangeEmployeePasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD)){
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ChangeEmployeePasswordCommand.MESSAGE_USAGE));
        }

        Index index;
        ArrayList<String> passwords;

        try {
            passwords = parsePasswords(argMultimap.getAllValues(PREFIX_PASSWORD));
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ChangeEmployeePasswordCommand.MESSAGE_USAGE));
        }

        if (!Password.isValidPassword(passwords.get(1))) {
            throw new ParseException(Password.MESSAGE_PASSWORD_CONSTRAINTS);
        }

        return new ChangeEmployeePasswordCommand(index, passwords);


    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses {@code Collection<String> passwords} into a {@code Set<Password>}.
     */
    public static ArrayList<String> parsePasswords(Collection<String> passwords) throws ParseException {
        requireNonNull(passwords);
        final ArrayList<String> passwordSet = new ArrayList<>();
           for (String password : passwords) {
            passwordSet.add(password);
        }

        if (passwordSet.size() != 3) {
            throw new ParseException("Incorrect number of passwords");
        }

        return passwordSet;
    }

}
