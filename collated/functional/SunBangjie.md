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
###### \java\seedu\ptman\commons\events\ui\OutletInformationChangedEvent.java
``` java
/**
 * Represents an information change in the Outlet Information
 */
public class OutletInformationChangedEvent extends BaseEvent {

    public final String operatingHours;
    public final String outletContact;
    public final String outletEmail;

    public OutletInformationChangedEvent(String operatingHours, String outletContact, String outletEmail) {
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\ptman\commons\events\ui\OutletNameChangedEvent.java
``` java
/**
 * Represents an information change in the Outlet Information
 */
public class OutletNameChangedEvent extends BaseEvent {

    public final String message;

    public OutletNameChangedEvent(String message) {
        this.message = message;
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

    public static final String COMMAND_FORMAT = "ANNOUNCEMENT_MESSAGE";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the announcement of the outlet "
            + "in admin mode. Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " "
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
###### \java\seedu\ptman\logic\commands\DecryptDataCommand.java
``` java
/**
 * Decrypts local storage files.
 */
public class DecryptDataCommand extends Command {

    public static final String COMMAND_WORD = "decrypt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Decrypts local storage files.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_DECRYPT_SUCCESS = "Local files successfully decrypted.";
    public static final String MESSAGE_DECRYPT_FAILURE = "Local files have already been decrypted.";

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        if (!model.getOutletInformation().getEncryptionMode()) {
            throw new CommandException(MESSAGE_DECRYPT_FAILURE);
        }
        model.decryptLocalStorage();
        return new CommandResult(MESSAGE_DECRYPT_SUCCESS);
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

    public static final String COMMAND_FORMAT = "[" + PREFIX_OUTLET_NAME + "OUTLETNAME] "
            + "[" + PREFIX_OPERATING_HOURS + "OPERATINGHOURS] "
            + "[" + PREFIX_OUTLET_CONTACT + "CONTACT] "
            + "[" + PREFIX_OUTLET_EMAIL + "EMAIL] ";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the outlet in admin "
            + "mode. Existing values will be overwritten by the input values.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " "
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
                    editedOutlet.getOperatingHours().getDisplayedMessage(),
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
###### \java\seedu\ptman\logic\commands\EncryptDataCommand.java
``` java
/**
 * Encrypts local storage files.
 */
public class EncryptDataCommand extends Command {

    public static final String COMMAND_WORD = "encrypt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Encrypts local storage files.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_ENCRYPT_SUCCESS = "Local files successfully encrypted.";
    public static final String MESSAGE_ENCRYPT_FAILURE = "Local files have already been encrypted.";

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.isAdminMode()) {
            throw new CommandException(MESSAGE_ACCESS_DENIED);
        }
        if (model.getEncryptionMode()) {
            throw new CommandException(MESSAGE_ENCRYPT_FAILURE);
        }
        model.encryptLocalStorage();
        return new CommandResult(MESSAGE_ENCRYPT_SUCCESS);
    }
}
```
###### \java\seedu\ptman\logic\commands\ViewEncryptionCommand.java
``` java
/**
 * Displays the details of outlet in the ptman.
 */
public class ViewEncryptionCommand extends Command {

    public static final String COMMAND_WORD = "viewencryption";
    public static final String COMMAND_ALIAS = "ve";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": displays encryption status of "
            + "local storage files.\n"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        String messageToDisplay = model.getEncryptionModeMessage();
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
    /** Raises an event to indicate the model has changed */
    private void indicateEncryptionModeChanged() {
        raise(new PartTimeManagerChangedEvent(partTimeManager));
        raise(new OutletDataChangedEvent(partTimeManager.getOutletInformation()));
    }

    /** Raises an event to indicate the model has changed */
    private void indicateOutletInformationChanged() {
        raise(new OutletDataChangedEvent(partTimeManager.getOutletInformation()));
    }
```
###### \java\seedu\ptman\model\ModelManager.java
``` java
    @Override
    public void updateOutlet(OutletInformation editedOutlet) throws NoOutletInformationFieldChangeException {
        partTimeManager.updateOutlet(editedOutlet);
        indicateOutletInformationChanged();
    }

    @Override
    public OutletInformation getOutletInformation() {
        return partTimeManager.getOutletInformation();
    }

    @Override
    public boolean getEncryptionMode() {
        return getOutletInformation().getEncryptionMode();
    }

    @Override
    public String getEncryptionModeMessage() {
        return getOutletInformation().getEncryptionModeMessage();
    }

    @Override
    public void encryptLocalStorage() {
        partTimeManager.encryptLocalStorage();
        indicateEncryptionModeChanged();
    }

    @Override
    public void decryptLocalStorage() {
        partTimeManager.decryptLocalStorage();
        indicateEncryptionModeChanged();
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
                    + "the format of HHMM and in terms of 24 hours. For example, 0900-2200";
    public static final String MESSAGE_START_END_TIME_CONSTRAINTS = "START time must be before END time.";
    public static final String OPERATING_HOUR_VALIDATION_REGEX = "\\d{4}" + "-" + "\\d{4}";

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
        checkArgument(isValidStartTimeEndTimeOrder(operatingHours), MESSAGE_START_END_TIME_CONSTRAINTS);
        String[] splitedTime = operatingHours.split("-");
        this.startTime = convertStringToLocalTime(splitedTime[0]);
        this.endTime = convertStringToLocalTime(splitedTime[1]);
        this.value = operatingHours;
    }

    /**
     * Converts a valid string of time to Local Time
     */
    public static LocalTime convertStringToLocalTime(String time) {
        String hourString = time.substring(0, 2);
        String minuteString = time.substring(2);
        int hour = Integer.parseInt(hourString);
        int minute = Integer.parseInt(minuteString);
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
        if (!test.matches(OPERATING_HOUR_VALIDATION_REGEX)) {
            return false;
        }
        String[] splitedTime = test.split("-");
        try {
            LocalTime.parse(splitedTime[0], DateTimeFormatter.ofPattern("HHmm"));
            LocalTime.parse(splitedTime[1], DateTimeFormatter.ofPattern("HHmm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if a given string has start time before end time.
     */
    public static boolean isValidStartTimeEndTimeOrder(String operatingHours) {
        String[] splitedTime = operatingHours.split("-");
        LocalTime startTime = convertStringToLocalTime(splitedTime[0]);
        LocalTime endTime = convertStringToLocalTime(splitedTime[1]);
        return startTime.isBefore(endTime);
    }

    @Override
    public String toString() {
        return value;
    }

    public String getDisplayedMessage() {
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
    public static final String DEFAULT_OPERATING_HOURS = "0900-2200";
    public static final String DEFAULT_OUTLET_CONTACT = "91234567";
    public static final String DEFAULT_OUTLET_EMAIL = "DefaultOutlet@gmail.com";
    public static final String DEFAULT_ANNOUNCEMENT_MESSAGE = "No announcement. "
            + "Please add new announcement with announcement command.";
    public static final String DATA_ENCRYPTED_MESSAGE = "Local storage files are encrypted.";
    public static final String DATA_NOT_ENCRYPTED_MESSAGE =
            "Local storage files are not encrypted.";

    private OutletName name;
    private Password masterPassword;
    private OperatingHours operatingHours;
    private OutletContact outletContact;
    private OutletEmail outletEmail;
    private Announcement announcement;
    private boolean isDataEncrypted;

    /**
     * Constructs an {@code OutletInformation}.
     */
    public OutletInformation(OutletName name, OperatingHours operatingHours, OutletContact outletContact,
                             OutletEmail outletEmail, Announcement announcement, Password masterPassword,
                             boolean isDataEncrypted) {
        requireAllNonNull(name, operatingHours, outletContact, outletEmail, announcement, masterPassword,
                isDataEncrypted);
        this.name = name;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
        this.announcement = announcement;
        this.masterPassword = masterPassword;
        this.isDataEncrypted = isDataEncrypted;
    }

    /**
     * Constructs a new {@code OutletInformation} from source outlet.
     * @param outlet source outlet
     */
    public OutletInformation(OutletInformation outlet) {
        this.name = new OutletName(outlet.getName().toString());
        this.masterPassword = new Password(outlet.getMasterPassword());
        this.outletContact = new OutletContact(outlet.getOutletContact().toString());
        this.operatingHours = new OperatingHours(outlet.getOperatingHours().toString());
        this.outletEmail = new OutletEmail(outlet.getOutletEmail().toString());
        this.announcement = new Announcement(outlet.getAnnouncement().toString());
        this.isDataEncrypted = outlet.getEncryptionMode();
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
        this.isDataEncrypted = false;
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

    public Announcement getAnnouncement() {
        return announcement;
    }

    public boolean getEncryptionMode() {
        return isDataEncrypted;
    }

    /**
     * Sets the outlet password.
     * only set after checking against outlet password.
     * @param password
     */
    public void setOutletPassword (Password password) {
        requireNonNull(password);
        this.masterPassword = password;

    }

    /**
     * Sets the outlet information attributes.
     * Some fields can be unspecified.
     * If all fields are unspecified, NoOutletInformationFieldChangeException will be thrown.
     */
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

    /**
     * sets outlet information from source outlet
     * @param outlet source outlet
     * @throws NoOutletInformationFieldChangeException
     */
    public void setOutletInformation(OutletInformation outlet) throws NoOutletInformationFieldChangeException {
        try {
            requireAllNonNull(outlet.getName(), outlet.getOperatingHours(), outlet.getMasterPassword(),
                    outlet.getOutletEmail(), outlet.getOutletContact(), outlet.getAnnouncement(),
                    outlet.getEncryptionMode());
        } catch (NullPointerException e) {
            throw new NoOutletInformationFieldChangeException();
        }
        this.name = outlet.getName();
        this.operatingHours = outlet.getOperatingHours();
        this.outletContact = outlet.getOutletContact();
        this.outletEmail = outlet.getOutletEmail();
        this.masterPassword = outlet.getMasterPassword();
        this.announcement = outlet.getAnnouncement();
        this.isDataEncrypted = outlet.getEncryptionMode();
    }

    /**
     * Sets encryption mode
     * @param isEncrypted
     */
    public void setEncryptionMode(boolean isEncrypted) {
        this.isDataEncrypted = isEncrypted;
    }

    /**
     * Sets outlet announcement
     * @param announcement
     */
    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    /**
     * Gets encryption mode message
     * @return message that decribes whether current local files are encrypted
     */
    public String getEncryptionModeMessage() {
        if (isDataEncrypted) {
            return DATA_ENCRYPTED_MESSAGE;
        }
        return DATA_NOT_ENCRYPTED_MESSAGE;
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
                && ((OutletInformation) other).getAnnouncement().equals(this.getAnnouncement())
                && ((OutletInformation) other).getEncryptionMode() == (this.getEncryptionMode()));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, masterPassword, operatingHours, outletContact, outletEmail, announcement,
                isDataEncrypted);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Outlet Name: ")
                .append(getName())
                .append(" Operating Hours: ")
                .append(getOperatingHours().getDisplayedMessage())
                .append(" Contact: ")
                .append(getOutletContact())
                .append(" Email: ")
                .append(getOutletEmail())
                .append(" Announcement: ")
                .append(getAnnouncement())
                .append(" Encryption: ")
                .append(getEncryptionModeMessage());
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
    public void setOutletInformation(OutletInformation outlet) throws NoOutletInformationFieldChangeException {
        this.outlet.setOutletInformation(outlet);
    }

```
###### \java\seedu\ptman\model\PartTimeManager.java
``` java
    public void updateOutlet(OutletInformation editedOutlet) throws NoOutletInformationFieldChangeException {
        outlet.setOutletInformation(editedOutlet);
    }

    @Override
    public OutletInformation getOutletInformation() {
        return outlet;
    }

    public void encryptLocalStorage() {
        outlet.setEncryptionMode(ENCRYPTED);
    }

    public void decryptLocalStorage() {
        outlet.setEncryptionMode(DECRYPTED);
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

    void backupOutletInformation(OutletInformation outletInformation) throws IOException;
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


    // ================ backup methods ==============================

```
###### \java\seedu\ptman\storage\StorageManager.java
``` java
    @Override
    public void backupPartTimeManager(ReadOnlyPartTimeManager partTimeManager) throws IOException {
        partTimeManagerStorage.backupPartTimeManager(partTimeManager);
    }

    @Override
    public void backupOutletInformation(OutletInformation outletInformation) throws IOException {
        outletInformationStorage.backupOutletInformation(outletInformation);
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
    public static final String ENCRYPTED = OutletInformation.DATA_ENCRYPTED_MESSAGE;
    public static final String DECRYPTED = OutletInformation.DATA_NOT_ENCRYPTED_MESSAGE;

    @XmlElement(required = true)
    private String encryptionMode;
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
        this.encryptionMode = null;
        this.outletName = null;
        this.operatingHours = null;
        this.outletContact = null;
        this.outletEmail = null;
        this.passwordHash = null;
        this.announcement = null;
    }

    public XmlAdaptedOutletInformation(String encryptionMode, String outletName, String operatingHours,
                                       String outletContact, String outletEmail, String passwordHash,
                                       String announcement) {
        if (encryptionMode.equals(ENCRYPTED)) {
            this.encryptionMode = encryptionMode;
            try {
                this.outletName = encrypt(outletName);
                this.operatingHours = encrypt(operatingHours);
                this.outletContact = encrypt(outletContact);
                this.outletEmail = encrypt(outletEmail);
                this.passwordHash = encrypt(passwordHash);
                this.announcement = encrypt(announcement);
            } catch (Exception e) {
                //encryption should not fail
            }
        } else {
            setAttributesFromStrings(encryptionMode, outletName, operatingHours, outletContact, outletEmail,
                    passwordHash, announcement);
        }
    }

    /**
     * Converts a given OutletInformation into this class for JAXB use.
     */
    public XmlAdaptedOutletInformation(OutletInformation source) {
        this();
        if (source.getEncryptionMode()) {
            encryptionMode = source.getEncryptionModeMessage();
            try {
                outletName = encrypt(source.getName().fullName);
                operatingHours = encrypt(source.getOperatingHours().value);
                outletContact = encrypt(source.getOutletContact().value);
                outletEmail = encrypt(source.getOutletEmail().value);
                passwordHash = encrypt(source.getMasterPassword().getPasswordHash());
                announcement = encrypt(source.getAnnouncement().value);
            } catch (Exception e) {
                //encryption should not fail
            }
        } else {
            setAttributesFromSource(source);
        }
    }

    public void setAttributesFromStrings(String encryptionMode, String outletName, String operatingHours,
                                         String outletContact, String outletEmail, String passwordHash,
                                         String announcement) {
        this.encryptionMode = encryptionMode;
        this.outletName = outletName;
        this.operatingHours = operatingHours;
        this.outletContact = outletContact;
        this.outletEmail = outletEmail;
        this.passwordHash = passwordHash;
        this.announcement = announcement;
    }

    public void setAttributesFromSource(OutletInformation source) {
        encryptionMode = source.getEncryptionModeMessage();
        outletName = source.getName().fullName;
        operatingHours = source.getOperatingHours().value;
        outletContact = source.getOutletContact().value;
        outletEmail = source.getOutletEmail().value;
        passwordHash = source.getMasterPassword().getPasswordHash();
        announcement = source.getAnnouncement().value;
    }

    private OutletName setOutletName() throws IllegalValueException {
        String decryptedOutletName;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOutletName = decrypt(this.outletName);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, OutletName.class.getSimpleName()));
            }
        } else {
            decryptedOutletName = this.outletName;
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
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOperatingHours = decrypt(this.operatingHours);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        OperatingHours.class.getSimpleName()));
            }
        } else {
            decryptedOperatingHours = this.operatingHours;
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
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOutletContact = decrypt(this.outletContact);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        OutletContact.class.getSimpleName()));
            }
        } else {
            decryptedOutletContact = this.outletContact;
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
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedOutletEmail = decrypt(this.outletEmail);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        OutletEmail.class.getSimpleName()));
            }
        } else {
            decryptedOutletEmail = this.outletEmail;
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
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedPasswordHash = decrypt(this.passwordHash);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Password.class.getSimpleName()));
            }
        } else {
            decryptedPasswordHash = this.passwordHash;
        }

        if (decryptedPasswordHash == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Password.class.getSimpleName()));
        }
        Password masterPassword = new Password(decryptedPasswordHash);
        return masterPassword;
    }

    private Announcement setAnnouncement() throws IllegalValueException {
        String decryptedAnnouncement;
        if (this.encryptionMode.equals(ENCRYPTED)) {
            try {
                decryptedAnnouncement = decrypt(this.announcement);
            } catch (Exception e) {
                throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE,
                        Announcement.class.getSimpleName()));
            }
        } else {
            decryptedAnnouncement = this.announcement;
        }

        if (decryptedAnnouncement == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, Announcement.class.getSimpleName()));
        }
        Announcement announcement = new Announcement(decryptedAnnouncement);
        return announcement;
    }

    private boolean getEncryptionMode() throws IllegalValueException {
        if (this.encryptionMode == null) {
            throw new IllegalValueException(String.format(FAIL_MESSAGE, "Encryption Mode"));
        }
        if (this.encryptionMode.equals(ENCRYPTED)) {
            return true;
        } else if (this.encryptionMode.equals(DECRYPTED)) {
            return false;
        } else {
            throw new IllegalValueException("Invalid encryption mode");
        }
    }

    /**
     * Converts this jaxb-friendly adapted outlet object into the model's OutletInformation object
     */
    public OutletInformation toModelType() throws IllegalValueException {
        final boolean isDataEncrypted = getEncryptionMode();
        final OutletName outletName = setOutletName();
        final OperatingHours operatingHours = setOperatingHours();
        final OutletContact outletContact = setOutletContact();
        final OutletEmail outletEmail = setOutletEmail();
        final Password masterPassword = setPassword();
        final Announcement announcement = setAnnouncement();
        return new OutletInformation(outletName, operatingHours, outletContact, outletEmail,
                announcement, masterPassword, isDataEncrypted);
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
                && Objects.equals(announcement, otherOutlet.announcement)
                && Objects.equals(encryptionMode, otherOutlet.encryptionMode);
    }
}
```
###### \java\seedu\ptman\storage\XmlEncryptedAdaptedEmployee.java
``` java
/**
 * JAXB-friendly version of the Employee.
 */
public class XmlEncryptedAdaptedEmployee {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Employee's %s field is missing!";
    public static final String DECRYPT_FAIL_MESSAGE = "Cannot decrypt %s";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String salary;
    @XmlElement(required = true)
    private String passwordHash;

    @XmlElement
    private List<XmlEncryptedAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedEmployee.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlEncryptedAdaptedEmployee() {}

    /**
     * Constructs an {@code XmlAdaptedEmployee} with the given employee details.
     */
    public XmlEncryptedAdaptedEmployee(String name, String phone, String email, String address,
                                       String salary, String passwordHash, List<XmlEncryptedAdaptedTag> tagged) {
        try {
            this.name = encrypt(name);
            this.phone = encrypt(phone);
            this.email = encrypt(email);
            this.address = encrypt(address);
            this.salary = encrypt(salary);
            this.passwordHash = encrypt(passwordHash);
        } catch (Exception e) {
            //Encryption should not fail
        }

        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Employee into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedEmployee
     */
    public XmlEncryptedAdaptedEmployee(Employee source) {
        try {
            name = encrypt(source.getName().fullName);
            phone = encrypt(source.getPhone().value);
            email = encrypt(source.getEmail().value);
            address = encrypt(source.getAddress().value);
            salary = encrypt(source.getSalary().value);
            passwordHash = encrypt(source.getPassword().getPasswordHash());
        } catch (Exception e) {
            //Encryption should not fail
        }

        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlEncryptedAdaptedTag(tag));
        }
    }

    public void setAttributesFromStrings(String name, String phone, String email, String address,
                                          String salary, String passwordHash) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.salary = salary;
        this.passwordHash = passwordHash;
    }

    public void setAttributesFromSource(Employee source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        salary = source.getSalary().value;
        passwordHash = source.getPassword().getPasswordHash();
    }

    private Name setName() throws IllegalValueException {
        String decryptedName;
        try {
            decryptedName = decrypt(this.name);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Name.class.getSimpleName()));
        }
        if (decryptedName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Name.class.getSimpleName()));
        }
        if (!Name.isValidName(decryptedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(decryptedName);
    }

    private Phone setPhone() throws IllegalValueException {
        String decryptedPhone;
        try {
            decryptedPhone = decrypt(this.phone);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Phone.class.getSimpleName()));
        }
        if (decryptedPhone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(decryptedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(decryptedPhone);
    }

    private Email setEmail() throws IllegalValueException {
        String decryptedEmail;
        try {
            decryptedEmail = decrypt(this.email);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Email.class.getSimpleName()));
        }
        if (decryptedEmail == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(decryptedEmail)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(decryptedEmail);
    }

    private Address setAddress() throws IllegalValueException {
        String decryptedAddress;
        try {
            decryptedAddress = decrypt(this.address);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Address.class.getSimpleName()));
        }
        if (decryptedAddress == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(decryptedAddress)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        return new Address(decryptedAddress);
    }

    private Salary setSalary() throws IllegalValueException {
        String decryptedSalary;
        try {
            decryptedSalary = decrypt(this.salary);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Salary.class.getSimpleName()));
        }
        if (decryptedSalary == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Salary.class.getSimpleName()));
        }
        if (!Salary.isValidSalary(decryptedSalary)) {
            throw new IllegalValueException(Salary.MESSAGE_SALARY_CONSTRAINTS);
        }
        return new Salary(decryptedSalary);
    }

    private Password setPassword() throws IllegalValueException {
        String decryptedPasswordHash;
        try {
            decryptedPasswordHash = decrypt(this.passwordHash);
        } catch (Exception e) {
            throw new IllegalValueException(String.format(DECRYPT_FAIL_MESSAGE, Password.class.getSimpleName()));
        }
        if (decryptedPasswordHash == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Password.class.getSimpleName()));
        }
        return new Password(decryptedPasswordHash);
    }

    /**
     * Converts this jaxb-friendly adapted employee object into the model's Employee object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted employee
     */
    public Employee toModelType() throws IllegalValueException {
        final List<Tag> employeeTags = new ArrayList<>();
        for (XmlEncryptedAdaptedTag tag : tagged) {
            employeeTags.add(tag.toModelType());
        }

        final Name name = setName();
        final Phone phone = setPhone();
        final Email email = setEmail();
        final Address address = setAddress();
        final Salary salary = setSalary();
        final Password password = setPassword();

        final Set<Tag> tags = new HashSet<>(employeeTags);
        return new Employee(name, phone, email, address, salary, password, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlEncryptedAdaptedEmployee)) {
            return false;
        }

        XmlEncryptedAdaptedEmployee otherEmployee = (XmlEncryptedAdaptedEmployee) other;
        return Objects.equals(name, otherEmployee.name)
                && Objects.equals(phone, otherEmployee.phone)
                && Objects.equals(email, otherEmployee.email)
                && Objects.equals(address, otherEmployee.address)
                && tagged.equals(otherEmployee.tagged);
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
    private static final String BACKUP_FILE_EXTENSION = ".backup";

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

    @Override
    public void backupOutletInformation(OutletInformation outletInformation) throws IOException {
        saveOutletInformation(outletInformation, addFileNameExtentionIfNotNull(filePath));
    }

    /**
     *
     * @param filePath location of data.
     * @return
     */
    private String addFileNameExtentionIfNotNull(String filePath) {
        if (filePath == null) {
            return null;
        } else {
            return filePath + BACKUP_FILE_EXTENSION;
        }
    }
}
```
###### \java\seedu\ptman\ui\OutletDetailsPanel.java
``` java
/**
 * The Outlet Panel of the App, which displays the Outlet name and details
 */
public class OutletDetailsPanel extends UiPart<Region> {

    private static final String FXML = "OutletDetailsPanel.fxml";

    public final OutletInformation outlet;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Label outletNamePanelHeader;

    @FXML
    private Label operatingHours;

    @FXML
    private Label outletContact;

    @FXML
    private Label outletEmail;

    @FXML
    private Label announcement;


    public OutletDetailsPanel(OutletInformation outlet) {
        super(FXML);
        this.outlet = outlet;
        //outletInformation.setWrapText(true);
        setOutletInformation(outlet.getOperatingHours().getDisplayedMessage(),
                outlet.getOutletContact().toString(),
                outlet.getOutletEmail().toString());
        setOutletName(outlet.getName().toString());
        setAnnouncement(outlet.getAnnouncement().toString());

        registerAsAnEventHandler(this);
    }

    private void setOutletName(String name) {
        outletNamePanelHeader.setText(name);
    }

    private void setOutletInformation(String operatingHours, String outletContact, String outletEmail) {
        this.operatingHours.setText(operatingHours + "    ");
        this.outletContact.setText(outletContact + "    ");
        this.outletEmail.setText(outletEmail);
    }

    private void setAnnouncement(String text) {
        announcement.setText(text);
    }

    @Subscribe
    private void handleOutletInformationChangedEvent(OutletInformationChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setOutletInformation(event.operatingHours, event.outletContact, event.outletEmail));
    }

    @Subscribe
    private void handleOutletNameChangedEvent(OutletNameChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setOutletName(event.message));
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
