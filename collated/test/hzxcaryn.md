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
###### \java\guitests\guihandles\TimetablePanelHandle.java
``` java
/**
 * A handler for the {@code TimetableView} of the UI
 */
public class TimetablePanelHandle extends NodeHandle<Node> {

    public static final String TIMETABLE_PANEL_PLACEHOLDER_ID = "#timetablePanelPlaceholder";

    private static final String TIMETABLE_VIEW_ID = "#timetableView";
    private static final String PREV_BUTTON_ID = "#prevButton";
    private static final String NEXT_BUTTON_ID = "#nextButton";
    private static final String MONTH_DISPLAY_ID = "#monthDisplay";

    private final CalendarView timetableViewNode;
    private final Button prevButtonNode;
    private final Button nextButtonNode;
    private final Label monthDisplayNode;

    public TimetablePanelHandle(Node rootNode) {
        super(rootNode);

        this.timetableViewNode = getChildNode(TIMETABLE_VIEW_ID);
        this.prevButtonNode = getChildNode(PREV_BUTTON_ID);
        this.nextButtonNode = getChildNode(NEXT_BUTTON_ID);
        this.monthDisplayNode = getChildNode(MONTH_DISPLAY_ID);
    }

    /**
     * @return current date timetable is using to display the week
     */
    public LocalDate getTimetableDate() {
        return timetableViewNode.getDate();
    }

    /**
     * @return selected page type that timetable is displaying
     */
    public PageBase getSelectedPage() {
        return timetableViewNode.getSelectedPage();
    }

    /**
     * @return a week page type
     */
    public PageBase getWeekPage() {
        return timetableViewNode.getWeekPage();
    }

    /**
     * @return month and year displayed in {@code monthDisplayNode}
     */
    public String getDisplayedMonthYear() {
        return monthDisplayNode.getText();
    }

    /**
     * Navigate to the prev timetable week by clicking the next button
     */
    public void navigateToPrevUsingButton() {
        guiRobot.clickOn(prevButtonNode);
    }

    /**
     * Navigate to the next timetable week by clicking the next button
     */
    public void navigateToNextUsingButton() {
        guiRobot.clickOn(nextButtonNode);
    }

    /**
     * Check for all the entries from all entry types in the timetable view
     * and returns a list of all the entries in sorted order
     * @return sorted list of all entries in timetable view
     */
    public List<Entry> getTimetableEntries() {
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

    /**
     * For the given {@code entryType}, check for all entries in the timetable view
     * of that entry type and return them in a list
     * @param entryType
     * @return list of all entries of the {@code entryType}
     */
    public List<Entry<?>> getEntriesForEntryType(Calendar entryType) {
        Map<LocalDate, List<Entry<?>>> entryMap = entryType.findEntries(
                LocalDate.of(2018, 3, 19).minusDays(7),
                LocalDate.of(2018, 3, 19).plusDays(7), ZoneId.systemDefault());
        List<Entry<?>> entryList = entryMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return entryList;
    }
}
```
###### \java\seedu\ptman\commons\util\DateUtilTest.java
``` java
    @Test
    public void getThursdayOfDate_validDate_returnsThursdayDate() {
        // Sunday 8th April 2018 returns Thursday 5th April 2018
        assertEquals(DateUtil.getThursdayOfDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 5));

        // Monday 9th April 2018 returns Thursday 12th April 2018
        assertEquals(DateUtil.getThursdayOfDate(LocalDate.of(2018, 4, 9)), LocalDate.of(2018, 4, 12));
    }

    @Test
    public void getThursdayOfDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getThursdayOfDate(null);
    }

    @Test
    public void getNextWeekDate_validDate_returnsNextWeekDate() {
        // Sunday 8th April 2018 returns 15th April 2018
        assertEquals(DateUtil.getNextWeekDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 15));
    }

    @Test
    public void getNextWeekDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getNextWeekDate(null);
    }

    @Test
    public void getPrevWeekDate_validDate_returnsNextWeekDate() {
        // Sunday 8th April 2018 returns 1st April 2018
        assertEquals(DateUtil.getPrevWeekDate(LocalDate.of(2018, 4, 8)), LocalDate.of(2018, 4, 1));
    }

    @Test
    public void getPrevWeekDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getPrevWeekDate(null);
    }

    @Test
    public void getMonthYearFromDate_validDate_returnsMonthYear() {
        // Sunday 8th April 2018 returns APRIL 2018
        assertEquals(DateUtil.getMonthYearFromDate(LocalDate.of(2018, 4, 8)), "APRIL 2018");
    }

    @Test
    public void getMonthYearFromDate_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        DateUtil.getMonthYearFromDate(null);
    }

}
```
###### \java\seedu\ptman\logic\commands\DeselectCommandTest.java
``` java
public class DeselectCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_main_success() {
        CommandResult result = new DeselectCommand().execute();
        assertEquals(MESSAGE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof JumpToListRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
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
###### \java\seedu\ptman\logic\commands\ViewShiftCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ViewShiftCommandTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalPartTimeManagerWithShifts(),
                new UserPrefs(), new OutletInformation());
        model.setFilteredShiftListToWeek(SHIFT_MONDAY_AM.getDate().getLocalDate());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredShiftList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexShiftWithEmployees_success() {
        assertExecutionSuccess(INDEX_SEVENTH_SHIFT,
                SHIFT_THURSDAY_PM.getEmployeeList().sorted()); //8th Shift has employees
    }

    @Test
    public void execute_validIndexShiftWithoutEmployees_success() {
        assertExecutionSuccess(INDEX_SECOND_SHIFT, SHIFT_MONDAY_PM.getEmployeeList());
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        Index outOfBoundsIndex = INDEX_OUT_OF_BOUNDS_SHIFT;
        // ensures that outOfBoundIndex is still in bounds of ptman shift list
        assertTrue(outOfBoundsIndex.getZeroBased() > model.getPartTimeManager().getShiftList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_SHIFT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ViewShiftCommand viewShiftFirstCommand = new ViewShiftCommand(INDEX_FIRST_SHIFT);
        ViewShiftCommand viewShiftSecondCommand = new ViewShiftCommand(INDEX_SECOND_SHIFT);

        // same object -> returns true
        assertTrue(viewShiftFirstCommand.equals(viewShiftFirstCommand));

        // same values -> returns true
        ViewShiftCommand viewShiftFirstCommandCopy = new ViewShiftCommand(INDEX_FIRST_SHIFT);
        assertTrue(viewShiftFirstCommand.equals(viewShiftFirstCommandCopy));

        // different types -> returns false
        assertFalse(viewShiftFirstCommand.equals(1));

        // null -> returns false
        assertFalse(viewShiftFirstCommand.equals(null));

        // different employee -> returns false
        assertFalse(viewShiftFirstCommand.equals(viewShiftSecondCommand));
    }

    /**
     * Executes a {@code ViewShiftCommand} with the given {@code index}, and checks that {@code filteredEmployeeList}
     * is updated with the correct employees.
     */
    private void assertExecutionSuccess(Index index, ObservableList<Employee> expectedEmployeeList) {
        ViewShiftCommand viewShiftCommand = prepareCommand(index);

        try {
            CommandResult commandResult = viewShiftCommand.execute();
            assertEquals(String.format(ViewShiftCommand.MESSAGE_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        // checks that filteredEmployeeList is updated with the correct employees.
        assertEquals(expectedEmployeeList, model.getFilteredEmployeeList());
    }

    /**
     * Executes a {@code ViewShiftCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        ViewShiftCommand viewShiftCommand = prepareCommand(index);

        try {
            viewShiftCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
        }
    }

    /**
     * Returns a {@code ViewShiftCommand} with parameters {@code index}.
     */
    private ViewShiftCommand prepareCommand(Index index) {
        ViewShiftCommand viewShiftCommand = new ViewShiftCommand(index);
        viewShiftCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return viewShiftCommand;
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
    public void parseCommand_defaultview() throws Exception {
        assertTrue(parser.parseCommand(DeselectCommand.COMMAND_WORD) instanceof DeselectCommand);
        assertTrue(parser.parseCommand(DeselectCommand.COMMAND_WORD + " 3") instanceof DeselectCommand);
    }

    @Test
    public void parseCommand_viewshift() throws Exception {
        ViewShiftCommand command = (ViewShiftCommand) parser.parseCommand(
                ViewShiftCommand.COMMAND_WORD + " " + INDEX_FIRST_SHIFT.getOneBased());
        assertEquals(new ViewShiftCommand(INDEX_FIRST_SHIFT), command);
    }

```
###### \java\seedu\ptman\logic\parser\ViewShiftCommandParserTest.java
``` java
public class ViewShiftCommandParserTest {

    private ViewShiftCommandParser parser = new ViewShiftCommandParser();

    @Test
    public void parse_validArgs_returnsSelectCommand() {
        assertParseSuccess(parser, "1", new ViewShiftCommand(INDEX_FIRST_SHIFT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewShiftCommand.MESSAGE_USAGE));
    }
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
                        + expectedShift.getCapacity().getCapacity(), actualEntry.getTitle());
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
        case "chef":
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
        case "cashier":
            return "yellow";
        case "alfresco":
            return "turquoise";
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

    private static final PartTimeManager TYPICAL_PTMAN =
            getTypicalPartTimeManagerWithShifts();
    private static final ObservableList<Shift> TYPICAL_SHIFTS = TYPICAL_PTMAN.getShiftList();
    private static final OutletInformation TYPICAL_OUTLET = TYPICAL_PTMAN.getOutletInformation();

    private static final String TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST = "Testing1";
    private static final String TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST = "Testing2";
    private static final Email TIMETABLE_IMAGE_EMAIL_TEST = new Email("example@gmail.com");

    private EmployeePanelSelectionChangedEvent employeePanelSelectionChangedEventAliceStub;
    private EmployeePanelSelectionChangedEvent employeePanelSelectionChangedEventNullStub;
    private ExportTimetableAsImageRequestEvent exportTimetableAsImageRequestEventStub;
    private ExportTimetableAsImageAndEmailRequestEvent exportTimetableAsImageAndEmailRequestEventStub;
    private TimetableWeekChangeRequestEvent timetableWeekChangeRequestEventPrevStub;
    private TimetableWeekChangeRequestEvent timetableWeekChangeRequestEventNextStub;
    private TimetableWeekChangeRequestEvent timetableWeekChangeRequestEventCurrStub;

    private TimetablePanel timetablePanel;
    private TimetablePanelHandle timetablePanelHandle;

    private Path testFilePathFirst;
    private Path testFilePathSecond;
    private String testFilePathNameSecond;
    private LocalDate startingDate;

    private Logic logic;

    @Before
    public void setUp() throws DuplicateShiftException {
        // Event stubs
        employeePanelSelectionChangedEventAliceStub =
                new EmployeePanelSelectionChangedEvent(new EmployeeCard(ALICE, 0));
        employeePanelSelectionChangedEventNullStub = new EmployeePanelSelectionChangedEvent(null);

        exportTimetableAsImageRequestEventStub =
                new ExportTimetableAsImageRequestEvent(TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST);
        exportTimetableAsImageAndEmailRequestEventStub = new ExportTimetableAsImageAndEmailRequestEvent(
                TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST, TIMETABLE_IMAGE_EMAIL_TEST);

        timetableWeekChangeRequestEventPrevStub = new TimetableWeekChangeRequestEvent(WeekChangeRequest.PREVIOUS);
        timetableWeekChangeRequestEventNextStub = new TimetableWeekChangeRequestEvent(WeekChangeRequest.NEXT);
        timetableWeekChangeRequestEventCurrStub = new TimetableWeekChangeRequestEvent(WeekChangeRequest.CURRENT);

        testFilePathFirst = Paths.get("." + File.separator + TIMETABLE_IMAGE_FILE_NAME_FIRST_TEST + "."
                + TIMETABLE_IMAGE_FILE_FORMAT);
        testFilePathNameSecond = "." + File.separator + TIMETABLE_IMAGE_FILE_NAME_SECOND_TEST + "."
                + TIMETABLE_IMAGE_FILE_FORMAT;
        testFilePathSecond = Paths.get(testFilePathNameSecond);

        Model model = new ModelManager(TYPICAL_PTMAN, new UserPrefs(), TYPICAL_OUTLET);
        logic = new LogicManager(model);
        logic.setFilteredShiftListToCustomWeek(SHIFT_MONDAY_AM.getDate().getLocalDate());
        timetablePanel = new TimetablePanel(logic);
        timetablePanelHandle = new TimetablePanelHandle(timetablePanel.getRoot());
        uiPartRule.setUiPart(timetablePanel);

        startingDate = timetablePanelHandle.getTimetableDate();
    }

    @Test
    public void display() {
        // Default timetable view: Displays current week view
        assertNotNull(timetablePanel.getRoot());
        assertEquals(timetablePanelHandle.getSelectedPage(), timetablePanelHandle.getWeekPage());
        assertEquals(startingDate, timetablePanelHandle.getTimetableDate());

        // Default timetable view: Displays all shifts
        List<Entry> defaultEntries = timetablePanelHandle.getTimetableEntries();
        for (int i = 0; i < TYPICAL_SHIFTS.size(); i++) {
            Shift expectedShift = TYPICAL_SHIFTS.get(i);
            Entry actualEntry = defaultEntries.get(i);
            assertEntryDisplaysShift(expectedShift, actualEntry, i + 1);
        }

        // Associated shifts of employee highlighted
        postNow(employeePanelSelectionChangedEventAliceStub);
        List<Entry> entriesAfterSelectionEventAlice = timetablePanelHandle.getTimetableEntries();
        for (int i = 0; i < TYPICAL_SHIFTS.size(); i++) {
            Shift expectedShift = TYPICAL_SHIFTS.get(i);
            Entry actualEntry = entriesAfterSelectionEventAlice.get(i);
            assertEntryDisplaysShift(expectedShift, actualEntry, i + 1);
        }

        // Load back to default timetable view: Displays current week view
        postNow(employeePanelSelectionChangedEventNullStub);
        List<Entry> entriesAfterSelectionEventNull = timetablePanelHandle.getTimetableEntries();
        for (int i = 0; i < logic.getFilteredShiftList().size(); i++) {
            Shift expectedShift = TYPICAL_SHIFTS.get(i);
            Entry actualEntry = entriesAfterSelectionEventNull.get(i);
            assertEntryDisplaysShift(expectedShift, actualEntry, i + 1);
        }
    }

    @Test
    public void timetablePanel_handleTimetableWeekChangeRequestEvent() {
        postNow(timetableWeekChangeRequestEventNextStub);
        assertEquals(getNextWeekDate(startingDate), timetablePanelHandle.getTimetableDate());

        postNow(timetableWeekChangeRequestEventPrevStub);
        assertEquals(startingDate, timetablePanelHandle.getTimetableDate());

        postNow(timetableWeekChangeRequestEventPrevStub);
        assertEquals(getPrevWeekDate(startingDate), timetablePanelHandle.getTimetableDate());

        postNow(timetableWeekChangeRequestEventCurrStub);
        assertEquals(LocalDate.now(), timetablePanelHandle.getTimetableDate());
    }

    @Test
    public void handleTimetableWeekChangeRequestEvent_usingButtons() {
        timetablePanelHandle.navigateToNextUsingButton();
        assertEquals(getNextWeekDate(startingDate), timetablePanelHandle.getTimetableDate());

        timetablePanelHandle.navigateToPrevUsingButton();
        assertEquals(startingDate, timetablePanelHandle.getTimetableDate());

        timetablePanelHandle.navigateToPrevUsingButton();
        assertEquals(getPrevWeekDate(startingDate), timetablePanelHandle.getTimetableDate());
    }

    @Test
    public void timetablePanel_monthDisplay() {
        String expectedMonthDisplay = startingDate.getMonth().name() + " " + startingDate.getYear();
        assertEquals(expectedMonthDisplay, timetablePanelHandle.getDisplayedMonthYear());
    }

    @Test
    public void timetablePanel_handleExportTimetableAsImageRequestEvent() {
        // Snapshot taken when export command called
        postNow(exportTimetableAsImageRequestEventStub);
        assertTrue(Files.exists(testFilePathFirst) && Files.isRegularFile(testFilePathFirst));
    }

    @Test
    public void timetablePanel_handleExportTimetableAsImageAndEmailRequestEvent() {
        // Snapshot taken when export and email command called: Emailed file is not saved locally
        File testFileSecond = new File(testFilePathNameSecond);
        postNow(exportTimetableAsImageAndEmailRequestEventStub);
        assertFalse(Files.exists(testFilePathSecond));
        assertFalse(testFileSecond.exists());
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
