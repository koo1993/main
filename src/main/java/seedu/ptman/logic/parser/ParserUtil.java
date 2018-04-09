package seedu.ptman.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.ptman.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.ptman.commons.core.index.Index;
import seedu.ptman.commons.exceptions.IllegalValueException;
import seedu.ptman.commons.util.StringUtil;
import seedu.ptman.model.Password;
import seedu.ptman.model.employee.Address;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.employee.Name;
import seedu.ptman.model.employee.Phone;
import seedu.ptman.model.employee.Salary;
import seedu.ptman.model.outlet.OperatingHours;
import seedu.ptman.model.outlet.OutletContact;
import seedu.ptman.model.outlet.OutletEmail;
import seedu.ptman.model.outlet.OutletName;
import seedu.ptman.model.shift.Capacity;
import seedu.ptman.model.shift.Date;
import seedu.ptman.model.shift.Time;
import seedu.ptman.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    private static final int EXPECTED_MIN_ARG_LENGTH = 2;

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a string containing 2 {@code oneBasedIndex} separated by a space
     * and returns the first {@code Index}. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseFirstIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim().split(" ")[0];
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a string containing 2 {@code oneBasedIndex} separated by a space
     * and returns the second {@code Index}. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseSecondIndex(String oneBasedIndex) throws IllegalValueException {
        String[] split = oneBasedIndex.trim().split(" ");
        if (split.length < EXPECTED_MIN_ARG_LENGTH) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        String trimmedIndex = split[1];
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(parseName(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String time} into a {@code Time}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code time} is invalid.
     */
    public static Time parseTime(String time) throws IllegalValueException {
        requireNonNull(time);
        String trimmedTime = time.trim();
        if (!Time.isValidTime(trimmedTime)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        return new Time(trimmedTime);
    }

    /**
     * Parses a {@code Optional<String> time} into an {@code Optional<Time>} if {@code time} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Time> parseTime(Optional<String> time) throws IllegalValueException {
        requireNonNull(time);
        return time.isPresent() ? Optional.of(parseTime(time.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String day} into a {@code int}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code day} is invalid.
     */
    public static Capacity parseCapacity(String capacity) throws IllegalValueException {
        requireNonNull(capacity);
        String trimmedCapacity = capacity.trim();
        if (!Capacity.isValidCapacity(trimmedCapacity)) {
            throw new IllegalValueException(Capacity.MESSAGE_CAPACITY_CONSTRAINTS);
        }
        return new Capacity(trimmedCapacity);
    }

    /**
     * Parses a {@code Optional<String> day} into an {@code Optional<Integer>} if {@code day} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Capacity> parseCapacity(Optional<String> capacity) throws IllegalValueException {
        requireNonNull(capacity);
        return capacity.isPresent() ? Optional.of(parseCapacity(capacity.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws IllegalValueException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(parsePhone(phone.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String ptman} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code ptman} is invalid.
     */
    public static Address parseAddress(String address) throws IllegalValueException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code Optional<String> ptman} into an {@code Optional<Address>} if {@code ptman} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Address> parseAddress(Optional<String> address) throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(parseAddress(address.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws IllegalValueException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code Optional<String> email} into an {@code Optional<Email>} if {@code email} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(parseEmail(email.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String outletName} into a {@code OutletName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code outletName} is invalid.
     */
    public static OutletName parseOutletName(String outletName) throws IllegalValueException {
        requireNonNull(outletName);
        String trimmedName = outletName.trim();
        if (!OutletName.isValidName(trimmedName)) {
            throw new IllegalValueException(OutletName.MESSAGE_NAME_CONSTRAINTS);
        }
        return new OutletName(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> outletName} into an {@code Optional<OutletName>}
     * if {@code outletName} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<OutletName> parseOutletName(Optional<String> outletName) throws IllegalValueException {
        requireNonNull(outletName);
        return outletName.isPresent() ? Optional.of(parseOutletName(outletName.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String operatingHours} into a {@code OperatingHours}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code operatingHours} is invalid.
     */
    public static OperatingHours parseOperatingHours(String operatingHours) throws IllegalValueException {
        requireNonNull(operatingHours);
        String trimmedOperatingHours = operatingHours.trim();
        if (!OperatingHours.isValidOperatingHours(trimmedOperatingHours)) {
            throw new IllegalValueException(OperatingHours.MESSAGE_OPERATING_HOUR_CONSTRAINTS);
        }
        if (!OperatingHours.isValidStartTimeEndTimeOrder(trimmedOperatingHours)) {
            throw new IllegalValueException(OperatingHours.MESSAGE_START_END_TIME_CONSTRAINTS);
        }
        return new OperatingHours(trimmedOperatingHours);
    }

    /**
     * Parses a {@code Optional<String> operatingHours} into an {@code Optional<OperatingHours>}
     * if {@code operatingHours} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<OperatingHours> parseOperatingHours(Optional<String> operatingHours)
            throws IllegalValueException {
        requireNonNull(operatingHours);
        return operatingHours.isPresent() ? Optional.of(parseOperatingHours(operatingHours.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String outletContact} into a {@code OutletContact}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code OutletContact} is invalid.
     */
    public static OutletContact parseOutletContact(String outletContact) throws IllegalValueException {
        requireNonNull(outletContact);
        String trimmedOutletContact = outletContact.trim();
        if (!OutletContact.isValidOutletContact(trimmedOutletContact)) {
            throw new IllegalValueException(OutletContact.MESSAGE_OUTLET_CONTACT_CONSTRAINTS);
        }
        return new OutletContact(trimmedOutletContact);
    }

    /**
     * Parses a {@code Optional<String> outletContact} into an {@code Optional<OutletContact>}
     * if {@code outletContact} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<OutletContact> parseOutletContact(Optional<String> outletContact)
            throws IllegalValueException {
        requireNonNull(outletContact);
        return outletContact.isPresent() ? Optional.of(parseOutletContact(outletContact.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String outletEmail} into an {@code OutletEmail}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code outletEmail} is invalid.
     */
    public static OutletEmail parseOutletEmail(String outletEmail) throws IllegalValueException {
        requireNonNull(outletEmail);
        String trimmedOutletEmail = outletEmail.trim();
        if (!OutletEmail.isValidOutletEmail(trimmedOutletEmail)) {
            throw new IllegalValueException(OutletEmail.MESSAGE_OUTLET_EMAIL_CONSTRAINTS);
        }
        return new OutletEmail(trimmedOutletEmail);
    }

    /**
     * Parses a {@code Optional<String> outletEmail} into an {@code Optional<OutletEmail>}
     * if {@code outletEmail} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<OutletEmail> parseOutletEmail(Optional<String> outletEmail) throws IllegalValueException {
        requireNonNull(outletEmail);
        return outletEmail.isPresent() ? Optional.of(parseOutletEmail(outletEmail.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String salary} into an {@code Salary}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code email} is invalid.
     */
    public static Salary parseSalary(String salary) throws IllegalValueException {
        requireNonNull(salary);
        String trimmedSalary = salary.trim();
        if (!Salary.isValidSalary(trimmedSalary)) {
            throw new IllegalValueException(Salary.MESSAGE_SALARY_CONSTRAINTS);
        }
        return new Salary(trimmedSalary);
    }

    /**
     * Parses a {@code Optional<String> salary} into an {@code Optional<Salary>} if {@code salary} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Salary> parseSalary(Optional<String> salary) throws IllegalValueException {
        requireNonNull(salary);
        return salary.isPresent() ? Optional.of(parseSalary(salary.get())) : Optional.empty();
    }

    /**
    * Parses a {@code String password} into an {@code Password}.
    * Leading and trailing whitespaces will be trimmed.
    *
    */
    public static Password parsePassword(String password) {
        requireNonNull(password);
        String trimmedPassword = password.trim();
        Password newPassword = new Password();
        newPassword.createPassword(trimmedPassword);
        return newPassword;
    }

    /**
    * Parses a {@code Optional<String> password} into an {@code Optional<Password>} if {@code password} is present.
    * See header comment of this class regarding the use of {@code Optional} parameters.
    */
    public static Optional<Password> parsePassword(Optional<String> password) {
        requireNonNull(password);
        return password.isPresent() ? Optional.of(parsePassword(password.get())) : Optional.empty();
    }


    /**
      * Returns true if none of the prefixes contains empty {@code Optional} values in the given
      * {@code ArgumentMultimap}.
      */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws IllegalValueException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String date} into a {@code Date}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code date} is invalid.
     */
    public static Date parseDate(String date) throws IllegalValueException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!Date.isValidDate(trimmedDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        return new Date(trimmedDate);
    }

    /**
     * Parses a {@code Optional<String> date} into an {@code Optional<Date>} if {@code date} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Date> parseDate(Optional<String> date) throws IllegalValueException {
        requireNonNull(date);
        return date.isPresent() ? Optional.of(parseDate(date.get())) : Optional.empty();
    }

    /**
     * cut away password at the end of the command
     */
    public static String clearPasswordFromCommand(String args) {
        int startIndex = args.indexOf(PREFIX_PASSWORD.getPrefix());
        if (startIndex == -1) {
            return args;
        } else {
            return args.substring(0, startIndex);
        }
    }
}
