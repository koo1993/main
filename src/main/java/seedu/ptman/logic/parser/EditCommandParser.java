package seedu.ptman.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_SALARY;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.logic.commands.EditCommand;
import seedu.ptman.logic.commands.EditCommand.EditEmployeeDescriptor;
import seedu.ptman.logic.parser.exceptions.ParseException;
import seedu.ptman.model.Password;
import seedu.ptman.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_SALARY, PREFIX_TAG, PREFIX_PASSWORD);

        Index index;
        Password adminPassword;

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PASSWORD)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        EditEmployeeDescriptor editEmployeeDescriptor = new EditEmployeeDescriptor();
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editEmployeeDescriptor::setName);
            ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).ifPresent(editEmployeeDescriptor::setPhone);
            ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).ifPresent(editEmployeeDescriptor::setEmail);
            ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).ifPresent(editEmployeeDescriptor::setAddress);
            ParserUtil.parseSalary(argMultimap.getValue(PREFIX_SALARY)).ifPresent(editEmployeeDescriptor::setSalary);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editEmployeeDescriptor::setTags);
            adminPassword = ParserUtil.parsePassword(argMultimap.getValue(PREFIX_PASSWORD)).get();
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editEmployeeDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editEmployeeDescriptor, adminPassword);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
