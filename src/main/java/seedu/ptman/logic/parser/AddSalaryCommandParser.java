package seedu.ptman.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.AddSalaryCommand;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.employee.Salary;

//@@author koo1993
/**
 * Parses input arguments and creates a new AddSalaryCommand object
 */
public class AddSalaryCommandParser implements Parser<AddSalaryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddSalaryCommand
     * and returns an AddSalaryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddSalaryCommand parse(String args) throws ParseException {
        requireNonNull(args);
        Index index;
        Salary salary;

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SALARY);

        if (!arePrefixesPresent(argMultimap, PREFIX_SALARY) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddSalaryCommand.MESSAGE_USAGE));
        }

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            salary = ParserUtil.parseSalary(argMultimap.getValue(PREFIX_SALARY)).get();
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
        return new AddSalaryCommand(index, salary);
    }

}
