package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.ui.StatusBarFooter.NUM_EMPLOYEES_STATUS;
import static seedu.ptman.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static seedu.ptman.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.ptman.ui.testutil.GuiTestAssert.assertListMatching;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.EmployeeListPanelHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.OutletDetailsPanelHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import guitests.guihandles.TimetableViewHandle;
import seedu.ptman.TestApp;
import seedu.ptman.commons.core.EventsCenter;
import seedu.ptman.commons.core.index.Index;
import seedu.ptman.logic.commands.ClearCommand;
import seedu.ptman.logic.commands.FindCommand;
import seedu.ptman.logic.commands.ListCommand;
import seedu.ptman.logic.commands.SelectCommand;
import seedu.ptman.model.Model;
import seedu.ptman.model.PartTimeManager;
import seedu.ptman.testutil.TypicalEmployees;
import seedu.ptman.ui.BrowserPanel;
import seedu.ptman.ui.CommandBox;
import seedu.ptman.ui.ResultDisplay;

/**
 * A system test class for PartTimeManager, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class PartTimeManagerSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    private List<String> defaultStyleOfResultDisplay;
    private List<String> errorStyleOfResultDisplay;

    private MainWindowHandle mainWindowHandle;
    private TestApp testApp;
    private SystemTestSetupHelper setupHelper;

    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();

        defaultStyleOfResultDisplay = mainWindowHandle.getResultDisplay().getStyleClass();
        errorStyleOfResultDisplay = mainWindowHandle.getResultDisplay().getStyleClass();
        errorStyleOfResultDisplay.add(ResultDisplay.ERROR_STYLE_CLASS);
        assertApplicationStartingStateIsCorrect();
    }

    @After
    public void tearDown() throws Exception {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected PartTimeManager getInitialData() {
        return TypicalEmployees.getTypicalPartTimeManager();
    }

    /**
     * Returns the directory of the data file.
     */
    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }
    public CommandBoxHandle getCommandBoxOutput() {
        return mainWindowHandle.getCommandBoxOutput();
    }

    public EmployeeListPanelHandle getEmployeeListPanel() {
        return mainWindowHandle.getEmployeeListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public OutletDetailsPanelHandle getOutletDetailsPanel() {
        return mainWindowHandle.getOutletDetailsPanel();
    }

    public TimetableViewHandle getTimetableView() {
        return mainWindowHandle.getTimetableView();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

    }

    /**
     * Displays all employees in PTMan.
     */
    protected void showAllEmployees() {
        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getPartTimeManager().getEmployeeList().size(),
                getModel().getFilteredEmployeeList().size());
    }

    /**
     * Displays all employees with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showEmployeesWithName(String keyword) {
        executeCommand(FindCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredEmployeeList().size()
                < getModel().getPartTimeManager().getEmployeeList().size());
    }

    /**
     * Selects the employee at {@code index} of the displayed list.
     */
    protected void selectEmployee(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getEmployeeListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all employees in PTMan.
     */
    protected void deleteAllEmployees() {
        executeCommand(ClearCommand.COMMAND_WORD + " ");
        assertEquals(0, getModel().getPartTimeManager().getEmployeeList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the model and storage contains the same employee objects as {@code expectedModel}
     * and the employee list panel displays the employees in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
            Model expectedModel) {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(expectedModel, getModel());
        assertEquals(expectedModel.getPartTimeManager(), testApp.readStoragePartTimeManager());
        assertListMatching(getEmployeeListPanel(), expectedModel.getFilteredEmployeeList());
    }

    /**
     * Calls {@code OutletDetailsPanelHandle}, {@code EmployeeListPanelHandle} and {@code StatusBarFooterHandle} to
     * remember their current state.
     */
    private void rememberStates() {
        OutletDetailsPanelHandle outletDetailsPanelHandle = getOutletDetailsPanel();
        outletDetailsPanelHandle.rememberOutletAnnouncement();
        outletDetailsPanelHandle.rememberOutletInformation();
        outletDetailsPanelHandle.rememberOutletName();
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        statusBarFooterHandle.rememberNumEmployees();
        getEmployeeListPanel().rememberSelectedEmployeeCard();
    }

    /**
     * Asserts that the previously selected card is now deselected and the browser's url remains displaying the details
     * of the previously selected employee.
     * @see BrowserPanelHandle#isUrlChanged()
     */
    protected void assertSelectedCardDeselected() {
        assertFalse(getEmployeeListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's url is changed to display the details of the employee in the employee list panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see EmployeeListPanelHandle#isSelectedEmployeeCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        String selectedCardName = getEmployeeListPanel().getHandleToSelectedCard().getName();
        URL expectedUrl;
        try {
            expectedUrl = new URL(BrowserPanel.SEARCH_PAGE_URL + selectedCardName.replaceAll(" ", "%20"));
        } catch (MalformedURLException mue) {
            throw new AssertionError("URL expected to be valid.");
        }

        assertEquals(expectedSelectedCardIndex.getZeroBased(), getEmployeeListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the browser's url and the selected card in the employee list panel remain unchanged.
     * @see BrowserPanelHandle#isUrlChanged()
     * @see EmployeeListPanelHandle#isSelectedEmployeeCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getEmployeeListPanel().isSelectedEmployeeCardChanged());
    }

    /**
     * Asserts that the command box and result display shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBoxOutput().getStyleClass());
        assertEquals(defaultStyleOfResultDisplay, getResultDisplay().getStyleClass());
    }

    /**
     * Asserts that the command box and result display shows the error style.
     */
    protected void assertCommandBoxAndResultDisplayShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBoxOutput().getStyleClass());
        assertEquals(errorStyleOfResultDisplay, getResultDisplay().getStyleClass());
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

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isNumEmployeesChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the save location in the status bar was unchanged, while sync status
     * is changed to the timing of {@code ClockRule#getInjectedClock()}, and num employees updates to
     * the current num of employees.
     */
    protected void assertStatusBarChangedExceptSaveLocation() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        int currNumEmployees = testApp.getModel().getPartTimeManager().getEmployeeList().size();
        String expectedNumEmployees = String.format(NUM_EMPLOYEES_STATUS, currNumEmployees);

        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertEquals(expectedNumEmployees, handle.getNumEmployees());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        try {
            assertEquals("", getCommandBox().getInput());
            assertEquals("Welcome to PTMan. Type a command in the search bar above to get started. "
                    + "If you need somewhere to start, search “help” to view the user guide.",
                    getResultDisplay().getText());
            assertListMatching(getEmployeeListPanel(), getModel().getFilteredEmployeeList());
            assertEquals("./" + testApp.getStorageSaveLocation(), getStatusBarFooter().getSaveLocation());
            assertEquals(SYNC_STATUS_INITIAL, getStatusBarFooter().getSyncStatus());
            assertEquals(String.format(NUM_EMPLOYEES_STATUS,
                    getModel().getPartTimeManager().getEmployeeList().size()), getStatusBarFooter().getNumEmployees());
        } catch (Exception e) {
            throw new AssertionError("Starting state is wrong.", e);
        }
    }

    /**
     * Returns a defensive copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
