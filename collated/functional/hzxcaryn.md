# hzxcaryn
###### \java\seedu\ptman\commons\events\model\UserModeChangedEvent.java
``` java
/**
 * Indicates that the user mode has changed. (Admin mode or not)
 */
public class UserModeChangedEvent extends BaseEvent {

    public final boolean isAdminMode;

    public UserModeChangedEvent(boolean isAdminMode) {
        this.isAdminMode = isAdminMode;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\ptman\commons\events\ui\ExportTimetableAsImageAndEmailRequestEvent.java
``` java
/**
 * An event requesting to export the timetable view as an image and email it to the given email
 */
public class ExportTimetableAsImageAndEmailRequestEvent extends BaseEvent {

    public final String filename;
    public final Email email;

    public ExportTimetableAsImageAndEmailRequestEvent(String filename, Email email) {
        this.filename = filename;
        this.email = email;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\ptman\commons\events\ui\ExportTimetableAsImageRequestEvent.java
``` java
/**
 * An event requesting to export the timetable view as an image locally
 */
public class ExportTimetableAsImageRequestEvent extends BaseEvent {

    public final String filename;

    public ExportTimetableAsImageRequestEvent(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \java\seedu\ptman\commons\events\ui\TimetableWeekChangeRequestEvent.java
``` java
/**
 * Indicates a request to change the timetable view to the next or previous week.
 */
public class TimetableWeekChangeRequestEvent extends BaseEvent {
    /**
     * This represents the different week change requests that can be made to the timetable
     */
    public enum WeekChangeRequest {
        NEXT,
        PREVIOUS,
        CURRENT
    }

    private WeekChangeRequest request;

    public TimetableWeekChangeRequestEvent(WeekChangeRequest request) {
        this.request = request;
    }

    public WeekChangeRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
```
###### \java\seedu\ptman\commons\services\EmailService.java
``` java
    /**
     * Send exported timetable image as an attachment to user
     * @param email
     * @param filename
     * @throws MessagingException
     */
    public void sendTimetableAttachment(String email, String filename) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmailTimetable));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("[PTMan] My Timetable");

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Dear Valued PTMan user,\n\nAttached is your exported timetable.\n"
                + "Thank you for using PTMan and have a nice day!\n\nBest Regards,\nThe PTMan Team");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);

        Transport.send(message);
    }

}
```
###### \java\seedu\ptman\commons\util\DateUtil.java
``` java
    /**
     * Given a {@code date}, returns the date of the week's Thursday
     */
    public static LocalDate getThursdayOfDate(LocalDate date) {
        requireNonNull(date);
        int week = getWeekFromDate(date);
        WeekFields weekFields = WeekFields.of(Locale.FRANCE);
        return LocalDate.now()
                .withYear(date.getYear())
                .with(weekFields.weekOfYear(), week)
                .with(weekFields.dayOfWeek(), 4);
    }


    /**
     * Given {@code currDate}, returns the date one week later
     */
    public static LocalDate getNextWeekDate(LocalDate currDate) {
        requireNonNull(currDate);
        LocalDate nextWeekDate = currDate.plusDays(NUM_DAYS_IN_WEEK);
        return nextWeekDate;
    }

    /**
     * Given {@code currDate}, returns the date one week before
     */
    public static LocalDate getPrevWeekDate(LocalDate currDate) {
        requireNonNull(currDate);
        LocalDate prevWeekDate = currDate.minusDays(NUM_DAYS_IN_WEEK);
        return prevWeekDate;
    }

    /**
     * Given {@code date}, returns the month and year as a string
     */
    public static String getMonthYearFromDate(LocalDate date) {
        requireNonNull(date);
        Month month = date.getMonth();
        int year = date.getYear();
        return month.name() + " " + year;
    }

}
```
###### \java\seedu\ptman\logic\commands\ExportCommand.java
``` java
/**
 * Exports current timetable view as an image locally
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String COMMAND_ALIAS = "exp";

    public static final String COMMAND_FORMAT = "[" + PREFIX_EMAIL + "EMAIL]";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": "
            + "Exports timetable as image to the folder your jar file is located. "
            + "If email is stated, timetable image will be sent as an attachment to the stated email. "
            + "Else, timetable image will be saved locally.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + "\nExample: " + COMMAND_WORD + " "
            + PREFIX_EMAIL + "email@example.com";

    public static final String MESSAGE_SAVE_SUCCESS = "Timetable is successfully exported!";
    public static final String MESSAGE_EMAIL_SUCCESS = "Timetable is successfully sent to your email!";

    private final Email emailToSendImageTo;

    /**
     * Creates an ExportCommand to save the timetable as image locally
     */
    public ExportCommand() {
        emailToSendImageTo = null;
    }

    /**
     * Creates an ExportCommand to send the timetable image to the user's email
     * @param email
     */
    public ExportCommand(Email email) {
        requireNonNull(email);
        emailToSendImageTo = email;
    }

    @Override
    public CommandResult execute() {
        if (emailToSendImageTo != null) {
            EventsCenter.getInstance().post(new ExportTimetableAsImageAndEmailRequestEvent(
                    TIMETABLE_IMAGE_FILE_NAME_DEFAULT + LocalDateTime.now().toString(), emailToSendImageTo));
            return new CommandResult(MESSAGE_EMAIL_SUCCESS);
        } else {
            EventsCenter.getInstance().post(new ExportTimetableAsImageRequestEvent(TIMETABLE_IMAGE_FILE_NAME_DEFAULT));
            return new CommandResult(MESSAGE_SAVE_SUCCESS);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        // state check
        ExportCommand e = (ExportCommand) other;
        return emailToSendImageTo.equals(e.emailToSendImageTo);
    }

}
```
###### \java\seedu\ptman\logic\commands\ViewShiftCommand.java
``` java
/**
 * Lists all employees in PTMan that belongs to the input shift to the user.
 */
public class ViewShiftCommand extends Command {

    public static final String COMMAND_WORD = "viewshift";
    public static final String COMMAND_ALIAS = "vs";

    public static final String COMMAND_FORMAT = "SHIFT_INDEX";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the employees that belongs to the input shift index.\n"
            + "Parameters: "
            + COMMAND_FORMAT
            + " (must be a positive integer)"
            + "\nExample: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_SUCCESS = "Listed all employees in shift %1$s.";

    private final Index targetIndex;
    private Shift targetShift;

    public ViewShiftCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<Shift> shiftList = model.getFilteredShiftList();
        if (targetIndex.getZeroBased() >= shiftList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
        }

        targetShift = shiftList.get(targetIndex.getZeroBased());

        model.updateFilteredEmployeeList(employee -> targetShift.containsEmployee(employee));
        return new CommandResult(String.format(MESSAGE_SUCCESS, targetIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewShiftCommand // instanceof handles nulls
                && this.targetIndex.equals(((ViewShiftCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\ptman\logic\LogicManager.java
``` java
    @Override
    public OutletInformation getOutletInformation() {
        return model.getOutletInformation();
    }

    @Override
    public LocalDate getCurrentDisplayedDate() {
        return currentDisplayedDate;
    }

    @Override
    public void setFilteredShiftListToNextWeek() {
        currentDisplayedDate = getNextWeekDate(currentDisplayedDate);
        model.setFilteredShiftListToWeek(currentDisplayedDate);
    }

    @Override
    public void setFilteredShiftListToPrevWeek() {
        currentDisplayedDate = getPrevWeekDate(currentDisplayedDate);
        model.setFilteredShiftListToWeek(currentDisplayedDate);
    }

    @Override
    public void setFilteredShiftListToCurrentWeek() {
        currentDisplayedDate = LocalDate.now();
        model.setFilteredShiftListToWeek(currentDisplayedDate);
    }

    @Override
    public void setFilteredShiftListToCustomWeek(LocalDate date) {
        currentDisplayedDate = date;
        model.setFilteredShiftListToWeek(date);
    }

    @Override
    public boolean isAdminMode() {
        return model.isAdminMode();
    }
}
```
###### \java\seedu\ptman\logic\parser\ExportCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ExportCommand
     * and returns an ExportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ExportCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMAIL);

        if (!arePrefixesPresent(argMultimap, PREFIX_EMAIL) && argMultimap.getPreamble().isEmpty()) {
            return new ExportCommand();
        } else if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        try {
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            return new ExportCommand(email);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
```
###### \java\seedu\ptman\logic\parser\ViewShiftCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ViewShiftCommand object
 */
public class ViewShiftCommandParser implements Parser<ViewShiftCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewShiftCommand
     * and returns an ViewShiftCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewShiftCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewShiftCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewShiftCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\ptman\model\ModelManager.java
``` java
    @Override
    public void setFilteredShiftListToWeek(LocalDate date) {
        updateFilteredShiftList(shift ->
                getWeekFromDate(shift.getDate().getLocalDate()) == getWeekFromDate(date));
    }

```
###### \java\seedu\ptman\model\util\SampleDataUtil.java
``` java
/**
 * Contains utility methods for populating {@code PartTimeManager} with sample data.
 */
public class SampleDataUtil {

    public static Employee[] getSampleEmployees() {
        return new Employee[] {
            new Employee(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("300"), new Password(),
                getTagSet("barista", "cashier")),
            new Employee(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Salary("0"), new Password(),
                getTagSet("barista", "supervisor")),
            new Employee(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Salary("100"), new Password(),
                getTagSet("bartender")),
            new Employee(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Salary("450"), new Password(),
                getTagSet("paperwork")),
            new Employee(new Name("Eden Lim"), new Phone("92624417"), new Email("eden123@example.com"),
                new Address("Blk 25 Aljunied Street 85, #10-10"), new Salary("250"), new Password(),
                getTagSet("supervisor", "bartender")),
            new Employee(new Name("Emelia Tan"), new Phone("91275306"), new Email("tanmeme@example.com"),
                new Address("Blk 45 Bras Basah Rd 55, #11-11"), new Salary("60"), new Password(),
                getTagSet("paperwork", "barista")),
            new Employee(new Name("Faith Foo"), new Phone("82935501"), new Email("faithful@example.com"),
                new Address("Blk 01 Pasir Ris Street 81, #01-01"), new Salary("110"), new Password(),
                getTagSet("barista", "bartender")),
            new Employee(new Name("Irfan Ibrahim"), new Phone("82492021"), new Email("irfan@example.com"),
                new Address("Blk 17 Tampines Street 20, #17-35"), new Salary("200"), new Password(),
                getTagSet("barista")),
            new Employee(new Name("Jessica Liu"), new Phone("92823467"), new Email("liushabao@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("300"), new Password(),
                getTagSet("alfresco", "cashier")),
            new Employee(new Name("Jorge Keng"), new Phone("90129036"), new Email("kengjjj@example.com"),
                new Address("Blk 105 Tampines Street 85, #04-01"), new Salary("80"), new Password(),
                getTagSet("supervisor", "paperwork")),
            new Employee(new Name("Katrina Rose"), new Phone("80924520"), new Email("rosie@example.com"),
                new Address("Blk 555 Simei Street 05, #06-06"), new Salary("100"), new Password(),
                getTagSet("cashier", "bartender", "paperwork")),
            new Employee(new Name("Lee Wenqi"), new Phone("88124243"), new Email("wenqiqi@example.com"),
                new Address("Blk 01 Defu Lane 05, #02-11"), new Salary("500"), new Password(),
                getTagSet("bartender", "barista")),
            new Employee(new Name("Liu Shi Qi"), new Phone("87438807"), new Email("liushishi@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), new Salary("300"), new Password(),
                getTagSet("barista", "cashier")),
            new Employee(new Name("Mallory Hek"), new Phone("99272758"), new Email("maliciousme@example.com"),
                new Address("Blk 30 Serangoon Gardens, #07-18"), new Salary("1000"), new Password(),
                getTagSet("barista", "chef")),
            new Employee(new Name("Matthew Koh"), new Phone("93210283"), new Email("madmatt@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Salary("100"), new Password(),
                getTagSet("bartender")),
            new Employee(new Name("Nathan Gay"), new Phone("91031282"), new Email("gaygaygay@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Salary("450"), new Password(),
                getTagSet("paperwork", "cashier")),
            new Employee(new Name("Ophelia Grey"), new Phone("92492021"), new Email("shades0fgrey@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), new Salary("200"), new Password(),
                getTagSet("barista")),
            new Employee(new Name("Patrick Soo"), new Phone("91234417"), new Email("guais00s00@example.com"),
                new Address("Blk 99 Boon Keng Road 85, #08-01"), new Salary("50"), new Password(),
                getTagSet("bartender", "barista")),
            new Employee(new Name("Philips Loy"), new Phone("89801253"), new Email("pheeloy@example.com"),
                new Address("Blk 103 Hougang Street 32, #05-03"), new Salary("60"), new Password(),
                getTagSet("alfresco")),
            new Employee(new Name("Quentin Cool"), new Phone("92624417"), new Email("iamcool123@example.com"),
                new Address("Blk 111 Punggol Street 05, #01-05"), new Salary("300"), new Password(),
                getTagSet("chef")),
            new Employee(new Name("Roy Balakrishnan"), new Phone("83623312"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("100"), new Password(),
                getTagSet("paperwork", "alfresco")),
            new Employee(new Name("Vaibhavi Shandilya"), new Phone("91530773"), new Email("chiobu@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), new Salary("100"), new Password(),
                getTagSet("supervisor")),
        };
    }

    public static Shift[] getSampleShifts() {
        LocalDate mondayDateOfCurrWeek = DateUtil.getMondayOfDate(LocalDate.now());
        LocalDate mondayDateOfPrevWeek = DateUtil.getPrevWeekDate(mondayDateOfCurrWeek);
        LocalDate mondayDateOfNextWeek = DateUtil.getNextWeekDate(mondayDateOfCurrWeek);
        return new Shift[] {
            // Build shifts for previous week
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(1)), new Time("0900"), new Time("1600"),
                new Capacity("3")),
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(2)), new Time("1500"), new Time("2200"),
                new Capacity("1")),
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(4)), new Time("1000"), new Time("1600"),
                new Capacity("5")),
            new Shift(new Date(mondayDateOfPrevWeek.plusDays(5)), new Time("1100"), new Time("1900"),
                new Capacity("2")),

            // Build shifts for current week
            new Shift(new Date(mondayDateOfCurrWeek), new Time("1500"), new Time("2200"), new Capacity("1")),
            new Shift(new Date(mondayDateOfCurrWeek.plusDays(1)), new Time("0900"), new Time("1600"),
                new Capacity("5")),
            new Shift(new Date(mondayDateOfCurrWeek.plusDays(2)), new Time("1000"), new Time("1700"),
                new Capacity("3")),
            new Shift(new Date(mondayDateOfCurrWeek.plusDays(4)), new Time("0900"), new Time("1700"),
                new Capacity("3")),

            // Build shifts for next week
            new Shift(new Date(mondayDateOfNextWeek.plusDays(2)), new Time("1100"), new Time("1900"),
                new Capacity("2")),
            new Shift(new Date(mondayDateOfNextWeek.plusDays(3)), new Time("0900"), new Time("1600"),
                new Capacity("5")),
            new Shift(new Date(mondayDateOfNextWeek.plusDays(5)), new Time("1500"), new Time("2200"),
                new Capacity("3")),
            new Shift(new Date(mondayDateOfNextWeek.plusDays(6)), new Time("1200"), new Time("1900"),
                new Capacity("5"))
        };

    }

    /**
     * This method is used in {@code getSamplePartTimeManager} to add employees to the sample shifts.
     * Each nested array contains employee indices that corresponds to an employee in the sample employee array.
     * Each sample shift is assigned one of these nested arrays according to their index in {@code getSampleShifts}.
     */
    public static int[][] getSampleEmployeesForEachShift() {
        return new int[][] {
            // previous week
            new int[] {5, 17, 21},
            new int[] {6},
            new int[] {1, 2, 3, 4, 5},
            new int[] {7, 9},

            // current week
            new int[] {},
            new int[] {2, 10},
            new int[] {2, 5, 7},
            new int[] {3},

            // next week
            new int[] {2},
            new int[] {15, 20},
            new int[] {3, 5, 7},
            new int[] {2, 8, 10},
        };
    }

    public static ReadOnlyPartTimeManager getSamplePartTimeManager() {
        try {
            PartTimeManager sampleAb = new PartTimeManager();
            Employee[] sampleEmployees = getSampleEmployees();
            Shift[] sampleShifts = getSampleShifts();
            int[][] sampleEmployeesForEachShift = getSampleEmployeesForEachShift();

            for (Employee sampleEmployee : sampleEmployees) {
                sampleAb.addEmployee(sampleEmployee);
            }
            for (int i = 0; i < sampleShifts.length; i++) {
                for (int employeeIndex : sampleEmployeesForEachShift[i]) {
                    sampleShifts[i].addEmployee(sampleEmployees[employeeIndex]);
                }
                sampleAb.addShift(sampleShifts[i]);
            }
            return sampleAb;
        } catch (DuplicateEmployeeException e) {
            throw new AssertionError("sample data cannot contain duplicate employees", e);
        } catch (DuplicateShiftException e) {
            throw new AssertionError("sample data cannot contain duplicate shifts", e);
        } catch (ShiftFullException e) {
            throw new AssertionError("sample data shifts cannot have more employees than capacity", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
```
###### \java\seedu\ptman\ui\AdminModeDisplay.java
``` java
/**
 * Admin mode display of the app. Displays whether the user is in admin mode or not.
 */
public class AdminModeDisplay extends UiPart<Region> {

    public static final String LABEL_STYLE_CLASS_ADMIN = "label-admin-mode";
    public static final String LABEL_STYLE_CLASS_NON_ADMIN = "label-non-admin-mode";

    private static final String adminModeText = "Admin Mode";
    private static final Logger logger = LogsCenter.getLogger(AdminModeDisplay.class);
    private static final String FXML = "AdminModeDisplay.fxml";

    @FXML
    private Label adminModeDisplay;

    public AdminModeDisplay(boolean isAdminMode) {
        super(FXML);

        adminModeDisplay.setText(adminModeText);
        setLabelStyle(isAdminMode);

        registerAsAnEventHandler(this);
    }

    private void setLabelStyle(boolean isAdminMode) {
        ObservableList<String> styleClass = this.adminModeDisplay.getStyleClass();
        if (isAdminMode) {
            styleClass.remove(LABEL_STYLE_CLASS_NON_ADMIN);
            styleClass.add(LABEL_STYLE_CLASS_ADMIN);
        } else {
            styleClass.remove(LABEL_STYLE_CLASS_ADMIN);
            styleClass.add(LABEL_STYLE_CLASS_NON_ADMIN);
        }
    }

    @Subscribe
    private void handleUserModeChangedEvent(UserModeChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> setLabelStyle(event.isAdminMode));
    }

}
```
###### \java\seedu\ptman\ui\EmployeeListPanel.java
``` java
    /**
     * Scrolls to the top of the {@code EmployeeListPanel} and deselect any current selection.
     */
    private void scrollToTopAndDeselect() {
        Platform.runLater(() -> {
            employeeListView.scrollTo(0);
            employeeListView.getSelectionModel().clearSelection();
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.isNewSelection) {
            scrollToAndSelect(event.targetIndex);
        } else {
            scrollToTopAndDeselect();
        }
    }

```
###### \java\seedu\ptman\ui\MainWindow.java
``` java
    private final KeyCombination keyCtrlShiftLeft =
            new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN);
    private final KeyCombination keyCtrlShiftRight =
            new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN);
    private final KeyCombination keyCtrlShiftDown =
            new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHIFT_DOWN, KeyCombination.SHORTCUT_DOWN);

```
###### \java\seedu\ptman\ui\MainWindow.java
``` java
    /**
     * Listens to the {@code keyEvent} that requests to navigate to the prev/next timetable view and handles it.
     * Windows: Ctrl, Mac: Command
     */
    private void setEventHandlerForTimetableWeekChangeRequestEvent() {
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (keyCtrlShiftLeft.match(event)) {
                event.consume();
                logger.fine("Timetable view requested to display the previous week.");
                raise(new TimetableWeekChangeRequestEvent(TimetableWeekChangeRequestEvent.WeekChangeRequest.PREVIOUS));
            } else if (keyCtrlShiftRight.match(event)) {
                event.consume();
                logger.fine("Timetable view requested to display the next week.");
                raise(new TimetableWeekChangeRequestEvent(TimetableWeekChangeRequestEvent.WeekChangeRequest.NEXT));
            } else if (keyCtrlShiftDown.match(event)) {
                event.consume();
                logger.fine("Timetable view requested to display the current week.");
                raise(new TimetableWeekChangeRequestEvent(TimetableWeekChangeRequestEvent.WeekChangeRequest.CURRENT));
            }
        });
    }

```
###### \java\seedu\ptman\ui\PtmanLogoDisplay.java
``` java
/**
 * Displays the PTMan logo at the left side of the command box.
 */
public class PtmanLogoDisplay extends UiPart<Region> {

    private static final String imagePath = "/icons/ptman_logo_icon.png";
    private static final String FXML = "PtmanLogoDisplay.fxml";

    @FXML
    private ImageView ptmanLogoView;

    public PtmanLogoDisplay() {
        super(FXML);

        Image ptmanLogo = new Image(imagePath);
        ptmanLogoView.setImage(ptmanLogo);
        setLogoSize();
    }

    /**
     * Scale the logo image to the desired size
     */
    private void setLogoSize() {
        ptmanLogoView.setFitWidth(35);
        ptmanLogoView.setPreserveRatio(true);
    }
}
```
###### \java\seedu\ptman\ui\TimetablePanel.java
``` java
/**
 * Displays the GUI Timetable.
 */
public class TimetablePanel extends UiPart<Region> {

    public static final String TIMETABLE_IMAGE_FILE_NAME_DEFAULT = "MyTimetable";
    public static final String TIMETABLE_IMAGE_FILE_FORMAT = "png";

    private static final int TIMETABLE_IMAGE_PIXEL_SCALE = 2;
    private static final String FXML = "TimetablePanel.fxml";
    private static final int MAX_SLOTS_LEFT_RUNNING_OUT = 3;

    private static final Style ENTRY_GREEN_STYLE = Style.STYLE1;
    private static final Style ENTRY_BLUE_STYLE = Style.STYLE2;
    private static final Style ENTRY_YELLOW_STYLE = Style.STYLE3;
    private static final Style ENTRY_RED_STYLE = Style.STYLE5;
    private static final Style ENTRY_BROWN_STYLE = Style.STYLE7;

    private static Calendar timetableAvail;
    private static Calendar timetableEmployee;
    private static Calendar timetableFull;
    private static Calendar timetableOthers;
    private static Calendar timetableRunningOut;

    protected Logic logic;

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private VBox timetableBox;

    @FXML
    private CalendarView timetableView;

    @FXML
    private BorderPane navBar;

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Label monthDisplay;

    private ObservableList<Shift> shiftObservableList;
    private OutletInformation outletInformation;

    private Employee currentEmployee = null;

    public TimetablePanel(Logic logic) {
        super(FXML);

        this.logic = logic;
        this.shiftObservableList = logic.getFilteredShiftList();
        this.outletInformation = logic.getOutletInformation();

        setUpNavBar();
        setTimetableViewStyle();
        showRelevantViewsOnly();

        // disable clicks on timetable view
        timetableView.getWeekPage().setMouseTransparent(true);

        updateTimetableView();

        registerAsAnEventHandler(this);
    }

    public static Calendar getTimetableAvail() {
        return timetableAvail;
    }

    public static Calendar getTimetableRunningOut() {
        return timetableRunningOut;
    }

    public static Calendar getTimetableFull() {
        return timetableFull;
    }

    public static Calendar getTimetableEmployee() {
        return timetableEmployee;
    }

    public static Calendar getTimetableOthers() {
        return timetableOthers;
    }

    /**
     * Sets up the navigation bar and its behavior
     */
    private void setUpNavBar() {
        setMonthDisplay(logic.getCurrentDisplayedDate());
        navBar.setLeft(prevButton);
        navBar.setRight(nextButton);
        navBar.setCenter(monthDisplay);

        prevButton.setOnAction(value -> Platform.runLater(() -> navigateToPreviousWeek()));
        nextButton.setOnAction(value -> Platform.runLater(() -> navigateToNextWeek()));
    }

    /**
     * Sets the displayed month on the {@code monthDisplay}.
     * Month is determined by the majority date displayed in the timetable view.
     * This means that if there are 4 dates that are of month April and 3 dates that are of month May,
     * April will be displayed on the {@code monthDisplay}.
     */
    private void setMonthDisplay(LocalDate date) {
        LocalDate thursdayDate = DateUtil.getThursdayOfDate(date);
        monthDisplay.setText(DateUtil.getMonthYearFromDate(thursdayDate));
    }

    /**
     * Sets the style of the timetable view
     */
    private void setTimetableViewStyle() {
        ObservableList<String> styleClass = timetableView.getStyleClass();
        styleClass.add("timetable-container");
    }

    /**
     * Only show the parts of CalendarFX that we need.
     */
    private void showRelevantViewsOnly() {
        timetableView.showWeekPage();

        timetableView.getWeekPage().setShowNavigation(false);
        timetableView.getWeekPage().setShowDate(false);
        timetableView.weekFieldsProperty().setValue(WeekFields.of(Locale.FRANCE)); // Start week from Monday
        timetableView.setShowToday(true);
        timetableView.setShowPrintButton(true);
        timetableView.setShowAddCalendarButton(false);
        timetableView.setShowSearchField(false);
        timetableView.setShowToolBar(false);
        timetableView.setShowPageSwitcher(false);
        timetableView.setShowPageToolBarControls(false);
        timetableView.setShowSearchResultsTray(false);
        timetableView.setShowSourceTray(false);
        timetableView.setShowSourceTrayButton(false);
        timetableView.getWeekPage().getDetailedWeekView().setShowAllDayView(false);
    }

    /**
     * This ensures that the range of the times shown by the timetable view is constrained to the
     * operating hours of the outlet.
     * Also ensures that no scrolling is required to view the entire timetable.
     */
    private void setTimetableRange() {
        LocalTime startTime = outletInformation.getOperatingHours().getStartTime();
        LocalTime endTime = outletInformation.getOperatingHours().getEndTime();
        timetableView.setStartTime(startTime);
        timetableView.setEndTime(endTime);

        DetailedWeekView detailedWeekView = timetableView.getWeekPage().getDetailedWeekView();
        detailedWeekView.setEarlyLateHoursStrategy(DayViewBase.EarlyLateHoursStrategy.HIDE);
        detailedWeekView.setHoursLayoutStrategy(DayViewBase.HoursLayoutStrategy.FIXED_HOUR_COUNT);
        detailedWeekView.setVisibleHours((int) ChronoUnit.HOURS.between(startTime, endTime));
        detailedWeekView.setShowScrollBar(false);
        detailedWeekView.setEnableCurrentTimeMarker(false);
    }

    private void setCurrentDisplayedDate() {
        timetableView.setDate(logic.getCurrentDisplayedDate());
    }

    /**
     * Takes default outlet shifts and set timetable entries based on these shifts.
     */
    private void setShifts() {
        int index = 1;
        for (Shift shift: shiftObservableList) {
            LocalDate date = shift.getDate().getLocalDate();
            Interval timeInterval = new Interval(date, shift.getStartTime().getLocalTime(),
                    date, shift.getEndTime().getLocalTime());
            Entry<String> shiftEntry = new Entry<>("SHIFT " + index + "\nSlots left: " + shift.getSlotsLeft()
                    + "/" + shift.getCapacity().getCapacity(), timeInterval);
            setEntryType(shift, shiftEntry);
            index++;
        }
    }

    /**
     * Sets the entry type (aka the color) of the shift in the timetable
     * @param shift
     * @param shiftEntry
     */
    private void setEntryType(Shift shift, Entry<String> shiftEntry) {
        Calendar entryType;
        if (currentEmployee != null) {
            entryType = getEntryTypeEmployee(shift);
        } else {
            entryType = getEntryTypeMain(shift);
        }
        entryType.addEntry(shiftEntry);
    }

    /**
     * @return the entryType (a Calendar object) for the shift in the main timetable view, which reflects
     * the color of the shift in the timetableView.
     */
    private Calendar getEntryTypeMain(Shift shift) {
        float ratio = (float) shift.getSlotsLeft() / (float) shift.getCapacity().getCapacity();
        if (ratio <= 0) {
            return timetableFull;
        } else if (ratio <= 0.5 || shift.getCapacity().getCapacity() < MAX_SLOTS_LEFT_RUNNING_OUT) {
            return timetableRunningOut;
        } else {
            return timetableAvail;
        }
    }

    /**
     * @return the entryType (a Calendar object) for the shift in the employee timetable view, which reflects
     * the color of the shift in the timetableView.
     */
    private Calendar getEntryTypeEmployee(Shift shift) {
        if (shift.containsEmployee(currentEmployee)) {
            return timetableEmployee;
        } else {
            return timetableOthers;
        }
    }

    /**
     * Navigates the timetable view to the following week.
     */
    private void navigateToNextWeek() {
        logic.setFilteredShiftListToNextWeek();
        shiftObservableList = logic.getFilteredShiftList();
        updateTimetableView();
    }

    /**
     * Navigates the timetable view to the previous week.
     */
    private void navigateToPreviousWeek() {
        logic.setFilteredShiftListToPrevWeek();
        shiftObservableList = logic.getFilteredShiftList();
        updateTimetableView();
    }

    /**
     * Navigates the timetable view to the current week.
     */
    private void navigateToCurrWeek() {
        logic.setFilteredShiftListToCurrentWeek();
        shiftObservableList = logic.getFilteredShiftList();
        updateTimetableView();
    }

    /**
     * Replaces the timetable view with a new timetable, with shifts taken by the employee being highlighted
     * @param employee
     */
    private void loadEmployeeTimetable(Employee employee) {
        currentEmployee = employee;
        updateTimetableView();
    }

    /**
     * Replaces the timetable view with a default timetable with no employee being selected.
     */
    private void loadDefaultTimetable() {
        currentEmployee = null;
        updateTimetableView();
    }

    /**
     * Replaces timetableView with a new timetable with updated shift and outlet information
     */
    private void updateTimetableView() {
        setCurrentDisplayedDate();
        setMonthDisplay(logic.getCurrentDisplayedDate());
        resetTimetableView();
        setTimetableRange();
    }

    /**
     * Clear current timetable view and resets it to a new timetable view with updated shifts.
     */
    private void resetTimetableView() {
        timetableView.getCalendarSources().clear();
        CalendarSource calendarSource = new CalendarSource("Shifts");
        addCalendars(calendarSource);

        setShifts();
        timetableView.getCalendarSources().add(calendarSource);
    }

    /**
     * Initialises all the Calendar objects
     */
    private void initialiseEntries() {
        timetableAvail = new Calendar("Available");
        timetableRunningOut = new Calendar("Running Out");
        timetableFull = new Calendar("Full");
        timetableEmployee = new Calendar("Employee's shift");
        timetableOthers = new Calendar("Other shifts");
    }

    /**
     * Sets the color styles of the entries
     */
    private void setEntryStyles() {
        timetableAvail.setStyle(ENTRY_GREEN_STYLE);
        timetableRunningOut.setStyle(ENTRY_YELLOW_STYLE);
        timetableFull.setStyle(ENTRY_RED_STYLE);
        timetableEmployee.setStyle(ENTRY_BLUE_STYLE);
        timetableOthers.setStyle(ENTRY_BROWN_STYLE);
    }

    /**
     * Adds all relevant Calendars (entryTypes) to its source
     */
    private void addCalendars(CalendarSource calendarSource) {
        initialiseEntries();
        setEntryStyles();
        calendarSource.getCalendars().addAll(timetableAvail, timetableRunningOut, timetableFull,
                timetableEmployee, timetableOthers);
    }

    /**
     * Takes a snapshot of the timetable panel
     */
    private WritableImage takeSnapshot() {
        WritableImage timetableWritableImage = new WritableImage(
                (int) (TIMETABLE_IMAGE_PIXEL_SCALE * timetableBox.getWidth()),
                (int) (TIMETABLE_IMAGE_PIXEL_SCALE * timetableBox.getHeight()));
        SnapshotParameters spa = new SnapshotParameters();
        spa.setTransform(Transform.scale(TIMETABLE_IMAGE_PIXEL_SCALE, TIMETABLE_IMAGE_PIXEL_SCALE));
        WritableImage snapshot = timetableBox.snapshot(spa, timetableWritableImage);
        return snapshot;
    }

    /**
     * Exports timetable as image and save it locally
     */
    private void exportTimetableAsImage(String filename) {
        File imageFile = new File("." + File.separator + filename + "." + TIMETABLE_IMAGE_FILE_FORMAT);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(takeSnapshot(), null), TIMETABLE_IMAGE_FILE_FORMAT, imageFile);
        } catch (IOException e) {
            logger.warning("Error taking snapshot of timetable.");
        }
    }

    /**
     * Exports timetable as image and email it
     * @param email
     */
    private void exportTimetableAsImageAndEmail(String filename, Email email) {
        String pathName = "." + File.separator + filename + "." + TIMETABLE_IMAGE_FILE_FORMAT;
        File imageFile = new File(pathName);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(takeSnapshot(), null), TIMETABLE_IMAGE_FILE_FORMAT, imageFile);
            EmailService emailService = EmailService.getInstance();
            emailService.sendTimetableAttachment(email.toString(), pathName);
        } catch (IOException e) {
            logger.warning("Error taking snapshot of timetable.");
        } catch (MessagingException e) {
            logger.warning("Error sending timetable as email.");
        }

        try {
            Files.deleteIfExists(Paths.get(pathName));
        } catch (IOException e) {
            logger.warning("Error deleting exported and emailed timetable image.");
        }
    }

    @Subscribe
    private void handlePartTimeManagerChangedEvent(PartTimeManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event) + ": Updating timetable view....");
        Platform.runLater(() -> updateTimetableView());
    }

    @Subscribe
    private void handleEmployeePanelSelectionChangedEvent(EmployeePanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        Platform.runLater(() -> {
            if (event.hasNewSelection()) {
                loadEmployeeTimetable(event.getNewSelection().employee);
            } else {
                loadDefaultTimetable();
            }
        });
    }

    @Subscribe
    private void handleTimetableWeekChangeRequestEvent(TimetableWeekChangeRequestEvent event) {
        WeekChangeRequest request = event.getRequest();
        Platform.runLater(() -> {
            if (request == WeekChangeRequest.NEXT) {
                logger.info(LogsCenter.getEventHandlingLogMessage(event)
                        + ": Navigating timetable to the next week....");
                navigateToNextWeek();
            } else if (request == WeekChangeRequest.PREVIOUS) {
                logger.info(LogsCenter.getEventHandlingLogMessage(event)
                        + ": Navigating timetable to the previous week....");
                navigateToPreviousWeek();
            } else if (request == WeekChangeRequest.CURRENT) {
                logger.info(LogsCenter.getEventHandlingLogMessage(event)
                        + ": Navigating timetable to the current week....");
                navigateToCurrWeek();
            }
        });
    }

    @Subscribe
    private void handleExportTimetableAsImageRequestEvent(ExportTimetableAsImageRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event) + ": Exporting timetable as image....");
        Platform.runLater(() -> exportTimetableAsImage(event.filename));
    }

    @Subscribe
    private void handleExportTimetableAsImageAndEmailRequestEvent(ExportTimetableAsImageAndEmailRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event)
                + ": Exporting timetable as image to send email....");
        Platform.runLater(() -> exportTimetableAsImageAndEmail(event.filename, event.email));
    }

}
```
###### \resources\view\AdminModeDisplay.fxml
``` fxml
<StackPane styleClass="anchor-pane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <Label fx:id="adminModeDisplay" />
</StackPane>
```
###### \resources\view\EmployeeListCard.fxml
``` fxml
      <StackPane minWidth="45" prefWidth="45" maxWidth="45">
        <Circle fx:id="idCircle" radius="18" fill="transparent" stroke="#5AC2BC"/>
        <Label fx:id="id" styleClass="cell_big_label"/>
      </StackPane>
```
###### \resources\view\MainWindow.fxml
``` fxml
        <HBox styleClass="pane-with-border">
          <StackPane VBox.vgrow="NEVER" fx:id="ptmanLogoDisplayPlaceholder" maxWidth="50">
            <padding>
              <Insets top="5" right="10" bottom="5" left="10" />
            </padding>
          </StackPane>

          <StackPane VBox.vgrow="NEVER" HBox.hgrow="ALWAYS" fx:id="commandBoxPlaceholder">
            <padding>
              <Insets top="5" right="0" bottom="5" left="0" />
            </padding>
          </StackPane>

          <StackPane VBox.vgrow="NEVER" HBox.hgrow="NEVER" fx:id="adminModeDisplayPlaceholder"
                      minWidth="115" prefWidth="115" maxWidth="115">
            <padding>
              <Insets top="5" right="10" bottom="5" left="10" />
            </padding>
          </StackPane>
        </HBox>

        <SplitPane id="splitPane" fx:id="splitPane" dividerPositions="0.8" VBox.vgrow="ALWAYS">
          <VBox>
            <StackPane VBox.vgrow="NEVER" fx:id="resultDisplayPlaceholder" styleClass="pane-with-dark-background"
                       minHeight="100" prefHeight="100" maxHeight="100">
              <padding>
                <Insets top="10" right="15" bottom="10" left="15" />
              </padding>
            </StackPane>

            <StackPane VBox.vgrow="NEVER" fx:id="outletDetailsPanelPlaceholder" >
              <padding>
                <Insets top="5" right="15" bottom="5" left="15" />
              </padding>
            </StackPane>

            <StackPane fx:id="timetablePanelPlaceholder" prefWidth="340">
              <padding>
                <Insets top="5" right="15" bottom="10" left="15" />
              </padding>
            </StackPane>
          </VBox>

          <VBox fx:id="employeeList" minWidth="260" SplitPane.resizableWithParent="false">
            <StackPane fx:id="employeeListPanelPlaceholder" VBox.vgrow="ALWAYS"/>
          </VBox>

        </SplitPane>

        <StackPane fx:id="statusbarPlaceholder" VBox.vgrow="NEVER" />
      </VBox>
    </Scene>
  </scene>
</fx:root>
```
###### \resources\view\OutletDetailsPanel.fxml
``` fxml
<StackPane xmlns="http://javafx.com/javafx/8.0.121" xmlns:fx="http://javafx.com/fxml/1">
    <VBox>
        <Label fx:id="outletNamePanelHeader" styleClass="panel_large_label" />
        <HBox fx:id="outletInformation">
            <Label fx:id="operatingHours"><graphic><FontAwesomeIconView fill="#7A8492" glyphName="CALENDAR" /></graphic></Label>
            <Label fx:id="outletContact"><graphic><FontAwesomeIconView fill="#7A8492" glyphName="PHONE" /></graphic></Label>
            <Label fx:id="outletEmail"><graphic><FontAwesomeIconView fill="#7A8492" glyphName="ENVELOPE" /></graphic></Label>
        </HBox>
        <Label fx:id="announcement"><graphic><FontAwesomeIconView fill="#7A8492" glyphName="BULLHORN" /></graphic></Label>
    </VBox>
</StackPane>
```
###### \resources\view\PtmanLogoDisplay.fxml
``` fxml
<StackPane xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <ImageView fx:id="ptmanLogoView" />
</StackPane>
```
###### \resources\view\PtmanTheme.css
``` css
/* Fonts */
@font-face {
    src: url("/fonts/Gotham-Rounded-Book.ttf");
}

@font-face {
    src: url("/fonts/Gotham-Rounded-Medium.ttf");
}

@font-face {
    font-family: "Proxima Nova Alt";
    src: url("/fonts/Proxima-Nova-Alt-Regular.ttf");
    font-weight: normal;
    font-style: normal;
}

@font-face {
    font-family: "Proxima Nova Alt";
    src: url("/fonts/Proxima-Nova-Alt-Bold.ttf");
    font-weight: bold;
    font-style: normal;
}


/* Classes */
.root {
    -fx-accent: #8E9AAA;
    -fx-focus-color: #8E9AAA;
}

.background {
    -fx-background-color: #FFFFFF;
    background-color: #FFFFFF; /* Used in the default.html file */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Gotham Rounded Book";
    -fx-text-fill: #7A8492;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Gotham Rounded Book";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Gotham Rounded Book";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-admin-mode {
    -fx-hgap: 7;
    -fx-vgap: 3;
    -fx-background-color: #E16A66;
    -fx-text-fill: white;
    -fx-padding: 5 5 5 5;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 12;
}

.label-non-admin-mode {
    -fx-hgap: 7;
    -fx-vgap: 3;
    -fx-text-fill: transparent;
    -fx-padding: 2 4 2 4;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 12;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Gotham Rounded Book";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #FFFFFF;
    -fx-control-inner-background: #FFFFFF;
    -fx-background-color: #FFFFFF;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        #FFFFFF
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Proxima Nova Alt";
    -fx-font-weight: bold;
    -fx-text-fill: white;
    -fx-alignment: center-left;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: #DDDEE4;
    -fx-padding: 1;
}

.split-pane {
    -fx-background-color: #FFFFFF;
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: #FFFFFF;
}

.list-cell {
    -fx-label-padding: 12 6 12 6;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
    -fx-background-color: #FFFFFF;
}

.list-cell:filled {
    -fx-background-color: #FFFFFF;
    -fx-border-color: transparent transparent #DDDEE4 transparent;
    -fx-border-width: 1px;
}

.list-cell:filled:selected {
    -fx-background-color: #F5F8FA;
}

.list-cell:filled:selected #idCircle {
    -fx-fill: #5AC2BC;
}

.list-cell:filled:selected #id {
    -fx-text-fill: white;
    -fx-font-weight: bold;
}

.list-cell .label {
    -fx-text-fill: #7A8492;
}

.panel_medium_text {
    -fx-font-family: "Proxima Nova Alt";
    -fx-font-size: 13px;
    -fx-text-fill: white;
}

.panel_big_label {
    -fx-font-family: "Proxima Nova Alt";
    -fx-font-weight: bold;
    -fx-font-size: 16px;
    -fx-text-fill: white;
    -fx-padding: 10 15 10 20;
}

.panel_large_label {
    -fx-font-family: "Proxima Nova Alt";
    -fx-font-weight: bold;
    -fx-font-size: 26px;
    -fx-font-weight: bold;
    -fx-text-fill: #5AC2BC;
    -fx-padding: 18 5 5 15;
}

.cell_big_label {
    -fx-font-family: "Gotham Rounded Medium";
    -fx-font-size: 16px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "Gotham Rounded Book";
    -fx-font-size: 12px;
    -fx-padding: 2 0 0 0;
    -fx-text-fill: #010504;
}

.nav-bar-content {
    -fx-padding: 6 15 6 15;
}

.nav-bar-label {
    -fx-font-family: "Gotham Rounded Medium";
    -fx-font-size: 16px;
}

.nav-bar-button {
    -fx-font-size: 17px;
}

.anchor-pane {
     -fx-background-color: #FFFFFF;
}

.pane-with-border {
     -fx-background-color: #FFFFFF;
     -fx-border-color: transparent transparent #DDDEE4 transparent;
     -fx-border-top-width: 1px;
}

.pane-with-dark-background {
     -fx-background-color: #F5F8FA;
}

.pane-with-mint-background {
    -fx-background-color: #5AC2BC;
}

.status-bar {
    -fx-background-color: #EFF2F5;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Gotham Rounded Book";
    -fx-font-size: 15px;
    -fx-text-fill: #7A8492;
}

.status-bar .label {
    -fx-font-family: "Proxima Nova Alt";
    -fx-text-fill: #7A8492;
}

.status-bar-with-border {
    -fx-background-color: #EFF2F5;
    -fx-border-color: #EFF2F5;
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: white;
}

.grid-pane {
    -fx-background-color: #EFF2F5;
}

.grid-pane .anchor-pane {
    -fx-padding: 5 10 5 10;
}

.context-menu {
    -fx-background-color: #EFF2F5;
}

.context-menu .label {
    -fx-text-fill: #7A8492;
}

.menu-bar {
    -fx-background-color: #EFF2F5;
}

.menu-bar .label {
    -fx-font-size: 15px;
    -fx-font-family: "Gotham Rounded Book";
    -fx-text-fill: #7A8492;
}

.menu-button:hover,
.menu-button:focused,
.menu-button:showing {
    -fx-background-color: #5AC2BC;
}

.menu-button:hover > .label,
.menu-button:focused > .label,
.menu-button:showing > .label {
    -fx-text-fill: white;
}

.menu-item:hover,
.menu-item:focused {
    -fx-background-color: #EFF2F5;
    -fx-cursor: hand;
}

.menu-item:hover > .label,
.menu-item:focused > .label {
    -fx-text-fill: #7A8492;
}

.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-border-width: 0;
    -fx-background-radius: 0;
    -fx-background-color: transparent;
    -fx-font-family: "Proxima Nova Alt", "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-text-fill: #7A8492;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-cursor: hand;
    -fx-text-fill: #8E9AAA;
}

.button:focused {
    -fx-background-insets: 0;
    -fx-background-radius: 0, 0;
}

.button:pressed, .button:default:hover:pressed {
  -fx-text-fill: #8E9AAAF;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #FFFFFF;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: #EFF2F5;
}

.dialog-pane {
    -fx-background-color: #FFFFFF;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #FFFFFF;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: #FFFFFF;
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: #EFF2F5;
}

.scroll-bar .thumb {
    -fx-background-color: #CDCDCD;
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

.timetable-container {
    -fx-opacity: 0.8;
}


/* Identities */
#outletInformation {
    -fx-padding: 5 5 3 20;
}

#outletInformation .label {
    -fx-font-family: "Gotham Rounded Book";
    -fx-font-size: 15px;
    -fx-text-fill: #7A8492;
}

#announcement {
    -fx-font-family: "Gotham Rounded Book";
    -fx-font-size: 15px;
    -fx-text-fill: #7A8492;
    -fx-padding: 0 5 10 20;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#id {
    -fx-font-family: "Proxima Nova Alt";
    -fx-text-fill: #5AC2BC;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #7A8492;
}


#commandTextField {
    -fx-background-color: transparent #FFFFFF transparent #FFFFFF;
    -fx-background-insets: 0;
    -fx-border-insets: 0;
    -fx-font-family: "Gotham Rounded Book";
    -fx-font-size: 16px;
    -fx-text-fill: #7A8492;
}

#resultDisplay .content {
    -fx-background-color: #F5F8FA;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 8;
    -fx-vgap: 3;
    -fx-padding: 5 0 3 0;
}

#tags .label {
    -fx-text-fill: white;
    -fx-padding: 3 4 3 4;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}

#tags .salmon {
    -fx-background-color: #E57373;
}

#tags .mint {
    -fx-background-color: #80CBC4;
}

#tags .teal {
    -fx-background-color: #26A69A;
}

#tags .turquoise {
    -fx-background-color: #4DD0E1;
}

#tags .pink {
    -fx-background-color: #F06292;
}

#tags .blue {
    -fx-background-color: #42A5F5;
}

#tags .purple {
    -fx-background-color: #9575CD;
}

#tags .pale-blue {
    -fx-background-color: #82B1FF;
}

#tags .indigo {
    -fx-background-color: #5C6BC0;
}

#tags .yellow {
    -fx-background-color: #FFC107;
}
```
###### \resources\view\TimetablePanel.fxml
``` fxml

<?import javafx.scene.control.Button?>
<StackPane xmlns="http://javafx.com/javafx/8.0.121" xmlns:fx="http://javafx.com/fxml/1">
    <VBox fx:id="timetableBox">
        <BorderPane fx:id="navBar">
            <left><StackPane styleClass="nav-bar-content">
                <Button fx:id="prevButton" styleClass="nav-bar-button">
                    <graphic><FontAwesomeIconView fill="#7A8492" glyphName="ANGLE_LEFT" size="17.0" /></graphic>
                </Button>
            </StackPane></left>

            <center><StackPane styleClass="nav-bar-content">
                <Label fx:id="monthDisplay" styleClass="nav-bar-label" />
            </StackPane></center>

            <right><StackPane styleClass="nav-bar-content">
                <Button fx:id="nextButton" styleClass="nav-bar-button">
                    <graphic><FontAwesomeIconView fill="#7A8492" glyphName="ANGLE_RIGHT" size="17.0" /></graphic>
                </Button>
            </StackPane></right>
        </BorderPane>
        <CalendarView fx:id="timetableView" />
    </VBox>
</StackPane>
```
