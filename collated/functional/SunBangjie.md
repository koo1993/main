# SunBangjie
###### \java\seedu\ptman\commons\encrypter\DataEncrypter.java
``` java
/**
 * Provides tools to encrypts data
 */
public class DataEncrypter {
    private static final String ALGO = "AES";
    private static final byte[] keyValue =
            new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    /**
     * Encrypt a string with AES algorithm.
     *
     * @param data is a string
     * @return the encrypted string
     */
    public static String encrypt(String data) throws Exception {
        if (data == null) {
            return null;
        }
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decrypt a string with AES algorithm.
     *
     * @param encryptedData is a string
     * @return the decrypted string
     */
    public static String decrypt(String encryptedData) throws Exception {
        if (encryptedData == null) {
            return null;
        }
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }

    /**
     * Generate a new encryption key.
     */
    private static Key generateKey() throws Exception {
        return new SecretKeySpec(keyValue, ALGO);
    }
}
```
###### \java\seedu\ptman\commons\events\model\OutletDataChangedEvent.java
``` java
/** Indicates the OutletInformation in the model has changed*/
public class OutletDataChangedEvent extends BaseEvent {
    public final OutletInformation data;

    public OutletDataChangedEvent(OutletInformation data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return OutletDataChangedEvent.class.getSimpleName();
    }
}
```
###### \java\seedu\ptman\commons\events\ui\AnnouncementChangedEvent.java
``` java
/**
 * Represents an announcement change in the Outlet Information
 */
public class AnnouncementChangedEvent extends BaseEvent {
    public final String information;

    public AnnouncementChangedEvent(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\ptman\logic\commands\AnnouncementCommand.java
``` java
/**
 * Edits the announcement of outlet in the ptman.
 */
public class AnnouncementCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "announcement";
    public static final String COMMAND_ALIAS = "announce";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the announcement of the outlet "
            + "in admin mode. Existing values will be overwritten by the input values.\n"
            + "Example: " + COMMAND_WORD + " "
            + "This is a new announcement.";
    public static final String MESSAGE_EDIT_OUTLET_SUCCESS = "Announcement successfully updated.";
    public static final String MESSAGE_EDIT_OUTLET_FAILURE = "New announcement cannot be empty."
            + MESSAGE_USAGE;

    private Announcement announcement;

    public AnnouncementCommand(Announcement announcement) {
        this.announcement = announcement;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        try {
            OutletInformation editedOutlet = new OutletInformation(model.getOutletInformation());
            editedOutlet.setAnnouncement(announcement);
            model.updateOutlet(editedOutlet);
            EventsCenter.getInstance().post(new AnnouncementChangedEvent(editedOutlet.getAnnouncement().value));
        } catch (NoOutletInformationFieldChangeException e) {
            throw new CommandException(MESSAGE_EDIT_OUTLET_FAILURE);
        }
        return new CommandResult(MESSAGE_EDIT_OUTLET_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AnnouncementCommand)) {
            return false;
        }

        // state check
        return announcement.equals(((AnnouncementCommand) other).announcement);
    }
}
```
###### \java\seedu\ptman\logic\commands\EditOutletCommand.java
``` java
/**
 * Edits the details of outlet in the ptman.
 */
public class EditOutletCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editoutlet";
    public static final String COMMAND_ALIAS = "eo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the outlet in admin "
            + "mode. Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + "[" + PREFIX_OUTLET_NAME + "OUTLETNAME] "
            + "[" + PREFIX_OPERATING_HOURS + "OPERATINGHOURS] "
            + "[" + PREFIX_OUTLET_CONTACT + "CONTACT] "
            + "[" + PREFIX_OUTLET_EMAIL + "EMAIL] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OUTLET_NAME + "AwesomeOutlet "
            + PREFIX_OPERATING_HOURS + "09:00-22:00 "
            + PREFIX_OUTLET_CONTACT + "91234567 "
            + PREFIX_OUTLET_EMAIL + "AwesomeOutlet@gmail.com";

    public static final String MESSAGE_EDIT_OUTLET_SUCCESS = "Outlet Information Edited.";
    public static final String MESSAGE_EDIT_OUTLET_FAILURE = "At least one field must be specified.\n"
            + MESSAGE_USAGE;

    private final OutletName name;
    private final OperatingHours operatingHours;
    private final OutletContact outletContact;
    private final OutletEmail outletEmail;

    /**
     * Constructor of EditOutletCommand
     */
    public EditOutletCommand(OutletName name, OperatingHours operatingHours, OutletContact outletContact,
                             OutletEmail outletEmail) {
        this.name = name;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        try {
            OutletInformation editedOutlet = new OutletInformation(model.getOutletInformation());
            editedOutlet.setOutletInformation(name, operatingHours, outletContact, outletEmail);
            model.updateOutlet(editedOutlet);
            EventsCenter.getInstance().post(new OutletInformationChangedEvent(
                    editedOutlet.getOperatingHours().toString(),
                    editedOutlet.getOutletContact().toString(),
                    editedOutlet.getOutletEmail().toString()));
            EventsCenter.getInstance().post(new OutletNameChangedEvent(editedOutlet.getName().toString()));
        } catch (NoOutletInformationFieldChangeException e) {
            throw new CommandException(MESSAGE_EDIT_OUTLET_FAILURE);
        }
        return new CommandResult(MESSAGE_EDIT_OUTLET_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditOutletCommand)) {
            return false;
        }

        // state check
        EditOutletCommand e = (EditOutletCommand) other;
        return outletContact.equals(e.outletContact)
                && name.equals(e.name)
                && operatingHours.equals(e.operatingHours)
                && outletEmail.equals(e.outletEmail);
    }
}
```
###### \java\seedu\ptman\logic\commands\ViewOutletCommand.java
``` java
/**
 * Displays the details of outlet in the ptman.
 */
public class ViewOutletCommand extends Command {

    public static final String COMMAND_WORD = "viewoutlet";
    public static final String COMMAND_ALIAS = "vo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": display basic outlet information\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        String messageToDisplay = "Outlet Name: " + model.getOutletInformation().getName() + " "
                + model.getOutletInformationMessage() + " Announcement: "
                + model.getOutletInformation().getAnnouncement();
        return new CommandResult(messageToDisplay);
    }
}
```
###### \java\seedu\ptman\logic\parser\AnnouncementCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AnnouncementCommand object
 */
public class AnnouncementCommandParser implements Parser {

    /**
     * Constructs an AnnouncementCommand parser
     * @param args
     * @return
     * @throws ParseException
     */
    public AnnouncementCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(AnnouncementCommand.MESSAGE_EDIT_OUTLET_FAILURE);
        }

        return new AnnouncementCommand(new Announcement(trimmedArgs));
    }
}
```
###### \java\seedu\ptman\logic\parser\EditOutletCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditOutletCommand object
 */
public class EditOutletCommandParser implements Parser<EditOutletCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditOutletCommand
     * and returns an EditOutletCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditOutletCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_OUTLET_NAME, PREFIX_OPERATING_HOURS,
                        PREFIX_OUTLET_CONTACT, PREFIX_OUTLET_EMAIL);

        OutletName outletName;
        OperatingHours operatingHours;
        OutletContact outletContact;
        OutletEmail outletEmail;

        try {
            outletName = ParserUtil.parseOutletName(argMultimap.getValue(PREFIX_OUTLET_NAME)).isPresent()
                    ? ParserUtil.parseOutletName(argMultimap.getValue(PREFIX_OUTLET_NAME)).get()
                    : null;
            operatingHours = ParserUtil.parseOperatingHours(argMultimap.getValue(PREFIX_OPERATING_HOURS)).isPresent()
                    ? ParserUtil.parseOperatingHours(argMultimap.getValue(PREFIX_OPERATING_HOURS)).get()
                    : null;
            outletContact = ParserUtil.parseOutletContact(argMultimap.getValue(PREFIX_OUTLET_CONTACT)).isPresent()
                    ? ParserUtil.parseOutletContact(argMultimap.getValue(PREFIX_OUTLET_CONTACT)).get()
                    : null;
            outletEmail = ParserUtil.parseOutletEmail(argMultimap.getValue(PREFIX_OUTLET_EMAIL)).isPresent()
                    ? ParserUtil.parseOutletEmail(argMultimap.getValue(PREFIX_OUTLET_EMAIL)).get()
                    : null;
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        return new EditOutletCommand(outletName, operatingHours, outletContact, outletEmail);
    }
}
```
###### \java\seedu\ptman\model\ModelManager.java
``` java
    @Override
    public void updateOutlet(OutletInformation editedOutlet) throws NoOutletInformationFieldChangeException {
        partTimeManager.updateOutlet(editedOutlet);
        indicatePartTimeManagerChanged();
    }

    @Override
    public String getOutletInformationMessage() {
        return partTimeManager.getOutletInformationMessage();
    }

    @Override
    public OutletInformation getOutletInformation() {
        return partTimeManager.getOutletInformation();
    }

    //=========== Filtered Employee List Accessors =============================================================
    @Override
    public void deleteTagFromAllEmployee(Tag tag) {
        partTimeManager.removeTagFromAllEmployees(tag);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Employee} backed by the internal list of
     * {@code partTimeManager}
     */
    @Override
    public ObservableList<Employee> getFilteredEmployeeList() {
        return FXCollections.unmodifiableObservableList(filteredEmployees);
    }

    @Override
    public void updateFilteredEmployeeList(Predicate<Employee> predicate) {
        requireNonNull(predicate);
        filteredEmployees.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return partTimeManager.equals(other.partTimeManager)
                && filteredEmployees.equals(other.filteredEmployees)
                && filteredShifts.equals(other.filteredShifts);
    }

}
```
###### \java\seedu\ptman\model\outlet\Announcement.java
``` java
/**
 * Represents an announcement in PTMan.
 * Guarantees: immutable;
 */
public class Announcement {

    public final String value;

    public Announcement(String announcement) {
        requireNonNull(announcement);
        this.value = announcement;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Announcement // instanceof handles nulls
                && this.value.equals(((Announcement) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\ptman\model\outlet\exceptions\NoOutletInformationFieldChangeException.java
``` java
/**
 * Signals that the set outlet information operation has all null fields
 */
public class NoOutletInformationFieldChangeException extends Exception {
}
```
###### \java\seedu\ptman\model\outlet\OperatingHours.java
``` java
/**
 * Represents operating hours in an outlet.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class OperatingHours {

    public static final String MESSAGE_OPERATING_HOUR_CONSTRAINTS =
            "Operating hours must be in the format of START-END where START and END must be in "
                    + "the format of hh:mm and in terms of 24 hours. For example, 09:00-22:00";
    public static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static final String OPERATING_HOUR_VALIDATION_REGEX = TIME24HOURS_PATTERN + "-"
            + TIME24HOURS_PATTERN;

    public final String value;
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * Constructs an {@code OperatingHours}.
     *
     * @param operatingHours A valid string of operating hours.
     */
    public OperatingHours(String operatingHours) {
        requireNonNull(operatingHours);
        checkArgument(isValidOperatingHours(operatingHours), MESSAGE_OPERATING_HOUR_CONSTRAINTS);
        String[] splitedTime = operatingHours.split("-");
        this.startTime = convertStringToLocalTime(splitedTime[0]);
        this.endTime = convertStringToLocalTime(splitedTime[1]);
        this.value = operatingHours;
    }

    /**
     * Converts a valid string of time to Local Time
     */
    public static LocalTime convertStringToLocalTime(String time) {
        String[] splitedTime = time.split(":");
        int hour = Integer.parseInt(splitedTime[0]);
        int minute = Integer.parseInt(splitedTime[1]);
        return LocalTime.of(hour, minute);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns true if a given string is a valid operating hours of an outlet.
     */
    public static boolean isValidOperatingHours(String test) {
        return test.matches(OPERATING_HOUR_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getStartTime())
                .append("-")
                .append(getEndTime());
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OperatingHours // instanceof handles nulls
                && this.startTime.equals(((OperatingHours) other).startTime)
                && this.endTime.equals(((OperatingHours) other).endTime)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

}
```
###### \java\seedu\ptman\model\outlet\OutletContact.java
``` java
/**
 * Represents an outlet's contact number in PTMan.
 * Guarantees: immutable; is valid as declared in {@link #isValidOutletContact(String)}
 */
public class OutletContact {

    public static final String MESSAGE_OUTLET_CONTACT_CONSTRAINTS =
            "Outlet contact numbers can only contain numbers, and should be at least 3 digits long";
    public static final String OUTLET_CONTACT_VALIDATION_REGEX = "\\d{3,}";
    public final String value;

    /**
     * Constructs a {@code OutletContact}.
     *
     * @param phone A valid phone number.
     */
    public OutletContact(String phone) {
        requireNonNull(phone);
        checkArgument(isValidOutletContact(phone), MESSAGE_OUTLET_CONTACT_CONSTRAINTS);
        this.value = phone;
    }

    /**
     * Returns true if a given string is a valid employee phone number.
     */
    public static boolean isValidOutletContact(String test) {
        return test.matches(OUTLET_CONTACT_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OutletContact // instanceof handles nulls
                && this.value.equals(((OutletContact) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\ptman\model\outlet\OutletEmail.java
``` java
/**
 * Represents an email of outlet in PTMan.
 * Guarantees: immutable; is valid as declared in {@link #isValidOutletEmail(String)}
 */
public class OutletEmail {

    private static  final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    public static final String MESSAGE_OUTLET_EMAIL_CONSTRAINTS = "Outlet emails should be of the format "
            + "local-part@domain and adhere to the following constraints:\n"
            + "1. The local-part should only contain alphanumeric characters and these special "
            + "characters, excluding the parentheses, (" + SPECIAL_CHARACTERS + ") .\n"
            + "2. This is followed by a '@' and then a domain name. "
            + "The domain name must:\n"
            + "    - be at least 2 characters long\n"
            + "    - start and end with alphanumeric characters\n"
            + "    - consist of alphanumeric characters, a period or a hyphen for the "
            + "characters in between, if any.";
    // alphanumeric and special characters
    private static final String LOCAL_PART_REGEX = "^[\\w" + SPECIAL_CHARACTERS + "]+";
    // alphanumeric characters except underscore
    private static final String DOMAIN_FIRST_CHARACTER_REGEX = "[^\\W_]";
    // alphanumeric, period and hyphen
    private static final String DOMAIN_MIDDLE_REGEX = "[a-zA-Z0-9.-]*";
    private static final String DOMAIN_LAST_CHARACTER_REGEX = "[^\\W_]$";
    public static final String OUTLET_EMAIL_VALIDATION_REGEX = LOCAL_PART_REGEX + "@"
            + DOMAIN_FIRST_CHARACTER_REGEX + DOMAIN_MIDDLE_REGEX + DOMAIN_LAST_CHARACTER_REGEX;

    public final String value;

    /**
     * Constructs an {@code OutletEmail}.
     *
     * @param outletEmail A valid outletEmail ptman.
     */
    public OutletEmail(String outletEmail) {
        requireNonNull(outletEmail);
        checkArgument(isValidOutletEmail(outletEmail), MESSAGE_OUTLET_EMAIL_CONSTRAINTS);
        this.value = outletEmail;
    }

    /**
     * Returns if a given string is a valid outlet email.
     */
    public static boolean isValidOutletEmail(String test) {
        return test.matches(OUTLET_EMAIL_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OutletEmail // instanceof handles nulls
                && this.value.equals(((OutletEmail) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\ptman\model\outlet\OutletInformation.java
``` java
/**
 * Represents an outlet in PTMan.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class OutletInformation {

    public static final String DEFAULT_OUTLET_NAME = "DefaultOutlet";
    public static final String DEFAULT_OPERATING_HOURS = "09:00-22:00";
    public static final String DEFAULT_OUTLET_CONTACT = "91234567";
    public static final String DEFAULT_OUTLET_EMAIL = "DefaultOutlet@gmail.com";
    public static final String DEFAULT_ANNOUNCEMENT_MESSAGE = "No announcement. "
            + "Please add new announcement with announcement command.";

    private OutletName name;
    private Password masterPassword;
    private OperatingHours operatingHours;
    private OutletContact outletContact;
    private OutletEmail outletEmail;
    private Announcement announcement;

    /**
     * Constructs an {@code OutletInformation}.
     *
     * @param name a valid outlet name
     * @param operatingHours a valid operating hours
     */
    public OutletInformation(OutletName name, OperatingHours operatingHours, OutletContact outletContact,
                             OutletEmail outletEmail, Password masterPassword, Announcement announcement) {
        requireAllNonNull(name, operatingHours, outletContact, outletEmail, masterPassword, announcement);
        this.name = name;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
        this.masterPassword = masterPassword;
        this.announcement = announcement;
    }

    public OutletInformation(OutletInformation outlet) {
        this.name = new OutletName(outlet.getName().toString());
        this.masterPassword = new Password(outlet.getMasterPassword());
        this.outletContact = new OutletContact(outlet.getOutletContact().toString());
        this.operatingHours = new OperatingHours(outlet.getOperatingHours().toString());
        this.outletEmail = new OutletEmail(outlet.getOutletEmail().toString());
        this.announcement = new Announcement(outlet.getAnnouncement().toString());
    }

    /**
     * Default constructor with default values
     */
    public OutletInformation() {
        this.name = new OutletName(DEFAULT_OUTLET_NAME);
        this.masterPassword = new Password();
        this.operatingHours = new OperatingHours(DEFAULT_OPERATING_HOURS);
        this.outletContact = new OutletContact(DEFAULT_OUTLET_CONTACT);
        this.outletEmail = new OutletEmail(DEFAULT_OUTLET_EMAIL);
        this.announcement = new Announcement(DEFAULT_ANNOUNCEMENT_MESSAGE);
    }

    public OutletName getName() {
        return name;
    }

    public Password getMasterPassword() {
        return masterPassword;
    }

    public OperatingHours getOperatingHours() {
        return operatingHours;
    }

    public OutletContact getOutletContact() {
        return outletContact;
    }

    public OutletEmail getOutletEmail() {
        return outletEmail;
    }

    /**
     * Set the outlet password.
     * only set after checking against outlet password.
     * @param password
     */
    public void setOutletPassword (Password password) {
        requireNonNull(password);
        this.masterPassword = password;

    }
    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setOutletInformation(OutletName name, OperatingHours operatingHours, OutletContact outletContact,
                                     OutletEmail outletEmail)
            throws NoOutletInformationFieldChangeException {
        if (name == null && operatingHours == null && outletContact == null && outletEmail == null) {
            throw new NoOutletInformationFieldChangeException();
        }
        if (name != null) {
            this.name = name;
        }
        if (operatingHours != null) {
            this.operatingHours = operatingHours;
        }
        if (outletContact != null) {
            this.outletContact = outletContact;
        }
        if (outletEmail != null) {
            this.outletEmail = outletEmail;
        }
    }


    public void setOutletInformation(OutletInformation outlet) throws NoOutletInformationFieldChangeException {
        try {
            requireAllNonNull(outlet.getName(), outlet.getOperatingHours(), outlet.getMasterPassword(),
                    outlet.getOutletEmail(), outlet.getOutletContact(), outlet.getAnnouncement());
        } catch (NullPointerException e) {
            throw new NoOutletInformationFieldChangeException();
        }
        this.name = outlet.getName();
        this.operatingHours = outlet.getOperatingHours();
        this.outletContact = outlet.getOutletContact();
        this.outletEmail = outlet.getOutletEmail();
        this.masterPassword = outlet.getMasterPassword();
        this.announcement = outlet.getAnnouncement();
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }


    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OutletInformation
                && ((OutletInformation) other).getName().equals(this.getName())
                && ((OutletInformation) other).getMasterPassword().equals(this.getMasterPassword())
                && ((OutletInformation) other).getOperatingHours().equals(this.getOperatingHours())
                && ((OutletInformation) other).getOutletContact().equals(this.getOutletContact())
                && ((OutletInformation) other).getOutletEmail().equals(this.getOutletEmail())
                && ((OutletInformation) other).getAnnouncement().equals(this.getAnnouncement()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, masterPassword, operatingHours, outletContact, outletEmail, announcement);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Operating Hours: ")
                .append(getOperatingHours())
                .append(" Contact: ")
                .append(getOutletContact())
                .append(" Email: ")
                .append(getOutletEmail());
        return builder.toString();
    }

}
```
###### \java\seedu\ptman\model\outlet\OutletName.java
``` java
/**
 * Represents an outlet's name in PTMan.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class OutletName {

    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Outlet name should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public OutletName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_NAME_CONSTRAINTS);
        this.fullName = name;
    }

    /**
     * Returns true if a given string is a valid outlet name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OutletName // instanceof handles nulls
                && this.fullName.equals(((OutletName) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}

```
###### \java\seedu\ptman\model\PartTimeManager.java
``` java
    public void updateOutlet(OutletInformation editedOutlet) throws NoOutletInformationFieldChangeException {
        outlet.setOutletInformation(editedOutlet);
    }

    public String getOutletInformationMessage() {
        return outlet.toString();
    }

    @Override
    public OutletInformation getOutletInformation() {
        return outlet;
    }

    /**
     *  Updates the master tag list to include tags in {@code employee} that are not in the list.
     *  @return a copy of this {@code employee} such that every tag in this employee points to a Tag
     *  object in the master list.
     */
    private Employee syncWithMasterTagList(Employee employee) {
        final UniqueTagList employeeTags = new UniqueTagList(employee.getTags());
        tags.mergeFrom(employeeTags);

        // Create map with values = tag object references in the master list
        // used for checking employee tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of employee tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        employeeTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Employee(
                employee.getName(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getAddress(),
                employee.getSalary(),
                employee.getPassword(),
                correctTagReferences
        );
    }

    /**
     * Removes {@code key} from this {@code PartTimeManager}.
     * @throws EmployeeNotFoundException if the {@code key} is not in this {@code PartTimeManager}.
     */
    public boolean removeEmployee(Employee key) throws EmployeeNotFoundException {
        if (employees.remove(key)) {
            removeUnusedTag();
            return true;
        } else {
            throw new EmployeeNotFoundException();
        }
    }

```
###### \java\seedu\ptman\storage\OutletInformationStorage.java
``` java
/**
 * Represents a storage for {@link seedu.ptman.model.outlet.OutletInformation}.
 */
public interface OutletInformationStorage {
    /**
     * Returns the file path of the data file.
     */
    String getOutletInformationFilePath();

    Optional<OutletInformation> readOutletInformation() throws DataConversionException, IOException;

    Optional<OutletInformation> readOutletInformation(String filePath) throws DataConversionException, IOException;

    void saveOutletInformation(OutletInformation outletInformation) throws IOException;

    void saveOutletInformation(OutletInformation outletInformation, String filePath) throws IOException;
}
```
###### \java\seedu\ptman\storage\StorageManager.java
``` java
    @Override
    public String getOutletInformationFilePath() {
        return outletInformationStorage.getOutletInformationFilePath();
    }

    @Override
    public Optional<OutletInformation> readOutletInformation() throws DataConversionException, IOException {
        return readOutletInformation(outletInformationStorage.getOutletInformationFilePath());
    }

    @Override
    public Optional<OutletInformation> readOutletInformation(String filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return outletInformationStorage.readOutletInformation(filePath);
    }

    @Override
    public void saveOutletInformation(OutletInformation outletInformation) throws IOException {
        saveOutletInformation(outletInformation, outletInformationStorage.getOutletInformationFilePath());
    }

    @Override
    public void saveOutletInformation(OutletInformation outletInformation, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        outletInformationStorage.saveOutletInformation(outletInformation, filePath);
    }

    @Override
    @Subscribe
    public void handleOutletDataChangedEvent(OutletDataChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveOutletInformation(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
}
```
###### \java\seedu\ptman\storage\XmlAdaptedOutletInformation.java
``` java
/**
 * JAXB-friendly version of the OutletInformation.
 */
@XmlRootElement(name = "outletinformation")
public class XmlAdaptedOutletInformation {

    public static final String FAIL_MESSAGE = "Outlet's %s field is missing!";
    public static final String DECRYPT_FAIL_MESSAGE = "Cannot decrypt %s";

    @XmlElement(required = true)
    private String outletName;
    @XmlElement(required = true)
    private String operatingHours;
    @XmlElement(required = true)
    private String outletContact;
    @XmlElement(required = true)
    private String outletEmail;
    @XmlElement(required = true)
    private String passwordHash;
    @XmlElement(required = true)
    private String announcement;

    public XmlAdaptedOutletInformation() {
        this.outletName = null;
        this.operatingHours = null;
        this.outletContact = null;
        this.outletEmail = null;
        this.passwordHash = null;
        this.announcement = null;
    }

    public XmlAdaptedOutletInformation(String outletName, String operatingHours, String outletContact,
                                       String outletEmail, String passwordHash, String announcement) {
        try {
            this.outletName = encrypt(outletName);
            this.operatingHours = encrypt(operatingHours);
            this.outletContact = encrypt(outletContact);
            this.outletEmail = encrypt(outletEmail);
            this.passwordHash = encrypt(passwordHash);
            this.announcement = encrypt(announcement);
        } catch (Exception e) {
            setAttributesFromStrings(outletName, operatingHours, outletContact, outletEmail,
                    passwordHash, announcement);
        }

    }

    /**
     * Converts a given OutletInformation into this class for JAXB use.
     */
    public XmlAdaptedOutletInformation(OutletInformation source) {
        this();
        try {
            outletName = encrypt(source.getName().fullName);
            operatingHours = encrypt(source.getOperatingHours().value);
            outletContact = encrypt(source.getOutletContact().value);
            outletEmail = encrypt(source.getOutletEmail().value);
            passwordHash = encrypt(source.getMasterPassword().getPasswordHash());
            announcement = encrypt(source.getAnnouncement().value);
        } catch (Exception e) {
            setAttributesFromSource(source);
        }

    }

    public void setAttributesFromStrings(String outletName, String operatingHours, String outletContact,
                                          String outletEmail, String passwordHash, String announcement) {
        this.outletName = outletName;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
        this.passwordHash = passwordHash;
        this.announcement = announcement;
    }

    public void setAttributesFromSource(OutletInformation source) {
        outletName = source.getName().fullName;
        operatingHours = source.getOperatingHours().value;
        outletContact = source.getOutletContact().value;
        outletEmail = source.getOutletEmail().value;
        passwordHash = source.getMasterPassword().getPasswordHash();
        announcement = source.getAnnouncement().value;
    }

    private OutletName setOutletName() throws IllegalValueException {
        String decryptedOutletName;
        try {
            decryptedOutletName = decrypt(this.outletName);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, OutletName.class.getSimpleName()));
        }
        if (decryptedOutletName == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletName.class.getSimpleName()));
        }
        if (!OutletName.isValidName(decryptedOutletName)) {
            throw new IllegalValueException(OutletName.MESSAGE_NAME_CONSTRAINTS);
        }
        OutletName outletName = new OutletName(decryptedOutletName);
        return outletName;
    }

    private OperatingHours setOperatingHours() throws IllegalValueException {
        String decryptedOperatingHours;
        try {
            decryptedOperatingHours = decrypt(this.operatingHours);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                    OperatingHours.class.getSimpleName()));
        }
        if (decryptedOperatingHours == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OperatingHours.class.getSimpleName()));
        }
        if (!OperatingHours.isValidOperatingHours(decryptedOperatingHours)) {
            throw new IllegalValueException(OperatingHours.MESSAGE_OPERATING_HOUR_CONSTRAINTS);
        }
        OperatingHours operatingHours = new OperatingHours(decryptedOperatingHours);
        return operatingHours;
    }

    private OutletContact setOutletContact() throws IllegalValueException {
        String decryptedOutletContact;
        try {
            decryptedOutletContact = decrypt(this.outletContact);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                    OutletContact.class.getSimpleName()));
        }
        if (decryptedOutletContact == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletContact.class.getSimpleName()));
        }
        if (!OutletContact.isValidOutletContact(decryptedOutletContact)) {
            throw new IllegalValueException(OutletContact.MESSAGE_OUTLET_CONTACT_CONSTRAINTS);
        }
        OutletContact outletContact = new OutletContact(decryptedOutletContact);
        return outletContact;
    }

    private OutletEmail setOutletEmail() throws IllegalValueException {
        String decryptedOutletEmail;
        try {
            decryptedOutletEmail = decrypt(this.outletEmail);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                    OutletEmail.class.getSimpleName()));
        }
        if (decryptedOutletEmail == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, OutletEmail.class.getSimpleName()));
        }
        if (!OutletEmail.isValidOutletEmail(decryptedOutletEmail)) {
            throw new IllegalValueException(OutletEmail.MESSAGE_OUTLET_EMAIL_CONSTRAINTS);
        }
        OutletEmail outletEmail = new OutletEmail(decryptedOutletEmail);
        return outletEmail;
    }

    private Password setPassword() throws IllegalValueException {
        String decryptedPasswordHash;
        try {
            decryptedPasswordHash = decrypt(this.passwordHash);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Password.class.getSimpleName()));
        }
        if (decryptedPasswordHash == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Password.class.getSimpleName()));
        }
        Password masterPassword = new Password(decryptedPasswordHash);
        return masterPassword;
    }

    private Announcement setAnnouncement() throws IllegalValueException {
        String decryptedAnnouncement;
        try {
            decryptedAnnouncement = decrypt(this.announcement);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                    Announcement.class.getSimpleName()));
        }
        if (decryptedAnnouncement == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Announcement.class.getSimpleName()));
        }
        Announcement announcement = new Announcement(decryptedAnnouncement);
        return announcement;
    }

    /**
     * Converts this jaxb-friendly adapted outlet object into the model's OutletInformation object
     */
    public OutletInformation toModelType() throws IllegalValueException {
        final OutletName outletName = setOutletName();
        final OperatingHours operatingHours = setOperatingHours();
        final OutletContact outletContact = setOutletContact();
        final OutletEmail outletEmail = setOutletEmail();
        final Password masterPassword = setPassword();
        final Announcement announcement = setAnnouncement();
        return new OutletInformation(outletName, operatingHours, outletContact, outletEmail,
                masterPassword, announcement);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedOutletInformation)) {
            return false;
        }

        XmlAdaptedOutletInformation otherOutlet = (XmlAdaptedOutletInformation) other;
        return Objects.equals(outletName, otherOutlet.outletName)
                && Objects.equals(operatingHours, otherOutlet.operatingHours)
                && Objects.equals(outletContact, otherOutlet.outletContact)
                && Objects.equals(outletEmail, otherOutlet.outletEmail)
                && Objects.equals(passwordHash, otherOutlet.passwordHash)
                && Objects.equals(announcement, otherOutlet.announcement);
    }
}
```
###### \java\seedu\ptman\storage\XmlOutletFileStorage.java
``` java
/**
 * Stores outlet information data in an XML file
 */
public class XmlOutletFileStorage {

    /**
     * Saves the given parttimemanager data to the specified file.
     */
    public static void saveDataToFile(File file, XmlAdaptedOutletInformation outletInformation)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, outletInformation);
        } catch (JAXBException e) {
            throw new AssertionError("Unexpected exception " + e.getMessage());
        }
    }

    /**
     * Returns outlet information in the file or an empty outlet information
     */
    public static XmlAdaptedOutletInformation loadDataFromSaveFile(File file) throws DataConversionException,
            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlAdaptedOutletInformation.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }
}
```
###### \java\seedu\ptman\storage\XmlOutletInformationStorage.java
``` java
/**
 * A class to access OutletInformation data stored as an xml file on the hard disk.
 */
public class XmlOutletInformationStorage implements OutletInformationStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlOutletInformationStorage.class);

    private String filePath;

    public XmlOutletInformationStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getOutletInformationFilePath() {
        return filePath;
    }

    @Override
    public Optional<OutletInformation> readOutletInformation() throws DataConversionException, IOException {
        return readOutletInformation(filePath);
    }

    /**
     * Reads outlet information from storage
     * @param filePath
     * @return
     * @throws DataConversionException
     * @throws FileNotFoundException
     */
    public Optional<OutletInformation> readOutletInformation(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File outletInformationFile = new File(filePath);

        if (!outletInformationFile.exists()) {
            logger.info("OutletInformation file " + outletInformationFile + " not found");
            return Optional.empty();
        }

        XmlAdaptedOutletInformation xmlAdaptedOutletInformation = XmlOutletFileStorage
                .loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlAdaptedOutletInformation.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + outletInformationFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveOutletInformation(OutletInformation outletInformation) throws IOException {
        saveOutletInformation(outletInformation, filePath);
    }

    /**
     * Saves outlet information into storage
     * @param outletInformation
     * @param filePath
     * @throws IOException
     */
    public void saveOutletInformation(OutletInformation outletInformation, String filePath) throws IOException {
        requireNonNull(outletInformation);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlOutletFileStorage.saveDataToFile(file, new XmlAdaptedOutletInformation(outletInformation));
    }
}
```
###### \java\seedu\ptman\ui\OutletDetailsPanel.java
``` java
    private void setAnnouncement(String text) {
        announcement.setText(text);
    }

```
###### \java\seedu\ptman\ui\OutletDetailsPanel.java
``` java
    @Subscribe
    public void handleAnnouncementChangedEvent(AnnouncementChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setAnnouncement(event.information));
    }
}
```
