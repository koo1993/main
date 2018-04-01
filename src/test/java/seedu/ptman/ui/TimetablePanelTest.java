package seedu.ptman.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static seedu.ptman.testutil.EventsUtil.postNow;
import static seedu.ptman.testutil.TypicalEmployees.ALICE;
import static seedu.ptman.testutil.TypicalShifts.getTypicalPartTimeManagerWithShiftsWithoutSunday;
import static seedu.ptman.ui.TimetablePanel.TIMETABLE_IMAGE_FILE_FORMAT;
import static seedu.ptman.ui.TimetablePanel.getTimetableAvail;
import static seedu.ptman.ui.TimetablePanel.getTimetableEmployee;
import static seedu.ptman.ui.TimetablePanel.getTimetableFull;
import static seedu.ptman.ui.TimetablePanel.getTimetableOthers;
import static seedu.ptman.ui.TimetablePanel.getTimetableRunningOut;
import static seedu.ptman.ui.testutil.GuiTestAssert.assertEntryDisplaysShift;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

import javafx.collections.ObservableList;
import seedu.ptman.commons.events.ui.EmployeePanelSelectionChangedEvent;
import seedu.ptman.commons.events.ui.ExportTimetableAsImageAndEmailRequestEvent;
import seedu.ptman.commons.events.ui.ExportTimetableAsImageRequestEvent;
import seedu.ptman.model.employee.Email;
import seedu.ptman.model.outlet.OutletInformation;
import seedu.ptman.model.outlet.Shift;


//@@author hzxcaryn
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
                LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), ZoneId.systemDefault());
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
