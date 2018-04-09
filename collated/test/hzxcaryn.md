# hzxcaryn
###### \java\guitests\guihandles\AdminModeDisplayHandle.java
``` java
/**
 * A handler for the {@code AdminModeDetails} of the UI
 */
public class AdminModeDisplayHandle extends NodeHandle<Label> {

    public static final String ADMIN_MODE_DISPLAY_LABEL_ID = "#adminModeDisplay";

    public AdminModeDisplayHandle (Label adminModeDisplayNode) {
        super(adminModeDisplayNode);
    }

    /**
     * @return the text in the admin mode label
     */
    public String getText() {
        return getRootNode().getText();
    }

    /**
     * @return the list of style classes present in the admin mode display
     */
    public List<String> getStyleClass() {
        return getRootNode().getStyleClass();
    }

}
```
###### \java\guitests\guihandles\OutletDetailsPanelHandle.java
``` java
/**
 * A handler for the {@code OutletDetailsPanel} of the UI
 */
public class OutletDetailsPanelHandle extends NodeHandle<Node> {

    public static final String OUTLET_ID = "#outletDetailsPanelPlaceholder";

    private static final String OUTLET_NAME_ID = "#outletNamePanelHeader";
    private static final String OUTLET_OPERATING_HOURS_ID = "#operatingHours";
    private static final String OUTLET_CONTACT_ID = "#outletContact";
    private static final String OUTLET_EMAIL_ID = "#outletEmail";
    private static final String OUTLET_ANNOUNCEMENT_ID = "#announcement";

    private final Label outletNameNode;
    private final Label outletOperatingHoursNode;
    private final Label outletContactNode;
    private final Label outletEmailNode;
    private final Label outletAnnouncementNode;

    private String lastRememberedOutletName;
    private String lastRememberedOutletOperatingHours;
    private String lastRememberedOutletContact;
    private String lastRememberedOutletEmail;
    private String lastRememberedOutletAnnouncement;

    public OutletDetailsPanelHandle(Node outletDetailsNode) {
        super(outletDetailsNode);

        this.outletNameNode = getChildNode(OUTLET_NAME_ID);
        this.outletOperatingHoursNode = getChildNode(OUTLET_OPERATING_HOURS_ID);
        this.outletContactNode = getChildNode(OUTLET_CONTACT_ID);
        this.outletEmailNode = getChildNode(OUTLET_EMAIL_ID);
        this.outletAnnouncementNode = getChildNode(OUTLET_ANNOUNCEMENT_ID);
    }

    /**
     * @return the outlet name in outlet panel.
     */
    public String getOutletName() {
        return outletNameNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getAnnouncement() {
        return outletAnnouncementNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getOutletOperatingHours() {
        return outletOperatingHoursNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getOutletContact() {
        return outletContactNode.getText();
    }

    /**
     * @return the outlet info in outlet panel.
     */
    public String getOutletEmail() {
        return outletEmailNode.getText();
    }

    /**
     * Remembers the outlet name in the outlet panel.
     */
    public void rememberOutletName() {
        lastRememberedOutletName = getOutletName();
    }

    /**
     * Returns true if the current outlet name is different from the value remembered by the most recent
     * {@code rememberOutletName()} call.
     */
    public boolean isOutletNameChanged() {
        return !lastRememberedOutletName.equals(getOutletName());
    }

    /**
     * Remembers the outlet info in the outlet panel.
     */
    public void rememberOutletInformation() {
        lastRememberedOutletOperatingHours = getOutletOperatingHours();
        lastRememberedOutletContact = getOutletContact();
        lastRememberedOutletEmail = getOutletEmail();
    }

    /**
     * Returns true if the current outlet information is different from the value remembered by the most
     * recent {@code rememberOutletInformation()} call.
     */
    public boolean isOutletInformationChanged() {
        boolean isOutletOperatingHoursChanged = !lastRememberedOutletOperatingHours.equals(getOutletOperatingHours());
        boolean isOutletContactChanged = !lastRememberedOutletContact.equals(getOutletContact());
        boolean isOutletEmailChanged = !lastRememberedOutletEmail.equals(getOutletContact());
        return isOutletOperatingHoursChanged && isOutletContactChanged && isOutletEmailChanged;
    }

    /**
     * Remembers the outlet name in the outlet panel.
     */
    public void rememberOutletAnnouncement() {
        lastRememberedOutletAnnouncement = getAnnouncement();
    }

    /**
     * Returns true if the current outlet name is different from the value remembered by the most recent
     * {@code rememberOutletName()} call.
     */
    public boolean isOutletAnnouncementChanged() {
        return !lastRememberedOutletAnnouncement.equals(getAnnouncement());
    }

}
```
###### \java\guitests\guihandles\TimetableViewHandle.java
``` java
/**
 * A handler for the {@code TimetableView} of the UI
 */
public class TimetableViewHandle extends NodeHandle<Node> {

    public static final String TIMETABLE_ID = "#timetableViewPlaceholder";

    protected TimetableViewHandle(Node rootNode) {
        super(rootNode);
    }
}
```
###### \java\seedu\ptman\logic\commands\ExportCommandTest.java
``` java
public class ExportCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_exportAndSave_success() {
        CommandResult result = new ExportCommand().execute();
        assertEquals(MESSAGE_SAVE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ExportTimetableAsImageRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void execute_exportAndEmail_success() {
        CommandResult result = new ExportCommand(new Email("email@example.com")).execute();
        assertEquals(MESSAGE_EMAIL_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent()
                instanceof ExportTimetableAsImageAndEmailRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

    @Test
    public void equals() {
        Email aliceEmail = new Email("alice@example.com");
        Email bobEmail = new Email("bob@example.com");
        ExportCommand exportAliceCommand = new ExportCommand(aliceEmail);
        ExportCommand exportBobCommand = new ExportCommand(bobEmail);

        // same object -> returns true
        assertTrue(exportAliceCommand.equals(exportAliceCommand));

        // same values -> returns true
        ExportCommand exportAliceCommandCopy = new ExportCommand(aliceEmail);
        assertTrue(exportAliceCommand.equals(exportAliceCommandCopy));

        // different types -> returns false
        assertFalse(exportAliceCommand.equals(1));

        // null -> returns false
        assertFalse(exportAliceCommand.equals(null));

        // different email -> returns false
        assertFalse(exportAliceCommand.equals(exportBobCommand));
    }
}
```
###### \java\seedu\ptman\logic\commands\MainCommandTest.java
``` java
public class MainCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_main_success() {
        CommandResult result = new MainCommand().execute();
        assertEquals(MESSAGE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof JumpToListRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }
}
```
###### \java\seedu\ptman\logic\parser\ExportCommandParserTest.java
``` java
public class ExportCommandParserTest {
    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Email expectedEmail = new Email(VALID_EMAIL_BOB);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + EMAIL_DESC_BOB, new ExportCommand(expectedEmail));

        // multiple emails - last email accepted
        assertParseSuccess(parser, EMAIL_DESC_AMY + EMAIL_DESC_BOB, new ExportCommand(expectedEmail));
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + EMAIL_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));

        assertParseFailure(parser, PREAMBLE_NON_EMPTY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
    }

}
```
###### \java\seedu\ptman\logic\parser\PartTimeManagerParserTest.java
``` java
    @Test
    public void parseCommand_export() throws Exception {
        String email = "example@gmail.com";
        ExportCommand command = (ExportCommand) parser.parseCommand(ExportCommand.COMMAND_WORD + " "
                + PREFIX_EMAIL + email);
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD) instanceof ExportCommand);
        assertEquals(new ExportCommand(new Email(email)), command);
    }

    @Test
    public void parseCommand_exportAlias() throws Exception {
        String email = "example@gmail.com";
        ExportCommand command = (ExportCommand) parser.parseCommand(ExportCommand.COMMAND_ALIAS + " "
                + PREFIX_EMAIL + email);
        assertTrue(parser.parseCommand(ExportCommand.COMMAND_WORD) instanceof ExportCommand);
        assertEquals(new ExportCommand(new Email(email)), command);
    }

```
###### \java\seedu\ptman\logic\parser\PartTimeManagerParserTest.java
``` java
    @Test
    public void parseCommand_main() throws Exception {
        assertTrue(parser.parseCommand(MainCommand.COMMAND_WORD) instanceof MainCommand);
        assertTrue(parser.parseCommand(MainCommand.COMMAND_WORD + " 3") instanceof MainCommand);
    }

```
###### \java\seedu\ptman\ui\AdminModeDisplayTest.java
``` java
public class AdminModeDisplayTest extends GuiUnitTest {

    private UserModeChangedEvent userModeChangedEventLoginStub = new UserModeChangedEvent(true);
    private UserModeChangedEvent userModeChangedEventLogoutStub = new UserModeChangedEvent(false);

    private ArrayList<String> defaultStyleOfAdminModeDetails;
    private ArrayList<String> loginStyleOfAdminModeDetails;

    private AdminModeDisplayHandle adminModeDisplayHandle;

    @Before
    public void setUp() {
        Model model = new ModelManager();
        Logic logic = new LogicManager(model);

        AdminModeDisplay adminModeDisplay = new AdminModeDisplay(logic.isAdminMode());
        uiPartRule.setUiPart(adminModeDisplay);

        adminModeDisplayHandle = new AdminModeDisplayHandle(getChildNode(adminModeDisplay.getRoot(),
                adminModeDisplayHandle.ADMIN_MODE_DISPLAY_LABEL_ID));

        defaultStyleOfAdminModeDetails = new ArrayList<>(adminModeDisplayHandle.getStyleClass());

        loginStyleOfAdminModeDetails = new ArrayList<>(defaultStyleOfAdminModeDetails);
        loginStyleOfAdminModeDetails.remove(AdminModeDisplay.LABEL_STYLE_CLASS_NON_ADMIN);
        loginStyleOfAdminModeDetails.add(AdminModeDisplay.LABEL_STYLE_CLASS_ADMIN);
    }

    @Test
    public void display() {
        // default Admin Mode Display text
        assertEquals("Admin Mode", adminModeDisplayHandle.getText());
        assertEquals(defaultStyleOfAdminModeDetails, adminModeDisplayHandle.getStyleClass());

        // login performed
        postNow(userModeChangedEventLoginStub);
        assertEquals(loginStyleOfAdminModeDetails, adminModeDisplayHandle.getStyleClass());

        // logout performed
        postNow(userModeChangedEventLogoutStub);
        assertEquals(defaultStyleOfAdminModeDetails, adminModeDisplayHandle.getStyleClass());
    }

}
```
###### \java\seedu\ptman\ui\OutletDetailsPanelTest.java
``` java
public class OutletDetailsPanelTest extends GuiUnitTest {
    private OutletInformationChangedEvent outletInformationChangedEventStub;
    private OutletNameChangedEvent outletNameChangedEventStub;
    private AnnouncementChangedEvent announcementChangedEventStub;

    private OutletDetailsPanel outletDetailsPanel;
    private OutletDetailsPanelHandle outletDetailsPanelHandle;
    private OutletInformation outlet = new OutletInformation();

    @Before
    public void setUp() {
        outletInformationChangedEventStub = new OutletInformationChangedEvent("New Operating Hours",
                "New Outlet Contact", "New Outlet Email");
        outletNameChangedEventStub = new OutletNameChangedEvent("New Outlet Name");
        announcementChangedEventStub = new AnnouncementChangedEvent("New Announcement");
        outletDetailsPanel = new OutletDetailsPanel(outlet);

        uiPartRule.setUiPart(outletDetailsPanel);
        outletDetailsPanelHandle = new OutletDetailsPanelHandle(outletDetailsPanel.getRoot());
    }

    @Test
    public void display() {
        // Default outlet name and information
        String expectedDefaultOutletName = "DefaultOutlet";
        String expectedTrimmedDefaultOutletOperatingHours = "09:00-22:00";
        String expectedTrimmedDefaultOutletContact = "91234567";
        String expectedTrimmedDefaultOutletEmail = "DefaultOutlet@gmail.com";
        assertEquals(expectedTrimmedDefaultOutletOperatingHours,
                outletDetailsPanelHandle.getOutletOperatingHours().trim());
        assertEquals(expectedTrimmedDefaultOutletContact, outletDetailsPanelHandle.getOutletContact().trim());
        assertEquals(expectedTrimmedDefaultOutletEmail, outletDetailsPanelHandle.getOutletEmail().trim());
        assertEquals(expectedDefaultOutletName, outletDetailsPanelHandle.getOutletName());

        // Outlet Information: Operating Hours Updated
        postNow(outletInformationChangedEventStub);
        String expectedTrimmedOutletOperatingHours = "New Operating Hours";
        assertEquals(expectedTrimmedOutletOperatingHours, outletDetailsPanelHandle.getOutletOperatingHours().trim());

        // Outlet Information: Contact Updated
        postNow(outletInformationChangedEventStub);
        String expectedTrimmedOutletContact = "New Outlet Contact";
        assertEquals(expectedTrimmedOutletContact, outletDetailsPanelHandle.getOutletContact().trim());

        // Outlet Information: Email Updated
        postNow(outletInformationChangedEventStub);
        String expectedTrimmedOutletEmail = "New Outlet Email";
        assertEquals(expectedTrimmedOutletEmail, outletDetailsPanelHandle.getOutletEmail().trim());

        // Outlet Name Updated
        postNow(outletNameChangedEventStub);
        String expectedOutletName = "New Outlet Name";
        assertEquals(expectedOutletName, outletDetailsPanelHandle.getOutletName());

        // Announcement Updated
        postNow(announcementChangedEventStub);
        String expectedAnnouncement = "New Announcement";
        assertEquals(expectedAnnouncement, outletDetailsPanelHandle.getAnnouncement());

    }

}
```
###### \java\seedu\ptman\ui\testutil\GuiTestAssert.java
``` java
    /**
     * Asserts that {@code actualEntry} displays the details of {@code expectedShift}.
     */
    public static void assertEntryDisplaysShift(Shift expectedShift, Entry actualEntry, int index) {
        assertEquals(expectedShift.getDate().getLocalDate(), actualEntry.getStartDate());
        assertEquals(expectedShift.getDate().getLocalDate(), actualEntry.getEndDate());
        assertEquals(expectedShift.getStartTime().getLocalTime(), actualEntry.getStartTime());
        assertEquals(expectedShift.getEndTime().getLocalTime(), actualEntry.getEndTime());
        assertEquals("SHIFT " + index + "\nSlots left: " + expectedShift.getSlotsLeft() + "/"
                        + expectedShift.getCapacity().getCapacity(),
                actualEntry.getTitle());
    }

    /**
     * Returns the color style for {@code tagName}'s label. The tag's color is determined by looking up the color
     * in {@code EmployeeCard#TAG_COLOR_STYLES}, using an index generated by the hash code of the tag's content.
     *
     * @see EmployeeCard#getTagColor(String)
     */
    private static String getTagColor(String tagName) {
        switch (tagName) {
        case "supervisor":
            return "purple";
        case "paperwork":
            return "mint";
        case "classmates":
        case "owesMoney":
            return "salmon";
        case "colleagues":
        case "neighbours":
            return "teal";
        case "family":
        case "friend":
        case "barista":
            return "pink";
        case "friends":
        case "bartender":
            return "pale-blue";
        case "husband":
            return "yellow";
        default:
            fail(tagName + " does not have a color assigned.");
            return "";
        }
    }

    /**
     * Asserts that the tags in {@code actualCard} matches all the tags in
     * {@code expectedEmployee} with the correct color.
     * */
    private static void assertTagEquals(Employee expectedEmployee,
                                        EmployeeCardHandle actualCard) {
        List<String> expectedTags = expectedEmployee.getTags().stream().map(tag -> tag.tagName)
                .collect(Collectors.toList());
        assertEquals(expectedTags, actualCard.getTags());
        expectedTags.forEach(tag ->
                assertEquals(Arrays.asList(LABEL_DEFAULT_STYLE, getTagColor(tag)), actualCard.getTagStyleClasses(tag)));
    }

```
###### \java\seedu\ptman\ui\TimetablePanelTest.java
``` java
public class TimetablePanelTest extends GuiUnitTest {

    private static final ObservableList<Shift> TYPICAL_SHIFTS =
            getTypicalPartTimeManagerWithShiftsWithoutSunday().getShiftList();
    private static final OutletInformation TYPICAL_OUTLET =
            getTypicalPartTimeManagerWithShiftsWithoutSunday().getOutletInformation();

    private static final String TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST = "Testing1";
    private static final String TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST = "Testing2";
    private static final Email TIMETABLE_IMAGE_EMAIL_TEST = new Email("example@gmail.com");

    private EmployeePanelSelectionChangedEvent employeePanelSelectionChangedEventAliceStub;
    private EmployeePanelSelectionChangedEvent employeePanelSelectionChangedEventNullStub;
    private ExportTimetableAsImageRequestEvent exportTimetableAsImageRequestEventStub;
    private ExportTimetableAsImageAndEmailRequestEvent exportTimetableAsImageAndEmailRequestEventStub;

    private TimetablePanel timetablePanel;

    private Path testFilePathFirst;
    private Path testFilePathSecond;
    private String testFilePathNameSecond;

    @Before
    public void setUp() {
        employeePanelSelectionChangedEventAliceStub =
                new EmployeePanelSelectionChangedEvent(new EmployeeCard(ALICE, 0));
        employeePanelSelectionChangedEventNullStub = new EmployeePanelSelectionChangedEvent(null);

        exportTimetableAsImageRequestEventStub =
                new ExportTimetableAsImageRequestEvent(TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST);
        exportTimetableAsImageAndEmailRequestEventStub = new ExportTimetableAsImageAndEmailRequestEvent(
                TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST, TIMETABLE_IMAGE_EMAIL_TEST);

        testFilePathFirst = Paths.get("." + File.separator + TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST + "."
                + TIMETABLE_IMAGE_FILE_FORMAT);
        testFilePathNameSecond = "." + File.separator + TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST + "."
                + TIMETABLE_IMAGE_FILE_FORMAT;
        testFilePathSecond = Paths.get(testFilePathNameSecond);

        timetablePanel = new TimetablePanel(TYPICAL_SHIFTS, TYPICAL_OUTLET);

        uiPartRule.setUiPart(timetablePanel);
    }

    @Test
    public void display() {
        // Default timetable view: Displays week view
        assertNotNull(timetablePanel.getRoot());
        assertEquals(timetablePanel.getRoot().getSelectedPage(), timetablePanel.getRoot().getWeekPage());

        // Default timetable view: Displays all shifts
        List<Entry> defaultEntries = getTimetableEntries();
        for (int i = 0; i < TYPICAL_SHIFTS.size(); i++) {
            Shift expectedShift = TYPICAL_SHIFTS.get(i);
            Entry actualEntry = defaultEntries.get(i);
            assertEntryDisplaysShift(expectedShift, actualEntry, i + 1);
        }

        // Snapshot taken when export command called
        postNow(exportTimetableAsImageRequestEventStub);
        assertTrue(Files.exists(testFilePathFirst) && Files.isRegularFile(testFilePathFirst));

        // Snapshot taken when export and email command called: Emailed file is not saved locally
        File testFileSecond = new File(testFilePathNameSecond);
        postNow(exportTimetableAsImageAndEmailRequestEventStub);
        assertFalse(Files.exists(testFilePathSecond));
        assertFalse(testFileSecond.exists());

        // Associated shifts of employee highlighted
        postNow(employeePanelSelectionChangedEventAliceStub);
        List<Entry> entriesAfterSelectionEventAlice = getTimetableEntries();
        for (int i = 0; i < TYPICAL_SHIFTS.size(); i++) {
            Shift expectedShift = TYPICAL_SHIFTS.get(i);
            Entry actualEntry = entriesAfterSelectionEventAlice.get(i);
            assertEntryDisplaysShift(expectedShift, actualEntry, i + 1);
        }

        // Load back to default timetable view: Displays current week view
        postNow(employeePanelSelectionChangedEventNullStub);
        List<Entry> entriesAfterSelectionEventNull = getTimetableEntries();
        for (int i = 0; i < TYPICAL_SHIFTS.size(); i++) {
            Shift expectedShift = TYPICAL_SHIFTS.get(i);
            Entry actualEntry = entriesAfterSelectionEventNull.get(i);
            assertEntryDisplaysShift(expectedShift, actualEntry, i + 1);
        }
    }

    @After
    public void tearDown() {
        try {
            Files.deleteIfExists(testFilePathFirst);
            Files.deleteIfExists(testFilePathSecond);
        } catch (IOException e) {
            throw new AssertionError("Error deleting test files.");
        }
    }

    private List<Entry<?>> getEntriesForEntryType(Calendar entryType) {
        Map<LocalDate, List<Entry<?>>> entryMap = entryType.findEntries(
                LocalDate.of(2018, 3, 19).minusDays(7), LocalDate.of(2018, 3, 19).plusDays(7), ZoneId.systemDefault());
        List<Entry<?>> entryList = entryMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return entryList;
    }

    private List<Entry> getTimetableEntries() {
        List<Entry<?>> availEntries = getEntriesForEntryType(getTimetableAvail());
        List<Entry<?>> runningOutEntries = getEntriesForEntryType(getTimetableRunningOut());
        List<Entry<?>> fullEntries = getEntriesForEntryType(getTimetableFull());
        List<Entry<?>> employeeEntries = getEntriesForEntryType(getTimetableEmployee());
        List<Entry<?>> othersEntries = getEntriesForEntryType(getTimetableOthers());

        ArrayList<Entry> entries = new ArrayList<>(Stream.of(
                availEntries, runningOutEntries, fullEntries, employeeEntries, othersEntries)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        Collections.sort(entries, Comparator.comparing(Entry::getStartAsLocalDateTime));
        return entries;
    }

}
```
###### \java\systemtests\PartTimeManagerSystemTest.java
``` java
    /**
     * Asserts that the admin mode display shows the default(logout) style.
     */
    protected void assertAdminModeDisplayShowsDefaultStyle() {
        assertEquals(defaultStyleOfAdminModeDisplay, getAdminModeDisplay().getStyleClass());
    }

    /**
     * Asserts that the admin mode display shows the login style.
     */
    protected void assertAdminModeDisplayShowsLoginStyle() {
        assertEquals(loginStyleOfAdminModeDisplay, getAdminModeDisplay().getStyleClass());
    }

    /**
     * Asserts that the entire outlet panel remains the same.
     */
    protected void assertOutletDetailsPanelUnchanged() {
        OutletDetailsPanelHandle handle = getOutletDetailsPanel();
        assertFalse(handle.isOutletAnnouncementChanged());
        assertFalse(handle.isOutletInformationChanged());
        assertFalse(handle.isOutletNameChanged());
    }

```
