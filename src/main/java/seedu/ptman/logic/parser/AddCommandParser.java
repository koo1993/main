package seedu.ptman.logic.parser;

import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.AddCommand;
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
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_SALARY, PREFIX_TAG, PREFIX_PASSWORD);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_SALARY,
                PREFIX_EMAIL, PREFIX_PASSWORD) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();
            Salary salary = ParserUtil.parseSalary(argMultimap.getValue(PREFIX_SALARY)).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            Employee employee = new Employee(name, phone, email, address, salary, new Password(), tagList);

            Password adminPassword = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD)).get();

            return new AddCommand(employee, adminPassword);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
